(ns asgnx.core-test
  (:require [clojure.test :refer :all]
            [clojure.core.async :refer [<!!]]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [clojure.test.check.generators :as gen]
            [asgnx.core :refer :all]
            [asgnx.kvstore :as kvstore :refer [put! get!]]))



(deftest words-test
  (testing "that sentences can be split into their constituent words"
    (is (= ["a" "b" "c"] (words "a b c")))
    (is (= [] (words "   ")))
    (is (= [] (words nil)))
    (is (= ["a"] (words "a")))
    (is (= ["a"] (words "a ")))
    (is (= ["a" "b"] (words "a b")))))


(deftest cmd-test
  (testing "that commands can be parsed from text messages"
    (is (= "foo" (cmd "foo")))
    (is (= "foo" (cmd "foo x y")))
    (is (= nil   (cmd nil)))
    (is (= ""    (cmd "")))))


(deftest args-test
  (testing "that arguments can be parsed from text messages"
    (is (= ["x" "y"] (args "foo x y")))
    (is (= ["x"] (args "foo x")))
    (is (= [] (args "foo")))
    (is (= [] (args nil)))))


(deftest parsed-msg-test
  (testing "that text messages can be parsed into cmd/args data structures"
    (is (= {:cmd "foo"
            :args ["x" "y"]}
           (parsed-msg "foo x y")))
    (is (= {:cmd "foo"
            :args ["x"]}
           (parsed-msg "foo x")))
    (is (= {:cmd "foo"
            :args []}
           (parsed-msg "foo")))
    (is (= {:cmd "foo"
            :args ["x" "y" "z" "somereallylongthing"]}
           (parsed-msg "foo x y z somereallylongthing")))))


(deftest format-date-test
  (testing "that dates are correctly formatted"
    (is (= "PPD is on 11/11/2018 at 11 a.m." (format-date "PPD" "11/11/2018" "11 a.m.")))
    (is (= "Speech Practice Workshop is on 10/28/2018 at 5 p.m." (format-date "Speech Practice Workshop" "10/28/2018" "5 p.m.")))))


(deftest format-clothing-test
  (testing "that outfits are correctly formatted"
    (is (= "For PPD, you need to wear: Recruitment shirt, medium/dark-wash jeans, white sneakers, and silver jewelry"
           (format-clothing "PPD" "Recruitment shirt, medium/dark-wash jeans, white sneakers, and silver jewelry")))))


(deftest date-test
  (testing "that dates are correctly looked up, formatted, and returned in a string"
    (is (= "PPD is on 11/11/2018 at 11 a.m." (last (find-date {} {:cmd "date" :args ["PPD"]}))))
    (is (= "DISPLAY is on 01/05/2019 at 11 a.m." (last (find-date {} {:cmd "date" :args ["Display"]}))))
    (is (= "DISPLAY is on 01/05/2019 at 11 a.m." (last (find-date {} {:cmd "date" :args ["display"]}))))
    (is (= "PHILANTHROPY is on 01/06/2019 at 1 p.m." (last (find-date {} {:cmd "date" :args ["Philanthropy"]}))))
    (is (= "SISTERHOOD is on 01/12/2019 at 5 p.m." (last (find-date {} {:cmd "date" :args ["Sisterhood"]}))))
    (is (= "PREF is on 01/13/2019 at 7 p.m." (last (find-date {} {:cmd "date" :args ["Pref"]}))))
    (is (= "BID DAY is on 01/14/2019 at 1 p.m." (last (find-date {} {:cmd "date" :args ["Bid Day"]}))))
    (is (= "SPEECH PRACTICE Workshop is on 10/28/2018 at 5 p.m." (last (find-date {} {:cmd "date" :args ["Speech Practice"]}))))
    (is (= "RANDOM is not a valid event. Try another event, or send 'ask' to pass your last question along to an officer." (last (find-date {} {:cmd "date" :args ["Random"]}))))))


(deftest outfit-test
  (testing "that outfits are looked up, formatted and returned correctly"
    (is (= "For PPD, you need to wear: Recruitment shirt, medium/dark-wash jeans, white sneakers, and silver jewelry"
           (last (find-outfit {} {:cmd "outfit" :args ["PPD"]}))))
    (is (= "RANDOM is not a valid event. Try another event, or send 'ask' to pass your last question along to an officer." (last (find-outfit {} {:cmd "date" :args ["Random"]}))))))

(deftest link-test(testing "that links are looked up correctly")
    (is (= "https://drive.google.com/slides" (last (find-link {} {:cmd "link" :args ["workshop slides"]})))))

(deftest excuses-test
  (testing "that the formate for excuses is looked up and returned correctly"
    (is (= "Excuses need to be sent to email A, email B, and email C. Send 'link excuses' for excuse deadlines." (excuses-format {})))))

