<template>
  <!-- Khung Header cố định trên cùng -->
  <div class="header-wrapper shadow-sm" :class="{ 'is-scrolled': isScrolled }">
    <!-- ================================================== -->
    <!-- 1. MAIN BAR (Thanh tìm kiếm & Icon trải rộng 2 bên) -->
    <!-- ================================================== -->
    <header class="main-bar py-3 bg-white transition-all border-bottom">
      <!-- Dùng container-fluid và px-5 để thanh này cách 2 lề một khoảng nhỏ -->
      <div
        class="container-fluid px-3 px-md-5 d-flex align-items-center justify-content-between"
      >
        <!-- LOGO -->
        <div class="d-flex align-items-center" style="min-width: 200px">
          <div
            class="fs-3 fw-bold text-black logo-text"
            style="cursor: pointer"
            @click="goToHomeAndScrollTop"
          >
            Old2New
          </div>

          <!-- DANH MỤC THU GỌN KHI SCROLL -->
          <div
            v-if="isScrolled"
            class="all-category-wrapper d-none d-md-block ms-4"
          >
            <div
              class="category-btn d-flex align-items-center gap-2 text-dark"
              style="cursor: pointer"
            >
              <i class="bi bi-grid-fill text-muted fs-5"></i>
              <span class="fw-semibold">Tất cả danh mục</span>
              <i class="bi bi-chevron-down small text-muted"></i>
            </div>
            <CategoryDropdownMenu
              :categories="categories"
              @select="goToCategory"
            />
          </div>
        </div>

        <!-- THANH TÌM KIẾM (Căn giữa) -->
        <div
          class="input-group search-input-group d-none d-md-flex transition-all mx-4"
          :class="isScrolled ? 'w-40' : 'w-50'"
        >
          <input
            type="text"
            class="form-control border-end-0 py-2 px-3"
            v-model="searchQuery"
            @keyup.enter="handleSearch"
            placeholder="Tìm kiếm sản phẩm bạn cần..."
          />
          <button
            class="btn border border-start-0 px-3 bg-light"
            type="button"
            @click="handleSearch"
          >
            <i class="bi bi-search text-primary"></i>
          </button>
        </div>

        <!-- CÁC NÚT TƯƠNG TÁC BÊN PHẢI -->
        <div
          class="d-flex align-items-center justify-content-end gap-3 gap-md-4"
          style="min-width: 200px"
        >
          <!-- THÔNG TIN USER -->
          <div
            v-if="currentUser"
            class="user-profile-dropdown position-relative"
          >
            <div
              class="d-flex align-items-center gap-2"
              style="cursor: pointer"
            >
              <img
                v-if="currentUser.anhDaiDien"
                :src="currentUser.anhDaiDien"
                class="rounded-circle object-fit-cover border"
                style="width: 32px; height: 32px"
              />
              <div
                v-else
                class="rounded-circle bg-dark text-white d-flex align-items-center justify-content-center fw-bold"
                style="width: 32px; height: 32px"
              >
                {{
                  currentUser.hoVaTen
                    ? currentUser.hoVaTen.charAt(0).toUpperCase()
                    : "U"
                }}
              </div>

              <!-- Tên User sẽ bị ẩn đi khi cuộn xuống để tiết kiệm diện tích -->
              <span
                v-show="!isScrolled"
                class="fw-semibold text-dark d-none d-lg-block text-truncate"
                style="max-width: 120px"
              >
                {{ currentUser.hoVaTen || currentUser.email }}
              </span>
              <i
                class="bi bi-caret-down-fill small text-muted d-none d-lg-block"
                style="font-size: 10px"
              ></i>
            </div>

            <ul class="dropdown-menu shadow custom-user-menu rounded border-0">
              <li class="px-3 py-2 border-bottom bg-light">
                <div class="fw-bold text-dark">
                  {{ currentUser.hoVaTen || "Khách hàng" }}
                </div>
                <div
                  class="small text-muted text-truncate"
                  style="max-width: 180px"
                >
                  {{ currentUser.email }}
                </div>
              </li>
              <li>
                <router-link to="/profile" class="dropdown-item py-2 mt-1"
                  >👤 Hồ sơ của tôi</router-link
                >
              </li>
              <li>
                <router-link to="/shop-profile" class="dropdown-item py-2"
                  >🏪 Hồ sơ cửa hàng</router-link
                >
              </li>
              <li>
                <router-link to="/quan-ly-don-hang" class="dropdown-item py-2"
                  >📦 Đơn hàng của tôi</router-link
                >
              </li>
              <li>
                <router-link to="/quan-ly-don-ban" class="dropdown-item py-2"
                  >🧾 Đơn khách đặt mua</router-link
                >
              </li>
              <li v-if="currentUser.vaiTroId === 1">
                <router-link
                  to="/admin/categories"
                  class="dropdown-item py-2 text-primary fw-bold"
                  >⚙️ Quản trị hệ thống</router-link
                >
              </li>
              <li><hr class="dropdown-divider my-1" /></li>
              <li>
                <button
                  @click="logout"
                  class="dropdown-item text-danger py-2 fw-bold"
                >
                  🚪 Đăng xuất
                </button>
              </li>
            </ul>
          </div>

          <router-link
            v-else
            to="/login"
            class="btn btn-outline-dark btn-sm d-none d-md-inline-block fw-bold"
            >Đăng nhập</router-link
          >

          <!-- GIỎ HÀNG -->
          <router-link
            to="/cart"
            class="text-muted text-decoration-none position-relative"
          >
            <i class="bi bi-cart3 fs-4"></i>
          </router-link>

          <!-- THÔNG BÁO -->
          <div
            v-if="currentUser"
            class="notification-dropdown dropdown position-relative"
          >
            <div
              class="text-decoration-none position-relative"
              style="cursor: pointer"
              data-bs-toggle="dropdown"
            >
              <i class="bi bi-bell-fill text-warning fs-4"></i>
              <span
                v-if="unreadCount > 0"
                class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger"
                style="font-size: 0.65rem"
              >
                {{ unreadCount > 99 ? "99+" : unreadCount }}
              </span>
            </div>

            <div
              class="dropdown-menu dropdown-menu-end shadow custom-noti-menu rounded p-0 border-0"
            >
              <div
                class="p-3 border-bottom bg-light fw-bold text-dark d-flex justify-content-between align-items-center"
              >
                <span>Thông báo mới nhận</span>
              </div>

              <div
                class="noti-list"
                style="max-height: 400px; overflow-y: auto"
              >
                <div
                  v-if="notifications.length === 0"
                  class="p-4 text-center text-muted small"
                >
                  <i class="bi bi-bell-slash fs-1 d-block mb-2"></i> Chưa có
                  thông báo nào
                </div>

                <div
                  v-for="noti in notifications"
                  :key="noti.id"
                  class="noti-item p-3 border-bottom d-flex gap-3 position-relative"
                  :class="{ 'bg-light-subtle': !noti.daDoc }"
                  @click="handleNotiClick(noti)"
                  style="cursor: pointer; transition: background 0.2s"
                >
                  <div class="fs-3">
                    <span v-if="noti.loai === 'ORDER_BUYER'">🛍️</span>
                    <span v-else-if="noti.loai === 'ORDER_SELLER'">💰</span>
                    <span v-else-if="noti.loai === 'WALLET'">💳</span>
                    <span v-else>📌</span>
                  </div>
                  <div class="flex-grow-1">
                    <div
                      class="fw-bold text-dark mb-1"
                      :class="{ 'text-primary': !noti.daDoc }"
                    >
                      {{ noti.tieuDe }}
                    </div>
                    <div class="small text-muted mb-2" style="line-height: 1.4">
                      {{ noti.noiDung }}
                    </div>
                    <div class="small text-black-50" style="font-size: 0.75rem">
                      {{ formatTime(noti.ngayTao) }}
                    </div>
                  </div>
                  <div
                    v-if="!noti.daDoc"
                    class="position-absolute rounded-circle bg-primary"
                    style="width: 8px; height: 8px; top: 15px; right: 15px"
                  ></div>
                </div>
              </div>
              <div class="p-2 text-center border-top bg-light">
                <a href="#" class="text-decoration-none small text-muted"
                  >Xem tất cả</a
                >
              </div>
            </div>
          </div>

          <!-- NÚT ĐĂNG BÁN -->
          <button
            @click="router.push('/dang-ban')"
            class="btn btn-dark text-white fw-bold px-4 py-2 rounded-1 shadow-sm d-none d-md-block"
          >
            Đăng bán
          </button>
        </div>
      </div>
    </header>

    <!-- ================================================== -->
    <!-- 2. BOTTOM BAR (Danh mục thu gọn vào giữa) -->
    <!-- ================================================== -->
    <div
      class="bg-white transition-all nav-bottom-bar"
      :class="
        isScrolled
          ? 'h-0 overflow-hidden py-0 border-0'
          : 'pb-2 pt-2 border-bottom shadow-sm'
      "
    >
      <!-- Dùng .container để ôm vừa vặn khu vực nội dung ở dưới -->
      <nav class="container d-none d-md-flex align-items-center px-2 px-md-0">
        <!-- DANH MỤC Ở CHẾ ĐỘ FULL (Đứng đầu hàng) -->
        <div class="all-category-wrapper me-4">
          <div
            class="category-btn d-flex align-items-center gap-2 text-dark"
            style="cursor: pointer; padding: 10px 0"
          >
            <i class="bi bi-grid-fill text-muted fs-5"></i>
            <span class="fw-semibold">Tất cả danh mục</span>
            <i
              class="bi bi-caret-down-fill small text-muted"
              style="font-size: 10px"
            ></i>
          </div>
          <!-- Gọi Component con -->
          <CategoryDropdownMenu
            :categories="categories"
            @select="goToCategory"
          />
        </div>

        <!-- CÁC DANH MỤC TRẢI NGANG THƯỜNG DÙNG -->
        <div
          class="d-flex flex-wrap gap-4 quick-links flex-grow-1 align-items-center h-100"
        >
          <div
            v-for="cat in categories.slice(0, 8)"
            :key="'nav-' + cat.id"
            class="nav-item dropdown custom-dropdown position-relative h-100"
          >
            <a
              href="#"
              @click.prevent="goToCategory(cat.id)"
              class="text-dark text-decoration-none py-2 d-inline-block hover-orange"
            >
              {{ cat.name }}
            </a>
            <ul
              class="dropdown-menu shadow-sm custom-dropdown-menu rounded-0 border-top-orange border-0"
              v-if="cat.children && cat.children.length > 0"
            >
              <li
                v-for="child in cat.children"
                :key="'nav-child-' + child.id"
                class="position-relative child-dropdown-item"
              >
                <a
                  class="dropdown-item py-2 d-flex justify-content-between align-items-center"
                  href="#"
                  @click.prevent="goToCategory(child.id)"
                >
                  {{ child.name }}
                  <span
                    v-if="child.children && child.children.length > 0"
                    class="text-muted small"
                    >›</span
                  >
                </a>
                <ul
                  class="dropdown-menu shadow-sm custom-sub-dropdown-menu rounded-0 border-0"
                  v-if="child.children && child.children.length > 0"
                >
                  <li
                    v-for="grandchild in child.children"
                    :key="'nav-grandchild-' + grandchild.id"
                  >
                    <a
                      class="dropdown-item py-2"
                      href="#"
                      @click.prevent="goToCategory(grandchild.id)"
                      >{{ grandchild.name }}</a
                    >
                  </li>
                </ul>
              </li>
            </ul>
          </div>
        </div>
      </nav>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, onUnmounted } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import CategoryDropdownMenu from "./CategoryDropdownMenu.vue";

