import random
import requests
import timeUpdatedMachine
import sys
import json
import base64
import time
from Crypto.Signature import pkcs1_15
from Crypto.Hash import SHA256
from Crypto.PublicKey import RSA


class Actuator(timeUpdatedMachine.TimeUpdatedMachine):
    def __init__(self, name, key_folder):
        self.on = True
        self.damaged = False
        super().__init__(name, key_folder)

    def update(self):
        print("Requesting configs for {}".format(self.name))
        data = {
                'damaged': True if self.damaged else random.randint(1, 100) > 95,
            }
        data['signature'] = self.request_body_signature(data)
        req = requests.patch(
            f'http://192.168.2.254:80/actuators/{self.name}',
            json=data
        )
        response = json.loads(req.text)
        self.on = response['on']
        self.damaged = response['damaged']
        self.ping_interval = response['pingInterval']
        print(response)

Actuator(sys.argv[1], sys.argv[2])
