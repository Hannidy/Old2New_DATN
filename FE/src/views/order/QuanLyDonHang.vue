<template>
  <div class="order-container">
    <h2 class="title">📦 Quản lý đơn hàng của bạn</h2>

    <div class="table-wrapper">
      <table class="order-table">
        <thead>
          <tr>
            <th>Mã Đơn</th>
            <th>Ngày Đặt</th>
            <th>Tổng Tiền</th>
            <th>Thanh Toán</th>
            <th>Trạng Thái Giao</th>
            <th>Hành Động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="orders.length === 0">
            <td colspan="6" class="empty-message">Bạn chưa có đơn hàng nào.</td>
          </tr>
          <tr v-for="order in orders" :key="order.donHangId">
            <td><strong>#{{ order.donHangId }}</strong></td>
            <td>{{ formatDate(order.ngayTao) }}</td>
            <td class="price">{{ formatCurrency(order.tongThanhTien) }}</td>
            <td>
              <span :class="['badge', order.trangThaiThanhToan === 'DA_THANH_TOAN' ? 'success' : 'warning']">
                {{ order.trangThaiThanhToan === 'DA_THANH_TOAN' ? 'Đã thanh toán' : 'Chưa thanh toán' }}
              </span>
              <br><small>({{ order.phuongThucThanhToan }})</small>
            </td>
            <td>
              <span class="badge info">{{ order.trangThaiDonHang || 'Chờ xác nhận' }}</span>
            </td>
            <td>
              <button class="btn-view" @click="viewDetails(order.donHangId)">👁️ Xem Chi Tiết</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="isModalOpen" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content shopee-modal">
        <div class="modal-header">
          <div class="header-left">
            <button class="back-btn" @click="closeModal">❮ TRỞ LẠI</button>
          </div>
          <div class="header-right">
            <span>MÃ ĐƠN HÀNG. O2N{{ selectedOrder?.donHangId }}</span> | 
            <span class="status-text">{{ selectedOrder?.trangThaiDonHang || 'CHỜ XÁC NHẬN' }}</span>
          </div>
        </div>
        
        <div class="modal-body" v-if="selectedOrder">
          
          <div class="stepper-wrapper">
            <div class="stepper-item completed">
              <div class="step-counter">📝</div>
              <div class="step-name">Đơn Hàng Đã Đặt</div>
            </div>
            <div :class="['stepper-item', selectedOrder.trangThaiThanhToan === 'DA_THANH_TOAN' ? 'completed' : '']">
              <div class="step-counter">💳</div>
              <div class="step-name">Đã Xác Nhận Thanh Toán</div>
            </div>
            <div :class="['stepper-item', selectedOrder.trangThaiDonHang === 'DANG_GIAO' || selectedOrder.trangThaiDonHang === 'DA_GIAO' ? 'completed' : '']">
              <div class="step-counter">🚚</div>
              <div class="step-name">Đã Giao Cho ĐVVC</div>
            </div>
            <div :class="['stepper-item', selectedOrder.trangThaiDonHang === 'DA_GIAO' ? 'completed' : '']">
              <div class="step-counter">⭐</div>
              <div class="step-name">Đơn Hàng Đã Hoàn Thành</div>
            </div>
          </div>

          <div class="address-section">
            <h3 class="section-title">Địa Chỉ Nhận Hàng</h3>
            <div class="address-content">
              <div class="user-info" v-if="selectedOrder.diaChiGiaoHang">
                <strong>{{ selectedOrder.diaChiGiaoHang.split(' | ')[0] || 'Khách hàng' }}</strong><br>
                <span class="phone-text">{{ selectedOrder.diaChiGiaoHang.split(' | ')[1] || 'Chưa cập nhật SĐT' }}</span><br>
                <span class="address-text">{{ selectedOrder.diaChiGiaoHang.split(' | ')[2] || selectedOrder.diaChiGiaoHang }}</span>
              </div>
            </div>
          </div>

          <div class="product-section">
            <table class="product-table">
              <tbody>
                <tr v-for="item in selectedOrder.chiTietDonHangs" :key="item.chiTietId">
                  <td class="product-info">
                    <div class="product-name">{{ item.tenSanPham }}</div>
                    <div class="product-qty">x{{ item.soLuongMua }}</div>
                  </td>
                  <td class="product-price">
                    <span class="sale-price">{{ formatCurrency(item.giaLucMua) }}</span>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <div class="summary-section">
            <div class="summary-row">
              <span>Tổng tiền hàng</span>
              <span>{{ formatCurrency(selectedOrder.tongTienHang) }}</span>
            </div>
            <div class="summary-row">
              <span>Phí vận chuyển</span>
              <span>{{ formatCurrency(selectedOrder.tongTienShip) }}</span>
            </div>
            <div class="summary-row total-row">
              <span>Thành tiền</span>
              <span class="final-price">{{ formatCurrency(selectedOrder.tongThanhTien) }}</span>
            </div>
            <div class="payment-method-row">
              <span class="payment-method-text">Phương thức Thanh toán: <strong>{{ selectedOrder.phuongThucThanhToan }}</strong></span>
            </div>
          </div>

        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router'; // Import thêm router để đá về trang đăng nhập nếu chưa login
