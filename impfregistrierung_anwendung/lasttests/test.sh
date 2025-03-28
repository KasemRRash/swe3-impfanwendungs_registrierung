#!/usr/bin/env bash

source local/config.txt || exit 1
path="$baseurl/$webapp"

# Statische Werte
output="login_ergebnisse.csv"
> "$output"

echo "user,time" > "$output"

start=${EPOCHREALTIME/[.,]}

# Benutzeranzahl je Testgruppe definieren
for users in 0 100 200 300 400 500 600 700 800 900 1000; do
    echo "[Test] Starte Login-Test mit $users Benutzern..."

    for i in $(seq 1 $users); do
        email="user${i}_@example.com"
        password="123"

        (

            # Registrierung}
        #    response1=$(curl -s -w "%{http_code}" -o /dev/null -X POST \
         #       -d "email=$email&password=$password&passwordConfirm=$password" \
         #       "$baseurl/register")

            # Überprüfen, ob Registrierung erfolgreich war
          #  if test "$response1" == "200" ; then

          zwischenstart=${EPOCHREALTIME/[.,]}
                # Login nur durchführen, wenn Registrierung erfolgreich war
###               echo "$email"
                response2=$(curl -s -w "%{http_code}" -o /dev/null -X POST -d "email=$email&password=123" "$path/UserAnmelden")
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

