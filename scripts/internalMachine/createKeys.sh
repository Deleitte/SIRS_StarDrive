#!bin/bash

mkdir keys
for i in {1..5}
do
    openssl genrsa -out keys/act$i.key
    openssl genrsa -out keys/sens$i.key
    openssl rsa -in keys/act$i.key -pubout > keys/act$i.pub
    openssl rsa -in keys/sens$i.key -pubout > keys/sens$i.pub
    openssl rsa -in keys/act$i.key -text > keys/act$i\_priv.pem
    openssl rsa -in keys/sens$i.key -text > keys/sens$i\_priv.pem
    openssl pkcs8 -topk8 -inform PEM -outform DER -in keys/act$i\_priv.pem -out keys/act$i\_priv.der -nocrypt
    openssl pkcs8 -topk8 -inform PEM -outform DER -in keys/sens$i\_priv.pem -out keys/sens$i\_priv.der -nocrypt
    rm keys/act$i\_priv.pem
    rm keys/sens$i\_priv.pem
done
