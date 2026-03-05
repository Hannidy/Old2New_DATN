<template>
  <div class="bg-white min-vh-100">
    <AppHeader />

    <main class="container main-content py-4">
      
      <div class="d-flex align-items-center justify-content-between mb-4">
        <nav aria-label="breadcrumb">
          <ol class="breadcrumb mb-0 bg-transparent p-0">
            <li class="breadcrumb-item">
              <router-link to="/" class="text-decoration-none text-muted">
                <i class="bi bi-house-door"></i> Trang chủ
              </router-link>
            </li>
            <li class="breadcrumb-item active text-danger" aria-current="page">Chi tiết sản phẩm</li>
          </ol>
        </nav>

        <button @click="router.push('/')" class="btn btn-sm btn-outline-secondary rounded-pill px-3 shadow-sm">
          <i class="bi bi-arrow-left"></i> Quay lại trang chủ
        </button>
      </div>

      <div v-if="isLoading" class="text-center py-5">
        <div class="spinner-border text-danger" role="status"></div>
        <p class="mt-2 text-muted">Đang tải thông tin sản phẩm...</p>
      </div>

      <div v-else-if="product" class="row g-4">
        
        <div class="col-md-5">
          <div class="product-image-wrapper border rounded overflow-hidden shadow-sm bg-light d-flex align-items-center justify-content-center">
            <img :src="selectedImage || product.hinhAnh || 'https://via.placeholder.com/500'" 
                 class="w-100 h-100 object-fit-contain" 
                 alt="Sản phẩm">
          </div>
          
          <div class="d-flex gap-2 mt-3 overflow-auto pb-2" v-if="product.danhSachHinhAnh && product.danhSachHinhAnh.length > 1">
            <div v-for="(img, idx) in product.danhSachHinhAnh" :key="idx" 
                 class="border rounded overflow-hidden"
                 style="width: 80px; height: 80px; cursor: pointer; flex-shrink: 0;"
                 :class="{'border-danger border-2': selectedImage === img || (!selectedImage && idx === 0)}"
                 @click="selectedImage = img">
              <img :src="img" class="w-100 h-100 object-fit-cover" alt="Thumbnail">
            </div>
          </div>
        </div>

        <div class="col-md-7 ps-md-5">
          <span class="badge bg-danger mb-2 px-3 py-2 rounded-pill">{{ product.danhMuc || 'Sản phẩm' }}</span>
          
          <h1 class="fw-bold text-dark mb-3 display-6">{{ product.tenSanPham }}</h1>
          
          <div class="price-box py-3 border-top border-bottom mb-4 d-flex align-items-center justify-content-between">
            <div>
              <span class="fs-2 fw-bold text-danger">{{ formatCurrency(product.gia) }}</span>
            </div>
            <div class="text-end">
              <span class="badge bg-light text-dark border px-3 py-2">
                Tình trạng: <strong class="text-primary">{{ product.tinhTrang }}</strong>
              </span>
            </div>
          </div>

          <div class="row g-3 mb-4">
            <div class="col-6">
              <div class="p-3 border rounded-3 bg-light-subtle">
                <small class="text-muted d-block fw-bold text-uppercase" style="font-size: 0.7rem;">Trọng lượng</small>
                <span class="fw-semibold">{{ product.trongLuongGram }} gram</span>
              </div>
            </div>
            <div class="col-6">
              <div class="p-3 border rounded-3 bg-light-subtle">
                <small class="text-muted d-block fw-bold text-uppercase" style="font-size: 0.7rem;">Kích thước (DxRxC)</small>
                <span class="fw-semibold">{{ product.kichThuoc }}</span>
              </div>
            </div>
          </div>

          <div class="seller-card d-flex align-items-center p-3 border rounded-3 mb-4 shadow-sm">
              <div class="avatar-circle bg-secondary text-white rounded-circle d-flex align-items-center justify-content-center fw-bold fs-4">
                {{ product.nguoiBan?.charAt(0).toUpperCase() || 'U' }}
              </div>
              <div class="ms-3">
                <div class="fw-bold text-dark fs-5">{{ product.nguoiBan }}</div>
                <small class="text-success"><i class="bi bi-patch-check-fill"></i> Đang hoạt động</small>
              </div>
              
              <button 
                @click="router.push(`/shop/${product.nguoiDungId}`)" 
                class="btn btn-outline-dark btn-sm ms-auto fw-bold px-3"
              >
                Xem Shop
              </button>
            </div>

          <div class="mb-5">
            <h5 class="fw-bold border-start border-4 border-danger ps-2 mb-3">Mô tả sản phẩm</h5>
            <div class="product-description text-secondary p-3 bg-light rounded-3" style="white-space: pre-line; line-height: 1.8;">
              {{ product.moTa || 'Sản phẩm này hiện chưa có mô tả chi tiết từ người bán.' }}
            </div>
          </div>

          <div class="d-flex gap-3 sticky-action">
            <button @click="addToCart(product)" class="btn btn-outline-danger btn-lg flex-grow-1 fw-bold py-3 shadow-sm">
              <i class="bi bi-cart-plus"></i> Thêm vào giỏ
            </button>
            
            <button class="btn btn-danger btn-lg flex-grow-1 fw-bold py-3 shadow-sm">
              MUA NGAY
            </button>
          </div>
        </div>
      </div>

      <div v-else class="text-center py-5 shadow-sm rounded-4 border">
        <span class="display-1">🔎</span>
        <h3 class="mt-3 fw-bold">Rất tiếc, sản phẩm không tồn tại!</h3>
        <p class="text-muted">Sản phẩm có thể đã được bán hoặc bị gỡ bỏ.</p>
        <router-link to="/" class="btn btn-primary px-5 py-2 mt-2">Quay về Trang chủ</router-link>
      </div>
    </main>

    <AppFooter />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import AppHeader from '@/layouts/Header.vue';
