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
  (swap! app-state update :schedule logic/add-activity day hour activity))

(defn remove-activity! [[day hour]]
  (swap! app-state update :schedule logic/remove-activity day hour))

(defn copy-activity! [[day hour] [to-day to-hour]]
  (swap! app-state update :schedule logic/copy-activity day hour to-day to-hour))

(defn swap-activity! [[from-day from-hour] [to-day to-hour]]
  (swap! app-state update :schedule logic/swap-activity from-day from-hour to-day to-hour))

(defn move-activity! [[from-day from-hour] [to-day to-hour]]
  (swap! app-state update :schedule logic/move-activity from-day from-hour to-day to-hour))
