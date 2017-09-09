(ns unicodify-clj.transcode)

(def xdvng-chars (list

"k" "K:" "g:" "G:"
"V" "K"  "g"  "G"

"c:" "C" "j:" "J:"
"c"  "C" "j"  "J"

"X" "Y" "R" "_" "Z" "`" "N:"
"X" "Y" "R" "`"  "N"

"t:" "T:" "d" "D:" "n:"
"t"  "T"  "d" "D"  "n"

"p:" "P" "f" "b:" "B:" "m:"
"p"  "P" "f" "b"  "B"  "m"

"y:" "r" "l:" "v:"
"y"  "r" "l"  "v"

"s:" "S:" "\\:" "h" "x:" "w"
"s"  "S"  "\\"  "h" "x"

"!" "*:" "¹"  "Â:" "É" "Ã:" "²" "-" "#" "¤" "½" "$" "Ä" "¶:" "Æ"
"¢"  "¾"  ")" "Ò"  "Ó" "Á:" "À"

"A:ð" "A:ò" "A:" "A" "E" "I" "u" "U" "Oð" "O" "?"

"Ï" "Î" "|" "a" "i" "Ù" "Û" "Ø" "Ú" "Ü" "Ý" "Þ"

":ð" "ð" "ñ" ":ò" "ò" "ó" "ö" "ú" "H" "á" "à"

"." "Q:" "ø" "z:"
))

(def unicode-chars (list
"क" "ख" "ग" "घ"
"क्" "ख्" "ग्" "घ्"

"च" "छ" "ज" "झ"
"च्" "छ्" "ज्" "झ्"

"ट" "ठ" "ड" "ड़" "ढ" "ढ़" "ण"
"ट्" "ठ्" "ड्" "ढ़्" "ण्"

"त" "थ" "द" "ध" "न"
"त्" "थ्" "द्" "ध्" "न्"

"प" "फ" "फ" "ब" "भ" "म"
"प्" "फ्" "फ्" "ब्" "भ्" "म्"

"य" "र" "ल" "व"
"य्" "र्" "ल्" "व्"

"स" "श" "ष" "ह" "क्ष" "ज्ञ"
"स्" "श्" "ष्" "ह्" "क्ष्"

"ॐ" "त्र" "द्ध" "श्र" "ह्म" "श्व" "ड्ड" "रू" "क्र" "ङ्ग" "द्य" "ज्र" "ष्ट" "त्त" "हृ"
"ङ्क" "द्व" "प्र" "्र" "्र" "श्च" "न्न"

"ओ" "औ" "आ" "अ" "इ" "ई" "उ" "ऊ" "ऐ" "ए" "ऋ"

"्" "्" "ऽ" "ा" "ी" "ु" "ु" "ु" "ु" "ू" "ू" "ू"

"ो" "े" "े" "ौ" "ै" "ै" "ं" "ँ" ":" "ृ" "ृ"

"।" "ख़" "ं" "ज़"
))

(def table (map vector xdvng-chars unicode-chars))

(defn- replace-normal-cases [s]
  (reduce (fn [acc [k v]] (clojure.string/replace acc k v))
          s
          table))

(defn transpose-i
  [sl]
  (cond (empty? sl) '()
        (= (first sl) \e)
        (if (nil? (second sl))
          (cons \ि '())
          (cons (second sl) (cons \ि (transpose-i (rest (rest sl))))))
        :else (cons (first sl) (transpose-i (rest sl)))))

(defn- slide-i
  [s]
  (reverse
   (reduce (fn [acc c3]
             (let [[c2 c1] (take 2 acc)]
               (if (and (= c1 \ि)
                        (= c2 \्))
                 (conj (drop 2 acc) c2 c3 c1)
                 (conj acc c3))))
           '()
           s)))

(defn- fix-chhoti-i-ki-matra
  [s]
  (apply str (slide-i (transpose-i (seq s)))))


(defn xdvng-to-unicode
  [s]
  (-> s
      replace-normal-cases
      fix-chhoti-i-ki-matra))
