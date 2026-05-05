<template>
  <!-- Ép nhỏ padding top/bottom của toàn bộ thẻ footer -->
  <footer class="footer border-top pt-3 pb-1 bg-white">
    <div class="container" style="max-width: 1000px">
      <!-- ========================================== -->
      <!-- DÒNG 1: LOGO VÀ GIỚI THIỆU -->
      <!-- ========================================== -->
      <div class="row mb-2 text-center">
        <div class="col-12">
          <!-- Đã xóa logo tái chế ở đây -->
          <div class="d-flex align-items-center justify-content-center mb-1">
            <span class="fs-4 fw-bold text-uppercase"
              >OLD<span class="text-danger">2</span>NEW</span
            >
          </div>
          <p class="text-muted small mx-auto mb-0" style="line-height: 1.5">
            OLD2NEW - Nền tảng mua và bán đồ cũ uy tín và có đảm bảo tại Việt
            Nam. Chạm cũ, mở mới vòng đời sản phẩm.
          </p>
        </div>
      </div>

      <!-- ========================================== -->
      <!-- DÒNG 2: 2 CỘT THÔNG TIN Ở DƯỚI -->
      <!-- ========================================== -->
      <!-- Giảm khoảng cách row (g-3) -->
      <div class="row g-3 justify-content-between mt-2">
        <!-- CỘT 1 (Bên trái): Thông tin Liên hệ -->
        <div class="col-md-7 mb-1 mb-md-0">
          <!-- Đổi sang text-dark và giảm margin bottom (mb-2) -->
          <h6 class="fw-bold mb-2 text-dark text-uppercase">
            Liên hệ với chúng tôi
          </h6>
          <div class="contact-info small text-muted">
            <!-- Ép sát các dòng bằng mb-1 thay vì mb-2 -->
            <p class="mb-1 d-flex align-items-center">
              <i class="bi bi-telephone text-danger fs-5 me-2"></i>
              <span class="text-dark">0396699538</span>
            </p>
            <p class="mb-1 d-flex align-items-center">
              <i class="bi bi-envelope text-danger fs-5 me-2"></i>
              <span class="text-dark">trantrongn14@gmail.com</span>
            </p>
            <p class="mb-0 d-flex align-items-start">
              <i class="bi bi-geo-alt text-danger fs-5 me-2 mt-n1"></i>
              <span class="text-dark" style="line-height: 1.5">
                Số nhà B2.25 Vinhomes Gardenia, Hàm Nghi, P. Cầu Diễn, Q. Nam Từ
                Liêm, TP. Hà Nội
              </span>
            </p>
          </div>
        </div>

        <!-- CỘT 2 (Bên phải): Thanh toán & Vận chuyển -->
        <div class="col-md-5 text-md-end">
          <!-- Đổi sang text-dark và giảm margin bottom (mb-2) -->
          <h6 class="fw-bold mb-2 text-dark text-uppercase">
            Thanh toán & Vận chuyển
          </h6>
          <div class="d-flex gap-2 flex-wrap justify-content-md-end">
            <span class="payment-icon outline-blue">
              Thanh toán trực tuyến
            </span>
            <span class="payment-icon outline-yellow"> COD </span>
            <span class="payment-icon outline-green"> GHN </span>
          </div>
        </div>
      </div>

      <!-- ========================================== -->
      <!-- DÒNG 3: BẢN QUYỀN (Copyright) -->
      <!-- ========================================== -->
      <!-- Rút gọn khoảng cách với phần trên (mt-3 pt-2) -->
      <div class="text-center mt-3 pt-2 border-top">
        <p class="text-muted small mb-0">
          &copy; 2026 <span class="text-danger fw-bold">OLD2NEW</span>. Nền tảng
          mua bán đồ cũ C2C.
        </p>
      </div>
    </div>
  </footer>
</template>

<style scoped>
.footer {
  font-family: "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
}

h6 {
  font-size: 0.85rem;
  letter-spacing: 0.5px;
}

.payment-icon {
  padding: 6px 12px; /* Thu nhỏ padding trong nút một xíu */
  border-radius: 4px;
  font-size: 0.7rem; /* Chữ trong nút gọn lại một xíu */
  font-weight: 600;
  text-transform: uppercase;
  background-color: #ffffff;
  letter-spacing: 0.5px;
}

.outline-blue {
  color: #0d6efd;
  border: 1px solid #0d6efd;
}

.outline-yellow {
  color: #d39e00;
  border: 1px solid #ffc107;
}

.outline-green {
  color: #006400;
  border: 1px solid #006400;
}

@media (max-width: 991px) {
  .footer {
    text-align: center;
  }
  .contact-info p {
    justify-content: center;
  }
  .text-md-end {
    text-align: center !important;
  }
  .justify-content-md-end {
    justify-content: center !important;
  }
}
</style>
