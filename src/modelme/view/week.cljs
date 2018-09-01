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

(defn week-view [schedule]
  (reagent/with-let [show-timeslot (reagent/atom nil)
                     on-selection (fn [result]
                                    (model/add-activity! @show-timeslot result)
                                    (reset! show-timeslot nil))]
    [:div
     [chooser/activity-choice show-timeslot on-selection]
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
            [sa/TableCell
             (str hour ":00")]
            (doall
              (for [day logic/days
                    :let [activity (get-in schedule [day hour])]]
                ^{:key (str day hour)}
                [sa/TableCell
                 {:on-click
                  (fn hour-click [e]
                    (reset! show-timeslot [day hour]))}
                 (when activity
                   [render-activity activity])]))]))]]]))
