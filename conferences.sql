/*
Created		19.03.2019
Modified		21.05.2019
Project
Model
Company
Author
Version
Database		mySQL 5
*/

DROP DATABASE IF EXISTS conferences;
CREATE DATABASE conferences;
USE conferences;

Create table `SysUser` (
	`userID` Int NOT NULL AUTO_INCREMENT,
	`login` Varchar(30) NOT NULL,
	`passwd` Varchar(35) NOT NULL,
	`name` Varchar(100) NOT NULL,
	`surname` Varchar(100) NOT NULL,
	`email` Varchar(100) NOT NULL,
	`pesel` Varchar(11),
	`streetaddress` Varchar(200),
	`PostCode` Varchar(6),
	`town` Varchar(100),
	`isAuthor` Bit(1) NOT NULL DEFAULT 0,
	`isReviewer` Bit(1) NOT NULL DEFAULT 0,
	`isRedactor` Bit(1) NOT NULL DEFAULT 0,
	`isAdmin` Bit(1) NOT NULL DEFAULT 0,
	UNIQUE (`userID`),
	UNIQUE (`login`),
	UNIQUE (`pesel`),
	Primary Key (`userID`)) ENGINE = MyISAM;

Create table `Specialization` (
	`specializationID` Int NOT NULL AUTO_INCREMENT,
	`name` Varchar(50) NOT NULL,
	`description` Varchar(255),
	UNIQUE (`specializationID`),
	UNIQUE (`name`),
	Primary Key (`specializationID`)) ENGINE = MyISAM;

Create table `Review` (
	`reviewID` Int NOT NULL AUTO_INCREMENT,
	`ReviewerID` Int NOT NULL,
	`articleID` Int NOT NULL,
	`title` Varchar(80) NOT NULL,
	`Rating` Int NOT NULL,
	`filepath` Varchar(120) NOT NULL,
	UNIQUE (`reviewID`),
	Primary Key (`reviewID`)) ENGINE = MyISAM;

Create table `Article` (
	`articleID` Int NOT NULL AUTO_INCREMENT,
	`AuthorID` Int NOT NULL,
	`RedactorID` Int,
	`specializationID` Int,
	`title` Varchar(80) NOT NULL,
	`filepath` Varchar(120) NOT NULL,
	`Keywords` Varchar(120),
	`state` Enum('new', 'accepted', 'need correction', 'rejected', 'presented') NOT NULL DEFAULT 'new',
	`pathID` Int,
	`startTime` Datetime,
	`endTime` Datetime,
	UNIQUE (`articleID`),
	Primary Key (`articleID`)) ENGINE = MyISAM;

Create table `Conference` (
	`conferenceID` Int NOT NULL AUTO_INCREMENT,
	`description` Varchar(255),
	`startDate` Datetime,
	`endDate` Datetime,
	Primary Key (`conferenceID`)) ENGINE = MyISAM;

Create table `Path` (
	`pathID` Int NOT NULL AUTO_INCREMENT,
	`conferenceID` Int NOT NULL,
	`specializationID` Int NOT NULL,
	`startDate` Datetime,
	`endDate` Datetime,
	`redactorID` Int NOT NULL,
	Primary Key (`pathID`)) ENGINE = MyISAM;

Alter table `Review` add Foreign Key (`ReviewerID`) references `SysUser` (`userID`) on delete  restrict on update  restrict;
Alter table `Article` add Foreign Key (`AuthorID`) references `SysUser` (`userID`) on delete  restrict on update  restrict;
Alter table `Article` add Foreign Key (`RedactorID`) references `SysUser` (`userID`) on delete  restrict on update  restrict;
Alter table `Path` add Foreign Key (`RedactorID`) references `SysUser` (`userID`) on delete  restrict on update  restrict;
Alter table `Path` add Foreign Key (`specializationID`) references `Specialization` (`specializationID`) on delete  restrict on update  restrict;
Alter table `Article` add Foreign Key (`specializationID`) references `Specialization` (`specializationID`) on delete  restrict on update  restrict;
Alter table `Review` add Foreign Key (`articleID`) references `Article` (`articleID`) on delete  restrict on update  restrict;
Alter table `Path` add Foreign Key (`conferenceID`) references `Conference` (`conferenceID`) on delete  restrict on update  restrict;
Alter table `Article` add Foreign Key (`pathID`) references `Path` (`pathID`) on delete  restrict on update  restrict;



INSERT INTO sysuser (login, passwd, name, surname, email, isAdmin) 			VALUES ('admin', '5F4DCC3B5AA765D61D8327DEB882CF99', 'admin', 'admin', 'admin@domain.com', 1);					-- password
INSERT INTO sysuser (login, passwd, name, surname, email, isAuthor) 	  VALUES ('autor1', '7C6A180B36896A0A8C02787EEAFB0E4C', 'autor1', 'autor1', 'autor1@domain.com', 1);				-- password1
INSERT INTO sysuser (login, passwd, name, surname, email, isAuthor)     VALUES ('autor2', '6CB75F652A9B52798EB6CF2201057C73', 'autor2', 'autor2', 'autor2@domain.com', 1);				-- password2
INSERT INTO sysuser (login, passwd, name, surname, email, isRedactor) 	VALUES ('redactor1', '819B0643D6B89DC9B579FDFC9094F28E', 'redactor1', 'redactor1', 'redactor1@domain.com',1);	-- password3
INSERT INTO sysuser (login, passwd, name, surname, email, isReviewer, isAuthor)   VALUES ('reviewer1', '34CC93ECE0BA9E3F6F235D4AF979B16C', 'reviewer1', 'reviewer1', 'reviewer1@domain.com', 1, 1);	-- password4
INSERT INTO sysuser (login, passwd, name, surname, email, isReviewer) 	VALUES ('reviewer2', 'DB0EDD04AAAC4506F7EDAB03AC855D56', 'reviewer2', 'reviewer2', 'reviewer2@domain.com', 1);	-- password5

INSERT INTO article (AuthorID, title, filepath) VALUES (2, 'authors1article', 'path/to/article1');
INSERT INTO article (AuthorID, RedactorID, title, filepath) VALUES (2, 4, 'authors1article2', 'path/to/article2');
INSERT INTO article (AuthorID, RedactorID, title, filepath) VALUES (5, 4, 'reviewers1article', 'path/to/article3');
INSERT INTO review  (ReviewerID, articleID, title, rating, filepath) VALUES (5, 2, 'article2review', 10, 'path/to/article2review');

CREATE VIEW admins AS SELECT * FROM sysuser WHERE isAdmin is TRUE;
CREATE VIEW authors AS SELECT * FROM sysuser WHERE isAuthor is TRUE;
CREATE VIEW redactors AS SELECT * FROM sysuser WHERE isRedactor is TRUE;
CREATE VIEW reviewers AS SELECT * FROM sysuser WHERE isReviewer is TRUE;