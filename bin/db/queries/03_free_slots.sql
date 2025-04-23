--  Zeige alle Zeitslots, bei denen die jeweilige Kapazität noch nicht voll ist für den 10.01.2025 zusammen mit dem Impfzentrum und den dort angebotenen Impfstoffen sortiert nach Datum und Zeit

SELECT
  s.slot_date,                                   -- Zeigt das Datum des Slots
  s.slot_time,                                   -- Zeigt die Uhrzeit des Slots
  z.name AS impfzentrum,                        -- Zeigt den Namen des Impfzentrums
  z.kapazitaet,                                 -- Zeigt die maximale Kapazität des Impfzentrums
  COUNT(b.id) AS gebuchte_termine,              -- Zählt die Anzahl der Buchungen für den Slot
  GROUP_CONCAT(DISTINCT i.name SEPARATOR ', ') AS angebotene_impfstoffe -- Listet alle in diesem Impfzentrum angebotenen Impfstoffe, getrennt durch ', '
FROM slot s
LEFT JOIN buchung b ON s.id = b.slot_id         -- Verknüpft die Slots mit Buchungen; auch Slots ohne Buchungen werden angezeigt (LEFT JOIN)
JOIN impfzentrum z ON s.impfzentrum_id = z.id  -- Verknüpft die Slots mit den zugehörigen Impfzentren
JOIN impfzentrum_impfstoff zi ON z.id = zi.impfzentrum_id -- Verknüpft die Impfzentren mit ihren angebotenen Impfstoffen
JOIN impfstoff i ON zi.impfstoff_id = i.id     -- Verknüpft die Impfstoffzuordnungen mit den Impfstoffnamen
WHERE s.slot_date = '2025-01-10'               -- Filtert die Ergebnisse nach Slots, die am 10. Januar 2025 stattfinden
GROUP BY s.id, z.name, z.kapazitaet            -- Gruppiert die Ergebnisse nach Slot-ID, Name des Impfzentrums und Kapazität
HAVING COUNT(b.id) < z.kapazitaet              -- Zeigt nur Slots, bei denen die Kapazität noch nicht vollständig ausgelastet ist
ORDER BY s.slot_date, s.slot_time;             -- Sortiert die Ergebnisse nach Datum und Uhrzeit
