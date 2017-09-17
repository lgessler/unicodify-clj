(ns unicodify-clj.transcode)

(def xdvng-chars (list

"ÔÎ"
"Á:" "Â:" "Ã:" "Á" "Â" "Ã"

"k" "K:" "g:" "G:" "{"
"V" "K"  "g"  "G"
"Q:" "Q" "z:" "z" "q" "]" "^:" "^"

"c:" "C" "j:" "J:"
"c"  "C" "j"  "J" "W"
"X" "Y" "R" "_" "Z" "`" "N:"
"X" "Y" "R" "`"  "N"
"t:" "T:" "d" "D:" "n:"
"t"  "T"  "d" "D"  "n"
"p:" "P" "f" "b:" "B:" "m:"
"p"  "P" "F" "b"  "B"  "m"

"y:" "r" "l:" "v:"
"y"  "r" "l"  "v"

"s:" "S:" "\\:" "h" "x:" "w"
"s"  "S"  "\\"  "h" "x"

"!" "*:" "¹"  "Â:" "É" "Ã:" "²" "-" "#" "¤" "½" "$" "Ä" "¶:" "¶" "Æ"
"*)" "¢"  "¾"  ")" "Ò"  "Ó" "Á:" "À" "¬" "(" "Õ" "¸" "," "Ê"

"A:ð" "A:ò" "A:" "A" "E" "I" "u" "U" "Oð" "O" "?" "@"

"Ï" "Î" "|" "a" "i" "Ù" "Û" "Ø" "Ú" "Ü" "Ý" "Þ"

":ð" "ð" "ñ" ":ò" "ò" "ó" "ö" "ù" "ú" "H" "á" "à"

"." "/" "ø" "û" "ô" "0" "&"

))

(def unicode-chars (list

"्र"
"श्च" "श्र" "श्व" "श्च्" "श्र्" "श्व्"

"क" "ख" "ग" "घ" "ङ"
"क्" "ख्" "ग्" "घ्"
"ख़" "ख़्" "ज़" "ज़्" "क़" "क़्" "ग़" "ग़्"

"च" "छ" "ज" "झ"
"च्" "छ्" "ज्" "झ्" "ञ्"
"ट" "ठ" "ड" "ड़" "ढ" "ढ़" "ण"
"ट्" "ठ्" "ड्" "ढ़्" "ण्"
"त" "थ" "द" "ध" "न"
"त्" "थ्" "द्" "ध्" "न्"
"प" "फ" "फ़" "ब" "भ" "म"
"प्" "फ्" "फ़्" "ब्" "भ्" "म्"

"य" "र" "ल" "व"
"य्" "र्" "ल्" "व्"

"स" "श" "ष" "ह" "क्ष" "ज्ञ"
"स्" "श्" "ष्" "ह्" "क्ष्"

"!" "त्र" "द्ध" "श्र" "ह्म" "श्व" "ड्ड" "रू" "क्र" "ङ्ग" "द्य" "ज्र" "ष्ट" "त्त" "त्त्" "हृ"
"अ" "ङ्क" "द्व" "प्र" "्र" "्र" "श्च" "न्न" "ट्ट" "फ़्र" "्र" "द्द" "रु" "ह्य"

"ओ" "औ" "आ" "अ" "इ" "ई" "उ" "ऊ" "ऐ" "ए" "ऋ" "ॠ"

"्" "्" "ऽ" "ा" "ी" "ु" "ु" "ु" "ु" "ू" "ू" "ू"

"ो" "े" "े" "ौ" "ै" "ै" "ं" "ं" "ँ" ":" "ृ" "ृ"

"।" "॥" "ं" "ँ" "ॅ" "॰" "."

"ईं" "ङ्ख" "ट्ट" "ट्क" "ट्ठ" "ट्य"
"ड्म" "ढ्य" "द्ब" "घ्न्" "श्च्"
))

