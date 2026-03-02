<template>
  <div class="header-sticky shadow-sm">
    <header class="d-flex align-items-center justify-content-between border-bottom py-3 px-3 px-md-5 mb-0 bg-white">
      <div class="fs-3 fw-bold text-dark" style="cursor: pointer;" @click="router.push('/')">Old2New</div>
      
      <div class="input-group w-50 d-none d-md-flex">
        <input type="text" class="form-control" v-model="searchQuery" @keyup.enter="handleSearch" placeholder="Tìm kiếm sản phẩm bạn cần...">
        <button class="btn btn-outline-secondary bg-light" type="button" @click="handleSearch">🔍</button>
      </div>
      
      <div class="d-flex align-items-center gap-3 gap-md-4">
        
        <div v-if="currentUser" class="user-profile-dropdown position-relative">
          <div class="d-flex align-items-center gap-2" style="cursor: pointer;">
            <div class="rounded-circle bg-dark text-white d-flex align-items-center justify-content-center fw-bold" style="width: 32px; height: 32px;">
              {{ currentUser.hoVaTen ? currentUser.hoVaTen.charAt(0).toUpperCase() : 'U' }}
            </div>
            <span class="fw-semibold text-dark d-none d-md-block" style="max-width: 120px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
              {{ currentUser.hoVaTen || currentUser.email }}
            </span>
            <span class="small text-muted">▾</span>
          </div>
          
          <ul class="dropdown-menu shadow custom-user-menu rounded">
            <li class="px-3 py-2 border-bottom bg-light">
              <div class="fw-bold text-dark">{{ currentUser.hoVaTen || 'Khách hàng' }}</div>
              <div class="small text-muted text-truncate" style="max-width: 180px;">{{ currentUser.email }}</div>
            </li>
            <li><router-link to="/profile" class="dropdown-item py-2 mt-1">👤 Hồ sơ của tôi</router-link></li>
            <li><router-link to="/quan-ly-don-hang" class="dropdown-item py-2">📦 Đơn hàng của tôi</router-link></li>
            <li><router-link to="/quan-ly-don-ban" class="dropdown-item py-2">🧾 Đơn khách đặt mua</router-link></li>
            <li><hr class="dropdown-divider my-1"></li>
            <li><button @click="logout" class="dropdown-item text-danger py-2 fw-bold">🚪 Đăng xuất</button></li>
          </ul>
        </div>

        <router-link v-else to="/login" class="btn btn-outline-dark btn-sm d-none d-md-inline-block fw-bold">Đăng nhập</router-link>
        
        <router-link to="/cart" class="text-dark text-decoration-none fs-5 position-relative">
          🛒
          </router-link>
        <a href="#" class="text-dark text-decoration-none fs-5">🔔</a>
        <button @click="router.push('/dang-ban')" class="btn btn-dark btn-sm px-3 fw-bold">Đăng bán</button>
      </div>
    </header>

    <nav class="container d-none d-md-flex align-items-center border-bottom pb-2 pt-2 position-relative bg-white">
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
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const searchQuery = ref('');
const categories = ref([]);
const currentUser = ref(null); // Biến lưu thông tin người dùng đang đăng nhập

// Lấy thông tin người dùng từ LocalStorage khi trang load
onMounted(() => {
  fetchCategories();
  
  const storedUser = localStorage.getItem('user');
  if (storedUser) {
    currentUser.value = JSON.parse(storedUser);
  }
});

// Hàm Đăng xuất
const logout = () => {
  // Xóa dữ liệu người dùng khỏi trình duyệt
  localStorage.removeItem('user');
  // Nếu bạn có dùng token thì xóa luôn token: localStorage.removeItem('token');
  
  // Cập nhật lại state
  currentUser.value = null;
  
  // Thông báo và chuyển hướng về trang chủ
  alert("Bạn đã đăng xuất thành công!");
  router.push('/');
};

