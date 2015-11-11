(ns moviesclj.authentication.authentication
  (:require [moviesclj.config :as config]
            [moviesclj.utils :as utils]
            [reagent.session :as session]
            [reagent.cookies :as cookies]
            [ajax.core :as ajax]))

(defn with-auth
  ([]
   (with-auth {}))
  ([ajax-request]
   (assoc-in ajax-request [:headers :Authorization] (cookies/get :session))))

(defn set-current-user!
  [user]
  (cookies/set! :session (:token user))
  (session/put! :user user))

(defn- url-encoded-ajax-post
  [url data handler]
  (ajax/ajax-request
   {:uri (str config/api-url url)
    :method :post
    :params data
    :handler handler 
    :format (ajax/url-request-format) 
    :response-format (ajax/json-response-format {:keywords? true})}))
  
(defn sign-in!
  [sign-in-data]
  (url-encoded-ajax-post "/login" sign-in-data (fn [[success? data]]
                                                 (when success?
                                                   (set-current-user! data)))))

(defn sign-out!
  []
  (cookies/remove! :session)
  (session/remove! :user))

(defn sign-up!
  [sign-up-data]
  (println (str sign-up-data))
  (url-encoded-ajax-post
   "/signUp" sign-up-data #(sign-in! {:email    (:email sign-up-data)
                                      :password (:password sign-up-data)})))

(defn update-user!
  []
  (let [session-id (cookies/get :session)]
    (when (not (nil? session-id))
      (ajax/GET (str config/api-url "/tokenInfo")
                (-> {:handler set-current-user!}
                    with-auth
                    utils/json-str->keyword)))))

(defn signed-in?
  []
  (not (nil? (session/get :user))))
