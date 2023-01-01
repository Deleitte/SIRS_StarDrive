#!/bin/sh

openssl genrsa -out $1.key
openssl rsa -in $1.key -pubout > $1.pub
openssl rsa -in $1.key -text > $1_priv.pem
openssl pkcs8 -topk8 -inform PEM -outform DER -in $1_priv.pem -out $1_priv.der -nocrypt

# This file isn't needed and can be deleted
rm $1_priv.pem
