from time import sleep
import rsa

# openssl genrsa -f4 -out test.priv 4096
# openssl rsa -in test.priv -pubout > test.pub
# openssl rsa -pubin -in test.pub -modulus -noout
# openssl rsa -pubin -in test.pub -text -noout

class TimeUpdatedMachine:
    def __init__(self, name, key_folder):
        with open(key_folder + f'/{name}_key.priv', 'rb') as p:
            self.privateKey = rsa.PrivateKey.load_pkcs1(p.read())
        with open(key_folder + f'/{name}_key.pub', 'rb') as p:
            self.publicKey = rsa.PublicKey.load_pkcs1_openssl_pem(p.read())  
        self.name = name
        self.ping_interval = None
        self.challenge = None
        self.start()

    def start(self):
        while True:
            self.update()
            sleep(self.ping_interval/1000)

    def update(self):
        raise NotImplementedError
    