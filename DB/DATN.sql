-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: old2new_db
-- ------------------------------------------------------
-- Server version	8.0.37

CREATE DATABASE old2new_db;

USE old2new_db;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `chi_tiet_don_hang`
--


DROP TABLE IF EXISTS `chi_tiet_don_hang`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chi_tiet_don_hang`
(
    `chi_tiet_id`  int       NOT NULL AUTO_INCREMENT,
    `don_hang_id`  int       NOT NULL,
    `san_pham_id`  int       NOT NULL,
    `so_luong_mua` int            DEFAULT NULL,
    `gia_luc_mua`  decimal(38, 2) DEFAULT NULL,
    `created_at`   timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`chi_tiet_id`),
    KEY `don_hang_id` (`don_hang_id`),
    KEY `san_pham_id` (`san_pham_id`),
    CONSTRAINT `chi_tiet_don_hang_ibfk_1` FOREIGN KEY (`don_hang_id`) REFERENCES `don_hang` (`don_hang_id`),
    CONSTRAINT `chi_tiet_don_hang_ibfk_2` FOREIGN KEY (`san_pham_id`) REFERENCES `san_pham` (`san_pham_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 55
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chi_tiet_don_hang`
--

LOCK TABLES `chi_tiet_don_hang` WRITE;
/*!40000 ALTER TABLE `chi_tiet_don_hang`
    DISABLE KEYS */;
INSERT INTO `chi_tiet_don_hang`
VALUES (1, 1, 1, 1, 5000000.00, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (2, 2, 2, 1, 8000000.00, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (3, 3, 3, 1, 300000.00, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (4, 4, 4, 1, 700000.00, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (5, 5, 5, 1, 400000.00, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (6, 6, 6, 1, 6000000.00, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (7, 7, 7, 1, 200000.00, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (8, 8, 8, 1, 1500000.00, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (9, 9, 9, 1, 100000.00, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (10, 10, 10, 1, 900000.00, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (17, 17, 92, 1, 500000.00, '2026-03-01 06:23:53', NULL),
       (18, 18, 85, 1, 70000.00, '2026-03-01 06:28:29', NULL),
       (19, 19, 84, 1, 80000.00, '2026-03-01 06:29:17', NULL),
       (20, 20, 83, 1, 90000.00, '2026-03-01 06:37:23', NULL),
       (21, 21, 90, 1, 120000.00, '2026-03-01 06:43:41', NULL),
       (22, 22, 80, 1, 120000.00, '2026-03-01 06:49:04', NULL),
       (23, 23, 11, 1, 6500000.00, '2026-03-01 06:56:07', NULL),
       (24, 24, 77, 1, 150000.00, '2026-03-01 07:17:36', NULL),
       (25, 25, 76, 1, 200000.00, '2026-03-01 07:22:37', NULL),
       (26, 26, 13, 1, 8000000.00, '2026-03-01 07:29:03', NULL),
       (27, 27, 78, 1, 180000.00, '2026-03-01 07:29:39', NULL),
       (28, 28, 74, 1, 400000.00, '2026-03-01 07:30:53', NULL),
       (29, 29, 94, 1, 100000.00, '2026-03-01 07:41:55', NULL),
       (30, 30, 92, 1, 500000.00, '2026-03-01 07:52:34', NULL),
       (31, 31, 93, 1, 500000.00, '2026-03-01 21:03:54', NULL),
       (32, 32, 79, 1, 150000.00, '2026-03-01 21:06:15', NULL),
       (33, 33, 81, 1, 160000.00, '2026-03-01 23:19:37', NULL),
       (34, 34, 75, 1, 350000.00, '2026-03-02 02:38:05', NULL),
       (35, 34, 73, 1, 300000.00, '2026-03-02 02:38:05', NULL),
       (36, 35, 70, 1, 9500000.00, '2026-03-02 02:38:42', NULL),
       (37, 36, 82, 1, 500000.00, '2026-03-02 02:43:30', NULL),
       (38, 37, 72, 1, 250000.00, '2026-03-02 03:41:42', NULL),
       (39, 38, 88, 1, 180000.00, '2026-03-02 04:09:12', NULL),
       (42, 41, 97, 1, 100000.00, '2026-03-02 04:44:26', NULL),
       (44, 44, 96, 1, 10000.00, '2026-03-06 00:38:48', NULL),
       (46, 51, 101, 1, 5000000.00, '2026-03-08 00:03:45', NULL),
       (47, 52, 115, 1, 600000.00, '2026-03-17 22:59:45', NULL),
       (48, 53, 128, 1, 400000.00, '2026-03-23 06:51:09', NULL),
       (49, 54, 102, 1, 30000.00, '2026-03-23 07:31:33', NULL),
       (50, 55, 129, 1, 3000000.00, '2026-03-23 08:29:07', NULL),
       (51, 56, 107, 1, 100000.00, '2026-03-23 08:46:30', NULL),
       (52, 57, 109, 1, 50000.00, '2026-03-28 07:54:16', NULL),
       (53, 58, 105, 1, 1999000.00, '2026-03-30 03:46:43', NULL),
       (54, 59, 108, 1, 45000.00, '2026-03-30 05:32:53', NULL);
/*!40000 ALTER TABLE `chi_tiet_don_hang`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `danh_gia`
--

DROP TABLE IF EXISTS `danh_gia`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `danh_gia`
(
    `danh_gia_id`   int       NOT NULL AUTO_INCREMENT,
    `san_pham_id`   int       NOT NULL,
    `nguoi_mua_id`  int       NOT NULL,
    `so_sao`        int            DEFAULT NULL,
    `binh_luan`     text,
    `tong_danh_gia` int            DEFAULT NULL,
    `created_at`    timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`    timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`danh_gia_id`),
    KEY `san_pham_id` (`san_pham_id`),
    KEY `nguoi_mua_id` (`nguoi_mua_id`),
    CONSTRAINT `danh_gia_ibfk_1` FOREIGN KEY (`san_pham_id`) REFERENCES `san_pham` (`san_pham_id`),
    CONSTRAINT `danh_gia_ibfk_2` FOREIGN KEY (`nguoi_mua_id`) REFERENCES `nguoi_dung` (`nguoi_dung_id`),
    CONSTRAINT `danh_gia_chk_1` CHECK ((`so_sao` between 1 and 5))
) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `danh_gia`
--

LOCK TABLES `danh_gia` WRITE;
/*!40000 ALTER TABLE `danh_gia`
    DISABLE KEYS */;