const handleSearch = () => {
  if (searchQuery.value.trim() !== '') alert("Đang tìm kiếm: " + searchQuery.value);
};

const fetchCategories = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/categories/tree');
    categories.value = response.data;
  } catch (error) {
    console.error("Lỗi tải Danh mục:", error);
  }
};
</script>

<style scoped>
/* LÀM CHO HEADER ĐỨNG YÊN */
.header-sticky {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  width: 100%;
  z-index: 1100;
  background-color: white;
}

/* ========================================= */
/* CSS CHO MENU HỒ SƠ NGƯỜI DÙNG CỰC MƯỢT    */
/* ========================================= */
.user-profile-dropdown {
  padding: 10px 0; /* Tăng diện tích nhận chuột */
}
.custom-user-menu {
  display: none; 
  position: absolute; 
  top: 100%; 
  right: -20px; /* Đẩy sát lề phải để không bị lệch */
  margin-top: -5px; 
  min-width: 220px; 
  border: none;
  z-index: 1050;
  animation: fadeIn 0.2s ease;
}
/* Xổ menu xuống khi trỏ chuột vào vùng avatar */
.user-profile-dropdown:hover .custom-user-menu {
  display: block;
}
.custom-user-menu .dropdown-item:hover {
  background-color: #f8f9fa;
  color: #007bff;
}
.custom-user-menu .text-danger:hover {
  background-color: #fff5f5;
  color: #dc3545 !important;
}

/* GIỮ NGUYÊN STYLE CŨ CỦA BẠN */
.all-category-wrapper { position: relative; }
.all-category-wrapper:hover .main-menu { display: block; animation: fadeIn 0.2s ease; }

.main-menu {
  display: none; position: absolute; top: 100%; left: 0; margin: 0; padding: 0;
  background-color: white; border: 1px solid #eee; border-top: none; min-width: 250px;
  list-style: none; z-index: 1050; border-radius: 0 0 8px 8px;
}
.border-bottom-dashed { border-bottom: 1px dashed #f0f0f0; }
.menu-item { position: relative; }
.menu-item > a { color: #444; font-size: 0.95rem; }
.menu-item:hover > a { background-color: #fcfcfc; color: #007bff; }

.sub-menu {
  display: none; position: absolute; top: 0; left: 100%; margin: 0; padding: 0;
  background-color: white; border: 1px solid #eee; min-width: 240px; min-height: 100%;
  list-style: none; z-index: 1060;
}
.menu-item:hover .sub-menu { display: block; }
.menu-item-child { position: relative; }
.menu-item-child > a { color: #555; font-size: 0.9rem; border-bottom: 1px solid #f9f9f9; }
.menu-item-child:hover > a { background-color: #f8f9fa; color: #007bff; }

.sub-menu-3 {
  display: none; position: absolute; top: 0; left: 100%; margin: 0; padding: 0;
  background-color: white; border: 1px solid #eee; min-width: 220px; min-height: 100%;
  list-style: none; z-index: 1070;
}
.menu-item-child:hover .sub-menu-3 { display: block; }
.sub-menu-3 a { color: #666; font-size: 0.85rem; border-bottom: 1px solid #f9f9f9; }
.sub-menu-3 a:hover { color: #007bff; background-color: #f8f9fa; }

.quick-links a { color: #555; font-size: 0.95rem; transition: color 0.2s; }
.hover-orange:hover { color: #007bff !important; }

.custom-dropdown:hover .custom-dropdown-menu { display: block; animation: fadeIn 0.2s ease; }
.custom-dropdown-menu {
  display: none; position: absolute; top: 100%; left: 0; margin-top: 0; border: none;
  min-width: 200px; z-index: 1000; padding: 0;
}
.border-top-orange { border-top: 3px solid #007bff !important; }

@keyframes fadeIn { from { opacity: 0; transform: translateY(5px); } to { opacity: 1; transform: translateY(0); } }
</style>