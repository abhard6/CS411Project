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
FOREIGN KEY (`post_id`) REFERENCES `Post` (`id`), 
FOREIGN KEY (`trend_id`) REFERENCES `Trend` (`value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Query` (
`id` bigint(20) PRIMARY KEY AUTO_INCREMENT,
`day_chosen` varchar(255),
`trend` varchar(255),
`latitude_top` float,
`latitude_bottom` float,
`longitude_left` float,
`longitude_right` float,
FOREIGN KEY (`trend`) REFERENCES `Trend` (`value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
   
CREATE TABLE Users (
username VARCHAR(45) PRIMARY KEY,
password VARCHAR(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE user_query (
username VARCHAR(45) NOT NULL,
query_id bigint(20),
FOREIGN KEY (username) REFERENCES Users(username),
FOREIGN KEY (query_id) REFERENCES Query(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
