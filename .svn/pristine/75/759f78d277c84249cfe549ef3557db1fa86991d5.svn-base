DROP TABLE IF EXISTS `character_revenge_history`;
CREATE TABLE IF NOT EXISTS `character_revenge_history` (
  `charId` int(10) UNSIGNED NOT NULL,
  `type` int(10) NOT NULL,
  `killer_name` VARCHAR(35),
  `killer_clan` VARCHAR(45),
  `killer_level` int UNSIGNED NOT NULL,
  `killer_race` int NOT NULL DEFAULT 0,
  `killer_class` int NOT NULL DEFAULT 0,
  `victim_name` VARCHAR(35),
  `victim_clan` VARCHAR(45),
  `victim_level` int UNSIGNED NOT NULL,
  `victim_race` int NOT NULL DEFAULT 0,
  `victim_class` int NOT NULL DEFAULT 0,
  `shared` TINYINT(1) NOT NULL DEFAULT 0,
  `show_location_remaining` int NOT NULL DEFAULT 0,
  `teleport_remaining` int NOT NULL DEFAULT 0,
  `shared_teleport_remaining` int NOT NULL DEFAULT 0,
  `kill_time` BIGINT(10) UNSIGNED NOT NULL,
  `share_time` BIGINT(10) UNSIGNED NOT NULL
) DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
