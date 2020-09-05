import requests
import json
import sys
import os
os.system("sudo openvpn /etc/openvpn/client.conf")
data={"idDispenser":210}
data_json=json.dumps(data)
print data_json
headers={'Content-type':'application/json','Accept':'application/json'}
r=requests.get('http://panda.fizyka.umk.pl:9092/api/Disp',json=data,headers=headers)
print r.json()
p=r.json()
print (p)
#print(p["dateAndTime"])
with open('plan.json','w') as json_file:
    json.dump(p,json_file)
