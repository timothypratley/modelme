(ns modelme.logic)

(def days
  ["Monday" "Tuesday" "Wednesday" "Thursday" "Friday" "Saturday" "Sunday"])

(def hours
  (range 8 21))

(def activities
  ["Activities"
   ["Meal"
    {:logical :and}
    ["Size"
     ["Light" {}]
     ["Medium" {}]
     ["Heavy" {}]]
    ["Composition"
     ["Carbohydrate" {}]
     ["Protein" {}]
     ["Ballanced" {}]]]
   ["Drink"
    ["Soda" {:quantity 5}]
    ["Coffee"
     ["Black" {}]
     ["Cream" {}]
     ["Sugar" {}]
     ["Sugar and Cream" {}]]
    ["Smoothie"
     ["Protein" {}]
     ["Sweet" {}]
     ["Blend" {}]]
    ["Alcohol"
     ["Beer"
      ["Lite" {:quantity 5}]
      ["Full" {:quantity 5}]]
     ["Wine" {:quantity 5}]
     ["Spirits" {:quantity 5}]]]
   ["Snack"
    ["Apple" {}]
    ["Chocolate" {}]
    ["Chips" {}]
    ["Bakery" {}]]
   ["Workout"
    ["Cardio" {}]
    ["Weights" {}]
    ["Sport" {}]
    ["Walk" {}]]
   ["Incidental"
    ["Walking" {}]
    ["Stairs" {}]
    ["Yoga" {}]
    ["Cycling" {}]]])

(defn index-by [k xs]
  (into {}
        (for [x xs]
          [(k x) x])))

(defn to-map
  [x]
  (if (vector? x)
    (let [[tag & children] x]
      (if (map? (first children))
        {tag (apply merge (map to-map (rest children)))}
        {tag (apply merge (map to-map children))}))
    x))

(defn to-tree
  [x]
  (if (vector? x)
    (let [[tag & children] x
          attrs? (first children)]
      (if (map? (first children))
        (assoc attrs? :tag tag
                      :children (mapv to-tree (rest children)))
        {:tag tag
         :children (mapv to-tree children)}))
    x))


(def activities-tree
  (to-tree activities))

(defn get-in-tree [tree idxs]
  (reduce (fn [{:keys [children]} idx]
            (get children idx))
          tree
          idxs))

(defn get-in-activites [idxs]
  (get-in-tree activities-tree idxs))

(defn update-insulin [{:keys [insulin] :as me} {:keys [insulin-effect]}]
  (if insulin-effect
    (update me :insulin + insulin-effect)
    (cond-> me
            (pos? insulin) (update me :insulin dec))))

(defn fat-tick [{:keys [insulin] :as me}]
  (if (pos? insulin)
    (update me :fat inc)
    (update me :fat dec)))

(defn muscle-tick [{:keys [stress] :as me}]
  (if (pos? stress)
    (update me :muscle inc)
    (update me :muscle dec)))

(defn calc-schedule [schedule]
  (reductions
    (fn simulation-step [me activity]
      (-> me
          (update-insulin activity)
          (fat-tick)
          (muscle-tick)
          (update :t inc)))
    {:fat 100
     :muscle 100
     :insulin 0
     :t 0}
    (for [day days
          hour (range 0 24)]
      (get-in schedule [day hour]))))
