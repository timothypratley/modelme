(ns modelme.view.week
  (:require [modelme.logic :as logic]
            [modelme.view.chooser :as chooser]
            [reagent.core :as reagent]
            [soda-ash.core :as sa]
            [modelme.model :as model]))

(defn week-view [schedule]
  (reagent/with-let [show-timeslot (reagent/atom nil)
                     on-selection (fn [result]
                                    (model/add-activity! @show-timeslot result)
                                    (reset! show-timeslot nil))]
    [:div
     [chooser/activity-choice show-timeslot on-selection]
     [sa/Table {:definition true
                :celled true}
      [sa/TableHeader
       [sa/TableRow
        [sa/TableHeaderCell]
        (doall
          (for [day logic/days]
            ^{:key day}
            [sa/TableHeaderCell day]))]]
      [sa/TableBody
       (for [hour logic/hours]
         ^{:key hour}
         [sa/TableRow
          [sa/TableCell
           (str hour ":00")]
          (doall
            (for [day logic/days]
              ^{:key (str day hour)}
              [sa/TableCell
               {:on-click
                (fn hour-click [e]
                  (reset! show-timeslot [day hour]))}
               (get-in schedule [day hour :tag])]))])]]]))
