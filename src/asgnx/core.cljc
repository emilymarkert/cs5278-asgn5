(ns asgnx.core
  (:require [clojure.string :as string]
            [clojure.core.async :as async :refer [go chan <! >!]]
            [asgnx.kvstore :as kvstore
             :refer [put! get! list! remove!]]))

; Stores the dates, times, and outfits for recruitment rounds
(def rounds {
                         "PPD" {
                                          :date "11/11/2018"
                                          :time "11 a.m."
                                          :clothing "Recruitment shirt, medium/dark-wash jeans, white sneakers, and silver jewelry"}
                         "DISPLAY" {
                                          :date "01/05/2019"
                                          :time "11 a.m."
                                          :clothing "Recruitment shirt, medium/dark-wash jeans, white sneakers, and silver jewelry"}
                         "PHILANTHROPY" {
                                          :date "01/06/2019"
                                          :time "1 p.m."
                                          :clothing "Philanthropy shirt, denim skirt, brown booties, and gold jewelry"}

                         "SISTERHOOD" {
                                           :date "01/12/2019"
                                           :time "5 p.m."
                                           :clothing "Cobalt blue dress, nude heels or wedges, and silver jewelry"}
                         "PREF" {
                                           :date "01/13/2019"
                                           :time "7 p.m."
                                           :clothing "White pin-attire dress, nude heels, pin, gold jewelry"}
                         "BID DAY" {
                                            :date "01/14/2019"
                                            :time "1 p.m."
                                            :clothing "Bid Day shirt and anything to match our theme"}})

; Stores the dates and times for recruitment workshops
(def workshops {
                "SPEECH PRACTICE" {
                                   :date "10/28/2018"
                                   :time "5 p.m."}
                "PPD PREP" {
                                    :date "11/04/2018"
                                    :time "5 p.m."}
                "FEEDBACK" {
                                    :date "11/26/2018"
                                    :time "6 p.m."}
                "MOCK RECRUTMENT" {
                                    :date "12/02/2018"
                                    :time "5 p.m."}
                "SPIRIT NIGHT 1" {
                                    :date "01/03/2019"
                                    :time "5 p.m."}
                "SPIRIT NIGHT 2" {
                                    :date "01/04/2019"
                                    :time "5 p.m."}})

; Stores links to popular resources
(def links {
            "WORKSHOP SLIDES" "https://drive.google.com/slides"
            "EXCUSES" "https://docs.google.com/excuses"
            "OUTFIT SLIDES" "https://docs.google.com/outfits"
            "DATES" "https://docs.google.com/dates"})

; Stores current officers
(def officers [
               "+17086741936"])


;; This is a helper function that you might want to use to implement
;; `cmd` and `args`.
(defn words [msg]
  (if msg
      (string/split msg #" ")
      []))

;; Asgn 1.
;;
;; @Todo: Fill in this function to return the first word in a text
;; message.
;;
;; Example: (cmd "foo bar") => "foo"
;;
;; See the cmd-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn cmd [msg]
  (first (words msg)))

;; Asgn 1.
;;
;; @Todo: Fill in this function to return the list of words following
;; the command in a text message.
;;
;; Example: (args "foo bar baz") => ("bar" "baz")
;;
;; See the args-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn args [msg]
  (rest (words msg)))

;; Asgn 1.
;;
;; @Todo: Fill in this function to return a map with keys for the
;; :cmd and :args parsed from the msg.
;;
;; Example:
;;
;; (parsed-msg "foo bar baz") => {:cmd "foo" :args ["bar" "baz"]}
;;
;; See the parsed-msg-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn parsed-msg [msg]
  {:cmd (cmd msg) :args (args msg)})

;; Formats the date and time of an event into a string message
;; Parameters:
;;    event: the name of the event
;;    date: the date of the event
;;    time: the time of the event
(defn format-date [event date time]
  (str event " is on " date " at " time))

;; Formats the outfit for an event into a string message
;; Parameters:
;;    event: the name of the event
;;    outfit: the required attire for the event
(defn format-clothing [event outfit]
  (str "For " event ", you need to wear: " outfit))