import axios from 'axios';

const router = useRouter();

// Các biến lưu trữ dữ liệu
const orders = ref([]);
const selectedOrder = ref(null);
const isModalOpen = ref(false);

// ==========================================
// BƯỚC 1: LẤY ID NGƯỜI DÙNG TỪ LOCAL STORAGE
// ==========================================
let userId = null;
const storedUser = localStorage.getItem('user'); // Chữ 'user' này phụ thuộc vào lúc bạn setItem ở trang Login nhé

if (storedUser) {
  const userData = JSON.parse(storedUser);
  // Lấy ID (Tùy vào API Login của bạn trả về tên biến là id hay nguoiDungId)
  userId = userData.nguoiDungId || userData.id; 
}

// Hàm gọi API lấy danh sách đơn hàng
const fetchOrders = async () => {
  if (!userId) {
    alert("Vui lòng đăng nhập để xem đơn hàng của bạn!");
    router.push('/login'); // Chưa đăng nhập thì đá văng ra trang Login
    return;
  }

  try {
    // Lúc này userId đã là số động (của bạn là 18, 19...) chứ không còn là 1 nữa
    const response = await axios.get(`http://localhost:8080/api/don-hang/danh-sach/${userId}`);
    orders.value = response.data;
  } catch (error) {
    console.error("Lỗi khi tải danh sách đơn hàng:", error);
  }
};

// Hàm gọi API xem chi tiết 1 đơn hàng
const viewDetails = async (donHangId) => {
  try {
    const response = await axios.get(`http://localhost:8080/api/don-hang/chi-tiet/${donHangId}`);
    selectedOrder.value = response.data;
    isModalOpen.value = true; // Mở cửa sổ Modal
  } catch (error) {
    console.error("Lỗi khi tải chi tiết đơn hàng:", error);
    alert("Không thể tải chi tiết đơn hàng lúc này!");
  }
};

// Đóng Modal
const closeModal = () => {
  isModalOpen.value = false;
  selectedOrder.value = null;
};

// Tiện ích format Tiền tệ (VNĐ)
const formatCurrency = (value) => {
  if (!value) return '0 ₫';
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
};

// Tiện ích format Ngày tháng
const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleString('vi-VN');
};

// Tự động chạy khi mở trang
onMounted(() => {
  fetchOrders();
});
</script>

