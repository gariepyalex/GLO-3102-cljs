(ns ^:figwheel-always moviesclj.core
  (:require [moviesclj.view.home :as home]
            [moviesclj.view.movie :as movie-component]
            [moviesclj.view.actor :as actor-component]
            [moviesclj.view.navbar :as navbar]
            [moviesclj.authentication.authentication :as authentication]
            [moviesclj.authentication.sign-up :as sign-up]
            [reagent.core :as reagent]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType])
  (:import goog.History))

(enable-console-print!)

(defonce app-state (reagent/atom {:text "Hello world!"}))

(def app-dom-mount (js/document.getElementById "app"))
(def navbar-dom-mount (js/document.getElementById "app-navbar"))


(defn page-not-found
  []
  [:h1 "Page not found"])

;; -------------------------
;; Routing
(defn current-page
  []
  [:div [(session/get :current-page)]])

(secretary/defroute "/" []
  (session/put! :current-page home/page))

(secretary/defroute "/signUp" []
  (session/put! :current-page sign-up/page))

(secretary/defroute "/actors/:id" [id]
  (session/put! :current-page (actor-component/actor-component id)))

(secretary/defroute "/movies/:id" [id]
  (session/put! :current-page (movie-component/movie-component id)))

(secretary/defroute "*" []
  (session/put! :current-page page-not-found))

;; -------------------------
;; History
(defn hook-browser-navigation!
  []
  (doto (History.)
    (events/listen
     HistoryEventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(defn mount-root
  []
  (reagent/render [current-page] app-dom-mount))

(defn mount-navbar
  []
  (reagent/render [navbar/navbar-component] navbar-dom-mount))

(defn init! []
  (secretary/set-config! :prefix "#")
  (hook-browser-navigation!)
  (authentication/update-user!)
  (mount-root)
  (mount-navbar))


(defn on-js-reload
  []
  (mount-root)
  (mount-navbar))

(init!)
