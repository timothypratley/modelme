(ns modelme.view.projection
  (:require [modelme.view.chart :as chart]
            [modelme.model :as model]))

(def line-plot
  {:data {:values (model/calc-schedule)}
   :encoding {:x {:field "t"}
              :y {:field "fat"}
              :color {:field "col" :type "nominal"}}
   :mark "line"})

(prn
  (model/calc-schedule))

(defn projection-view []
  [chart/vega-lite line-plot])