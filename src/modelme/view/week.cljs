(ns modelme.view.week
  (:require [modelme.logic :as logic]
            [modelme.view.chooser :as chooser]
            [reagent.core :as reagent]
            [soda-ash.core :as sa]
            [modelme.model :as model]
            [clojure.string :as str]))

;; TODO: sometimes it would be nice to show the parent too
(defn render-activity [{:keys [tag selected-categories children parent]}]
  [:span
   tag
   [:br]
   (if selected-categories
     (str "["
          (str/join ", "
                    (for [[k v] selected-categories]
                      (get-in children [k :children v :tag])))
          "]")
     (:tag parent))])

(defn actions-choice
  "Presents a tree of options as a series of dialogs, the user follows a branch to a leaf."
  [activity on-result]
  (reagent/with-let [close
                     (fn close-modal [result]
                       (reset! activity nil)
                       (on-result result))]
    [sa/Modal {:open (boolean @activity)
               :on-close #(close nil)
               :close-icon true}
     [sa/ModalHeader "Select an action"]
     (into
       [sa/ModalActions]
       (for [action ["Copy" "Move" "Delete" "Swap" "Replace"]]
         [sa/Button
          {:size "massive"
           :on-click
           (fn choice-click [e]
             (close action))}
          action]))]))

(defn week-view [schedule]
  (reagent/with-let [choose-activity-for-timeslot (reagent/atom nil)
                     show-actions (reagent/atom nil)
                     selected-timeslot (reagent/atom nil)
                     mode (reagent/atom nil)
                     on-activity-selection
                     (fn activity-selected [activity]
                       (when activity
                         (model/add-activity! @choose-activity-for-timeslot activity))
                       (reset! choose-activity-for-timeslot nil))
                     exit-mode
                     (fn exit-mode []
                       (reset! selected-timeslot nil)
                       (reset! mode nil))
                     on-action-selection
                     (fn action-selected [action]
                       (condp = action
                         "Delete" (do
                                    (model/remove-activity! @selected-timeslot)
                                    (exit-mode))
                         "Replace" (do
                                     (reset! choose-activity-for-timeslot @selected-timeslot)
                                     (exit-mode))
                         nil (reset! selected-timeslot nil)
                         (reset! mode action)))]
    [:div
     [actions-choice show-actions on-action-selection]
     [chooser/activity-choice choose-activity-for-timeslot on-activity-selection]
     [sa/Table {:definition true
                :celled true
                :fixed true}
      [sa/TableHeader
       [sa/TableRow
        [sa/TableHeaderCell]
        (doall
          (for [day logic/days]
            ^{:key day}
            [sa/TableHeaderCell day]))]]
      [sa/TableBody
       (doall
         (for [hour logic/hours]
           ^{:key hour}
           [sa/TableRow
            [sa/TableCell (str hour ":00")]
            (doall
              (for [day logic/days
                    :let [slot [day hour]
                          activity (get-in schedule slot)]]
                ^{:key (str day hour)}
                [sa/TableCell
                 {:active (= slot @selected-timeslot)
                  :on-click
                  (fn hour-click [e]
                    (condp = @mode
                      "Swap" (do
                               (model/swap-activity! @selected-timeslot slot)
                               (exit-mode))
                      "Move" (do
                               (model/move-activity! @selected-timeslot slot)
                               (exit-mode))
                      "Copy" (if activity
                               (exit-mode)
                               (model/copy-activity! @selected-timeslot slot))
                      (if activity
                        (do (reset! show-actions activity)
                            (reset! selected-timeslot slot))
                        (reset! choose-activity-for-timeslot slot))))}
                 (when activity
                   [render-activity activity])]))]))]]]))
