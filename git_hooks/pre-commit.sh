#!/bin/bash
#
# An example hook script to verify what is about to be committed.
# Called by "git commit" with no arguments.  The hook should
# exit with non-zero status after issuing an appropriate message if
# it wants to stop the commit.
#
# To enable this hook, rename this file to "pre-commit".

#geänderte Dateien anzeigen:
changed_files=$(git diff --cached --name-only --diff-filter=ACM)

#nur .java-Dateien filtern
java_files=$(echo "$changed_files" | grep -E '\.java$')

#wenn keine Java-Dateien dabei sind, früh raus:
if [ -z "$java_files" ]; then
  exit 0
fi


# Pre-commit-Hook:
# Prüft Codeformatierung und Checkstyle-Regeln

# echo "Starte google-java-formatter..."
# --set-exit-if-changed beendet das Skript, falls der Code formatiert werden müsste.
# java -jar ~/google-java-format-1.15.0-all-deps.jar --set-exit-if-changed -r $java_files
# if [ $? -ne 0 ]; then
#    echo "Fehler: Der Code entspricht nicht den Formatierungsregeln von google-java-formatter."
#    exit 1
# fi

echo "Starte Checkstyle..."

#checkstyle ausführen
checkstyle -c ~/swe3-2024-03/misc/checkstyle.xml src
if [ $? -ne 0 ]; then
    echo "Fehler: Checkstyle-Regeln wurden nicht eingehalten."
    exit 1
fi

echo "Alle Prüfungen erfolgreich."
exit 0
