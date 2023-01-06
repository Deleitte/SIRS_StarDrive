# StarDrive

The developed application is meant to facilitate StarDrive's management of the factory's production process. It provides the employees an interface to consult information on their job status, a dashboard to monitor the production process and statistics and an interface for managing employees and controlling more details of the production process of self-driving and remote control vehicles.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them

```
Give examples
```

### Installing

A step by step series of examples that tell you how to get a development env running

Say what the step will be

```
Give the example
```

And repeat

```
until finished
```

End with an example of getting some data out of the system or using it for a little demo

## Deployment

For deploying this, we are currently using the [Caddy web server](https://caddyserver.com/), with the following config:

```
:443

tls ./certs/webserver.crt ./certs/webserver.key

@remote {
    not remote_ip 192.168.2.0/24
}

route /api/auth/* {
    uri strip_prefix /api
    reverse_proxy :8080
}

route /api/external/* {
    uri strip_prefix /api
    reverse_proxy :8080
}

route /api/* {
    respond @remote "Unauthorized" 403
    uri strip_prefix /api
    reverse_proxy :8080
}

route @remote {
    root * /home/vasco/public
    try_files {path} /index.html
    file_server
}

route {
    root * /home/vasco/private
    try_files {path} /index.html
    file_server
}
```

This config can be adapted. We serve two different versions of the frontend, one doesn't include views that should only be accessible to users connected to the company's private network and one that includes. If a user not connected to the company's private network attempts to access api endpoints other than authentication and auditing.

Other web servers can also be used, similar configurations should be possible.

## Built With

* [Spring Boot](https://spring.io/projects/spring-boot) - The web framework used for the backend
* [Kotlin](https://kotlinlang.org/) - The language used in the backend
* [Gradle](https://gradle.org/) - Dependency Management and build tool for the backend
* [NPM](https://www.npmjs.com/) - Dependency Management for the frontend
* [VueJS](https://vuejs.org/) - The frontend framework we used
* [Python](https://www.python.org/) - The language used for the actuator and sensor scripts

## Authors

* **Eduardo Espadeiro** - 95568
* **Guilherme Salvador** - 95584
* **Vasco Correia** - 94188
