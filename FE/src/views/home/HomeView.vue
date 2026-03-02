<template>
  <div class="bg-white text-dark pb-5">
    <AppHeader />

    <main class="container main-content">
      
      <div id="homeBanner" class="carousel slide carousel-fade mb-5 border rounded overflow-hidden shadow-sm" data-bs-ride="carousel">
        <div class="carousel-inner" style="height: 300px;">
          <div class="carousel-item active h-100">
            <img class="w-100 h-100 object-fit-cover" src="https://i.pinimg.com/1200x/02/90/7a/02907ad6256dc7cf4e80e3ace6084f4b.jpg" alt="Banner 1">
          </div>
          <div class="carousel-item h-100">
            <img class="w-100 h-100 object-fit-cover" src="https://i.pinimg.com/1200x/e2/2f/a8/e22fa8b21cfd5c6420aceaba337f9623.jpg" alt="Banner 2">
          </div>
        </div>
        <button class="carousel-control-prev" type="button" data-bs-target="#homeBanner" data-bs-slide="prev">
          <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        </button>
        <button class="carousel-control-next" type="button" data-bs-target="#homeBanner" data-bs-slide="next">
          <span class="carousel-control-next-icon" aria-hidden="true"></span>
        </button>
      </div>

      <section id="product-section">
        <div class="d-flex justify-content-between align-items-end mb-4 border-start border-4 border-danger ps-3">
          <div>
            <h2 class="fs-4 fw-bold mb-0 text-dark">🛍️ Khám phá sản phẩm</h2>
            <small class="text-muted">Đồ cũ chất lượng - Giá rẻ cho mọi nhà</small>
          </div>
          <span class="text-muted small bg-light px-2 py-1 rounded">Trang {{ currentPage }} / {{ totalPages }}</span>
        </div>
        
        <div v-if="isLoading" class="text-center py-5">
          <div class="spinner-border text-danger" role="status"></div>
          <p class="mt-2 text-muted">Đang tải sản phẩm...</p>
        </div>

        <div v-else class="row row-cols-2 row-cols-md-3 row-cols-lg-5 g-3">
          <div v-for="product in products" :key="product.id" class="col">
            <div class="card h-100 shadow-sm product-card border-0">
              
              <div class="bg-light d-flex align-items-center justify-content-center border-bottom overflow-hidden position-relative" style="height: 180px;">
                <img v-if="product.hinhAnh" :src="product.hinhAnh" class="w-100 h-100 object-fit-cover" alt="Sản phẩm">
                <div v-else class="text-muted small d-flex flex-column align-items-center">
                   <span class="fs-2">🖼️</span>
                   <span>Chưa có ảnh</span>
                </div>
                <span class="position-absolute top-0 start-0 badge bg-warning text-dark m-2 small" style="font-size: 0.7rem;">
                  {{ product.tinhTrang || 'Đồ cũ' }}
                </span>
              </div>

              <div class="card-body p-2 d-flex flex-column">
                <h6 class="card-title text-dark text-truncate mb-1" style="font-size: 0.9rem;" :title="product.tenSanPham">
                  {{ product.tenSanPham }}
                </h6>
                <div class="mb-2">
                  <span class="text-danger fw-bold d-block fs-6">{{ formatCurrency(product.gia) }}</span>
                  <small class="text-muted"><i class="bi bi-person"></i> {{ product.nguoiBan || 'Ẩn danh' }}</small>
                </div>

                <div class="d-flex gap-1 mt-auto">
                  <button 
                    @click="goToDetail(product.id)" 
                    class="btn btn-outline-primary btn-sm flex-grow-1 py-1 fw-bold"
                    style="font-size: 0.75rem;"
                  >
                    Chi tiết
                  </button>
                  <button 
                    @click="addToCart(product)" 
                    class="btn btn-primary btn-sm px-2 py-1"
                    title="Thêm vào giỏ hàng"
                  >
                    🛒
                  </button>
                </div>

              </div>
            </div>
          </div>
        </div>

        <nav v-if="totalPages > 1" class="mt-5 d-flex justify-content-center">
          <ul class="pagination shadow-sm">
            <li class="page-item" :class="{ disabled: currentPage === 1 }">
              <button class="page-link" @click="changePage(currentPage - 1)">Trước</button>
            </li>
            <li v-for="page in totalPages" :key="page" class="page-item" :class="{ active: currentPage === page }">
              <button class="page-link" @click="changePage(page)">{{ page }}</button>
            </li>
            <li class="page-item" :class="{ disabled: currentPage === totalPages }">
              <button class="page-link" @click="changePage(currentPage + 1)">Sau</button>
            </li>
          </ul>
        </nav>
      </section>

    </main>

    <AppFooter />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

