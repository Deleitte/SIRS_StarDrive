sudo iptables -F
sudo iptables -P INPUT DROP
sudo iptables -P FORWARD DROP
sudo iptables -P OUTPUT DROP
sudo iptables -A OUTPUT -p tcp -j ACCEPT
sudo iptables -A INPUT -p tcp -j ACCEPT

sudo sh -c 'iptables-save > /etc/iptables/rules.v4'