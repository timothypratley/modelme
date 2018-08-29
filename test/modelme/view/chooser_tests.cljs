(ns modelme.view.chooser-tests
  (:require [modelme.view.chooser :as chooser]
            [devcards.core :refer-macros [defcard-rg]]
            [reagent.core :as reagent]))

;; TODO: don't rely on model implicitly

(defcard-rg choose-between-example
            (reagent/with-let [children [{} {} {}]
                               time-slot (reagent/atom [0 0])
                               selection (reagent/atom [])
                               close (fn mock-close [result]
                                       (js/alert (str "Got:" result)))]
              [chooser/choose-between children time-slot selection close]))

(defcard-rg choose-all-example
            (reagent/with-let [activity {}
                               time-slot (reagent/atom [0 0])
                               close (fn mock-close [result]
                                       (js/alert (str "Got:" result)))]
              [chooser/choose-all activity time-slot close]))

(defcard-rg activity-choice-example
            (reagent/with-let [time-slot (reagent/atom [0 0])]
              [chooser/activity-choice time-slot]))