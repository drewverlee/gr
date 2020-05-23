#!/usr/bin/env bb

(ns gr
  (:require [clojure.string :as str]
            [babashka.curl :as curl]
            [cheshire.core :as json]
            [clojure.java.shell :as shell]
            [clojure.java.io :as io]))

(def repo (-> "git"
            (shell/sh "rev-parse" "--show-toplevel")
            :out
            (str/split #"/")
            last
            (str/trim)))

(def cmd (str "git remote add origin git@github.com:drewverlee/" repo ".git"))

(defn create-github-repo!
  [name]
  (curl/post "https://api.github.com/user/repos"
    {:headers {"Authorization" (str "token " (System/getenv "GITHUB_TOKEN"))}
     :body    (json/generate-string {"name" name})}))

(create-github-repo! repo)
(println cmd)

(when *command-line-args*
  (println "adding remote!")
  (apply shell/sh (str/split cmd #" ")))
