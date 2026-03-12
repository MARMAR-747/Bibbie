#!/bin/bash

if [ -z "$1" ]; then
    echo "Uso: ./subnet-info-v6.sh IPv6/prefix"
    exit 1
fi

IP_CIDR="$1"

python3 - "$IP_CIDR" << 'PY'
import sys
import ipaddress

def ipv6_to_bin(addr):
    n = int(addr)
    bits = f"{n:0128b}"
    groups = [bits[i:i+16] for i in range(0, 128, 16)]
    return ":".join(groups)

def ipv6_to_bin_groups(addr):
    n = int(addr)
    bits = f"{n:0128b}"
    return [bits[i:i+16] for i in range(0, 128, 16)]

def prefix_to_mask(prefix):
    mask_int = ((1 << 128) - 1) ^ ((1 << (128 - prefix)) - 1) if prefix > 0 else 0
    return ipaddress.IPv6Address(mask_int)

if len(sys.argv) < 2:
    print("Uso: ./subnet-info-v6.sh IPv6/prefix")
    sys.exit(1)

ip_cidr = sys.argv[1]

try:
    iface = ipaddress.IPv6Interface(ip_cidr)
except Exception:
    print("Errore: input non valido. Usa il formato IPv6/prefix, ad esempio:")
    print("  ./subnet-info-v6.sh 2001:db8:abcd:12::1/64")
    sys.exit(1)

ip = iface.ip
network = iface.network
prefix = network.prefixlen
mask = prefix_to_mask(prefix)

ip_int = int(ip)
net_int = int(network.network_address)
and_result = ip_int & int(mask)

host_bits = 128 - prefix
total_addresses = 1 << host_bits

first_addr = network.network_address
last_addr = network.broadcast_address  # in ipaddress, per IPv6 coincide con l'ultimo indirizzo del range

print()
print(f"1) IPv6 address: {ip.compressed}")
print(f"2) Expanded IPv6 address: {ip.exploded}")
print(f"3) Prefix length: /{prefix}")
print(f"4) Subnet mask: {mask.exploded}")
ip_groups = ipv6_to_bin_groups(ip)
mask_groups = ipv6_to_bin_groups(mask)
and_groups = ipv6_to_bin_groups(and_result)
print("5) Bitwise operation (aligned):")
print("IP   :", ":".join(ip_groups))
print("MASK :", ":".join(mask_groups))
print("AND  :", ":".join(and_groups))
print(f"6) Network address: {network.network_address.compressed}")
print(f"7) Expanded network address: {network.network_address.exploded}")
print(f"8) First address in subnet: {first_addr.compressed}")
print(f"9) Last address in subnet: {last_addr.compressed}")
print(f"10) Number of host bits: 128 - {prefix} = {host_bits}")
print(f"11) Number of addresses in subnet: 2^{host_bits} = {total_addresses}")
print()

PY