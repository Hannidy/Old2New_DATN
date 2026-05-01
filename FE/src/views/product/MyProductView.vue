<template>
  <div class="bg-light min-vh-100">
    <AppHeader />

    <main class="container py-5" style="margin-top: 80px">
      <div class="d-flex align-items-center justify-content-between mb-4">
        <nav aria-label="breadcrumb">
          <ol class="breadcrumb mb-0 bg-transparent p-0">
            <li class="breadcrumb-item">
              <router-link to="/" class="text-decoration-none text-muted"
                ><i class="bi bi-house-door"></i> Trang chủ</router-link
              >
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
                Bạn đang có {{ products.length }} sản phẩm đang rao bán
              </p>
            </div>

            <div v-if="isLoading" class="text-center py-5">
              <div class="spinner-border text-primary" role="status"></div>
            </div>

            <div
              v-else-if="products.length > 0"
              class="border rounded overflow-hidden"
            >
              <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                  <thead class="bg-light text-secondary">
                    <tr>
                      <th class="ps-4 py-3 small text-uppercase">Sản phẩm</th>
                      <th class="small text-uppercase">Danh mục</th>
                      <th class="small text-uppercase">Giá bán</th>
                      <th class="text-end pe-4 small text-uppercase">
                        Thao tác
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr
                      v-for="p in products"
                      :key="p.id"
                      :class="{
                        'opacity-50 bg-light': p.trangThai === 'AN_TIN',
                        'transition-all': true,
                      }"
                    >
                      <td class="ps-4 py-3">
                        <div class="d-flex align-items-center gap-3">
                          <img
                            :src="p.hinhAnh || 'https://via.placeholder.com/60'"
                            class="rounded border object-fit-cover shadow-sm"
                            :class="{ grayscale: p.trangThai === 'AN_TIN' }"
                            style="width: 60px; height: 60px"
                          />
                          <div>
                            <div
                              class="fw-bold text-dark text-truncate"
                              style="max-width: 250px"
                            >
                              {{ p.tenSanPham }}
                            </div>
                            <small class="text-muted">
                              ID: #{{ p.id }} | Tình trạng: {{ p.tinhTrang }} |
                              <span
                                :class="
                                  p.trangThai === 'AN_TIN'
                                    ? 'text-danger'
                                    : 'text-success'
                                "
                              >
                                {{
                                  p.trangThai === "AN_TIN"
                                    ? "[Đang ẩn]"
                                    : "[Đang hiện]"
                                }}
                              </span>
                            </small>
                          </div>
                        </div>
                      </td>

                      <td>
                        <span class="badge bg-light text-dark border">{{
                          p.danhMuc
                        }}</span>
                      </td>
                      <td>
                        <span class="text-danger fw-bold">{{
                          formatCurrency(p.gia)
                        }}</span>
                      </td>

                      <td class="text-end pe-4">
                        <div class="btn-group shadow-sm">
                          <button
                            class="btn btn-sm"
                            :class="
                              p.trangThai === 'AN_TIN'
                                ? 'btn-outline-success'
                                : 'btn-outline-warning'
                            "
                            @click="handleToggleHide(p)"
                          >
                            <i
                              class="bi"
                              :class="
                                p.trangThai === 'AN_TIN'
                                  ? 'bi-eye'
                                  : 'bi-eye-slash'
                              "
                            ></i>
                            {{
                              p.trangThai === "AN_TIN" ? "Hiện tin" : "Ẩn tin"
                            }}
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

            <div
              v-else
              class="text-center py-5 bg-light rounded border border-dashed"
            >
              <div class="display-1 text-muted opacity-25">📦</div>
              <h5 class="mt-3 fw-bold text-secondary">
                Bạn chưa đăng bán món hàng nào
              </h5>
              <p class="text-muted small">
                Hãy bắt đầu dọn nhà và kiếm thêm thu nhập ngay hôm nay!
              </p>
              <button
                class="btn btn-primary px-4 py-2 mt-2 fw-bold"
                @click="router.push('/dang-ban')"
              >
                Bắt đầu bán hàng
              </button>
            </div>
          </div>
        </div>
      </div>
    </main>

    <AppFooter />
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import AppHeader from "@/layouts/Header.vue";
import AppFooter from "@/layouts/Footer.vue";
import UserSidebar from "@/layouts/UserSidebar.vue";

const router = useRouter();
const products = ref([]);
const isLoading = ref(true);
let currentUserId = null;

const formatCurrency = (val) => {
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  }).format(val || 0);
};

const getAuthHeaders = () => {
  const user = JSON.parse(localStorage.getItem("user"));
  return {
    headers: {
      Authorization: `Bearer ${user?.token || localStorage.getItem("token")}`,
    },
  };
};

const fetchMyProducts = async () => {
  isLoading.value = true;
  try {
    const res = await axios.get(
      `http://localhost:8080/api/products/seller/${currentUserId}`,
      getAuthHeaders(),
    );
    products.value = res.data;
  } catch (error) {
    console.error("Lỗi lấy danh sách sản phẩm:", error);
  } finally {
    isLoading.value = false;
  }
};

const handleDelete = async (id) => {
  const confirmDelete = confirm(
    "Bạn có chắc chắn muốn xóa vĩnh viễn sản phẩm này không?",
  );
  if (!confirmDelete) return;

  try {
    const response = await axios.delete(
      `http://localhost:8080/api/products/${id}`,
      getAuthHeaders(),
    );
    alert(response.data.message || "Đã xóa sản phẩm thành công!");
    await fetchMyProducts();
  } catch (error) {
    const msg =
      error.response?.data?.message ||
      "Không thể xóa sản phẩm. Vui lòng thử lại!";
    alert(msg);
  }
};

const handleToggleHide = async (product) => {
  const isCurrentlyHidden = product.trangThai === "AN_TIN";
  const newStatus = isCurrentlyHidden ? "DANG_BAN" : "AN_TIN";

  const confirmMsg = isCurrentlyHidden
    ? "Bạn muốn hiển thị lại sản phẩm này trên trang chủ?"
    : "Bạn có chắc muốn ẩn sản phẩm này? (Sản phẩm sẽ không hiện ở trang chủ nữa)";

  if (!confirm(confirmMsg)) return;

  try {
    await axios.put(
      `http://localhost:8080/api/products/${product.id}/status`,
      { trangThai: newStatus },
      getAuthHeaders(),
    );
    product.trangThai = newStatus;
    alert(
      `Đã ${newStatus === "AN_TIN" ? "ẩn" : "hiển thị"} tin đăng thành công!`,
    );
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

  fetchMyProducts();
  window.scrollTo(0, 0);
});
</script>

<style scoped>
.table thead th {
  font-weight: 600;
  letter-spacing: 0.5px;
}
.object-fit-cover {
  object-fit: cover;
}
.border-dashed {
  border-style: dashed !important;
}
</style>
