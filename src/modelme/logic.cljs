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

(defn get-in-activities [idxs]
  (get-in-tree activities-tree idxs))

(defn safe-add [x y]
  (max 0.0 (+ x y)))

(defn update-nutrients [me {:keys [insulin-effect carb-effect protein-effect fat-effect]}]
  (-> me
      (update :insulin-g safe-add (or insulin-effect -0.5))
      (update :carbs + (or carb-effect -1.0))
      (update :proteins + (or protein-effect -1.0))
      (update :fats + (or fat-effect -1.0))))

(defn update-stress [{:keys [stress-hr] :as me} {:keys [stress-effect]}]
  (if stress-effect
    (update me :stress-hr + (or stress-effect -1.0))))

(defn fat-tick [{:keys [insulin-g] :as me}]
  (if (pos? insulin-g)
    (update me :fat-kg + 0.1)
    (update me :fat-kg - 0.1)))

(defn muscle-tick [{:keys [stress] :as me}]
  (if (pos? stress)
    (update me :muscle-kg + 0.1)
    (update me :muscle-kg - 0.1)))

(defn calc-schedule [schedule me]
  (reductions
    (fn simulation-step [me activity]
      (-> me
          (update :t inc)
          (update-nutrients activity)
          (update-stress activity)
          (fat-tick)
          (muscle-tick)))
    (assoc me :t 0)
    (for [day days
          hour (range 0 24)]
      (get-in schedule [day hour]))))
