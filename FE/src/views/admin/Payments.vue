<template>
  <div class="doi-soat-page">
    <div class="card border-0 shadow-sm rounded-4 p-4">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h4 class="fw-bold mb-0 text-primary">
          <i class="bi bi-shield-check me-2"></i> Đối soát giao dịch VNPay
        </h4>
        <button class="btn btn-outline-secondary btn-sm" @click="fetchOrders">
          <i class="bi bi-arrow-clockwise"></i> Làm mới
        </button>
      </div>

      <div class="table-responsive">
        <table class="table table-hover align-middle">
          <thead class="table-light">
            <tr>
              <th>Mã Đơn</th>
              <th>Ngày Giao Dịch</th>
              <th>Số Tiền (VNĐ)</th>
              <th>Trạng Thái Đối Soát</th>
              <th class="text-center">Hành Động</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="order in orders" :key="order.donHangId">
              <td class="fw-bold">O2N-{{ order.donHangId }}</td>
              <td>{{ formatDateTime(order.ngayTao) }}</td>
              <td class="text-danger fw-bold">{{ formatCurrency(order.tongThanhTien) }}</td>
              
              <td>
                <span v-if="order.daDoiSoat" class="badge bg-success bg-opacity-10 text-success border border-success px-3 py-2 rounded-pill">
                  <i class="bi bi-check-circle-fill me-1"></i> Đã tiền về
                </span>
                <span v-else class="badge bg-warning bg-opacity-10 text-warning border border-warning px-3 py-2 rounded-pill">
                  <i class="bi bi-hourglass-split me-1"></i> Đang chờ check
                </span>
              </td>

              <td class="text-center">
                <button 
                  v-if="!order.daDoiSoat" 
                  class="btn btn-primary btn-sm px-3 rounded-pill shadow-sm"
                  @click="confirmDoiSoat(order.donHangId)">
                  ✅ Xác nhận tiền về
                </button>
                <span v-else class="text-muted fst-italic fs-6">Đã chốt sổ</span>
              </td>
            </tr>
            
            <tr v-if="orders.length === 0">
              <td colspan="5" class="text-center py-5 text-muted">
                <i class="bi bi-inbox fs-1 d-block mb-2"></i>
                Hiện không có giao dịch VNPay nào cần đối soát.
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

const orders = ref([]);

// 1. Hàm gọi API lấy danh sách đơn VNPAY
const fetchOrders = async () => {
  try {
    const storedUser = localStorage.getItem('user');
    if (!storedUser) return;
    const token = JSON.parse(storedUser).token || JSON.parse(storedUser).accessToken;

    const response = await axios.get('http://localhost:8080/api/admin/doi-soat', {
      headers: { Authorization: `Bearer ${token}` }
    });
    orders.value = response.data;
  } catch (error) {
    console.error("Lỗi khi tải danh sách đối soát:", error);
  }
};

// 2. Hàm gọi API xác nhận đối soát khi Admin bấm nút
const confirmDoiSoat = async (id) => {
  const msg = "Bạn đã mở app Ngân hàng và kiểm tra chắc chắn tiền đã về tài khoản chưa?\n\nHành động này không thể hoàn tác!";
  
  if (confirm(msg)) {
    try {
      const storedUser = localStorage.getItem('user');
      const token = JSON.parse(storedUser).token || JSON.parse(storedUser).accessToken;

      await axios.put(`http://localhost:8080/api/admin/doi-soat/${id}/xac-nhan`, null, {
        headers: { Authorization: `Bearer ${token}` }
      });
      
      alert("✅ Đã chốt sổ đối soát thành công!");
      fetchOrders(); // Tải lại danh sách để cập nhật trạng thái
    } catch (error) {
      console.error("Lỗi đối soát:", error);
      alert("Có lỗi xảy ra, vui lòng thử lại!");
    }
  }
};

// --- Các hàm tiện ích định dạng (Format) ---
const formatCurrency = (value) => {
  if (!value) return "0 ₫";
  return new Intl.NumberFormat('vi-VN').format(value) + ' ₫';
};

const formatDateTime = (dateString) => {
  if (!dateString) return "N/A";
  // Xử lý cả trường hợp Spring Boot trả về chuỗi hoặc mảng [năm, tháng, ngày...]
  let date = Array.isArray(dateString) 
    ? new Date(dateString[0], dateString[1] - 1, dateString[2], dateString[3] || 0, dateString[4] || 0) 
    : new Date(dateString);
  
  return date.toLocaleString('vi-VN', {
    hour: '2-digit', minute: '2-digit',
    day: '2-digit', month: '2-digit', year: 'numeric'
  });
};

onMounted(() => {
  fetchOrders();
});
</script>

<style scoped>
.table th {
  font-weight: 600;
  color: #495057;
}
.badge {
  font-size: 0.85rem;
}
</style>