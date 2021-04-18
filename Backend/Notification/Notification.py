import firebase_admin
from firebase_admin import db
import os
from pyfcm import FCMNotification
# Init firebase with your credentials
cred_object = firebase_admin.credentials.Certificate("alzimers-firebase-firebase-adminsdk-6fm3e-49738bb51b.json")
default_app = firebase_admin.initialize_app(cred_object, {
	'databaseURL':"https://alzimers-firebase-default-rtdb.firebaseio.com/"
	})
ref =db.reference("/TOKEN")
tokens=ref.get()
count_data=db.reference("/COUNT").get()

push_service = FCMNotification(api_key="AAAAwdxJDjc:APA91bEIb81-mdYBospdA2uWdQFMUBnJC68X83zhcq5qZTA1CGfIQZnR0drW_jfnqjfXKWbZs8lwWr8EzdiLbTG2fZYTTizr0g28WUYaNfe3ugQrpT71JcDi8WRe4aE4v9c53idtjMJT")



for token in tokens.keys():
    registration_id = token
    message_title = "Time To Test Yourself"
    message_body = "Day"+str(count_data[tokens[token]])+"! "+str(7-count_data[tokens[token]])+" days left Go on to Play the game to make sure you are ok!...prevention is better than cure"
    result = push_service.notify_single_device(registration_id=registration_id, message_title=message_title, message_body=message_body)

    col_data=db.reference("/DATA").child(tokens[token]).child("colour_quiz").get()
    c_days=[s.split("_")[1] for s in col_data.keys()]
    cm=max(c_days)
    map_data=db.reference("/DATA").child(tokens[token]).child("map_quiz").get()
    m_days=[s.split("_")[1] for s in map_data.keys()]
    mm=max(m_days)
    word_data=db.reference("/DATA").child(tokens[token]).child("word_quiz").get()
    w_days=[s.split("_")[1] for s in word_data.keys()]
    wm=max(w_days)

    message_title = "Analytics"
    message_body = "Word Quiz Completed:{} Left :{} \n Map Quiz Completed:{} Left :{} \n Colour Quiz Completed:{} Left :{} \n".format(wm,7-int(wm),mm,7-int(mm),cm,7-int(cm))
    result = push_service.notify_single_device(registration_id=registration_id, message_title=message_title, message_body=message_body)
print(result)
