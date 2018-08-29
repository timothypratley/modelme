(ns modelme.view.chart-tests
  (:require [modelme.view.chart :as chart]
            [clojure.test :refer [deftest is]]
            [devcards.core :refer-macros [defcard-rg]]))

(defn group-data [& names]
  (apply concat (for [n names]
                  (map-indexed (fn [i x] {:x i :y x :col n}) (take 20 (repeatedly #(rand-int 100)))))))

(def line-plot
  {:data {:values (group-data "monkey" "slipper" "broom")}
   :encoding {:x {:field "x"}
              :y {:field "y"}
              :color {:field "col" :type "nominal"}}
   :mark "line"})

(defcard-rg line-plot-example
  [chart/vega-lite line-plot])
