-- MySQL dump 10.13  Distrib 8.0.28, for macos11 (x86_64)
--
-- Host: 127.0.0.1    Database: translator_app
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` varchar(255) DEFAULT NULL,
  `has_read` bit(1) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `project_id` bigint DEFAULT NULL,
  `receiver_id` bigint DEFAULT NULL,
  `sender_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKi35sfx0x08fonfxf2l8cp2xcp` (`project_id`),
  KEY `FKmlidwdldgmdw67l7pbrval0un` (`receiver_id`),
  KEY `FKnbt1hengkgjqru2q44q8rlc2c` (`sender_id`),
  CONSTRAINT `FKi35sfx0x08fonfxf2l8cp2xcp` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`),
  CONSTRAINT `FKmlidwdldgmdw67l7pbrval0un` FOREIGN KEY (`receiver_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKnbt1hengkgjqru2q44q8rlc2c` FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (1,'',_binary '\0','tuan pham đã nhận yêu cầu project1','Thông báo',3,12,6),(2,'',_binary '\0','tuan pham đã nhận yêu cầu project1','Thông báo',5,12,8),(3,'',_binary '\0','tuan pham đã nhận yêu cầu project1','Thông báo',3,12,6);
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `project` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `budget` double DEFAULT NULL,
  `created_at` varchar(255) DEFAULT NULL,
  `deadline` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `source_language` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `target_language` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `translate_file` varchar(255) DEFAULT NULL,
  `translated_file` varchar(255) DEFAULT NULL,
  `translator_id` bigint DEFAULT NULL,
  `client_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKca5dcvf5190qa1trdnlc2l2lg` (`translator_id`),
  KEY `FK1xyvksttvuuyps5pcpxt8hyqi` (`client_id`),
  CONSTRAINT `FK1xyvksttvuuyps5pcpxt8hyqi` FOREIGN KEY (`client_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKca5dcvf5190qa1trdnlc2l2lg` FOREIGN KEY (`translator_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
INSERT INTO `project` VALUES (2,1000,'','2024-11-29T17:00:00.000Z','project2','vietnam','success','japan','project2','Activate AIO Tools v3.1.2.zip',NULL,NULL,12),(3,5000,'','2024-11-29T17:00:00.000Z','project1','english','paymenting','vietnamese','project1','Activate AIO Tools v3.1.2.zip',NULL,6,12),(4,5000,'','2024-11-29T17:00:00.000Z','projectabcca','english','paymenting','vietnamese','project1','Activate AIO Tools v3.1.2.zip',NULL,NULL,12),(5,5000,'','2024-11-29T17:00:00.000Z','projectasdfdos','english','requesting','vietnamese','project1','Activate AIO Tools v3.1.2.zip',NULL,8,12);
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `feed_back` varchar(255) DEFAULT NULL,
  `review_star` double DEFAULT NULL,
  `project_id` bigint DEFAULT NULL,
  `receiver_id` bigint DEFAULT NULL,
  `sender_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKdubcm9p32b787jxhmn2fj5b82` (`project_id`),
  KEY `FKgvkx7atv2jar2xujktj4antct` (`receiver_id`),
  KEY `FKg80dwhpmh1fkad1fcbqj43nwy` (`sender_id`),
  CONSTRAINT `FKcv4hm14o255rb1jfsk9nmp0kp` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`),
  CONSTRAINT `FKg80dwhpmh1fkad1fcbqj43nwy` FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKgvkx7atv2jar2xujktj4antct` FOREIGN KEY (`receiver_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review`
--

LOCK TABLES `review` WRITE;
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
/*!40000 ALTER TABLE `review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service_fee`
--

DROP TABLE IF EXISTS `service_fee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_fee` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `base_fee` double DEFAULT NULL,
  `source_language` varchar(255) DEFAULT NULL,
  `target_language` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_fee`
--

LOCK TABLES `service_fee` WRITE;
/*!40000 ALTER TABLE `service_fee` DISABLE KEYS */;
INSERT INTO `service_fee` VALUES (1,1,'english','vietnamese');
/*!40000 ALTER TABLE `service_fee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `support_ticket`
--

DROP TABLE IF EXISTS `support_ticket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `support_ticket` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmttbh8adhmi7oupybatp8tbk2` (`user_id`),
  CONSTRAINT `FKmttbh8adhmi7oupybatp8tbk2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `support_ticket`
--

LOCK TABLES `support_ticket` WRITE;
/*!40000 ALTER TABLE `support_ticket` DISABLE KEYS */;
/*!40000 ALTER TABLE `support_ticket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'tuanpham@gmail.com','tuan pham','$2a$10$gf/yGM5A0dSa6xecVEqT3ujbwL9iweT/Gh.Yaqhp61FIXtgqe60Da','0988888888','CUSTOMER',NULL),(2,'tuanpha1m@gmail.com','tuan pham','$2a$10$PuGzvUAG058IaL/wkZUO1Op4Bt7r/qMHXr1LB4sKzoZ1Lq3mFZLa2','09880888888','CUSTOMER',NULL),(3,'tuanpha11m@gmail.com','tuan pham','$2a$10$Yi65u8.2Q5WCtaaQZGJH8.jf7hEmTbzpD9Aqc8HBfodeU.MnI.XEy','098801888888','CUSTOMER',NULL),(4,'tuanpha111m@gmail.com','tuan pham','$2a$10$Fos21VScQqY4Bz2hzT/IK.GULfG3uzX32qnkv8QI/1rjrdvJvjxby','0988101888888','CUSTOMER',NULL),(5,'tuanpha1111m@gmail.com','tuan pham','$2a$10$9Vrc2OVaTI.SGx7rPoelNOBr6AFCbVBza3CQPfB87phhxPCivO47u','09818101888888','CUSTOMER',NULL),(6,'tuanpha111111m@gmail.com','tuan pham','$2a$10$YP8v/4Vg2jHiH0ZNOiRnseKDt/BfO6hxqt8xQ98kUQ5ZwZZmX3a/G','0981118101888888','translator',NULL),(7,'tuanpha1111111m@gmail.com','tuan pham','tuanpham','09811118101888888','translator',NULL),(8,'tuanpha11121111m@gmail.com','tuan pham','$2a$10$0Rv5f6GKHDiXCWEYXkJld.eMqwzU2bSElCIXDWCRjlJTMAeVusB4i','098111181021888888','translator',NULL),(9,'tuanpha111211111m@gmail.com','tuan pham','$2a$10$LMEhPXHaSI9WVDYjj6kLR.vevEnthdU7DBbgzeY.5doOq2r4WqCpi','0981111181021888888','translator',NULL),(12,'doquang227@gmail.com','đỗ quang','$2a$10$5M2YvM1FCCuSso5ia3.JQuwaL0ZUOKbdLYHDYtxTninwRyglC70dO','0976183378','admin',NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-04 13:47:30
