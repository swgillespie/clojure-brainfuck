(ns brainfuck.tape)

(defprotocol Tape
  "Do a protocol just for the hell of it"
  (head [tape]
    "Return the data at the head of the tape")
  (apply-head [tape f]
    "Apply a function f to the data at the head of the tape,
     returning a new tape.")
  (set-head [tape val]
    "Set the data at the head of the tape to val and return a new tape")
  (shift-left [tape]
    "Shift the tape once to the left")
  (shift-right [tape]
    "Shift the tape once to the right")
  (right-empty? [tape]
    "True if the right side of the tape is empty, false otherwise"))

(deftype MemoryTape [head left right]
  Tape
  (head [tape]
    head)
  (apply-head [tape f]
    (MemoryTape. (f head) left right))
  (set-head [tape val]
    (MemoryTape. val left right))
  (shift-right [tape]
    (let [[new-head & new-right] right]
      (MemoryTape. new-head (cons head left) new-right)))
  (shift-left [tape]
    (let [[new-head & new-left] left]
      (MemoryTape. new-head new-left (cons head right))))
  (right-empty? [tape]
    (empty? right)))

(defn new-tape []
  "Creates a new MemoryTape as an infinite tape with a head."
  (MemoryTape. 0 (repeat 0) (repeat 0)))

(defn load-tape [str]
  "Loads a string into a new MemoryTape"
  (MemoryTape. (first str) (repeat 0) (rest str)))
  
