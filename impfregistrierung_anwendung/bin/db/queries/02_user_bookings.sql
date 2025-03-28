-- zeige alle Buchungen zusammen mit den Zeitslots, Impfstationen und Wunsch-Impfstoffen für User y

SELECT
  b.id AS buchung_id,           -- Zeigt die ID der Buchung
  u.email AS benutzer,          -- Zeigt die E-Mail-Adresse des Benutzers
  s.slot_date,                  -- Zeigt das Datum des gebuchten Slots
  s.slot_time,                  -- Zeigt die Uhrzeit des gebuchten Slots
  z.name AS impfzentrum,        -- Zeigt den Namen des Impfzentrums, in dem die Buchung erfolgt
  i.name AS impfstoff           -- Zeigt den Namen des gebuchten Impfstoffs
FROM buchung b
JOIN user u ON b.user_id = u.id           -- Verknüpft die Buchungen mit den Benutzern (benutzer_id = user_id)
JOIN slot s ON b.slot_id = s.id           -- Verknüpft die Buchungen mit den Slots (slot_id = slot_id)
JOIN impfzentrum z ON s.impfzentrum_id = z.id -- Verknüpft die Slots mit den Impfzentren (impfzentrum_id = impfzentrum_id)
JOIN impfstoff i ON b.impfstoff_id = i.id -- Verknüpft die Buchungen mit den Impfstoffen (impfstoff_id = impfstoff_id)
WHERE u.email = 'theotester@example.com'; -- Filtert die Ergebnisse nach Buchungen des Benutzers mit der angegebenen E-Mail-Adresse
