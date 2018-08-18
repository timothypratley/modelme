(ns modelme.view.main
  (:require [modelme.view.projection :as projection]))

(defn main-view []
  [:div
   [:h3 "Model me"]
   [projection/projection-view]])
