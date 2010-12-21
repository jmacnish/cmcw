Welcome to Grails 1.3.6 - http://grails.org/
Licensed under Apache Standard License 2.0
Grails home is set to: /usr/local/java/grails-1.3.6

Base Directory: /home/jmacnish/IdeaProjects/cmcw
Resolving dependencies...
Dependencies resolved in 1035ms.
Running script /home/jmacnish/.grails/1.3.6/projects/cmcw/plugins/liquibase-1.9.3.5/scripts/MigrateSql.groovy
Environment set to development
Connecting to database with URL: jdbc:mysql://localhost/cmcw_dev
-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: changelog.xml
-- Ran at: 12/20/10 11:59 PM
-- Against: cmcw@jdbc:mysql://localhost/cmcw_dev
-- LiquiBase version: 1.9.3
-- *********************************************************************

-- Create Database Lock Table
CREATE TABLE `DATABASECHANGELOGLOCK` (`ID` INT NOT NULL, `LOCKED` TINYINT(1) NOT NULL, `LOCKGRANTED` DATETIME, `LOCKEDBY` VARCHAR(255), CONSTRAINT `PK_DATABASECHANGELOGLOCK` PRIMARY KEY (`ID`));

INSERT INTO `DATABASECHANGELOGLOCK` (`ID`, `LOCKED`) VALUES (1, 0);

SELECT LOCKED FROM `DATABASECHANGELOGLOCK` WHERE `ID`=1;

-- Lock Database
UPDATE `DATABASECHANGELOGLOCK` SET `LOCKEDBY` = 'fe80:0:0:0:21f:c6ff:fe3b:8c85%2 (fe80:0:0:0:21f:c6ff:fe3b:8c85%2)', `LOCKGRANTED` = '2010-12-20 23:59:47.211', `LOCKED` = 1 WHERE ID  = 1;

-- Create Database Change Log Table
CREATE TABLE `DATABASECHANGELOG` (`ID` VARCHAR(63) NOT NULL, `AUTHOR` VARCHAR(63) NOT NULL, `FILENAME` VARCHAR(200) NOT NULL, `DATEEXECUTED` DATETIME NOT NULL, `MD5SUM` VARCHAR(32), `DESCRIPTION` VARCHAR(255), `COMMENTS` VARCHAR(255), `TAG` VARCHAR(255), `LIQUIBASE` VARCHAR(10), CONSTRAINT `PK_DATABASECHANGELOG` PRIMARY KEY (`ID`, `AUTHOR`, `FILENAME`));

