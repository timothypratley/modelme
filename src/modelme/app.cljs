(ns modelme.app
  (:require [modelme.view.main :as main]
            [reagent.core :as reagent]
            [goog.dom :as dom]))

(defn main []
  (reagent/render-component
    [main/main-view]
    (dom/getElement "app")))

(main)