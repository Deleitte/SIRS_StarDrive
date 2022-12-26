import random
import requests
import timeUpdatedMachine
import sys
import json

class Actuator(timeUpdatedMachine.TimeUpdatedMachine):
    def __init__(self, name):
        self.on = True
        self.damaged = False
        req = requests.patch(
            'http://localhost:8080/actuators/{}'.format(name),
            json={'damaged': self.damaged}
        )
        super().__init__(name, json.loads(req.text)['pingInterval'])
        
    def update(self):
        print("Requesting configs for {}".format(self.name))
        req = requests.patch(
            'http://localhost:8080/actuators/{}'.format(self.name),
            json={'damaged': True if self.damaged else random.randint(1, 100) > 95 }
        )
        response = json.loads(req.text)
        self.on = response['on']
        self.damaged = response['damaged']
        self.ping_interval = response['pingInterval']
        print(response)

Actuator(sys.argv[1])