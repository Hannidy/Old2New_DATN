<template>
  <div class="bg-white text-dark pb-5">
    
    <header class="d-flex align-items-center justify-content-between border-bottom py-3 px-3 px-md-5 mb-3">
      <div class="fs-3 fw-bold text-dark" style="cursor: pointer;" @click="router.push('/')">Old2New</div>
      
      <div class="input-group w-50 d-none d-md-flex">
        <input type="text" class="form-control" v-model="searchQuery" @keyup.enter="handleSearch" placeholder="Tìm kiếm sản phẩm bạn cần...">
        <button class="btn btn-outline-secondary bg-light" type="button" @click="handleSearch">🔍</button>
      </div>

      <div class="d-flex align-items-center gap-3 gap-md-4">
        <router-link to="/login" class="btn btn-primary btn-sm d-none d-md-inline-block">Đăng nhập</router-link>
        <a href="#" class="text-dark text-decoration-none fs-5">🛒</a>
        <a href="#" class="text-dark text-decoration-none fs-5">🔔</a>
        <button @click="goToPostProduct" class="btn btn-dark btn-sm px-3">Đăng bán</button>
      </div>
    </header>

    <nav class="container d-none d-md-flex align-items-center border-bottom pb-2 mb-4 position-relative">
      
      <div class="all-category-wrapper me-4">
        <div class="category-btn d-flex align-items-center gap-2 text-dark" style="cursor: pointer; padding: 10px 0;">
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" class="bi bi-grid-fill text-muted" viewBox="0 0 16 16">
            <path d="M1 2.5A1.5 1.5 0 0 1 2.5 1h3A1.5 1.5 0 0 1 7 2.5v3A1.5 1.5 0 0 1 5.5 7h-3A1.5 1.5 0 0 1 1 5.5v-3zm8 0A1.5 1.5 0 0 1 10.5 1h3A1.5 1.5 0 0 1 15 2.5v3A1.5 1.5 0 0 1 13.5 7h-3A1.5 1.5 0 0 1 9 5.5v-3zm-8 8A1.5 1.5 0 0 1 2.5 9h3A1.5 1.5 0 0 1 7 10.5v3A1.5 1.5 0 0 1 5.5 15h-3A1.5 1.5 0 0 1 1 13.5v-3zm8 0A1.5 1.5 0 0 1 10.5 9h3a1.5 1.5 0 0 1 1.5 1.5v3a1.5 1.5 0 0 1-1.5 1.5h-3A1.5 1.5 0 0 1 9 13.5v-3z"/>
          </svg>
          <span class="fw-semibold">Tất cả danh mục</span> 
          <span class="small text-muted ms-1">▾</span>
        </div>

        <ul class="main-menu shadow-sm rounded-bottom">
          <li v-for="category in categories" :key="category.id" class="menu-item border-bottom-dashed">
            <a href="#" class="d-flex justify-content-between align-items-center text-decoration-none py-2 px-3">
              {{ category.name }}
              <span v-if="category.children && category.children.length > 0" class="text-muted small">›</span>
            </a>

            <ul class="sub-menu shadow-sm" v-if="category.children && category.children.length > 0">
              <li v-for="child in category.children" :key="child.id" class="menu-item-child">
                <a href="#" class="d-flex justify-content-between align-items-center text-decoration-none py-2 px-3">
                  {{ child.name }}
                  <span v-if="child.children && child.children.length > 0" class="text-muted small">›</span>
                </a>

                <ul class="sub-menu-3 shadow-sm" v-if="child.children && child.children.length > 0">
                  <li v-for="grandchild in child.children" :key="grandchild.id">
                    <a href="#" class="text-decoration-none py-2 px-3 d-block">{{ grandchild.name }}</a>
                  </li>
                </ul>
              </li>
            </ul>
          </li>
        </ul>
      </div>

      <div class="d-flex flex-wrap gap-4 quick-links flex-grow-1 align-items-center h-100">
        <div v-for="cat in categories.slice(0, 8)" :key="'nav-'+cat.id" class="nav-item dropdown custom-dropdown position-relative h-100">
          <a href="#" class="text-dark text-decoration-none py-2 d-inline-block hover-orange">
            {{ cat.name }}
          </a>
          
          <ul class="dropdown-menu shadow-sm custom-dropdown-menu rounded-0 border-top-orange" v-if="cat.children && cat.children.length > 0">
            <li v-for="child in cat.children" :key="'nav-child-'+child.id">
              <a class="dropdown-item py-2" href="#">{{ child.name }}</a>
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <main class="container">
      <div id="homeBanner" class="carousel slide carousel-fade mb-5 border rounded overflow-hidden shadow-sm" data-bs-ride="carousel" data-bs-interval="3000">
        <div class="carousel-inner" style="height: 300px;">
          <div class="carousel-item active h-100 ">
            <h2 class="text-white"><img style="width: 100%; height: auto;" src="https://i.pinimg.com/1200x/02/90/7a/02907ad6256dc7cf4e80e3ace6084f4b.jpg" alt=""></h2>
          </div>
          <div class="carousel-item h-100 ">
            <h2 class="text-white"><img style="width: 100%; height: auto;" src="https://i.pinimg.com/1200x/e2/2f/a8/e22fa8b21cfd5c6420aceaba337f9623.jpg" alt=""></h2>
          </div>
        </div>
      </div>

      <section class="mb-5" id="product-section">
        <div class="d-flex justify-content-between align-items-end mb-4">
          <h2 class="fs-4 fw-bold mb-0 text-danger">🛍️ Khám phá sản phẩm</h2>
          <span class="text-muted small">Đang xem trang {{ currentPage }} / {{ totalPages }}</span>
        </div>
        
        <div v-if="isLoading" class="text-center py-5">
          <div class="spinner-border text-secondary" role="status"></div>
        </div>

        <div v-else class="row row-cols-2 row-cols-md-3 row-cols-lg-5 g-3">
          <div v-for="product in products" :key="product.id" class="col">
            <div class="card h-100 shadow-sm product-card">
              <div class="bg-light d-flex align-items-center justify-content-center border-bottom overflow-hidden" style="height: 180px;">
                <img v-if="product.hinhAnh" :src="product.hinhAnh" class="w-100 h-100 object-fit-cover" alt="Sản phẩm">
                <span v-else class="text-muted small">Chưa có ảnh</span>
              </div>
              <div class="card-body p-2 d-flex flex-column">
                <h6 class="card-title text-dark text-truncate mb-1" style="font-size: 0.9rem;" :title="product.tenSanPham">
                  {{ product.tenSanPham }}
                </h6>
                <div class="mt-auto">
                  <span class="text-danger fw-bold d-block">{{ formatCurrency(product.gia) }}</span>
                  <small class="text-muted">Bởi: {{ product.nguoiBan || 'Ẩn danh' }}</small>
                </div>
              </div>
            </div>
          </div>
        </div>

        <nav v-if="totalPages > 1" class="mt-5 d-flex justify-content-center">
          <ul class="pagination">
            <li class="page-item" :class="{ disabled: currentPage === 1 }">
              <button class="page-link" @click="changePage(currentPage - 1)">Trang trước</button>
            </li>
            
            <li v-for="page in totalPages" :key="page" class="page-item" :class="{ active: currentPage === page }">
              <button class="page-link" @click="changePage(page)">{{ page }}</button>
            </li>
            
            <li class="page-item" :class="{ disabled: currentPage === totalPages }">
              <button class="page-link" @click="changePage(currentPage + 1)">Trang sau</button>
            </li>
          </ul>
        </nav>
      </section>
    </main>

    <footer class="border-top mt-5 py-5 bg-light">
      <div class="container text-center">
        <h2 class="fs-4 fw-bold mb-4">Về Old2New - Chạm Cũ, Mở Mới</h2>
        <p class="text-secondary mx-auto" style="max-width: 800px; line-height: 1.8;">
          Chào mừng bạn đến với <strong>Old2New</strong> – Nền tảng giao thương đồ cũ C2C hàng đầu. Chúng tôi tin rằng mỗi món đồ đều mang một vòng đời vô tận...
        </p>
      </div>
    </footer>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const searchQuery = ref('');
