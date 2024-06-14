DROP TABLE IF EXISTS `mable_game`;
CREATE TABLE `mable_game` (
  `account_name` varchar(255) NOT NULL,
  `round` int(11) NOT NULL,
  `current_cell_id` int(11) NOT NULL,
  `remain_common_dice` int(11) NOT NULL,
  `remain_prison_rolls` int(11) NOT NULL,
  PRIMARY KEY (`account_name`)
) DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
