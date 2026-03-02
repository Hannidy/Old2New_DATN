<template>
  <div class="order-container">
    <h2 class="title text-primary">🛍️ Quản lý Đơn Bán (Dành cho Người bán)</h2>
    <p class="text-muted mb-4">Danh sách các đơn hàng khách đã đặt mua sản phẩm của bạn.</p>

    <div class="table-wrapper">
      <table class="order-table">
        <thead>
          <tr>
            <th>Mã Đơn</th>
            <th>Ngày Đặt</th>
            <th>Tổng Tiền</th>
            <th>Trạng Thái Giao</th>
            <th>Hành Động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="orders.length === 0">
            <td colspan="5" class="empty-message">Hiện chưa có khách nào đặt mua sản phẩm của bạn.</td>
          </tr>
          <tr v-for="order in orders" :key="order.donHangId">
            <td><strong>O2N-{{ order.donHangId }}</strong></td>
            <td>{{ formatDate(order.ngayTao) }}</td>
            <td class="price">{{ formatCurrency(order.tongThanhTien) }}</td>
            <td>
              <span :class="['badge', getStatusClass(order.trangThaiDonHang)]">
                {{ order.trangThaiDonHang || 'CHO_XAC_NHAN' }}
              </span>
            </td>
            <td>
              <div class="action-buttons">
                <button 
                  v-if="!order.trangThaiDonHang || order.trangThaiDonHang === 'CHO_XAC_NHAN'" 
                  class="btn-action btn-confirm" 
                  @click="updateOrderStatus(order.donHangId, 'DANG_GIAO')">
                  📦 Xác nhận & Giao Hàng
                </button>

                <button 
                  v-if="!order.trangThaiDonHang || order.trangThaiDonHang === 'CHO_XAC_NHAN'" 
                  class="btn-action btn-cancel" 
                  @click="updateOrderStatus(order.donHangId, 'DA_HUY')">
                  ❌ Từ chối
                </button>
                
                <span v-if="order.trangThaiDonHang === 'DANG_GIAO'" class="text-success fw-bold">🚚 Đang vận chuyển</span>
                <span v-if="order.trangThaiDonHang === 'DA_HUY'" class="text-danger fw-bold">Đã hủy bỏ</span>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const orders = ref([]);
let currentUserId = null;

onMounted(() => {
  const storedUser = localStorage.getItem('user');
  if (!storedUser) {
    alert("Vui lòng đăng nhập để xem đơn bán!");
    router.push('/login');
    return;
  }
  
  currentUserId = JSON.parse(storedUser).id || JSON.parse(storedUser).nguoiDungId;
  fetchSellerOrders();
});

// Lấy danh sách đơn hàng mà người khác đặt mua đồ của bạn
const fetchSellerOrders = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/api/don-hang/danh-sach-ban/${currentUserId}`);
    orders.value = response.data;
  } catch (error) {
    console.error("Lỗi khi tải danh sách đơn bán:", error);
  }
};

// Gọi API Cập nhật trạng thái đơn hàng (API này chúng ta đã viết sẵn ở đợt trước)
const updateOrderStatus = async (donHangId, newStatus) => {
  const confirmMsg = newStatus === 'DANG_GIAO' 
    ? "Xác nhận đã đóng gói và giao cho đơn vị vận chuyển?" 
    : "Bạn có chắc chắn muốn từ chối (hủy) đơn hàng này?";
    
  if (confirm(confirmMsg)) {
    try {
      await axios.put(`http://localhost:8080/api/don-hang/cap-nhat-trang-thai/${donHangId}?trangThaiMoi=${newStatus}`);
      alert("Cập nhật trạng thái thành công!");
      fetchSellerOrders(); // Tải lại danh sách
    } catch (error) {
      console.error("Lỗi cập nhật trạng thái:", error);
      alert("Cập nhật thất bại. Vui lòng thử lại!");
    }
  }
};

// Tiện ích hiển thị
const formatCurrency = (value) => {
  if (!value) return '0 ₫';
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
};
const formatDate = (dateString) => {
  if (!dateString) return '';
  return new Date(dateString).toLocaleString('vi-VN');
};
const getStatusClass = (status) => {
  if (status === 'DANG_GIAO') return 'info';
  if (status === 'DA_GIAO') return 'success';
  if (status === 'DA_HUY') return 'danger';
  return 'warning'; // Mặc định là Chờ xác nhận
};
</script>

<style scoped>
.order-container { max-width: 1100px; margin: 0 auto; padding: 20px; font-family: 'Helvetica Neue', Arial, sans-serif; }
.title { border-bottom: 2px solid #007bff; padding-bottom: 10px; text-transform: uppercase; font-size: 20px; color: #007bff;}
.table-wrapper { overflow-x: auto; margin-top: 20px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); border-radius: 8px; background: white;}
.order-table { width: 100%; border-collapse: collapse; text-align: left; }
.order-table th { background-color: #f8f9fa; padding: 15px; border-bottom: 2px solid #eaeaea; color: #555;}
.order-table td { padding: 15px; border-bottom: 1px solid #eaeaea; vertical-align: middle; color: #333;}
.price { color: #ee4d2d; font-weight: bold; font-size: 16px;}
.badge { padding: 6px 12px; border-radius: 20px; font-size: 12px; font-weight: bold; color: white; display: inline-block;}
.badge.success { background-color: #28a745; }
.badge.warning { background-color: #ffc107; color: #333; }
.badge.info { background-color: #17a2b8; }
.badge.danger { background-color: #dc3545; }
.empty-message { text-align: center; color: #888; padding: 30px; }

.action-buttons { display: flex; gap: 10px; align-items: center;}
.btn-action { padding: 8px 12px; border: none; border-radius: 4px; cursor: pointer; font-weight: bold; transition: 0.2s; font-size: 13px;}
.btn-confirm { background-color: #28a745; color: white; }
.btn-confirm:hover { background-color: #218838; transform: translateY(-1px);}
.btn-cancel { background-color: #f8f9fa; color: #dc3545; border: 1px solid #dc3545;}
.btn-cancel:hover { background-color: #dc3545; color: white;}
</style>