const isLoading = ref(true);

const categories = ref([]);
const products = ref([]);
const currentPage = ref(1);
const totalPages = ref(1);
const itemsPerPage = 30;

const formatCurrency = (value) => {
  if (!value) return '0 đ';
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
};

const handleSearch = () => {
  if (searchQuery.value.trim() !== '') alert("Đang tìm kiếm: " + searchQuery.value);
};

const goToPostProduct = () => router.push('/dang-ban');

const fetchProducts = async (page = 1) => {
  isLoading.value = true;
  try {
    const response = await axios.get(`http://localhost:8080/api/products/home?page=${page - 1}&size=${itemsPerPage}`);
    products.value = response.data.products;
    currentPage.value = response.data.currentPage + 1;
    totalPages.value = response.data.totalPages;
  } catch (error) {
    console.error("Lỗi tải SP:", error);
  } finally {
    isLoading.value = false;
  }
};

const changePage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    fetchProducts(page);
    document.getElementById('product-section').scrollIntoView({ behavior: 'smooth' });
  }
};

const fetchCategories = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/categories/tree');
    categories.value = response.data;
  } catch (error) {
    console.error("Lỗi tải Danh mục:", error);
  }
};

onMounted(() => {
  fetchCategories();
  fetchProducts(1);
});
</script>

