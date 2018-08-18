(ns modelme.view.projection
  (:require [modelme.view.chart :as chart]))

(defn group-data [& names]
  (apply concat (for [n names]
                  (map-indexed (fn [i x] {:x i :y x :col n}) (take 20 (repeatedly #(rand-int 100)))))))

(def line-plot
  {:data {:values (group-data "monkey" "slipper" "broom")}
   :encoding {:x {:field "x"}
              :y {:field "y"}
              :color {:field "col" :type "nominal"}}
   :mark "line"})

(defn projection-view []
  [chart/vega-lite line-plot])