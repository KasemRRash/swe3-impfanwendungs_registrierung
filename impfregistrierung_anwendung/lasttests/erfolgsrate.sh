#!/bin/bash
input="login_ergebnisse.csv"
output="erfolgsrate.dat"

for i in 100 200 300 400 500 600; do
  anzahlErfolgreicherAnfragen=$(grep "$i-" "$input" | wc -l)
  erfolgsrate=$(echo "scale=2; ($anzahlErfolgreicherAnfragen/$i)*100" | bc)
  echo "$i $erfolgsrate" >> $output
done


