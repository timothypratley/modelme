(ns modelme.view.main
  (:require [modelme.view.projection :as projection]
            [modelme.view.week :as week]
            [soda-ash.core :as sa]))

(defn main-view [app-state]
  [sa/Container
   [sa/Header {:as "h2"} "Model me"]
   [week/week-view (:schedule @app-state)]
   [projection/projection-view @app-state]])
