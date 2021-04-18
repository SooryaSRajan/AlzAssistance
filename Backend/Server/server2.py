from flask import Flask,jsonify,request,render_template
import os
import requests
import random
import pandas as pd
import firebase_admin
from firebase_admin import db
import json
from Report import analysis
import pdfkit
from datetime import date as datenow
from flask_mail import Mail, Message

app=Flask(__name__)
app.config['MAIL_SERVER'] = 'smtp.gmail.com'
app.config['MAIL_PORT'] = 587
app.config['MAIL_USERNAME'] = 'alzproj2021@gmail.com'
app.config['MAIL_PASSWORD'] = 'alzapp2021'
app.config['MAIL_USE_TLS'] = True
app.config['MAIL_USE_SSL'] = False
app.config['MAIL_DEFAULT_SENDER'] = ('Admin','alzproj2021@gmail.com')
mail = Mail()
mail.init_app(app)
def conv_dict(data):
    ret_data=[]
    for row in data:
        dict={}
        s= str(row[0].encode("ascii", errors="replace"))[2:-2]
        dict["question"]=s
        dict_opts={}
        print(row[1])
        for i in range(int(row[1])):
            dict_opts["Option"+str(i+1)]=row[2][i]
            if row[2][i]== row[3]:
                dict["correct_opt"]= i+1
        dict["options"]=dict_opts
        dict["no_of_options"]=row[1]
        ret_data.append(dict)
    return ret_data
def conv_dict_map(data,area):
    ret_data={}
    area_details=pd.read_json("areas.json")

    ad_np=area_details.to_numpy()
    for a in ad_np:
        if a[0]==area:
            ret_data["lat"]=a[1]
            ret_data["long"]=a[2]
            break
    loc=[]
    for l in data:
        di={"name":l[0],"lat":l[1],"long":l[2]}
        loc.append(di)
    ret_data["locations"]=loc
    return ret_data

@app.route("/get_questions_small", methods=["GET","POST"])
def questions_small():

    ukey=request.args.get("user_id")
    cred_obj = firebase_admin.credentials.Certificate(r"C:\Users\Asus\Desktop\quiz_app\Firebase_python\alzimers-firebase-firebase-adminsdk-6fm3e-49738bb51b.json")
    try:
        default_app = firebase_admin.initialize_app(cred_obj, {
        	'databaseURL':"https://alzimers-firebase-default-rtdb.firebaseio.com/"
        	})
    except:
        pass
    count=db.reference("/COUNT").child(ukey).get()
    ref = db.reference("/SMALL_POOL")
    if count==None:
        data=db.reference("/COUNT").get()
        data[ukey]=1
        r=db.reference("/COUNT")
        r.set(data)
    elif count==7:
        data=db.reference("/COUNT").get()
        data[ukey]=0
        r=db.reference("/COUNT")
        r.set(data)
        d=db.reference("/SMALL_POOL").get()
        database=pd.read_json("quiz_questions_new.json")
        #del database[database.keys()[0]]
        data=database.to_numpy()
        rand_ids=random.sample(range(0,len(data)-1),10)
        rand_rows_small=[data[i] for i in rand_ids]
        small_row_dict_conv=conv_dict(rand_rows_small)
        df={}
        for i in range(5):
            df["Question"+str(i+1)]=small_row_dict_conv[i]
        d[ukey]=df
        ref.set(d)
    else:
        data=db.reference("/COUNT").get()
        data[ukey]+=1
        r=db.reference("/COUNT")
        r.set(data)

    user_data=ref.child(ukey).get()

    if user_data==None:
        d=db.reference("/SMALL_POOL").get()
        if d==None:
            d={}
        database=pd.read_json("quiz_questions_new.json")
        #del database[database.keys()[0]]
        data=database.to_numpy()
        rand_ids=random.sample(range(0,len(data)-1),10)
        rand_rows_small=[data[i] for i in rand_ids]
        small_row_dict_conv=conv_dict(rand_rows_small)
        df={}
        for i in range(5):
            df["Question"+str(i+1)]=small_row_dict_conv[i]
        d[ukey]=df
        ref.set(d)
    user_data=ref.child(ukey).get()

    return jsonify(user_data)
@app.route("/get_questions_big", methods=["GET","POST"])
def questions_big():
    database=pd.read_json("quiz_questions_new.json")
    #del database[database.keys()[0]]
    data=database.to_numpy()
    rand_ids=random.sample(range(0,len(data)-1),5)
    rand_rows_big=[data[i] for i in rand_ids]
    small_row_dict_conv=conv_dict(rand_rows_big)

    return jsonify(small_row_dict_conv[0])

@app.route("/map_questions",methods=["GET","POST"])
def map_json():
    db=pd.read_json("location_2.json")
    data=db.to_numpy()
    areas=db["Area"].unique()
    flag=True
    fin_loc_list=[]

    rand_id=random.sample(range(0,areas.shape[0]-1),1)[0]
    area=areas[rand_id]
    loc_list=[]
    print(area==None)
    for d in data:
        if d[1]==area:
            print(d[1],area)
            loc_list.append([d[0],d[3],d[4]])
    rand_ids=random.sample(range(0,len(loc_list)),4)
    fin_loc_list=[loc_list[i] for i in rand_ids]

    out_dict=conv_dict_map(fin_loc_list,area)
    return jsonify(out_dict)
@app.route("/imgs_data",methods=["GET","POST"])
def imgs():
    data=pd.read_csv("col_data.csv")
    del data[data.keys()[0]]
    np_data=data.to_numpy()
    r=random.sample(range(0,np_data.shape[0]),1)
    s_tuples=[np_data[ra] for ra in r]

    dict={"Image_Name":s_tuples[0][0],"Blue":s_tuples[0][1],"Green":s_tuples[0][2],"Yellow":s_tuples[0][3],"Red":s_tuples[0][4],"Violet":s_tuples[0][5]}
    return jsonify(dict)
