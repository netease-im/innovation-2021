-- MySQL dump 10.13  Distrib 8.0.18, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: plannote
-- ------------------------------------------------------
-- Server version	8.0.18

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `plan`
--

DROP TABLE IF EXISTS `plan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `plan` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `user_id` int(255) NOT NULL,
  `plan_text` text,
  `planLog_text` text,
  `plan_name` varchar(255) NOT NULL,
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `plan_pk` (`user_id`,`plan_name`),
  CONSTRAINT `user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `plan`
--

LOCK TABLES `plan` WRITE;
/*!40000 ALTER TABLE `plan` DISABLE KEYS */;
INSERT INTO `plan` VALUES (82,9,'{\"process\":[{\"startTime\":\"15:56:20\",\"pro_text\":\"好的开始是成功的一半！\",\"startDate\":\"2021 年 08 月 04 日\",\"pro_name\":\"计划开始\"},{\"endDate\":\"2021 年 08 月 24 日\",\"startTime\":\"16:56:55\",\"id\":1,\"pro_text\":\"任务测试\",\"endTime\":\"16:56:56\",\"startDate\":\"2021 年 08 月 02 日\",\"status\":0,\"pro_name\":\"任务111111\"},{\"startTime\":\"17:56:23\",\"pro_text\":\"坚持就是胜利！\",\"startDate\":\"2021 年 08 月 18 日\",\"pro_name\":\"计划结束\"}],\"_id\":\"aU3Cr0Txfyi2FRum\",\"type\":\"planItemTest2\",\"plan\":{\"memCount\":0,\"manager\":\"\",\"endDate\":\"2021 年 08 月 18 日\",\"excutor\":\"曹逸\",\"name\":\"创建计划测试\",\"member\":[],\"startTime\":\"15:56:20\",\"endTime\":\"17:56:23\",\"type\":\"个人计划\",\"startDate\":\"2021 年 08 月 04 日\",\"desc\":\"计划描述\"}}','{\"planNotes\":{\"测试一下\":\"测试一下云同步，第二次测试\"},\"_id\":\"3dV5RhuxLmnI2mhj\",\"type\":\"planItemNotes\",\"dailyNotes\":{},\"plan\":\"创建计划测试\"}','创建计划测试'),(85,9,'{\"process\":[{\"startTime\":\"09:38:08\",\"pro_text\":\"好的开始是成功的一半！\",\"startDate\":\"2021 年 08 月 04 日\",\"pro_name\":\"计划开始\"},{\"endDate\":\"2021 年 08 月 30 日\",\"startTime\":\"09:38:34\",\"id\":1,\"pro_text\":\"曹逸\",\"endTime\":\"09:38:35\",\"startDate\":\"2021 年 08 月 02 日\",\"status\":0,\"pro_name\":\"啊手动阀\"},{\"startTime\":\"10:38:10\",\"pro_text\":\"坚持就是胜利！\",\"startDate\":\"2021 年 08 月 19 日\",\"pro_name\":\"计划结束\"}],\"type\":\"planItemTest2\",\"plan\":{\"memCount\":0,\"manager\":\"\",\"endDate\":\"2021 年 08 月 19 日\",\"excutor\":\"曹逸\",\"name\":\"使命召唤\",\"member\":[],\"startTime\":\"09:38:08\",\"endTime\":\"10:38:10\",\"type\":\"个人计划\",\"startDate\":\"2021 年 08 月 04 日\",\"desc\":\"草审议士大夫\"}}','{\"type\":\"planItemNotes\",\"dailyNotes\":{},\"plan\":\"使命召唤\"}','使命召唤');
/*!40000 ALTER TABLE `plan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`,`email`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `user_pk` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Aa15340521294@163.com','456234'),(9,'1511500653@qq.com','456234');
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

-- Dump completed on 2021-08-19 11:13:32
