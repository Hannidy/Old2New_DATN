<template>
  <div class="bg-light min-vh-100">
    <!-- <AppHeader /> -->

    <main class="container py-5" style="margin-top: 80px">
      <div class="d-flex align-items-center justify-content-between mb-4">
        <nav aria-label="breadcrumb">
          <ol class="breadcrumb mb-0 bg-transparent p-0">
            <li class="breadcrumb-item">
              <router-link to="/" class="text-decoration-none text-muted">
                <i class="bi bi-house-door"></i> Trang chủ
              </router-link>
            </li>
            <li class="breadcrumb-item active text-danger" aria-current="page">
              Sản phẩm của tôi
            </li>
          </ol>
        </nav>

        <button
          @click="router.push('/')"
          class="btn btn-sm btn-outline-secondary rounded-pill px-3 shadow-sm"
        >
          <i class="bi bi-arrow-left"></i> Quay lại trang chủ
        </button>
      </div>

      <div class="row">
        <div class="col-md-3 mb-4">
          <UserSidebar activeTab="products" />
        </div>

        <div class="col-md-9">
          <div class="bg-white rounded shadow-sm p-4 p-md-5">
            <div class="border-bottom pb-3 mb-4">
              <h2 class="h4 fw-bold mb-1 text-dark">Quản lý sản phẩm</h2>
              <p class="text-muted small mb-0">
                Danh sách các sản phẩm bạn đã đăng bán
              </p>
            </div>

            <div v-if="isLoading" class="text-center py-5">
              <div class="spinner-border text-primary" role="status"></div>
            </div>

            <div v-else-if="products.length > 0">
              <div class="border rounded overflow-hidden">
                <div class="table-responsive">
                  <table class="table table-hover align-middle mb-0">
                    <thead class="bg-light text-secondary">
                      <tr>
                        <th class="ps-4 py-3 small text-uppercase">Sản phẩm</th>
                        <th class="small text-uppercase">Danh mục</th>
                        <th class="small text-uppercase">Giá bán</th>
                        <th class="text-end pe-4 small text-uppercase">Thao tác</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr
                        v-for="p in products"
                        :key="p.id"
                        :class="{
                          'opacity-50 bg-light': p.trangThai === 'AN_TIN',
                          'bg-warning-subtle': p.trangThai === 'CHO_DUYET',
                          'transition-all': true,
                        }"
                      >
                        <td class="ps-4 py-3">
                          <div class="d-flex align-items-center gap-3">
                            <img
                              :src="getImageUrl(p.hinhAnh)"
                              @error="handleImageError"
                              class="rounded border object-fit-cover shadow-sm bg-secondary bg-opacity-10"
                              :class="{ grayscale: p.trangThai === 'AN_TIN' }"
                              style="width: 60px; height: 60px; font-size: 10px; color: #6c757d; text-align: center; line-height: 60px;"
                              alt="Ảnh SP"
                            />
                            <div>
                              <div class="fw-bold text-dark text-truncate" style="max-width: 250px">
                                {{ p.tenSanPham }}
                              </div>
                              <small class="text-muted">
                                ID: #{{ p.id }} |
                                <span :class="getStatusClass(p.trangThai)">
                                  {{ getStatusText(p.trangThai) }}
                                </span>
                              </small>
                            </div>
                          </div>
                        </td>

                        <td>
                          <span class="badge bg-light text-dark border">{{ p.danhMuc }}</span>
                        </td>
                        <td>
                          <span class="text-danger fw-bold">{{ formatCurrency(p.gia) }}</span>
                        </td>

                        <td class="text-end pe-4">
                          <div class="btn-group shadow-sm">
                            <button
                              v-if="p.trangThai !== 'CHO_DUYET' && p.trangThai !== 'BI_TU_CHOI'"
                              class="btn btn-sm"
                              :class="p.trangThai === 'AN_TIN' ? 'btn-outline-success' : 'btn-outline-warning'"
                              @click="handleToggleHide(p)"
                            >
                              <i class="bi" :class="p.trangThai === 'AN_TIN' ? 'bi-eye' : 'bi-eye-slash'"></i>
                              {{ p.trangThai === "AN_TIN" ? "Hiện tin" : "Ẩn tin" }}
                            </button>

                            <button
                              v-else-if="p.trangThai === 'CHO_DUYET'"
                              class="btn btn-sm btn-outline-secondary disabled"
                            >
                              <i class="bi bi-clock-history"></i> Đang chờ duyệt
                            </button>

                            <button
                              class="btn btn-sm btn-outline-danger"
                              @click="handleDelete(p.id)"
                            >
                              <i class="bi bi-trash"></i> Xóa
                            </button>
                          </div>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>

              <!-- GIAO DIỆN PHÂN TRANG -->
              <div v-if="totalPages > 1" class="d-flex justify-content-end mt-4">
                <nav aria-label="Page navigation">
                  <ul class="pagination mb-0">
                    <li class="page-item" :class="{ disabled: currentPage === 1 }">
                      <button class="page-link" @click="goToPage(currentPage - 1)">
                        <i class="bi bi-chevron-left"></i> Trước
                      </button>
                    </li>
                    
                    <li 
                      class="page-item" 
                      v-for="page in totalPages" 
                      :key="page" 
                      :class="{ active: currentPage === page }"
                    >
                      <button class="page-link" @click="goToPage(page)">{{ page }}</button>
                    </li>

                    <li class="page-item" :class="{ disabled: currentPage === totalPages }">
                      <button class="page-link" @click="goToPage(currentPage + 1)">
                        Sau <i class="bi bi-chevron-right"></i>
                      </button>
                    </li>
                  </ul>
                </nav>
              </div>

            </div>

            <div v-else class="text-center py-5 bg-light rounded border border-dashed">
              <div class="display-1 text-muted opacity-25">📦</div>
              <h5 class="mt-3 fw-bold text-secondary">Bạn chưa đăng bán món hàng nào</h5>
              <button class="btn btn-primary px-4 py-2 mt-2 fw-bold" @click="router.push('/dang-ban')">
                Bắt đầu bán hàng
              </button>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import UserSidebar from "@/layouts/UserSidebar.vue";