import AppFooter from '@/layouts/Footer.vue';

const route = useRoute();
const router = useRouter();
const product = ref(null);
const isLoading = ref(true);
const selectedImage = ref(null); // Biến chứa ảnh đang được chọn xem


// 1. Định dạng tiền tệ VND
const formatCurrency = (val) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(val || 0);
};

// 2. Lấy dữ liệu chi tiết từ Spring Boot
const fetchProductDetail = async () => {
  const productId = route.params.id; // Lấy ID từ URL
  isLoading.value = true;
  try {
    const response = await axios.get(`http://localhost:8080/api/products/${productId}`);
    product.value = response.data;
  } catch (error) {
    console.error("Lỗi khi tải chi tiết sản phẩm:", error);
  } finally {
    isLoading.value = false;
  }
};

// 3. Xử lý Thêm vào giỏ hàng (Giữ logic giống hệt HomeView)
const addToCart = (productData) => {
  if (!productData) return;

  // Lấy giỏ hàng từ LocalStorage
  let cart = JSON.parse(localStorage.getItem('cart') || '[]');
  
  // Kiểm tra xem sản phẩm đã có trong giỏ chưa (dựa vào ID)
  const existingItem = cart.find(item => item.id === productData.id);

  if (existingItem) {
    // Nếu đã có -> Báo lỗi vì đây là hàng cũ/độc bản (mỗi món chỉ có 1)
    alert(`Sản phẩm "${productData.tenSanPham}" là hàng độc bản và đã nằm trong giỏ hàng của bạn rồi!`);
  } else {
    // Nếu chưa có -> Thêm mới vào mảng
    cart.push({
      id: productData.id,
      tenSanPham: productData.tenSanPham,
      gia: productData.gia,
      hinhAnh: productData.hinhAnh
    });
    
    // Lưu lại vào LocalStorage
    localStorage.setItem('cart', JSON.stringify(cart));
    
    // Thông báo thành công
    alert(`Đã thêm "${productData.tenSanPham}" vào giỏ hàng thành công!`);
  }
};

// Khi trang được mở
onMounted(() => {
  fetchProductDetail();
  // Luôn luôn cuộn lên đầu trang khi vào xem chi tiết
  window.scrollTo({ top: 0, behavior: 'instant' });
});
</script>

<style scoped>
/* FIX LỖI PADDING/MARGIN CHO HEADER FIXED */
.main-content {
  /* Header của bạn cao khoảng 150px, nên dùng margin-top để đẩy hẳn xuống */
  margin-top: 160px;
  min-height: 70vh;
}

/* Khung ảnh sản phẩm */
.product-image-wrapper {
  height: 500px;
  background-color: #fbfbfb;
  border-color: #f0f0f0 !important;
}

.object-fit-contain {
  object-fit: contain;
}

/* Hộp giá tiền */
.price-box {
  background-color: #fffcfc;
  padding-left: 15px;
  padding-right: 15px;
  border-radius: 10px;
}

/* Thẻ người bán */
.avatar-circle {
  width: 55px;
  height: 55px;
  border: 2px solid #fff;
  box-shadow: 0 0 10px rgba(0,0,0,0.1);
}

.seller-card {
  background-color: #fff;
  transition: all 0.3s;
}

.seller-card:hover {
  background-color: #fcfcfc;
}

/* Responsive cho điện thoại */
@media (max-width: 768px) {
  .main-content {
    margin-top: 110px; /* Header mobile thường thấp hơn */
  }
  .product-image-wrapper {
    height: 350px;
  }
  .sticky-action {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    background: white;
    padding: 15px;
    z-index: 1000;
    box-shadow: 0 -5px 15px rgba(0,0,0,0.05);
  }
}
</style>