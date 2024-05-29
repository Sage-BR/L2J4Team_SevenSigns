DROP TABLE IF EXISTS `enchant_challenge_points_recharges`;
CREATE TABLE IF NOT EXISTS `enchant_challenge_points_recharges` (
  `charId` int(10) unsigned NOT NULL DEFAULT 0,
  `groupId` int(3) UNSIGNED NOT NULL DEFAULT 0,
  `optionIndex` INT NOT NULL,
  `count` INT NOT NULL,
  PRIMARY KEY (`charId`, `groupId`, `optionIndex`)
) DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;