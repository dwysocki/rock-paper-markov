(ns rock-paper-markov.heuristic
  (:require [rock-paper-markov.markov :as markov]
            [rock-paper-markov.rules :refer [move->weakness move->strength
                                             all-moves]]))

(defn pick-move
  [table prev heuristic]
  (if prev
    ; select best move based on previous move entry in markov table
    (-> prev table heuristic)
    ; no previous move, pick one at random
    (rand-nth all-moves)))

(defn max-prob-win
  [move->count]
  (let [; find the highest count
        max-count (apply max (vals move->count))
        ; filter all moves with same count
        max-moves (filter (fn [[move count]] (= count max-count))
                          move->count)
        ; select one of those moves randomly
        move (first (rand-nth max-moves))]
    ; return the move that beats the selected move
    (move->weakness move)))

(defn min-prob-lose
  [move->count]
  (let [move->prob (markov/move->prob move->count)
        move->prob-lose
        (into {}
              (map (fn [move]
                     [move (-> move move->weakness move->prob)])
                   all-moves))
        min-prob (apply min (vals move->prob-lose))
        min-moves (filter (fn [[move prob]] (= prob min-prob))
                          move->prob-lose)]
    (first (rand-nth min-moves))))
