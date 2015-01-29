(ns rock-paper-markov.markov)

(def empty-row
  {:rock     0,
   :paper    0,
   :scissors 0})

(def initial-table
  {:rock     empty-row,
   :paper    empty-row,
   :scissors empty-row})

(defn update-table
  "Increments the count for 'action' followed by 'reaction' in 'table'."
  [table [action reaction :as event]]
  (update-in table event inc))

(defn move->prob
  [move->count]
  (let [total-count (apply + (vals move->count))]
    (into {}
          (if (zero? total-count)
            ; nothing has been counted yet, so initialize all prob to 1/3
            (zipmap (keys move->count)
                    (repeat 1/3))
            (map (fn [[move count]]
                   [move (/ count total-count)])
                 move->count)))))
