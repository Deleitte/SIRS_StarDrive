sudo apt update -y
sudo apt upgrade -y
sudo apt -y install netplan.io
sudo apt install iptables-persistent -y

sudo cp 01-network-manager-all.yaml /etc/netplan/
sudo netplan try