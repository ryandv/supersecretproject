(ns supersecretproject.routes.home
  (:require [supersecretproject.layout :as layout]
            [supersecretproject.db.core :as db]
            [bouncer.core :as b]
            [bouncer.validators :as v]
            [clojure.string :as s]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :refer [ok]]
            [ring.util.response :refer [redirect]]
            [clojure.java.io :as io]))

(defn home-page [{:keys [flash]}]
  (layout/render
    "home.html" (merge {:listings (db/get-all-listings)}
                       (select-keys flash [:name :message :errors]))))

(defn about-page []
  (layout/render "about.html"))

(defn validate-listing [params]
  (first
    (b/validate
      ;; complete hack because ring does not appear to parse strings
      ;; representing numbers in www-form-urlencoded requests into actual
      ;; Numbers (this can be avoided if we just use JSON)
      (if (not (s/blank? (get params :price)))
        (merge params {:price (Float/parseFloat (get params :price))})
        params)
      :author v/required
      :title v/required
      :price [v/required v/number v/positive])))

(defn save-listing! [{:keys [params]}]
  (if-let [errors (validate-listing params)]
    (-> (redirect "/")
      (assoc :flash (assoc params :errors errors)))
    (do
      (db/create-listing! params)
      (redirect "/"))))

(defroutes home-routes
  (GET "/" request (home-page request))
  (POST "/" request (save-listing! request))
  (GET "/about" request (about-page)))
