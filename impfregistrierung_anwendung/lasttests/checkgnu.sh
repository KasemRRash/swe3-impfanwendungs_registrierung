#!/usr/bin/env bash

# Eingabedatei mit den Ergebnissen
ergebnisdatei="login_ergebnisse.csv"
ausgabedatei="auswertung.dat"

if test "$ergebnisdatei" == "" ; then
  echo "Fehler: Datei '$ergebnisdatei' nicht gefunden!"
  exit 1
fi

> "$ausgabedatei"

cut -d ',' -f1 "$ergebnisdatei" | grep -o '^[0-9]*' | sort -nu | while read -r group; do
    zeiten=$(grep "^$group-" "$ergebnisdatei" | cut -d ',' -f2)

  summe=0
  anzahl=0

  for zeit in $zeiten; do
    summe=$((summe + zeit))
    anzahl=$((anzahl + 1))
  done
   echo "summe: "$summe" anzahl: $anzahl"
  if test "$anzahl" -gt 0 ; then
    durchschnitt=$(echo "scale=2; $summe / $anzahl" | bc)
    nullenformat=$(echo "$durchschnitt" | sed 's/^\./0./')
    echo "$group $nullenformat" >> "$ausgabedatei"
  fi
done

echo "Daten für gnuplot wurden in '$ausgabedatei' gespeichert."

