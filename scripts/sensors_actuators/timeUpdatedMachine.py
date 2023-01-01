from time import sleep
from Crypto.Signature import pkcs1_15
from Crypto.Hash import SHA256
from Crypto.PublicKey import RSA
import json
import base64
import time


class TimeUpdatedMachine:
    def __init__(self, name, key_folder, ping_interval=None):
        with open(key_folder + f'/{name}_priv.der', 'rb') as p:
            self.privateKey = RSA.import_key(p.read())
        self.name = name
        self.ping_interval = ping_interval
        self.challenge = None
        self.start()

    def start(self):
        while True:
            self.update()
            sleep(self.ping_interval/1000)

    def update(self):
        raise NotImplementedError
    
    def request_body_signature(self, body):
        body['timestamp'] = int(time.time())
        json_data = json.dumps(body)
        digest = SHA256.new(json_data.encode('ascii'))
        signature = pkcs1_15.new(self.privateKey).sign(digest)
        return base64.b64encode(signature).decode('ascii')
