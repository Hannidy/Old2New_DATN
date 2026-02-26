<template>
  <div class="home-container">
    <header class="header">
      <div class="logo">O2N Market</div>
      <nav class="nav-links">
        <a href="#">Trang chủ</a>
        <a href="#">Sản phẩm</a>
        <a href="#">Blog</a>
      </nav>
      <div class="auth-buttons">
        <router-link to="/login" class="btn-login">Đăng nhập</router-link>
      </div>
    </header>

    <section class="banner">
      <h1>Chào mừng đến với OLD2NEW</h1>
      <p>Sàn thương mại điện tử C2C - Mua cũ đổi mới, uy tín hàng đầu.</p>
      <button class="btn-explore">Khám phá ngay</button>
    </section>

    <section class="products">
      <h2>Sản phẩm nổi bật</h2>
      <div class="product-grid">
        <div class="product-card">
          <div class="placeholder-img">Ảnh SP</div>
          <h3>Laptop Gaming cũ</h3>
          <p class="price">15.000.000đ</p>
          <button>Xem chi tiết</button>
        </div>
        <div class="product-card">
          <div class="placeholder-img">Ảnh SP</div>
          <h3>Điện thoại iPhone 12</h3>
          <p class="price">8.500.000đ</p>
          <button>Xem chi tiết</button>
        </div>
         <div class="product-card">
          <div class="placeholder-img">Ảnh SP</div>
          <h3>Bàn phím cơ</h3>
          <p class="price">500.000đ</p>
          <button>Xem chi tiết</button>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
// Hiện tại trang chủ chưa cần logic phức tạp
</script>

<style scoped>
/* Reset và style cơ bản */
.home-container {
  font-family: 'Segoe UI', sans-serif;
  color: #333;
}

/* Header */
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 2rem;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.logo {
  font-weight: bold;
  font-size: 1.5rem;
  color: #007bff;
}

.nav-links a {
  margin: 0 15px;
  text-decoration: none;
  color: #555;
  font-weight: 500;
}

.btn-login {
  text-decoration: none;
  background-color: #007bff;
  color: white;
  padding: 8px 16px;
  border-radius: 4px;
  font-weight: 500;
}

/* Banner */
.banner {
  background: linear-gradient(135deg, #007bff, #00d4ff);
  color: white;
  text-align: center;
  padding: 4rem 1rem;
}

.banner h1 {
  font-size: 2.5rem;
  margin-bottom: 0.5rem;
}

.btn-explore {
  background: white;
  color: #007bff;
  border: none;
  padding: 10px 25px;
  border-radius: 20px;
  font-weight: bold;
  cursor: pointer;
  margin-top: 1rem;
}

/* Products */
.products {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.products h2 {
  text-align: center;
  margin-bottom: 2rem;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
}

.product-card {
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 1rem;
  text-align: center;
  transition: transform 0.2s;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.placeholder-img {
  width: 100%;
  height: 150px;
  background-color: #eee;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 1rem;
  color: #888;
}

.price {
  color: #d0021b;
  font-weight: bold;
  font-size: 1.1rem;
}

.product-card button {
  background-color: white;
  border: 1px solid #007bff;
  color: #007bff;
  padding: 5px 15px;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 10px;
}
.product-card button:hover {
  background-color: #007bff;
  color: white;
}
</style>