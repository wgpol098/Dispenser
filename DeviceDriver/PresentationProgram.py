import Queue
import requests
import json
import sys
import os
import time 
import RPi.GPIO as GPIO
GPIO.setmode(GPIO.BCM)
GPIO.setup(21,GPIO.OUT)
GPIO.setup(20,GPIO.OUT)
GPIO.setup(16,GPIO.OUT)
GPIO.setup(19,GPIO.OUT)
GPIO.setup(26,GPIO.OUT)
GPIO.setup(13,GPIO.OUT)
GPIO.setup(12,GPIO.OUT)
GPIO.setup(4,GPIO.IN)

q=Queue.Queue()
def getFun():
    r=requests.get('http://panda.fizyka.umk.pl:9092/api/Disp/PresentationGet')
    getInfo=r.json()
    #print getInfo
    for x in  getInfo:
        #print x['flag'],x['id']
        if (x['flag'] == 1) and (x['id'] is not 0) :
            q.put(x['id'])
            noled=x['id']
            print noled
            if noled == 1:
                GPIO.output(21,GPIO.HIGH)
            if noled == 2:         
                GPIO.output(20,GPIO.HIGH)
            if noled == 3:         
                GPIO.output(16,GPIO.HIGH)
            if noled == 4:         
                GPIO.output(12,GPIO.HIGH)
            if noled == 5:         
                GPIO.output(26,GPIO.HIGH)
            if noled == 6:         
                GPIO.output(19,GPIO.HIGH)
            if noled == 7:         
                GPIO.output(13,GPIO.HIGH)
    
def postFun(noW,fl): 
    if noW == 21:
        noW=1#etc etc
    if noW == 20:
        noW=2
    if noW == 16:
        noW=3
    if noW == 12:
        noW=4
    if noW == 26:
        noW=5
    if noW == 19:
        noW=6
    if noW == 13:
        noW=7
    data={"numberWindow":noW,"windowFlag":fl}
    data_json=json.dumps(data)
    print data_json
    headers={'Content-type':'application/json','Accept':'application/json'}
    r=requests.post('http://panda.fizyka.umk.pl:9092/api/Disp/PresentationPost',json=data,headers=headers)
#GPIO.cleanup()
GPIO.output(21,GPIO.LOW)
getFun()
w=0
rozmiar=q.qsize()
#gpioInCounter=0
while True:
    if not GPIO.input(4):
        w=q.get()
        postFun(w,-1)
        while not GPIO.input(4):
            time.sleep(0.5)
        time.sleep(2)
        print w
        postFun(w,0)
        rozmiar=rozmiar -1
        if w == 1:
             w=21#etc etc
        if w == 2:
             w=20
        if w == 3:
            w=16
        if w == 4:
            w=12
        if w == 5:
            w=26
        if w == 6:
            w=19
        if w == 7:
            w=13
 
        GPIO.output(w,GPIO.LOW) 
    if rozmiar <= 0:
        getFun()
        rozmiar=q.qsize()
    time.sleep(1)
