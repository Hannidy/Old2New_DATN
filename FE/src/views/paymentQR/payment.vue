<template>
  <div class="bg-light min-vh-100 d-flex flex-column">
    <main class="container flex-grow-1 d-flex align-items-center justify-content-center py-5">
      <div class="card border-0 shadow-lg rounded-4 overflow-hidden" style="max-width: 550px; width: 100%;">
        
        <!-- Màn hình hết hạn -->
        <div v-if="isExpired" class="card-body text-center px-4 py-5">
           <i class="bi bi-x-circle text-danger mb-3" style="font-size: 5rem;"></i>
           <h3 class="fw-bold text-dark">Mã QR đã hết hạn!</h3>
           <p class="text-muted">Bạn đã quá thời gian 5 phút để thanh toán.<br>Vui lòng thử lại sau.</p>
           <button @click="huyThanhToan" class="btn btn-primary px-5 py-2 mt-3 rounded-pill fw-bold">
             Quay lại đơn hàng
           </button>
        </div>

        <!-- Màn hình thành công -->
        <div v-else-if="isSuccess" class="card-body text-center px-4 py-5">
          <div class="mb-4 mt-3">
            <i class="bi bi-check-circle-fill text-success" style="font-size: 5rem;"></i>
          </div>
          <h2 class="fw-bold text-dark mb-3">Thanh Toán Thành Công!</h2>
          <p class="text-muted px-3" style="font-size: 1.1rem; line-height: 1.6;">
            Sàn O2N đã nhận được tiền thanh toán cho đơn hàng <strong>#{{ donHangId }}</strong>.<br>
            Người bán đang chuẩn bị hàng cho bạn.
          </p>
          <div class="mt-5 mb-2">
            <button @click="huyThanhToan" class="btn btn-success py-3 px-5 fw-bold rounded-pill shadow-sm fs-6">
              VỀ TRANG QUẢN LÝ ĐƠN HÀNG
            </button>
          </div>
        </div>

        <!-- Màn hình Quét mã QR -->
        <div v-else>
          <div class="card-header bg-white text-center pt-5 pb-3 border-0">
            <h4 class="fw-bold text-dark mb-2">Thanh toán đơn hàng: #{{ donHangId }}</h4>
            <div class="badge bg-danger bg-gradient fs-5 px-4 py-2 mt-2 rounded-pill shadow-sm">
              <i class="bi bi-stopwatch me-1"></i> Mã hết hạn trong: {{ formattedTime }}
            </div>
          </div>

          <div class="card-body px-4 pb-5">
            <div v-if="isLoading" class="text-center my-5">
              <div class="spinner-border text-primary" role="status" style="width: 3rem; height: 3rem;"></div>
              <p class="mt-3 text-muted">Đang lấy mã QR từ hệ thống O2N...</p>
            </div>
            
            <div v-else>
              <div class="text-center mb-4">
                <!-- 🔥 LINK ẢNH QR ĐƯỢC LOAD TRỰC TIẾP TỪ BACKEND TRẢ VỀ -->
                <img :src="qrUrl" alt="QR Thanh Toán" class="img-fluid border rounded-4 p-2 shadow-sm" style="max-width: 250px; background-color: #fff;">
              </div>

              <div class="bg-light p-3 rounded-4 mb-4 border shadow-sm">
                  <div class="d-flex border-bottom pb-2 mb-2">
                    <div style="width: 130px;" class="text-muted fw-bold">Sản phẩm:</div>
                    <div class="fw-semibold text-dark text-truncate">{{ productNames || 'Đang tải...' }}</div>
                  </div>
                  
                  <div class="d-flex border-bottom pb-2 mb-2">
                    <div style="width: 130px;" class="text-muted fw-bold">Số tiền:</div>
                    <div class="fw-bold text-danger fs-5" style="margin-top: -3px;">{{ formatCurrency(tongTien) }}</div>
                  </div>
                  
                  <div class="d-flex align-items-center">
                    <div style="width: 130px;" class="text-muted fw-bold">Nội dung CK:</div>
                    <div class="fw-bold text-primary fs-6 bg-white border px-2 py-1 rounded user-select-all">
                      {{ noiDungCK }}
                    </div>
                  </div>
              </div>

              <div class="d-flex justify-content-center align-items-center text-primary mb-4 fw-bold">
                <span class="spinner-grow spinner-grow-sm me-2" role="status" aria-hidden="true"></span>
                Hệ thống đang tự động chờ nhận tiền...
              </div>

              <div class="text-center">
                <button class="btn btn-outline-secondary px-5 py-2 fw-bold rounded-pill shadow-sm" @click="huyThanhToan">
                  Đóng / Thanh toán sau
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const router = useRouter();

