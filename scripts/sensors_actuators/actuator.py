import random
import requests
import timeUpdatedMachine
import sys
import json
import rsa
import base64

class Actuator(timeUpdatedMachine.TimeUpdatedMachine):
    def __init__(self, name, key_folder):   
        self.on = True
        self.damaged = False
        super().__init__(name, key_folder)
        
    def update(self):
        print("Requesting configs for {}".format(self.name))
        self.getChallenge(self.name)
        req = requests.patch(
            f'http://localhost:8080/actuators/{self.name}',
            json={
                'damaged': True if self.damaged else random.randint(1, 100) > 95,
                'challenge': self.challenge,
            }
        )
        response = json.loads(req.text)
        self.on = response['on']
        self.damaged = response['damaged']
        self.ping_interval = response['pingInterval']
        print(response)
    
    def getChallenge(self):
        req = requests.get(f'http://localhost:8080/actuators/{self.name}/challenge')
        response = json.loads(req.text)
        self.challenge = int(rsa.decrypt(base64.b64decode(response['challenge']), self.privateKey).decode('ascii')) + 1

Actuator(sys.argv[1], sys.argv[2])