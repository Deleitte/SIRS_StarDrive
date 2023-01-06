sudo apt update -y
sudo apt upgrade -y
sudo apt -y install netplan.io
sudo apt install iptables-persistent
sudo apt install python3 -y
sudo apt install python3-pip -y
pip3 install pycryptodome

sudo cp 01-network-manager-all.yaml /etc/netplan/
sudo netplan try