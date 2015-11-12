(ns moviesclj.view.movie
  (:require [moviesclj.view.data-utils :as utils]
            [moviesclj.config :as config]
            [reagent.core :as reagent]
            [clojure.string :as string]))

(defonce movie-data (reagent/atom {}))

(defn add-high-res-img!
  []
  (let [low-res-img (:artworkUrl100 @movie-data)]
    (when low-res-img
      (swap! movie-data assoc :artworkUrl500
             (string/replace  low-res-img #"100x100" "500x500")))))

(defn movie-component
  [id]
  (utils/update-single-data! movie-data (str config/api-url "/movies/" id))
  (fn []
    (add-high-res-img!)
    [:div
     [:h1 (:trackName @movie-data)]
     [:h4 "Genre - " (:primaryGenreName @movie-data)]
     [:img {:src (:artworkUrl500 @movie-data)}]
     [:a.itunes-link {:href (:trackViewUrl @movie-data)}]]))
