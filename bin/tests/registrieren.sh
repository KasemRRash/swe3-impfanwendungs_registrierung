#!/usr/bin/env bash

source local/config.txt || exit 1
baseurl="$baseurl/$webapp"

# Statische Werte
output="/home/kasrashrash/repos/swe3-2024-03/lasttests/ergebnis.txt"
> "$output"

echo "user,time" > "$output"

start=${EPOCHREALTIME/[.,]}

# Benutzeranzahl je Testgruppe definieren
for users in 4000; do
  echo "[Test] Starte Login-Test mit $users Benutzern..."

  for i in $(seq 3597 $users); do
    email="swe3-${users}_$i@bremerhaven.de"
    password="123"


            # Registrierung
            response1=$(curl -s -w "%{http_code}" -o /dev/null -X POST \
              -d "email=$email&password=$password&passwordConfirm=$password" \
              "$baseurl/register")

          done

        done

        ende=${EPOCHREALTIME/[.,]}
        echo "Gesamtzeit: $(( (ende - start) / 1000000 )) ms"
        echo "Alle Login-Tests abgeschlossen. Ergebnisse gespeichert in $output."

