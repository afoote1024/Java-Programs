CREATE DATABASE IF NOT EXISTS IS345
;

USE IS345;

DROP TABLE IF EXISTS tblCourse
;
DROP TABLE IF EXISTS tblStudent
;


CREATE TABLE tblCourse (
	CourseID varchar (10) NOT NULL,
	CourseName varchar (50) NOT NULL,
	PRIMARY KEY (CourseID)
) TYPE=INNODB
;

CREATE TABLE tblStudent (
	ID integer NOT NULL,
	StudentName varchar (50) NOT NULL,
	Total integer,
	Percentage real,
	CourseID varchar (10) NOT NULL,
	INDEX (ID),
	FOREIGN KEY (CourseID) REFERENCES tblCourse (CourseID)
) TYPE=INNODB
;


INSERT INTO tblCourse VALUES ('IS110B','Intro to Computers')
;

INSERT INTO tblCourse VALUES ('IS115A','Spreadsheets')
;

INSERT INTO tblCourse VALUES ('IS210B','Databases')
;

INSERT INTO tblStudent VALUES (0,'Jane Evans',289,0.00,'IS110B')
;

INSERT INTO tblStudent VALUES (1,'Carol Lucas',343,85.75,'IS110B')
;

INSERT INTO tblStudent VALUES (2,'Lynn Garvey',370,92.50,'IS110B')
;

INSERT INTO tblStudent VALUES (3,'Kim Grey',334,83.50,'IS110B')
;

INSERT INTO tblStudent VALUES (4,'Dan Leri',367,91.75,'IS110B')
;

INSERT INTO tblStudent VALUES (5,'John Michaelson',310,77.50,'IS110B')
;

INSERT INTO tblStudent VALUES (6,'Steve Finley',374,93.50,'IS110B')
;

INSERT INTO tblStudent VALUES (7,'Sheri Edwards',368,92.00,'IS110B')
;

INSERT INTO tblStudent VALUES (8,'William Harrison',382,95.50,'IS115A')
;

INSERT INTO tblStudent VALUES (9,'Paula Reynolds',375,93.75,'IS115A')
;

INSERT INTO tblStudent VALUES (10,'Thomas Jenkins',324,81.00,'IS115A')
;

INSERT INTO tblStudent VALUES (11,'John Garrison',345,86.25,'IS115A')
;

INSERT INTO tblStudent VALUES (12,'John Green',306,76.50,'IS115A')
;

INSERT INTO tblStudent VALUES (13,'Mike Kennedy',343,85.75,'IS210B')
;

INSERT INTO tblStudent VALUES (14,'Donna Wiser',377,94.25,'IS210B')
;

INSERT INTO tblStudent VALUES (15,'Lisa Smith',320,80.00,'IS210B')
;

INSERT INTO tblStudent VALUES (16,'Bob Walker',352,88.00,'IS210B')
;

INSERT INTO tblStudent VALUES (17,'Frank Maller',341,85.25,'IS210B')
;

INSERT INTO tblStudent VALUES (19,'Bill Larsen',322,80.50,'IS110B')
;

INSERT INTO tblStudent VALUES (25,'Mike Smith',300,75.00,'IS210B')
;

INSERT INTO tblStudent VALUES (27,'Jack Evans',191,47.00,'IS110B')
;

INSERT INTO tblStudent VALUES (28,'Joe Anderson',211,52.00,'IS210B')
;
