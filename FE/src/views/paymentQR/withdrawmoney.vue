<template>
  <div class="bg-light min-vh-100 py-4">
    <div class="container">

      <div class="d-flex align-items-center mb-4">
        <button @click="router.push('/profile')"
          class="btn btn-outline-secondary rounded-circle me-3 d-flex align-items-center justify-content-center shadow-sm bg-white"
          style="width: 45px; height: 45px;">
          <i class="bi bi-arrow-left fs-5 text-dark"></i>
        </button>
        <div>
          <h3 class="fw-bold mb-1 text-dark">Yêu cầu rút tiền</h3>
          <p class="text-muted mb-0 small">Tạo yêu cầu chuyển tiền từ ví về tài khoản ngân hàng của bạn.</p>
        </div>
      </div>

      <div class="row">
        <div class="col-lg-8 mb-4">
          <div class="card border-0 shadow-sm rounded-4 h-100">
            <div class="card-header bg-white p-4 border-bottom">
              <div class="d-flex align-items-center">
                <i class="bi bi-cash-stack me-2 text-primary fs-5"></i>
                <h5 class="card-title mb-0 fw-bold">Thông tin giao dịch</h5>
              </div>
            </div>

            <div class="card-body p-4 p-md-5">
              <form @submit.prevent="submitWithdrawRequest">

                <div class="row">
                  <div class="col-md-12 mb-4">
                    <label class="form-label fw-bold text-dark">Số tiền cần rút (VNĐ) <span
                        class="text-danger">*</span></label>
                    <div class="input-group input-group-lg shadow-sm rounded-3">
                      <span class="input-group-text bg-light fw-bold border-end-0 text-muted">VNĐ</span>
                      <input type="number" v-model="withdrawForm.soTien"
                        class="form-control border-start-0 fw-bold text-primary fs-4" :max="walletBalance"
                        placeholder="0" required>
                    </div>
                    <div class="d-flex justify-content-between mt-2 align-items-center">
                      <div class="form-text mt-0">
                        Hạn mức rút: từ <strong>10,000đ</strong>
                      </div>
                      <button type="button" class="btn btn-sm btn-outline-primary fw-bold rounded-pill px-3"
                        @click="withdrawForm.soTien = walletBalance">
                        Rút toàn bộ
                      </button>
                    </div>
                  </div>

                  <div class="col-md-12 mb-4">

                    <div v-if="hasLinkedBank"
                      class="border rounded-4 p-3 bg-white shadow-sm d-flex justify-content-between align-items-center border-primary border-opacity-50">
                      <div>
                        <p class="mb-1 text-muted small"><i class="bi bi-check-circle-fill text-success me-1"></i> Tài
                          khoản nhận tiền</p>
                        <h6 class="mb-1 fw-bold text-primary">{{ getBankName(withdrawForm.nganHang) }}</h6>
                        <h5 class="mb-0 fw-bold">{{ withdrawForm.stk }} - <span class="text-uppercase">{{
                            withdrawForm.chuTk }}</span></h5>
                      </div>
                      <div>
                        <button type="button" class="btn btn-outline-secondary btn-sm rounded-pill"
                          @click="changeBankAccount">
                          <i class="bi bi-arrow-left-right me-1"></i>Đổi thẻ
                        </button>
                      </div>
                    </div>

                    <div v-else class="border rounded-4 p-4 bg-light">
                      <h6 class="fw-bold mb-3"><i class="bi bi-bank me-2"></i>Nhập thông tin thẻ nhận tiền mới</h6>
                      <div class="row">
                        <div class="col-md-6 mb-3">
                          <label class="form-label fw-bold text-dark small">Ngân hàng thụ hưởng <span
                              class="text-danger">*</span></label>
                          <select v-model="withdrawForm.nganHang" class="form-select bg-white" required>
                            <option value="" disabled>-- Chọn ngân hàng --</option>
                            <option v-for="bank in danhSachNganHang" :key="bank.code" :value="bank.code">
                              {{ bank.name }}
                            </option>
                          </select>
                        </div>
                        <div class="col-md-6 mb-3">
                          <label class="form-label fw-bold text-dark small">Số tài khoản <span
                              class="text-danger">*</span></label>
                          <input type="text" v-model="withdrawForm.stk" class="form-control bg-white"
                            placeholder="Nhập số tài khoản..." required>
                        </div>
                        <div class="col-md-12 mb-3">
                          <label class="form-label fw-bold text-dark small">Tên chủ tài khoản <span
                              class="text-danger">*</span></label>
                          <input type="text" v-model="withdrawForm.chuTk" class="form-control bg-white text-uppercase"
                            placeholder="VD: NGUYEN VAN A" required>
                        </div>
                        <div class="col-md-12">
                          <div class="form-check form-switch mt-2">
                            <input class="form-check-input" type="checkbox" id="saveBankToggle" v-model="saveBankInfo">
                            <label class="form-check-label fw-bold text-primary" for="saveBankToggle">
                              Lưu thông tin tài khoản này cho lần rút sau
                            </label>
                          </div>
                        </div>
                      </div>
                    </div>

                  </div>
                </div>

                <hr class="text-muted my-4">

                <div class="d-flex justify-content-end align-items-center">
                  <button type="button" class="btn btn-light btn-lg me-3 px-4 fw-bold rounded-pill"
                    @click="router.push('/profile')">Hủy bỏ</button>
                  <button type="submit" class="btn btn-primary bg-gradient btn-lg px-5 fw-bold rounded-pill shadow"
                    :disabled="isSubmittingWithdraw || withdrawForm.soTien > walletBalance || withdrawForm.soTien < 10000">
                    <span v-if="isSubmittingWithdraw" class="spinner-border spinner-border-sm me-2"></span>
                    <i v-else class="bi bi-send-fill me-2"></i>
                    {{ isSubmittingWithdraw ? 'ĐANG XỬ LÝ...' : 'XÁC NHẬN RÚT TIỀN' }}
                  </button>
                </div>

              </form>
            </div>
          </div>
        </div>

        <div class="col-lg-4">
          <div class="bg-dark bg-gradient text-white mb-4 shadow rounded-4 p-4 position-relative overflow-hidden">
            <h6 class="text-white-50 mb-1 small text-uppercase">Số dư khả dụng để rút</h6>
            <h2 class="fw-bold mb-3">{{ formatCurrency(walletBalance) }}</h2>
            <div class="d-flex justify-content-between align-items-center border-top border-secondary pt-3">
              <span class="text-white-50 small">Trạng thái ví:</span>
              <span class="badge bg-light text-dark rounded-pill px-3 py-2 shadow-sm"><i
                  class="bi bi-shield-check text-success me-1"></i> Đang hoạt động</span>
            </div>
          </div>

          <div class="card border-0 shadow-sm rounded-4">
            <div class="card-header bg-white p-4 border-bottom">
              <h6 class="card-title fw-bold mb-0 text-dark"><i class="bi bi-info-circle-fill text-warning me-2"></i>Lưu
                ý khi rút tiền</h6>
            </div>
            <div class="card-body p-4">
              <ul class="text-muted small ps-3 mb-0" style="line-height: 1.6;">
                <li class="mb-2">Thời gian Sàn duyệt yêu cầu thường trong vòng <strong>48h làm việc</strong>.</li>
                <li class="mb-2">Số tiền rút tối thiểu cho mỗi giao dịch là <strong>10,000 VNĐ</strong>.</li>
                <li class="mb-2">Vui lòng kiểm tra kỹ thông tin ngân hàng trước khi xác nhận. Nếu sai thông tin, tiền sẽ
                  được hoàn lại ví.</li>
                <li>Mọi thắc mắc xin liên hệ bộ phận CSKH để được hỗ trợ.</li>
              </ul>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const walletBalance = ref(0);
