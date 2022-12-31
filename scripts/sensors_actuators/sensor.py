import random
import requests
import timeUpdatedMachine
import sys


class Sensor(timeUpdatedMachine.TimeUpdatedMachine):
    def __init__(self, name, ping_interval, key_folder):
        super().__init__(name, key_folder, ping_interval)

    def update(self):
        value = random.randint(50, 100)
        print("Updating sensor {} with value {}".format(self.name, value))
        data = { 'value': value }
        data['signature'] = self.request_body_signature(data)
        req = requests.patch(
            f'http://localhost:8080/sensors/{self.name}/update', json=data
        )
        print('Success' if req.status_code == 200 else 'Failure')
Sensor(sys.argv[1], int(sys.argv[2]), sys.argv[3])