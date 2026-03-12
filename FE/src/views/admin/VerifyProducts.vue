<template>
  <div class="verify-page">
    <div class="card border-0 shadow-sm rounded-4 p-4">
      <h5 class="fw-bold mb-4">📦 Danh sách chờ kiểm duyệt</h5>

      <div class="table-responsive">
        <table class="table table-hover align-middle">
          <thead class="table-light">
            <tr>
              <th>Ảnh</th>
              <th>Tên sản phẩm</th>
              <th>Người bán</th>
              <th>Giá</th>
              <th class="text-center">Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="sp in pendingProducts" :key="sp.sanPhamId">
              <td>
                <img :src="sp.danhSachHinhAnh?.[0]?.duongDan || 'https://via.placeholder.com/100'" 
                     class="rounded shadow-sm object-fit-cover" width="60" height="60">
              </td>
              <td>
                <div class="fw-bold">{{ sp.tenSanPham }}</div>
                <small class="text-muted">ID: #{{ sp.sanPhamId }}</small>
              </td>
              <td>{{ sp.nguoiDung?.hoVaTen }}</td>
              <td class="text-danger fw-bold">{{ sp.gia?.toLocaleString('vi-VN') }} ₫</td>
              <td class="text-center">
                <div class="btn-group shadow-sm">
                  <button class="btn btn-success btn-sm px-3" @click="handleVerify(sp.sanPhamId, 'DA_DUYET')">
                    Duyệt
                  </button>
                  <button class="btn btn-danger btn-sm px-3" @click="handleVerify(sp.sanPhamId, 'BI_TU_CHOI')">
                    Từ chối
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const pendingProducts = ref([]);

const fetchPendingProducts = async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/admin/products/pending');
    pendingProducts.value = res.data;
  } catch (error) {
    console.error("Lỗi fetch data:", error);
  }
};

const handleVerify = async (id, status) => {
  if (confirm(`Xác nhận thay đổi trạng thái sản phẩm này?`)) {
    try {
      await axios.put(`http://localhost:8080/api/admin/products/${id}/status?status=${status}`);
      fetchPendingProducts(); // Load lại danh sách sau khi duyệt
    } catch (error) {
      alert("Lỗi cập nhật!");
    }
  }
};

onMounted(fetchPendingProducts);
</script>