const router = useRouter();
const products = ref([]);
const isLoading = ref(true);
let currentUserId = null;

// --- STATE PHÂN TRANG BACKEND ---
const currentPage = ref(1); // Giao diện luôn bắt đầu từ 1
const itemsPerPage = ref(5); // Số sản phẩm trên 1 trang
const totalPages = ref(0);

// Chuyển trang
const goToPage = (page) => {
  if (page >= 1 && page <= totalPages.value && page !== currentPage.value) {
    currentPage.value = page;
    // Gọi API trang mới (trừ 1 vì Spring Boot đếm từ 0)
    fetchMyProducts(page - 1);
    window.scrollTo({ top: 0, behavior: "smooth" });
  }
};
// ------------------------

const formatCurrency = (val) => {
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  }).format(val || 0);
};

// --- XỬ LÝ ẢNH ---
const getImageUrl = (url) => {
  // 1. Nếu không có ảnh -> Trả về ảnh mặc định
  if (!url || url.trim() === "") {
    return "https://placehold.co/60x60/e9ecef/495057?text=O2N";
  }

  // 2. Nếu url là link web hoàn chỉnh (Cloudinary, Imgur...)
  if (url.startsWith("http://") || url.startsWith("https://")) {
    return url;
  }

  // 3. Nếu url là đường dẫn tương đối từ backend Spring Boot
  if (url.startsWith("/")) {
    return `http://localhost:8080${url}`;
  }

  // Fallback
  return `http://localhost:8080/${url}`;
};

// Hàm thay thế bằng ảnh mặc định nếu ảnh bị lỗi (VD link Cloudinary chết)
const handleImageError = (event) => {
  const fallbackImage = "https://placehold.co/60x60/e9ecef/495057?text=Loi";
  if (event.target.src !== fallbackImage) {
    event.target.src = fallbackImage;
  }
};
// -----------------

