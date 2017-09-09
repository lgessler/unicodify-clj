(ns unicodify-clj.core
  (:require [clojure.java.io :as io]
            [pl.danieljanus.tagsoup :as ts]
            [hiccup.core :as hiccup]
            [clojure.string :as strng]
            [unicodify-clj.transcode :as xc]))

(def INPUT_PATH "/Users/lukegessler/play/malhar/www.hindi-urdu-malhar.org/")
(def OUTPUT_PATH "/tmp/malhar")

(defn- transcode
  [elt]
  (cond (coll? elt) (into [(first elt) (second elt)]
                          (->> elt rest rest (map transcode)))
        (string? elt) (xc/xdvng-to-unicode elt)
        :else elt))

(defn- is-xdvng-node?
  [elt]
  (and (coll? elt)
       (= (first elt) :font)
       (map? (second elt))
       (:face (second elt))
       (= (strng/lower-case (:face (second elt))) "xdvng")))

(defn- unicodify
  "Takes in hiccup HTML"
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
    (when (and (not (.isDirectory f))
               (= ext "html"))
      (spit (str OUTPUT_PATH "/" fname)
            (-> f
                .toString
                ts/parse
                unicodify
                hiccup/html)))))

(defn- walk
  [path]
  (doall
   (map #(do
           (process-file %)
           (println "Processed file" (.toString %)))
        (file-seq (io/file path))))
  :ok)

(defn -main
  []
  (walk INPUT_PATH))

