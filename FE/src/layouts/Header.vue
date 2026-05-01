<template>
  <div class="header-sticky shadow-sm">
    <header
      class="d-flex align-items-center justify-content-between border-bottom py-3 px-3 px-md-5 mb-0 bg-white"
    >
      <div
        class="fs-3 fw-bold text-dark"
        style="cursor: pointer"
        @click="router.push('/')"
      >
        Old2New
      </div>

      <div class="input-group w-50 d-none d-md-flex">
        <input
          type="text"
          class="form-control"
          v-model="searchQuery"
          @keyup.enter="handleSearch"
          placeholder="Tìm kiếm sản phẩm bạn cần..."
        />
        <button
          class="btn btn-outline-secondary bg-light"
          type="button"
          @click="handleSearch"
        >
          🔍
        </button>
      </div>

      <div class="d-flex align-items-center gap-3 gap-md-4">
        <div v-if="currentUser" class="user-profile-dropdown position-relative">
          <div class="d-flex align-items-center gap-2" style="cursor: pointer">
            <img
              v-if="currentUser.anhDaiDien"
              :src="currentUser.anhDaiDien"
              class="rounded-circle object-fit-cover shadow-sm border"
              style="width: 32px; height: 32px"
              alt="Avatar"
            />

            <div
              v-else
              class="rounded-circle bg-dark text-white d-flex align-items-center justify-content-center fw-bold shadow-sm"
              style="width: 32px; height: 32px"
            >
              {{
                currentUser.hoVaTen
                  ? currentUser.hoVaTen.charAt(0).toUpperCase()
                  : "U"
              }}
            </div>

            <span
              class="fw-semibold text-dark d-none d-md-block"
              style="
                max-width: 120px;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
              "
            >
              {{ currentUser.hoVaTen || currentUser.email }}
            </span>
            <span class="small text-muted">▾</span>
          </div>

          <ul class="dropdown-menu shadow custom-user-menu rounded">
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

        <router-link
          to="/cart"
          class="text-dark text-decoration-none fs-5 position-relative"
        >
          🛒
        </router-link>
        <a href="#" class="text-dark text-decoration-none fs-5">🔔</a>
        <button
          @click="router.push('/dang-ban')"
          class="btn btn-dark btn-sm px-3 fw-bold"
        >
          Đăng bán
        </button>
      </div>
    </header>

    <nav
      class="container d-none d-md-flex align-items-center border-bottom pb-2 pt-2 position-relative bg-white"
    >
      <div class="all-category-wrapper me-4">
        <div
          class="category-btn d-flex align-items-center gap-2 text-dark"
          style="cursor: pointer; padding: 10px 0"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="18"
            height="18"
            fill="currentColor"
            class="bi bi-grid-fill text-muted"
            viewBox="0 0 16 16"
          >
            <path
              d="M1 2.5A1.5 1.5 0 0 1 2.5 1h3A1.5 1.5 0 0 1 7 2.5v3A1.5 1.5 0 0 1 5.5 7h-3A1.5 1.5 0 0 1 1 5.5v-3zm8 0A1.5 1.5 0 0 1 10.5 1h3A1.5 1.5 0 0 1 15 2.5v3A1.5 1.5 0 0 1 13.5 7h-3A1.5 1.5 0 0 1 9 5.5v-3zm-8 8A1.5 1.5 0 0 1 2.5 9h3A1.5 1.5 0 0 1 7 10.5v3A1.5 1.5 0 0 1 5.5 15h-3A1.5 1.5 0 0 1 1 13.5v-3zm8 0A1.5 1.5 0 0 1 10.5 9h3a1.5 1.5 0 0 1 1.5 1.5v3a1.5 1.5 0 0 1-1.5 1.5h-3A1.5 1.5 0 0 1 9 13.5v-3z"
            />
          </svg>
          <span class="fw-semibold">Tất cả danh mục</span>
          <span class="small text-muted ms-1">▾</span>
        </div>
        <!-- Cây Danh Mục  -->
        <ul class="main-menu shadow-sm rounded-bottom">
          <li
            v-for="category in categories"
            :key="category.id"
            class="menu-item border-bottom-dashed"
          >
            <a
              href="#"
              @click.prevent="goToCategory(category.id)"
              class="d-flex justify-content-between align-items-center text-decoration-none py-2 px-3"
            >
              {{ category.name }}
              <span
                v-if="category.children && category.children.length > 0"
                class="text-muted small"
                >›</span
              >
            </a>

            <ul
              class="sub-menu shadow-sm"
              v-if="category.children && category.children.length > 0"
            >
              <li
                v-for="child in category.children"
                :key="child.id"
                class="menu-item-child"
              >
                <a
                  href="#"
                  @click.prevent="goToCategory(child.id)"
                  class="d-flex justify-content-between align-items-center text-decoration-none py-2 px-3"
                >
                  {{ child.name }}
                  <span
                    v-if="child.children && child.children.length > 0"
                    class="text-muted small"
                    >›</span
                  >
                </a>

                <ul
                  class="sub-menu-3 shadow-sm"
                  v-if="child.children && child.children.length > 0"
                >
                  <li
                    v-for="grandchild in child.children"
                    :key="grandchild.id"
                    class="menu-item-grandchild"
                  >
                    <a
                      href="#"
                      @click.prevent="goToCategory(grandchild.id)"
                      class="d-flex justify-content-between align-items-center text-decoration-none py-2 px-3"
                    >
                      {{ grandchild.name }}
                      <span
                        v-if="
                          grandchild.children && grandchild.children.length > 0
                        "
                        class="text-muted small"
                        >›</span
                      >
                    </a>

                    <ul
                      class="sub-menu-4 shadow-sm"
                      v-if="
                        grandchild.children && grandchild.children.length > 0
                      "
                    >
                      <li
                        v-for="greatgrandchild in grandchild.children"
                        :key="greatgrandchild.id"
                      >
                        <a
                          href="#"
                          @click.prevent="goToCategory(greatgrandchild.id)"
                          class="text-decoration-none py-2 px-3 d-block"
                          >{{ greatgrandchild.name }}</a
                        >
                      </li>
                    </ul>
                  </li>
                </ul>
              </li>
            </ul>
          </li>
        </ul>
      </div>

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
            class="dropdown-menu shadow-sm custom-dropdown-menu rounded-0 border-top-orange"
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
                class="dropdown-menu shadow-sm custom-sub-dropdown-menu rounded-0"
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
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";

