# Host: localhost  (Version: 5.5.47)
# Date: 2016-03-28 01:47:28
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

INSERT INTO `userdatabase` VALUES (1,'Rapha','Silva','raphael@rac.com',NULL,'dsadasdasdada','23/03/2016','Homem','9808980','[]',NULL),(2,'Silva','Justo','dasdas','dasda','2343242','30/03/2016','Homem','312321312',NULL,NULL),(3,'Vanessa','Oliveira','vah','dasdsa','dsfds','24/03/2016','Mulher','989890',NULL,NULL),(4,'jessica','roberta','vxcvcxvxc','vxcvxc','ewqrwr','19/03/2016','Homem','342432432','[Corrida, Skate, Basquete, Skate, Corrida, Basquete]',NULL),(5,'Bia','Bia','','','','27/03/2016','Homem','','[Corrida, Skate, Basquete, Skate, Corrida, Basquete]',NULL),(6,'Raphael','Silva','sadasdsa','dasdasd','dsada','27/03/2016','Homem','909889098','[Basquete, Basquete]',NULL),(7,'dsada',NULL,NULL,NULL,NULL,'0000-00-00',NULL,NULL,'',NULL),(8,'das',NULL,NULL,NULL,NULL,'0000-00-00',NULL,NULL,'',NULL),(9,'fvdxcvdsf','dsfsdf','fsdfds','fdsfsfsd','fdsfsd','27/03/2016','Homem','fdsfsdfsd','[]',NULL);
