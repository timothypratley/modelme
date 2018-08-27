(ns modelme.model
  (:require [reagent.core :as reagent]
            [modelme.logic :as logic]))

(defonce app-state
  (reagent/atom {}))

(defn add-activity! [[day hour] activity]
  (swap! app-state assoc-in [:schedule day hour] activity))

(defn calc-schedule []
  (logic/calc-schedule (:schedule @app-state)))