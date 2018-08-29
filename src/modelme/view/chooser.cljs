(ns modelme.view.chooser
  (:require [modelme.logic :as logic]
            [modelme.model :as model]
            [reagent.core :as reagent]))

(defn choose-between
  "Making a choice may open up further sub-choices, or reach a leaf choice."
  [children time-slot selection on-result]
  (into
    [sa/ModalActions]
    (map-indexed
      (fn child-action [idx {:keys [tag] :as activity}]
        [sa/Button
         {:size "massive"
          :on-click
          (fn choice-click [e]
            (swap! selection conj idx)
            (when (empty? (:children (logic/get-in-activities @selection)))
              (model/add-activity! @time-slot activity)
              (on-result activity)))}
         tag])
      children)))

(defn maybe-add [a b]
  (cond (nil? a) b
        (nil? b) a
        (number? a) (+ a b)
        (map? a) (merge a b)
        :else (throw (ex-info (str "Don't know how to merge '"
                                   (type a) "' with '" (type b) "'")
                              {:a a
                               :b b}))))

(defn choose-all
  "User needs to select multiple options that describe the activity before proceeding.
  Must be a leaf choice, as further navigation is blocked.
  Each child of the activity is considered a category.
  Each category has children which are the options to choose from.
  The result is the merge of the initial activity with all the selected options."
  [{:keys [children tag] :as activity} on-result]
  (reagent/with-let
    [selected-categories (reagent/atom {})
     required-category-count (count children)]
    (into
      [sa/ModalActions
       [sa/Header tag]]
      (map-indexed
        (fn [category-index {:keys [tag children]}]
          (into
            [sa/ModalActions
             [sa/Header tag]]
            (map-indexed
              (fn [choice-index {:keys [tag]}]
                (let [selected? (= (get @selected-categories category-index)
                                   choice-index)]
                  [sa/Button
                   {:size "massive"
                    :highlight selected?
                    :on-click
                    (fn choose-one [e]
                      (swap! selected-categories assoc category-index choice-index)
                      (when (= (count @selected-categories)
                               required-category-count)
                        (let [combined-activity
                              (apply merge-with maybe-add
                                     activity
                                     {:selected-categories @selected-categories}
                                     (for [[k v] @selected-categories]
                                       (-> activity
                                           (:children)
                                           (get k)
                                           (:children)
                                           (get v)
                                           (dissoc :tag))))]
                          (on-result combined-activity))))}
                   tag]))
              children)))
        children))))

(defn activity-choice
  "Presents a tree of options as a series of dialogs, the user follows a branch to a leaf."
  [time-slot on-result]
  (reagent/with-let
    [selection (reagent/atom [])
     close (fn close-modal [result]
             (reset! selection [])
             (on-result result))]
    [sa/Modal {:open (boolean @time-slot)
               :on-close #(close nil)
               :close-icon true}
     [sa/ModalHeader "Select an activity"
      (when (seq @selection)
        [sa/Button
         {:floated "right"
          :on-click
          (fn back-click [e]
            (swap! selection pop))}
         "back"])]
     ;; TODO: pass in the activity tree... where does get-in-activities belong?
     (let [{:keys [children logical] :as activity} (logic/get-in-activities @selection)]
       (if (= logical :and)
         [choose-all activity close]
         [choose-between children selection close]))]))
