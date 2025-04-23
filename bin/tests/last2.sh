#!/usr/bin/env bash

# Konfiguration laden
source local/config.txt || exit 1
baseurl="$baseurl/$webapp"

# Ausgabe-Datei definieren
output="/home/kasrashrash/repos/swe3-2024-03/lasttests/ergebnis.txt"
> "$output"

echo "user,time_ms,http_status" > "$output"

# Gesamtstartzeit (optional, nicht zur Auswertung)
start=$(date +%s%3N)

# Benutzeranzahl je Testgruppe
for users in 50 100 250 500 1000 1500; do
    echo "[Test] Starte Login-Test mit $users Benutzern..."

    for i in $(seq 1 $users); do
        email="swe3-5000_$i@bremerhaven.de"
        password="123"

        (
            # Startzeit in Millisekunden
            zwischenstart=$(date +%s%3N)

            # Login durchführen
            response=$(curl -s -w "%{http_code}" -o /dev/null -X POST \
                -d "email=$email&password=$password" "$path/UserAnmelden")

            # Endzeit
            zwischenende=$(date +%s%3N)

            # Zeitdifferenz berechnen
            time=$((zwischenende - zwischenstart))

            # Ergebnis speichern: Benutzer-Zahl-Nr, Zeit, HTTP-Code
            echo "$users-$i,$time,$response" >> "$output"
        ) &
    done

    wait
done

ende=$(date +%s%3N)
gesamtzeit=$((ende - start))

echo "Gesamtzeit: $gesamtzeit ms"
echo "Alle Login-Tests abgeschlossen. Ergebnisse gespeichert in $output."

