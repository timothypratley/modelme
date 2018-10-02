(ns modelme.view.chooser-tests
  (:require [modelme.view.chooser :as chooser]
            [devcards.core :refer-macros [defcard-rg]]
            [reagent.core :as reagent]))

(defcard-rg choose-between-example
  (reagent/with-let [activity {:children [{} {} {}]}
                     time-slot (reagent/atom [0 0])
                     selection (reagent/atom [])
                     on-result (fn mock-close [result]
                                 (js/alert (str "Got:" result)))]
    [chooser/choose-between activity time-slot selection on-result]))

(defcard-rg choose-all-example
  (reagent/with-let [activity {}
                     time-slot (reagent/atom [0 0])
                     on-result (fn mock-close [result]
                                 (js/alert (str "Got:" result)))]
    [chooser/choose-all-from-category activity time-slot on-result]))

(defcard-rg activity-choice-example
  (reagent/with-let [time-slot (reagent/atom [0 0])]
    [chooser/activity-choice time-slot]))