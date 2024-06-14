DROP TABLE IF EXISTS `huntpass`;	
CREATE TABLE IF NOT EXISTS `huntpass` (
  `account_name` VARCHAR(45) NOT NULL DEFAULT '',
  `current_step` INT NOT NULL DEFAULT 0,
  `points` INT NOT NULL DEFAULT 0,
  `reward_step` INT NOT NULL DEFAULT 0,
  `is_premium` BOOLEAN NOT NULL DEFAULT FALSE,
  `premium_reward_step` INT NOT NULL DEFAULT 0,
  `sayha_points_available` INT NOT NULL DEFAULT 0,
  `sayha_points_used` INT NOT NULL DEFAULT 0,
  `unclaimed_reward` BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`account_name`)
) DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
