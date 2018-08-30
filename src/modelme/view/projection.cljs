(ns modelme.view.projection
  (:require [modelme.view.chart :as chart]
            [modelme.model :as model]))

(defn group-data [schedule]
  (vec
    (for [k [:fat-kg :muscle-kg :weight-kg :insulin-g]
          m schedule]
      {:x (:t m) :y (get m k) :col k})))

;; TODO: timeseries multiple values, is there a vega way?
;; (avoid reshaping with group-data)

(def line-plot
  {:data {:values (group-data (model/calc-schedule))}
   :encoding {:x {:field "x" :type "quantitative"}
              :y {:field "y" :type "quantitative"}
              :color {:field "col" :type "nominal"}}
   :mark "line"
   ;; TODO: I just want it big, is there a large?
   :width "500"})

(defn projection-view []
  [chart/vega-lite line-plot])