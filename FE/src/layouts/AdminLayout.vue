<template>
  <div class="admin-container d-flex bg-light min-vh-100">
    <aside class="sidebar bg-dark text-white shadow-lg" style="width: 280px; position: sticky; top: 0; height: 100vh;">
      <div class="p-4 border-bottom border-secondary mb-3 text-center">
        <h3 class="fw-bold text-danger mb-0">OLD2NEW</h3>
        <small class="text-muted text-uppercase fw-bold" style="font-size: 0.7rem;">Quản trị hệ thống</small>
      </div>

      <nav class="nav flex-column px-3 gap-2">
        <router-link to="/admin/thong-ke" class="nav-link admin-nav-item">
          <i class="bi bi-speedometer2 me-2"></i> Thống kê tổng quan
        </router-link>
        
        <router-link to="/admin/users" class="nav-link admin-nav-item">
          <i class="bi bi-people me-2"></i> Quản lý Người dùng
        </router-link>

        <router-link to="/admin/categories" class="nav-link admin-nav-item">
          <i class="bi bi-grid me-2"></i> Quản lý Danh mục
        </router-link>

        <router-link to="/admin/verify-products" class="nav-link admin-nav-item">
        <i class="bi bi-shield-check me-2"></i> Kiểm duyệt tin đăng
        </router-link>

        <router-link to="/admin/payments" class="nav-link admin-nav-item">
          <i class="bi bi-wallet2 me-2"></i> Đối soát thanh toán
        </router-link>
      </nav>

      <div class="mt-auto p-3 w-100 border-top border-secondary">
        <button @click="router.push('/')" class="btn btn-outline-light w-100 btn-sm">
          <i class="bi bi-box-arrow-left me-2"></i> Home
        </button>
      </div>
    </aside>

    <main class="flex-grow-1 overflow-auto">
      <header class="bg-white border-bottom p-3 d-flex justify-content-between align-items-center shadow-sm">
        <h5 class="mb-0 fw-bold text-dark text-uppercase">
          {{ currentPageName }}
        </h5>

      </header>

      <div class="p-4">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </div>
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();

// Tự động lấy tên trang dựa trên route hiện tại
const currentPageName = computed(() => {
  return route.meta.title || 'Bảng điều khiển';
});

const logout = () => {
  if(confirm("Bạn muốn đăng xuất khỏi trang Quản trị?")) {
    localStorage.removeItem('user');
    router.push('/login');
  }
};
</script>

<style scoped>
.admin-nav-item {
  color: #adb5bd;
  padding: 12px 15px;
  border-radius: 10px;
  transition: all 0.3s ease;
  font-weight: 500;
}

.admin-nav-item:hover {
  background: rgba(255, 255, 255, 0.1);
  color: white;
}

.router-link-active {
  background: #dc3545 !important;
  color: white !important;
  box-shadow: 0 4px 15px rgba(220, 53, 69, 0.3);
}

.fade-enter-active, .fade-leave-active { transition: opacity 0.2s ease; }
.fade-enter-from, .fade-leave-to { opacity: 0; }
</style>