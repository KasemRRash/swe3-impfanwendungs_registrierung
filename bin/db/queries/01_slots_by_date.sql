-- zeige alle Zeitslots am 10.1.2025 für die Impfstation x zusammen mit der Anzahl existierender Buchungen und der Kapazität

SELECT
  s.slot_date,                      -- Zeigt das Datum des Slots
  s.slot_time,                      -- Zeigt die Uhrzeit des Slots
  z.name AS impfzentrum,            -- Zeigt den Namen des Impfzentrums
  COUNT(b.id) AS gebuchte_termine,  -- Zählt die Anzahl der Buchungen für diesen Slot
  z.kapazitaet                      -- Zeigt die Kapazität des Impfzentrums
FROM slot s
LEFT JOIN buchung b ON s.id = b.slot_id -- Verknüpft die Slots mit Buchungen; auch Slots ohne Buchungen werden angezeigt (LEFT JOIN)
JOIN impfzentrum z ON s.impfzentrum_id = z.id -- Verknüpft die Slots mit dem entsprechenden Impfzentrum
WHERE s.slot_date = '2025-01-10' -- Filtert die Ergebnisse nach Slots, die am 10.01.2025 stattfinden
  AND z.name = 'Nemerb-Ost'      -- Filtert die Ergebnisse auf das Impfzentrum 'Nemerb-Ost'
GROUP BY s.id, z.name, z.kapazitaet; -- Gruppiert die Ergebnisse nach Slot-ID, Name des Impfzentrums und Kapazität
