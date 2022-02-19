ALTER TABLE `movies`
	ADD COLUMN `avg_rating` DOUBLE NOT NULL DEFAULT '0' AFTER `release_date`;