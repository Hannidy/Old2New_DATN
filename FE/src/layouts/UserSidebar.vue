<template>
  <div class="bg-white rounded shadow-sm p-3">
    <!-- KHU VỰC AVATAR (Chỉ hiển thị, không click đổi ảnh được nữa) -->
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

    <!-- DANH SÁCH MENU -->
    <div class="menu-list">
      <div
        :class="['menu-item', activeTab === 'profile' ? 'active' : '']"
        @click="navigate('profile', '/profile')"
      >
        <span class="icon">👤</span> Hồ sơ của tôi
      </div>
      <div
        :class="['menu-item', activeTab === 'shop' ? 'active' : '']"
        @click="navigate('shop', '/shop-profile')"
      >
        <span class="icon">🏪</span> Hồ sơ Shop
      </div>
      <div
        :class="['menu-item', activeTab === 'products' ? 'active' : '']"
        @click="navigate('products', '/my-products')"
      >
        <span class="icon">📋</span> Tất cả sản phẩm
      </div>
      <div
        :class="['menu-item', activeTab === 'orders' ? 'active' : '']"
        @click="navigate('orders', '/quan-ly-don-hang')"
      >
        <span class="icon">📦</span> Đơn mua
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
  gap: 10px;
}
.menu-item {
  padding: 10px 15px;
  border-radius: 6px;
  cursor: pointer;
  color: #555;
  transition: all 0.2s ease;
  font-weight: 500;
}
.menu-item .icon {
  margin-right: 8px;
}
.menu-item:hover {
  background-color: #f8f9fa;
  color: #007bff;
}
.menu-item.active {
  color: #ee4d2d;
  font-weight: bold;
  background-color: #fff5f5;
  border-left: 4px solid #ee4d2d;
}
</style>