(deftest create-router-test
  (testing "correct creation of a function to lookup a handler for a parsed message"
    (let [router (create-router {"hello" #(str (:cmd %) " " "test")
                                 "argc"  #(count (:args %))
                                 "echo"  identity
                                 "default" (fn [& a] "No!")})
          msg1   {:cmd "hello"}
          msg2   {:cmd "argc" :args [1 2 3]}
          msg3   {:cmd "echo" :args ["a" "z"]}
          msg4   {:cmd "echo2" :args ["a" "z"]}]
      (is (= "hello test" ((router msg1) msg1)))
      (is (= "No!" ((router msg4) msg4)))
      (is (= 3 ((router msg2) msg2)))
      (is (= msg3 ((router msg3) msg3))))))

(deftest action-send-msg-test
  (testing "That action send msg returns a correctly formatted map"
    (is (= :send
           (:action (action-send-msg :bob "foo"))))
    (is (= :bob
           (:to (action-send-msg :bob "foo"))))
    (is (= "foo"
           (:msg (action-send-msg [:a :b] "foo"))))))


(deftest action-send-msgs-test
  (testing "That action send msgs generates a list of sends"
    (let [a (action-send-msg [:a :f :b] 1)
          b (action-send-msg [:a :f :d] 1)
          c (action-send-msg [:a :f :e] 1)
          d (action-send-msg [:a :f :c] 1)]
      (is (= [a b c d]
             (action-send-msgs [[:a :f :b]
                                [:a :f :d]
                                [:a :f :e]
                                [:a :f :c]]
                              1))))))

(deftest action-insert-test
  (testing "That action insert returns a correctly formatted map"
    (is (= #{:action :ks :v}
           (into #{}(keys (action-insert [:a :b] {:foo 1})))))
    (is (= #{:assoc-in [:a :b] {:foo 1}}
           (into #{}(vals (action-insert [:a :b] {:foo 1})))))
    (is (= :assoc-in
           (:action (action-insert [:a :b] {:foo 1}))))
    (is (= {:foo 1}
           (:v (action-insert [:a :b] {:foo 1}))))
    (is (= [:a :b]
           (:ks (action-insert [:a :b] {:foo 1}))))))


(deftest action-remove-test
  (testing "That action remove returns a correctly formatted map"
    (is (= #{:action :ks}
         (into #{} (keys (action-remove [:a :b])))))
    (is (= #{:dissoc-in [:a :b]}
          (into #{}(vals (action-remove [:a :b])))))
    (is (= :dissoc-in
           (:action (action-remove [:a :b]))))
    (is (= [:a :b]
           (:ks (action-remove [:a :b]))))))


(deftest action-inserts-test
  (testing "That action inserts generates a list of inserts"
    (let [a (action-insert [:a :f :b] 1)
          b (action-insert [:a :f :d] 1)
          c (action-insert [:a :f :e] 1)
          d (action-insert [:a :f :c] 1)]
      (is (= [a b c d]
             (action-inserts [:a :f] [:b :d :e :c] 1))))))


(defn action-send [system {:keys [to msg]}]
  (put! (:state-mgr system) [:msgs to] msg))

(defn pending-send-msgs [system to]
  (get! (:state-mgr system) [:msgs to]))

(def send-action-handlers
  {:send action-send})

(deftest handle-message-test
  (testing "the integration and handling of messages"
    (let [ehdlrs (merge
                   send-action-handlers
                   kvstore/action-handlers)
          state  (atom {})
          smgr   (kvstore/create state)
          system {:state-mgr smgr
                  :effect-handlers ehdlrs}]
      (is (= "You haven't been asked a question."
             (<!! (handle-message
                   system
                   "+17086741936"
                   "answer hello"))))
      (is (= "PPD is on 11/11/2018 at 11 a.m."
             (<!! (handle-message
                    system
                    "test-user"
                    "date ppd"))))
      (is (= "For PPD, you need to wear: Recruitment shirt, medium/dark-wash jeans, white sneakers, and silver jewelry"
             (<!! (handle-message
                    system
                    "test-user"
                    "outfit ppd"))))
      (is (= "Excuses need to be sent to email A, email B, and email C. Send 'link excuses' for excuse deadlines."
             (<!! (handle-message
                    system
                    "test-user"
                    "excuses"))))
      (is (= "https://docs.google.com/excuses"
             (<!! (handle-message
                    system
                    "test-user"
                    "link excuses"))))
      (is (= "TUESDAY is not a valid event. Try another event, or send 'ask' to pass your last question along to an officer."
             (<!! (handle-message
                    system
                    "test-user"
                    "outfit Tuesday"))))
      (is (= "Asking 1 officer(s) for an answer to: \"outfit Tuesday\""
             (<!! (handle-message
                    system
                    "test-user"
                    "ask"))))
      (is (= "outfit Tuesday"
             (<!! (pending-send-msgs system "+17086741936"))))
      (is (= "You did not provide an answer."
             (<!! (handle-message
                   system
                   "+17086741936"
                   "answer"))))
      (is (= "Your answer was sent."
             (<!! (handle-message
                   system
                   "test-user"
                   "answer Wear your Recruitment shirt!"))))
      (is (= "Wear your Recruitment shirt!"
             (<!! (pending-send-msgs system "test-user")))))))
