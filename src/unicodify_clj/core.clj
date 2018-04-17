(ns unicodify-clj.core
  (:require [clojure.java.io :as io]
            [hickory.core :as hickory]
            [pl.danieljanus.tagsoup :as ts]
            [hiccup.core :as hiccup]
            [clojure.string :as strng]
            [unicodify-clj.transcode :as xc]))

(def INPUT_PATH "/home/luke/Sync/playground/malhar/www.hindi-urdu-malhar.org/")
(def TEST_PATH "/home/luke/Sync/playground/malhar/test/")
(def OUTPUT_PATH "/tmp/malhar")

(defn- transcode
  "Recursively called on xdvng nodes and their child DOM elements"
  [elt]
  (cond (coll? elt)
        (into [;; keep the elt
               (first elt)
               ;; remove {:face "xdvng"} if present as an attr
               (if (and (:face (second elt))
                        (= (strng/lower-case (:face (second elt))) "xdvng"))
                 (dissoc (second elt) :face)
                 (second elt))]
              (->> elt rest rest (map transcode))) ;; call recursively on body
        (string? elt)
        (xc/xdvng-to-unicode elt) ;; text node that needs to be transcoded
        :else
        elt))

(defn- is-xdvng-node?
  "Returns true if the element looks like [:font {:face \"xdvng\"} ...]"
  [elt]
  (and (coll? elt)
       (= (first elt) :font)
       (map? (second elt))
       (:face (second elt))
       (= (strng/lower-case (:face (second elt))) "xdvng")))

(defn- fix-bad-idiosyncrasies
  [elt]
  (cond (and (vector? elt)
             (#{:c} (first elt))) (assoc elt 0 :center)
        :else elt))

(defn- unicodify
  "Takes in a hiccup representation of the document and recursively finds
   text nodes that need to be transcoded"
  [elt]
  (cond (vector? elt) (apply vector (map (fn [elt]
                                           (let [elt (fix-bad-idiosyncrasies elt)]
                                             (if (is-xdvng-node? elt)
                                               (transcode elt)
                                               (unicodify elt))))
                                         elt))
        :else elt))

(defn- DIRTY-strip-out-p-tags
  "Sometimes a file contains a <p> tag that is not closed (e.g.
  mirdardinaa^.html). This causes the parser to sloppily infer
  where the </p> should go, which causes transcoding issues. This
  is an attempt to get rid of these."
  [s]
  (if (and (clojure.string/includes? s "<p>")
           (not (clojure.string/includes? s "</p>")))
    ;(clojure.string/replace s #"(<p>\s*)+(\r\n|\r|\n)" "\n")
    (clojure.string/replace s #"<p>" "<br/>")
    s))

(defn- DIRTY-fix-quotes
  [s]
  (clojure.string/replace s #"(?i)face=xdvng" "face=\"Xdvng\""))

(defn- transcode-file-string
  [s]
  (-> s
      DIRTY-strip-out-p-tags
      DIRTY-fix-quotes
      hickory/parse
      hickory/as-hiccup
      first
      unicodify
      hiccup/html))

(defn process-file
  [f]
  (let [fname (last (strng/split (.toString f) #"/"))
        ext (->>
             (strng/split fname #"\.")
             (last)
             (strng/lower-case))]
    (when (not (.isDirectory f))
      (if (= ext "html")
        (spit (str OUTPUT_PATH "/" fname)
              (transcode-file-string (slurp f)))
        (io/copy f (io/file (str OUTPUT_PATH "/" fname)))))))

(defn- walk
  [path]

  (doseq [file (file-seq (io/file path))]
    (println "Processing file" (.toString file))
    (process-file file))
  :ok)

(defn -main
  []
  #_(walk TEST_PATH)
  (walk INPUT_PATH))
