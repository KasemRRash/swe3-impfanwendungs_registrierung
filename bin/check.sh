#!/usr/bin/env bash

source local/config.txt || exit 1
path="$baseurl/$webapp"
mkdir -p tmp

# 1. Registrierung
echo "1. Registrierung"
echo " "
curl -s -X POST -d "email=testuser@example.com&password=123&passwordConfirm=123" \
     "$path/register"

echo " "
# 2. Login mit Cookie-Speicherung
echo "2. Login"
curl -s -L -c tmp/cookie-$$.jar -b tmp/cookie-$$.jar -X POST \
     -d "email=testuser@example.com&password=123" "$path/UserAnmelden"

# 3. Session prüfen
echo "3. Session prüfen"
echo " "
curl -s -b tmp/cookie-$$.jar "$path/SessionPruefen"

echo " "

# 4. Verfügbare Slots anzeigen
echo "4. Freie Slots"
#curl -s -b tmp/cookie-$$.jar "$path/FreieSlotsAnzeigen"
curl -X POST -b tmp/cookie-$$.jar \
  $path/FreieSlotsAnzeigen
echo " "

# 5. Impfstoffe abrufen
echo "5. Impfstoffe für Zentrum"
curl -s -b tmp/cookie-$$.jar "$path/ImpfstoffAnzeigen?impfzentren=Nemerb-Nord"

echo " "

# 6. Termin buchen
echo "6. Termin buchen"
curl -s -b tmp/cookie-$$.jar -X POST \
     -d "datum=2025-03-20&zeit=08:30&impfstoff_id=1&impfzentrum=Nemerb-Nord" "$path/TerminBuchen"

echo " "

# 7. Eigene Buchungen abrufen
echo "7. Eigene Buchungen"
curl -s -b tmp/cookie-$$.jar "$path/BuchungAnzeigen"

echo " "

# 8. Termin stornieren
echo "8. Termin stornieren"
curl -s -b tmp/cookie-$$.jar -X POST \
     -d "buchung_id=1" "$path/TerminStornieren"

echo " "

# 9. Logout
echo "9. Logout"
curl -s -b tmp/cookie-$$.jar -X POST "$path/UserAbmelden"

echo " "

# 10. Zugriff nach Logout testen
echo "10. Zugriff nach Logout"
curl -s -b tmp/cookie-$$.jar "$path/SessionPruefen"

rm -f tmp/cookie-$$.jar