-- Changeset changelog.xml::1292918006353-1::jmacnish (generated)::(MD5Sum: b9c3cbdfd860234ccbfa49822257cfa)
CREATE TABLE `available_format` (`id` BIGINT AUTO_INCREMENT  NOT NULL, `version` BIGINT NOT NULL, `available_from` DATETIME NOT NULL, `available_until` DATETIME NOT NULL, `format_id` BIGINT NOT NULL, CONSTRAINT `PK_AVAILABLE_FORMAT` PRIMARY KEY (`id`));

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Create Table', '', 'b9c3cbdfd860234ccbfa49822257cfa', '1292918006353-1', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-2::jmacnish (generated)::(MD5Sum: e82214d7f51fb41c6a4c5101fa8369a)
CREATE TABLE `catalog_import` (`id` BIGINT AUTO_INCREMENT  NOT NULL, `version` BIGINT NOT NULL, `date_created` DATETIME NOT NULL, `date_imported` DATETIME, `etag` VARCHAR(255) NOT NULL, `file` VARCHAR(255) NOT NULL, `last_updated` DATETIME NOT NULL, CONSTRAINT `PK_CATALOG_IMPORT` PRIMARY KEY (`id`));

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Create Table', '', 'e82214d7f51fb41c6a4c5101fa8369a', '1292918006353-2', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-3::jmacnish (generated)::(MD5Sum: fc31c4b5cd354096159824d53ea2b3d)
CREATE TABLE `format` (`id` BIGINT AUTO_INCREMENT  NOT NULL, `version` BIGINT NOT NULL, `netflix_label` VARCHAR(255) NOT NULL, `type` VARCHAR(255) NOT NULL, CONSTRAINT `PK_FORMAT` PRIMARY KEY (`id`));

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Create Table', '', 'fc31c4b5cd354096159824d53ea2b3d', '1292918006353-3', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-4::jmacnish (generated)::(MD5Sum: 857aaaec851f12fd12a74e5734bd7e34)
CREATE TABLE `openid` (`id` BIGINT AUTO_INCREMENT  NOT NULL, `version` BIGINT NOT NULL, `url` VARCHAR(255) NOT NULL, `user_id` BIGINT NOT NULL, CONSTRAINT `PK_OPENID` PRIMARY KEY (`id`));

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Create Table', '', '857aaaec851f12fd12a74e5734bd7e34', '1292918006353-4', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-5::jmacnish (generated)::(MD5Sum: 75c1f511b9ef7fd589c4e0dd4173ba1)
CREATE TABLE `role` (`id` BIGINT AUTO_INCREMENT  NOT NULL, `version` BIGINT NOT NULL, `authority` VARCHAR(255) NOT NULL, CONSTRAINT `PK_ROLE` PRIMARY KEY (`id`));

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Create Table', '', '75c1f511b9ef7fd589c4e0dd4173ba1', '1292918006353-5', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-6::jmacnish (generated)::(MD5Sum: e8c1d0c3ccace6bc3a66dc8c3f73e)
CREATE TABLE `user` (`id` BIGINT AUTO_INCREMENT  NOT NULL, `version` BIGINT NOT NULL, `account_expired` BIT NOT NULL, `account_locked` BIT NOT NULL, `enabled` BIT NOT NULL, `password` VARCHAR(255) NOT NULL, `password_expired` BIT NOT NULL, `username` VARCHAR(255) NOT NULL, CONSTRAINT `PK_USER` PRIMARY KEY (`id`));

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Create Table', '', 'e8c1d0c3ccace6bc3a66dc8c3f73e', '1292918006353-6', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-7::jmacnish (generated)::(MD5Sum: 806598ae1fcce3c082e580fb52ddbcb)
CREATE TABLE `user_role` (`role_id` BIGINT NOT NULL, `user_id` BIGINT NOT NULL);

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Create Table', '', '806598ae1fcce3c082e580fb52ddbcb', '1292918006353-7', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-8::jmacnish (generated)::(MD5Sum: 1e4b8874c8587e9699b848c748e1e79)
CREATE TABLE `video` (`id` BIGINT AUTO_INCREMENT  NOT NULL, `version` BIGINT NOT NULL, `box_art_large_url` VARCHAR(255), `content_hash` VARCHAR(40), `date_created` DATETIME NOT NULL, `last_updated` DATETIME NOT NULL, `netflix_id` VARCHAR(255) NOT NULL, `title` VARCHAR(255) NOT NULL, CONSTRAINT `PK_VIDEO` PRIMARY KEY (`id`));

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Create Table', '', '1e4b8874c8587e9699b848c748e1e79', '1292918006353-8', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-9::jmacnish (generated)::(MD5Sum: d3f5adc7daabba83e2527933534bdaa)
CREATE TABLE `video_available_format` (`video_available_formats_id` BIGINT, `available_format_id` BIGINT);

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Create Table', '', 'd3f5adc7daabba83e2527933534bdaa', '1292918006353-9', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-10::jmacnish (generated)::(MD5Sum: 575856d0e490ddb7d17a9eefa9daa83)
CREATE TABLE `video_back` (`id` BIGINT DEFAULT 0 NOT NULL, `version` BIGINT NOT NULL, `available_from` DATETIME, `available_until` DATETIME, `box_art_large_url` VARCHAR(255), `content_hash` VARCHAR(40), `date_created` DATETIME NOT NULL, `last_updated` DATETIME NOT NULL, `netflix_id` VARCHAR(255) NOT NULL, `title` VARCHAR(255) NOT NULL);

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Create Table', '', '575856d0e490ddb7d17a9eefa9daa83', '1292918006353-10', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-11::jmacnish (generated)::(MD5Sum: 535884c6bddc461ff5e7d63560ed4a8b)
CREATE TABLE `video_shadow` (`bluray_available_from` DATETIME, `bluray_available_until` DATETIME, `dvd_available_from` DATETIME, `dvd_available_until` DATETIME, `instant_available_from` DATETIME, `instant_available_until` DATETIME, `netflix_id` VARCHAR(255) NOT NULL, `title` VARCHAR(255) NOT NULL, `content_hash` VARCHAR(40) NOT NULL);

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Create Table', '', '535884c6bddc461ff5e7d63560ed4a8b', '1292918006353-11', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-12::jmacnish (generated)::(MD5Sum: 405bdfb3d7e130244e358e777668728b)
CREATE TABLE `video_type` (`id` BIGINT AUTO_INCREMENT  NOT NULL, `version` BIGINT NOT NULL, `netflix_identifier` VARCHAR(255) NOT NULL, CONSTRAINT `PK_VIDEO_TYPE` PRIMARY KEY (`id`));

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Create Table', '', '405bdfb3d7e130244e358e777668728b', '1292918006353-12', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-13::jmacnish (generated)::(MD5Sum: ee1ac088ae07e757fc7a4a7fe9df5)
ALTER TABLE `user_role` ADD PRIMARY KEY (`role_id`, `user_id`);

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Add Primary Key', '', 'ee1ac088ae07e757fc7a4a7fe9df5', '1292918006353-13', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-14::jmacnish (generated)::(MD5Sum: 5c88291212cd1754c7a2f695ea4fdf80)
CREATE INDEX `available_index` ON `available_format`(`available_from`, `available_until`);

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Create Index', '', '5c88291212cd1754c7a2f695ea4fdf80', '1292918006353-14', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-15::jmacnish (generated)::(MD5Sum: 79e434ba5b3fc281f45bedc06f5f868b)
CREATE UNIQUE INDEX `etag` ON `catalog_import`(`etag`);

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Create Index', '', '79e434ba5b3fc281f45bedc06f5f868b', '1292918006353-15', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-16::jmacnish (generated)::(MD5Sum: 9c87ea4afc642e641fd9811aed410ee)
CREATE UNIQUE INDEX `file` ON `catalog_import`(`file`);

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Create Index', '', '9c87ea4afc642e641fd9811aed410ee', '1292918006353-16', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-17::jmacnish (generated)::(MD5Sum: 14ca5fe2fbd45ddcb8ba8cee8310d62)
CREATE UNIQUE INDEX `netflix_label` ON `format`(`netflix_label`);

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Create Index', '', '14ca5fe2fbd45ddcb8ba8cee8310d62', '1292918006353-17', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-18::jmacnish (generated)::(MD5Sum: ec4c78d0311d876884bbfacc4a446712)
CREATE UNIQUE INDEX `type` ON `format`(`type`);

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Create Index', '', 'ec4c78d0311d876884bbfacc4a446712', '1292918006353-18', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-19::jmacnish (generated)::(MD5Sum: d755aa514dbed3a2c4b91eb33ef53e92)
CREATE UNIQUE INDEX `url` ON `openid`(`url`);

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Create Index', '', 'd755aa514dbed3a2c4b91eb33ef53e92', '1292918006353-19', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-20::jmacnish (generated)::(MD5Sum: 3dabbcc6e0f941fbafa1fcc42e0b356)
CREATE UNIQUE INDEX `authority` ON `role`(`authority`);

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Create Index', '', '3dabbcc6e0f941fbafa1fcc42e0b356', '1292918006353-20', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-21::jmacnish (generated)::(MD5Sum: 20cbd75233ad10ca5651bd0a8d0f2b5)
CREATE UNIQUE INDEX `username` ON `user`(`username`);

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Create Index', '', '20cbd75233ad10ca5651bd0a8d0f2b5', '1292918006353-21', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-22::jmacnish (generated)::(MD5Sum: 7214498ae268ec749e9da1c663254ad8)
CREATE INDEX `ContentHash_Idx` ON `video`(`content_hash`);

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Create Index', '', '7214498ae268ec749e9da1c663254ad8', '1292918006353-22', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-23::jmacnish (generated)::(MD5Sum: 42848cf75a65ea1832f8f79342cf84)
CREATE UNIQUE INDEX `netflix_id` ON `video`(`netflix_id`);

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Create Index', '', '42848cf75a65ea1832f8f79342cf84', '1292918006353-23', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-24::jmacnish (generated)::(MD5Sum: 13239b7c4f604f664b4ca78a8ad5fd97)
CREATE INDEX `content_hash_idx` ON `video_shadow`(`content_hash`);

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Create Index', '', '13239b7c4f604f664b4ca78a8ad5fd97', '1292918006353-24', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-25::jmacnish (generated)::(MD5Sum: 3f7aabc8c9bc2c1a2d68983c3cf0b837)
CREATE UNIQUE INDEX `netflix_id_idx` ON `video_shadow`(`netflix_id`);

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Create Index', '', '3f7aabc8c9bc2c1a2d68983c3cf0b837', '1292918006353-25', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-26::jmacnish (generated)::(MD5Sum: ba1ce50dc25bd3f8226dc6d5665153)
CREATE UNIQUE INDEX `netflix_identifier` ON `video_type`(`netflix_identifier`);

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Create Index', '', 'ba1ce50dc25bd3f8226dc6d5665153', '1292918006353-26', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-27::jmacnish (generated)::(MD5Sum: e0fe60caf3457065a4a1333c7e4dfd4a)
ALTER TABLE `available_format` ADD CONSTRAINT `FK586B662DA2F7000A` FOREIGN KEY (`format_id`) REFERENCES `format`(`id`);

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Add Foreign Key Constraint', '', 'e0fe60caf3457065a4a1333c7e4dfd4a', '1292918006353-27', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-28::jmacnish (generated)::(MD5Sum: 168f7bcde8f72299973f28c14b02e32)
ALTER TABLE `openid` ADD CONSTRAINT `FKC3C3C8E5CB5C2B8A` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`);

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Add Foreign Key Constraint', '', '168f7bcde8f72299973f28c14b02e32', '1292918006353-28', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-29::jmacnish (generated)::(MD5Sum: 71945fa22ff6d9b0b2127955682969d0)
ALTER TABLE `user_role` ADD CONSTRAINT `FK143BF46A263167AA` FOREIGN KEY (`role_id`) REFERENCES `role`(`id`);

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Add Foreign Key Constraint', '', '71945fa22ff6d9b0b2127955682969d0', '1292918006353-29', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-30::jmacnish (generated)::(MD5Sum: 6aa8a8d8e3f3de6c9fc93ef779e3ec0)
ALTER TABLE `user_role` ADD CONSTRAINT `FK143BF46ACB5C2B8A` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`);

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Add Foreign Key Constraint', '', '6aa8a8d8e3f3de6c9fc93ef779e3ec0', '1292918006353-30', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-31::jmacnish (generated)::(MD5Sum: ffe71bab9c8073993bdd423845d8428d)
ALTER TABLE `video_available_format` ADD CONSTRAINT `FK4E86D971716EF4DD` FOREIGN KEY (`available_format_id`) REFERENCES `available_format`(`id`);

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Add Foreign Key Constraint', '', 'ffe71bab9c8073993bdd423845d8428d', '1292918006353-31', 'changelog.xml');

-- Changeset changelog.xml::1292918006353-32::jmacnish (generated)::(MD5Sum: e448c751de6d837b35b3a330eac0fd)
ALTER TABLE `video_available_format` ADD CONSTRAINT `FK4E86D97143F06A23` FOREIGN KEY (`video_available_formats_id`) REFERENCES `video`(`id`);

INSERT INTO `DATABASECHANGELOG` (`DATEEXECUTED`, `AUTHOR`, `LIQUIBASE`, `DESCRIPTION`, `COMMENTS`, `MD5SUM`, `ID`, `FILENAME`) VALUES (NOW(), 'jmacnish (generated)', '1.9.3', 'Add Foreign Key Constraint', '', 'e448c751de6d837b35b3a330eac0fd', '1292918006353-32', 'changelog.xml');

-- Release Database Lock
UPDATE `DATABASECHANGELOGLOCK` SET `LOCKEDBY` = NULL, `LOCKGRANTED` = NULL, `LOCKED` = 0 WHERE  ID = 1;

