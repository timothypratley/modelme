(ns modelme.model
  (:require [reagent.core :as reagent]
            [modelme.logic :as logic]))

(defonce app-state
  (reagent/atom {:me {:weight-kg 80.0
                      :height-cm 173.0
                      :fat-kg 10.0
                      :muscle-kg 20.0
                      :stress-hr 0.0
                      :insulin-g 0.0}
                 :schedule {}}))

(defn add-activity! [[day hour] activity]
  (swap! app-state assoc-in [:schedule day hour] activity))

(defn calc-schedule []
  (logic/calc-schedule (:schedule @app-state) (:me @app-state)))