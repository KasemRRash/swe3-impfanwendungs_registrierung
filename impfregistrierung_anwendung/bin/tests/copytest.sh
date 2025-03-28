#!/usr/bin/env bash

source local/config.txt || exit 1
baseurl="$baseurl/$webapp"
echo $baseurl

# Statische Werte
output="/home/kasrashrash/repos/swe3-2024-03/lasttests/ergebnis.txt"
> "$output"

echo "user,time" > "$output"

start=${EPOCHREALTIME/[.,]}

# Benutzeranzahl je Testgruppe definieren
for users in 10; do
    echo "[Test] Starte Login-Test mit $users Benutzern..."

    for i in $(seq 1 $users); do
        email="testuser$i@example.com"
        password="test$i"
        (


		zwischenstart=${EPOCHREALTIME/[.,]}
                # Login nur durchführen, wenn Registrierung erfolgreich war
                curl -s -X POST \
     -d "email=$email&password=$password" "$baseurl/UserAnmelden"
                    zwischenende=${EPOCHREALTIME/[.,]}
                    time=$(( (zwischenende - zwischenstart / 1000000)))
                    echo "$users-$i,$time" >> "$output"          
        ) &
    done

    wait
done

ende=${EPOCHREALTIME/[.,]}
echo "Gesamtzeit: $(( (ende - start) / 1000000 )) ms"
echo "Alle Login-Tests abgeschlossen. Ergebnisse gespeichert in $output."