@app.route("/imgs_data_arr",methods=["GET","POST"])
def imgs_arr():
    data=pd.read_csv("col_data.csv")
    del data[data.keys()[0]]
    np_data=data.to_numpy()
    r=random.sample(range(0,np_data.shape[0]),175)
    s_tuples=[np_data[ra] for ra in r]
    conv_big_pool=[]
    for i in s_tuples[:150]:
        dict={"Image_Name":i[0],"Colour_Present":[i[1],i[2],i[3],i[4],i[5]]}
        conv_big_pool.append(dict)
    conv_small_pool=[]
    for i in s_tuples[150:]:
        dict={"Image_Name":i[0],"Colour_Present":[i[1],i[2],i[3],i[4],i[5]]}
        conv_small_pool.append(dict)
    out_dict={"Big_Pool":conv_big_pool,"Small_Pool":conv_small_pool}
    return out_dict
@app.route("/test_post_data",methods=["POST"])
def test():
    data=request.get_json()
    ty=data["type"]
    ukey=data["user_id"]
    ans=json.loads(data["answer"])
    c=0
    for i in ans:
        if i==True:
            c+=1
    cred_obj = firebase_admin.credentials.Certificate(r"C:\Users\Asus\Desktop\quiz_app\Firebase_python\alzimers-firebase-firebase-adminsdk-6fm3e-49738bb51b.json")
    try:
        default_app = firebase_admin.initialize_app(cred_obj, {
        	'databaseURL':"https://alzimers-firebase-default-rtdb.firebaseio.com/"
        	})
    except:
        pass
    req_ref=db.reference("/DATA").child(ukey).child(ty)
    present_data=req_ref.get()
    if present_data==None:
        present_data={"Day_1":c}
    else:
        days=[int(s.split("_")[1]) for s in present_data.keys()]
        max_day=max(days)
        present_data["Day_"+str(max_day+1)]=c
    req_ref.set(present_data)

    count=db.reference("/COUNT").child(ukey).get()
    if count==7:
        cp,mp,wp,total=analysis(ukey)
        name=db.reference("/USER").child(ukey).child("NAME").get()
        email=db.reference("/USER").child(ukey).child("EMAIL").get()
        gen_pdf(cp,mp,wp,ukey,name)
        mail_pdf(email)
        r_ref=db.reference("/DATA").child(ukey)
        r_ref.set({})
        small_ref=db.reference("/SMALL_POOL").child(ukey)
        small_ref.set({})
        count_data=db.reference("/COUNT").get()
        count_data[ukey]=0
        db.reference("/COUNT").set(count_data)
    print(type(ans[1]))

    return jsonify({"status":200})
@app.route("/emergency_hospital_location",methods=["GET","POST"])
def ehl():
    API_KEY='c27484f580f04e65af2c749d2a99d9b8'
    lat=request.args.get("lat")
    long=request.args.get("long")
    cat='healthcare.hospital'
    rad=10000
    req=requests.get("https://api.geoapify.com/v2/places?categories="+cat+"&filter=circle:"+str(long)+","+str(lat)+",10000&limit=20&apiKey="+API_KEY)
    x=json.loads(req.text)
    out=[]
    for i in range(len(x["features"])):
        d={}
        d["name"]=x["features"][i]["properties"]['name']
        d["lat"]=x["features"][i]["properties"]['lat']
        d["lon"]=x["features"][i]["properties"]['lon']
        out.append(d)
    return jsonify(out)

def gen_pdf(cp,mp,wp,ukey,name):
    today = datenow.today()
    date = today.strftime("%d/%m/%Y")
    time=today.strftime("%H:%M:%S")
    if cp<1:
        cp_t="You Should Focus On this Area"
    else:
        cp_t="You are Good in this Area"
    if mp<1:
        mp_t="You Should Focus On this Area"
    else:
        mp_t="You are Good in this Area"
    if wp<1:
        wp_t="You Should Focus On this Area"
    else:
        wp_t="You are Good in this Area"
    c_url=os.path.join("C:/Users/Asus/Desktop/quiz_app/Report/Graphs",ukey,"Colour_Game_line_plot.jpg")
    m_url=os.path.join("C:/Users/Asus/Desktop/quiz_app/Report/Graphs",ukey,"map_Game_line_plot.jpg")
    w_url=os.path.join("C:/Users/Asus/Desktop/quiz_app/Report/Graphs",ukey,"word_Game_line_plot.jpg")
    config = pdfkit.configuration(wkhtmltopdf=r'C:\Users\Asus\Desktop\Crop_Prediction_Using_Nutrients\flask_server\wkhtmltopdf\bin\\wkhtmltopdf.exe')
    rendered=render_template("report2.html",cp=cp,mp=mp,wp=wp,cp_t=cp_t,wp_t=wp_t,mp_t=mp_t,ukey=ukey,date=date,time=time,name=name,c_url=c_url,m_url=m_url,w_url=w_url)
    options = {
  "enable-local-file-access": None
}
    print(rendered)
    pdf = pdfkit.from_string(rendered ,"output.pdf",configuration=config,options=options)

def mail_pdf(email):

    msg = Message(
        'report',
        sender=('Admin','alzproj2021@gmail.com'),
        recipients=[email]
    )
    with app.open_resource("output.pdf") as pdf:
        msg.attach("output.pdf", 'application/pdf', pdf.read())

    mail.send(msg)
app.run(debug=True)
