-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: old2new_db
-- ------------------------------------------------------
-- Server version	8.0.37

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
-- Table structure for table `chi_tiet_don_hang`
--

DROP TABLE IF EXISTS `chi_tiet_don_hang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chi_tiet_don_hang` (
  `chi_tiet_id` int NOT NULL AUTO_INCREMENT,
  `don_hang_id` int NOT NULL,
  `san_pham_id` int NOT NULL,
  `so_luong_mua` int DEFAULT NULL,
  `gia_luc_mua` decimal(38,2) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`chi_tiet_id`),
  KEY `don_hang_id` (`don_hang_id`),
  KEY `san_pham_id` (`san_pham_id`),
  CONSTRAINT `chi_tiet_don_hang_ibfk_1` FOREIGN KEY (`don_hang_id`) REFERENCES `don_hang` (`don_hang_id`),
  CONSTRAINT `chi_tiet_don_hang_ibfk_2` FOREIGN KEY (`san_pham_id`) REFERENCES `san_pham` (`san_pham_id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chi_tiet_don_hang`
--

LOCK TABLES `chi_tiet_don_hang` WRITE;
/*!40000 ALTER TABLE `chi_tiet_don_hang` DISABLE KEYS */;
INSERT INTO `chi_tiet_don_hang` VALUES (1,1,1,1,5000000.00,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(2,2,2,1,8000000.00,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(3,3,3,1,300000.00,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(4,4,4,1,700000.00,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(5,5,5,1,400000.00,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(6,6,6,1,6000000.00,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(7,7,7,1,200000.00,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(8,8,8,1,1500000.00,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(9,9,9,1,100000.00,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(10,10,10,1,900000.00,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(17,17,92,1,500000.00,'2026-03-01 06:23:53',NULL),(18,18,85,1,70000.00,'2026-03-01 06:28:29',NULL),(19,19,84,1,80000.00,'2026-03-01 06:29:17',NULL),(20,20,83,1,90000.00,'2026-03-01 06:37:23',NULL),(21,21,90,1,120000.00,'2026-03-01 06:43:41',NULL),(22,22,80,1,120000.00,'2026-03-01 06:49:04',NULL),(23,23,11,1,6500000.00,'2026-03-01 06:56:07',NULL),(24,24,77,1,150000.00,'2026-03-01 07:17:36',NULL),(25,25,76,1,200000.00,'2026-03-01 07:22:37',NULL),(26,26,13,1,8000000.00,'2026-03-01 07:29:03',NULL),(27,27,78,1,180000.00,'2026-03-01 07:29:39',NULL),(28,28,74,1,400000.00,'2026-03-01 07:30:53',NULL),(29,29,94,1,100000.00,'2026-03-01 07:41:55',NULL),(30,30,92,1,500000.00,'2026-03-01 07:52:34',NULL),(31,31,93,1,500000.00,'2026-03-01 21:03:54',NULL),(32,32,79,1,150000.00,'2026-03-01 21:06:15',NULL),(33,33,81,1,160000.00,'2026-03-01 23:19:37',NULL),(34,34,75,1,350000.00,'2026-03-02 02:38:05',NULL),(35,34,73,1,300000.00,'2026-03-02 02:38:05',NULL),(36,35,70,1,9500000.00,'2026-03-02 02:38:42',NULL),(37,36,82,1,500000.00,'2026-03-02 02:43:30',NULL),(38,37,72,1,250000.00,'2026-03-02 03:41:42',NULL),(39,38,88,1,180000.00,'2026-03-02 04:09:12',NULL),(42,41,97,1,100000.00,'2026-03-02 04:44:26',NULL);
/*!40000 ALTER TABLE `chi_tiet_don_hang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `danh_gia`
--

DROP TABLE IF EXISTS `danh_gia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `danh_gia` (
  `danh_gia_id` int NOT NULL AUTO_INCREMENT,
  `san_pham_id` int NOT NULL,
  `nguoi_mua_id` int NOT NULL,
  `so_sao` int DEFAULT NULL,
  `binh_luan` text,
  `tong_danh_gia` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`danh_gia_id`),
  KEY `san_pham_id` (`san_pham_id`),
  KEY `nguoi_mua_id` (`nguoi_mua_id`),
  CONSTRAINT `danh_gia_ibfk_1` FOREIGN KEY (`san_pham_id`) REFERENCES `san_pham` (`san_pham_id`),
  CONSTRAINT `danh_gia_ibfk_2` FOREIGN KEY (`nguoi_mua_id`) REFERENCES `nguoi_dung` (`nguoi_dung_id`),
  CONSTRAINT `danh_gia_chk_1` CHECK ((`so_sao` between 1 and 5))
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `danh_gia`
--

LOCK TABLES `danh_gia` WRITE;
/*!40000 ALTER TABLE `danh_gia` DISABLE KEYS */;
INSERT INTO `danh_gia` VALUES (1,1,1,5,'Tot',1,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(2,2,2,4,'On',1,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(3,3,1,5,'Rat tot',1,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(4,4,2,3,'Tam duoc',1,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(5,5,5,4,'Dep',1,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(6,6,6,5,'Ngon',1,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(7,7,8,4,'OK',1,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(8,8,9,5,'Xuat sac',1,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(9,9,1,5,'Hay',1,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(10,10,2,4,'Dung duoc',1,'2026-02-25 17:04:24','2026-02-25 17:04:24');
/*!40000 ALTER TABLE `danh_gia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `danh_muc`
--

DROP TABLE IF EXISTS `danh_muc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `danh_muc` (
  `danh_muc_id` int NOT NULL AUTO_INCREMENT,
  `ten_danh_muc` varchar(150) NOT NULL,
  `cha_id` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`danh_muc_id`),
  KEY `cha_id` (`cha_id`),
  CONSTRAINT `danh_muc_ibfk_1` FOREIGN KEY (`cha_id`) REFERENCES `danh_muc` (`danh_muc_id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `danh_muc`
--

LOCK TABLES `danh_muc` WRITE;
/*!40000 ALTER TABLE `danh_muc` DISABLE KEYS */;
INSERT INTO `danh_muc` VALUES (1,'Dien thoai',NULL,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(2,'Laptop',NULL,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(3,'Quan ao',NULL,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(4,'Giay dep',NULL,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(5,'Dong ho',NULL,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(6,'May tinh bang',NULL,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(7,'Phu kien',NULL,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(8,'Noi that',NULL,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(9,'Sach',NULL,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(10,'Do gia dung',NULL,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(11,'Sách',NULL,'2026-02-28 08:27:55','2026-02-28 08:27:55'),(12,'Thời trang',NULL,'2026-02-28 08:27:55','2026-02-28 08:27:55'),(13,'Đồ điện tử',NULL,'2026-02-28 08:27:55','2026-02-28 08:27:55'),(14,'Sách giáo khoa',11,'2026-02-28 08:28:04','2026-02-28 08:28:04'),(15,'Tiểu thuyết',11,'2026-02-28 08:28:04','2026-02-28 08:28:04'),(16,'Áo nam',12,'2026-02-28 08:28:04','2026-02-28 08:28:04'),(17,'Váy nữ',12,'2026-02-28 08:28:04','2026-02-28 08:28:04'),(18,'Điện thoại cũ',13,'2026-02-28 08:28:04','2026-02-28 08:28:04'),(19,'Laptop cũ',13,'2026-02-28 08:28:04','2026-02-28 08:28:04'),(20,'iPhone cũ',1,'2026-02-28 08:41:29','2026-02-28 08:41:29'),(21,'Điện thoại Android',1,'2026-02-28 08:41:29','2026-02-28 08:41:29'),(22,'Laptop Dell',2,'2026-02-28 08:41:29','2026-02-28 08:41:29'),(23,'Macbook cũ',2,'2026-02-28 08:41:29','2026-02-28 08:41:29'),(24,'Áo thun nam/nữ',3,'2026-02-28 08:41:29','2026-02-28 08:41:29'),(25,'Quần Jean',3,'2026-02-28 08:41:29','2026-02-28 08:41:29'),(26,'Giày thể thao',4,'2026-02-28 08:41:29','2026-02-28 08:41:29'),(27,'Giày cao gót',4,'2026-02-28 08:41:29','2026-02-28 08:41:29'),(28,'Đồng hồ cơ',5,'2026-02-28 08:41:29','2026-02-28 08:41:29'),(29,'Smartwatch',5,'2026-02-28 08:41:29','2026-02-28 08:41:29'),(30,'iPad',6,'2026-02-28 08:41:29','2026-02-28 08:41:29'),(31,'Tablet Android',6,'2026-02-28 08:41:29','2026-02-28 08:41:29'),(32,'Tai nghe / Loa',7,'2026-02-28 08:41:29','2026-02-28 08:41:29'),(33,'Ốp lưng / Sạc',7,'2026-02-28 08:41:29','2026-02-28 08:41:29'),(34,'Bàn ghế làm việc',8,'2026-02-28 08:41:29','2026-02-28 08:41:29'),(35,'Tủ / Kệ',8,'2026-02-28 08:41:29','2026-02-28 08:41:29'),(36,'Sách kinh doanh',9,'2026-02-28 08:41:29','2026-02-28 08:41:29'),(37,'Sách văn học',9,'2026-02-28 08:41:29','2026-02-28 08:41:29'),(38,'Nồi chiên / Lò vi sóng',10,'2026-02-28 08:41:29','2026-02-28 08:41:29'),(39,'Máy xay / Dụng cụ bếp',10,'2026-02-28 08:41:29','2026-02-28 08:41:29');
/*!40000 ALTER TABLE `danh_muc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dia_chi`
--

DROP TABLE IF EXISTS `dia_chi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dia_chi` (
  `dia_chi_id` int NOT NULL AUTO_INCREMENT,
  `nguoi_dung_id` int NOT NULL,
  `phuong_xa_id` varchar(255) DEFAULT NULL,
  `dia_chi_chi_tiet` varchar(255) DEFAULT NULL,
  `dia_chi_mac_dinh` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `huyen_code` int DEFAULT NULL,
  PRIMARY KEY (`dia_chi_id`),
  KEY `nguoi_dung_id` (`nguoi_dung_id`),
  KEY `phuong_xa_id` (`phuong_xa_id`),
  CONSTRAINT `dia_chi_ibfk_1` FOREIGN KEY (`nguoi_dung_id`) REFERENCES `nguoi_dung` (`nguoi_dung_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dia_chi`
--

LOCK TABLES `dia_chi` WRITE;
/*!40000 ALTER TABLE `dia_chi` DISABLE KEYS */;
INSERT INTO `dia_chi` VALUES (1,1,'1','123 Street',1,'2026-02-25 17:04:24','2026-02-25 17:04:24',NULL),(2,2,'2','124 Street',1,'2026-02-25 17:04:24','2026-02-25 17:04:24',NULL),(3,3,'3','125 Street',1,'2026-02-25 17:04:24','2026-02-25 17:04:24',NULL),(4,4,'4','126 Street',1,'2026-02-25 17:04:24','2026-02-25 17:04:24',NULL),(5,5,'5','127 Street',1,'2026-02-25 17:04:24','2026-02-25 17:04:24',NULL),(6,6,'6','128 Street',1,'2026-02-25 17:04:24','2026-02-25 17:04:24',NULL),(7,7,'7','129 Street',1,'2026-02-25 17:04:24','2026-02-25 17:04:24',NULL),(8,8,'8','130 Street',1,'2026-02-25 17:04:24','2026-02-25 17:04:24',NULL),(9,9,'9','131 Street',1,'2026-02-25 17:04:24','2026-02-25 17:04:24',NULL),(10,10,'10','132 Street',1,'2026-02-25 17:04:24','2026-02-25 17:04:24',NULL),(13,19,'90767','Hoàng Nhật - 0123456789 | Hồ Chí Minh Thủ Đức Trưng Tây, Phường Bình Trưng Tây, Thành Phố Thủ Đức, Hồ Chí Minh',0,'2026-03-02 19:58:14','2026-03-02 19:58:14',NULL),(16,18,'90768','Tuấn Duy - 0379185939 | TP Hồ Chí Minh, Thủ Đức,AN Khánh, Phường An Khánh, Thành Phố Thủ Đức, Hồ Chí Minh',1,'2026-03-03 16:35:02','2026-03-03 16:35:02',3695),(17,18,'300830','Khoa - 0987654321 | Hà Tĩnh Huyện Thạch Hà,Xã Tượng Sơn, Xã Tượng Sơn, Huyện Thạch Hà, Hà Tĩnh',0,'2026-03-03 16:36:40','2026-03-03 16:44:13',2024),(18,18,'281901','Nghĩa - 0123456789 | TP Thanh Hóa, Huyện Nông Cống, Thị Trấn Nông Cống,36, Thị trấn Nông Cống, Huyện Nông Cống, Thanh Hóa',0,'2026-03-03 16:39:47','2026-03-03 16:39:47',2181);
/*!40000 ALTER TABLE `dia_chi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `don_hang`
--

DROP TABLE IF EXISTS `don_hang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `don_hang` (
  `don_hang_id` int NOT NULL AUTO_INCREMENT,
  `nguoi_dung_id` int NOT NULL,
  `dia_chi_id` int NOT NULL,
  `tong_tien_hang` decimal(38,2) DEFAULT NULL,
  `tong_tien_ship` decimal(38,2) DEFAULT NULL,
  `tong_thanh_tien` decimal(38,2) DEFAULT NULL,
  `phuong_phuc_thanh_toan` varchar(50) DEFAULT NULL,
  `trang_thai_thanh_toan` varchar(50) DEFAULT NULL,
  `trang_thai_don_hang` varchar(50) DEFAULT NULL,
  `ngay_tao` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`don_hang_id`),
  KEY `nguoi_dung_id` (`nguoi_dung_id`),
  KEY `dia_chi_id` (`dia_chi_id`),
  CONSTRAINT `don_hang_ibfk_1` FOREIGN KEY (`nguoi_dung_id`) REFERENCES `nguoi_dung` (`nguoi_dung_id`),
  CONSTRAINT `don_hang_ibfk_2` FOREIGN KEY (`dia_chi_id`) REFERENCES `dia_chi` (`dia_chi_id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `don_hang`
--

LOCK TABLES `don_hang` WRITE;
/*!40000 ALTER TABLE `don_hang` DISABLE KEYS */;
INSERT INTO `don_hang` VALUES (1,1,1,5000000.00,30000.00,5030000.00,NULL,NULL,NULL,'2026-02-26 00:04:24','2026-02-25 17:04:24'),(2,2,2,8000000.00,30000.00,8030000.00,NULL,NULL,NULL,'2026-02-26 00:04:24','2026-02-25 17:04:24'),(3,1,1,300000.00,20000.00,320000.00,NULL,NULL,NULL,'2026-02-26 00:04:24','2026-02-25 17:04:24'),(4,2,2,700000.00,20000.00,720000.00,NULL,NULL,NULL,'2026-02-26 00:04:24','2026-02-25 17:04:24'),(5,5,5,400000.00,20000.00,420000.00,NULL,NULL,NULL,'2026-02-26 00:04:24','2026-02-25 17:04:24'),(6,6,6,6000000.00,30000.00,6030000.00,NULL,NULL,NULL,'2026-02-26 00:04:24','2026-02-25 17:04:24'),(7,8,8,200000.00,20000.00,220000.00,NULL,NULL,NULL,'2026-02-26 00:04:24','2026-02-25 17:04:24'),(8,9,9,1500000.00,30000.00,1530000.00,NULL,NULL,NULL,'2026-02-26 00:04:24','2026-02-25 17:04:24'),(9,1,1,100000.00,20000.00,120000.00,NULL,NULL,NULL,'2026-02-26 00:04:24','2026-02-25 17:04:24'),(10,2,2,900000.00,20000.00,920000.00,NULL,NULL,NULL,'2026-02-26 00:04:24','2026-02-25 17:04:24'),(17,1,1,500000.00,30000.00,530000.00,'VNPAY','CHUA_THANH_TOAN','DA_HUY','2026-03-01 13:23:53',NULL),(18,1,1,70000.00,30000.00,100000.00,'COD','CHUA_THANH_TOAN','CHO_XAC_NHAN','2026-03-01 13:28:29',NULL),(19,1,1,80000.00,30000.00,110000.00,'VNPAY','CHUA_THANH_TOAN','CHO_XAC_NHAN','2026-03-01 13:29:17',NULL),(20,1,1,90000.00,30000.00,120000.00,'VNPAY','CHUA_THANH_TOAN','CHO_XAC_NHAN','2026-03-01 13:37:23',NULL),(21,1,1,120000.00,30000.00,150000.00,'VNPAY','CHUA_THANH_TOAN','CHO_XAC_NHAN','2026-03-01 13:43:40',NULL),(22,1,1,120000.00,30000.00,150000.00,'VNPAY','CHUA_THANH_TOAN','CHO_XAC_NHAN','2026-03-01 13:49:04',NULL),(23,1,1,6500000.00,30000.00,6530000.00,'VNPAY','CHUA_THANH_TOAN','CHO_XAC_NHAN','2026-03-01 13:56:07',NULL),(24,1,1,150000.00,30000.00,180000.00,'VNPAY','CHUA_THANH_TOAN','CHO_XAC_NHAN','2026-03-01 14:17:36',NULL),(25,1,1,200000.00,30000.00,230000.00,'VNPAY','CHUA_THANH_TOAN','CHO_XAC_NHAN','2026-03-01 14:22:37',NULL),(26,1,1,8000000.00,30000.00,8030000.00,'VNPAY','CHUA_THANH_TOAN','CHO_XAC_NHAN','2026-03-01 14:29:02',NULL),(27,1,1,180000.00,30000.00,210000.00,'VNPAY','CHUA_THANH_TOAN','CHO_XAC_NHAN','2026-03-01 14:29:39',NULL),(28,1,1,400000.00,30000.00,430000.00,'VNPAY','CHUA_THANH_TOAN','CHO_XAC_NHAN','2026-03-01 14:30:53',NULL),(29,1,1,100000.00,30000.00,130000.00,'VNPAY','DA_THANH_TOAN','CHO_XAC_NHAN','2026-03-01 14:41:55',NULL),(30,1,1,500000.00,30000.00,530000.00,'VNPAY','DA_THANH_TOAN','CHO_XAC_NHAN','2026-03-01 14:52:34',NULL),(31,1,1,500000.00,30000.00,530000.00,'VNPAY','DA_THANH_TOAN','CHO_XAC_NHAN','2026-03-02 04:03:54',NULL),(32,1,1,150000.00,30000.00,180000.00,'VNPAY','THANH_TOAN_THAT_BAI','CHO_XAC_NHAN','2026-03-02 04:06:15',NULL),(33,1,1,160000.00,30000.00,190000.00,'VNPAY','CHUA_THANH_TOAN','CHO_XAC_NHAN','2026-03-02 06:19:37',NULL),(34,1,1,650000.00,30000.00,680000.00,'COD','CHUA_THANH_TOAN','CHO_XAC_NHAN','2026-03-02 09:38:05',NULL),(35,1,1,9500000.00,30000.00,9530000.00,'VNPAY','DA_THANH_TOAN','CHO_XAC_NHAN','2026-03-02 09:38:42',NULL),(36,1,1,500000.00,30000.00,530000.00,'COD','CHUA_THANH_TOAN','CHO_XAC_NHAN','2026-03-02 09:43:30',NULL),(37,1,1,250000.00,30000.00,280000.00,'COD','CHUA_THANH_TOAN','CHO_XAC_NHAN','2026-03-02 10:41:41',NULL),(38,18,1,180000.00,30000.00,210000.00,'COD','CHUA_THANH_TOAN','CHO_XAC_NHAN','2026-03-02 11:09:12',NULL),(40,18,1,10000.00,30000.00,40000.00,'VNPAY','DA_THANH_TOAN','CHO_XAC_NHAN','2026-03-02 11:32:36',NULL),(41,18,1,100000.00,30000.00,130000.00,'VNPAY','DA_THANH_TOAN','DANG_GIAO','2026-03-02 11:44:26',NULL);
/*!40000 ALTER TABLE `don_hang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hinh_anh_san_pham`
--

DROP TABLE IF EXISTS `hinh_anh_san_pham`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hinh_anh_san_pham` (
  `hinh_anh_id` int NOT NULL AUTO_INCREMENT,
  `san_pham_id` int NOT NULL,
  `duong_dan_anh` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`hinh_anh_id`),
  KEY `san_pham_id` (`san_pham_id`),
  CONSTRAINT `hinh_anh_san_pham_ibfk_1` FOREIGN KEY (`san_pham_id`) REFERENCES `san_pham` (`san_pham_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hinh_anh_san_pham`
--

LOCK TABLES `hinh_anh_san_pham` WRITE;
/*!40000 ALTER TABLE `hinh_anh_san_pham` DISABLE KEYS */;
INSERT INTO `hinh_anh_san_pham` VALUES (1,1,'uploads/1.jpg','2026-02-25 17:04:24','2026-02-25 17:04:24'),(2,2,'uploads/2.jpg','2026-02-25 17:04:24','2026-02-25 17:04:24'),(3,3,'uploads/3.jpg','2026-02-25 17:04:24','2026-02-25 17:04:24'),(4,4,'uploads/4.jpg','2026-02-25 17:04:24','2026-02-25 17:04:24'),(5,5,'uploads/5.jpg','2026-02-25 17:04:24','2026-02-25 17:04:24'),(6,6,'uploads/6.jpg','2026-02-25 17:04:24','2026-02-25 17:04:24'),(7,7,'uploads/7.jpg','2026-02-25 17:04:24','2026-02-25 17:04:24'),(8,8,'uploads/8.jpg','2026-02-25 17:04:24','2026-02-25 17:04:24'),(9,9,'uploads/9.jpg','2026-02-25 17:04:24','2026-02-25 17:04:24'),(10,10,'uploads/10.jpg','2026-02-25 17:04:24','2026-02-25 17:04:24'),(11,92,'http://localhost:8080/uploads/4ea51a9e-3716-4fec-bd85-173fb3165032_cahoingungok (1).jpg',NULL,NULL),(12,92,'http://localhost:8080/uploads/b96adb29-66ec-488d-8631-295ec4d9f9fd_cahoingungok (3).jpg',NULL,NULL),(13,92,'http://localhost:8080/uploads/615fc40f-d114-4234-aa5d-0a24bdc65efd_cahoingungok (5).jpg',NULL,NULL),(14,92,'http://localhost:8080/uploads/3eca851f-ca73-4dd2-9951-e501814539a6_cahoingungok (2).jpg',NULL,NULL),(15,92,'http://localhost:8080/uploads/14345854-3262-4eb6-9577-190705b47f7b_cahoingungok (4).jpg',NULL,NULL),(16,93,'http://localhost:8080/uploads/3d495ed8-37b2-4d45-b2ce-b25b35154e10_GiaHan.jpg',NULL,NULL),(17,93,'http://localhost:8080/uploads/65819222-171c-49d6-b3b0-4d4c9d32c184_KhuongGiaHan (5).jpg',NULL,NULL),(18,93,'http://localhost:8080/uploads/ae4ecc48-d274-433a-ac50-6740b489197e_KhuongGiaHan (6).jpg',NULL,NULL),(19,94,'http://localhost:8080/uploads/d6429f9d-1d38-4464-9474-2d2fccbc425f_⋆ ˙⟡.jpg',NULL,NULL),(20,94,'http://localhost:8080/uploads/3e8c0156-6af7-4a28-949d-58bba63e07da_شكلي وانا اشوف كيف جسمي عليه كدمات وعلامات دخان مطفيه فيني بسبب ابوي وانهار على نفسي??.jpg',NULL,NULL),(21,94,'http://localhost:8080/uploads/bd93e41b-cc0d-4975-b070-38b6df7f2289_tải xuống.jpg',NULL,NULL),(22,94,'http://localhost:8080/uploads/0fd35f4b-533d-4744-81bf-c4f203f45ec3_?.jpg',NULL,NULL),(23,95,'http://localhost:8080/uploads/7dec369c-266b-4243-9632-e1bf5ef6817f_konichiwaaa.. (1).jpg',NULL,NULL),(24,95,'http://localhost:8080/uploads/26303d89-6d0d-48a9-974e-bb60e7e94578_konichiwaaa...jpg',NULL,NULL),(25,96,'http://localhost:8080/uploads/9c8d0463-cd77-4c1a-b4ce-d20850fe3529_joey lynch_.jpg',NULL,NULL),(26,96,'http://localhost:8080/uploads/6fe32763-829a-4988-8470-47c3b55d54c2_dechquantam.jpg',NULL,NULL),(27,96,'http://localhost:8080/uploads/87fe2a7f-0230-4ef3-b79f-6941b07b1234_avt (1).jpg',NULL,NULL),(28,96,'http://localhost:8080/uploads/a21406a4-121d-4e3e-81f0-09084e5267ed_blue v1 -alxandr wallpaper (2).jpg',NULL,NULL),(29,97,'http://localhost:8080/uploads/f48058c0-72cb-472e-910f-c7952afafb6e_MUSIC IS MY ONLY DRUG.jpg',NULL,NULL),(30,97,'http://localhost:8080/uploads/2d1d2a4e-19ac-480f-b98c-f2e67c49f487_tải xuống (2).jpg',NULL,NULL),(31,97,'http://localhost:8080/uploads/ac129853-7b11-455e-af22-5690b915836a_tải xuống (1).jpg',NULL,NULL),(32,97,'http://localhost:8080/uploads/d0ed934d-4110-4dc2-af74-7f4b99bf86f6_dc banner_ combo prt 1.jpg',NULL,NULL);
/*!40000 ALTER TABLE `hinh_anh_san_pham` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lich_su_giao_dich`
--

DROP TABLE IF EXISTS `lich_su_giao_dich`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lich_su_giao_dich` (
  `giao_dich_id` int NOT NULL AUTO_INCREMENT,
  `vi_id` int NOT NULL,
  `so_tien` decimal(38,2) DEFAULT NULL,
  `loai_giao_dich` varchar(100) DEFAULT NULL,
  `noi_dung` text,
  `log_id` varchar(255) DEFAULT NULL,
  `ngay_tao` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`giao_dich_id`),
  KEY `vi_id` (`vi_id`),
  CONSTRAINT `lich_su_giao_dich_ibfk_1` FOREIGN KEY (`vi_id`) REFERENCES `vi_tien` (`vi_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lich_su_giao_dich`
--

LOCK TABLES `lich_su_giao_dich` WRITE;
/*!40000 ALTER TABLE `lich_su_giao_dich` DISABLE KEYS */;
INSERT INTO `lich_su_giao_dich` VALUES (1,1,500000.00,'NAP',NULL,NULL,'2026-02-26 00:04:24','2026-02-25 17:04:24'),(2,2,100000.00,'NAP',NULL,NULL,'2026-02-26 00:04:24','2026-02-25 17:04:24'),(3,3,200000.00,'NAP',NULL,NULL,'2026-02-26 00:04:24','2026-02-25 17:04:24'),(4,4,300000.00,'NAP',NULL,NULL,'2026-02-26 00:04:24','2026-02-25 17:04:24'),(5,5,400000.00,'NAP',NULL,NULL,'2026-02-26 00:04:24','2026-02-25 17:04:24'),(6,6,500000.00,'NAP',NULL,NULL,'2026-02-26 00:04:24','2026-02-25 17:04:24'),(7,7,600000.00,'NAP',NULL,NULL,'2026-02-26 00:04:24','2026-02-25 17:04:24'),(8,8,700000.00,'NAP',NULL,NULL,'2026-02-26 00:04:24','2026-02-25 17:04:24'),(9,9,800000.00,'NAP',NULL,NULL,'2026-02-26 00:04:24','2026-02-25 17:04:24'),(10,10,900000.00,'NAP',NULL,NULL,'2026-02-26 00:04:24','2026-02-25 17:04:24');
/*!40000 ALTER TABLE `lich_su_giao_dich` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nguoi_dung`
--

DROP TABLE IF EXISTS `nguoi_dung`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nguoi_dung` (
  `nguoi_dung_id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(150) DEFAULT NULL,
  `mat_khau_ma_hoa` varchar(255) DEFAULT NULL,
  `anh_dai_dien` varchar(255) DEFAULT NULL,
  `ho_va_ten` varchar(150) DEFAULT NULL,
  `gioi_tinh` varchar(20) DEFAULT NULL,
  `ngay_sinh` date DEFAULT NULL,
  `so_dien_thoai` varchar(20) DEFAULT NULL,
  `vai_tro_id` int DEFAULT NULL,
  `trang_thai_nguoi_mua` varchar(50) DEFAULT NULL,
  `mo_ta_vi_pham` text,
  `ngay_tao` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ma_xac_nhan` varchar(10) DEFAULT NULL,
  `thoi_gian_het_han_ma` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`nguoi_dung_id`),
  UNIQUE KEY `email` (`email`),
  KEY `vai_tro_id` (`vai_tro_id`),
  CONSTRAINT `nguoi_dung_ibfk_1` FOREIGN KEY (`vai_tro_id`) REFERENCES `vai_tro` (`vai_tro_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nguoi_dung`
--

LOCK TABLES `nguoi_dung` WRITE;
/*!40000 ALTER TABLE `nguoi_dung` DISABLE KEYS */;
INSERT INTO `nguoi_dung` VALUES (1,'user1@gmail.com','$2a$10$0GqbBV0xY4GeqGm0D/REruhhj5I4wiXJ6NrNIUvV25uo84d/Z44Py',NULL,'User 1',NULL,NULL,'0900000001',2,'HOAT_DONG',NULL,'2026-02-26 00:04:24','2026-03-02 11:58:40',NULL,NULL),(2,'user2@gmail.com','123',NULL,'User 2',NULL,NULL,'0900000002',2,NULL,NULL,'2026-02-26 00:04:24','2026-02-25 17:04:24',NULL,NULL),(3,'user3@gmail.com','123',NULL,'User 3',NULL,NULL,'0900000003',2,NULL,NULL,'2026-02-26 00:04:24','2026-03-02 13:36:37',NULL,NULL),(4,'user4@gmail.com','123',NULL,'User 4',NULL,NULL,'0900000004',2,NULL,NULL,'2026-02-26 00:04:24','2026-03-02 13:36:37',NULL,NULL),(5,'user5@gmail.com','123',NULL,'User 5',NULL,NULL,'0900000005',2,NULL,NULL,'2026-02-26 00:04:24','2026-02-25 17:04:24',NULL,NULL),(6,'user6@gmail.com','123',NULL,'User 6',NULL,NULL,'0900000006',2,NULL,NULL,'2026-02-26 00:04:24','2026-02-25 17:04:24',NULL,NULL),(7,'user7@gmail.com','123',NULL,'User 7',NULL,NULL,'0900000007',2,NULL,NULL,'2026-02-26 00:04:24','2026-03-02 13:36:37',NULL,NULL),(8,'user8@gmail.com','123',NULL,'User 8',NULL,NULL,'0900000008',2,NULL,NULL,'2026-02-26 00:04:24','2026-02-25 17:04:24',NULL,NULL),(9,'user9@gmail.com','123',NULL,'User 9',NULL,NULL,'0900000009',2,NULL,NULL,'2026-02-26 00:04:24','2026-02-25 17:04:24',NULL,NULL),(10,'admin@gmail.com','123',NULL,'Admin',NULL,NULL,'0999999999',2,NULL,NULL,'2026-02-26 00:04:24','2026-03-02 13:36:37',NULL,NULL),(15,'bvy6866@gmail.com','$2a$10$eK0IcB3PnVqINbL/KXPl8uPwC3dNCEwr9eq8pB7OwCWTV1/kE8K7y',NULL,'vy bao',NULL,NULL,NULL,NULL,NULL,NULL,'2026-02-26 14:24:37',NULL,NULL,NULL),(18,'dinhtuanduy2007@gmail.com','$2a$10$0GqbBV0xY4GeqGm0D/REruhhj5I4wiXJ6NrNIUvV25uo84d/Z44Py','http://localhost:8080/uploads/avatars/1772524884841_botbien.jpg','Duy','Nam','2007-10-17','0379185939',1,NULL,NULL,'2026-02-26 15:28:50','2026-03-02 13:36:37',NULL,NULL),(19,'duydttv00080@fpt.edu.vn','$2a$10$nfIGAjaiTveHuDxk6.71YOH4wUImSXNO/sZLh8omDCmbB9dZ2b5Em','http://localhost:8080/uploads/avatars/1772464312613_tải xuống (2).jpg','Dinh Tuan Duy (PTCD_BDG)',NULL,NULL,NULL,NULL,'HOAT_DONG',NULL,'2026-02-28 04:02:03',NULL,NULL,NULL);
/*!40000 ALTER TABLE `nguoi_dung` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nhat_ky_thanh_toan`
--

DROP TABLE IF EXISTS `nhat_ky_thanh_toan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nhat_ky_thanh_toan` (
  `log_id` int NOT NULL AUTO_INCREMENT,
  `don_hang_id` int DEFAULT NULL,
  `ma_gd_vnpay` varchar(100) DEFAULT NULL,
  `noi_dung_json` text,
  `ket_qua` varchar(100) DEFAULT NULL,
  `ngay_tao` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`),
  KEY `don_hang_id` (`don_hang_id`),
  CONSTRAINT `nhat_ky_thanh_toan_ibfk_1` FOREIGN KEY (`don_hang_id`) REFERENCES `don_hang` (`don_hang_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nhat_ky_thanh_toan`
--

LOCK TABLES `nhat_ky_thanh_toan` WRITE;
/*!40000 ALTER TABLE `nhat_ky_thanh_toan` DISABLE KEYS */;
INSERT INTO `nhat_ky_thanh_toan` VALUES (1,1,'VNP001',NULL,'SUCCESS','2026-02-26 00:04:24','2026-02-25 17:04:24'),(2,2,'VNP002',NULL,'SUCCESS','2026-02-26 00:04:24','2026-02-25 17:04:24'),(3,3,'VNP003',NULL,'SUCCESS','2026-02-26 00:04:24','2026-02-25 17:04:24'),(4,4,'VNP004',NULL,'SUCCESS','2026-02-26 00:04:24','2026-02-25 17:04:24'),(5,5,'VNP005',NULL,'SUCCESS','2026-02-26 00:04:24','2026-02-25 17:04:24'),(6,6,'VNP006',NULL,'SUCCESS','2026-02-26 00:04:24','2026-02-25 17:04:24'),(7,7,'VNP007',NULL,'SUCCESS','2026-02-26 00:04:24','2026-02-25 17:04:24'),(8,8,'VNP008',NULL,'SUCCESS','2026-02-26 00:04:24','2026-02-25 17:04:24'),(9,9,'VNP009',NULL,'SUCCESS','2026-02-26 00:04:24','2026-02-25 17:04:24'),(10,10,'VNP010',NULL,'SUCCESS','2026-02-26 00:04:24','2026-02-25 17:04:24');
/*!40000 ALTER TABLE `nhat_ky_thanh_toan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `san_pham`
--

DROP TABLE IF EXISTS `san_pham`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `san_pham` (
  `san_pham_id` int NOT NULL AUTO_INCREMENT,
  `nguoi_dung_id` int NOT NULL,
  `ten_san_pham` varchar(255) NOT NULL,
  `danh_muc_id` int DEFAULT NULL,
  `gia` decimal(38,2) NOT NULL,
  `mo_ta` text,
  `tinh_trang` varchar(100) DEFAULT NULL,
  `trong_luong_gram` int DEFAULT NULL,
  `chieu_dai_cm` int DEFAULT NULL,
  `chieu_rong_cm` int DEFAULT NULL,
  `chieu_cao_cm` int DEFAULT NULL,
  `trang_thai` varchar(50) DEFAULT NULL,
  `ngay_dang` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`san_pham_id`),
  KEY `nguoi_dung_id` (`nguoi_dung_id`),
  KEY `danh_muc_id` (`danh_muc_id`),
  CONSTRAINT `san_pham_ibfk_1` FOREIGN KEY (`nguoi_dung_id`) REFERENCES `nguoi_dung` (`nguoi_dung_id`),
  CONSTRAINT `san_pham_ibfk_2` FOREIGN KEY (`danh_muc_id`) REFERENCES `danh_muc` (`danh_muc_id`)
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `san_pham`
--

LOCK TABLES `san_pham` WRITE;
/*!40000 ALTER TABLE `san_pham` DISABLE KEYS */;
INSERT INTO `san_pham` VALUES (1,3,'Iphone X',1,5000000.00,NULL,NULL,NULL,NULL,NULL,NULL,'CON_HANG','2026-02-26 00:04:24','2026-02-25 17:04:24'),(2,4,'Laptop Dell',2,8000000.00,NULL,NULL,NULL,NULL,NULL,NULL,'CON_HANG','2026-02-26 00:04:24','2026-02-25 17:04:24'),(3,3,'Ao khoac',3,300000.00,NULL,NULL,NULL,NULL,NULL,NULL,'CON_HANG','2026-02-26 00:04:24','2026-02-25 17:04:24'),(4,4,'Giay Nike',4,700000.00,NULL,NULL,NULL,NULL,NULL,NULL,'CON_HANG','2026-02-26 00:04:24','2026-02-25 17:04:24'),(5,3,'Dong ho Casio',5,400000.00,NULL,NULL,NULL,NULL,NULL,NULL,'CON_HANG','2026-02-26 00:04:24','2026-02-25 17:04:24'),(6,7,'Ipad',6,6000000.00,NULL,NULL,NULL,NULL,NULL,NULL,'CON_HANG','2026-02-26 00:04:24','2026-02-25 17:04:24'),(7,7,'Tai nghe',7,200000.00,NULL,NULL,NULL,NULL,NULL,NULL,'CON_HANG','2026-02-26 00:04:24','2026-02-25 17:04:24'),(8,3,'Ban go',8,1500000.00,NULL,NULL,NULL,NULL,NULL,NULL,'CON_HANG','2026-02-26 00:04:24','2026-02-25 17:04:24'),(9,4,'Sach Java',9,100000.00,NULL,NULL,NULL,NULL,NULL,NULL,'CON_HANG','2026-02-26 00:04:24','2026-02-25 17:04:24'),(10,7,'Noi chien khong dau',10,900000.00,NULL,NULL,NULL,NULL,NULL,NULL,'CON_HANG','2026-02-26 00:04:24','2026-02-25 17:04:24'),(11,3,'iPhone 7 Pro 64GB',1,6500000.00,'Máy mất zin','36%',188,14,7,1,'DA_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(12,3,'iPhone 11 Pro 64GB',1,6500000.00,'Máy zin chưa sửa','95%',188,14,7,1,'DA_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(13,4,'iPhone 12 128GB',1,8000000.00,'Pin 88%','90%',164,15,7,1,'DA_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(14,7,'iPhone 13 Pro Max',1,15000000.00,'Bản VN/A','99%',240,16,8,1,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(15,1,'iPhone 14 Plus',1,16500000.00,'Còn bảo hành','99%',203,16,8,1,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(16,3,'iPhone 15 Pro',1,23000000.00,'Sạc 10 lần','Mới',187,15,7,1,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(17,4,'Samsung S21 Ultra',1,8500000.00,'Xước viền nhẹ','90%',227,17,8,1,'DANG_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(18,7,'Samsung S22 Plus',1,11000000.00,'Máy đẹp, dán full','95%',195,16,8,1,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(19,1,'Samsung S23 Ultra',1,18500000.00,'Kèm bút S-Pen','99%',234,16,8,1,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(20,3,'Samsung Z Fold 4',1,17000000.00,'Cơ cáp chắc','95%',263,16,13,1,'DANG_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(21,4,'Samsung Z Flip 3',1,5500000.00,'Gập mượt','90%',183,17,7,1,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(22,7,'Samsung Note 20',1,5000000.00,'Bản Mỹ 2 sim','90%',192,16,8,1,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(23,1,'Xiaomi Redmi Note 12',1,3500000.00,'Màn 120Hz','99%',188,17,8,1,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(24,3,'Xiaomi 13 Lite',1,6500000.00,'Mỏng nhẹ','95%',171,16,7,1,'DANG_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(25,4,'Xiaomi Poco X4 Pro',1,4000000.00,'Chiến game tốt','90%',205,16,8,1,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(26,7,'Xiaomi 12T Pro',1,8000000.00,'Camera 200MP','95%',205,16,8,1,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(27,1,'Oppo Reno 7',1,4500000.00,'Lưng da cam','95%',175,16,7,1,'DANG_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(28,3,'Oppo Reno 8T 5G',1,5500000.00,'Màn cong','99%',171,16,7,1,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(29,4,'Oppo Find X5',1,8500000.00,'Chụp đêm siêu đẹp','95%',196,16,7,1,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(30,7,'Realme 10 Pro',1,4800000.00,'Viền siêu mỏng','99%',173,16,7,1,'DANG_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(31,1,'Realme GT Neo 3',1,5200000.00,'Sạc 150W','95%',188,16,8,1,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(32,3,'Vivo V25',1,4000000.00,'Đổi màu mặt lưng','90%',186,16,7,1,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(33,4,'Vivo X80',1,9000000.00,'Camera Zeiss','95%',206,16,8,1,'DANG_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(34,7,'Google Pixel 6',1,4500000.00,'Quốc tế','90%',207,16,7,1,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(35,1,'Google Pixel 7 Pro',1,9500000.00,'Mượt mà','95%',212,16,8,1,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(36,3,'Sony Xperia 1 II',1,4000000.00,'Màn 4K','90%',181,17,7,1,'DANG_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(37,4,'Sony Xperia 5 III',1,6000000.00,'Nhỏ gọn dễ cầm','95%',168,16,7,1,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(38,7,'Asus ROG Phone 5',1,7500000.00,'LED RGB lưng','90%',238,17,8,1,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(39,1,'Asus Zenfone 9',1,10000000.00,'Flagship mini','99%',169,15,7,1,'DANG_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(40,3,'Nokia G21',1,1500000.00,'Máy phụ pin trâu','90%',190,16,8,1,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(41,4,'OnePlus 10 Pro',1,8500000.00,'Hasselblad','95%',201,16,7,1,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(42,7,'MacBook Air M1 256GB',2,13500000.00,'Pin 95%','99%',1290,30,21,2,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(43,1,'MacBook Pro M1 16GB',2,18000000.00,'Bao code mượt','95%',1400,30,21,2,'DANG_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(44,3,'MacBook Air M2',2,19000000.00,'Màu Midnight','99%',1240,30,21,1,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(45,4,'MacBook Pro 2015',2,6000000.00,'15 inch Retina','90%',2040,36,25,2,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(46,7,'MacBook Pro 2019',2,11000000.00,'Touchbar đầy đủ','95%',1370,30,21,2,'DANG_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(47,1,'Dell XPS 13 9370',2,8500000.00,'Vỏ nhôm xước dăm','90%',1200,30,20,1,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(48,3,'Dell Latitude 7490',2,5000000.00,'Bền bỉ','95%',1400,33,22,2,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(49,4,'Dell Precision 5530',2,11500000.00,'Card rời vẽ 3D','95%',1800,36,24,2,'DANG_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(50,7,'Dell Inspiron 3511',2,7000000.00,'RAM 8GB','99%',1800,36,25,2,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(51,1,'Dell Vostro 3400',2,6500000.00,'Văn phòng ổn','95%',1600,33,24,2,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(52,3,'ThinkPad T480s',2,5500000.00,'Phím gõ đỉnh','95%',1320,33,23,2,'DANG_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(53,4,'ThinkPad X1 Carbon',2,9000000.00,'Gen 6 siêu nhẹ','95%',1130,32,22,1,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(54,7,'ThinkPad P52s',2,10500000.00,'Máy trạm','90%',1950,37,25,2,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(55,1,'Lenovo Legion 5',2,14000000.00,'RTX 3050Ti','98%',2400,36,26,3,'DANG_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(56,3,'Lenovo IdeaPad 3',2,5000000.00,'Học online','90%',1600,36,25,2,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(57,4,'Asus ROG Strix G15',2,15000000.00,'LED gầm sáng','95%',2300,36,26,3,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(58,7,'Asus TUF Gaming',2,12000000.00,'Màn 144Hz','95%',2000,36,25,2,'DANG_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(59,1,'Asus Zenbook 14',2,11500000.00,'Nhôm nguyên khối','99%',1170,32,21,1,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(60,3,'Asus Vivobook 15',2,6500000.00,'Màn to rõ','95%',1700,36,24,2,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(61,4,'Asus ExpertBook',2,15000000.00,'Doanh nhân','99%',880,32,20,1,'DANG_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(62,7,'HP Envy 13',2,10000000.00,'Bản lề nâng','95%',1300,31,21,1,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(63,1,'HP Elitebook 840',2,6500000.00,'G6 đẹp keng','95%',1480,33,23,2,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(64,3,'HP Pavilion 14',2,5500000.00,'Loa B&O','90%',1450,32,22,2,'DANG_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(65,4,'HP Omen 15',2,13500000.00,'Tản nhiệt cực mát','95%',2360,36,26,2,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(66,7,'Acer Nitro 5',2,11000000.00,'Bao game nặng','90%',2500,36,27,3,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(67,1,'Acer Swift 3',2,8000000.00,'Pin 6 tiếng','95%',1200,32,21,2,'DANG_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(68,3,'Acer Aspire 7',2,9000000.00,'Laptop đồ họa','95%',2150,36,25,2,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(69,4,'MSI Modern 14',2,7500000.00,'Xách nhẹ nhàng','99%',1300,32,22,2,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(70,7,'MSI GF63',2,9500000.00,'GTX 1650','95%',1860,36,25,2,'DA_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(71,1,'Surface Laptop 3',2,8000000.00,'Màn 2K','95%',1280,31,22,1,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(72,3,'Ao khoac gio chong nuoc',3,250000.00,'Size L','99%',350,30,20,5,'DA_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(73,4,'Ao khoac jean nam',3,300000.00,'Dày dặn, xanh nhạt','95%',600,35,25,5,'DA_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(74,7,'Ao bomber da PU',3,400000.00,'Bo chun chắc chắn','90%',700,40,30,10,'DA_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(75,1,'Ao phao sieu nhe',3,350000.00,'Mùa đông cực ấm','99%',400,35,25,10,'DA_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(76,3,'Ao hoodie form rong',3,200000.00,'Nỉ bông dày','95%',500,30,25,5,'DA_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(77,4,'Ao thun Local Brand',3,150000.00,'Cotton 100%','99%',200,20,15,2,'DA_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(78,7,'Ao polo co be',3,180000.00,'Dáng ôm','95%',250,25,20,2,'DA_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(79,1,'Ao len co lo',3,150000.00,'Lông cừu','95%',300,30,20,5,'DA_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(80,3,'Ao so mi trang',3,120000.00,'Lụa trơn','99%',200,25,20,2,'DA_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(81,4,'Ao so mi caro',3,160000.00,'Khoác ngoài cool ngầu','90%',350,30,25,3,'DA_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(82,7,'Ao vest cong so',3,500000.00,'Đệm vai đứng form','95%',800,45,30,5,'DA_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(83,1,'Ao gile len',3,90000.00,'Họa tiết thổ cẩm','99%',200,20,15,3,'DA_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(84,3,'Ao the thao da banh',3,80000.00,'Thấm hút mồ hôi','Mới',150,20,15,2,'DA_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(85,4,'Ao croptop nu',3,70000.00,'Cổ vuông quyến rũ','95%',100,15,10,2,'DA_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(86,7,'Ao blazer nu',3,250000.00,'Màu be Hàn Quốc','99%',450,35,25,5,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(87,1,'Quan jean ong suong',3,250000.00,'Hack dáng','95%',600,35,20,5,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(88,3,'Quan kaki nam',3,180000.00,'Mặc đi làm','90%',500,30,20,5,'DA_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(89,4,'Quan au xep ly',3,150000.00,'Đã là phẳng','99%',400,35,20,3,'CON_HANG','2026-02-28 15:44:28','2026-02-28 08:44:28'),(90,7,'Quan short tui hop',3,120000.00,'Phong cách bụi bặm','95%',250,25,15,3,'DA_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(91,1,'Quan jogger the thao',3,160000.00,'Bo gấu năng động','95%',450,30,20,5,'DANG_BAN','2026-02-28 15:44:28','2026-02-28 08:44:28'),(92,1,'Đầm Thức Cho Người Đẹp',3,500000.00,'Tiêu đề: Đầm Ngủ Lụa Satin Phối Ren – Đánh Thức Vẻ Đẹp Kiêu Kỳ.\nMô tả: \"Chất lụa satin loại 1 mềm mướt như mây, mang lại cảm giác mát lạnh tức thì ngay khi chạm vào da\". Thiết kế 2 dây mảnh mai kết hợp cùng đường khoét lưng hở sâu giúp nàng khoe trọn đường cong mềm mại.\nĐiểm nổi bật:\nChất liệu lụa Latin cao cấp, không nhăn, không xù lông.\nPhối ren tinh tế tại phần ngực tạo điểm nhấn quyến rũ.\nTặng kèm quần chip sexy cùng tone','Mới',5,36,18,5,'DANG_BAN','2026-02-28 16:37:16','2026-03-02 11:30:40'),(93,1,'Đồ Bơi Mùa Hè',3,500000.00,'Đồ Bơi Cho Mùa Hè','Như mới',5,10,10,10,'DANG_BAN','2026-02-28 17:43:40','2026-03-02 11:30:40'),(94,1,'Hình Gái Xinh Cho Người Cô Đơn',6,100000.00,'Ai cần thì id nhá','Tốt',2,5,5,5,'DANG_BAN','2026-03-01 06:49:25','2026-03-02 11:30:40'),(95,19,'BACIAMI - Set đồ lót nữ quần T hoặc Y ngẫu nhiên gợi cảm sexy kèm choker',3,200000.00,'/ ??????? - Hôn em đi /\n\nCái tên nói lên tất cả , sự ngọt ngào và quyến rũ là cảm hứng để BEI làm ra set đồ này . Sự quyến rũ không thể chối từ , sự cám dỗ không cần lời nói , mật ngọt để chàng không thể nào dứt ra được . Em bé hư hỏng được chàng cưng chiều \n\n? \".\" nhẹ cho BEI biết nếu bạn thích ạ , BEI sẽ gửi ngay thông tin chi tiết sản phẩm cho nàng nhé \n\nFreesize dưới 65kg\n\n__\n\nBEI - Lingerie, sleepwear & corset\n\nNội y nữ theo yêu cầu \n\n?  Based in SaiGon, Shipping Worldwide','Như mới',4,10,10,10,'DANG_BAN','2026-03-02 11:26:49','2026-03-03 16:41:50'),(96,18,'Ảnh treo tường',6,10000.00,'Ai cần Id mình nha ','Mới',12,12,12,12,'DANG_BAN','2026-03-02 11:32:13','2026-03-03 16:41:50'),(97,19,'Banner dán phòng',7,100000.00,'Ảnh dán trang trí phòng ngủ ','Tốt',2,10,10,10,'DANG_BAN','2026-03-02 11:42:40','2026-03-03 16:41:50');
/*!40000 ALTER TABLE `san_pham` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vai_tro`
--

DROP TABLE IF EXISTS `vai_tro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vai_tro` (
  `vai_tro_id` int NOT NULL AUTO_INCREMENT,
  `ten_vai_tro` varchar(100) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`vai_tro_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vai_tro`
--

LOCK TABLES `vai_tro` WRITE;
/*!40000 ALTER TABLE `vai_tro` DISABLE KEYS */;
INSERT INTO `vai_tro` VALUES (1,'ADMIN','2026-02-25 17:04:24','2026-02-25 17:04:24'),(2,'USER','2026-02-25 17:04:24','2026-02-25 17:04:24');
/*!40000 ALTER TABLE `vai_tro` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `van_chuyen`
--

DROP TABLE IF EXISTS `van_chuyen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `van_chuyen` (
  `van_chuyen_id` int NOT NULL AUTO_INCREMENT,
  `don_hang_id` int NOT NULL,
  `ma_van_don_ghn` varchar(100) DEFAULT NULL,
  `phi_ship` decimal(38,2) DEFAULT NULL,
  `trang_thai_ghn` varchar(100) DEFAULT NULL,
  `ngay_du_kien` date DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`van_chuyen_id`),
  KEY `don_hang_id` (`don_hang_id`),
  CONSTRAINT `van_chuyen_ibfk_1` FOREIGN KEY (`don_hang_id`) REFERENCES `don_hang` (`don_hang_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `van_chuyen`
--

LOCK TABLES `van_chuyen` WRITE;
/*!40000 ALTER TABLE `van_chuyen` DISABLE KEYS */;
INSERT INTO `van_chuyen` VALUES (1,1,'GHN001',30000.00,'DANG_GIAO','2026-03-01','2026-02-25 17:04:24','2026-02-25 17:04:24'),(2,2,'GHN002',30000.00,'DANG_GIAO','2026-03-01','2026-02-25 17:04:24','2026-02-25 17:04:24'),(3,3,'GHN003',20000.00,'DA_GIAO','2026-03-01','2026-02-25 17:04:24','2026-02-25 17:04:24'),(4,4,'GHN004',20000.00,'DA_GIAO','2026-03-01','2026-02-25 17:04:24','2026-02-25 17:04:24'),(5,5,'GHN005',20000.00,'DA_GIAO','2026-03-01','2026-02-25 17:04:24','2026-02-25 17:04:24'),(6,6,'GHN006',30000.00,'DANG_GIAO','2026-03-01','2026-02-25 17:04:24','2026-02-25 17:04:24'),(7,7,'GHN007',20000.00,'DANG_GIAO','2026-03-01','2026-02-25 17:04:24','2026-02-25 17:04:24'),(8,8,'GHN008',30000.00,'DANG_GIAO','2026-03-01','2026-02-25 17:04:24','2026-02-25 17:04:24'),(9,9,'GHN009',20000.00,'DA_GIAO','2026-03-01','2026-02-25 17:04:24','2026-02-25 17:04:24'),(10,10,'GHN010',20000.00,'DANG_GIAO','2026-03-01','2026-02-25 17:04:24','2026-02-25 17:04:24');
/*!40000 ALTER TABLE `van_chuyen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vi_tien`
--

DROP TABLE IF EXISTS `vi_tien`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vi_tien` (
  `vi_id` int NOT NULL AUTO_INCREMENT,
  `nguoi_dung_id` int NOT NULL,
  `ngay_cap_nhat` datetime DEFAULT NULL,
  `so_du` decimal(38,2) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`vi_id`),
  KEY `nguoi_dung_id` (`nguoi_dung_id`),
  CONSTRAINT `vi_tien_ibfk_1` FOREIGN KEY (`nguoi_dung_id`) REFERENCES `nguoi_dung` (`nguoi_dung_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vi_tien`
--

LOCK TABLES `vi_tien` WRITE;
/*!40000 ALTER TABLE `vi_tien` DISABLE KEYS */;
INSERT INTO `vi_tien` VALUES (1,1,'2026-02-26 00:04:24',1000000.00,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(2,2,'2026-02-26 00:04:24',2000000.00,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(3,3,'2026-02-26 00:04:24',3000000.00,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(4,4,'2026-02-26 00:04:24',4000000.00,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(5,5,'2026-02-26 00:04:24',5000000.00,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(6,6,'2026-02-26 00:04:24',6000000.00,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(7,7,'2026-02-26 00:04:24',7000000.00,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(8,8,'2026-02-26 00:04:24',8000000.00,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(9,9,'2026-02-26 00:04:24',9000000.00,'2026-02-25 17:04:24','2026-02-25 17:04:24'),(10,10,'2026-02-26 00:04:24',10000000.00,'2026-02-25 17:04:24','2026-02-25 17:04:24');
/*!40000 ALTER TABLE `vi_tien` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-04  0:03:24
