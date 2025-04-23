#!/usr/bin/env bash

source local/config.txt || exit 1
path="$baseurl/$webapp"

# Statische Werte
output="/home/kasrashrash/repos/swe3-2024-03/lasttests/ergebnis.txt"
> "$output"

echo "user,time" > "$output"

start=${EPOCHREALTIME/[.,]}

# Benutzeranzahl je Testgruppe definieren
for users in 0 250 500 1000 1500 2000 3000; do
    echo "[Test] Starte Login-Test mit $users Benutzern..."

    for i in $(seq 1 $users); do
        email="swe3-2000_$i@bremerhaven.de"
        password="123"



		zwischenstart=${EPOCHREALTIME/[.,]}
                # Login nur durchführen, wenn Registrierung erfolgreich war

                (

                response2=$(curl -s -w "%{http_code}" -o /dev/null 
                -X POST \
                    -d "email=$email&password=$password" \
                    "$path/UserAnmelden")

                # Wenn Login ebenfalls erfolgreich, Endzeit erfassen und Zeit berechnen
                if test "$response2" == "200" ; then
                    zwischenende=${EPOCHREALTIME/[.,]}
                    time=$(( (zwischenende - zwischenstart) / 1000000))
                    echo "$users-$i,$time" >> "$output"
                else
                    echo "[Fehler] Benutzer $users-$i"
                        exit 1
               
            fi
        ) &
    done

    wait
done

ende=${EPOCHREALTIME/[.,]}
echo "Gesamtzeit: $(( (ende - start) / 1000000 )) ms"
echo "Alle Login-Tests abgeschlossen. Ergebnisse gespeichert in $output."