// ĐIỀU CHỈNH LẠI CHỮ HOA/CHỮ THƯỜNG CHO KHỚP VỚI MÁY BẠN NẾU CẦN NHÉ!
import AppHeader from '@/layouts/Header.vue';
import AppFooter from '@/layouts/Footer.vue';

const router = useRouter();
const products = ref([]);
const isLoading = ref(true);
const currentPage = ref(1);
const totalPages = ref(1);
const itemsPerPage = 30;

// Format tiền tệ
const formatCurrency = (val) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(val || 0);
};

// Gọi API lấy danh sách sản phẩm
const fetchProducts = async (page = 1) => {
  isLoading.value = true;
  try {
    const res = await axios.get(`http://localhost:8080/api/products/home?page=${page - 1}&size=${itemsPerPage}`);
    products.value = res.data.products;
    currentPage.value = res.data.currentPage + 1;
    totalPages.value = res.data.totalPages;
  } catch (error) {
    console.error("Lỗi kết nối Backend:", error);
  } finally {
    isLoading.value = false;
  }
};

// Chuyển trang chi tiết
const goToDetail = (id) => {
  router.push(`/product/${id}`);
};

// Thêm vào giỏ hàng (Local Storage)
// Thêm vào giỏ hàng (Hàng độc bản)
const addToCart = (product) => {
  let cart = JSON.parse(localStorage.getItem('cart') || '[]');
  const existingItem = cart.find(item => item.id === product.id);

  if (existingItem) {
    // Nếu đã có trong giỏ -> Báo lỗi không cho thêm
    alert(`Sản phẩm "${product.tenSanPham}" là hàng độc bản và đã nằm trong giỏ hàng của bạn rồi!`);
  } else {
    // Nếu chưa có -> Thêm vào giỏ với số lượng mặc định luôn là 1
    cart.push({
      id: product.id,
      tenSanPham: product.tenSanPham,
      gia: product.gia,
      hinhAnh: product.hinhAnh
    });
    localStorage.setItem('cart', JSON.stringify(cart));
    alert(`Đã thêm "${product.tenSanPham}" vào giỏ hàng!`);
  }
};

// Đổi trang
const changePage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    fetchProducts(page);
    const section = document.getElementById('product-section');
    if (section) {
      window.scrollTo({ top: section.offsetTop - 180, behavior: 'smooth' });
    }
  }
};

// Khởi chạy khi mở web
onMounted(() => {
  fetchProducts(1);
});
</script>

<style scoped>
/* Đẩy nội dung xuống không bị Header (fixed) đè */
.main-content {
  padding-top: 155px; 
}

/* Hiệu ứng cho Card Sản Phẩm */
.product-card { 
  transition: all 0.3s ease; 
  cursor: pointer;
  border-radius: 12px;
  overflow: hidden;
}
.product-card:hover { 
  transform: translateY(-8px); 
  box-shadow: 0 12px 25px rgba(0,0,0,0.12) !important; 
}
.object-fit-cover { object-fit: cover; }

/* Nút phân trang */
.pagination .page-link {
  color: #555; border: none; margin: 0 3px; border-radius: 8px; font-weight: 500;
}
.pagination .active .page-link {
  background-color: #dc3545; color: white;
}
.pagination .page-link:hover {
  background-color: #f8f9fa; color: #dc3545;
}

@media (max-width: 768px) {
  .main-content { padding-top: 120px; }
  .carousel-inner { height: 180px !important; }
}
</style>