<style scoped>
/* Card Sản Phẩm */
.product-card { transition: transform 0.2s ease, box-shadow 0.2s ease; cursor: pointer; }
.product-card:hover { transform: translateY(-5px); box-shadow: 0 10px 20px rgba(0,0,0,0.1) !important; }

/* Phân trang */
.pagination .page-link { color: #007bff; cursor: pointer;}
.pagination .active .page-link { background-color: #007bff; border-color: #007bff; color: white; }

/* =========================================
   MEGA MENU BAY NGANG (FLYOUT)
   ========================================= */
.all-category-wrapper { position: relative; }
.all-category-wrapper:hover .main-menu { display: block; animation: fadeIn 0.2s ease; }

/* Tầng 1 */
.main-menu {
  display: none; position: absolute; top: 100%; left: 0; margin: 0; padding: 0;
  background-color: white; border: 1px solid #eee; border-top: none; min-width: 250px;
  list-style: none; z-index: 1050; border-radius: 0 0 8px 8px;
}
.border-bottom-dashed { border-bottom: 1px dashed #f0f0f0; }
.menu-item { position: relative; }
.menu-item > a { color: #444; font-size: 0.95rem; }
.menu-item:hover > a { background-color: #fcfcfc; color: #007bff; }

/* Tầng 2 */
.sub-menu {
  display: none; position: absolute; top: 0; left: 100%; margin: 0; padding: 0;
  background-color: white; border: 1px solid #eee; min-width: 240px; min-height: 100%;
  list-style: none; z-index: 1060;
}
.menu-item:hover .sub-menu { display: block; }
.menu-item-child { position: relative; }
.menu-item-child > a { color: #555; font-size: 0.9rem; border-bottom: 1px solid #f9f9f9; }
.menu-item-child:hover > a { background-color: #f8f9fa; color: #007bff; }

/* Tầng 3 (Dành cho cấp Cháu nếu có) */
.sub-menu-3 {
  display: none; position: absolute; top: 0; left: 100%; margin: 0; padding: 0;
  background-color: white; border: 1px solid #eee; min-width: 220px; min-height: 100%;
  list-style: none; z-index: 1070;
}
.menu-item-child:hover .sub-menu-3 { display: block; }
.sub-menu-3 a { color: #666; font-size: 0.85rem; border-bottom: 1px solid #f9f9f9; }
.sub-menu-3 a:hover { color: #007bff; background-color: #f8f9fa; }

/* =========================================
   DROPDOWN THANH NGANG (QUICK LINKS)
   ========================================= */
.quick-links a { color: #555; font-size: 0.95rem; transition: color 0.2s; }
.hover-orange:hover { color: #007bff !important; }

.custom-dropdown:hover .custom-dropdown-menu { display: block; animation: fadeIn 0.2s ease; }
.custom-dropdown-menu {
  display: none; position: absolute; top: 100%; left: 0; margin-top: 0; border: none;
  min-width: 200px; z-index: 1000; padding: 0;
}
.border-top-orange { border-top: 3px solid #007bff !important; }
.dropdown-item { font-size: 0.9rem; color: #444; border-bottom: 1px dashed #f5f5f5; }
.dropdown-item:hover { background-color: #f8f9fa; color: #007bff; }

@keyframes fadeIn { from { opacity: 0; transform: translateY(5px); } to { opacity: 1; transform: translateY(0); } }
</style>