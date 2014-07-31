(ns brainfuck.main
  (:require [brainfuck.core :as core])
  (:gen-class :main true))

(defn -main
  ([]
     (do
       (core/execute (read-line))
       (flush)))
  ([file]
     (do
       (core/execute (slurp file))
       (flush)))
  ([whatever & args]
     (println "Usage: brainfuck.jar [filename]")))