;; Asgn 2.
;;
;; @Todo: Create a function called action-send-msg that takes
;; a destination for the msg in a parameter called `to`
;; and the message in a parameter called `msg` and returns
;; a map with the keys :to and :msg bound to each parameter.
;; The map should also have the key :action bound to the value
;; :send.
;;
(defn action-send-msg [to msg]
  {:to to :msg msg :action :send})


;; Asgn 2.
;;
;; @Todo: Create a function called action-send-msgs that takes
;; takes a list of people to receive a message in a `people`
;; parameter and a message to send them in a `msg` parmaeter
;; and returns a list produced by invoking the above `action-send-msg`
;; function on each person in the people list.
;;
;; java-like pseudo code:
;;
;; output = new list
;; for person in people:
;;   output.add( action-send-msg(person, msg) )
;; return output
;;
(defn action-send-msgs [people msg]
  (into [] (map action-send-msg people (repeat msg))))

;; Asgn 2.
;;
;; @Todo: Create a function called action-insert that takes
;; a list of keys in a `ks` parameter, a value to bind to that
;; key path to in a `v` parameter, and returns a map with
;; the key :ks bound to the `ks` parameter value and the key :v
;; vound to the `v` parameter value.)
;; The map should also have the key :action bound to the value
;; :assoc-in.
;;
(defn action-insert [ks v]
  {:action :assoc-in :ks ks :v v})

;; Asgn 2.
;;
;; @Todo: Create a function called action-inserts that takes:
;; 1. a key prefix (e.g., [:a :b])
;; 2. a list of suffixes for the key (e.g., [:c :d])
;; 3. a value to bind
;;
;; and calls (action-insert combined-key value) for each possible
;; combined-key that can be produced by appending one of the suffixes
;; to the prefix.
;;
;; In other words, this invocation:
;;
;; (action-inserts [:foo :bar] [:a :b :c] 32)
;;
;; would be equivalent to this:
;;
;; [(action-insert [:foo :bar :a] 32)
;;  (action-insert [:foo :bar :b] 32)
;;  (action-insert [:foo :bar :c] 32)]
;;
(defn action-inserts [prefix ks v]
  (into [] (map action-insert
            (into [] (map concat (repeat prefix) (map vector ks)))
            (repeat v))))

;; Asgn 2.
;;
;; @Todo: Create a function called action-remove that takes
;; a list of keys in a `ks` parameter and returns a map with
;; the key :ks bound to the `ks` parameter value.
;; The map should also have the key :action bound to the value
;; :dissoc-in.
;;
(defn action-remove [ks]
  {:action :dissoc-in :ks ks})

;; Formats a question into a string message that can be sent to officers
;; Parameters:
;;    officers: a list of officers that can answer questions
;;    question-words: the question as a list
(defn officers-question-msg [officers question-words]
  (str "Asking " (count officers) " officer(s) for an answer to: \""
       (string/join " " question-words) "\""))

;; Returns the required format for submitting excuses
;; Implements the functionality of the excuses command
(defn excuses-format [_]
  "Excuses need to be sent to email A, email B, and email C. Send 'link excuses' for excuse deadlines.")

