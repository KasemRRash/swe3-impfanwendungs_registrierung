#!/bin/bash
# post-receive-Hook

configfile="/home/$USER/repos/swe3-2024-03/local/config.txt"

while read oldrev newrev refname
do
  echo "Neuer Push empfangen für $refname:"
  echo "Alte Revision: $oldrev"
  echo "Neue Revision: $newrev"

  # vllt. bin/build.sh oder so...
  # ...wenn das der main-Branch ist, Deployment triggern
  branch=$(echo "$refname" | sed 's|^refs/heads/||')
  if [ "$branch" = "main" ]; then
    echo "Starte Deployment für main..."

  if test "$configfile" != ""; then
    source "$configfile"
  fi

   cd ~/repos/swe3-2024-03/ || exit 1
   bin/build.sh
   # Problem mit local/config.txt - private


   #alle freie Slots abrufen
   #curl -X POST -H "Content-Type: application/json" https://informatik.hs-bremerhaven.de/docker-$USER-java/FreieSlotsAnzeigen

   #status überprüfen
   echo "Testen ob die Anwendung online ist...:"
   curl -I https://informatik.hs-bremerhaven.de/docker-$USER-java/

   #Login
   #curl -X POST -d "email=tester123@gmail.com&password=123" https://informatik.hs-bremerhaven.de/docker-$USER-java/UserAnmelden

   #Buchungen abrufen
  # curl -X GET \
  #   -H "Content-Type: application/json" \
   #  https://informatik.hs-bremerhaven.de/docker-$USER-java/BuchungAnzeigen

  fi
done

exit 0

