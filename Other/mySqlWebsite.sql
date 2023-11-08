CREATE TABLE `log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `time` bigint NOT NULL,
  `priority` enum('info','warning','error') NOT NULL,
  `message` longtext NOT NULL,
  `sid` int NOT NULL,
  PRIMARY KEY (`id`)
);
INSERT INTO `log` VALUES (1,1611836005533,'info','this is a test',1),(2,1611836005533,'warning','this is the second message',1),(5,1611836005533,'info','this is a test',1),(6,1611836005533,'info','1',2),(7,1611836005533,'info','2',3),(8,1611836005533,'warning','3',4),(9,1611836005533,'error','4',5),(10,1611836005533,'error','5',6),(11,1611836005533,'info','6',6),(12,1612179036671,'error','!!!',1),(13,1614869358693,'error','Bad formated request from the board. URI=/board, Parameters= {cmd=[timee]}',-1),(14,1614869429770,'error','Bad formated request from the board. URI=/board, Parameters= {value1.2=[],cmd=[measure],time=[1]}',-1),(15,1614869544256,'error','Bad formated request from the board. URI=/board, Parameters= {value=[1.2],cmd=[measure],time=[1]}',-1),(16,1616420450407,'warning','please check the sensor',1),(17,1616400450407,'info','more info ',3);

CREATE TABLE `measures` (
  `id` int NOT NULL AUTO_INCREMENT,
  `time` bigint NOT NULL,
  `sid` int NOT NULL,
  `value` double NOT NULL,
  PRIMARY KEY (`id`)
);
INSERT INTO `measures` VALUES (5,1601947356484,1,2),(6,1612179036671,1,3),(11,1616420450407,1,4),(13,1601947356484,2,3),(15,1612179036671,2,2),(16,1616420450407,2,5);

CREATE TABLE `sensors` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `units` varchar(45) NOT NULL,
  `displayName` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
);
INSERT INTO `sensors` VALUES (1,'EC','S/m','מוליכות'),(2,'PH',' ','חומציות'),(3,'Turbidness','NTU','עכירות'),(4,'temp1','celsius','טמפרטורה'),(5,'temp2','celsius','טמפרטורה'),(6,'temp3','celsius','טמפרטורה');

CREATE TABLE `settings` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sid` int NOT NULL,
  `min` double NOT NULL,
  `max` double NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nickname` varchar(45) NOT NULL,
  `password` varchar(90) NOT NULL,
  `role` enum('regular','manager') NOT NULL DEFAULT 'regular',
  `lastLogin` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nickname_UNIQUE` (`nickname`)
);
INSERT INTO `users` VALUES (1,'admin','1313','manager','1616740638128'),(2,'elad','13','regular','1616741671097'),(32,'asasdasdad','13','regular','1615211041897'),(33,'bbnbnv','13','manager','1615211062289'),(34,'test','1','regular','1616417070954');