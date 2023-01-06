sudo iptables -F
sudo iptables -P INPUT DROP
sudo iptables -P FORWARD DROP
sudo iptables -P OUTPUT DROP
sudo iptables -A INPUT -p tcp -s 192.168.2.254 -j ACCEPT
sudo iptables -A OUTPUT -p tcp -d 192.168.2.254 --dport 443 -j ACCEPT
sudo iptables -A OUTPUT -p tcp -d 192.168.2.254 --dport 80 -j ACCEPT

sudo sh -c 'iptables-save > /etc/iptables/rules.v4'
