-- INSERT INTO buchung (user_id, slot_id, impfstoff_id) VALUES --
-- (1, 1, 1), -- Theo bucht Moderna in Nemerb-Süd
-- (2, 2, 2), -- Bruno bucht BioNTech in Nemerb-Süd
-- (3, 3, 2), -- Tolga bucht BioNTech in Nemerb-Nord
-- (1, 4, 3), -- Theo bucht AstraZeneca in Nemerb-Nord
-- (2, 5, 3), -- Bruno bucht AstraZeneca in Nevahremerb-Nord
-- (3, 6, 1), -- Tolga bucht Moderna in Nevahremerb-Nord
-- (1, 7, 1), -- Theo bucht Moderna in Nevahremerb-Süd
-- (2, 8, 1); -- Bruno bucht Moderna in Nevahremerb-Süd

-- (3, 3, 3), -- Tolga bucht AstraZeneca in Nemerb-Nord
-- (4, 4, 4), -- Anna bucht Johnson & Johnson in Nemerb-Nord
-- (5, 5, 5), -- Peter bucht Novavax in Nevahremerb-Nord
-- (6, 6, 6), -- Clara bucht Valneva in Nevahremerb-Nord
-- (7, 7, 1), -- Reinhold bucht Moderna in Nevahremerb-Süd
-- (8, 8, 2); -- Jens bucht BioNTech in Nevahremerb-Süd

INSERT INTO buchung (user_id, slot_id, impfstoff_id) VALUES
(1, 1, 1),    -- User 1 (annaschmidt@example.com) bucht Slot 1 (09:00:00, Impfzentrum 1) mit Impfstoff 1 (Biontech)
(2, 256, 2),  -- User 2 (peterschmidt@example.com) bucht Slot 256 (10:00:00, Impfzentrum 2) mit Impfstoff 2 (Moderna)
(3, 511, 3),  -- User 3 (clarastern@example.com) bucht Slot 511 (08:30:00, Impfzentrum 3) mit Impfstoff 3 (AstraZeneca)
(4, 638, 4),  -- User 4 (reinholdbaum@example.com) bucht Slot 638 (11:00:00, Impfzentrum 4) mit Impfstoff 4 (Johnson & Johnson)
(5, 57, 2);   -- User 5 (jensbusch@example.com) bucht Slot 57 (11:00:00, Impfzentrum 1) mit Impfstoff 2 (Moderna)


