(ns moviesclj.actor
  (:require [moviesclj.authentication.authentication :as auth]
            [moviesclj.config :as config]
            [moviesclj.utils :as utils]
            [reagent.core :as reagent]
            [ajax.core :as ajax]))

(defonce actor-data (reagent/atom {}))

(defn update-actor-data!
  [id]
  (ajax/GET (str config/api-url "/actors/" id)
            (-> {:handler #(reset! actor-data (first (:results %)))}
                auth/with-auth
                utils/json-str->keyword)))

(defn actor-component
  [id]
  (update-actor-data! id)
  (fn []
    [:div
     [:h1 (:artistName @actor-data)]
     [:h4 "Genre - " (:primaryGenreName @actor-data)]
     [:a.itunes-link {:href (:artistLinkUrl @actor-data)}]]))
