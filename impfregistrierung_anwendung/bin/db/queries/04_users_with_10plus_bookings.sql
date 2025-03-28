-- zeige alle USer, die mehr als 10 Buchungen haben sortiert nach dem Datum und der Zeit des zugeordneten Zeitslots

SELECT
  u.email AS benutzer,                     -- Zeigt die E-Mail-Adresse des Benutzers
  COUNT(b.id) AS buchungen                 -- Zählt die Anzahl der Buchungen für den Benutzer
FROM user u
JOIN buchung b ON u.id = b.user_id         -- Verknüpft die Benutzer mit ihren Buchungen
GROUP BY u.id                              -- Gruppiert die Ergebnisse nach Benutzer-ID
HAVING COUNT(b.id) > 10                    -- Filtert die Benutzer mit mehr als 10 Buchungen
ORDER BY (
  SELECT MIN(s.slot_date)                  -- Findet das früheste Datum aller gebuchten Slots des Benutzers
  FROM buchung b2
  JOIN slot s ON b2.slot_id = s.id         -- Verknüpft die Buchungen mit den Slots, um das Datum zu erhalten
  WHERE b2.user_id = u.id                  -- Bezieht sich auf denselben Benutzer wie in der Hauptabfrage
), (
  SELECT MIN(s.slot_time)                  -- Findet die früheste Uhrzeit aller gebuchten Slots des Benutzers
  FROM buchung b3
  JOIN slot s ON b3.slot_id = s.id         -- Verknüpft die Buchungen mit den Slots, um die Uhrzeit zu erhalten
  WHERE b3.user_id = u.id                  -- Bezieht sich auf denselben Benutzer wie in der Hauptabfrage
);
