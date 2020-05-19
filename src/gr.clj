#!/usr/bin/env bb

(ns gr
  (:require [clojure.string :as str]
            [babashka.curl :as curl]
            [cheshire.core :as json]
            [clojure.java.io :as io]))

(defn cwd []
  (-> (System/getProperty "user.dir")
    (str/split #"/")
    last))

(defn create-github-repo!
  [name]
  (curl/post "https://api.github.com/user/repos"
    {:headers {"Authorization" (str "token " (System/getenv "GITHUB_TOKEN"))}
     :body    (json/generate-string {"name" name})}))

;; creates a new github repo with the name of the current
;;directory
(->>
  (cwd)
  create-github-repo!)
