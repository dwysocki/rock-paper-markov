(ns rock-paper-markov.core
  (:require [rock-paper-markov.heuristic :as heuristic]
            [rock-paper-markov.markov :as markov]
            [rock-paper-markov.rules  :as rules]
            [clojure.pprint :refer [pprint]]
            [clojure.string :as string])
  (:gen-class))

(defn print-score
  [wins losses ties]
  (println (str "Wins   = " wins "\n"
                "Losses = " losses "\n"
                "Ties   = " ties)))

(def str->heuristic
  {"max-prob-win"  heuristic/max-prob-win,
   "min-prob-lose" heuristic/min-prob-lose})

(defn -main
  "Starts the game."
  [& args]
  (loop []
    (print "Select AI heuristic: max-prob-win, min-prob-lose (or quit): ")
    (flush)
    (let [choice (string/lower-case (read-line))]
      (when (= choice "quit")
        (System/exit 0))
      (if-let [heuristic (str->heuristic choice)]
        (def ai-heuristic heuristic)
        (do
          (println "Invalid heuristic")
          (recur)))))

  (loop [table  markov/initial-table
         prev   nil
         wins   0
         losses 0
         ties   0]
    (print-score wins losses ties)
    (print "rock, paper, scissors (or quit): ")
    (flush)
    (let [ai-move     (heuristic/pick-move table prev ai-heuristic)
          player-move (-> (read-line)
                          string/lower-case
                          keyword)
          _           (when (= player-move :quit)
                        (println "GG")
                        (System/exit 0))
          outcome     (rules/outcome player-move ai-move)
          new-table   (if prev
                        (markov/update-table table [prev player-move])
                        table)]
      (case outcome
        :win
        (do
          (rules/print-outcome player-move ai-move outcome)
          (println "You won!")
          (recur new-table ai-move (inc wins) losses ties))

        :lose
        (do
          (rules/print-outcome player-move ai-move outcome)
          (println "You lost.")
          (recur new-table ai-move wins (inc losses) ties))

        :tie
        (do
          (rules/print-outcome player-move ai-move outcome)
          (recur new-table ai-move wins losses (inc ties)))

        (do
          (println "Invalid move:" player-move)
          (recur table prev wins losses ties))))))
