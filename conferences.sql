/*
Created		19.03.2019
Modified		16.05.2019
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
	`name` Varchar(20) NOT NULL,
	`surname` Varchar(30) NOT NULL,
	`pesel` Varchar(11),
	`address` Varchar(100),
	`email` Varchar(40) NOT NULL,
	`login` Varchar(30) NOT NULL,
	`passwd` Varchar(35) NOT NULL,
	`interests` Varchar(100),
	`specializationID` Int,
	`isAuthor` Bit(1) NOT NULL DEFAULT 0,
	`isReviewer` Bit(1) NOT NULL DEFAULT 0,
	`isRedactor` Bit(1) NOT NULL DEFAULT 0,
	`isAdmin` Bit(1) NOT NULL DEFAULT 0,
	UNIQUE (`userID`),
	UNIQUE (`pesel`),
	UNIQUE (`login`),
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
	`title` Varchar(50) NOT NULL,
	`Rating` Int NOT NULL,
	`filepath` Varchar(80) NOT NULL,
	UNIQUE (`reviewID`),
 Primary Key (`reviewID`)) ENGINE = MyISAM;

Create table `Article` (
	`articleID` Int NOT NULL AUTO_INCREMENT,
	`AuthorID` Int NOT NULL,
	`RedactorID` Int NOT NULL,
	`title` Varchar(50) NOT NULL,
	`filepath` Varchar(80) NOT NULL,
	`Keywords` Varchar(120),
	`state` Enum('new', 'accepted', 'need correction', 'rejected', 'presented') NOT NULL DEFAULT 'new',
	`pathID` Int,
	`startTime` Datetime,
	`endTime` Datetime,
	UNIQUE (`articleID`),
 Primary Key (`articleID`)) ENGINE = MyISAM;

Create table `Conference` (
	`conferenceID` Int NOT NULL AUTO_INCREMENT,
	`description` Varchar(40),
	`startDate` Datetime NOT NULL,
	`endDate` Datetime NOT NULL,
 Primary Key (`conferenceID`)) ENGINE = MyISAM;

Create table `Path` (
	`pathID` Int NOT NULL AUTO_INCREMENT,
	`conferenceID` Int NOT NULL,
	`specializationID` Int NOT NULL,
	`startDate` Datetime NOT NULL,
	`endDate` Datetime NOT NULL,
	`userID` Int NOT NULL,
 Primary Key (`pathID`)) ENGINE = MyISAM;


Alter table `Review` add Foreign Key (`ReviewerID`) references `SysUser` (`userID`) on delete  restrict on update  restrict;
Alter table `Article` add Foreign Key (`AuthorID`) references `SysUser` (`userID`) on delete  restrict on update  restrict;
Alter table `Article` add Foreign Key (`RedactorID`) references `SysUser` (`userID`) on delete  restrict on update  restrict;
Alter table `Path` add Foreign Key (`userID`) references `SysUser` (`userID`) on delete  restrict on update  restrict;
Alter table `Path` add Foreign Key (`specializationID`) references `Specialization` (`specializationID`) on delete  restrict on update  restrict;
Alter table `SysUser` add Foreign Key (`specializationID`) references `Specialization` (`specializationID`) on delete  restrict on update  restrict;
Alter table `Review` add Foreign Key (`articleID`) references `Article` (`articleID`) on delete  restrict on update  restrict;
Alter table `Path` add Foreign Key (`conferenceID`) references `Conference` (`conferenceID`) on delete  restrict on update  restrict;
Alter table `Article` add Foreign Key (`pathID`) references `Path` (`pathID`) on delete  restrict on update  restrict;


INSERT INTO sysuser (name, surname, email, login, passwd, isAdmin) 		VALUES ('admin', 'admin', 'admin@domain.com', 'admin', 					'5F4DCC3B5AA765D61D8327DEB882CF99', 1);		-- password
INSERT INTO sysuser (name, surname, email, login, passwd, isAuthor) 	VALUES ('autor1', 'autor1', 'autor1@domain.com', 'autor1', 				'7C6A180B36896A0A8C02787EEAFB0E4C', 1);		-- password1
INSERT INTO sysuser (name, surname, email, login, passwd, isAuthor) 	VALUES ('autor2', 'autor2', 'autor12@domain.com', 'autor2', 			'6CB75F652A9B52798EB6CF2201057C73', 1);		-- password2
INSERT INTO sysuser (name, surname, email, login, passwd, isRedactor) 	VALUES ('redactor1', 'redactor1', 'redactor1@domain.com', 'redactor1', 	'819B0643D6B89DC9B579FDFC9094F28E', 1);		-- password3
