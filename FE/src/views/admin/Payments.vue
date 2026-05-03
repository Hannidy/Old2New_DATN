<template>
  <div class="p-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h3 class="fw-bold text-dark mb-0">Quản lý Giải Ngân (Admin)</h3>
      <button class="btn btn-outline-secondary btn-sm" @click="fetchRequests">
        <i class="bi bi-arrow-clockwise"></i> Làm mới
      </button>
    </div>

    <div class="card shadow-sm border-0 rounded-4">
      <div class="card-body p-0">
        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0">
            <thead class="table-light">
              <tr>
                <th class="px-4 py-3 border-0">Mã Yêu Cầu</th>
                <th class="py-3 border-0">Thời Gian Gửi</th>
                <th class="py-3 border-0">Số Tiền Rút</th>
                <th class="py-3 border-0">Thông Tin Nhận Tiền</th>
                <th class="px-4 py-3 border-0 text-center">Hành Động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="requests.length === 0">
                <td colspan="5" class="text-center py-5 text-muted">
                  <i class="bi bi-wallet2 fs-1 text-secondary mb-2 d-block"></i>
                  Tuyệt vời! Hiện không có yêu cầu giải ngân nào.
                </td>
              </tr>
              <tr v-for="req in requests" :key="req.id">
                <td class="px-4 fw-bold text-primary">#{{ req.id }}</td>
                <td class="text-muted">{{ formatDate(req.createdAt) }}</td>
                <td class="fw-bold fs-5 text-danger">{{ formatCurrency(req.soTien) }}</td>
                <td>
                  <div class="fw-bold text-dark">{{ req.nganHang }} - {{ req.soTaiKhoan }}</div>
                  <div class="small text-muted text-uppercase">{{ req.chuTaiKhoan }}</div>
                </td>
                <td class="px-4 text-center">
                  <div class="d-flex justify-content-center gap-2">
                    <!-- 🔥 NÚT HIỂN THỊ MÃ QR -->
                    <button 
                      class="btn btn-outline-primary btn-sm fw-bold rounded-pill shadow-sm px-3"
                      data-bs-toggle="modal" 
                      data-bs-target="#qrModal"
                      @click="loadQRCode(req.id, req.soTien)"
                    >
                      <i class="bi bi-qr-code-scan me-1"></i> Quét QR
                    </button>
                    
                    <!-- NÚT XÁC NHẬN CHUYỂN TIỀN -->
                    <button 
                      class="btn btn-success btn-sm fw-bold px-3 rounded-pill shadow-sm" 
                      @click="approveRequest(req.id, req.soTien)"
                      :disabled="isApproving === req.id"
                    >
                      <span v-if="isApproving === req.id" class="spinner-border spinner-border-sm me-1"></span>
                      <i v-else class="bi bi-check-circle me-1"></i> Đã chuyển tiền
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- 🔥 MODAL (POPUP) HIỂN THỊ MÃ QR CODE -->
    <div class="modal fade" id="qrModal" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content rounded-4 border-0 shadow">
          <div class="modal-header border-bottom-0">
            <h5 class="modal-title fw-bold text-primary"><i class="bi bi-qr-code me-2"></i>Quét mã để chuyển tiền</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body text-center pb-4">
            
            <!-- Hiệu ứng đang tải -->
            <div v-if="isLoadingQR" class="my-5">
              <div class="spinner-border text-primary" role="status"></div>
              <p class="mt-2 text-muted">Đang tạo mã QR tự động...</p>
            </div>
            
            <!-- Hiển thị QR khi tải thành công -->
            <div v-else-if="qrCodeUrl">
              <img :src="qrCodeUrl" alt="Mã QR Chuyển Tiền" class="img-fluid rounded-3 border p-2 mb-3 shadow-sm" style="max-width: 250px;">
              <h3 class="fw-bold text-danger mb-1">{{ formatCurrency(currentQrAmount) }}</h3>
              <p class="text-muted small px-3">Hãy dùng App ngân hàng của bạn quét mã này để chuyển khoản. Hệ thống đã tự động nhập đúng <strong>Số Tiền</strong> và <strong>Nội Dung</strong>.</p>
            </div>
            
            <!-- Hiển thị khi bị lỗi -->
            <div v-else class="text-danger my-4">
              <i class="bi bi-exclamation-triangle fs-1"></i>
              <p class="mt-2 fw-bold">Không thể tải mã QR!</p>
              <p class="small text-muted">Vui lòng kiểm tra lại ngân hàng hoặc thử lại sau.</p>
            </div>

          </div>
          <div class="modal-footer border-top-0 justify-content-center">
            <button type="button" class="btn btn-secondary rounded-pill px-5 fw-bold" data-bs-dismiss="modal">Đóng</button>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const requests = ref([]);