const router = useRouter();
const searchQuery = ref("");
const categories = ref([]);
const currentUser = ref(null);
const notifications = ref([]);
let notiTimer = null;

const isScrolled = ref(false);

const handleScroll = () => {
  isScrolled.value = window.scrollY > 50;
};

const goToHomeAndScrollTop = () => {
  router.push("/").then(() => {
    window.scrollTo({ top: 0, behavior: "smooth" });
  });
};

const unreadCount = computed(
  () => notifications.value.filter((n) => !n.daDoc).length,
);

const formatTime = (dateString) => {
  if (!dateString) return "";
  const date = new Date(dateString);
  return date.toLocaleString("vi-VN", {
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
  });
};

const fetchNotifications = async () => {
  if (!currentUser.value) return;
  const userId = currentUser.value.id || currentUser.value.nguoiDungId;
  try {
    const response = await axios.get(
      `http://localhost:8080/api/notifications/${userId}`,
    );
    notifications.value = response.data || [];
  } catch (error) {
    console.error("Lỗi lấy thông báo:", error);
  }
};

const handleNotiClick = async (noti) => {
  const userId = currentUser.value.id || currentUser.value.nguoiDungId;
  if (!noti.daDoc) {
    try {
      await axios.put(
        `http://localhost:8080/api/notifications/${userId}/read/${noti.id}`,
      );
      noti.daDoc = true;
    } catch (error) {
      console.error("Lỗi cập nhật:", error);
    }
  }
  if (noti.duongDan) router.push(noti.duongDan);
};