const router = useRouter();
const searchQuery = ref("");
const categories = ref([]);
const currentUser = ref(null);

onMounted(() => {
  fetchCategories();

  const storedUser = localStorage.getItem("user");
  if (storedUser) {
    currentUser.value = JSON.parse(storedUser);
  }
});

const logout = () => {
  localStorage.removeItem("user");
  currentUser.value = null;
  alert("Bạn đã đăng xuất thành công!");
  router.push("/");
};

const handleSearch = () => {
  if (searchQuery.value.trim() !== "")
    alert("Đang tìm kiếm: " + searchQuery.value);
};

const fetchCategories = async () => {
  try {
    const response = await axios.get(
      "http://localhost:8080/api/categories/tree",
    );
    categories.value = response.data;
    // console.log("KIỂM TRA CÂY DANH MỤC:", JSON.stringify(categories.value, null, 2));
  } catch (error) {
    console.error("Lỗi tải Danh mục:", error);
  }
};

//  Hàm xử lý khi bấm vào 1 danh mục
const goToCategory = (id) => {
  // Đẩy ID danh mục lên thanh URL (VD: localhost:5173/?category=5)
  router.push({ path: "/", query: { category: id } });
};
</script>

<style scoped>
.header-sticky {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  width: 100%;
  z-index: 1100;
  background-color: white;
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
  border: none;
  z-index: 1050;
  animation: fadeIn 0.2s ease;
}
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

/* MENU TẤT CẢ DANH MỤC */
.all-category-wrapper {
  position: relative;
}
.all-category-wrapper:hover .main-menu {
  display: block;
  animation: fadeIn 0.2s ease;
}

