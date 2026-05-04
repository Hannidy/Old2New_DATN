<template>
  <footer class="footer border-top pt-5 pb-3 bg-white">
    <div class="container">
      <div class="row g-4">
        <!-- Cột 1: Logo và Mô tả -->
        <div class="col-lg-4 col-md-6">
          <div class="footer-logo d-flex align-items-center mb-3">
            <div class="logo-icon me-2">
              <i class="bi bi-recycle fs-2 text-danger"></i>
            </div>
            <span class="fs-4 fw-bold text-uppercase">OLD<span class="text-danger">2</span>NEW</span>
          </div>
          <p class="text-muted small mb-4" style="max-width: 300px;">
            OLD2NEW - Nền tảng mua và bán đồ cũ uy tín và có đảm bảo tại Việt Nam. Chạm cũ, mở mới vòng đời sản phẩm.
          </p>
          
          <h6 class="fw-bold mb-3 text-danger text-uppercase">Liên hệ với chúng tôi</h6>
          <div class="contact-info small text-muted">
            <p class="mb-2">
              <i class="bi bi-telephone me-2 text-danger"></i> 
              <strong>0379185939</strong>
            </p>
            <p class="mb-2">
              <i class="bi bi-envelope me-2 text-danger"></i> 
              doanngocduong03112006@gmail.com
            </p>
            <p class="mb-0">
              <i class="bi bi-geo-alt me-2 text-danger"></i> 
              Số nhà B2.25 Vinhomes Gardenia, Hàm Nghi, P. Cầu Diễn, Q. Nam Từ Liêm, TP. Hà Nội
            </p>
          </div>
        </div>

        <!-- Cột 2: Về OLD2NEW -->
        <div class="col-lg-2 col-md-6">
          <h6 class="fw-bold mb-4 text-danger text-uppercase">Về OLD2NEW</h6>
          <ul class="list-unstyled footer-links">
            <li><a href="#">Giới thiệu OLD2NEW</a></li>
            <li><a href="#">Các câu hỏi thường gặp</a></li>
            <li><a href="#">Điều khoản dịch vụ</a></li>
            <li><a href="#">Quy chế hoạt động</a></li>
            <li><a href="#">Hướng dẫn bán hàng</a></li>
            <li><a href="#">Blog & Sự kiện</a></li>
          </ul>
        </div>

        <!-- Cột 3: Chính sách -->
        <div class="col-lg-2 col-md-6">
          <h6 class="fw-bold mb-4 text-danger text-uppercase">Chính sách</h6>
          <ul class="list-unstyled footer-links">
            <li><a href="#">Chính sách bảo mật</a></li>
            <li><a href="#">Bảo vệ người mua</a></li>
            <li><a href="#">Giải quyết tranh chấp</a></li>
            <li><a href="#">Chính sách giao tiếp</a></li>
            <li><a href="#">Những món đồ bị cấm</a></li>
          </ul>
        </div>

        <!-- Cột 4: Thanh toán & Ứng dụng -->
        <div class="col-lg-4 col-md-6">
          <h6 class="fw-bold mb-4 text-danger text-uppercase">Thanh toán & Vận chuyển</h6>
          <div class="d-flex gap-2 flex-wrap mb-4">
            <span class="payment-icon">VISA</span>
            <span class="payment-icon">VNPAY</span>
            <span class="payment-icon border-danger text-danger">MOMO</span>
            <span class="payment-icon">GHN</span>
            <span class="payment-icon">NINJAVAN</span>
          </div>

          <h6 class="fw-bold mb-3 text-danger text-uppercase">Tải ứng dụng</h6>
          <div class="d-flex gap-2 mb-4">
            <img src="https://upload.wikimedia.org/wikipedia/commons/3/3c/Download_on_the_App_Store_Badge.svg" alt="App Store" width="110">
            <img src="https://upload.wikimedia.org/wikipedia/commons/7/78/Google_Play_Store_badge_EN.svg" alt="Google Play" width="110">
          </div>

          <h6 class="fw-bold mb-3 text-danger text-uppercase">Theo dõi chúng tôi</h6>
          <div class="d-flex gap-3 fs-4">
            <a href="#" class="text-primary"><i class="bi bi-facebook"></i></a>
            <a href="#" class="text-danger"><i class="bi bi-instagram"></i></a>
            <a href="#" class="text-danger"><i class="bi bi-youtube"></i></a>
            <a href="#" class="text-dark"><i class="bi bi-tiktok"></i></a>
          </div>
        </div>
      </div>

      <div class="text-center mt-5 pt-3 border-top">
        <p class="text-muted small mb-0">
          &copy; 2026 <span class="text-danger fw-bold">OLD2NEW</span>. Nền tảng mua bán đồ cũ C2C.
        </p>
      </div>
    </div>
  </footer>
</template>

<style scoped>
.footer {
  font-family: 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
}

h6 {
  font-size: 0.85rem;
  letter-spacing: 0.5px;
}

.footer-links li {
  margin-bottom: 10px;
}

.footer-links a {
  color: #6c757d;
  text-decoration: none;
  font-size: 0.9rem;
  transition: all 0.3s ease;
}

.footer-links a:hover {
  color: #dc3545;
  padding-left: 4px;
}

.payment-icon {
  border: 1px solid #dee2e6;
  padding: 4px 10px;
  border-radius: 4px;
  font-size: 0.65rem;
  font-weight: 800;
  color: #495057;
  background-color: #f8f9fa;
}

@media (max-width: 991px) {
  .footer {
    text-align: center;
  }
  .footer-logo, .contact-info p, .d-flex {
    justify-content: center;
  }
}
</style>