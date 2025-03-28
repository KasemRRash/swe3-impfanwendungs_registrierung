-- zeige die Impfstationen zusammen mit der Anzahl der Buchungen pro Tag für eine ganze Woche

SELECT
  z.name AS impfzentrum,               -- Zeigt den Namen des Impfzentrums
  s.slot_date,                         -- Zeigt das Datum des Slots
  COUNT(b.id) AS buchungen_pro_tag     -- Zählt die Anzahl der Buchungen pro Tag
FROM slot s
LEFT JOIN buchung b ON s.id = b.slot_id -- Verknüpft die Slots mit Buchungen; zeigt auch Slots ohne Buchungen an (LEFT JOIN)
JOIN impfzentrum z ON s.impfzentrum_id = z.id -- Verknüpft die Slots mit den zugehörigen Impfzentren
WHERE s.slot_date BETWEEN '2025-01-10' AND '2025-01-17' -- Filtert die Ergebnisse auf Slots, die im Zeitraum vom 10.01.2025 bis 17.01.2025 liegen
GROUP BY z.name, s.slot_date           -- Gruppiert die Ergebnisse nach Impfzentrum und Datum
ORDER BY s.slot_date, z.name;          -- Sortiert die Ergebnisse zuerst nach Datum, dann nach dem Namen des Impfzentrums
