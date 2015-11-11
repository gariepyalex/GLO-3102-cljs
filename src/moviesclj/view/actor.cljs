(ns moviesclj.view.actor
  (:require [moviesclj.authentication.authentication :as auth]
            [moviesclj.config :as config]
            [moviesclj.view.data-utils :as utils]
            [reagent.core :as reagent]))

(defonce actor-data (reagent/atom {}))

(defn actor-component
  [id]
  (utils/update-single-data! actor-data (str config/api-url "/actors/" id))
  (fn []
    [:div
     [:h1 (:artistName @actor-data)]
     [:h4 "Genre - " (:primaryGenreName @actor-data)]
     [:a.itunes-link {:href (:artistLinkUrl @actor-data)}]]))