;; Sends the last question a user asked to officers
;; Implements the functionality of the ask command
;; Parameters:
;;    conversation: the user's current conversation
;;    args: the user's question (should be an event name)
;;    user-id: the id of the user asking the question
(defn ask-officers [conversation {:keys [args user-id]}]
  (if (empty? officers)
    [[] "There are no officers available"]
   ;(let [question (get-in :conversations [user-id :last-question])]
   (let [question (:last-question conversation)]
    (if (not (empty? question))
      [(concat (action-send-msgs officers (string/join " " question))
        (action-inserts [:conversations] officers
                        {:last-question question :asker user-id}))
       (officers-question-msg officers question)]
      [[] "You haven't asked a question yet."]))))

;; Looks up the date of an event, if it exists
;; Implements the functionality of the date command
;; Parameters:
;;    conversation: the user's current conversation
;;    args: the user's question (should be an event name)
;;    user-id: the id of the user asking the question
(defn find-date [conversation {:keys [args user-id]}]
 (let [event (string/upper-case (string/join " " args))]
   [[(action-insert [:conversations user-id]
                   {:last-question (cons "date" args) :asker user-id})]
    (if (some? (find rounds event))
      (format-date
       event
       (:date (get rounds event))
       (:time (get rounds event)))
      (if (some? (find workshops event))
         (format-date
          (str event " Workshop")
          (:date (get workshops event))
          (:time (get workshops event)))
         (str event " is not a valid event. Try another event, or send 'ask' to pass your last question along to an officer.")))]))

;; Looks up the outfit for an event, if it exists
;; Implements the functionality of the outfit command
;; Parameters:
;;    conversation: the user's current conversation
;;    args: the user's question (should be an event name)
;;    user-id: the id of the user asking the question
(defn find-outfit [conversation {:keys [args user-id]}]
  (let [event (string/upper-case (string/join " " args))]
    [[(action-insert [:conversations user-id] {:last-question (cons "outfit" args) :asker user-id})]
     (if (some? (find rounds event))
       (format-clothing
         event
         (get-in rounds [event :clothing]))
       (if (some? (find workshops event))
         (str "You can wear whatever you want for the " event  " Workshop.")
         (str event " is not a valid event. Try another event, or send 'ask' to pass your last question along to an officer.")))]))

;; Looks up the link for a resources, if it exists
;; Implements the functionality of the link command
;; Parameters:
;;    conversation: the user's current conversation
;;    args: the user's question (should be an event name)
;;    user-id: the id of the user asking the question
(defn find-link [conversation {:keys [args user-id]}]
  (let [target (string/upper-case (string/join " " args))]
    [[(action-insert [:conversations user-id] {:last-question (cons "link" args) :asker user-id})]
     (if (some? (find links target))
       (get links target)
       (str target " is not a valid resource. Try another name, or send 'ask' to pass your last question along to an officer."))]))

;; Asgn 3.
;;
;; @Todo: Create a function called "answer-question"
;; that takes two parameters:
;;
;; 1. the last conversation describing the last question that was routed
;;    to the officer
;; 2. a parsed message with the format:
;;    {:cmd "ask"
;;     :user-id "+15555555555"
;;     :args [topic answer-word1 answer-word2 ... answer-wordN]}
;;
;; The parsed message is generated by breaking up the words in the ask
;; text message. For example, if someone sent the message:
;;
;; "answer joey's house of pizza"
;;
;; The conversation will be data that you store as a side-effect in
;; ask-officers. You probably want this data to be information about the
;; last question asked to each officer. See the "think about" comment above.
;;
;; The parsed message would be:
;;
;; {:cmd "answer"
;;  :user-id "+15555555555"
;;  :args ["joey's" "house" "of" "pizza"]}
;;
;; This function needs to return a list with two elements:
;; [[actions...] "response to officer answering"]
;;
;; The actions in the list are the *side effects* that need to take place
;; to send the answer to the original question asker. The string
;; is the response that is going to be sent back to the officer answering
;; the question.
;;
;; Think about how you are going to figure out where to route messages
;; when an officer answers (see the conversations query) and make sure you
;; handle the needed side effect for storing the conversation state.
;;
;; Why this strange architecture? By returning a list of the actions to take,
;; rather than directly taking that action, we can keep this function pure.
;; Pure functions are WAY easier to test / maintain. Also, we can isolate our
;; messy impure action handling at the "edges" of the application, where it is
;; easier to track and reason about.
;;
;; You should look at `handle-message` to get an idea of the way that this
;; function is going to be used, its expected signature, and how the actions
;; and output are going to work.
;;
;; See the integration test in See handle-message-test for the
;; expectations on how your code operates
;;
(defn answer-question [conversation {:keys [args]}]
  (if (empty? args)
   [[] "You did not provide an answer."]
   (if (empty? conversation)
     [[] "You haven't been asked a question."]
     [[(action-send-msg (:asker conversation) (string/join " " args))
       (action-remove [conversation])]
      "Your answer was sent."])))


;; Don't edit!
(defn stateless [f]
  (fn [_ & args]
    [[] (apply f args)]))


(def routes {"default"  (stateless (fn [& args] "Unknown command."))
             "excuses"   (stateless excuses-format)
             "ask"      ask-officers
             "answer"   answer-question
             "date"     find-date
             "outfit"   find-outfit
             "link"     find-link})


;; Don't edit!
(defn conversations-for-user-query [state-mgr pmsg]
  (let [user-id (:user-id pmsg)]
    (get! state-mgr [:conversations user-id])))

;; Don't edit!
(def queries
  {"ask"    conversations-for-user-query
   "answer" conversations-for-user-query
   "date"   conversations-for-user-query
   "outfit" conversations-for-user-query
   "link"   conversations-for-user-query})


;; Don't edit!
(defn read-state [state-mgr pmsg]
  (go
    (if-let [qfn (get queries (:cmd pmsg))]
      (<! (qfn state-mgr pmsg))
      {})))


;; Asgn 1.
;;
;; @Todo: This function should return a function (<== pay attention to the
;; return type) that takes a parsed message as input and returns the
;; function in the `routes` map that is associated with a key matching
;; the `:cmd` in the parsed message. The returned function would return
;; `welcome` if invoked with `{:cmd "welcome"}`.
;;
;; Example:
;;
;; (let [msg {:cmd "welcome" :args ["bob"]}]
;;   (((create-router {"welcome" welcome}) msg) msg) => "Welcome bob"
;;
;; If there isn't a function in the routes map that is mapped to a
;; corresponding key for the command, you should return the function
;; mapped to the key "default".
;;
;; See the create-router-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn create-router [routes]
 (fn [msg]
  (if (some? (find routes (get msg :cmd)))
    (get routes (get msg :cmd))
    (get routes "default"))))

;; Don't edit!
(defn output [o]
  (second o))


;; Don't edit!
(defn actions [o]
  (first o))


;; Don't edit!
(defn invoke [{:keys [effect-handlers] :as system} e]
  (go
    (println "    Invoke:" e)
    (if-let [action (get effect-handlers (:action e))]
      (do
        (println "    Invoking:" action "with" e)
        (<! (action system e))))))


;; Don't edit!
(defn process-actions [system actions]
  (go
    (println "  Processing actions:" actions)
    (let [results (atom [])]
      (doseq [action actions]
        (let [result (<! (invoke system action))]
          (swap! results conj result)))
      @results)))


;; Don't edit!
(defn handle-message
  "
    This function orchestrates the processing of incoming messages
    and glues all of the pieces of the processing pipeline together.

    The basic flow to handle a message is as follows:

    1. Create the router that will be used later to find the
       function to handle the message
    2. Parse the message
    3. Load any saved state that is going to be needed to process
       the message (e.g., querying the list of officers, etc.)
    4. Find the function that can handle the message
    5. Call the handler function with the state from #3 and
       the message
    6. Run the different actions that the handler returned...these actions
       will be bound to different implementations depending on the environemnt
       (e.g., in test, the actions aren't going to send real text messages)
    7. Return the string response to the message

  "
  [{:keys [state-mgr] :as system} src msg]
  (go
    (println "=========================================")
    (println "  Processing:\"" msg "\" from" src)
    (let [rtr    (create-router routes)
          _      (println "  Router:" rtr)
          pmsg   (assoc (parsed-msg msg) :user-id src)
          _      (println "  Parsed msg:" pmsg)
          state  (<! (read-state state-mgr pmsg))
          _      (println "  Read state:" state)
          hdlr   (rtr pmsg)
          _      (println "  Hdlr:" hdlr)
          [as o] (hdlr state pmsg)
          _      (println "  Hdlr result:" [as o])
          arslt  (<! (process-actions system as))
          _      (println "  Action results:" arslt)]
      (println "=========================================")
      o)))