INSERT INTO `danh_gia`
VALUES (1, 1, 1, 5, 'Tot', 1, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (2, 2, 2, 4, 'On', 1, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (3, 3, 1, 5, 'Rat tot', 1, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (4, 4, 2, 3, 'Tam duoc', 1, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (5, 5, 5, 4, 'Dep', 1, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (6, 6, 6, 5, 'Ngon', 1, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (7, 7, 8, 4, 'OK', 1, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (8, 8, 9, 5, 'Xuat sac', 1, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (9, 9, 1, 5, 'Hay', 1, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (10, 10, 2, 4, 'Dung duoc', 1, '2026-02-25 17:04:24', '2026-02-25 17:04:24');
/*!40000 ALTER TABLE `danh_gia`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `danh_muc`
--

DROP TABLE IF EXISTS `danh_muc`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `danh_muc`
(
    `danh_muc_id`  int          NOT NULL AUTO_INCREMENT,
    `ten_danh_muc` varchar(150) NOT NULL,
    `cha_id`       int               DEFAULT NULL,
    `created_at`   timestamp    NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   timestamp    NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`danh_muc_id`),
    KEY `cha_id` (`cha_id`),
    CONSTRAINT `danh_muc_ibfk_1` FOREIGN KEY (`cha_id`) REFERENCES `danh_muc` (`danh_muc_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 49
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `danh_muc`
--

LOCK TABLES `danh_muc` WRITE;
/*!40000 ALTER TABLE `danh_muc`
    DISABLE KEYS */;
INSERT INTO `danh_muc`
VALUES (1, 'Điện Thoại', NULL, '2026-02-25 17:04:24', '2026-03-06 02:44:12'),
       (2, 'Laptop', NULL, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (3, 'Quần Áo', NULL, '2026-02-25 17:04:24', '2026-03-15 09:55:02'),
       (4, 'Giày Dép', NULL, '2026-02-25 17:04:24', '2026-03-15 09:55:10'),
       (5, 'Đồng Hồ', NULL, '2026-02-25 17:04:24', '2026-03-15 09:55:16'),
       (6, 'Đồ Điện Tử', NULL, '2026-02-25 17:04:24', '2026-03-15 09:55:30'),
       (7, 'Phụ Kiện', NULL, '2026-02-25 17:04:24', '2026-03-15 09:55:38'),
       (8, 'Nội Thất Nhà', NULL, '2026-02-25 17:04:24', '2026-03-15 09:55:48'),
       (9, 'Sách ', NULL, '2026-02-25 17:04:24', '2026-03-15 09:55:55'),
       (10, 'Đồ Gia Dụng', NULL, '2026-02-25 17:04:24', '2026-03-15 09:56:05'),
       (11, 'Sách', NULL, '2026-02-28 08:27:55', '2026-02-28 08:27:55'),
       (12, 'Thời trang', NULL, '2026-02-28 08:27:55', '2026-02-28 08:27:55'),
       (13, 'Đồ điện tử', NULL, '2026-02-28 08:27:55', '2026-02-28 08:27:55'),
       (14, 'Sách giáo khoa', 11, '2026-02-28 08:28:04', '2026-02-28 08:28:04'),
       (15, 'Tiểu thuyết', 11, '2026-02-28 08:28:04', '2026-02-28 08:28:04'),
       (16, 'Áo nam', 12, '2026-02-28 08:28:04', '2026-02-28 08:28:04'),
       (17, 'Váy nữ', 12, '2026-02-28 08:28:04', '2026-02-28 08:28:04'),
       (18, 'Điện thoại cũ', 13, '2026-02-28 08:28:04', '2026-02-28 08:28:04'),
       (19, 'Laptop cũ', 2, '2026-02-28 08:28:04', '2026-03-06 02:45:36'),
       (20, 'iPhone cũ', 1, '2026-02-28 08:41:29', '2026-02-28 08:41:29'),
       (21, 'Điện thoại Android', 1, '2026-02-28 08:41:29', '2026-02-28 08:41:29'),
       (22, 'Laptop Dell', 2, '2026-02-28 08:41:29', '2026-02-28 08:41:29'),
       (23, 'Macbook cũ', 2, '2026-02-28 08:41:29', '2026-02-28 08:41:29'),
       (24, 'Áo thun nam/nữ', 3, '2026-02-28 08:41:29', '2026-02-28 08:41:29'),
       (25, 'Quần Jean', 3, '2026-02-28 08:41:29', '2026-02-28 08:41:29'),
       (26, 'Giày thể thao', 4, '2026-02-28 08:41:29', '2026-02-28 08:41:29'),
       (27, 'Giày cao gót', 4, '2026-02-28 08:41:29', '2026-02-28 08:41:29'),
       (28, 'Đồng hồ cơ', 5, '2026-02-28 08:41:29', '2026-02-28 08:41:29'),
       (29, 'Smartwatch', 5, '2026-02-28 08:41:29', '2026-02-28 08:41:29'),
       (30, 'iPad', 6, '2026-02-28 08:41:29', '2026-02-28 08:41:29'),
       (31, 'Tablet Android', 6, '2026-02-28 08:41:29', '2026-02-28 08:41:29'),
       (32, 'Tai nghe / Loa', 7, '2026-02-28 08:41:29', '2026-02-28 08:41:29'),
       (33, 'Ốp lưng / Sạc', 7, '2026-02-28 08:41:29', '2026-02-28 08:41:29'),
       (34, 'Bàn ghế làm việc', 8, '2026-02-28 08:41:29', '2026-02-28 08:41:29'),
       (35, 'Tủ / Kệ', 8, '2026-02-28 08:41:29', '2026-02-28 08:41:29'),
       (36, 'Sách kinh doanh', 9, '2026-02-28 08:41:29', '2026-02-28 08:41:29'),
       (37, 'Sách văn học', 9, '2026-02-28 08:41:29', '2026-02-28 08:41:29'),
       (38, 'Nồi chiên / Lò vi sóng', 10, '2026-02-28 08:41:29', '2026-02-28 08:41:29'),
       (39, 'Máy xay / Dụng cụ bếp', 10, '2026-02-28 08:41:29', '2026-02-28 08:41:29'),
       (40, 'Quần Dài ', 3, '2026-03-06 02:43:18', NULL),
       (41, 'Quần jean bó', 40, '2026-03-06 02:44:45', NULL),
       (42, 'Hàng 99%', 1, '2026-03-06 02:46:11', NULL),
       (43, 'Iphone 11', 42, '2026-03-06 02:46:38', NULL),
       (44, 'Phụ Kiện Xe', NULL, '2026-03-15 09:54:32', NULL),
       (45, 'Các Loại Xe', NULL, '2026-03-15 09:54:52', NULL),
       (46, 'Xe điện', 45, '2026-03-30 06:34:12', NULL),
       (47, 'Xe máy', 45, '2026-03-30 06:34:21', NULL),
       (48, 'Đàn Piano', 6, '2026-03-30 06:35:45', NULL);
/*!40000 ALTER TABLE `danh_muc`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dia_chi`
--

DROP TABLE IF EXISTS `dia_chi`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dia_chi`
(
    `dia_chi_id`       int       NOT NULL AUTO_INCREMENT,
    `nguoi_dung_id`    int       NOT NULL,
    `huyen_code`       int            DEFAULT NULL,
    `phuong_xa_id`     varchar(255)   DEFAULT NULL,
    `dia_chi_chi_tiet` varchar(255)   DEFAULT NULL,
    `dia_chi_mac_dinh` int            DEFAULT NULL,
    `created_at`       timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`       timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`dia_chi_id`),
    KEY `nguoi_dung_id` (`nguoi_dung_id`),
    KEY `phuong_xa_id` (`phuong_xa_id`),
    CONSTRAINT `dia_chi_ibfk_1` FOREIGN KEY (`nguoi_dung_id`) REFERENCES `nguoi_dung` (`nguoi_dung_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 23
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dia_chi`
--

LOCK TABLES `dia_chi` WRITE;
/*!40000 ALTER TABLE `dia_chi`
    DISABLE KEYS */;
INSERT INTO `dia_chi`
VALUES (1, 1, '610706',
        'Người Dùng 1 - 09897654322 |  Cà mau Huyện Ngọc Hiển Xã Viễn An, Xã Viên An, Huyện Ngọc Hiển, Cà Mau', 1,
        '2026-02-25 17:04:24', '2026-03-06 07:37:36', 2186),
       (2, 2, '2', '124 Street', 1, '2026-02-25 17:04:24', '2026-02-25 17:04:24', NULL),
       (3, 3, '3', '125 Street', 1, '2026-02-25 17:04:24', '2026-02-25 17:04:24', NULL),
       (4, 4, '4', '126 Street', 1, '2026-02-25 17:04:24', '2026-02-25 17:04:24', NULL),
       (5, 5, '5', '127 Street', 1, '2026-02-25 17:04:24', '2026-02-25 17:04:24', NULL),
       (6, 6, '6', '128 Street', 1, '2026-02-25 17:04:24', '2026-02-25 17:04:24', NULL),
       (7, 7, '7', '129 Street', 1, '2026-02-25 17:04:24', '2026-02-25 17:04:24', NULL),
       (8, 8, '8', '130 Street', 1, '2026-02-25 17:04:24', '2026-02-25 17:04:24', NULL),
       (9, 9, '9', '131 Street', 1, '2026-02-25 17:04:24', '2026-02-25 17:04:24', NULL),
       (10, 10, '10', '132 Street', 1, '2026-02-25 17:04:24', '2026-02-25 17:04:24', NULL),
       (16, 18, '90768',
        'Tuấn Duy - 0379185939 | TP Hồ Chí Minh, Thủ Đức,AN Khánh, Phường An Khánh, Thành Phố Thủ Đức, Hồ Chí Minh', 1,
        '2026-03-03 16:35:02', '2026-03-03 16:35:02', 3695),
       (17, 18, '300830',
        'Khoa - 0987654321 | Hà Tĩnh Huyện Thạch Hà,Xã Tượng Sơn, Xã Tượng Sơn, Huyện Thạch Hà, Hà Tĩnh', 0,
        '2026-03-03 16:36:40', '2026-03-03 16:44:13', 2024),
       (18, 18, '281901',
        'Nghĩa - 0123456789 | TP Thanh Hóa, Huyện Nông Cống, Thị Trấn Nông Cống,36, Thị trấn Nông Cống, Huyện Nông Cống, Thanh Hóa',
        0, '2026-03-03 16:39:47', '2026-03-03 16:39:47', 2181),
       (20, 15, '250505', 'Bảo Vy - 098786543 | Nam Định, Ý yên , Yên Cường, Xã Yên Cường, Huyện Ý Yên, Nam Định', 1,
        '2026-03-06 07:51:24', '2026-03-06 07:51:24', 1841),
       (21, 19, '910180', 'Hoàng Nhật - 094562874 | Thanh Hóa quê tôi, Thị trấn Hậu Hiền, Huyện Thiệu Hóa, Thanh Hóa',
        1, '2026-03-06 07:55:33', '2026-03-06 07:55:33', 3298),
       (22, 19, '21211',
        'Test - 0987654321 | Công Viên phần mềm quang trung , Phường Trung Mỹ Tây, Quận 12, Hồ Chí Minh', 0,
        '2026-03-08 07:08:25', '2026-03-08 07:08:25', 1454);
/*!40000 ALTER TABLE `dia_chi`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `don_hang`
--

DROP TABLE IF EXISTS `don_hang`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `don_hang`
(
    `don_hang_id`            int       NOT NULL AUTO_INCREMENT,
    `nguoi_dung_id`          int       NOT NULL,
    `dia_chi_id`             int       NOT NULL,
    `tong_tien_hang`         decimal(38, 2) DEFAULT NULL,
    `tong_tien_ship`         decimal(38, 2) DEFAULT NULL,
    `tong_thanh_tien`        decimal(38, 2) DEFAULT NULL,
    `phuong_phuc_thanh_toan` varchar(50)    DEFAULT NULL,
    `trang_thai_thanh_toan`  varchar(50)    DEFAULT NULL,
    `trang_thai_don_hang`    varchar(50)    DEFAULT NULL,
    `ngay_tao`               datetime       DEFAULT CURRENT_TIMESTAMP,
    `updated_at`             timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `ngay_nhan_hang`         datetime(6)    DEFAULT NULL,
    `da_doi_soat`            tinyint(1)     DEFAULT '0',
    PRIMARY KEY (`don_hang_id`),
    KEY `nguoi_dung_id` (`nguoi_dung_id`),
    KEY `dia_chi_id` (`dia_chi_id`),
    CONSTRAINT `don_hang_ibfk_1` FOREIGN KEY (`nguoi_dung_id`) REFERENCES `nguoi_dung` (`nguoi_dung_id`),
    CONSTRAINT `don_hang_ibfk_2` FOREIGN KEY (`dia_chi_id`) REFERENCES `dia_chi` (`dia_chi_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 60
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `don_hang`
--

LOCK TABLES `don_hang` WRITE;
/*!40000 ALTER TABLE `don_hang`
    DISABLE KEYS */;
INSERT INTO `don_hang`
VALUES (1, 1, 1, 5000000.00, 30000.00, 5030000.00, NULL, NULL, NULL, '2026-02-26 00:04:24', '2026-02-25 17:04:24', NULL,
        0),
       (2, 2, 2, 8000000.00, 30000.00, 8030000.00, NULL, NULL, NULL, '2026-02-26 00:04:24', '2026-02-25 17:04:24', NULL,
        0),
       (3, 1, 1, 300000.00, 20000.00, 320000.00, NULL, NULL, NULL, '2026-02-26 00:04:24', '2026-02-25 17:04:24', NULL,
        0),
       (4, 2, 2, 700000.00, 20000.00, 720000.00, NULL, NULL, NULL, '2026-02-26 00:04:24', '2026-02-25 17:04:24', NULL,
        0),
       (5, 5, 5, 400000.00, 20000.00, 420000.00, NULL, NULL, NULL, '2026-02-26 00:04:24', '2026-02-25 17:04:24', NULL,
        0),
       (6, 6, 6, 6000000.00, 30000.00, 6030000.00, NULL, NULL, NULL, '2026-02-26 00:04:24', '2026-02-25 17:04:24', NULL,
        0),
       (7, 8, 8, 200000.00, 20000.00, 220000.00, NULL, NULL, NULL, '2026-02-26 00:04:24', '2026-02-25 17:04:24', NULL,
        0),
       (8, 9, 9, 1500000.00, 30000.00, 1530000.00, NULL, NULL, NULL, '2026-02-26 00:04:24', '2026-02-25 17:04:24', NULL,
        0),
       (9, 1, 1, 100000.00, 20000.00, 120000.00, NULL, NULL, NULL, '2026-02-26 00:04:24', '2026-02-25 17:04:24', NULL,
        0),
       (10, 2, 2, 900000.00, 20000.00, 920000.00, NULL, NULL, NULL, '2026-02-26 00:04:24', '2026-02-25 17:04:24', NULL,
        0),
       (17, 1, 1, 500000.00, 30000.00, 530000.00, 'VNPAY', 'CHUA_THANH_TOAN', 'DA_HUY', '2026-03-01 13:23:53', NULL,
        NULL, 0),
       (18, 1, 1, 70000.00, 30000.00, 100000.00, 'COD', 'CHUA_THANH_TOAN', 'CHO_XAC_NHAN', '2026-03-01 13:28:29', NULL,
        NULL, 0),
       (19, 1, 1, 80000.00, 30000.00, 110000.00, 'VNPAY', 'CHUA_THANH_TOAN', 'CHO_XAC_NHAN', '2026-03-01 13:29:17',
        NULL, NULL, 0),
       (20, 1, 1, 90000.00, 30000.00, 120000.00, 'VNPAY', 'CHUA_THANH_TOAN', 'CHO_XAC_NHAN', '2026-03-01 13:37:23',
        NULL, NULL, 0),
       (21, 1, 1, 120000.00, 30000.00, 150000.00, 'VNPAY', 'CHUA_THANH_TOAN', 'CHO_XAC_NHAN', '2026-03-01 13:43:40',
        NULL, NULL, 0),
       (22, 1, 1, 120000.00, 30000.00, 150000.00, 'VNPAY', 'CHUA_THANH_TOAN', 'CHO_XAC_NHAN', '2026-03-01 13:49:04',
        NULL, NULL, 0),
       (23, 1, 1, 6500000.00, 30000.00, 6530000.00, 'VNPAY', 'CHUA_THANH_TOAN', 'CHO_XAC_NHAN', '2026-03-01 13:56:07',
        NULL, NULL, 0),
       (24, 1, 1, 150000.00, 30000.00, 180000.00, 'VNPAY', 'CHUA_THANH_TOAN', 'CHO_XAC_NHAN', '2026-03-01 14:17:36',
        NULL, NULL, 0),
       (25, 1, 1, 200000.00, 30000.00, 230000.00, 'VNPAY', 'CHUA_THANH_TOAN', 'CHO_XAC_NHAN', '2026-03-01 14:22:37',
        NULL, NULL, 0),
       (26, 1, 1, 8000000.00, 30000.00, 8030000.00, 'VNPAY', 'CHUA_THANH_TOAN', 'CHO_XAC_NHAN', '2026-03-01 14:29:02',
        NULL, NULL, 0),
       (27, 1, 1, 180000.00, 30000.00, 210000.00, 'VNPAY', 'CHUA_THANH_TOAN', 'CHO_XAC_NHAN', '2026-03-01 14:29:39',
        NULL, NULL, 0),
       (28, 1, 1, 400000.00, 30000.00, 430000.00, 'VNPAY', 'CHUA_THANH_TOAN', 'CHO_XAC_NHAN', '2026-03-01 14:30:53',
        NULL, NULL, 0),
       (29, 1, 1, 100000.00, 30000.00, 130000.00, 'VNPAY', 'DA_THANH_TOAN', 'CHO_XAC_NHAN', '2026-03-01 14:41:55', NULL,
        NULL, 0),
       (30, 1, 1, 500000.00, 30000.00, 530000.00, 'VNPAY', 'DA_THANH_TOAN', 'CHO_XAC_NHAN', '2026-03-01 14:52:34', NULL,
        NULL, 0),
       (31, 1, 1, 500000.00, 30000.00, 530000.00, 'VNPAY', 'DA_THANH_TOAN', 'CHO_XAC_NHAN', '2026-03-02 04:03:54', NULL,
        NULL, 0),
       (32, 1, 1, 150000.00, 30000.00, 180000.00, 'VNPAY', 'THANH_TOAN_THAT_BAI', 'CHO_XAC_NHAN', '2026-03-02 04:06:15',
        NULL, NULL, 0),
       (33, 1, 1, 160000.00, 30000.00, 190000.00, 'VNPAY', 'CHUA_THANH_TOAN', 'CHO_XAC_NHAN', '2026-03-02 06:19:37',
        NULL, NULL, 0),
       (34, 1, 1, 650000.00, 30000.00, 680000.00, 'COD', 'CHUA_THANH_TOAN', 'CHO_XAC_NHAN', '2026-03-02 09:38:05', NULL,
        NULL, 0),
       (35, 1, 1, 9500000.00, 30000.00, 9530000.00, 'VNPAY', 'DA_THANH_TOAN', 'CHO_XAC_NHAN', '2026-03-02 09:38:42',
        NULL, NULL, 0),
       (36, 1, 1, 500000.00, 30000.00, 530000.00, 'COD', 'CHUA_THANH_TOAN', 'CHO_XAC_NHAN', '2026-03-02 09:43:30', NULL,
        NULL, 0),
       (37, 1, 1, 250000.00, 30000.00, 280000.00, 'COD', 'CHUA_THANH_TOAN', 'CHO_XAC_NHAN', '2026-03-02 10:41:41', NULL,
        NULL, 0),
       (38, 18, 1, 180000.00, 30000.00, 210000.00, 'COD', 'CHUA_THANH_TOAN', 'CHO_XAC_NHAN', '2026-03-02 11:09:12',
        NULL, NULL, 0),
       (40, 18, 1, 10000.00, 30000.00, 40000.00, 'VNPAY', 'DA_THANH_TOAN', 'CHO_XAC_NHAN', '2026-03-02 11:32:36', NULL,
        NULL, 0),
       (41, 18, 1, 100000.00, 30000.00, 130000.00, 'VNPAY', 'DA_THANH_TOAN', 'DANG_GIAO', '2026-03-02 11:44:26', NULL,
        NULL, 0),
       (44, 1, 1, 10000.00, 60500.00, 70500.00, 'COD', 'CHUA_THANH_TOAN', 'CHO_XAC_NHAN', '2026-03-06 07:38:48', NULL,
        NULL, 0),
       (51, 19, 21, 5000000.00, 82500.00, 5082500.00, 'VNPAY', 'CHUA_THANH_TOAN', 'CHO_XAC_NHAN', '2026-03-08 07:03:45',
        NULL, NULL, 0),
       (52, 18, 17, 600000.00, 82500.00, 682500.00, 'VNPAY', 'DA_THANH_TOAN', 'DANG_GIAO', '2026-03-18 05:59:45', NULL,
        NULL, 0),
       (53, 18, 16, 400000.00, 31900.00, 431900.00, 'VNPAY', 'DA_THANH_TOAN', 'CHO_XAC_NHAN', '2026-03-23 13:51:09',
        NULL, NULL, 0),
       (54, 18, 16, 30000.00, 31900.00, 61900.00, 'VNPAY', 'DA_THANH_TOAN', 'HOAN_THANH', '2026-03-23 14:31:33', NULL,
        NULL, 0),
       (55, 18, 16, 3000000.00, 31900.00, 3031900.00, 'VNPAY', 'DA_THANH_TOAN', 'HOAN_THANH', '2026-03-23 15:29:07',
        NULL, NULL, 0),
       (56, 19, 21, 100000.00, 82500.00, 182500.00, 'VNPAY', 'DA_THANH_TOAN', 'HOAN_THANH', '2026-03-20 15:46:30',
        '2026-03-23 15:49:47', NULL, 0),
       (57, 18, 16, 50000.00, 31900.00, 81900.00, 'COD', 'CHUA_THANH_TOAN', 'DANG_HOAN_HANG', '2026-03-28 14:54:16',
        '2026-03-30 11:20:30', NULL, 0),
       (58, 19, 21, 1999000.00, 82500.00, 2081500.00, 'VNPAY', 'DA_THANH_TOAN', 'HOAN_THANH', '2026-03-30 10:46:43',
        NULL, NULL, 0),
       (59, 18, 16, 45000.00, 0.00, 45000.00, 'VNPAY', 'DA_THANH_TOAN', 'DA_HOAN_TIEN', '2026-03-30 12:32:53',
        '2026-03-30 12:38:15', '2026-03-30 12:38:49.876320', 1);
/*!40000 ALTER TABLE `don_hang`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hinh_anh_san_pham`
--

DROP TABLE IF EXISTS `hinh_anh_san_pham`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hinh_anh_san_pham`
(
    `hinh_anh_id`   int       NOT NULL AUTO_INCREMENT,
    `san_pham_id`   int       NOT NULL,
    `duong_dan_anh` varchar(255)   DEFAULT NULL,
    `created_at`    timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`    timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`hinh_anh_id`),
    KEY `san_pham_id` (`san_pham_id`),
    CONSTRAINT `hinh_anh_san_pham_ibfk_1` FOREIGN KEY (`san_pham_id`) REFERENCES `san_pham` (`san_pham_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 78
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hinh_anh_san_pham`
--

LOCK TABLES `hinh_anh_san_pham` WRITE;
/*!40000 ALTER TABLE `hinh_anh_san_pham`
    DISABLE KEYS */;
INSERT INTO `hinh_anh_san_pham`
VALUES (50, 111, 'https://res.cloudinary.com/dep3rejur/image/upload/v1774877992/ol5vqn2c6mbh6g1tvav3.jpg', NULL,
        '2026-03-30 14:54:42'),
       (51, 112, 'https://res.cloudinary.com/dep3rejur/image/upload/v1774877992/ol5vqn2c6mbh6g1tvav3.jpg', NULL,
        '2026-03-30 14:54:42'),
       (52, 113, 'https://res.cloudinary.com/dep3rejur/image/upload/v1774374003/ar05ohge8qhovrz2xm1w.jpg', NULL,
        '2026-03-30 14:54:42'),
       (53, 114, 'https://res.cloudinary.com/dep3rejur/image/upload/v1774368607/j2cfpqgebprzzeipwdqz.jpg', NULL,
        '2026-03-30 14:54:42'),
       (54, 115, 'https://res.cloudinary.com/dep3rejur/image/upload/v1774364797/y6ngmckpul1t7jalvhnu.jpg', NULL,
        '2026-03-30 14:54:42'),
       (55, 116, 'https://res.cloudinary.com/dep3rejur/image/upload/v1774368607/j2cfpqgebprzzeipwdqz.jpg', NULL,
        '2026-03-30 14:54:42'),
       (56, 117, 'https://res.cloudinary.com/dep3rejur/image/upload/v1774368607/j2cfpqgebprzzeipwdqz.jpg', NULL,
        '2026-03-30 14:54:42'),
       (57, 118, 'https://res.cloudinary.com/dep3rejur/image/upload/v1774364797/y6ngmckpul1t7jalvhnu.jpg', NULL,
        '2026-03-30 14:54:42'),
       (58, 119, 'https://res.cloudinary.com/dep3rejur/image/upload/v1774877583/wilivr8nk3mit6zedd9m.jpg', NULL,
        '2026-03-30 14:54:42'),
       (59, 120, 'https://res.cloudinary.com/dep3rejur/image/upload/v1774877992/ol5vqn2c6mbh6g1tvav3.jpg', NULL,
        '2026-03-30 14:54:42'),
       (60, 121, 'https://res.cloudinary.com/dep3rejur/image/upload/v1774374003/ar05ohge8qhovrz2xm1w.jpg', NULL,
        '2026-03-30 14:54:42'),
       (61, 122, 'https://res.cloudinary.com/dep3rejur/image/upload/v1774877992/ol5vqn2c6mbh6g1tvav3.jpg', NULL,
        '2026-03-30 14:54:42'),
       (62, 123, 'https://res.cloudinary.com/dep3rejur/image/upload/v1774877583/wilivr8nk3mit6zedd9m.jpg', NULL,
        '2026-03-30 14:54:42'),
       (63, 124, 'https://res.cloudinary.com/dep3rejur/image/upload/v1774374003/ar05ohge8qhovrz2xm1w.jpg', NULL,
        '2026-03-30 14:54:42'),
       (65, 125, 'https://res.cloudinary.com/dep3rejur/image/upload/v1774368607/j2cfpqgebprzzeipwdqz.jpg', NULL,
        '2026-03-30 14:54:42'),
       (67, 128, 'https://res.cloudinary.com/dep3rejur/image/upload/v1774374003/ar05ohge8qhovrz2xm1w.jpg', NULL,
        '2026-03-30 14:54:42'),
       (68, 129, 'https://res.cloudinary.com/dep3rejur/image/upload/v1774877583/wilivr8nk3mit6zedd9m.jpg', NULL,
        '2026-03-30 14:54:42'),
       (69, 130, 'https://res.cloudinary.com/dep3rejur/image/upload/v1774364796/dosuvwggvqssodxdg0el.jpg', NULL, NULL),
       (70, 131, 'https://res.cloudinary.com/dep3rejur/image/upload/v1774364797/y6ngmckpul1t7jalvhnu.jpg', NULL, NULL),
       (71, 132, 'https://res.cloudinary.com/dep3rejur/image/upload/v1774368605/brzq9pz0wkrrwi5b1emh.jpg', NULL, NULL),
       (72, 133, 'https://res.cloudinary.com/dep3rejur/image/upload/v1774368607/j2cfpqgebprzzeipwdqz.jpg', NULL, NULL),
       (73, 134, 'https://res.cloudinary.com/dep3rejur/image/upload/v1774374003/ar05ohge8qhovrz2xm1w.jpg', NULL, NULL),
       (74, 135, 'https://res.cloudinary.com/dep3rejur/image/upload/v1774877583/wilivr8nk3mit6zedd9m.jpg', NULL, NULL),
       (75, 136, 'https://res.cloudinary.com/dep3rejur/image/upload/v1774877706/l495k8ybik3kvqsgsyuu.jpg', NULL, NULL),
       (76, 137, 'https://res.cloudinary.com/dep3rejur/image/upload/v1774877791/svdnaevfcy59zobpvqps.jpg', NULL, NULL),
       (77, 138, 'https://res.cloudinary.com/dep3rejur/image/upload/v1774877992/ol5vqn2c6mbh6g1tvav3.jpg', NULL, NULL);
/*!40000 ALTER TABLE `hinh_anh_san_pham`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lich_su_giao_dich`
--

DROP TABLE IF EXISTS `lich_su_giao_dich`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lich_su_giao_dich`
(
    `giao_dich_id`   int       NOT NULL AUTO_INCREMENT,
    `vi_id`          int       NOT NULL,
    `so_tien`        decimal(38, 2) DEFAULT NULL,
    `loai_giao_dich` varchar(100)   DEFAULT NULL,
    `noi_dung`       text,
    `log_id`         varchar(255)   DEFAULT NULL,
    `ngay_tao`       datetime       DEFAULT CURRENT_TIMESTAMP,
    `updated_at`     timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `don_hang_id`    int            DEFAULT NULL,
    `trang_thai`     varchar(50)    DEFAULT 'DANG_CHO',
    PRIMARY KEY (`giao_dich_id`),
    KEY `vi_id` (`vi_id`),
    KEY `fk_lich_su_don_hang` (`don_hang_id`),
    CONSTRAINT `fk_lich_su_don_hang` FOREIGN KEY (`don_hang_id`) REFERENCES `don_hang` (`don_hang_id`),
    CONSTRAINT `lich_su_giao_dich_ibfk_1` FOREIGN KEY (`vi_id`) REFERENCES `vi_tien` (`vi_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 22
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lich_su_giao_dich`
--

LOCK TABLES `lich_su_giao_dich` WRITE;
/*!40000 ALTER TABLE `lich_su_giao_dich`
    DISABLE KEYS */;
INSERT INTO `lich_su_giao_dich`
VALUES (1, 1, 500000.00, 'NAP', NULL, NULL, '2026-02-26 00:04:24', '2026-02-25 17:04:24', NULL, 'DANG_CHO'),
       (2, 2, 100000.00, 'NAP', NULL, NULL, '2026-02-26 00:04:24', '2026-02-25 17:04:24', NULL, 'DANG_CHO'),
       (3, 3, 200000.00, 'NAP', NULL, NULL, '2026-02-26 00:04:24', '2026-02-25 17:04:24', NULL, 'DANG_CHO'),
       (4, 4, 300000.00, 'NAP', NULL, NULL, '2026-02-26 00:04:24', '2026-02-25 17:04:24', NULL, 'DANG_CHO'),
       (5, 5, 400000.00, 'NAP', NULL, NULL, '2026-02-26 00:04:24', '2026-02-25 17:04:24', NULL, 'DANG_CHO'),
       (6, 6, 500000.00, 'NAP', NULL, NULL, '2026-02-26 00:04:24', '2026-02-25 17:04:24', NULL, 'DANG_CHO'),
       (7, 7, 600000.00, 'NAP', NULL, NULL, '2026-02-26 00:04:24', '2026-02-25 17:04:24', NULL, 'DANG_CHO'),
       (8, 8, 700000.00, 'NAP', NULL, NULL, '2026-02-26 00:04:24', '2026-02-25 17:04:24', NULL, 'DANG_CHO'),
       (9, 9, 800000.00, 'NAP', NULL, NULL, '2026-02-26 00:04:24', '2026-02-25 17:04:24', NULL, 'DANG_CHO'),
       (10, 10, 900000.00, 'NAP', NULL, NULL, '2026-02-26 00:04:24', '2026-02-25 17:04:24', NULL, 'DANG_CHO'),
       (11, 11, 61900.00, 'THANH_TOAN_MUA_HANG', 'Thanh toán đơn hàng #54 qua VNPAY', '15463674', '2026-03-23 14:31:59',
        NULL, 54, 'THANH_CONG'),
       (12, 12, 30000.00, 'GIAI_NGAN_BAN_HANG', 'Giải ngân tiền bán đơn hàng #54', NULL, '2026-03-23 15:17:50', NULL,
        54, 'THANH_CONG'),
       (13, 11, 3031900.00, 'THANH_TOAN_MUA_HANG', 'Thanh toán đơn hàng #55 qua VNPAY', '15463736',
        '2026-03-23 15:29:48', NULL, 55, 'THANH_CONG'),
       (14, 12, 3000000.00, 'GIAI_NGAN_BAN_HANG', 'Giải ngân tiền bán đơn hàng #55', NULL, '2026-03-23 15:31:20', NULL,
        55, 'THANH_CONG'),
       (15, 12, 182500.00, 'THANH_TOAN_MUA_HANG', 'Thanh toán đơn hàng #56 qua VNPAY', '15463753',
        '2026-03-23 15:46:57', NULL, 56, 'THANH_CONG'),
       (16, 11, 100000.00, 'GIAI_NGAN_BAN_HANG', 'Giải ngân tiền bán đơn hàng #56', NULL, '2026-03-23 15:49:51', NULL,
        56, 'THANH_CONG'),
       (17, 12, 2081500.00, 'THANH_TOAN_MUA_HANG', 'Thanh toán đơn hàng #58 qua VNPAY', '15475123',
        '2026-03-30 10:47:26', NULL, 58, 'THANH_CONG'),
       (18, 11, 1999000.00, 'GIAI_NGAN_BAN_HANG', 'Giải ngân tiền bán đơn hàng #58', NULL, '2026-03-30 10:58:53', NULL,
        58, 'THANH_CONG'),
       (19, 11, 45000.00, 'THANH_TOAN_MUA_HANG', 'Thanh toán đơn hàng #59 qua VNPAY', '15475226', '2026-03-30 12:33:19',
        NULL, 59, 'THANH_CONG'),
       (20, 12, 45000.00, 'GIAI_NGAN_BAN_HANG', 'Giải ngân tiền bán đơn hàng #59', NULL, '2026-03-30 12:34:45', NULL,
        59, 'THANH_CONG'),
       (21, 11, 45000.00, 'HOAN_TIEN', 'Hoàn tiền do trả hàng thành công đơn O2N-59', NULL, '2026-03-30 12:55:17', NULL,
        59, 'THANH_CONG');
/*!40000 ALTER TABLE `lich_su_giao_dich`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nguoi_dung`
--

DROP TABLE IF EXISTS `nguoi_dung`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nguoi_dung`
(
    `nguoi_dung_id`        int       NOT NULL AUTO_INCREMENT,
    `email`                varchar(150)   DEFAULT NULL,
    `mat_khau_ma_hoa`      varchar(255)   DEFAULT NULL,
    `anh_dai_dien`         varchar(255)   DEFAULT NULL,
    `ho_va_ten`            varchar(150)   DEFAULT NULL,
    `gioi_tinh`            varchar(20)    DEFAULT NULL,
    `ngay_sinh`            date           DEFAULT NULL,
    `so_dien_thoai`        varchar(20)    DEFAULT NULL,
    `vai_tro_id`           int            DEFAULT NULL,
    `trang_thai_nguoi_mua` varchar(50)    DEFAULT NULL,
    `mo_ta_vi_pham`        text,
    `ngay_tao`             datetime       DEFAULT CURRENT_TIMESTAMP,
    `updated_at`           timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `ma_xac_nhan`          varchar(10)    DEFAULT NULL,
    `thoi_gian_het_han_ma` datetime(6)    DEFAULT NULL,
    PRIMARY KEY (`nguoi_dung_id`),
    UNIQUE KEY `email` (`email`),
    KEY `vai_tro_id` (`vai_tro_id`),
    CONSTRAINT `nguoi_dung_ibfk_1` FOREIGN KEY (`vai_tro_id`) REFERENCES `vai_tro` (`vai_tro_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 20
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nguoi_dung`
--

LOCK TABLES `nguoi_dung` WRITE;
/*!40000 ALTER TABLE `nguoi_dung`
    DISABLE KEYS */;
INSERT INTO `nguoi_dung`
VALUES (1, 'user1@gmail.com', '$2a$10$0GqbBV0xY4GeqGm0D/REruhhj5I4wiXJ6NrNIUvV25uo84d/Z44Py',
        'https://res.cloudinary.com/dep3rejur/image/upload/v1774424974/atp7y1vcokec6qxnsspc.png', 'User 1', 'Khác',
        NULL, '', 2, 'HOAT_DONG', NULL, '2026-02-26 00:04:24', '2026-03-02 11:58:40', NULL, NULL),
       (2, 'user2@gmail.com', '123', NULL, 'User 2', NULL, NULL, '0900000002', 2, 'HOAT_DONG', NULL,
        '2026-02-26 00:04:24', '2026-03-12 16:08:04', NULL, NULL),
       (3, 'user3@gmail.com', '123', NULL, 'User 3', NULL, NULL, '0900000003', 2, 'HOAT_DONG', NULL,
        '2026-02-26 00:04:24', '2026-03-12 16:08:04', NULL, NULL),
       (4, 'user4@gmail.com', '123', NULL, 'User 4', NULL, NULL, '0900000004', 2, 'HOAT_DONG', NULL,
        '2026-02-26 00:04:24', '2026-03-12 16:08:04', NULL, NULL),
       (5, 'user5@gmail.com', '123', NULL, 'User 5', NULL, NULL, '0900000005', 2, 'HOAT_DONG', NULL,
        '2026-02-26 00:04:24', '2026-03-12 16:08:04', NULL, NULL),
       (6, 'user6@gmail.com', '123', NULL, 'User 6', NULL, NULL, '0900000006', 2, 'HOAT_DONG', NULL,
        '2026-02-26 00:04:24', '2026-03-12 16:08:04', NULL, NULL),
       (7, 'user7@gmail.com', '123', NULL, 'User 7', NULL, NULL, '0900000007', 2, 'HOAT_DONG', NULL,
        '2026-02-26 00:04:24', '2026-03-12 16:08:04', NULL, NULL),
       (8, 'user8@gmail.com', '123', NULL, 'User 8', NULL, NULL, '0900000008', 2, 'HOAT_DONG', NULL,
        '2026-02-26 00:04:24', '2026-03-12 16:08:04', NULL, NULL),
       (9, 'user9@gmail.com', '123', NULL, 'User 9', NULL, NULL, '0900000009', 2, 'HOAT_DONG', NULL,
        '2026-02-26 00:04:24', '2026-03-12 16:08:04', NULL, NULL),
       (10, 'admin@gmail.com', '123', NULL, 'Admin', NULL, NULL, '0999999999', 2, 'HOAT_DONG', NULL,
        '2026-02-26 00:04:24', '2026-03-12 16:08:04', NULL, NULL),
       (15, 'bvy6866@gmail.com', '$2a$10$eK0IcB3PnVqINbL/KXPl8uPwC3dNCEwr9eq8pB7OwCWTV1/kE8K7y', NULL, 'vy bao', 'Khác',
        NULL, '0379185939', NULL, 'HOAT_DONG', NULL, '2026-02-26 14:24:37', '2026-03-12 16:08:04', NULL, NULL),
       (18, 'dinhtuanduy2007@gmail.com', '$2a$10$0GqbBV0xY4GeqGm0D/REruhhj5I4wiXJ6NrNIUvV25uo84d/Z44Py',
        'https://res.cloudinary.com/dep3rejur/image/upload/v1774878045/evldvvcq3shxom8f6rie.jpg', 'Duy', 'Nam',
        '2007-10-17', '0379185939', 1, 'HOAT_DONG', NULL, '2026-02-26 15:28:50', '2026-03-12 16:08:04', NULL, NULL),
       (19, 'duydttv00080@fpt.edu.vn', '$2a$10$nfIGAjaiTveHuDxk6.71YOH4wUImSXNO/sZLh8omDCmbB9dZ2b5Em',
        'http://localhost:8080/uploads/avatars/1772464312613_tải xuống (2).jpg', 'Dinh Tuan Duy (PTCD_BDG)', 'Khác',
        NULL, '0379185939', NULL, 'HOAT_DONG', NULL, '2026-02-28 04:02:03', NULL, NULL, NULL);
/*!40000 ALTER TABLE `nguoi_dung`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nhat_ky_thanh_toan`
--

DROP TABLE IF EXISTS `nhat_ky_thanh_toan`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nhat_ky_thanh_toan`
(
    `log_id`        int       NOT NULL AUTO_INCREMENT,
    `don_hang_id`   int            DEFAULT NULL,
    `ma_gd_vnpay`   varchar(100)   DEFAULT NULL,
    `noi_dung_json` text,
    `ket_qua`       varchar(100)   DEFAULT NULL,
    `ngay_tao`      datetime       DEFAULT CURRENT_TIMESTAMP,
    `updated_at`    timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`log_id`),
    KEY `don_hang_id` (`don_hang_id`),
    CONSTRAINT `nhat_ky_thanh_toan_ibfk_1` FOREIGN KEY (`don_hang_id`) REFERENCES `don_hang` (`don_hang_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nhat_ky_thanh_toan`
--

LOCK TABLES `nhat_ky_thanh_toan` WRITE;
/*!40000 ALTER TABLE `nhat_ky_thanh_toan`
    DISABLE KEYS */;
INSERT INTO `nhat_ky_thanh_toan`
VALUES (1, 1, 'VNP001', NULL, 'SUCCESS', '2026-02-26 00:04:24', '2026-02-25 17:04:24'),
       (2, 2, 'VNP002', NULL, 'SUCCESS', '2026-02-26 00:04:24', '2026-02-25 17:04:24'),
       (3, 3, 'VNP003', NULL, 'SUCCESS', '2026-02-26 00:04:24', '2026-02-25 17:04:24'),
       (4, 4, 'VNP004', NULL, 'SUCCESS', '2026-02-26 00:04:24', '2026-02-25 17:04:24'),
       (5, 5, 'VNP005', NULL, 'SUCCESS', '2026-02-26 00:04:24', '2026-02-25 17:04:24'),
       (6, 6, 'VNP006', NULL, 'SUCCESS', '2026-02-26 00:04:24', '2026-02-25 17:04:24'),
       (7, 7, 'VNP007', NULL, 'SUCCESS', '2026-02-26 00:04:24', '2026-02-25 17:04:24'),
       (8, 8, 'VNP008', NULL, 'SUCCESS', '2026-02-26 00:04:24', '2026-02-25 17:04:24'),
       (9, 9, 'VNP009', NULL, 'SUCCESS', '2026-02-26 00:04:24', '2026-02-25 17:04:24'),
       (10, 10, 'VNP010', NULL, 'SUCCESS', '2026-02-26 00:04:24', '2026-02-25 17:04:24');
/*!40000 ALTER TABLE `nhat_ky_thanh_toan`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `san_pham`
--

-- ====================================================
-- BẢNG 2: SẢN PHẨM
-- ====================================================
DROP TABLE IF EXISTS `san_pham`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `san_pham`
(
    `san_pham_id`         int            NOT NULL AUTO_INCREMENT,
    `nguoi_dung_id`       int            NOT NULL,
    `ten_san_pham`        varchar(255)   NOT NULL,
    `danh_muc_id`         int                 DEFAULT NULL,
    `gia`                 decimal(38, 2) NOT NULL,
    `mo_ta`               text,
    `tinh_trang`          varchar(100)        DEFAULT NULL,
    `trong_luong_gram`    int                 DEFAULT NULL,
    `chieu_dai_cm`        int                 DEFAULT NULL,
    `chieu_rong_cm`       int                 DEFAULT NULL,
    `chieu_cao_cm`        int                 DEFAULT NULL,
    `trang_thai`          varchar(50)         DEFAULT NULL,
    `id_dia_chi_cua_hang` int                 DEFAULT NULL, -- 🔥 CỘT ĐỊA CHỈ KHO NẰM Ở ĐÂY
    `ngay_dang`           datetime            DEFAULT CURRENT_TIMESTAMP,
    `updated_at`          timestamp      NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`san_pham_id`),
    KEY `nguoi_dung_id` (`nguoi_dung_id`),
    KEY `danh_muc_id` (`danh_muc_id`),
    KEY `id_dia_chi_cua_hang` (`id_dia_chi_cua_hang`),
    CONSTRAINT `san_pham_ibfk_1` FOREIGN KEY (`nguoi_dung_id`) REFERENCES `nguoi_dung` (`nguoi_dung_id`),
    CONSTRAINT `san_pham_ibfk_2` FOREIGN KEY (`danh_muc_id`) REFERENCES `danh_muc` (`danh_muc_id`),
    CONSTRAINT `fk_sp_diachicuahang` FOREIGN KEY (`id_dia_chi_cua_hang`) REFERENCES `dia_chi_cua_hang` (`id`) ON DELETE SET NULL
) ENGINE = InnoDB
  AUTO_INCREMENT = 139
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `san_pham` WRITE;
/*!40000 ALTER TABLE `san_pham`
    DISABLE KEYS */;
INSERT INTO `san_pham`
(`san_pham_id`, `nguoi_dung_id`, `ten_san_pham`, `danh_muc_id`, `gia`, `mo_ta`, `tinh_trang`, `trong_luong_gram`,
 `chieu_dai_cm`, `chieu_rong_cm`, `chieu_cao_cm`, `trang_thai`, `ngay_dang`, `updated_at`)
VALUES (1, 3, 'Iphone X', 1, 5000000.00, NULL, NULL, NULL, NULL, NULL, NULL, 'CON_HANG', '2026-02-26 00:04:24',
        '2026-02-25 17:04:24'),
       (2, 4, 'Laptop Dell', 2, 8000000.00, NULL, NULL, NULL, NULL, NULL, NULL, 'CON_HANG', '2026-02-26 00:04:24',
        '2026-02-25 17:04:24'),
       (3, 3, 'Ao khoac', 3, 300000.00, NULL, NULL, NULL, NULL, NULL, NULL, 'CON_HANG', '2026-02-26 00:04:24',
        '2026-02-25 17:04:24'),
       (4, 4, 'Giay Nike', 4, 700000.00, NULL, NULL, NULL, NULL, NULL, NULL, 'CON_HANG', '2026-02-26 00:04:24',
        '2026-02-25 17:04:24'),
       (5, 3, 'Dong ho Casio', 5, 400000.00, NULL, NULL, NULL, NULL, NULL, NULL, 'CON_HANG', '2026-02-26 00:04:24',
        '2026-02-25 17:04:24'),
       (6, 7, 'Ipad', 6, 6000000.00, NULL, NULL, NULL, NULL, NULL, NULL, 'CON_HANG', '2026-02-26 00:04:24',
        '2026-02-25 17:04:24'),
       (7, 7, 'Tai nghe', 7, 200000.00, NULL, NULL, NULL, NULL, NULL, NULL, 'CON_HANG', '2026-02-26 00:04:24',
        '2026-02-25 17:04:24'),
       (8, 3, 'Ban go', 8, 1500000.00, NULL, NULL, NULL, NULL, NULL, NULL, 'CON_HANG', '2026-02-26 00:04:24',
        '2026-02-25 17:04:24'),
       (9, 4, 'Sach Java', 9, 100000.00, NULL, NULL, NULL, NULL, NULL, NULL, 'CON_HANG', '2026-02-26 00:04:24',
        '2026-02-25 17:04:24'),
       (10, 7, 'Noi chien khong dau', 10, 900000.00, NULL, NULL, NULL, NULL, NULL, NULL, 'CON_HANG',
        '2026-02-26 00:04:24', '2026-02-25 17:04:24'),
       (11, 3, 'iPhone 7 Pro 64GB', 1, 6500000.00, 'Máy mất zin', '36%', 188, 14, 7, 1, 'DANG_BAN',
        '2026-02-28 15:44:28', '2026-03-30 06:40:13'),
       (12, 3, 'iPhone 11 Pro 64GB', 1, 6500000.00, 'Máy zin chưa sửa', '95%', 188, 14, 7, 1, 'DA_BAN',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (13, 4, 'iPhone 12 128GB', 1, 8000000.00, 'Pin 88%', '90%', 164, 15, 7, 1, 'DA_BAN', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (14, 7, 'iPhone 13 Pro Max', 1, 15000000.00, 'Bản VN/A', '99%', 240, 16, 8, 1, 'CON_HANG', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (15, 1, 'iPhone 14 Plus', 1, 16500000.00, 'Còn bảo hành', '99%', 203, 16, 8, 1, 'CON_HANG',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (16, 3, 'iPhone 15 Pro', 1, 23000000.00, 'Sạc 10 lần', 'Mới', 187, 15, 7, 1, 'CON_HANG', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (17, 4, 'Samsung S21 Ultra', 1, 8500000.00, 'Xước viền nhẹ', '90%', 227, 17, 8, 1, 'DANG_BAN',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (18, 7, 'Samsung S22 Plus', 1, 11000000.00, 'Máy đẹp, dán full', '95%', 195, 16, 8, 1, 'CON_HANG',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (19, 1, 'Samsung S23 Ultra', 1, 18500000.00, 'Kèm bút S-Pen', '99%', 234, 16, 8, 1, 'CON_HANG',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (20, 3, 'Samsung Z Fold 4', 1, 17000000.00, 'Cơ cáp chắc', '95%', 263, 16, 13, 1, 'DANG_BAN',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (21, 4, 'Samsung Z Flip 3', 1, 5500000.00, 'Gập mượt', '90%', 183, 17, 7, 1, 'CON_HANG', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (22, 7, 'Samsung Note 20', 1, 5000000.00, 'Bản Mỹ 2 sim', '90%', 192, 16, 8, 1, 'CON_HANG',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (23, 1, 'Xiaomi Redmi Note 12', 1, 3500000.00, 'Màn 120Hz', '99%', 188, 17, 8, 1, 'CON_HANG',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (24, 3, 'Xiaomi 13 Lite', 1, 6500000.00, 'Mỏng nhẹ', '95%', 171, 16, 7, 1, 'DANG_BAN', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (25, 4, 'Xiaomi Poco X4 Pro', 1, 4000000.00, 'Chiến game tốt', '90%', 205, 16, 8, 1, 'CON_HANG',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (26, 7, 'Xiaomi 12T Pro', 1, 8000000.00, 'Camera 200MP', '95%', 205, 16, 8, 1, 'CON_HANG', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (27, 1, 'Oppo Reno 7', 1, 4500000.00, 'Lưng da cam', '95%', 175, 16, 7, 1, 'DANG_BAN', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (28, 3, 'Oppo Reno 8T 5G', 1, 5500000.00, 'Màn cong', '99%', 171, 16, 7, 1, 'CON_HANG', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (29, 4, 'Oppo Find X5', 1, 8500000.00, 'Chụp đêm siêu đẹp', '95%', 196, 16, 7, 1, 'CON_HANG',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (30, 7, 'Realme 10 Pro', 1, 4800000.00, 'Viền siêu mỏng', '99%', 173, 16, 7, 1, 'DANG_BAN',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (31, 1, 'Realme GT Neo 3', 1, 5200000.00, 'Sạc 150W', '95%', 188, 16, 8, 1, 'CON_HANG', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (32, 3, 'Vivo V25', 1, 4000000.00, 'Đổi màu mặt lưng', '90%', 186, 16, 7, 1, 'CON_HANG', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (33, 4, 'Vivo X80', 1, 9000000.00, 'Camera Zeiss', '95%', 206, 16, 8, 1, 'DANG_BAN', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (34, 7, 'Google Pixel 6', 1, 4500000.00, 'Quốc tế', '90%', 207, 16, 7, 1, 'CON_HANG', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (35, 1, 'Google Pixel 7 Pro', 1, 9500000.00, 'Mượt mà', '95%', 212, 16, 8, 1, 'CON_HANG', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (36, 3, 'Sony Xperia 1 II', 1, 4000000.00, 'Màn 4K', '90%', 181, 17, 7, 1, 'DANG_BAN', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (37, 4, 'Sony Xperia 5 III', 1, 6000000.00, 'Nhỏ gọn dễ cầm', '95%', 168, 16, 7, 1, 'CON_HANG',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (38, 7, 'Asus ROG Phone 5', 1, 7500000.00, 'LED RGB lưng', '90%', 238, 17, 8, 1, 'CON_HANG',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (39, 1, 'Asus Zenfone 9', 1, 10000000.00, 'Flagship mini', '99%', 169, 15, 7, 1, 'DANG_BAN',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (40, 3, 'Nokia G21', 1, 1500000.00, 'Máy phụ pin trâu', '90%', 190, 16, 8, 1, 'CON_HANG', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (41, 4, 'OnePlus 10 Pro', 1, 8500000.00, 'Hasselblad', '95%', 201, 16, 7, 1, 'CON_HANG', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (42, 7, 'MacBook Air M1 256GB', 2, 13500000.00, 'Pin 95%', '99%', 1290, 30, 21, 2, 'CON_HANG',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (43, 1, 'MacBook Pro M1 16GB', 2, 18000000.00, 'Bao code mượt', '95%', 1400, 30, 21, 2, 'DANG_BAN',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (44, 3, 'MacBook Air M2', 2, 19000000.00, 'Màu Midnight', '99%', 1240, 30, 21, 1, 'CON_HANG',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (45, 4, 'MacBook Pro 2015', 2, 6000000.00, '15 inch Retina', '90%', 2040, 36, 25, 2, 'CON_HANG',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (46, 7, 'MacBook Pro 2019', 2, 11000000.00, 'Touchbar đầy đủ', '95%', 1370, 30, 21, 2, 'DANG_BAN',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (47, 1, 'Dell XPS 13 9370', 2, 8500000.00, 'Vỏ nhôm xước dăm', '90%', 1200, 30, 20, 1, 'CON_HANG',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (48, 3, 'Dell Latitude 7490', 2, 5000000.00, 'Bền bỉ', '95%', 1400, 33, 22, 2, 'CON_HANG', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (49, 4, 'Dell Precision 5530', 2, 11500000.00, 'Card rời vẽ 3D', '95%', 1800, 36, 24, 2, 'DANG_BAN',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (50, 7, 'Dell Inspiron 3511', 2, 7000000.00, 'RAM 8GB', '99%', 1800, 36, 25, 2, 'CON_HANG',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (51, 1, 'Dell Vostro 3400', 2, 6500000.00, 'Văn phòng ổn', '95%', 1600, 33, 24, 2, 'CON_HANG',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (52, 3, 'ThinkPad T480s', 2, 5500000.00, 'Phím gõ đỉnh', '95%', 1320, 33, 23, 2, 'DANG_BAN',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (53, 4, 'ThinkPad X1 Carbon', 2, 9000000.00, 'Gen 6 siêu nhẹ', '95%', 1130, 32, 22, 1, 'CON_HANG',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (54, 7, 'ThinkPad P52s', 2, 10500000.00, 'Máy trạm', '90%', 1950, 37, 25, 2, 'CON_HANG', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (55, 1, 'Lenovo Legion 5', 2, 14000000.00, 'RTX 3050Ti', '98%', 2400, 36, 26, 3, 'DANG_BAN',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (56, 3, 'Lenovo IdeaPad 3', 2, 5000000.00, 'Học online', '90%', 1600, 36, 25, 2, 'CON_HANG',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (57, 4, 'Asus ROG Strix G15', 2, 15000000.00, 'LED gầm sáng', '95%', 2300, 36, 26, 3, 'CON_HANG',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (58, 7, 'Asus TUF Gaming', 2, 12000000.00, 'Màn 144Hz', '95%', 2000, 36, 25, 2, 'DANG_BAN',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (59, 1, 'Asus Zenbook 14', 2, 11500000.00, 'Nhôm nguyên khối', '99%', 1170, 32, 21, 1, 'CON_HANG',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (60, 3, 'Asus Vivobook 15', 2, 6500000.00, 'Màn to rõ', '95%', 1700, 36, 24, 2, 'CON_HANG',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (61, 4, 'Asus ExpertBook', 2, 15000000.00, 'Doanh nhân', '99%', 880, 32, 20, 1, 'DANG_BAN',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (62, 7, 'HP Envy 13', 2, 10000000.00, 'Bản lề nâng', '95%', 1300, 31, 21, 1, 'CON_HANG', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (63, 1, 'HP Elitebook 840', 2, 6500000.00, 'G6 đẹp keng', '95%', 1480, 33, 23, 2, 'CON_HANG',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (64, 3, 'HP Pavilion 14', 2, 5500000.00, 'Loa B&O', '90%', 1450, 32, 22, 2, 'DANG_BAN', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (65, 4, 'HP Omen 15', 2, 13500000.00, 'Tản nhiệt cực mát', '95%', 2360, 36, 26, 2, 'CON_HANG',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (66, 7, 'Acer Nitro 5', 2, 11000000.00, 'Bao game nặng', '90%', 2500, 36, 27, 3, 'CON_HANG',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (67, 1, 'Acer Swift 3', 2, 8000000.00, 'Pin 6 tiếng', '95%', 1200, 32, 21, 2, 'DANG_BAN', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (68, 3, 'Acer Aspire 7', 2, 9000000.00, 'Laptop đồ họa', '95%', 2150, 36, 25, 2, 'CON_HANG',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (69, 4, 'MSI Modern 14', 2, 7500000.00, 'Xách nhẹ nhàng', '99%', 1300, 32, 22, 2, 'CON_HANG',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (70, 7, 'MSI GF63', 2, 9500000.00, 'GTX 1650', '95%', 1860, 36, 25, 2, 'DA_BAN', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (71, 1, 'Surface Laptop 3', 2, 8000000.00, 'Màn 2K', '95%', 1280, 31, 22, 1, 'CON_HANG', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (72, 3, 'Ao khoac gio chong nuoc', 3, 250000.00, 'Size L', '99%', 350, 30, 20, 5, 'DA_BAN',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (73, 4, 'Ao khoac jean nam', 3, 300000.00, 'Dày dặn, xanh nhạt', '95%', 600, 35, 25, 5, 'DA_BAN',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (74, 7, 'Ao bomber da PU', 3, 400000.00, 'Bo chun chắc chắn', '90%', 700, 40, 30, 10, 'DA_BAN',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (75, 1, 'Ao phao sieu nhe', 3, 350000.00, 'Mùa đông cực ấm', '99%', 400, 35, 25, 10, 'DA_BAN',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (76, 3, 'Ao hoodie form rong', 3, 200000.00, 'Nỉ bông dày', '95%', 500, 30, 25, 5, 'DA_BAN',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (77, 4, 'Ao thun Local Brand', 3, 150000.00, 'Cotton 100%', '99%', 200, 20, 15, 2, 'DA_BAN',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (78, 7, 'Ao polo co be', 3, 180000.00, 'Dáng ôm', '95%', 250, 25, 20, 2, 'DA_BAN', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (79, 1, 'Ao len co lo', 3, 150000.00, 'Lông cừu', '95%', 300, 30, 20, 5, 'DA_BAN', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (80, 3, 'Ao so mi trang', 3, 120000.00, 'Lụa trơn', '99%', 200, 25, 20, 2, 'DA_BAN', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (81, 4, 'Ao so mi caro', 3, 160000.00, 'Khoác ngoài cool ngầu', '90%', 350, 30, 25, 3, 'DA_BAN',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (82, 7, 'Ao vest cong so', 3, 500000.00, 'Đệm vai đứng form', '95%', 800, 45, 30, 5, 'DA_BAN',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (83, 1, 'Ao gile len', 3, 90000.00, 'Họa tiết thổ cẩm', '99%', 200, 20, 15, 3, 'DA_BAN', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (84, 3, 'Ao the thao da banh', 3, 80000.00, 'Thấm hút mồ hôi', 'Mới', 150, 20, 15, 2, 'DA_BAN',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (85, 4, 'Ao croptop nu', 3, 70000.00, 'Cổ vuông quyến rũ', '95%', 100, 15, 10, 2, 'DA_BAN',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (86, 7, 'Ao blazer nu', 3, 250000.00, 'Màu be Hàn Quốc', '99%', 450, 35, 25, 5, 'CON_HANG',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (87, 1, 'Quan jean ong suong', 3, 250000.00, 'Hack dáng', '95%', 600, 35, 20, 5, 'CON_HANG',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (88, 3, 'Quan kaki nam', 3, 180000.00, 'Mặc đi làm', '90%', 500, 30, 20, 5, 'DA_BAN', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (89, 4, 'Quan au xep ly', 3, 150000.00, 'Đã là phẳng', '99%', 400, 35, 20, 3, 'CON_HANG', '2026-02-28 15:44:28',
        '2026-02-28 08:44:28'),
       (90, 7, 'Quan short tui hop', 3, 120000.00, 'Phong cách bụi bặm', '95%', 250, 25, 15, 3, 'DA_BAN',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (91, 1, 'Quan jogger the thao', 3, 160000.00, 'Bo gấu năng động', '95%', 450, 30, 20, 5, 'DANG_BAN',
        '2026-02-28 15:44:28', '2026-02-28 08:44:28'),
       (92, 1, 'Đầm Thức Cho Người Đẹp', 3, 500000.00,
        'Tiêu đề: Đầm Ngủ Lụa Satin Phối Ren – Đánh Thức Vẻ Đẹp Kiêu Kỳ.\nMô tả: "Chất lụa satin loại 1 mềm mướt như mây, mang lại cảm giác mát lạnh tức thì ngay khi chạm vào da". Thiết kế 2 dây mảnh mai kết hợp cùng đường khoét lưng hở sâu giúp nàng khoe trọn đường cong mềm mại.\nĐiểm nổi bật:\nChất liệu lụa Latin cao cấp, không nhăn, không xù lông.\nPhối ren tinh tế tại phần ngực tạo điểm nhấn quyến rũ.\nTặng kèm quần chip sexy cùng tone',
        'Mới', 5, 36, 18, 5, 'DA_BAN', '2026-02-28 16:37:16', '2026-03-06 16:07:43'),
       (93, 1, 'Đồ Bơi Mùa Hè', 3, 500000.00, 'Đồ Bơi Cho Mùa Hè', 'Như mới', 5, 10, 10, 10, 'DA_BAN',
        '2026-02-28 17:43:40', '2026-03-06 16:07:43'),
       (94, 1, 'Hình Gái Xinh Cho Người Cô Đơn', 6, 100000.00, 'Ai cần thì id nhá', 'Tốt', 2, 5, 5, 5, 'DA_BAN',
        '2026-03-01 06:49:25', '2026-03-06 16:07:43'),
       (95, 19, 'BACIAMI - Set đồ lót nữ quần T hoặc Y ngẫu nhiên gợi cảm sexy kèm choker', 3, 200000.00,
        '/ ??????? - Hôn em đi /\n\nCái tên nói lên tất cả , sự ngọt ngào và quyến rũ là cảm hứng để BEI làm ra set đồ này . Sự quyến rũ không thể chối từ , sự cám dỗ không cần lời nói , mật ngọt để chàng không thể nào dứt ra được . Em bé hư hỏng được chàng cưng chiều \n\n? "." nhẹ cho BEI biết nếu bạn thích ạ , BEI sẽ gửi ngay thông tin chi tiết sản phẩm cho nàng nhé \n\nFreesize dưới 65kg\n\n__\n\nBEI - Lingerie, sleepwear & corset\n\nNội y nữ theo yêu cầu \n\n?  Based in SaiGon, Shipping Worldwide',
        'Như mới', 4, 10, 10, 10, 'DA_BAN', '2026-03-02 11:26:49', '2026-03-06 16:07:43'),
       (96, 18, 'Ảnh treo tường', 6, 10000.00, 'Ai cần Id mình nha ', 'Mới', 12, 12, 12, 12, 'DA_BAN',
        '2026-03-02 11:32:13', '2026-03-03 16:41:50'),
       (97, 19, 'Banner dán phòng', 7, 100000.00, 'Ảnh dán trang trí phòng ngủ ', 'Tốt', 2, 10, 10, 10, 'DANG_BAN',
        '2026-03-02 11:42:40', '2026-03-03 16:41:50'),
       (98, 18, 'Dép đi chơi với bạn gái đã đi qua 5 lần ', 2, 100000.00, 'Dép sục vân hồng ', 'Kém', 14, 10, 14, 14,
        'DANG_BAN', '2026-03-06 08:21:09', NULL),
       (99, 18, 'Dép lọ', 4, 190000.00, 'Màu Hồng sữa , size 30', 'Như mới', 11, 11, 11, 11, 'DANG_BAN',
        '2026-03-06 09:21:25', NULL),
       (100, 19, 'Áo In Hình Động Vật Dễ Thươngs', 3, 150000.00, 'In áo theo yêu cầu luôn nha ', 'Mới', 11, 11, 11, 111,
        'DANG_BAN', '2026-03-06 16:10:02', NULL),
       (101, 18, 'Đồng Hồ Casio', 5, 5000000.00, 'Đồng hồ trẻ trung - sang trọng .', 'Như mới', 14, 15, 14, 14,
        'DA_BAN', '2026-03-08 03:47:43', '2026-03-08 05:54:50'),
       (102, 19, 'Chapman đã dùng 10 điếu ', 10, 30000.00,
        'Đã dùng 10 điều còn 10 điếu nữa , do mình không còn nhu cầu sử dụng nên đăng lên bán lại nhá !', 'Tốt', 11, 11,
        11, 11, 'DA_BAN', '2026-03-08 03:51:55', NULL),
       (103, 19, 'Dép Nữ', 4, 190000.00, 'Dép dành cho nữ dễ thương\nMàu : Xanh Sữa\nSize : 42 \n', 'Tốt', 11, 111, 11,
        11, 'DANG_BAN', '2026-03-08 03:56:13', NULL),
       (104, 1, 'Dép lọ ', 4, 100000.00, '100k cho một đôi tùy chọn, nhanh thì còn nha ', 'Trung bình', 11, 11, 11, 11,
        'DANG_BAN', '2026-03-08 03:58:26', NULL),
       (105, 18, 'Mit BK', 7, 1999000.00, 'Gậy Lướt', 'Tốt', 11, 11, 11, 11, 'DA_BAN', '2026-03-09 15:25:14', NULL),
       (106, 18, 'aaaaaaaa', 5, 1111111.00, '111111111', 'Trung bình', 11, 11, 11, 11, 'DANG_BAN',
        '2026-03-09 16:08:01', NULL),
       (107, 18, 'File Nhạc TỊnh Tâm', 1, 100000.00, 'bảo hành 5 tháng', 'Tốt', 11, 11, 11, 11, 'DA_BAN',
        '2026-03-09 16:15:54', NULL),
       (108, 19, 'Bia', 10, 45000.00, 'Bia', 'Kém', 11, 11, 11, 11, 'DA_BAN', '2026-03-09 16:18:18', NULL),
       (109, 1, 'dao không chuôi', 10, 50000.00, 'Dao cắt trái cây', 'Kém', 11, 11, 11, 11, 'DA_BAN',
        '2026-03-09 16:20:10', NULL),
       (110, 18, 'Điện của mẹ mang đi bán ', 1, 500000.00, 'Mua đi mẹ tôi đã muốn tôi bán ', 'Kém', 1, 1, 1, 1,
        'AN_TIN', '2026-03-15 16:06:09', '2026-03-16 08:49:57'),
       (111, 15, 'Laptop HP 2009', 2, 2000000.00, 'Laptop lướt ', 'Tốt', 11, 11, 11, 11, 'DANG_BAN',
        '2026-03-15 16:51:34', NULL),
       (112, 15, 'Dàn áo xe yamaha', 44, 150000.00, 'DÀN ÁO XE MOTO', 'Như mới', 111, 123, 11, 11, 'DANG_BAN',
        '2026-03-15 16:57:27', NULL),
       (113, 19, 'Màn Hình Edra 100hz', 6, 1000000.00, 'màn Hình', 'Tốt', 11, 112, 11, 11, 'DANG_BAN',
        '2026-03-15 17:02:03', NULL),
       (114, 19, 'Xe Đạp ', 45, 500000.00, 'Xe đẹp nha ', 'Trung bình', 11, 11, 11, 11, 'DANG_BAN',
        '2026-03-15 17:02:39', NULL),
       (115, 19, 'Tủ Lạnh ', 10, 600000.00, 'Tủ lạnh mini', 'Tốt', 11, 11, 11, 11, 'DA_BAN', '2026-03-15 17:03:25',
        NULL),
       (116, 19, 'Xe Còn mới ', 45, 35000000.00, 'GDTT tại hoặc sdt 0987654321', 'Như mới', 11, 11, 11, 11, 'DANG_BAN',
        '2026-03-15 17:04:27', NULL),
       (117, 19, 'XE đồ chơi ', 7, 15000.00, '', 'Trung bình', 11, 11, 11, 11, 'DANG_BAN', '2026-03-15 17:05:10', NULL),
       (118, 19, 'Tai nghe không dây ', 13, 150000.00, 'Tai nghe ko dây', 'Trung bình', 11, 11, 11, 11, 'DANG_BAN',
        '2026-03-15 17:05:54', NULL),
       (119, 18, 'Tay cầm chơi game ', 13, 1200000.00, '', 'Tốt', 11, 11, 11, 11, 'DANG_BAN', '2026-03-15 17:06:38',
        NULL),
       (120, 18, 'PC', 13, 12000000.00, 'PC ', 'Trung bình', 11, 11, 11, 11, 'DANG_BAN', '2026-03-15 17:07:12', NULL),
       (121, 18, 'Bàn nâu', 10, 500000.00, 'Bàn ', 'Như mới', 11, 111, 111, 11, 'DANG_BAN', '2026-03-15 17:07:47',
        '2026-03-17 06:22:06'),
       (122, 18, 'Máy Ảnh Nhật', 6, 1880000.00, 'Máy ảnh ', 'Như mới', 11, 11, 11, 11, 'DANG_BAN',
        '2026-03-16 15:51:51', NULL),
       (123, 18, 'Iphone Xom Sum', 1, 36000000.00, 'Iphone 17 màu Rau #/^', 'Tốt', 19, 36, 18, 38, 'DANG_BAN',
        '2026-03-17 13:33:35', NULL),
       (124, 18, 'Máy hút bụi', 10, 120000.00, 'Máy hút bụi', 'Tốt', 52, 12, 123, 13, 'DANG_BAN', '2026-03-18 06:02:53',
        NULL),
       (125, 18, 'Laptop', 1, 1000000.00, 'Máy tính cũ ', 'Tốt', 11, 11, 11, 11, 'CHO_DUYET', '2026-03-19 14:05:04',
        NULL),
       (128, 15, 'ADX', 13, 400000.00, 'tay cầm chơi điện tử', 'Tốt', 3, 3, 7, 4, 'DA_BAN', '2026-03-19 14:11:26',
        '2026-03-19 07:11:58'),
       (129, 19, 'ip11', 43, 3000000.00, 'ip11', 'Kém', 3, 5, 4, 2, 'DA_BAN', '2026-03-23 15:28:06',
        '2026-03-23 08:28:24'),
       (130, 18, 'Oto', 45, 12000000.00, 'Oto', 'Tốt', 11, 11, 11, 11, 'DANG_BAN', '2026-03-24 15:06:34',
        '2026-03-24 08:06:50'),
       (131, 18, 'Oto', 45, 12000000.00, 'Oto', 'Tốt', 11, 11, 11, 11, 'DANG_BAN', '2026-03-24 15:06:36',
        '2026-03-30 06:33:41'),
       (132, 18, 'vỏ xe ', 44, 400000.00, 'Vỏ xe màu xanh ', 'Tốt', 2, 1, 2, 2, 'DANG_BAN', '2026-03-24 16:10:03',
        '2026-03-30 06:33:30'),
       (133, 18, 'vỏ xe ', 44, 400000.00, 'Vỏ xe màu xanh ', 'Tốt', 2, 1, 2, 2, 'BI_TU_CHOI', '2026-03-24 16:10:05',
        '2026-03-29 07:43:56'),
       (134, 18, 'Xem oto 2012', 45, 100000000.00, 'Mua id', 'Tốt', 11, 111, 11, 11, 'DANG_BAN', '2026-03-24 17:40:01',
        '2026-03-24 10:40:29'),
       (135, 18, 'Laptop cũ', 23, 5000000.00, 'Laptop cũ', 'Tốt', 11, 12, 11, 11, 'DANG_BAN', '2026-03-30 13:33:01',
        '2026-03-30 06:33:23'),
       (136, 18, 'Xe điện nội địa trung', 46, 1200000.00, 'Xe điện ', 'Như mới', 111, 12, 12, 12, 'DANG_BAN',
        '2026-03-30 13:35:05', '2026-03-30 06:40:07'),
       (137, 18, 'Đàn Piano', 48, 1000000.00, 'Đàn cũ ', 'Như mới', 111, 112, 12, 21, 'DANG_BAN', '2026-03-30 13:36:30',
        '2026-03-30 06:40:09'),
       (138, 18, 'Laptop Dell cũ', 22, 1000000.00, 'Laptopcux', 'Trung bình', 11, 11, 11, 11, 'DANG_BAN',
        '2026-03-30 13:39:50', '2026-03-30 06:40:11');
/*!40000 ALTER TABLE `san_pham`
    ENABLE KEYS */;
UNLOCK TABLES;

-- ====================================================
-- BƯỚC 3: GÁN ĐỊA CHỈ KHO VÀO SẢN PHẨM (CHẠY 1 LẦN LÀ XONG)
-- ====================================================
-- Tự động gán toàn bộ 138 sản phẩm vào kho hàng vừa tạo (id = 1)
UPDATE `san_pham`
SET `id_dia_chi_cua_hang` = 1
WHERE `id_dia_chi_cua_hang` IS NULL;

-- 4. Bật lại kiểm tra khóa ngoại
SET FOREIGN_KEY_CHECKS = 1;

--
-- Table structure for table `vai_tro`
--

DROP TABLE IF EXISTS `vai_tro`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vai_tro`
(
    `vai_tro_id`  int          NOT NULL AUTO_INCREMENT,
    `ten_vai_tro` varchar(100) NOT NULL,
    `created_at`  timestamp    NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  timestamp    NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`vai_tro_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vai_tro`
--

LOCK TABLES `vai_tro` WRITE;
/*!40000 ALTER TABLE `vai_tro`
    DISABLE KEYS */;
INSERT INTO `vai_tro`
VALUES (1, 'ADMIN', '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (2, 'USER', '2026-02-25 17:04:24', '2026-02-25 17:04:24');
/*!40000 ALTER TABLE `vai_tro`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `van_chuyen`
--

DROP TABLE IF EXISTS `van_chuyen`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `van_chuyen`
(
    `van_chuyen_id`  int       NOT NULL AUTO_INCREMENT,
    `don_hang_id`    int       NOT NULL,
    `ma_van_don_ghn` varchar(100)   DEFAULT NULL,
    `phi_ship`       decimal(38, 2) DEFAULT NULL,
    `trang_thai_ghn` varchar(100)   DEFAULT NULL,
    `ngay_du_kien`   date           DEFAULT NULL,
    `created_at`     timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`     timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`van_chuyen_id`),
    KEY `don_hang_id` (`don_hang_id`),
    CONSTRAINT `van_chuyen_ibfk_1` FOREIGN KEY (`don_hang_id`) REFERENCES `don_hang` (`don_hang_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `van_chuyen`
--

LOCK TABLES `van_chuyen` WRITE;
/*!40000 ALTER TABLE `van_chuyen`
    DISABLE KEYS */;
INSERT INTO `van_chuyen`
VALUES (1, 1, 'GHN001', 30000.00, 'DANG_GIAO', '2026-03-01', '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (2, 2, 'GHN002', 30000.00, 'DANG_GIAO', '2026-03-01', '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (3, 3, 'GHN003', 20000.00, 'DA_GIAO', '2026-03-01', '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (4, 4, 'GHN004', 20000.00, 'DA_GIAO', '2026-03-01', '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (5, 5, 'GHN005', 20000.00, 'DA_GIAO', '2026-03-01', '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (6, 6, 'GHN006', 30000.00, 'DANG_GIAO', '2026-03-01', '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (7, 7, 'GHN007', 20000.00, 'DANG_GIAO', '2026-03-01', '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (8, 8, 'GHN008', 30000.00, 'DANG_GIAO', '2026-03-01', '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (9, 9, 'GHN009', 20000.00, 'DA_GIAO', '2026-03-01', '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (10, 10, 'GHN010', 20000.00, 'DANG_GIAO', '2026-03-01', '2026-02-25 17:04:24', '2026-02-25 17:04:24');
/*!40000 ALTER TABLE `van_chuyen`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vi_tien`
--

DROP TABLE IF EXISTS `vi_tien`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vi_tien`
(
    `vi_id`         int       NOT NULL AUTO_INCREMENT,
    `nguoi_dung_id` int       NOT NULL,
    `ngay_cap_nhat` datetime       DEFAULT NULL,
    `so_du`         decimal(38, 2) DEFAULT NULL,
    `created_at`    timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`    timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`vi_id`),
    KEY `nguoi_dung_id` (`nguoi_dung_id`),
    CONSTRAINT `vi_tien_ibfk_1` FOREIGN KEY (`nguoi_dung_id`) REFERENCES `nguoi_dung` (`nguoi_dung_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 13
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vi_tien`
--

LOCK TABLES `vi_tien` WRITE;
/*!40000 ALTER TABLE `vi_tien`
    DISABLE KEYS */;
INSERT INTO `vi_tien`
VALUES (1, 1, '2026-02-26 00:04:24', 1000000.00, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (2, 2, '2026-02-26 00:04:24', 2000000.00, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (3, 3, '2026-02-26 00:04:24', 3000000.00, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (4, 4, '2026-02-26 00:04:24', 4000000.00, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (5, 5, '2026-02-26 00:04:24', 5000000.00, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (6, 6, '2026-02-26 00:04:24', 6000000.00, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (7, 7, '2026-02-26 00:04:24', 7000000.00, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (8, 8, '2026-02-26 00:04:24', 8000000.00, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (9, 9, '2026-02-26 00:04:24', 9000000.00, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (10, 10, '2026-02-26 00:04:24', 10000000.00, '2026-02-25 17:04:24', '2026-02-25 17:04:24'),
       (11, 18, '2026-03-30 12:55:17', 2144000.00, NULL, NULL),
       (12, 19, '2026-03-30 12:34:45', 3075000.00, NULL, NULL);
/*!40000 ALTER TABLE `vi_tien`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `yeu_cau_tra_hang`
--

DROP TABLE IF EXISTS `yeu_cau_tra_hang`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `yeu_cau_tra_hang`
(
    `id`                  int NOT NULL AUTO_INCREMENT,
    `hinh_anh_bang_chung` varchar(1000) DEFAULT NULL,
    `ly_do`               varchar(255)  DEFAULT NULL,
    `mo_ta_chi_tiet`      varchar(1000) DEFAULT NULL,
    `ngay_xu_ly`          datetime(6)   DEFAULT NULL,
    `ngay_yeu_cau`        datetime(6)   DEFAULT NULL,
    `trang_thai`          varchar(255)  DEFAULT NULL,
    `video_bang_chung`    varchar(1000) DEFAULT NULL,
    `don_hang_id`         int           DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UKfoyliv5xuev3m4joeesiswapr` (`don_hang_id`),
    CONSTRAINT `FKpwkqgjit9mh9klxiurrhi0tuk` FOREIGN KEY (`don_hang_id`) REFERENCES `don_hang` (`don_hang_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `yeu_cau_tra_hang`
--

LOCK TABLES `yeu_cau_tra_hang` WRITE;
/*!40000 ALTER TABLE `yeu_cau_tra_hang`
    DISABLE KEYS */;
INSERT INTO `yeu_cau_tra_hang`
VALUES (3, 'https://res.cloudinary.com/dep3rejur/image/upload/v1774866211/so03mo1fzv6hwptv0vyn.jpg',
        'Hàng khác với mô tả', 'ADV', '2026-03-30 13:01:19.857955', '2026-03-30 10:24:00.533560', 'DA_DUYET',
        'https://res.cloudinary.com/dep3rejur/video/upload/v1774866218/ajmchzq635ke8cwpvxuz.mov', 57),
       (4, 'https://res.cloudinary.com/dep3rejur/image/upload/v1774874387/gpzx8k8c6dr2j1i9papd.jpg',
        'Hàng khác với mô tả', 'ABCD không giống mô tả', '2026-03-30 12:40:27.484661', '2026-03-30 12:40:06.716562',
        'HOAN_THANH', 'https://res.cloudinary.com/dep3rejur/video/upload/v1774874398/zlnpnpywilqbxt9lerpb.mov', 59);
/*!40000 ALTER TABLE `yeu_cau_tra_hang`
    ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `cua_hang`
--

DROP TABLE IF EXISTS `cua_hang`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cua_hang`
(
    `id`                  int          NOT NULL AUTO_INCREMENT,
    `id_nguoi_dung`       int          NOT NULL,
    `ten_shop`            varchar(255) NOT NULL,
    `anh_dai_dien`        varchar(500) DEFAULT NULL,
    `gioi_thieu`          text,
    `trang_thai_cua_hang` tinyint      DEFAULT '1',
    `mo_ta_vi_pham`       text,
    `ngay_tao`            datetime     DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uc_cuahang_id_nguoi_dung` (`id_nguoi_dung`),
    CONSTRAINT `fk_cuahang_id_nguoi_dung` FOREIGN KEY (`id_nguoi_dung`) REFERENCES `nguoi_dung` (`nguoi_dung_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cua_hang`
--

LOCK TABLES `cua_hang` WRITE;
/*!40000 ALTER TABLE `cua_hang`
    DISABLE KEYS */;
-- Dọn dẹp sạch sẽ bảng trước khi thêm dữ liệu mới (để tránh lỗi trùng lặp nếu em lỡ chạy 2 lần)
TRUNCATE TABLE cua_hang;

-- Thêm dữ liệu mẫu vào bảng cua_hang
INSERT INTO `cua_hang`
(`id_nguoi_dung`, `ten_shop`, `anh_dai_dien`, `gioi_thieu`, `trang_thai_cua_hang`, `ngay_tao`)
VALUES (1, 'User 1 Shop', 'https://ui-avatars.com/api/?name=User+1+Shop&background=random',
        'Cửa hàng chuyên bán đồ gia dụng cũ của User 1.', 1, NOW()),
       (2, 'User 2 Shop', 'https://ui-avatars.com/api/?name=User+2+Shop&background=random',
        'Góc thanh lý đồ thời trang nam nữ giá rẻ.', 1, NOW()),
       (18, 'Tuấn Duy Store', 'https://ui-avatars.com/api/?name=Tuan+Duy+Store&background=random',
        'Chuyên thanh lý phụ kiện công nghệ, điện thoại lướt.', 1, NOW()),
       (19, 'Duy FPT Shop', 'https://ui-avatars.com/api/?name=Duy+FPT+Shop&background=random',
        'Shop mới tạo, chưa cập nhật địa chỉ kho hàng.', 1, NOW());
/*!40000 ALTER TABLE `cua_hang`
    ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `dia_chi_cua_hang`
--

-- 1. Tạm tắt kiểm tra khóa ngoại để an toàn xóa bảng
SET FOREIGN_KEY_CHECKS = 0;

-- ====================================================
-- BẢNG 1: ĐỊA CHỈ CỬA HÀNG
-- ====================================================
DROP TABLE IF EXISTS `dia_chi_cua_hang`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dia_chi_cua_hang`
(
    `id`               int          NOT NULL AUTO_INCREMENT,
    `id_cua_hang`      int          NOT NULL,
    `huyen_code`       int          NOT NULL,
    `phuong_xa_id`     varchar(50)  NOT NULL,
    `dia_chi_chi_tiet` varchar(500) NOT NULL,
    `is_default`       tinyint  DEFAULT '0' COMMENT '1: Là địa chỉ lấy hàng mặc định, 0: Địa chỉ phụ',
    `ngay_tao`         datetime DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `fk_dc_cuahang_id` (`id_cua_hang`),
    CONSTRAINT `fk_dc_cuahang_id` FOREIGN KEY (`id_cua_hang`) REFERENCES `cua_hang` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `dia_chi_cua_hang` WRITE;
/*!40000 ALTER TABLE `dia_chi_cua_hang`
    DISABLE KEYS */;
-- Gán 1 kho hàng mẫu tại Xã Nật Sơn, Kim Bôi, Hòa Bình cho cửa hàng số 3 (Tuấn Duy Store)
INSERT INTO `dia_chi_cua_hang` (`id`, `id_cua_hang`, `huyen_code`, `phuong_xa_id`, `dia_chi_chi_tiet`, `is_default`,
                                `ngay_tao`)
VALUES (1, 3, 1496, '12345', 'Xã Nật Sơn, Huyện Kim Bôi, Tỉnh Hòa Bình', 1, NOW());
/*!40000 ALTER TABLE `dia_chi_cua_hang`
    ENABLE KEYS */;
UNLOCK TABLES;

/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;


-- Dump completed on 2026-03-30 23:32:19
