sudo apt update -y
sudo apt upgrade -y

sudo apt install curl
sudo apt -y install netplan.io
sudo apt install iptables-persistent

sudo cp 01-network-manager-all.yaml /etc/netplan/
sudo netplan try

sudo apt-get install gnupg
wget -qO - https://www.mongodb.org/static/pgp/server-4.0.asc | sudo apt-key add -
echo "deb http://repo.mongodb.org/apt/debian bullseye/mongodb-org/4.0 main" | sudo tee /etc/apt/sources.list.d/mongodb-org-4.0.list
sudo apt-get update -y
sudo apt-get install -y mongodb-org
