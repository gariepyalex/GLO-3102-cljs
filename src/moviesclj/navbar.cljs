(ns moviesclj.navbar
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

(defn- sign-in-form
  []
  [:form.navbar-form.navbar-right
   [:div.form-group
    [:input.form-control {:type "email"
                          :value (:email @login-data)
                          :on-change #(swap! login-data assoc :email (utils/input-value %))
                          :placeholder "Email"}]
    [:input.form-control {:type "password"
                          :on-change #(swap! login-data assoc :password (utils/input-value %))
                          :placeholder "Password"}]]
   [:button.btn.btn-default {:type "submit"
                             :on-click #(do (.preventDefault %)
                                            ((with-data-validation auth/sign-in!) @login-data))}
    "Log in"]])

(defn navbar-component
  []
  (if (auth/signed-in?)
    [:ul.nav.navbar-nav.navbar-right
    [:li [:p.navbar-text (session/get-in [:user :name])]]
    [:li [:button.btn.btn-default.navbar-btn.navbar-right
     {:type "button"
      :on-click auth/sign-out!}
     "Sign out"]]]
    [sign-in-form]))