// ================= CÁC BIẾN QUẢN LÝ DỮ LIỆU =================
const donHangId = ref(route.params.id);
const qrUrl = ref(''); // Sẽ chứa link ảnh do Backend trả về
const tongTien = ref(0);
const productNames = ref('');
const noiDungCK = ref(`THANH TOAN DH ${donHangId.value}`); // Khớp chính xác với chuỗi trong VietQRService.java
const isLoading = ref(true);

// Biến trạng thái màn hình
const isSuccess = ref(false);
const isExpired = ref(false);

// Biến đếm ngược (5 phút = 300 giây)
const timeLeft = ref(300);
let pollingInterval = null;
let timerInterval = null;

// ================= CÁC HÀM XỬ LÝ =================
const formatCurrency = (val) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(val || 0);
};

const formattedTime = computed(() => {
  const minutes = Math.floor(timeLeft.value / 60);
  const seconds = timeLeft.value % 60;
  return `${minutes < 10 ? '0' : ''}${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
});

const startTimer = () => {
  timerInterval = setInterval(() => {
    if (timeLeft.value > 0) {
      timeLeft.value--;
    } else {
      isExpired.value = true; 
      stopAllIntervals();     
    }
  }, 1000);
};

const stopAllIntervals = () => {
  if (pollingInterval) clearInterval(pollingInterval);
  if (timerInterval) clearInterval(timerInterval);
};

// ================= LẤY DỮ LIỆU TỪ BACKEND =================
const fetchPaymentInfo = async () => {
  try {
    // 1. Lấy chi tiết đơn hàng (lấy tiền và tên sản phẩm)
    const resDetails = await axios.get(`http://localhost:8080/api/don-hang/chi-tiet/${donHangId.value}`);
    if (resDetails.data) {
      tongTien.value = resDetails.data.tongThanhTien || resDetails.data.tongTienHang || 0;
      if (resDetails.data.chiTietDonHangs) {
        productNames.value = resDetails.data.chiTietDonHangs.map(item => item.tenSanPham).join(', ');
      }
      
      // 2. 🔥 GỌI API ĐỂ LẤY URL ẢNH QR TỪ BACKEND (VietQRService)
      // Lưu ý: Đảm bảo Backend của bạn có một API trả về cái chuỗi URL này nhé!
      const resQR = await axios.get(`http://localhost:8080/api/payment/qr-url/${donHangId.value}`);
      
      // Gán URL ảnh QR nhận được từ Backend vào biến qrUrl
      if (resQR.data && resQR.data.qrUrl) {
          qrUrl.value = resQR.data.qrUrl;
      } else {
          // Nếu backend trả trực tiếp String thì dùng:
          qrUrl.value = resQR.data;
      }
    }
  } catch (error) {
    console.error("Lỗi lấy thông tin đơn hàng hoặc QR từ Backend:", error);
    alert("Không thể khởi tạo mã thanh toán. Vui lòng thử lại sau!");
  } finally {
    isLoading.value = false; 
  }
};

// ================= KIỂM TRA TRẠNG THÁI NGẦM (POLLING) =================
const checkPaymentStatus = async () => {
  if (isSuccess.value || isExpired.value) return; 

  try {
    const response = await axios.get(`http://localhost:8080/api/don-hang/chi-tiet/${donHangId.value}`);
    if (response.data.trangThaiThanhToan === 'DA_THANH_TOAN') {
      isSuccess.value = true; 
      stopAllIntervals();
    }
  } catch (error) {
    // Bỏ qua lỗi nếu mạng chập chờn
  }
};

const huyThanhToan = () => {
  router.push('/quan-ly-don-hang');
};

// ================= LIFECYCLE HOOKS =================
onMounted(() => {
  window.scrollTo(0, 0);
  fetchPaymentInfo();
  startTimer();
  pollingInterval = setInterval(checkPaymentStatus, 3000);
});

onBeforeUnmount(() => {
  stopAllIntervals();
});
</script>

<style scoped>
.user-select-all {
  user-select: all;
  -webkit-user-select: all;
  cursor: pointer;
  letter-spacing: 1px;
}
</style>  