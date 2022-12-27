from time import sleep


class TimeUpdatedMachine:
    def __init__(self, name, ping_interval):
        self.name = name
        self.ping_interval = ping_interval
        self.start()

    def start(self):
        while True:
            self.update()
            sleep(self.ping_interval/1000)

    def update(self):
        raise NotImplementedError
    