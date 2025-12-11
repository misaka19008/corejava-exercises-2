-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: 192.168.9.5    Database: java_exercise
-- ------------------------------------------------------
-- Server version	8.0.29

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
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ 'c8f304d8-9b38-11f0-a443-000c291d2a33:1-361';

--
-- Table structure for table `authors`
--

DROP TABLE IF EXISTS `authors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `authors` (
  `Author_ID` char(4) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL,
  `Name` char(25) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL,
  `Fname` char(25) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`Author_ID`),
  UNIQUE KEY `authors_unique` (`Author_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authors`
--

LOCK TABLES `authors` WRITE;
/*!40000 ALTER TABLE `authors` DISABLE KEYS */;
INSERT INTO `authors` VALUES ('ALEX','Alexander','Christopher'),('BROO','Brooks','Frederick P.'),('CORM','Cormen','Thomas H.'),('DARW','Darwen','Hugh'),('DATE','Date','C. J.'),('FEIN','Feiner','Steven K.'),('FLAN','Flanagan','David'),('FOLE','Foley','James D.'),('GAMM','Gamma','Erich'),('GARF','Garfinkel','Simson'),('HEIN','Hein','Trent R.'),('HELM','Helm','Richard'),('HOPC','Hopcroft','John E.'),('HUGH','Hughes','John F.'),('ISHI','Ishikawa','Sara'),('JOHN','Johnson','Ralph'),('KAHN','Kahn','David'),('KERN','Kernighan','Brian'),('KIDD','Kidder','Tracy'),('KNUT','Knuth','Donald E.'),('LEIS','Leiserson','Charles E.'),('MOTW','Motwani','Rajeev'),('NEME','Nemeth','Evi'),('RAYM','Raymond','Eric'),('RITC','Ritchie','Dennis'),('RIVE','Rivest','Ronald R.'),('SCHN','Schneier','Bruce'),('SEEB','Seebass','Scott'),('SILV','Silverstein','Murray'),('SNYD','Snyder','Garth'),('STEI','Stein','Clifford E.'),('STOL','Stoll','Clifford'),('STRA','Strassmann','Steven'),('STRO','Stroustrup','Bjarne'),('ULLM','Ullman','Jeffrey D.'),('VAND','van Dam','Andries'),('VLIS','Vlissides','John'),('WEIS','Weise','Daniel');
/*!40000 ALTER TABLE `authors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books` (
  `Title` char(60) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL,
  `ISBN` char(13) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL,
  `Publisher_ID` char(6) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL,
  `Price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`ISBN`),
  KEY `books_publishers_FK` (`Publisher_ID`),
  CONSTRAINT `books_publishers_FK` FOREIGN KEY (`Publisher_ID`) REFERENCES `publishers` (`Publisher_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES ('UNIX System Administration Handbook','0-13-020601-6','013',68.00),('The C Programming Language','0-13-110362-8','013',42.00),('A Pattern Language: Towns, Buildings, Construction','0-19-501919-9','019',65.00),('Introduction to Automata Theory, Languages, and Computation','0-201-44124-1','0201',105.00),('Design Patterns','0-201-63361-2','0201',54.99),('The C++ Programming Language','0-201-70073-5','0201',64.99),('The Mythical Man-Month','0-201-83595-9','0201',29.95),('Computer Graphics: Principles and Practice','0-201-84840-6','0201',79.99),('The Art of Computer Programming vol. 1','0-201-89683-4','0201',59.99),('The Art of Computer Programming vol. 2','0-201-89684-2','0201',59.99),('The Art of Computer Programming vol. 3','0-201-89685-0','0201',59.99),('A Guide to the SQL Standard','0-201-96426-0','0201',47.95),('Introduction to Algorithms','0-262-03293-7','0262',210.00),('Applied Cryptography','0-471-11709-9','0471',60.00),('JavaScript: The Definitive Guide','0-596-00048-0','0596',44.95),('The Cathedral and the Bazaar','0-596-00108-8','0596',16.95),('The Soul of a New Machine','0-679-60261-5','0679',18.95),('The Codebreakers','0-684-83130-9','07434',54.00),('Cuckoo\'s Egg','0-7434-1146-3','07434',54.00),('The UNIX Hater\'s Handbook','1-56884-203-1','0471',16.95);
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `booksauthors`
--

DROP TABLE IF EXISTS `booksauthors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booksauthors` (
  `ISBN` char(13) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL,
  `Author_ID` char(4) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL,
  `Seq_No` int NOT NULL,
  KEY `booksauthors_authors_FK` (`Author_ID`),
  KEY `booksauthors_books_FK` (`ISBN`),
  CONSTRAINT `booksauthors_authors_FK` FOREIGN KEY (`Author_ID`) REFERENCES `authors` (`Author_ID`),
  CONSTRAINT `booksauthors_books_FK` FOREIGN KEY (`ISBN`) REFERENCES `books` (`ISBN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booksauthors`
--

LOCK TABLES `booksauthors` WRITE;
/*!40000 ALTER TABLE `booksauthors` DISABLE KEYS */;
INSERT INTO `booksauthors` VALUES ('0-201-96426-0','DATE',1),('0-201-96426-0','DARW',2),('0-19-501919-9','ALEX',1),('0-19-501919-9','ISHI',2),('0-19-501919-9','SILV',3),('0-471-11709-9','SCHN',1),('0-201-84840-6','FOLE',1),('0-201-84840-6','VAND',2),('0-201-84840-6','FEIN',3),('0-201-84840-6','HUGH',4),('0-7434-1146-3','STOL',1),('0-201-63361-2','GAMM',1),('0-201-63361-2','HELM',2),('0-201-63361-2','JOHN',3),('0-201-63361-2','VLIS',4),('0-262-03293-7','CORM',1),('0-262-03293-7','LEIS',2),('0-262-03293-7','RIVE',3),('0-262-03293-7','STEI',4),('0-201-44124-1','HOPC',1),('0-201-44124-1','ULLM',2),('0-201-44124-1','MOTW',3),('0-596-00048-0','FLAN',1),('0-201-89683-4','KNUT',1),('0-201-89684-2','KNUT',1),('0-201-89685-0','KNUT',1),('0-13-110362-8','KERN',1),('0-13-110362-8','RITC',2),('0-201-70073-5','STRO',1),('0-596-00108-8','RAYM',1),('0-684-83130-9','KAHN',1),('0-201-83595-9','BROO',1),('0-679-60261-5','KIDD',1),('1-56884-203-1','GARF',1),('1-56884-203-1','WEIS',2),('1-56884-203-1','STRA',3),('0-13-020601-6','NEME',1),('0-13-020601-6','SNYD',2),('0-13-020601-6','SEEB',3),('0-13-020601-6','HEIN',4);
/*!40000 ALTER TABLE `booksauthors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `greetings`
--

DROP TABLE IF EXISTS `greetings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `greetings` (
  `Message` char(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `greetings`
--

LOCK TABLES `greetings` WRITE;
/*!40000 ALTER TABLE `greetings` DISABLE KEYS */;
INSERT INTO `greetings` VALUES ('希儿世界第一可爱');
/*!40000 ALTER TABLE `greetings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `publishers`
--

DROP TABLE IF EXISTS `publishers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `publishers` (
  `Publisher_ID` char(6) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL,
  `Name` char(30) DEFAULT NULL,
  `URL` char(80) DEFAULT NULL,
  PRIMARY KEY (`Publisher_ID`),
  UNIQUE KEY `publishers_unique` (`Name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `publishers`
--

LOCK TABLES `publishers` WRITE;
/*!40000 ALTER TABLE `publishers` DISABLE KEYS */;
INSERT INTO `publishers` VALUES ('013','Prentice Hall','www.phptr.com'),('019','Oxford University Press','www.oup.co.uk'),('0201','Addison-Wesley','www.aw-bc.com'),('0262','MIT Press','mitpress.mit.edu'),('0471','John Wiley & Sons','www.wiley.com'),('0596','O\'Reilly','www.ora.com'),('0679','Random House','www.randomhouse.com'),('07434','Simon & Schuster','www.simonsays.com');
/*!40000 ALTER TABLE `publishers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'java_exercise'
--
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-11 12:35:08
