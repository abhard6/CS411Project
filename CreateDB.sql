CREATE TABLE `Trend` (
`value` varchar(255) NOT NULL,
`created_at` datetime NOT NULL,
`trending_till` datetime, 
PRIMARY KEY (`value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Post` (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`content` varchar(500) NOT NULL,
`latitude` float NOT NULL,
`longitude` float NOT NULL,
`sentiment` int(11) NOT NULL,
`source` varchar(255) NOT NULL,
`timestamp` datetime NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `trended_post` (
`post_id` bigint(20) NOT NULL,
`trend_id` varchar(255) NOT NULL,
KEY `FK_jqwi82fkhchbp32jgln1qq76m` (`trend_id`),
KEY `FK_jk8e058l6d20lj54mosty0opw` (`post_id`),
FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
FOREIGN KEY (`trend_id`) REFERENCES `trend` (`value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
