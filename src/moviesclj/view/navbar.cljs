(ns moviesclj.view.navbar
  (:require [moviesclj.utils :as utils]
            [reagent.core :as reagent]
            [reagent.session :as session]
            [moviesclj.authentication.authentication :as auth]))

(def login-data (reagent/atom {}))

(defn- with-data-validation
  [function]
  (if (or (empty? (:email @login-data))
          (empty? (:password @login-data)))
    identity
    function))

(defn- update-form-data
  [key form-input]
  (swap! login-data assoc key (utils/input-value form-input)))


(defn- submit-sign-in
  [click-event]
  (.preventDefault click-event)
  ((with-data-validation auth/sign-in!) @login-data))

(defn- sign-in-form
  []
  [:form.navbar-form
   [:div.form-group
    [:input.form-control {:type "email"
                          :value (:email @login-data)
                          :on-change #(update-form-data :email %)
                          :placeholder "Email"}]
    [:input.form-control {:type "password"
                          :value (:password @login-data)
                          :on-change #(update-form-data :password %)
                          :placeholder "Password"}]]
   [:button.btn.btn-default {:type "submit"
                             :on-click submit-sign-in}
    "Log in"]])

(defn- connection-info
  []
  [:ul.nav.navbar-nav
   [:li [:p.navbar-text (session/get-in [:user :name])]]
   [:li [:button.btn.btn-default.navbar-btn.navbar-right
         {:type "button"
          :on-click auth/sign-out!}
         "Sign out"]]])

(defn navbar-component
  []
  (if (auth/signed-in?)
    [connection-info]
    [sign-in-form]))
