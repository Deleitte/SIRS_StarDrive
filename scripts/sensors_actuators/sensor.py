import random
import requests
import timeUpdatedMachine
import sys


class Sensor(timeUpdatedMachine.TimeUpdatedMachine):
    def __init__(self, name, ping_interval):
        super().__init__(name, ping_interval)

    def update(self):
        value = random.randint(50, 100)
        print("Updating sensor {} with value {}".format(self.name, value))
        req = requests.patch(
            'http://localhost:8080/sensors/{}/update'.format(self.name), json={'value': value}
        )
        print(req.status_code)
Sensor(sys.argv[1], int(sys.argv[2]))