const logout = () => {
  localStorage.removeItem("user");
  currentUser.value = null;
  router.push("/");
};

const handleSearch = () => {
  const query = searchQuery.value.trim();
  router.push({ path: "/", query: { search: query } });
};

const fetchCategories = async () => {
  try {
    const response = await axios.get(
      "http://localhost:8080/api/categories/tree",
    );
    categories.value = response.data;
  } catch (error) {
    console.error("Lỗi tải Danh mục:", error);
  }
};

const goToCategory = (id) => {
  router.push({ path: "/", query: { category: id } });
};

onMounted(() => {
  fetchCategories();
  window.addEventListener("scroll", handleScroll);

  const storedUser = localStorage.getItem("user");
  if (storedUser) {
    currentUser.value = JSON.parse(storedUser);
    fetchNotifications();
    notiTimer = setInterval(() => {
      fetchNotifications();
    }, 30000);
  }
});

onUnmounted(() => {
  window.removeEventListener("scroll", handleScroll);
  if (notiTimer) clearInterval(notiTimer);
});
</script>

<style scoped>
.header-wrapper {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  width: 100%;
  z-index: 1100;
  background-color: white;
  transition: all 0.3s ease-in-out;
}

.transition-all {
  transition: all 0.3s ease-in-out;
}

.h-0 {
  height: 0 !important;
  opacity: 0;
  visibility: hidden;
}

