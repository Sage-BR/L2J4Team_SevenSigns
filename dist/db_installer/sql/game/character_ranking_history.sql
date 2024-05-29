DROP TABLE IF EXISTS `character_ranking_history`;
CREATE TABLE IF NOT EXISTS `character_ranking_history` (
  `charId` int(20) NOT NULL,
  `day` int(20) NOT NULL,
  `ranking` int(20) NOT NULL,
  `exp` bigint(20) NOT NULL,
  PRIMARY KEY (`charId`,`day`)
) DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;