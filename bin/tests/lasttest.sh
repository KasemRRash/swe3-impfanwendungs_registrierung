#!/usr/bin/env bash

source local/config.txt || exit 1
baseurl="$baseurl/$webapp"
output="login_ergebnisse.csv"

echo "user,time_ms,status" > "$output"

# Benutzeranzahl je Testgruppe definieren
for users in 50 100 250 500 1000 5000; do
    echo "[Test] Starte Login-Test mit $users Benutzern..."

    for i in $(seq 1 $users); do
        email="testuser${users}_$i@example.com"
        password="123"

        # Registrierung mit curl
        curl -s -X POST -d "email=$email&password=$password&passwordConfirm=$password" \
            "$baseurl/register" > /dev/null

        # Login-Zeit messen
        start=$(date +%s%3N)
        response=$(curl -s -w "%{http_code}" -o /dev/null -X POST \
            -d "email=$email&password=$password" "$baseurl/UserAnmelden")
        end=$(date +%s%3N)
        time_ms=$((end - start))

        echo "$users-$i,$time_ms,$response" >> "$output"
    done

done

echo "Alle Login-Tests abgeschlossen. Ergebnisse gespeichert in $output."
