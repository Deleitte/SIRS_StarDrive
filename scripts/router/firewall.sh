sudo sysctl net.ipv4.ip_forward=1
sysctl net.ipv4.conf.all.forwarding

sudo iptables -F
sudo iptables -t nat -F
sudo iptables -P INPUT DROP
sudo iptables -P FORWARD DROP
sudo iptables -P OUTPUT DROP
sudo iptables -t nat -A PREROUTING -p tcp -m tcp --dport 80 -j DNAT --to-destination 192.168.1.1:443
sudo iptables -t nat -A PREROUTING -p tcp -m tcp --dport 443 -j DNAT --to-destination 192.168.1.1
sudo iptables -A FORWARD -p tcp -m tcp --dport 80 -j ACCEPT
sudo iptables -A FORWARD -p tcp -m tcp --dport 443 -j ACCEPT
sudo iptables -A FORWARD -s 192.168.1.1/32 -i enp0s9 -p tcp -j ACCEPT
sudo iptables -A FORWARD -s 192.168.0.1/32 -i enp0s8 -p tcp -j ACCEPT
sudo iptables -A FORWARD -s 192.168.56.0/24 -i enp0s10 -p tcp -j ACCEPT

sudo sh -c 'iptables-save > /etc/iptables/rules.v4'

