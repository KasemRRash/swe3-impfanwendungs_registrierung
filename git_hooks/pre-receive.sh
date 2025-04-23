#!/bin/bash
#
# pre-receive Hook: Entpackt alle geänderten .java-Dateien ins /tmp  und prüft sie mit Checkstyle via "find + xargs".
# NUR DIE JAVA-DATEIEN mit checkstyle, die anderen passen lassen..

while read oldrev newrev refname
do
  # alle geänderten Dateien suchen zwischen altem und neuem Commit
  changed_files=$(git diff-tree --no-commit-id --name-only -r "$oldrev" "$newrev")

  # Nur die .java-Dateien herausfiltern
  java_files=$(echo "$changed_files" | grep -E '\.java$')

  #falls keine Java-Dateien dabei sind, einfach nächsten Ref verarbeiten
  if [ -z "$java_files" ]; then
     continue
  fi

  echo "Extrahiere geänderte Java-Dateien in ein temporäres Verzeichnis..."
  # temporäres Verzeichnis anlegen
  tmpdir=$(mktemp -d)

  # entpacken der Dateien aus dem neuen Commit in $tmpdir
  for file in $java_files; do
    mkdir -p "$tmpdir/$(dirname "$file")"
    # Datei-Inhalt aus Commit entnehmen und speichern
    git show "$newrev:$file" > "$tmpdir/$file"
  done


  echo "Prüfe Formatierung mit google-java-formatter..."

  find "$tmpdir" -name '*.java' \
       | xargs -r -n 1000 google-java-format --set-exit-if-changed
  formatter_status=$?
  if [ $formatter_status -ne 0 ]; then
    echo "Fehler: Einige Dateien entsprechen nicht den Formatierungsregeln von google-java-formatter."
    rm -rf "$tmpdir"
    exit 1
  fi

  echo "Starte CheckStyle..."
  # Mit find + xargs Checkstyle starten (keine endlose Argumentliste)  -n 50: maximal 50 Dateien pro Aufruf um push-probleme zu vermeiden

find "$tmpdir" -name '*.java' \
     | xargs -r -n 1000 checkstyle -c ~/repos/swe3-2024-03/misc/checkstyle.xml

status=$?
if [ $status -ne 0 ]; then
  echo "Fehler: Checkstyle-Regeln wurden nicht eingehalten."
  rm -rf "$tmpdir"
  exit 1
fi

  rm -rf "$tmpdir"

done

echo "Push erfolgreich."
exit 0
