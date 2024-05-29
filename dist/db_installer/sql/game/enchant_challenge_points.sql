DROP TABLE IF EXISTS `enchant_challenge_points`;
CREATE TABLE IF NOT EXISTS `enchant_challenge_points` (
  `charId` int(10) unsigned NOT NULL DEFAULT 0,
  `groupId` INT NOT NULL,
  `points` INT NOT NULL,
  PRIMARY KEY (`charId`,`groupId`)
) DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;