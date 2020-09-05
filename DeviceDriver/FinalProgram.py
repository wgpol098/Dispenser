import RPi.GPIO as GPIO
import time as czas
import requests
import json
import sys
import os
from datetime import *
import threading

GPIO.setmode(GPIO.BCM)
GPIO.setup(21,GPIO.OUT)
GPIO.setup(20,GPIO.OUT)
GPIO.setup(16,GPIO.OUT)
GPIO.setup(19,GPIO.OUT)
GPIO.setup(26,GPIO.OUT)
GPIO.setup(13,GPIO.OUT)
GPIO.setup(12,GPIO.OUT)
GPIO.setup(4,GPIO.IN)

cUNow=0

def postApi(fl,noW,time):
    time=time.strftime("%Y-%m-%dT%H:%M:%S.%f")
    time=time[:-3]
    data={"idDispenser":210,"dateAndTime":time,"noWindow":noW,"flag":fl}
    data_json=json.dumps(data)
    #print data_json
    headers={'Content-type':'application/json','Accept':'application/json'}
    r=requests.post('http://panda.fizyka.umk.pl:9092/api/Disp',json=data,headers=headers)
    cUNow=cUNow+1
    #print r


class ledWatek(threading.Thread):
    def __init__(self,nr,nrl):
        threading.Thread.__init__(self)
        self.nr=nr
        self.nrl=nrl
    def run(self):
        print "start th"
        GPIO.output(self.nr,GPIO.HIGH)
        now=datetime.now()
        stoptime=now+timedelta(hours=1)
        wziete=False
        flaga=1
        while now < stoptime:
            print "while th"
            if not GPIO.input(4):
                flaga=0
                break
            #if przerwanie->wziete=True->break i wtedy flaga=0;
            czas.sleep(3)
            now=datetime.now()
        print "stop th"
        postApi(flaga,nrl,now)  #wywoalnie f.sendToServer
        GPIO.output(self.nr,GPIO.LOW)

def checkUpdate():
    data={"idDispenser":210}
    data_json=json.dumps(data)
    headers={'Content-type':'application/json','Accept':'application/json'}
    r=requests.post('http://panda.fizyka.umk.pl:9092/api/Android/UpdateCounter',json=data,headers=headers)
    r=r.json()
    upNo=r['noUpdate']
    #print "Numert update'u: ",upNo
    return upNo


#---------------------------------------------------------
l1=ledWatek(21,1)
l2=ledWatek(20,2)
l3=ledWatek(16,3)
l4=ledWatek(12,4)
l5=ledWatek(26,5)
l6=ledWatek(19,6)
l7=ledWatek(13,7)
f=open('plan.json')
data_dict=json.load(f)
f.close()
uf=open('noUpdate.txt')
cUNow=int(uf.readline())
uf.close()
while True:
    now=datetime.now()
    print now
    for x in data_dict:
        cs=x['dateAndTime']  #cs="2020-04-08T10:30:00"
        cs=cs.split("T")     #zmiana formatu
        csd=cs[0].split("-")
        cst=cs[1].split(":")
        cs=csd+cst
        for i in range(0,len(cs)): #przetwarzanie json->int->datetime
            cs[i]=int(cs[i])
        xdt=datetime(cs[0],cs[1],cs[2],cs[3],cs[4],cs[5])
        xdtp=datetime(cs[0],cs[1],cs[2],cs[3],cs[4]+10,cs[5])
        if xdt<now<=xdtp: #sprawdzanie dopasowania
            noled=x['noWindow']
            print "swiec leda numer: ",noled
            if noled == 1:
                l1.start()
            if noled == 2:         
                l2.start()
            if noled == 3:         
                l3.start()
            if noled == 4:         
                l4.start()
            if noled == 5:         
                l5.start()
            if noled == 6:         
                l6.start()
            if noled == 7:         
                l7.start()

        #else:
           # print " nie pasuje pomiedzy",xdt," a  ",xdtp
    cU=checkUpdate()
    if  cU > cUNow :
        cUNow=cU
        os.system("python getapiDisp.py")
        print "Update Plan"
        f=open('plan.json')
        data_dict=json.load(f)
        f.close()
        with open('noUpdate.txt','w') as updateFile:
            updateFile.write(str(cU))

    print("pauza na ",now)
    czas.sleep(10)
