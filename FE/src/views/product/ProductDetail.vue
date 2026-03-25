<template>
  <div class="bg-white min-vh-100">
    <AppHeader />

    <main class="container main-content py-4">
      
      <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb mb-0">
          <li class="breadcrumb-item">
            <router-link to="/" class="text-decoration-none text-muted">Trang chủ</router-link>
          </li>
          <li class="breadcrumb-item active" aria-current="page">Chi tiết sản phẩm</li>
        </ol>
      </nav>

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
                <span class="fw-semibold">{{ product.kichThuoc || (product.chieuDaiCm + 'x' + product.chieuRongCm + 'x' + product.chieuCaoCm) }}</span>
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
                class="btn btn-outline-dark btn-sm ms-auto fw-bold px-3">
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
            <button v-if="isMyProduct" class="btn btn-secondary btn-lg w-100 fw-bold py-3 shadow-sm" disabled>
              <i class="bi bi-lock-fill"></i> SẢN PHẨM CỦA BẠN
            </button>

            <template v-else>
              <button @click="addToCart(product)" class="btn btn-outline-danger btn-lg flex-grow-1 fw-bold py-3 shadow-sm">
                <i class="bi bi-cart-plus"></i> Thêm vào giỏ
              </button>

              <button class="btn btn-danger btn-lg flex-grow-1 fw-bold py-3 shadow-sm">
                MUA NGAY
              </button>
            </template>
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
import { ref, onMounted, computed } from 'vue'; // Nhớ thêm computed
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import AppHeader from '@/layouts/Header.vue';
import AppFooter from '@/layouts/Footer.vue';

const route = useRoute();
const router = useRouter();
const product = ref(null);
const isLoading = ref(true);
const selectedImage = ref(null); 

// 🔥 LOGIC MỚI: Lấy ID người dùng hiện tại đang đăng nhập
const currentUserId = computed(() => {
  const storedUser = localStorage.getItem('user');
  if (storedUser) {
    const userData = JSON.parse(storedUser);
    return userData.id || userData.nguoiDungId; 
  }
  return null;
});

// 🔥 LOGIC MỚI: Kiểm tra sản phẩm này có phải của User đang đăng nhập hay không
const isMyProduct = computed(() => {
  if (!product.value || !currentUserId.value) return false;
  
  // 1. In toàn bộ dữ liệu sản phẩm ra console để xem Spring Boot thực sự gửi gì về
  console.log("📦 Dữ liệu sản phẩm từ BE:", product.value);
  console.log("👤 ID của tôi (currentUserId):", currentUserId.value);
  
  // 2. Quét mọi trường hợp tên biến ID mà Backend có thể trả về
  const ownerId = product.value.nguoiDung?.nguoiDungId 
               || product.value.nguoiDungId 
               || product.value.nguoiBanId 
               || product.value.sellerId;
               
  console.log("🏪 ID của người bán (ownerId):", ownerId);
  
  // 3. So sánh
  return currentUserId.value == ownerId; 
});

const formatCurrency = (val) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(val || 0);
};

const fetchProductDetail = async () => {
  const productId = route.params.id; 
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

const addToCart = async (product) => {
  if (!currentUserId.value) {
    alert("Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng!");
    router.push('/login');
    return;
  }

  try {
    const storedUser = JSON.parse(localStorage.getItem('user'));
    const token = storedUser.token || storedUser.accessToken;
    const userId = currentUserId.value;

    // 1. Lấy giỏ hàng hiện tại từ Redis về
    const getRes = await axios.get(`http://localhost:8080/api/cart/${userId}`, {
      headers: { Authorization: `Bearer ${token}` }
    });
    let cart = getRes.data || [];

    // 2. Kiểm tra trùng lặp
    const existingItem = cart.find(item => item.id === product.id || item.id === product.sanPhamId);
    if (existingItem) {
      alert(`Sản phẩm "${product.tenSanPham}" là hàng độc bản và đã nằm trong giỏ hàng của bạn rồi!`);
      return;
    }

    // 3. Thêm sản phẩm mới vào mảng
    cart.push({
      id: product.sanPhamId || product.id,
      tenSanPham: product.tenSanPham,
      gia: product.gia,
      hinhAnh: product.hinhAnh
    });

    // 4. Đồng bộ ngược mảng mới lên Redis
    await axios.post(`http://localhost:8080/api/cart/${userId}`, cart, {
      headers: { Authorization: `Bearer ${token}` }
    });

    alert(`Đã thêm "${product.tenSanPham}" vào giỏ hàng!`);
    
  } catch (error) {
    console.error("Lỗi thêm giỏ hàng:", error);
    alert("Không thể thêm vào giỏ hàng lúc này. Vui lòng thử lại!");
  }
};

onMounted(() => {
  fetchProductDetail();
  window.scrollTo({ top: 0, behavior: 'instant' });
});
</script>

<style scoped>
/* FIX LỖI PADDING/MARGIN CHO HEADER FIXED */
.main-content {
  margin-top: 160px;
  min-height: 70vh;
}
.product-image-wrapper { height: 500px; background-color: #fbfbfb; border-color: #f0f0f0 !important; }
.object-fit-contain { object-fit: contain; }
.price-box { background-color: #fffcfc; padding-left: 15px; padding-right: 15px; border-radius: 10px; }
.avatar-circle { width: 55px; height: 55px; border: 2px solid #fff; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
.seller-card { background-color: #fff; transition: all 0.3s; }
.seller-card:hover { background-color: #fcfcfc; }

@media (max-width: 768px) {
  .main-content { margin-top: 110px; }
  .product-image-wrapper { height: 350px; }
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