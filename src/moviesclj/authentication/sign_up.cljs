(ns moviesclj.authentication.sign-up
  (:require [moviesclj.authentication.authentication :as authentication]
            [moviesclj.utils :as utils]
            [reagent.core :as reagent]))

(def data (reagent/atom {}))

(defn update-data
  [key value]
  (reset! data (assoc @data key value)))

(defn page
  []
  (reset! data {})
  (fn []
    [:form
     [:div.form-group
      [:label "Email"]
      [:input.form-control {:placeholder "Email"
                            :value (:email @data)
                            :on-change #(update-data :email (utils/input-value %))
                            :type "email"}]]
     [:div.form-group
      [:label "Name"]
      [:input.form-control {:placeholder "Username"
                            :value (:name @data)
                            :on-change #(update-data :name (utils/input-value %))
                            :type "text"}]]
     [:div.form-group
      [:label "Password"]
      [:input.form-control {:placeholder "Password"
                            :password (:password @data)
                            :on-change #(update-data :password (utils/input-value %))
                            :type "password"}]]
     [:button.btn.btn-md.btn-success {:on-click #(do
                                                   (.preventDefault %)
                                                   (authentication/sign-up! @data))}
      "Sign Up"]]))
