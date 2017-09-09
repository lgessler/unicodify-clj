(ns unicodify-clj.transcode)

(def xdvng-chars (list
"k" "K:" "g:" "G:"
"V" "K"  "g"  "G"
"Q:" "Q" "z:" "z" "q" "^:" "^" 

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
"¢"  "¾"  ")" "Ò"  "Ó" "Á:" "À" "¬" "("

"A:ð" "A:ò" "A:" "A" "E" "I" "u" "U" "Oð" "O" "?"

"Ï" "Î" "|" "a" "i" "Ù" "Û" "Ø" "Ú" "Ü" "Ý" "Þ"

":ð" "ð" "ñ" ":ò" "ò" "ó" "ö" "ù" "ú" "H" "á" "à"

"." "ø" "û" "ô" "0"
))

(def unicode-chars (list
"क" "ख" "ग" "घ"
"क्" "ख्" "ग्" "घ्"
"ख़" "ख़्" "ज़" "ज़्" "क़" "ग़" "ग़्" 

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
"ङ्क" "द्व" "प्र" "्र" "्र" "श्च" "न्न" "ट्ट" "फ़्र"

"ओ" "औ" "आ" "अ" "इ" "ई" "उ" "ऊ" "ऐ" "ए" "ऋ"

"्" "्" "ऽ" "ा" "ी" "ु" "ु" "ु" "ु" "ू" "ू" "ू"

"ो" "े" "े" "ौ" "ै" "ै" "ं" "ं" "ँ" ":" "ृ" "ृ"

"।" "ं" "ँ" "ॅ" "॰"
))

(def table (map vector xdvng-chars unicode-chars))

(defn- replace-normal-cases [s]
  "Use simple string replacement to take care of the majority of cases.
   Note that the table is ordered so that the replacements are done correctly."
  (reduce (fn [acc [k v]] (clojure.string/replace acc k v))
          s
          table))

(defn transpose-i
  "Swaps the position of the first chhoti i and the character next to it"
  [sl]
  (cond (empty? sl) '()
        (= (first sl) \e)
        (if (nil? (second sl))
          (cons \ि '())
          (cons (second sl) (cons \ि (transpose-i (rest (rest sl))))))
        :else (cons (first sl) (transpose-i (rest sl)))))

(defn- slide-i
  "Shifts the chhoti i until it's totally past the associated 'chunk'"
  [s]
  (reverse
   (reduce (fn [acc c3]
             (let [[c2 c1] (take 2 acc)]
               (cond (or (and (= c1 \ि) (= c2 \्))
                         (and (= c1 \ि) (= c2 \़))) (conj (drop 2 acc) c2 c3 c1)
                     (or (and (= c2 \ि) (= c1 \्))
                         (and (= c2 \ि) (= c1 \ý))) (conj (drop 2 acc) c1 c3 c2)
                     :else (conj acc c3))))
           '()
           s)))

(defn- fix-chhoti-i-ki-matra
  [s]
  (apply str (slide-i (transpose-i (seq s)))))

(defn fix-ra
  "Fixes the position of half ras"
  [s]
  (let [matras (set "ािीुूृेैोौंःँॅ")
        shifter-matra? #(contains? matras %)
        shifted
        (reduce (fn [acc c]
                  (if (or (= c \ý) (= c \ü))
                    (let [chars (take-while shifter-matra? acc)
                          leftmost (first (take 1 (drop-while shifter-matra? acc)))
                          rest-of-acc (drop 1 (drop-while shifter-matra? acc))]
                      (apply conj (conj rest-of-acc \र \् leftmost) chars))
                    (conj acc c))
                  )
                '()
                (seq s))]
    (apply str (reverse shifted))))

(defn xdvng-to-unicode
  [s]
  (-> s
      replace-normal-cases
      fix-chhoti-i-ki-matra
      fix-ra))
