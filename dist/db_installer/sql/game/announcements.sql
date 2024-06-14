DROP TABLE IF EXISTS `announcements`;
CREATE TABLE IF NOT EXISTS `announcements` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `type` int(11) NOT NULL,
  `initial` bigint(20) NOT NULL DEFAULT 0,
  `delay` bigint(20) NOT NULL DEFAULT 0,
  `repeat` int(11) NOT NULL DEFAULT 0,
  `author` text NOT NULL,
  `content` text NOT NULL,
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO announcements (`type`, `author`, `content`) VALUES 
(0, 'Admin', 'Thanks for using L2j4Team!'),
(0, 'Admin', '[=http://forum.4teambr.com/=]');
