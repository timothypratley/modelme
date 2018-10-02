(ns modelme.view.projection
  (:require [modelme.view.chart :as chart]
            [modelme.model :as model]
            [modelme.logic :as logic]))

(defn group-data [schedule]
  (vec
    (for [k [:fat-kg :muscle-kg :weight-kg :insulin-g]
          m schedule]
      {:x (:t m)
       :y (get m k)
       :col k})))

;; TODO: timeseries multiple values, is there a vega way?
;; (avoid reshaping with group-data)

(defn line-plot [values]
  {:data {:values values}
   :encoding {:x {:field "x" :type "quantitative"}
              :y {:field "y" :type "quantitative"}
              :color {:field "col" :type "nominal"}}
   :mark "line"
   ;; TODO: I just want it big, is there a large?
   :width "500"})

(defn projection-view [{:keys [schedule me]}]
  [chart/vega-lite (line-plot (group-data
                                (logic/calc-schedule schedule me)))])