(def table (into (map vector xdvng-chars unicode-chars)
                 (vec
{
 "¥" "ङ्घ", "o" "ईं", "£" "ङ्ख", "¬" "ट्ट", "«" "ट्क", (str (char 173)) "ट्ठ", "®" "ट्य",
 "¯" "ठ्य", "´" "ड्य", "·" "द्ग", "»" "द्भ", "¼" "द्म", "Å" "ष्ठ", "Ç" "ह्ण", "È" "ह्न", "É" "ह्म",
 "Ê" "ह्य", "Ë" "ह्र", "Ì" "ह्ल", "Í" "ह्व", "¦" "ङ्ङ", "§" "ङ्न", "¨" "ङ्म", "©" "ङ्य", "ª" "छ्य",
 "°" "ड्ग", "±" "ड्घ", "³" "ड्म", "µ" "ढ्य", "º" "द्ब", "¿" "घ्न्",
 "Ð" "्", "Ñ" "्", "Ô" "्र", "Ö" "्र", "×" "्र", "ß" "ू", "â" "ृ", "ã" "ृ", "ä" "ॄ",
 "å" "ॄ", "æ" "ॄ", "ç" "ॄ", "è" "ॢ", "é" "ॢ", "ê" "ॢ", "ë" "ॢ", "ì" "ॣ", "í" "ॣ",
 "î" "ॣ", "ï" "ॣ", "õ" "ॅ", "0" "०", "1" "१", "2" "२", "3" "३", "4" "४", "5" "५",
 "6" "६", "7" "७", "8" "८", "9" "९", "Ā" "ऋ", "÷" "ं"

 "~" "ं", "!" "ॐ", "%" "ज़्र", "&" ".", "*" "त्र्", "[" "फ्", "}" "॰", ";" "ऌ", "'" "फ्र",
 "+" "स्र", "=" "ॡ", "L" "ळ", "M" "ळ्", "<" "ो", ">" "ौ",
 })))

(defn- replace-normal-cases [s]
  "Use simple string replacement to take care of the majority of cases.
   Note that the table is ordered so that the replacements are done correctly."
  (reduce (fn [acc [k v]] (clojure.string/replace acc k v))
          s
          table))

(defn transpose-i
  "Swaps the position of the first chhoti i and the character next to it
   \\e is a chhoti i, and \\¡ is a longer chhoti i that is used with conjuncts"
  [sl]
  (cond (empty? sl) '()
        (or (= (first sl) \e) (= (first sl) \¡))
        (if (nil? (second sl))
          (cons \ि '())
          ;swap spots with char to right
          (cons (second sl) (cons \ि (transpose-i (rest (rest sl)))))) 
        :else (cons (first sl) (transpose-i (rest sl)))))

(defn- slide-i
  "Shifts the chhoti i until it's totally past the associated 'chunk'"
  [sl]
  (reverse
   (reduce (fn [acc c3]
             ;; Note that the list is the REVERSE of the string!
             (let [[c2 c1] (take 2 acc)]
               (cond
                 ;; You should never find the seq ि् or ि़ in real hindi--keep shifting
                 (or (and (= c1 \ि) (= c2 \्))
                     (and (= c1 \ि) (= c2 \़))) (conj (drop 2 acc) c2 c3 c1)
                 ;; You should also never find ्ि or र्ि
                 (or (and (= c2 \ि) (= c1 \्))
                     (and (= c2 \ि) (= c1 \ý))) (conj (drop 2 acc) c1 c3 c2)
                 :else (conj acc c3))))
           '()
           sl)))

(defn- fix-chhoti-i-ki-matra
  [s]
  (apply str (slide-i (transpose-i (seq s)))))

(defn shifter-matra?
  [s]
  ((set "ािीुूृेैोौंःँॅ") s))

(defn fix-ra
  "Fixes the position of half ras"
  [s]
  (let [shifted
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

(defn fix-ra-anusvara
  ""
  [s]
  (let [shifted
        (reduce (fn [acc c]
                  (if (or (= c \þ) (= c \ÿ))
                    (let [chars (take-while shifter-matra? acc)
                          leftmost (first (take 1 (drop-while shifter-matra? acc)))
                          rest-of-acc (drop 1 (drop-while shifter-matra? acc))]
                      (conj (apply conj (conj rest-of-acc \र \् leftmost) chars) \ं))
                    (conj acc c)))
                '()
                (seq s))]
    (apply str (reverse shifted))))

(defn- print-ascii-chars-found
  [s]
  (doseq [c (range 33 256)]
    (if (clojure.string/includes? s (str (char c)))
      (if (not ((set [160 46 48 49 50 51 52 53 54 55 56 57 58]) c))
        (println "WARNING: found non-Devanagari char"
                 (str "\"" (char c) "\"")
                 (str " (" c ")")
                 (str " in this passage:\n\n\t" s))))))

(defn xdvng-to-unicode
  [s]
  (let [unicodified (-> s
                        replace-normal-cases
                        fix-chhoti-i-ki-matra
                        fix-ra
                        fix-ra-anusvara)]
    (print-ascii-chars-found unicodified)
    unicodified))


;; todo:
;; line 80 in aavanchit
;; no transcode in the ghazals
