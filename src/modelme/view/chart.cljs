(ns modelme.view.chart
  (:require [reagent.core :as reagent]
            [cljsjs.vega]
            [cljsjs.vega-lite]
            [cljsjs.vega-embed]
            [cljsjs.vega-tooltip]))

;; TODO: fix interop advanced mode
(defn render-vega-lite
  ([spec elem]
   (when spec
     (let [spec (clj->js spec)
           opts {:renderer "canvas"
                 :mode "vega-lite"}]
       (-> (js/vegaEmbed elem spec (clj->js opts))
           ;; TODO: fix tooltip syntax
           #_(.then (fn [res]
                    (js/vegaTooltip.vegaLite (.-view res) spec)))
           (.catch println))))))

(defn vega-lite
  "Reagent component that renders vega-lite."
  [spec]
  (reagent/create-class
    {:display-name "vega-lite"
     :component-did-mount
     (fn vega-lite-did-mount [this]
       (render-vega-lite spec (reagent/dom-node this)))
     :component-will-update
     (fn vega-lite-will-update [this [_ new-spec]]
       (render-vega-lite new-spec (reagent/dom-node this)))
     :reagent-render
     (fn vega-lite-render [spec]
       [:div])}))
