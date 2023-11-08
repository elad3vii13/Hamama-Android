CREATE TABLE log (
 	id int NOT NULL AUTO_INCREMENT,
 	time bigint NOT NULL,
 	priority enum('info','warning','error') NOT NULL,
	message longtext NOT NULL,
 	sid int NOT NULL,
 	PRIMARY KEY (id)
);
INSERT INTO log (id, time, priority, message, sid) VALUES
(1, 1611836005533, 'info', 'this is a test', 1),
(2, 1611836005533, 'warning', 'this is the second message', 1),
(5, 1611836005533, 'info', 'this is a test', 1),
(6, 1611836005533, 'info', '1', 2),
(7, 1611836005533, 'info', '2', 3),
(8, 1611836005533, 'warning', '3', 4),
(9, 1611836005533, 'error', '4', 5),
(10, 1611836005533, 'error', '5', 6),
(11, 1611836005533, 'info', '6', 6),
(12, 1612179036671, 'error', '8', 1),
(13, 1614869358693, 'error', 'Bad formated request from the board. URI=/board, Parameters= {cmd=[timee]}', -1),
(14, 1614869429770, 'error', 'Bad formated request from the board. URI=/board, Parameters= {value1.2=[],cmd=[measure],time=[1]}', -1),
(15, 1614869544256, 'error', 'Bad formated request from the board. URI=/board, Parameters= {value=[1.2],cmd=[measure],time=[1]}', -1),
(16, 1616420450407, 'warning', 'please check the sensor', 1),
(17, 1616400450407, 'info', 'more info ', 3);

CREATE TABLE measures (
 	id int AUTO_INCREMENT,
	time bigint NOT NULL,
	sid int NOT NULL,
	value double NOT NULL,
	PRIMARY KEY (id)
);
INSERT INTO measures (time, sid, value) VALUES
(1644861600000, 1, 4),
(1644948000000, 2, 5),
(1645034400000, 1, 6),
(1645120800000, 2, 7),
(1645207200000, 1, 8),
(1645293600000, 2, 9),
(1645380000000, 1, 10),
(1645466400000, 2, 11),
(1645552800000, 1, 12),
(1645639200000, 2, 13),
(1645718400000, 3, 1),
(1645804800000, 4, 2),
(1645891200000, 3, 3),
(1645977600000, 4, 4),
(1646064000000, 3, 5),
(1646150400000, 4, 6),
(1646236800000, 3, 7),
(1646323200000, 4, 8),
(1646409600000, 3, 9),
(1646496000000, 4, 10);

CREATE TABLE sensors (
  id int NOT NULL AUTO_INCREMENT,
  name varchar(45) CHARACTER SET utf8 NOT NULL,
  units varchar(45) CHARACTER SET utf8 NOT NULL,
  displayName varchar(45) CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
INSERT INTO sensors VALUES (1,'EC','S/m','מוליכות'),(2,'PH',' ','חומציות'),(3,'Turbidness','NTU','עכירות'),(4,'temp1','celsius','טמפרטורה'),(5,'temp2','celsius','טמפרטורה'),(6,'temp3','celsius','טמפרטורה');

CREATE TABLE settings (
	id int NOT NULL AUTO_INCREMENT,
	sid int NOT NULL,
	min double NOT NULL,
	max double NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (sid) REFERENCES sensors(id)
);
INSERT INTO settings VALUES (1,1,0,100),(2,2,0,14),(3,3,0,100),(4,4,-10,40),(5,5,-10,40),(6,6,-10,40);

CREATE TABLE users (
	id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	nickname varchar(45) NOT NULL,
	password varchar(90) NOT NULL,
	role enum('regular','manager') NOT NULL DEFAULT 'regular',
	lastLogin varchar(45) DEFAULT NULL
);
INSERT INTO users VALUES (1,'admin','1313','manager','1616740638128');