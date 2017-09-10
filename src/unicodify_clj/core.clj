(ns unicodify-clj.core
  (:require [clojure.java.io :as io]
            [pl.danieljanus.tagsoup :as ts]
            [hiccup.core :as hiccup]
            [clojure.string :as strng]
            [unicodify-clj.transcode :as xc]))

(def INPUT_PATH "/Users/lukegessler/play/malhar/www.hindi-urdu-malhar.org/")
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

(defn- unicodify
  "Takes in a hiccup representation of the document and recursively finds
   text nodes that need to be transcoded"
  [elt]
  (cond (vector? elt) (apply vector (map (fn [elt]
                                           (if (is-xdvng-node? elt)
                                             (transcode elt)
                                             (unicodify elt)))
                                         elt))
        :else elt))

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
              (-> f
                  .toString
                  ts/parse
                  unicodify
                  hiccup/html))
        (io/copy f (io/file (str OUTPUT_PATH "/" fname)))))))

(defn- walk
  [path]
  (doall
   (map #(do
           (println "Processing file" (.toString %))
           (process-file %))
        (file-seq (io/file path))))
  :ok)

(defn -main
  []
  (walk INPUT_PATH))

