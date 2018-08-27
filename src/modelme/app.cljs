(ns modelme.app
  (:require [modelme.view.main :as main]
            [reagent.core :as reagent]
            [goog.dom :as dom]
            [modelme.model :as model]))

(defn main []
  (reagent/render-component
    [main/main-view model/app-state]
    (dom/getElement "app")))

(main)
