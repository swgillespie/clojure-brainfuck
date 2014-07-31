(ns brainfuck.core
  (:require [brainfuck.tape :as tape]))

(defn is-valid? [program]
  "Checks for the only syntax error in brainfuck - unbalanced brackets."
  (let [f (fn [balance list]
            (cond
             (empty? list) (= balance 0)
             (= (first list) \[) (and (>= balance 0) (recur (+ balance 1) (rest list)))
             (= (first list) \]) (and (>= balance 0) (recur (- balance 1) (rest list)))
             :else (and (>= balance 0) (recur balance (rest list)))))]
    (f 0 program)))

(declare run)

(defn advance [source memory]
  "Advances the tape if there is more source to be consumed, otherwise does
   nothing."
  (if (not (tape/right-empty? source))
    #(run (tape/shift-right source) memory)))

(defn jump-forward [offset source memory]
  "Jump foward to the matching close bracket and continue execution."
  (let [opcode (tape/head source)]
    (cond
     (and (= offset 1) (= opcode \])) #(advance source memory)
     (= opcode \[) (recur (+ offset 1) (tape/shift-right source) memory)
     (= opcode \]) (recur (- offset 1) (tape/shift-right source) memory)
     :else (recur offset (tape/shift-right source) memory))))

(defn jump-backward [offset source memory]
  "Jump backward to the matching open bracket and continue execution."
  (let [opcode (tape/head source)]
    (cond
     (and (= offset 1) (= opcode \[)) #(advance source memory)
     (= opcode \]) (recur (+ offset 1) (tape/shift-left source) memory)
     (= opcode \[) (recur (- offset 1) (tape/shift-left source) memory)
     :else (recur offset (tape/shift-left source) memory))))

(defn run [source memory]
  "Run one step of the brainfuck interpreter."
  (let [opcode (tape/head source)]
    (cond
     (= opcode \+) #(advance source (tape/apply-head memory inc))
     (= opcode \-) #(advance source (tape/apply-head memory dec))
     (= opcode \<) #(advance source (tape/shift-left memory))
     (= opcode \>) #(advance source (tape/shift-right memory))
     (= opcode \.) (do
                     (print (char (tape/head memory)))
                     #(advance source memory))
     (= opcode \,) (advance source (tape/set-head memory (int (.read *in*))))
     (= opcode \[) (if (= (tape/head memory) 0)
                     #(jump-forward 0 source memory)
                     #(advance source memory))
     (= opcode \]) (if (not (= (tape/head memory) 0))
                     #(jump-backward 0 source memory)
                     #(advance source memory)))))   
    
(defn execute [program]
  (if (not (is-valid? program))
    (println "Syntax error: unbalanced brackets.")
    (trampoline run (tape/load-tape program) (tape/new-tape))))
