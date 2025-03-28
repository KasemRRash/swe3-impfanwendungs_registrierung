INSERT INTO impfzentrum_impfstoff (impfzentrum_id, impfstoff_id) VALUES
-- Biontech in Ost und Nord
((SELECT id FROM impfzentrum WHERE name = 'Nemerb-Ost'), (SELECT id FROM impfstoff WHERE name = 'Biontech')),
((SELECT id FROM impfzentrum WHERE name = 'Nemerb-Nord'), (SELECT id FROM impfstoff WHERE name = 'Biontech')),

-- Moderna in Süd und Ost
((SELECT id FROM impfzentrum WHERE name = 'Nemerb-Sued'), (SELECT id FROM impfstoff WHERE name = 'Moderna')),
((SELECT id FROM impfzentrum WHERE name = 'Nemerb-Ost'), (SELECT id FROM impfstoff WHERE name = 'Moderna')),

-- AstraZeneca nur in Nord
((SELECT id FROM impfzentrum WHERE name = 'Nemerb-Nord'), (SELECT id FROM impfstoff WHERE name = 'AstraZeneca')),

-- Johnson & Johnson nur in Süd
((SELECT id FROM impfzentrum WHERE name = 'Nemerb-Sued'), (SELECT id FROM impfstoff WHERE name = 'Johnson & Johnson'))

-- Impfstoffe für Nemerb-West hinzufügen
INSERT INTO impfzentrum_impfstoff (impfzentrum_id, impfstoff_id) VALUES
((SELECT id FROM impfzentrum WHERE name = 'Nemerb-West'), (SELECT id FROM impfstoff WHERE name = 'Biontech')),
((SELECT id FROM impfzentrum WHERE name = 'Nemerb-West'), (SELECT id FROM impfstoff WHERE name = 'Moderna')),
((SELECT id FROM impfzentrum WHERE name = 'Nemerb-West'), (SELECT id FROM impfstoff WHERE name = 'AstraZeneca'));