<style scoped>
/* ================= CSS CHUNG CHO TRANG ================= */
.order-container { max-width: 1100px; margin: 0 auto; padding: 20px; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; }
.title { border-bottom: 2px solid #ee4d2d; padding-bottom: 10px; color: #333; text-transform: uppercase; font-size: 20px;}
.table-wrapper { overflow-x: auto; margin-top: 20px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); border-radius: 8px; background: white;}
.order-table { width: 100%; border-collapse: collapse; text-align: left; }
.order-table th { background-color: #f8f9fa; padding: 15px; border-bottom: 2px solid #eaeaea; color: #555;}
.order-table td { padding: 15px; border-bottom: 1px solid #eaeaea; vertical-align: middle; color: #333;}
.price { color: #ee4d2d; font-weight: bold; }
.badge { padding: 6px 12px; border-radius: 20px; font-size: 12px; font-weight: bold; color: white; display: inline-block; text-align: center;}
.badge.success { background-color: #28a745; }
.badge.warning { background-color: #ffc107; color: #333; }
.badge.info { background-color: #17a2b8; }
.btn-view { background-color: #ee4d2d; color: white; border: none; padding: 8px 15px; border-radius: 4px; cursor: pointer; transition: 0.2s; font-weight: bold;}
.btn-view:hover { background-color: #d73a27; transform: translateY(-1px); }
.empty-message { text-align: center; color: #888; padding: 30px; }

/* ================= LỚP PHỦ TỐI & MODAL BẬT LÊN ================= */
.modal-overlay { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.6); display: flex; justify-content: center; align-items: center; z-index: 1000; backdrop-filter: blur(2px); }
.modal-content { max-height: 90vh; overflow-y: auto; }

/* ================= CSS MODAL SHOPEE STYLE ================= */
.shopee-modal { width: 95%; max-width: 850px; padding: 0; background: #f5f5f5; border-radius: 8px; overflow: hidden; display: flex; flex-direction: column; box-shadow: 0 5px 20px rgba(0,0,0,0.2);}
.modal-header { display: flex; justify-content: space-between; align-items: center; padding: 18px 25px; background: white; border-bottom: 1px solid #eaeaea; position: sticky; top: 0; z-index: 10;}
.back-btn { background: none; border: none; font-weight: bold; color: #ee4d2d; cursor: pointer; font-size: 14px; display: flex; align-items: center; gap: 5px;}
.back-btn:hover { opacity: 0.8; }
.status-text { color: #ee4d2d; font-weight: bold; text-transform: uppercase; font-size: 15px;}
.modal-body { padding: 25px; overflow-y: auto; }

/* Stepper Tracker (Thanh Tiến Trình) */
.stepper-wrapper { display: flex; justify-content: space-between; margin-bottom: 25px; background: white; padding: 35px 20px; border-radius: 4px; box-shadow: 0 1px 2px rgba(0,0,0,.05); position: relative;}
.stepper-item { display: flex; flex-direction: column; align-items: center; flex: 1; position: relative; z-index: 1;}
.stepper-item::before { position: absolute; content: ""; border-bottom: 4px solid #e0e0e0; width: 100%; top: 20px; left: -50%; z-index: -1; }
.stepper-item:first-child::before { content: none; }
.step-counter { width: 45px; height: 45px; background: #e0e0e0; border-radius: 50%; display: flex; justify-content: center; align-items: center; color: white; font-size: 20px; margin-bottom: 12px; border: 4px solid white; box-shadow: 0 0 0 2px #e0e0e0;}
.stepper-item.completed .step-counter { background: #26aa99; box-shadow: 0 0 0 2px #26aa99; }
.stepper-item.completed::before { border-color: #26aa99; }
.step-name { font-size: 13px; color: #888; text-align: center; font-weight: 500;}
.stepper-item.completed .step-name { color: #26aa99; font-weight: bold;}

/* Address Section (Địa chỉ) */
.address-section { background: white; padding: 25px; border-radius: 4px; margin-bottom: 20px; box-shadow: 0 1px 2px rgba(0,0,0,.05);}
.section-title { margin-top: 0; font-size: 18px; color: #333; border-bottom: 1px solid #eaeaea; padding-bottom: 12px; margin-bottom: 15px; font-weight: 500;}
.user-info strong { font-size: 18px; color: #333; margin-bottom: 8px; display: inline-block;}
.phone-text { color: #888; font-size: 14px; margin-bottom: 8px; display: inline-block;}
.address-text { color: #555; font-size: 15px; line-height: 1.6; display: block; margin-top: 5px;}

/* Product Section (Sản phẩm) */
.product-section { background: white; padding: 25px; border-radius: 4px; margin-bottom: 20px; box-shadow: 0 1px 2px rgba(0,0,0,.05);}
.product-table { width: 100%; border-collapse: collapse; }
.product-info { padding: 20px 0; border-bottom: 1px dashed #eaeaea; text-align: left; }
.product-name { font-size: 16px; color: #333; margin-bottom: 10px; font-weight: 500; }
.product-qty { font-size: 14px; color: #888; }
.product-price { text-align: right; vertical-align: middle; border-bottom: 1px dashed #eaeaea; padding: 20px 0;}
.sale-price { color: #ee4d2d; font-weight: bold; font-size: 16px;}

/* Summary Section (Tổng kết tiền) */
.summary-section { background: #fffcf5; padding: 25px; border-radius: 4px; box-shadow: 0 1px 2px rgba(0,0,0,.05); border: 1px solid #faebd7;}
.summary-row { display: flex; justify-content: flex-end; padding: 10px 0; font-size: 15px; color: #555;}
.summary-row span:last-child { width: 200px; text-align: right; }
.total-row { border-top: 1px dashed #eaeaea; margin-top: 15px; padding-top: 20px; align-items: center;}
.final-price { color: #ee4d2d; font-size: 28px; font-weight: bold; }
.payment-method-row { border-top: 1px solid #eaeaea; margin-top: 20px; padding-top: 20px; text-align: right; font-size: 14px; color: #555;}
.payment-method-row strong { color: #333; font-size: 15px; margin-left: 5px;}
</style>