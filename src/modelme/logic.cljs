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
     ["Light" {:insulin-effect 1}]
     ["Medium" {:insulin-effect 2}]
     ["Heavy" {:insulin-effect 3}]]
    ["Composition"
     ["Carbs" {:insulin-effect 3
               :carb-effect 5}]
     ["Protein" {:protein-effect 4
                 :fat-effect 1}]
     ["Balanced" {:carb-effect 2
                  :protein-effect 2
                  :fat-effect 1}]]]
   ["Drink"
    ["Soda" {:quantity 5}]
    ["Coffee"
     ["Black" {}]
     ["Cream" {:fat-effect 1}]
     ["Sugar" {:insulin-effect 2}]
     ["Sugar and Cream" {:fat-effect 1
                         :insulin-effect 2}]]
    ["Smoothie"
     ["Protein" {:protein-effect 2}]
     ["Sweet" {:carb-effect 2
               :insulin-effect 3}]
     ["Blend" {:carb-effect 1
               :protein-effect 1
               :insulin-effect 1}]]
    ["Alcohol"
     ["Beer"
      ["Lite" {:quantity 5
               :carb-effect 1
               :insulin-effect 1}]
      ["Full" {:quantity 5
               :carb-effect 2}]]
     ["Wine" {:quantity 5
              :insulin-effect 2
              :carb-effect 1}]
     ["Spirits" {:quantity 5
                 :insulin-effect 1}]]]
   ["Snack"
    ["Apple" {:carb-effect 1
              :insulin-effect 1}]
    ["Chocolate" {:carb-effect 1
                  :fat-effect 1
                  :insulin-effect 1}]
    ["Chips" {:carb-effect 1
              :insulin-effect 1
              :salt-effect 1}]
    ["Bakery" {:carb-effect 2
               :insluin-effect 1}]]
   ["Workout"
    ["Cardio" {:stress-effect 48}]
    ["Weights" {:stress-effect 72}]
    ["Sport" {:stress-effect 24}]]
   ["Incidental"
    ["Walking" {:stress-effect 12}]
    ["Stairs" {:stress-effect 4}]
    ["Yoga" {:stress-effect 8}]
    ["Cycling" {:stress-effect 24}]]])

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

(defn update-nutrients [me {:keys [insulin-effect carb-effect protein-effect fat-effect stress-effect]}]
  (-> me
      (update :insulin-g safe-add (or insulin-effect -0.1))
      (update :ready-carbs-kg safe-add (or carb-effect -1.0))
      (update :ready-proteins-kg safe-add (or protein-effect -1.0))
      (update :ready-fats-kg safe-add (or fat-effect -1.0))
      (update :stress-hr safe-add (or stress-effect -1.0))))

(defn fat-tick [{:keys [insulin-g] :as me}]
  (if (pos? insulin-g)
    (update me :fat-kg safe-add 0.1)
    (update me :fat-kg safe-add -0.1)))

(defn muscle-tick [{:keys [stress-hr] :as me}]
  (if (pos? stress-hr)
    (update me :muscle-kg safe-add 0.1)
    (update me :muscle-kg safe-add -0.1)))

(defn calc-schedule [schedule me]
  (reductions
    (fn simulation-step [me activity]
      (-> me
          (update :t inc)
          (update-nutrients activity)
          (fat-tick)
          (muscle-tick)))
    (assoc me :t 0)
    (for [day days
          hour (range 0 24)]
      (get-in schedule [day hour]))))

(defn add-activity [schedule day hour activity]
  (assoc-in schedule [day hour] activity))

(defn remove-activity [schedule day hour]
  (update-in schedule [day] dissoc hour))

(defn assoc-in-dissoc-out [m ks v]
  (if v
    (assoc-in m ks v)
    (update-in m (butlast ks) dissoc (last ks))))

(defn swap-activity [schedule from-day from-hour to-day to-hour]
  (let [a (get-in schedule [from-day from-hour])
        b (get-in schedule [to-day to-hour])]
    (-> schedule
        (assoc-in-dissoc-out [from-day from-hour] b)
        (assoc-in-dissoc-out [to-day to-hour] a))))

(defn move-activity [schedule from-day from-hour to-day to-hour]
  (let [activity (get-in schedule [from-day from-hour])]
    (-> schedule
        (assoc-in [to-day to-hour] activity)
        (update-in [from-day] dissoc from-hour))))

(defn copy-activity [schedule from-day from-hour to-day to-hour]
  (let [activity (get-in schedule [from-day from-hour])]
    (assoc-in schedule [to-day to-hour] activity)))
