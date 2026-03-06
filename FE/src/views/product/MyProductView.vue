<template>
  <div class="bg-light min-vh-100">
    <AppHeader />

    <main class="container main-content py-4">
      <div class="d-flex align-items-center justify-content-between mb-4">
        <nav aria-label="breadcrumb">
          <ol class="breadcrumb mb-0 bg-transparent p-0">
            <li class="breadcrumb-item">
              <router-link to="/" class="text-decoration-none text-muted">Trang chủ</router-link>
            </li>
            <li class="breadcrumb-item active text-danger" aria-current="page">Sản phẩm của tôi</li>
          </ol>
        </nav>

        <button @click="router.push('/')" class="btn btn-sm btn-outline-secondary rounded-pill px-3 shadow-sm">
          Quay lại trang chủ
        </button>
      </div>

      <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h2 class="fw-bold text-dark mb-0">Quản lý sản phẩm</h2>
          <p class="text-muted mb-0">Bạn đang có {{ products.length }} sản phẩm đang rao bán</p>
        </div>

      </div>

      <div v-if="isLoading" class="text-center py-5">
        <div class="spinner-border text-danger" role="status"></div>
        <p class="mt-2 text-muted">Đang tải danh sách hàng hóa...</p>
      </div>

      <div v-else-if="products.length > 0" class="card border-0 shadow-sm rounded-4 overflow-hidden">
        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0">
            <thead class="bg-dark text-white">
              <tr>
                <th class="ps-4 py-3">Sản phẩm</th>
                <th>Danh mục</th>
                <th>Giá bán</th>
                <th class="text-end pe-4">Thao tác</th>
              </tr>
            </thead>
            <tbody>
            <tr v-for="p in products" :key="p.id">
                <td class="ps-4 py-3">
                <div class="d-flex align-items-center gap-3">
                    <img :src="p.hinhAnh || 'https://via.placeholder.com/60'" 
                        class="rounded border object-fit-cover shadow-sm" 
                        style="width: 60px; height: 60px;">
                    <div>
                    <div class="fw-bold text-dark text-truncate" style="max-width: 250px;">{{ p.tenSanPham }}</div>
                    <small class="text-muted">ID: #{{ p.id }} | Tình trạng: {{ p.tinhTrang }}</small>
                    </div>
                </div>
                </td>
                <td><span class="badge bg-light text-dark border">{{ p.danhMuc }}</span></td>
                <td><span class="text-danger fw-bold">{{ formatCurrency(p.gia) }}</span></td>
                
                <td class="text-end pe-4">
                    <div class="btn-group shadow-sm">
                        <button class="btn btn-sm" 
                                :class="p.trangThai === 'AN_TIN' ? 'btn-outline-success' : 'btn-outline-warning'"
                                @click="handleToggleHide(p)">
                        <i class="bi" :class="p.trangThai === 'AN_TIN' ? 'bi-eye' : 'bi-eye-slash'"></i>
                        {{ p.trangThai === 'AN_TIN' ? 'Hiện tin' : 'Ẩn tin' }}
                        </button>

                        <button class="btn btn-sm btn-outline-danger" @click="handleDelete(p.id)">
                        <i class="bi bi-trash"></i> Xóa
                        </button>
                    </div>
                </td>
                </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div v-else class="text-center py-5 bg-white rounded-4 border shadow-sm">
        <div class="display-1 text-muted opacity-25">📦</div>
        <h4 class="mt-3 fw-bold text-secondary">Bạn chưa đăng bán món hàng nào</h4>
        <p class="text-muted">Hãy bắt đầu dọn nhà và kiếm thêm thu nhập ngay hôm nay!</p>
        <button class="btn btn-primary px-5 py-2 mt-2 fw-bold" @click="router.push('/dang-ban')">Bắt đầu bán hàng</button>
      </div>
    </main>

    <AppFooter />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import AppHeader from '@/layouts/Header.vue';
import AppFooter from '@/layouts/Footer.vue';

const router = useRouter();
const products = ref([]);
const isLoading = ref(true);
let currentUserId = null;

const formatCurrency = (val) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(val || 0);
};

const getAuthHeaders = () => {
  const user = JSON.parse(localStorage.getItem('user'));
  return { headers: { Authorization: `Bearer ${user?.token || localStorage.getItem('token')}` } };
};

const fetchMyProducts = async () => {
  isLoading.value = true;
  try {
    const res = await axios.get(`http://localhost:8080/api/products/seller/${currentUserId}`, getAuthHeaders());
    products.value = res.data;
  } catch (error) {
    console.error("Lỗi lấy danh sách sản phẩm:", error);
  } finally {
    isLoading.value = false;
  }
};

const handleDelete = async (id) => {
  if (!confirm("Bạn có chắc muốn xóa sản phẩm này khỏi hệ thống?")) return;
  try {
    await axios.delete(`http://localhost:8080/api/products/${id}`, getAuthHeaders());
    alert("Đã xóa sản phẩm thành công!");
    fetchMyProducts();
  } catch (error) {
    alert("Lỗi khi xóa sản phẩm. Vui lòng thử lại!");
  }
};

onMounted(() => {
  const storedUser = localStorage.getItem('user');
  if (!storedUser) {
    router.push('/login');
    return;
  }
  const user = JSON.parse(storedUser);
  currentUserId = user.id || user.nguoiDungId;
  
  fetchMyProducts();
  window.scrollTo(0, 0);
});

// Hàm thay đổi trạng thái ẩn hiện của tin đăng
const handleToggleStatus = async (product) => {
  const newStatus = product.trangThai === 'HIEN_THI' ? 'AN_TIN' : 'HIEN_THI';
  try {
    // Gọi API cập nhật trạng thái (Bạn cần viết API này ở Backend)
    await axios.put(`http://localhost:8080/api/products/${product.id}/status`, 
      { trangThai: newStatus }, 
      getAuthHeaders()
    );
    product.trangThai = newStatus; // Cập nhật lại UI ngay lập tức
    alert(`Đã ${newStatus === 'AN_TIN' ? 'ẩn' : 'hiển thị'} tin đăng thành công!`);
  } catch (error) {
    console.error("Lỗi cập nhật trạng thái:", error);
    alert("Không thể cập nhật trạng thái tin đăng!");
  }
};

</script>

<style scoped>
.main-content {
  margin-top: 160px;
  min-height: 80vh;
}
.table thead th {
  font-weight: 600;
  text-transform: uppercase;
  font-size: 0.8rem;
  letter-spacing: 0.5px;
}
.object-fit-cover {
  object-fit: cover;
}
@media (max-width: 768px) {
  .main-content { margin-top: 110px; }
}
</style>