.main-menu {
  display: none;
  position: absolute;
  top: 100%;
  left: 0;
  margin: 0;
  padding: 0;
  background-color: white;
  border: 1px solid #eee;
  border-top: none;
  min-width: 250px;
  list-style: none;
  z-index: 1050;
  border-radius: 0 0 8px 8px;
}
.border-bottom-dashed {
  border-bottom: 1px dashed #f0f0f0;
}
.menu-item {
  position: relative;
}
.menu-item > a {
  color: #444;
  font-size: 0.95rem;
}
.menu-item:hover > a {
  background-color: #fcfcfc;
  color: #007bff;
}

.sub-menu {
  display: none;
  position: absolute;
  top: 0;
  left: 100%;
  margin: 0;
  padding: 0;
  background-color: white;
  border: 1px solid #eee;
  min-width: 240px;
  min-height: 100%;
  list-style: none;
  z-index: 1060;
}
.menu-item:hover .sub-menu {
  display: block;
}
.menu-item-child {
  position: relative;
}
.menu-item-child > a {
  color: #555;
  font-size: 0.9rem;
  border-bottom: 1px solid #f9f9f9;
}
.menu-item-child:hover > a {
  background-color: #f8f9fa;
  color: #007bff;
}

.sub-menu-3 {
  display: none;
  position: absolute;
  top: 0;
  left: 100%;
  margin: 0;
  padding: 0;
  background-color: white;
  border: 1px solid #eee;
  min-width: 220px;
  min-height: 100%;
  list-style: none;
  z-index: 1070;
}
.menu-item-child:hover .sub-menu-3 {
  display: block;
}
.menu-item-grandchild {
  position: relative;
}
.menu-item-grandchild > a {
  color: #666;
  font-size: 0.85rem;
  border-bottom: 1px solid #f9f9f9;
}
.menu-item-grandchild > a:hover {
  color: #007bff;
  background-color: #f8f9fa;
}

/* CSS TẦNG 4 MỚI THÊM */
.sub-menu-4 {
  display: none;
  position: absolute;
  top: 0;
  left: 100%;
  margin: 0;
  padding: 0;
  background-color: white;
  border: 1px solid #eee;
  min-width: 220px;
  min-height: 100%;
  list-style: none;
  z-index: 1080;
}
.menu-item-grandchild:hover .sub-menu-4 {
  display: block;
}
.sub-menu-4 a {
  color: #777;
  font-size: 0.8rem;
  border-bottom: 1px solid #f9f9f9;
}
.sub-menu-4 a:hover {
  color: #007bff;
  background-color: #f8f9fa;
}

/* MENU NGANG LIÊN KẾT NHANH */
.quick-links a {
  color: #555;
  font-size: 0.95rem;
  transition: color 0.2s;
}
.hover-orange:hover {
  color: #007bff !important;
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
  border: none;
  min-width: 200px;
  z-index: 1000;
  padding: 0;
}
.border-top-orange {
  border-top: 3px solid #007bff !important;
}

/* CSS TẦNG 3 MENU NGANG MỚI THÊM */
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
  border: none;
  min-width: 200px;
  z-index: 1010;
  padding: 0;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(5px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* --- HIỆU ỨNG DI CHUỘT CHO TẦNG 3 ĐỂ HIỆN TẦNG 4 --- */
.menu-item-grandchild {
  position: relative;
}

/* Khi di chuột vào mục ở tầng 3, tầng 4 sẽ hiện ra */
.menu-item-grandchild:hover > .sub-menu-4 {
  display: block !important;
  animation: fadeIn 0.2s ease;
}

/* Style cho khung menu tầng 4 */
.sub-menu-4 {
  display: none;
  position: absolute;
  top: 0;
  left: 100%; /* Đẩy sang bên phải của tầng 3 */
  margin: 0;
  padding: 0;
  background-color: white;
  border: 1px solid #eee;
  min-width: 220px;
  min-height: 100%;
  list-style: none;
  z-index: 1080;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.sub-menu-4 a {
  color: #777;
  font-size: 0.85rem;
  border-bottom: 1px solid #f9f9f9;
}

.sub-menu-4 a:hover {
  color: #007bff !important;
  background-color: #f8f9fa;
}
</style>
