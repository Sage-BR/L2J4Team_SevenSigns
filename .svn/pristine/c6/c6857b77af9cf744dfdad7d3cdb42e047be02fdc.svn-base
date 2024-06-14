DROP TABLE IF EXISTS `character_gacha_history`;
CREATE TABLE `character_gacha_history` (
  `char_id` int NOT NULL,
  `item_id` int NULL DEFAULT NULL,
  `item_count` bigint NULL DEFAULT NULL,
  `item_enchant` int NULL DEFAULT NULL,
  `item_rank` int NULL DEFAULT 1,
  `receive_time` bigint NOT NULL,
  PRIMARY KEY (`char_id`, `receive_time`) USING BTREE
) DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;