const isApproving = ref(null);

// Các biến phục vụ cho mã QR
const qrCodeUrl = ref('');
const isLoadingQR = ref(false);
const currentQrAmount = ref(0);

const formatCurrency = (value) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value || 0);
};

const formatDate = (dateString) => {
  if (!dateString) return '';
  return new Date(dateString).toLocaleString('vi-VN');
};

const getAuthHeaders = () => {
  const userStr = localStorage.getItem('user');
  if (userStr) {
    const userData = JSON.parse(userStr);
    const token = userData.token || userData.accessToken;
    if (token) return { headers: { Authorization: `Bearer ${token}` } };
  }
  return {}; 
};

// 1. Lấy danh sách yêu cầu
const fetchRequests = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/vi-tien/admin/danh-sach-rut-tien', getAuthHeaders());
    const data = response.data;
    
    if (Array.isArray(data)) {
        requests.value = data; 
    } else if (data && Array.isArray(data.data)) {
        requests.value = data.data; 
    } else if (data && Array.isArray(data.content)) {
        requests.value = data.content; 
    } else {
        requests.value = [];
    }
  } catch (error) {
    console.error("Lỗi lấy danh sách:", error);
  }
};

// 2. 🔥 HÀM MỚI: Gọi API Java để lấy cái ảnh QR Code
const loadQRCode = async (id, soTien) => {
  qrCodeUrl.value = ''; // Reset QR cũ
  isLoadingQR.value = true; // Bật hiệu ứng xoay xoay
  currentQrAmount.value = soTien; // Lưu lại số tiền để hiển thị dưới ảnh

  try {
    const response = await axios.get(`http://localhost:8080/api/vi-tien/admin/rut-tien/${id}/qr`, getAuthHeaders());
    // Java trả về: { "qrUrl": "https://img.vietqr.io/..." }
    qrCodeUrl.value = response.data.qrUrl;
  } catch (error) {
    console.error("Lỗi lấy mã QR:", error);
    qrCodeUrl.value = null;
  } finally {
    isLoadingQR.value = false; // Tắt hiệu ứng xoay
  }
};

// 3. Duyệt chuyển tiền
const approveRequest = async (id, soTien) => {
  if (!confirm(`Xác nhận bạn đã chuyển khoản thành công ${formatCurrency(soTien)} cho yêu cầu #${id}?`)) return;

  isApproving.value = id;
  try {
    await axios.put(`http://localhost:8080/api/vi-tien/admin/rut-tien/${id}/xac-nhan`, {}, getAuthHeaders());
    
    alert(`✅ Giải ngân thành công! Yêu cầu #${id} đã được chuyển sang trạng thái SUCCESS.`);
    fetchRequests(); 
    
  } catch (error) {
    const errorMsg = error.response?.data?.error;
    if (errorMsg && errorMsg.includes("thời gian")) {
      alert("⏳ " + errorMsg);
    } else {
      alert("❌ Có lỗi xảy ra khi duyệt: " + (errorMsg || "Lỗi hệ thống"));
    }
  } finally {
    isApproving.value = null;
  }
};

onMounted(() => {
  fetchRequests();
});
</script>