const getStatusText = (status) => {
  if (status === "CHO_DUYET") return "[Đang chờ duyệt]";
  if (status === "AN_TIN") return "[Đang ẩn]";
  if (status === "BI_TU_CHOI") return "[Bị từ chối]";
  return "[Đang hiện]";
};

const getStatusClass = (status) => {
  if (status === "CHO_DUYET") return "text-warning fw-bold";
  if (status === "AN_TIN") return "text-danger";
  if (status === "BI_TU_CHOI") return "text-secondary text-decoration-line-through";
  return "text-success";
};

const getAuthHeaders = () => {
  const user = JSON.parse(localStorage.getItem("user"));
  return {
    headers: {
      Authorization: `Bearer ${user?.token || localStorage.getItem("token")}`,
    },
  };
};

// Gọi API lấy dữ liệu theo Page
const fetchMyProducts = async (pageIndex = 0) => {
  isLoading.value = true;
  try {
    const res = await axios.get(
      `http://localhost:8080/api/products/seller/${currentUserId}?page=${pageIndex}&size=${itemsPerPage.value}`,
      getAuthHeaders()
    );
    
    // Gán dữ liệu từ Spring Boot Page object
    products.value = res.data.content; 
    totalPages.value = res.data.totalPages;
    
  } catch (error) {
    console.error("Lỗi lấy danh sách sản phẩm:", error);
  } finally {
    isLoading.value = false;
  }
};

const handleDelete = async (id) => {
  if (!confirm("Bạn có chắc chắn muốn xóa vĩnh viễn sản phẩm này không?")) return;
  try {
    await axios.delete(`http://localhost:8080/api/products/${id}`, getAuthHeaders());
    alert("Đã xóa sản phẩm thành công!");
    
    // Gọi lại dữ liệu của trang hiện tại sau khi xóa
    // Nếu trang hiện tại bị rỗng (xóa hết item của trang đó), có thể xử lý lùi về trang trước
    let targetPage = currentPage.value - 1;
    if (products.value.length === 1 && currentPage.value > 1) {
        targetPage -= 1;
        currentPage.value -= 1;
    }
    await fetchMyProducts(targetPage);
    
  } catch (error) {
    alert("Không thể xóa sản phẩm. Vui lòng thử lại!");
  }
};

const handleToggleHide = async (product) => {
  if (product.trangThai === "CHO_DUYET") {
    alert("Sản phẩm đang chờ Admin phê duyệt.");
    return;
  }

  const isCurrentlyHidden = product.trangThai === "AN_TIN";
  const newStatus = isCurrentlyHidden ? "DANG_BAN" : "AN_TIN";

  if (!confirm(isCurrentlyHidden ? "Hiển thị lại sản phẩm này?" : "Tạm ẩn sản phẩm này?")) return;

  try {
    await axios.put(
      `http://localhost:8080/api/products/${product.id}/status`,
      { trangThai: newStatus },
      getAuthHeaders()
    );
    product.trangThai = newStatus;
    alert(`Đã cập nhật trạng thái thành công!`);
  } catch (error) {
    alert("Không thể cập nhật trạng thái tin đăng!");
  }
};

onMounted(() => {
  const storedUser = localStorage.getItem("user");
  if (!storedUser) {
    router.push("/login");
    return;
  }
  const user = JSON.parse(storedUser);
  currentUserId = user.id || user.nguoiDungId;

  // Lần đầu tiên load trang sẽ gọi API với trang 0 của Spring Boot
  fetchMyProducts(0);
  window.scrollTo(0, 0);
});
</script>

<style scoped>
.table thead th { font-weight: 600; }
.object-fit-cover { object-fit: cover; }
.border-dashed { border-style: dashed !important; }
.bg-warning-subtle { background-color: #fff3cd !important; }
.grayscale { filter: grayscale(1); }
</style>