const isSubmittingWithdraw = ref(false);
let currentUserId = null;

// Biến quản lý trạng thái liên kết thẻ
const hasLinkedBank = ref(false);
const saveBankInfo = ref(true); 

const withdrawForm = ref({
  soTien: '',
  nganHang: '',
  stk: '',
  chuTk: ''
});

const danhSachNganHang = ref([
  { code: 'VCB', name: 'Vietcombank - Ngân hàng Ngoại Thương' },
  { code: 'CTG', name: 'VietinBank - Ngân hàng Công Thương' },
  { code: 'BIDV', name: 'BIDV - Đầu tư và Phát triển VN' },
  { code: 'VBA', name: 'Agribank - Ngân hàng Nông nghiệp' },
  { code: 'MB', name: 'MB - Ngân hàng Quân Đội' },
  { code: 'TCB', name: 'Techcombank - Ngân hàng Kỹ Thương' },
  { code: 'VPB', name: 'VPBank - Việt Nam Thịnh Vượng' },
  { code: 'ACB', name: 'ACB - Ngân hàng Á Châu' },
  { code: 'STB', name: 'Sacombank - Sài Gòn Thương Tín' },
  { code: 'HDB', name: 'HDBank - Phát triển TP.HCM' },
  { code: 'TPB', name: 'TPBank - Ngân hàng Tiên Phong' },
  { code: 'VIB', name: 'VIB - Ngân hàng Quốc Tế' },
  { code: 'SHB', name: 'SHB - Sài Gòn - Hà Nội' },
  { code: 'EIB', name: 'Eximbank - Ngân hàng Xuất Nhập Khẩu' },
  { code: 'MSB', name: 'MSB - Ngân hàng Hàng Hải' },
  { code: 'OCB', name: 'OCB - Ngân hàng Phương Đông' },
  { code: 'SCB', name: 'SCB - Ngân hàng Sài Gòn' },
  { code: 'NAB', name: 'Nam A Bank - Ngân hàng Nam Á' },
  { code: 'LPB', name: 'LPBank - Ngân hàng Lộc Phát' },
  { code: 'SGB', name: 'Saigonbank - Sài Gòn Công Thương' },
  { code: 'BAB', name: 'Bac A Bank - Ngân hàng Bắc Á' },
  { code: 'BVB', name: 'BaoViet Bank - Ngân hàng Bảo Việt' },
  { code: 'KLB', name: 'Kienlongbank - Ngân hàng Kiên Long' },
  { code: 'VAB', name: 'VietABank - Ngân hàng Việt Á' },
  { code: 'PGB', name: 'PGBank - Xăng dầu Petrolimex' },
  { code: 'NVB', name: 'NCB - Ngân hàng Quốc Dân' },
  { code: 'OJB', name: 'OceanBank - Ngân hàng Đại Dương' },
  { code: 'GPB', name: 'GPBank - Dầu Khí Toàn Cầu' },
  { code: 'CBB', name: 'CBBank - Ngân hàng Xây Dựng' },
  { code: 'VCCB', name: 'Viet Capital Bank - Bản Việt' },
  { code: 'ABB', name: 'ABBank - Ngân hàng An Bình' },
  { code: 'SFCB', name: 'DongA Bank - Ngân hàng Đông Á' },
  { code: 'VRB', name: 'VRB - Liên doanh Việt - Nga' },
  { code: 'UOB', name: 'UOB - United Overseas Bank' },
  { code: 'HSBC', name: 'HSBC - Ngân hàng TNHH MTV HSBC' },
  { code: 'SCVN', name: 'Standard Chartered Vietnam' },
  { code: 'SHBVN', name: 'Shinhan Bank Vietnam' },
  { code: 'WVN', name: 'Woori Bank Vietnam' },
  { code: 'TIMO', name: 'Timo - Ngân hàng số Timo' },
  { code: 'CAKE', name: 'Cake - Ngân hàng số Cake by VPBank' }
]);

