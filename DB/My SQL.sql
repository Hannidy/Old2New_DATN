
-- CREATE DATABASE old2new_db;
-- USE old2new_db;

-- ========================
-- 1. Vai trò
-- ========================
CREATE TABLE vai_tro (
    vai_tro_id INT AUTO_INCREMENT PRIMARY KEY,
    ten_vai_tro VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ========================
-- 2. Địa lý
-- ========================
CREATE TABLE tinh_thanh (
    tinh_thanh_id INT AUTO_INCREMENT PRIMARY KEY,
    ten_tinh VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE quan_huyen (
    quan_huyen_id INT AUTO_INCREMENT PRIMARY KEY,
    tinh_thanh_id INT NOT NULL,
    ten_quan VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (tinh_thanh_id) REFERENCES tinh_thanh(tinh_thanh_id)
);

CREATE TABLE phuong_xa (
    phuong_xa_id INT AUTO_INCREMENT PRIMARY KEY,
    quan_huyen_id INT NOT NULL,
    ten_phuong VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (quan_huyen_id) REFERENCES quan_huyen(quan_huyen_id)
);

-- ========================
-- 3. Người dùng
-- ========================
CREATE TABLE nguoi_dung (
    nguoi_dung_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(150) UNIQUE,
    mat_khau_ma_hoa VARCHAR(255),
    anh_dai_dien VARCHAR(255),
    ho_va_ten VARCHAR(150),
    gioi_tinh VARCHAR(20),
    ngay_sinh DATE,
    so_dien_thoai VARCHAR(20),
    vai_tro_id INT,
    trang_thai_nguoi_mua VARCHAR(50),
    mo_ta_vi_pham TEXT,
    ngay_tao DATETIME DEFAULT CURRENT_TIMESTAMP, -- Giữ nguyên theo code cũ
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Thêm mới
    FOREIGN KEY (vai_tro_id) REFERENCES vai_tro(vai_tro_id)
);

CREATE TABLE dia_chi (
    dia_chi_id INT AUTO_INCREMENT PRIMARY KEY,
    nguoi_dung_id INT NOT NULL,
    phuong_xa_id INT NOT NULL,
    dia_chi_chi_tiet VARCHAR(255),
    dia_chi_mac_dinh BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (nguoi_dung_id) REFERENCES nguoi_dung(nguoi_dung_id),
    FOREIGN KEY (phuong_xa_id) REFERENCES phuong_xa(phuong_xa_id)
);

-- ========================
-- 4. Danh mục & Sản phẩm
-- ========================
CREATE TABLE danh_muc (
    danh_muc_id INT AUTO_INCREMENT PRIMARY KEY,
    ten_danh_muc VARCHAR(150) NOT NULL,
    cha_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cha_id) REFERENCES danh_muc(danh_muc_id)
);

CREATE TABLE san_pham (
    san_pham_id INT AUTO_INCREMENT PRIMARY KEY,
    nguoi_dung_id INT NOT NULL,
    ten_san_pham VARCHAR(255) NOT NULL,
    danh_muc_id INT,
    gia DECIMAL(15,0) NOT NULL,
    mo_ta TEXT,
    tinh_trang VARCHAR(100),
    trong_luong_gram INT,
    chieu_dai_cm INT,
    chieu_rong_cm INT,
    chieu_cao_cm INT,
    trang_thai VARCHAR(50),
    ngay_dang DATETIME DEFAULT CURRENT_TIMESTAMP, -- Giữ nguyên theo code cũ
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Thêm mới
    FOREIGN KEY (nguoi_dung_id) REFERENCES nguoi_dung(nguoi_dung_id),
    FOREIGN KEY (danh_muc_id) REFERENCES danh_muc(danh_muc_id)
);

CREATE TABLE hinh_anh_san_pham (
    hinh_anh_id INT AUTO_INCREMENT PRIMARY KEY,
    san_pham_id INT NOT NULL,
    duong_dan_anh VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (san_pham_id) REFERENCES san_pham(san_pham_id) ON DELETE CASCADE
);

-- ========================
-- 5. Đơn hàng
-- ========================
CREATE TABLE don_hang (
    don_hang_id INT AUTO_INCREMENT PRIMARY KEY,
    nguoi_dung_id INT NOT NULL,
    dia_chi_id INT NOT NULL,
    tong_tien_hang DECIMAL(15,0),
    tong_tien_ship DECIMAL(15,0),
    tong_thanh_tien DECIMAL(15,0),
    phuong_phuc_thanh_toan VARCHAR(50),
    trang_thai_thanh_toan VARCHAR(50),
    trang_thai_don_hang VARCHAR(50),
    ngay_tao DATETIME DEFAULT CURRENT_TIMESTAMP, -- Giữ nguyên theo code cũ
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Thêm mới
    FOREIGN KEY (nguoi_dung_id) REFERENCES nguoi_dung(nguoi_dung_id),
    FOREIGN KEY (dia_chi_id) REFERENCES dia_chi(dia_chi_id)
);

CREATE TABLE chi_tiet_don_hang (
    chi_tiet_id INT AUTO_INCREMENT PRIMARY KEY,
    don_hang_id INT NOT NULL,
    san_pham_id INT NOT NULL,
    so_luong_mua INT,
    gia_luc_mua DECIMAL(15,0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (don_hang_id) REFERENCES don_hang(don_hang_id),
    FOREIGN KEY (san_pham_id) REFERENCES san_pham(san_pham_id)
);

CREATE TABLE van_chuyen (
    van_chuyen_id INT AUTO_INCREMENT PRIMARY KEY,
    don_hang_id INT NOT NULL,
    ma_van_don_ghn VARCHAR(100),
    phi_ship DECIMAL(15,0),
    trang_thai_ghn VARCHAR(100),
    ngay_du_kien DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (don_hang_id) REFERENCES don_hang(don_hang_id)
);

-- ========================
-- 6. Ví tiền
-- ========================
CREATE TABLE vi_tien (
    vi_id INT AUTO_INCREMENT PRIMARY KEY,
    nguoi_dung_id INT NOT NULL,
    ngay_cap_nhat DATETIME, -- Cột cũ bạn đã có
    so_du DECIMAL(15,0) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Thêm mới
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Thêm mới (thay thế logic tự động cho ngay_cap_nhat)
    FOREIGN KEY (nguoi_dung_id) REFERENCES nguoi_dung(nguoi_dung_id)
);

CREATE TABLE lich_su_giao_dich (
    giao_dich_id INT AUTO_INCREMENT PRIMARY KEY,
    vi_id INT NOT NULL,
    so_tien DECIMAL(15,0),
    loai_giao_dich VARCHAR(100),
    noi_dung TEXT,
    log_id VARCHAR(255),
    ngay_tao DATETIME DEFAULT CURRENT_TIMESTAMP, -- Giữ nguyên cột cũ
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Thêm mới
    FOREIGN KEY (vi_id) REFERENCES vi_tien(vi_id)
);

CREATE TABLE nhat_ky_thanh_toan (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    don_hang_id INT,
    ma_gd_vnpay VARCHAR(100),
    noi_dung_json TEXT,
    ket_qua VARCHAR(100),
    ngay_tao DATETIME DEFAULT CURRENT_TIMESTAMP, -- Giữ nguyên cột cũ
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Thêm mới
    FOREIGN KEY (don_hang_id) REFERENCES don_hang(don_hang_id)
);

-- ========================
-- 7. Đánh giá
-- ========================
CREATE TABLE danh_gia (
    danh_gia_id INT AUTO_INCREMENT PRIMARY KEY,
    san_pham_id INT NOT NULL,
    nguoi_mua_id INT NOT NULL,
    so_sao INT CHECK (so_sao BETWEEN 1 AND 5),
    binh_luan TEXT,
    tong_danh_gia INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (san_pham_id) REFERENCES san_pham(san_pham_id),
    FOREIGN KEY (nguoi_mua_id) REFERENCES nguoi_dung(nguoi_dung_id)
);


-- ========================
-- 1. vai_tro
-- ========================
INSERT INTO vai_tro (vai_tro_id, ten_vai_tro) VALUES
(1,'ADMIN'), (2,'USER'), (3,'SELLER'), (4,'MODERATOR'), (5,'SHIPPER'),
(6,'ACCOUNTANT'), (7,'SUPPORT'), (8,'MANAGER'), (9,'EDITOR'), (10,'GUEST');

-- ========================
-- 2. tinh_thanh
-- ========================
INSERT INTO tinh_thanh (tinh_thanh_id, ten_tinh) VALUES
(1,'HCM'), (2,'Ha Noi'), (3,'Da Nang'), (4,'Can Tho'), (5,'Binh Duong'),
(6,'Dong Nai'), (7,'Hue'), (8,'Hai Phong'), (9,'Quang Ninh'), (10,'Nha Trang');

-- ========================
-- 3. quan_huyen
-- ========================
INSERT INTO quan_huyen (quan_huyen_id, tinh_thanh_id, ten_quan) VALUES
(1,1,'Quan 1'), (2,2,'Ba Dinh'), (3,3,'Hai Chau'), (4,4,'Ninh Kieu'), (5,5,'Thu Dau Mot'),
(6,6,'Bien Hoa'), (7,7,'Huong Thuy'), (8,8,'Le Chan'), (9,9,'Ha Long'), (10,10,'Nha Trang Center');

-- ========================
-- 4. phuong_xa
-- ========================
INSERT INTO phuong_xa (phuong_xa_id, quan_huyen_id, ten_phuong) VALUES
(1,1,'Ben Nghe'), (2,2,'Dien Bien'), (3,3,'Thanh Binh'), (4,4,'An Cu'), (5,5,'Phu Cuong'),
(6,6,'Tan Bien'), (7,7,'Phu Bai'), (8,8,'An Bien'), (9,9,'Bai Chay'), (10,10,'Loc Tho');

-- ========================
-- 5. nguoi_dung
-- ========================
INSERT INTO nguoi_dung (nguoi_dung_id, email, mat_khau_ma_hoa, ho_va_ten, so_dien_thoai, vai_tro_id, ngay_tao) VALUES
(1,'user1@gmail.com','123','User 1','0900000001',2,NOW()),
(2,'user2@gmail.com','123','User 2','0900000002',2,NOW()),
(3,'user3@gmail.com','123','User 3','0900000003',3,NOW()),
(4,'user4@gmail.com','123','User 4','0900000004',3,NOW()),
(5,'user5@gmail.com','123','User 5','0900000005',2,NOW()),
(6,'user6@gmail.com','123','User 6','0900000006',2,NOW()),
(7,'user7@gmail.com','123','User 7','0900000007',3,NOW()),
(8,'user8@gmail.com','123','User 8','0900000008',2,NOW()),
(9,'user9@gmail.com','123','User 9','0900000009',2,NOW()),
(10,'admin@gmail.com','123','Admin','0999999999',1,NOW());

-- ========================
-- 6. dia_chi
-- ========================
INSERT INTO dia_chi (dia_chi_id, nguoi_dung_id, phuong_xa_id, dia_chi_chi_tiet, dia_chi_mac_dinh) VALUES
(1,1,1,'123 Street',1), (2,2,2,'124 Street',1), (3,3,3,'125 Street',1), (4,4,4,'126 Street',1), (5,5,5,'127 Street',1),
(6,6,6,'128 Street',1), (7,7,7,'129 Street',1), (8,8,8,'130 Street',1), (9,9,9,'131 Street',1), (10,10,10,'132 Street',1);

-- ========================
-- 7. danh_muc
-- ========================
INSERT INTO danh_muc (danh_muc_id, ten_danh_muc, cha_id) VALUES
(1,'Dien thoai',NULL), (2,'Laptop',NULL), (3,'Quan ao',NULL), (4,'Giay dep',NULL), (5,'Dong ho',NULL),
(6,'May tinh bang',NULL), (7,'Phu kien',NULL), (8,'Noi that',NULL), (9,'Sach',NULL), (10,'Do gia dung',NULL);

-- ========================
-- 8. san_pham
-- ========================
INSERT INTO san_pham (san_pham_id, nguoi_dung_id, ten_san_pham, danh_muc_id, gia, trang_thai, ngay_dang) VALUES
(1,3,'Iphone X',1,5000000,'CON_HANG',NOW()),
(2,4,'Laptop Dell',2,8000000,'CON_HANG',NOW()),
(3,3,'Ao khoac',3,300000,'CON_HANG',NOW()),
(4,4,'Giay Nike',4,700000,'CON_HANG',NOW()),
(5,3,'Dong ho Casio',5,400000,'CON_HANG',NOW()),
(6,7,'Ipad',6,6000000,'CON_HANG',NOW()),
(7,7,'Tai nghe',7,200000,'CON_HANG',NOW()),
(8,3,'Ban go',8,1500000,'CON_HANG',NOW()),
(9,4,'Sach Java',9,100000,'CON_HANG',NOW()),
(10,7,'Noi chien khong dau',10,900000,'CON_HANG',NOW());

-- ========================
-- 9. hinh_anh_san_pham
-- ========================
INSERT INTO hinh_anh_san_pham (hinh_anh_id, san_pham_id, duong_dan_anh) VALUES
(1,1,'uploads/1.jpg'), (2,2,'uploads/2.jpg'), (3,3,'uploads/3.jpg'), (4,4,'uploads/4.jpg'), (5,5,'uploads/5.jpg'),
(6,6,'uploads/6.jpg'), (7,7,'uploads/7.jpg'), (8,8,'uploads/8.jpg'), (9,9,'uploads/9.jpg'), (10,10,'uploads/10.jpg');

-- ========================
-- 10. don_hang
-- ========================
INSERT INTO don_hang (don_hang_id, nguoi_dung_id, dia_chi_id, tong_tien_hang, tong_tien_ship, tong_thanh_tien, ngay_tao) VALUES
(1,1,1,5000000,30000,5030000,NOW()), (2,2,2,8000000,30000,8030000,NOW()), (3,1,1,300000,20000,320000,NOW()),
(4,2,2,700000,20000,720000,NOW()), (5,5,5,400000,20000,420000,NOW()), (6,6,6,6000000,30000,6030000,NOW()),
(7,8,8,200000,20000,220000,NOW()), (8,9,9,1500000,30000,1530000,NOW()), (9,1,1,100000,20000,120000,NOW()), (10,2,2,900000,20000,920000,NOW());

-- ========================
-- 11. chi_tiet_don_hang
-- ========================
INSERT INTO chi_tiet_don_hang (chi_tiet_id, don_hang_id, san_pham_id, so_luong_mua, gia_luc_mua) VALUES
(1,1,1,1,5000000), (2,2,2,1,8000000), (3,3,3,1,300000), (4,4,4,1,700000), (5,5,5,1,400000),
(6,6,6,1,6000000), (7,7,7,1,200000), (8,8,8,1,1500000), (9,9,9,1,100000), (10,10,10,1,900000);

-- ========================
-- 12. van_chuyen
-- ========================
INSERT INTO van_chuyen (van_chuyen_id, don_hang_id, ma_van_don_ghn, phi_ship, trang_thai_ghn, ngay_du_kien) VALUES
(1,1,'GHN001',30000,'DANG_GIAO','2026-03-01'), (2,2,'GHN002',30000,'DANG_GIAO','2026-03-01'), (3,3,'GHN003',20000,'DA_GIAO','2026-03-01'),
(4,4,'GHN004',20000,'DA_GIAO','2026-03-01'), (5,5,'GHN005',20000,'DA_GIAO','2026-03-01'), (6,6,'GHN006',30000,'DANG_GIAO','2026-03-01'),
(7,7,'GHN007',20000,'DANG_GIAO','2026-03-01'), (8,8,'GHN008',30000,'DANG_GIAO','2026-03-01'), (9,9,'GHN009',20000,'DA_GIAO','2026-03-01'), (10,10,'GHN010',20000,'DANG_GIAO','2026-03-01');

-- ========================
-- 13. vi_tien
-- ========================
INSERT INTO vi_tien (vi_id, nguoi_dung_id, ngay_cap_nhat, so_du) VALUES
(1,1,NOW(),1000000), (2,2,NOW(),2000000), (3,3,NOW(),3000000), (4,4,NOW(),4000000), (5,5,NOW(),5000000),
(6,6,NOW(),6000000), (7,7,NOW(),7000000), (8,8,NOW(),8000000), (9,9,NOW(),9000000), (10,10,NOW(),10000000);

-- ========================
-- 14. lich_su_giao_dich
-- ========================
INSERT INTO lich_su_giao_dich (giao_dich_id, vi_id, so_tien, loai_giao_dich, ngay_tao) VALUES
(1,1,500000,'NAP',NOW()), (2,2,100000,'NAP',NOW()), (3,3,200000,'NAP',NOW()), (4,4,300000,'NAP',NOW()), (5,5,400000,'NAP',NOW()),
(6,6,500000,'NAP',NOW()), (7,7,600000,'NAP',NOW()), (8,8,700000,'NAP',NOW()), (9,9,800000,'NAP',NOW()), (10,10,900000,'NAP',NOW());

-- ========================
-- 15. nhat_ky_thanh_toan
-- ========================
INSERT INTO nhat_ky_thanh_toan (log_id, don_hang_id, ma_gd_vnpay, ket_qua, ngay_tao) VALUES
(1,1,'VNP001','SUCCESS',NOW()), (2,2,'VNP002','SUCCESS',NOW()), (3,3,'VNP003','SUCCESS',NOW()), (4,4,'VNP004','SUCCESS',NOW()), (5,5,'VNP005','SUCCESS',NOW()),
(6,6,'VNP006','SUCCESS',NOW()), (7,7,'VNP007','SUCCESS',NOW()), (8,8,'VNP008','SUCCESS',NOW()), (9,9,'VNP009','SUCCESS',NOW()), (10,10,'VNP010','SUCCESS',NOW());

-- ========================
-- 16. danh_gia
-- ========================
INSERT INTO danh_gia (danh_gia_id, san_pham_id, nguoi_mua_id, so_sao, binh_luan, tong_danh_gia) VALUES
(1,1,1,5,'Tot',1), (2,2,2,4,'On',1), (3,3,1,5,'Rat tot',1), (4,4,2,3,'Tam duoc',1), (5,5,5,4,'Dep',1),
(6,6,6,5,'Ngon',1), (7,7,8,4,'OK',1), (8,8,9,5,'Xuat sac',1), (9,9,1,5,'Hay',1), (10,10,2,4,'Dung duoc',1);




