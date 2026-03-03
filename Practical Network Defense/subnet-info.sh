#!/bin/bash

if [ -z "$1" ]; then
  echo "Uso: ./subnet-info.sh IP/prefix"
  exit 1
fi

IP_CIDR=$1
IP=${IP_CIDR%/*}
PREFIX=${IP_CIDR#*/}

IFS=. read -r o1 o2 o3 o4 <<< "$IP"

IP_INT=$(( (o1<<24) + (o2<<16) + (o3<<8) + o4 ))

MASK=$(( 0xFFFFFFFF << (32 - PREFIX) & 0xFFFFFFFF ))

NETWORK=$(( IP_INT & MASK ))
BROADCAST=$(( NETWORK | ~MASK & 0xFFFFFFFF ))

HOST_BITS=$((32 - PREFIX))

if [ "$PREFIX" -ge 31 ]; then
  USABLE="Caso speciale (/31 o /32)"
  RANGE="N/A"
else
  USABLE=$((2**HOST_BITS - 2))
  FIRST=$((NETWORK + 1))
  LAST=$((BROADCAST - 1))
  RANGE="$(printf "%d.%d.%d.%d" $((FIRST>>24&255)) $((FIRST>>16&255)) $((FIRST>>8&255)) $((FIRST&255))) to $(printf "%d.%d.%d.%d" $((LAST>>24&255)) $((LAST>>16&255)) $((LAST>>8&255)) $((LAST&255)))"
fi

to_ip() {
  printf "%d.%d.%d.%d" $(($1>>24&255)) $(($1>>16&255)) $(($1>>8&255)) $(($1&255))
}

to_bin_octet() {
  local x=$1
  local b=""
  for ((i=7; i>=0; i--)); do
    if (( (x>>i)&1 )); then b+="1"; else b+="0"; fi
  done
  echo -n "$b"
}

to_bin() {
  local n=$1
  local a=$(( (n>>24)&255 ))
  local b=$(( (n>>16)&255 ))
  local c=$(( (n>>8)&255 ))
  local d=$(( n&255 ))
  printf "%s.%s.%s.%s" \
    "$(to_bin_octet $a)" \
    "$(to_bin_octet $b)" \
    "$(to_bin_octet $c)" \
    "$(to_bin_octet $d)"
}

echo ""
echo "1) IP address: $IP"
echo "2) Subnet mask: $(to_ip $MASK) (/$PREFIX)"
echo "3) IP binary representation:   $(to_bin $IP_INT)"
echo "4) Mask binary representation: $(to_bin $MASK)"
echo "5) Bitwise AND operation:      $(to_bin $NETWORK)"
echo "6) Network address: $(to_ip $NETWORK)"
echo "7) Broadcast address: $(to_ip $BROADCAST)"

echo "8) Number of host bits: 32 - $PREFIX = $HOST_BITS"

if [ "$PREFIX" -ge 31 ]; then
  if [ "$PREFIX" -eq 31 ]; then
    echo "9) Number of usable hosts: caso speciale /31 (tipicamente 2 per point-to-point)"
  else
    echo "9) Number of usable hosts: caso speciale /32 (1 indirizzo)"
  fi
  echo "10) Valid host range: $RANGE"
else
  TOTAL=$((2**HOST_BITS))
  USABLE=$((TOTAL - 2))
  echo "9) Number of usable hosts: 2^$HOST_BITS - 2 = $TOTAL - 2 = $USABLE"
  echo "10) Valid host range: $RANGE"
fi

echo ""
