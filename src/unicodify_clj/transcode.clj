(ns unicodify-clj.transcode)

(def table
[["ÔÎ" "्र"] ["Á:" "श्च"] ["Â:" "श्र"] ["Ã:" "श्व"] ["Á" "श्च्"] ["Â" "श्र्"] ["Ã" "श्व्"] ["k" "क"]
 ["Aaô" "ऑ"] ["aô" "ॉ"]
 ["K:" "ख"] ["g:" "ग"] ["G:" "घ"] ["{" "ङ"] ["V" "क्"] ["K" "ख्"] ["g" "ग्"] ["G" "घ्"]
 ["Q:" "ख़"] ["Q" "ख़्"] ["z:" "ज़"] ["z" "ज़्"] ["q" "क़"] ["]" "क़्"] ["^:" "ग़"] ["^" "ग़्"]
 ["c:" "च"] ["C" "छ"] ["j:" "ज"] ["J:" "झ"] ["c" "च्"] ["C" "छ्"] ["j" "ज्"] ["J" "झ्"]
 ["W" "ञ्"] ["X" "ट"] ["Y" "ठ"] ["R" "ड"] ["_" "ड़"] ["Z" "ढ"] ["`" "ढ़"] ["N:" "ण"]
 ["X" "ट्"] ["Y" "ठ्"] ["R" "ड्"] ["`" "ढ़्"] ["N" "ण्"] ["t:" "त"] ["T:" "थ"] ["d" "द"]
 ["D:" "ध"] ["n:" "न"] ["t" "त्"] ["T" "थ्"] ["d" "द्"] ["D" "ध्"] ["n" "न्"] ["p:" "प"]
 ["P" "फ"] ["f" "फ़"] ["b:" "ब"] ["B:" "भ"] ["m:" "म"] ["p" "प्"] ["P" "फ्"] ["F" "फ़्"]
 ["b" "ब्"] ["B" "भ्"] ["m" "म्"] ["y:" "य"] ["r" "र"] ["l:" "ल"] ["v:" "व"] ["y" "य्"]
 ["r" "र्"] ["l" "ल्"] ["v" "व्"] ["s:" "स"] ["S:" "श"] ["\\:" "ष"] ["h" "ह"] ["x:" "क्ष"]
 ["w" "ज्ञ"] ["s" "स्"] ["S" "श्"] ["\\" "ष्"] ["h" "ह्"] ["x" "क्ष्"] ["!" "!"] ["*:" "त्र"]
 ["¹" "द्ध"] ["Â:" "श्र"] ["É" "ह्म"] ["Ã:" "श्व"] ["²" "ड्ड"] ["-" "रू"] ["#" "क्र"] ["¤" "ङ्ग"]
 ["½" "द्य"] ["$" "ज्र"] ["Ä" "ष्ट"] ["¶:" "त्त"] ["¶" "त्त्"] ["Æ" "हृ"] ["*)" "अ"] ["¢" "ङ्क"]
 ["¾" "द्व"] [")" "प्र"] ["Ò" "्र"] ["Ó" "्र"] ["Á:" "श्च"] ["À" "न्न"] ["¬" "ट्ट"] ["(" "फ़्र"]
 ["Õ" "्र"] ["¸" "द्द"] ["," "रु"] ["Ê" "ह्य"] ["A:ð" "ओ"] ["A:ò" "औ"] ["A:" "आ"] ["A" "अ"]
 ["E" "इ"] ["I" "ई"] ["u" "उ"] ["U" "ऊ"] ["Oð" "ऐ"] ["O" "ए"] ["?" "ऋ"] ["@" "ॠ"]
 ["Ï" "्"] ["Î" "्"] ["|" "ऽ"] ["i" "ी"] ["Ù" "ु"] ["Û" "ु"] ["Ø" "ु"]
 ["Ú" "ु"] ["Ü" "ू"] ["Ý" "ू"] ["Þ" "ू"] [":ð" "ो"] [":ò" "ौ"] ["að" "ो"] ["aò" "ौ"]
 ["ð" "े"] ["ñ" "े"] ["a" "ा"]
 ["ò" "ै"] ["ó" "ै"] ["ö" "ं"] ["ù" "ं"] ["ú" "ँ"] ["H" ":"] ["á" "ृ"] ["à" "ृ"]
 ["." "।"] ["/" "॥"] ["ø" "ं"] ["û" "ँ"] ["ô" "ॅ"] ["0" "॰"] ["&" "."]
 ["¥" "ङ्घ"] ["o" "ईं"] ["£" "ङ्ख"] ["¬" "ट्ट"] ["«" "ट्क"] [(str (char 173)) "ट्ठ"] ["®" "ट्य"]
 ["¯" "ठ्य"] ["´" "ड्य"] ["·" "द्ग"] ["»" "द्भ"] ["¼" "द्म"] ["Å" "ष्ठ"] ["Ç" "ह्ण"] ["È" "ह्न"]
 ["É" "ह्म"] ["Ê" "ह्य"] ["Ë" "ह्र"] ["Ì" "ह्ल"] ["Í" "ह्व"] ["¦" "ङ्ङ"] ["§" "ङ्न"] ["¨" "ङ्म"]
 ["©" "ङ्य"] ["ª" "छ्य"] ["°" "ड्ग"] ["±" "ड्घ"] ["³" "ड्म"] ["µ" "ढ्य"] ["º" "द्ब"] ["¿" "घ्न्"]
 ["Ð" "्"] ["Ñ" "्"] ["Ô" "्र"] ["Ö" "्र"] ["×" "्र"] ["ß" "ू"] ["â" "ृ"] ["ã" "ृ"]
 ["ä" "ॄ"] ["å" "ॄ"] ["æ" "ॄ"] ["ç" "ॄ"] ["è" "ॢ"] ["é" "ॢ"] ["ê" "ॢ"] ["ë" "ॢ"] ["ì" "ॣ"]
 ["í" "ॣ"] ["î" "ॣ"] ["ï" "ॣ"] ["õ" "ॅ"] ["0" "०"] ["1" "१"] ["2" "२"] ["3" "३"] ["4" "४"]
 ["5" "५"] ["6" "६"] ["7" "७"] ["8" "८"] ["9" "९"] ["Ā" "ऋ"] ["÷" "ं"]
 ["~" "ं"] ["!" "ॐ"] ["%" "ज़्र"] ["*" "त्र्"] ["[" "फ्"] ["}" "॰"] [";" "ऌ"] ["'" "फ्र"]
 ["+" "स्र"] ["=" "ॡ"] ["L" "ळ"] ["M" "ळ्"] ["<" "ो"] [">" "ौ"]])

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

(defn- unicodify [s]
  (-> s
      replace-normal-cases
      fix-ra
      fix-ra-anusvara
      fix-chhoti-i-ki-matra))

(defn xdvng-to-unicode
  [s]
  (let [unicodified (unicodify s)]
    (print-ascii-chars-found unicodified)
    unicodified))