const formatCurrency = (val) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(val || 0);
};

const getBankName = (code) => {
  const bank = danhSachNganHang.value.find(b => b.code === code);
  return bank ? bank.name : code;
};

const getAuthHeaders = () => {
  const userStr = localStorage.getItem('user');
  let token = null;
  if (userStr) {
    const userData = JSON.parse(userStr);
    token = userData.token || userData.accessToken;
  }
  if (!token) token = localStorage.getItem('token');
  return token ? { headers: { Authorization: `Bearer ${token}` } } : {};
};

// 🔥 ĐÃ SỬA: Hàm lấy số dư được viết lại để ăn khớp 100% với cột so_du
const fetchWalletBalance = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/api/vi-tien/so-du/${currentUserId}`, getAuthHeaders());
    const data = response.data;
    
    // Xử lý 2 trường hợp: API trả về 1 con số nguyên, hoặc trả về một Object có chứa trường soDu
    if (typeof data === 'number') {
        walletBalance.value = data;
    } else if (data && data.soDu !== undefined) {
        walletBalance.value = data.soDu;
    } else {
        walletBalance.value = 0;
    }
  } catch (error) {
    console.error("Lỗi lấy số dư ví:", error);
    walletBalance.value = 0;
  }
};

const checkLinkedBank = () => {
  const savedBankStr = localStorage.getItem(`savedBank_${currentUserId}`);
  if (savedBankStr) {
    const savedBank = JSON.parse(savedBankStr);
    withdrawForm.value.nganHang = savedBank.nganHang;
    withdrawForm.value.stk = savedBank.stk;
    withdrawForm.value.chuTk = savedBank.chuTk;
    hasLinkedBank.value = true;
  }
};

const changeBankAccount = () => {
  hasLinkedBank.value = false;
  withdrawForm.value.nganHang = '';
  withdrawForm.value.stk = '';
  withdrawForm.value.chuTk = '';
};

const submitWithdrawRequest = async () => {
  if (withdrawForm.value.soTien > walletBalance.value) {
    alert("Số dư ví không đủ để thực hiện giao dịch này!");
    return;
  }

  isSubmittingWithdraw.value = true;
  try {
    const payload = {
      nguoiDungId: currentUserId,
      soTien: withdrawForm.value.soTien,
      nganHang: withdrawForm.value.nganHang,
      soTaiKhoan: withdrawForm.value.stk,
      chuTaiKhoan: withdrawForm.value.chuTk.toUpperCase()
    };

    await axios.post('http://localhost:8080/api/vi-tien/rut-tien', payload, getAuthHeaders());

    if (!hasLinkedBank.value && saveBankInfo.value) {
      localStorage.setItem(`savedBank_${currentUserId}`, JSON.stringify({
        nganHang: withdrawForm.value.nganHang,
        stk: withdrawForm.value.stk,
        chuTk: withdrawForm.value.chuTk.toUpperCase()
      }));
    }

    alert("🎉 Đã gửi yêu cầu rút tiền thành công! Vui lòng chờ Sàn duyệt.");
    router.push('/profile');

  } catch (error) {
    console.error("Lỗi gửi yêu cầu rút tiền:", error);
    alert(error.response?.data?.error || "❌ Có lỗi xảy ra, vui lòng thử lại sau!");
  } finally {
    isSubmittingWithdraw.value = false;
  }
};

onMounted(() => {
  const storedUser = localStorage.getItem('user');
  if (!storedUser) {
    alert("Vui lòng đăng nhập!");
    router.push('/login');
    return;
  }
  currentUserId = JSON.parse(storedUser).id || JSON.parse(storedUser).nguoiDungId;

  fetchWalletBalance();
  checkLinkedBank(); 
});
</script>

<style scoped>
/* Chỉ giữ lại duy nhất 1 CSS ẩn mũi tên của ô input số tiền */
input[type="number"]::-webkit-inner-spin-button,
input[type="number"]::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}
</style>