.w-40 {
  width: 40% !important;
}

.search-input-group .form-control:focus {
  box-shadow: none;
  border: 1px solid #ced4da !important;
}

.all-category-wrapper {
  position: relative;
}
.all-category-wrapper:hover .main-menu {
  display: block;
  animation: fadeIn 0.2s ease;
}

.quick-links a {
  color: #555;
  font-size: 0.95rem;
  transition: color 0.2s;
}
.hover-orange:hover {
  color: #f26522 !important;
}

.custom-dropdown:hover .custom-dropdown-menu {
  display: block;
  animation: fadeIn 0.2s ease;
}
.custom-dropdown-menu {
  display: none;
  position: absolute;
  top: 100%;
  left: 0;
  margin-top: 0;
  min-width: 200px;
  z-index: 1000;
  padding: 0;
}
.border-top-orange {
  border-top: 3px solid #f26522 !important;
}
.child-dropdown-item:hover .custom-sub-dropdown-menu {
  display: block;
  animation: fadeIn 0.2s ease;
}
.custom-sub-dropdown-menu {
  display: none;
  position: absolute;
  top: 0;
  left: 100%;
  margin-top: 0;
  min-width: 200px;
  z-index: 1010;
  padding: 0;
}

.user-profile-dropdown {
  padding: 10px 0;
}
.custom-user-menu {
  display: none;
  position: absolute;
  top: 100%;
  right: -20px;
  margin-top: -5px;
  min-width: 220px;
  z-index: 1050;
  animation: fadeIn 0.2s ease;
}
.user-profile-dropdown:hover .custom-user-menu {
  display: block;
}
.notification-dropdown:hover .custom-noti-menu {
  display: block;
}
.custom-noti-menu {
  display: none;
  position: absolute;
  top: 100%;
  right: -20px;
  width: 350px;
  z-index: 1050;
  animation: fadeIn 0.2s ease;
}
.noti-list {
  background-color: white;
}
.noti-item:hover {
  background-color: #f8f9fa !important;
}
.bg-light-subtle {
  background-color: #fff4f1 !important;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
