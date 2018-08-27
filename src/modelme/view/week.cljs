(ns modelme.view.week
  (:require [modelme.model :as model]
            [modelme.logic :as logic]
            [reagent.core :as reagent]
            [soda-ash.core :as sa]))

(defn activity-choice [time-slot]
  (reagent/with-let [selection (reagent/atom [])
                     close (fn close-modal [e]
                             (reset! selection [])
                             (reset! time-slot nil))]
    [sa/Modal {:open (boolean @time-slot)
               :on-close close
               :close-icon true}
     [sa/ModalHeader "Select an activity"
      (when (seq @selection)
        [sa/Button
         {:floated "right"
          :on-click
          (fn back-click [e]
            (swap! selection pop))}
         "back"])]
     (into
       [sa/ModalActions]
       (let [{:keys [children]} (logic/get-in-activites @selection)]
         (map-indexed
           (fn child-action [idx {:keys [tag] :as activity}]
             [sa/Button
              {:size "massive"
               :on-click
               (fn choice-click [e]
                 (swap! selection conj idx)
                 (when (empty? (:children (logic/get-in-activites @selection)))
                   (model/add-activity! @time-slot activity)
                   (close)))}
              tag])
           children)))]))

(defn week-view [schedule]
  (reagent/with-let [show-timeslot (reagent/atom nil)]
    [:div
     [activity-choice show-timeslot]
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
