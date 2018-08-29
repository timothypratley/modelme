(ns modelme.view.week-tests
  (:require [modelme.view.week :as week]
            [devcards.core :refer-macros [defcard-rg]]
            #_[soda-ash.core :as sa]))

;; TODO: how to make a permanent modal? that doesn't block the screen
#_(defcard-rg activity-example
  (with-redefs [sa/Modal :div]
    (let [show-modal? (reagent/atom true)]
      (fn []
        [week/activity-choice show-modal?]))))

(defcard-rg week-example
  [week/week-view {"Wednesday" {10 "Chocolate"}}])