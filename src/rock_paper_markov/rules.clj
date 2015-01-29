(ns rock-paper-markov.rules
  (:require [clojure.set :as set]))

(def all-moves
  [:rock :paper :scissors])

(def outcome->verb
  {:win  "beats",
   :lose "loses to",
   :tie  "ties with"})

(def move->weakness
  "Maps 'move' to the move it loses to."
  {:rock     :paper,
   :paper    :scissors,
   :scissors :rock})

(def move->strength
  "Maps 'move' to the move it wins to."
  (set/map-invert move->weakness))

(defn outcome
  "Returns the outcome of 'player-move' against 'ai-move'. One of
  :win, :lose, :tie, or nil if invalid."
  [player-move ai-move]
  (cond
   (= player-move ai-move)
   :tie

   (= (move->strength player-move) ai-move)
   :win

   (= (move->weakness player-move) ai-move)
   :lose))

(defn print-outcome
  [player-move ai-move outcome]
  (println (name player-move)
           (outcome->verb outcome)
           (name ai-move)))

