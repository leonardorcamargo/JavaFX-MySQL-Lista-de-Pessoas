# Host: localhost  (Version: 5.5.47)
# Date: 2016-03-28 04:33:01
# Generator: MySQL-Front 5.3  (Build 4.271)

/*!40101 SET NAMES latin1 */;

#
# Structure for table "userdatabase"
#

DROP TABLE IF EXISTS `userdatabase`;
CREATE TABLE `userdatabase` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `firstName` varchar(255) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `dob` varchar(255) DEFAULT '0000-00-00',
  `gender` varchar(255) DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `hobbies` varchar(255) DEFAULT '',
  `image` blob,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1;

#
# Data for table "userdatabase"
#

INSERT INTO `userdatabase` VALUES (1,'Rapha','Silva','raphael@silva.com','admin','admin','23/03/1989','Homem','00-9987-1234','[]',NULL),(2,'Vanessa ','Oliveira','vanessa@van.com','van','nessa','30/03/2016','Mulher','9999999999','[]',NULL),(3,'Bianca','Johnson','bia@b.com','bbia','bbbbiaa','24/03/2016','Mulher','9999999999',NULL,NULL),(4,'Jessica','Abreu','jessy@abre.com','jessy','abreu','19/03/2016','Mulher','9999999999','[]',NULL),(5,'Felipe','Machado','felipe@abreu.com','felipe','abre','27/03/2016','Homem','9999999999','[Corrida, Skate, Basquete, Skate, Corrida, Basquete]',NULL),(6,'Sergio','Felipe','sergio@felipe.com','sergio','sergio','27/03/2016','Homem','9999999999','[Basquete, Basquete]',NULL),(7,'Samara','Barbosa','sam@lisa.com','sam','asdas@1321das','21/03/2016','Homem','9999999999','[]',NULL),(8,'Agatha','Carvalho','agatha@carvalho.com','agatha','312ewasd','13/03/2012','Homem','9999999999','[]',NULL),(9,'Julian','Ferreira','julian@fer.com','aaaaa','czxczxc','27/03/2016','Homem','9999999999','[]',NULL);
