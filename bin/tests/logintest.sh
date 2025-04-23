#!/usr/bin/env bash

source local/config.txt || exit 1
baseurl="$baseurl/$webapp"
output="login_results.csv"

echo "user,time_ms,status" > $output

# Anzahl Benutzer
users=50

for i in $(seq 1 $users); do
  email="testuser$i@example.com"
  password="123"

  # Registrierung (falls noch nicht vorhanden)
  curl -s -X POST -d "email=$email&password=$password&passwordConfirm=$password" \
    "$baseurl/register" > /dev/null

  # Login mit Zeitmessung
  start=$(date +%s%3N)
  response=$(curl -s -w "%{http_code}" -o /dev/null -X POST \
    -d "email=$email&password=$password" "$baseurl/UserAnmelden")
  end=$(date +%s%3N)
  time_ms=$((end - start))

  # Speichern
  echo "$email,$time_ms,$response" >> $output
done

echo "Login-Test abgeschlossen. Ergebnisse in $output"

