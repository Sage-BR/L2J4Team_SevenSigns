DROP TABLE IF EXISTS `world_exchange_items`;
CREATE TABLE `world_exchange_items`  (
  `world_exchange_id` INT NOT NULL DEFAULT 0,
  `item_object_id` INT NOT NULL DEFAULT 0,
  `item_status` smallint(6) NOT NULL,
  `category_id` smallint(6) NOT NULL,
  `price` BIGINT UNSIGNED NOT NULL DEFAULT 0,
  `old_owner_id` INT NOT NULL DEFAULT 0,
  `start_time` bigint(13) unsigned NOT NULL DEFAULT '0',
  `end_time` bigint(13) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`world_exchange_id`)
) DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;
