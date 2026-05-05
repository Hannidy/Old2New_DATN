<template>
  <div class="bg-white rounded shadow-sm p-3 border-0">
    <!-- KHU VỰC AVATAR -->
    <div class="d-flex align-items-center gap-3 mb-4 border-bottom pb-3">
      <div class="position-relative">
        <img
          v-if="currentUser.anhDaiDien"
          :src="currentUser.anhDaiDien"
          class="avatar-circle object-fit-cover shadow-sm"
        />
        <div
          v-else
          class="avatar-circle bg-dark text-white fw-bold fs-4 d-flex align-items-center justify-content-center shadow-sm"
        >
          {{
            currentUser.hoVaTen
              ? currentUser.hoVaTen.charAt(0).toUpperCase()
              : "U"
          }}
        </div>
      </div>

      <!-- Thông tin User -->
      <div>
        <div class="fw-bold text-dark text-truncate" style="max-width: 150px">
          {{ currentUser.hoVaTen || "Khách hàng" }}
        </div>
        <div class="small text-muted text-truncate" style="max-width: 150px">
          <span class="text-secondary">Thành viên Old2New</span>
        </div>
      </div>
    </div>

    <!-- DANH SÁCH MENU ĐÃ ĐƯỢC PHÂN NHÓM & THAY ICON -->
    <div class="menu-list">
      <!-- NHÓM TÀI KHOẢN -->
      <div class="menu-group-title">Tài khoản</div>
      <div
        :class="[
          'menu-item d-flex align-items-center',
          activeTab === 'profile' ? 'active' : '',
        ]"
        @click="navigate('profile', '/profile')"
      >
        <i class="bi bi-person me-2 fs-5 icon"></i> Hồ sơ của tôi
      </div>

      <!-- NHÓM MUA HÀNG -->
      <div class="menu-group-title mt-3">Mua hàng</div>
      <div
        :class="[
          'menu-item d-flex align-items-center',
          activeTab === 'orders' ? 'active' : '',
        ]"
        @click="navigate('orders', '/order-management')"
      >
        <i class="bi bi-cart2 me-2 fs-5 icon"></i> Quản lý đơn mua
      </div>

      <!-- NHÓM BÁN HÀNG -->
      <div class="menu-group-title mt-3">Bán hàng</div>
      <div
        :class="[
          'menu-item d-flex align-items-center',
          activeTab === 'shop' ? 'active' : '',
        ]"
        @click="navigate('shop', '/shop-profile')"
      >
        <i class="bi bi-shop me-2 fs-5 icon"></i> Hồ sơ cửa hàng
      </div>
      <div
        :class="[
          'menu-item d-flex align-items-center',
          activeTab === 'products' ? 'active' : '',
        ]"
        @click="navigate('products', '/product-management')"
      >
        <i class="bi bi-card-list me-2 fs-5 icon"></i> Quản lý sản phẩm
      </div>
      <div
        :class="[
          'menu-item d-flex align-items-center',
          activeTab === 'sales-orders' ? 'active' : '',
        ]"
        @click="navigate('sales-orders', '/sales-order-management')"
      >
        <i class="bi bi-graph-up me-2 fs-5 icon"></i> Quản lý đơn bán
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";

const props = defineProps({
  activeTab: { type: String, default: "profile" },
});

const emit = defineEmits(["changeTab"]);
const router = useRouter();
const currentUser = ref({});

onMounted(() => {
  const storedUser = localStorage.getItem("user");
  if (storedUser) currentUser.value = JSON.parse(storedUser);
});

// Hàm điều hướng
const navigate = (tabName, path) => {
  emit("changeTab", tabName);
  router.push(path);
};
</script>

<style scoped>
.avatar-circle {
  width: 50px;
  height: 50px;
  border-radius: 50%;
}
.menu-list {
  display: flex;
  flex-direction: column;
  gap: 4px;
}
.menu-group-title {
  font-size: 0.8rem;
  font-weight: 700;
  color: #888;
  text-transform: uppercase;
  padding: 0 15px;
  margin-bottom: 4px;
  letter-spacing: 0.5px;
}
.menu-item {
  padding: 10px 15px;
  border-radius: 6px;
  cursor: pointer;
  color: #444;
  transition: all 0.2s ease;
  font-weight: 500;
  font-size: 0.95rem;
}
.menu-item .icon {
  color: #757575; /* Màu icon mặc định */
  transition: color 0.2s ease;
}
.menu-item:hover {
  background-color: #f8f9fa;
  color: #007bff;
}
.menu-item:hover .icon {
  color: #007bff;
}
.menu-item.active {
  color: #ee4d2d;
  font-weight: bold;
  background-color: #fff5f5;
  border-left: 4px solid #ee4d2d;
}
.menu-item.active .icon {
  color: #ee4d2d;
}
</style>
