#!/bin/bash

COMMIT_MSG_FILE="$1"
COMMIT_MSG=$(cat "$COMMIT_MSG_FILE")

# Regex-Beschreibung:
#STRUKTUR (ADD|DELETE|CHANGE)
REGEX='^(ADD|DELETE|CHANGE)\s*\|\s*[^|]+\s*\|\s*.*$'

if [[ ! "$COMMIT_MSG" =~ $REGEX ]]; then
    echo "ERROR: Commit-Nachricht entspricht nicht dem geforderten
Format."
    echo "Erwartet wird: AUFGABE(ADD,DELETE,CHANGE) | DATEINAME | NACHRICHT"
    exit 1
fi

# Alles gut: Commit wird durchgelassen
exit 0
