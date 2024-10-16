(ns electionscaling.core
  (:gen-class :main true)
  (:require [conexp.fca.contexts :refer :all]
            [conexp.fca.many-valued-contexts :refer :all]
            [conexp.io.contexts :refer [write-context]]
            [clojure.data.json :as json]
            [clojure.java.io :as io]
            [clojure.tools.cli :refer [parse-opts]]))

(defn download-context [address]
  (let [answers-json (json/read-str (slurp (str "https://raw.githubusercontent.com/gockelhahn/qual-o-mat-data/master/data/" 
                                                address 
                                                "/answer.json")))
        party-json (json/read-str (slurp (str "https://raw.githubusercontent.com/gockelhahn/qual-o-mat-data/master/data/" 
                                              address 
                                              "/party.json")))
        statement-json (json/read-str (slurp (str "https://raw.githubusercontent.com/gockelhahn/qual-o-mat-data/master/data/" 
                                                  address 
                                                  "/statement.json")))
        opinion-json (json/read-str (slurp (str "https://raw.githubusercontent.com/gockelhahn/qual-o-mat-data/master/data/" 
                                                address 
                                                "/opinion.json")))
        answers (into {} (for [x answers-json] [(x "id") (x "message")]))
        party (into {} (for [x party-json] [(x "id") (x "name")]))
        statement (into {} (for [x statement-json] [(x "id") (x "label")]))
        incidence (for [x opinion-json] [(party (x "party")) (statement (x "statement")) (answers (x "answer"))])]

    (make-mv-context (vals party) (vals statement) incidence))

)

(def vote-scale (ordinal-scale #{"Stimme nicht zu" "Neutral" "Stimme zu"} 
                               #(or (= %2 %1) 
                                    (.contains #{["Stimme nicht zu" "Neutral"] ["Stimme nicht zu" "Stimme zu"]
                                                 ["Neutral" "Stimme zu"]}
                                               [%2 %1]))))

(defn- list-dir [path]
  (map #(.getCanonicalPath %) (.listFiles (java.io.File. path)))
)

(defn save-scaled-context [path]

  (let [answers-json (json/read-str (slurp (str path "/answer.json")))
        party-json (json/read-str (slurp (str path  "/party.json")))
        statement-json (json/read-str (slurp (str path "/statement.json")))
        opinion-json (json/read-str (slurp (str path "/opinion.json")))

        answers (into {} (for [x answers-json] [(x "id") (x "message")]))
        party (into {} (for [x party-json] [(x "id") (x "name")]))
        statement (into {} (for [x statement-json] [(x "id") (x "label")]))
        incidence (for [x opinion-json] [(party (x "party")) (statement (x "statement")) (answers (x "answer"))])

        mv-ctx (make-mv-context (vals party) (vals statement) incidence)
        blank(into {} (for [x (attributes mv-ctx)] [x vote-scale]))

        scaled-ctx (scale-mv-context mv-ctx (into {} (for [x (attributes mv-ctx)] [x vote-scale])))
        name (take-last 2 (clojure.string/split path #"/"))]

    (println name)
    (write-context :burmeister scaled-ctx (str "qual-o-mat-data/scaled/"
                                               (first name) 
                                               "_" 
                                               (second name) 
                                               ".ctx")))
)

(defn convert-all [] 
  (let [path "qual-o-mat-data/data"
        years (list-dir path)
        elections (flatten (for [year years] (list-dir year)))]
    (doall (map #(save-scaled-context %) elections)))
)



(defn -main [& args]
  (try 
    (convert-all)
    (catch Exception e (println (str "caught exception: " (.getMessage e))))))
