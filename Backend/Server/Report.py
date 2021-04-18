import matplotlib.pyplot as plt
import pandas as pd
import firebase_admin
import os
import numpy as np
from firebase_admin import db
import math
import pdfkit
from flask import render_template
def colour_calc(col_data):
    no_of_days_played=len(col_data.keys())
    points_aggr={
        "5":5,
        "4":4.5,
        "3":4,
        "2":2,
        "1":1,
    }
    points_arr=[]
    is_decreasing=0
    for i in col_data.keys():
        points_arr.append(points_aggr[str(col_data[i])])
    derivative=(points_arr[0]-points_arr[-1])/7
    if derivative<-1:
        is_decreasing=1
    else:
        is_decreasing=0
    cum_derv=[]
    col_data_keys=list(col_data.keys())
    print(col_data_keys)
    for i in range(1,len(col_data_keys)):
        d=(points_arr[i]-points_arr[i-1])
        cum_derv.append(d)

    count=0
    ignore=0
    for i in range(1,len(cum_derv)):
        if cum_derv[i]>cum_derv[i-1]:
            count+=1
    if count >=3:
        ignore=1
    else:
        ignore=0
    return ignore,is_decreasing,points_arr
def do_line_plot(t,points,l_label,p_label,ukey):
    plt.figure(figsize=(14, 6), dpi=80)
    plt.title("Points Aggregation - "+t)
    plt.plot(range(1,len(points)+1),points,'g-',label=l_label)
    plt.plot(range(1,len(points)+1),points,'ro',label=p_label)
    plt.xlabel("Days")
    plt.ylabel("Points Earned")
    plt.legend(loc="lower right")
    try:
        os.mkdir(os.path.join('C:/Users/Asus/Desktop/quiz_app/Report','graphs',ukey))
    except:
        pass
    plt.savefig(os.path.join('C:/Users/Asus/Desktop/quiz_app/Report','graphs',ukey,t+"_line_plot.jpg"))
def map_calc(map_data):
    no_of_days_played=len(map_data.keys())

    points_arr=[]
    is_decreasing=0
    for i in map_data.keys():
        points_arr.append(int(map_data[i]))
    derivative=(points_arr[0]-points_arr[-1])/7
    if derivative<-1:
        is_decreasing=1
    else:
        is_decreasing=0
    cum_derv=[]
    map_data_keys=list(map_data.keys())
    for i in range(1,len(map_data_keys)):
        d=(points_arr[i]-points_arr[i-1])
        cum_derv.append(d)
    print(cum_derv)
    count=0
    ignore=0
    for i in range(1,len(cum_derv)):
        if cum_derv[i]>cum_derv[i-1]:
            count+=1
    if count >=3:
        ignore=1
    else:
        ignore=0
    return ignore,is_decreasing,points_arr
def word_calc(word_data):
    no_of_days_played=len(word_data.keys())

    points_arr=[]
    is_decreasing=0
    for i in word_data.keys():
        points_arr.append(int(word_data[i]))
    derivative=(points_arr[0]-points_arr[-1])/7
    if derivative<-1:
        is_decreasing=1
    else:
        is_decreasing=0
    cum_derv=[]
    word_data_keys=list(word_data.keys())
    for i in range(1,len(word_data_keys)):
        d=(points_arr[i]-points_arr[i-1])
        cum_derv.append(d)
    count=0
    ignore=0
    for i in range(1,len(cum_derv)):
        if cum_derv[i]>cum_derv[i-1]:
            count+=1
    if count >=3:
        ignore=1
    else:
        ignore=0
    return ignore,is_decreasing,points_arr
def cal_fin_points(c_i,c_d,c_p,m_i,m_d,m_p,w_i,w_d,w_p):

    if c_i==0:
        col_portion=0.25*(1-c_d)*(sum(c_p)/len(c_p))
    else:
        col_portion=0.25*(sum(c_p)/len(c_p))
    if m_i==0:
        map_portion=0.35*(1-m_d)*(sum(m_p)/len(m_p))
    else:
        col_portion=0.35*(sum(m_p)/len(m_p))
    if w_i==0:
        word_portion=0.40*(1-w_d)*(sum(w_p)/len(w_p))
    else:
        word_portion=0.40*w_d*(sum(w_p)/len(w_p))

    total=col_portion+map_portion+word_portion
    return col_portion,map_portion,word_portion,total
def analysis(ukey):
    plt.ioff()
    cred_obj = firebase_admin.credentials.Certificate(r"C:\Users\Asus\Desktop\quiz_app\Firebase_python\alzimers-firebase-firebase-adminsdk-6fm3e-49738bb51b.json")
    try:
        default_app = firebase_admin.initialize_app(cred_obj, {'databaseURL':"https://alzimers-firebase-default-rtdb.firebaseio.com/"})
    except:
        pass
    data=db.reference("/DATA").child(ukey).get()
    col_ignore,col_is_decreasing,col_points=colour_calc(data["colour_quiz"])
    do_line_plot("Colour_Game",col_points,"Flow Of Points","Points For the Day",ukey)
    map_ignore,map_is_decreasing,map_points=map_calc(data["map_quiz"])
    do_line_plot("Map_Game",map_points,"Flow Of Points","Points For the Day",ukey)
    word_ignore,word_is_decreasing,word_points=word_calc(data["word_quiz"])
    do_line_plot("Word_Game",word_points,"Flow Of Points","Points For the Day",ukey)
    col_portion,map_portion,word_portion,total=cal_fin_points(col_ignore,col_is_decreasing,col_points,map_ignore,map_is_decreasing,map_points,word_ignore,word_is_decreasing,word_points)
    do_monthly=upload_to_firebase(col_portion,map_portion,word_portion,total,ukey)
    return col_portion,map_portion,word_portion,total
def upload_to_firebase(col_portion,map_portion,word_portion,total,ukey):
    to_send_monthly_report=0
    cred_obj = firebase_admin.credentials.Certificate(r"C:\Users\Asus\Desktop\quiz_app\Firebase_python\alzimers-firebase-firebase-adminsdk-6fm3e-49738bb51b.json")
    try:
        default_app = firebase_admin.initialize_app(cred_obj, {'databaseURL':"https://alzimers-firebase-default-rtdb.firebaseio.com/"})
    except:
        pass
    ref=db.reference("/MONTHLY").child(ukey)
    data=ref.get()
    upload_data={
        "Colour_Agg":col_portion,
        "Map_Agg":map_portion,
        "Word_Agg":word_portion,
        "Total_Agg":total
    }
    if data==None:
        up_ref=db.reference("/MONTHLY").child(ukey).child("Week_1")
        up_ref.set(upload_data)
    else:
        weeks=[int(s.split("_")[1]) for s in data.keys()]
        m=max(weeks)
        data["Week_"+str(m+1)]=upload_data
        ref.set(data)
        if m+1==4:
            to_send_monthly_report=1
    return to_send_monthly_report
