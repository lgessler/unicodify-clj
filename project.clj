(defproject unicodify-clj "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [hickory "0.7.1"]
                 [hiccup "1.0.5"]
                 [clj-tagsoup/clj-tagsoup "0.3.0"]
                 ]
  :main unicodify-clj.core
  :jvm-opts ["-Xss515m"])
