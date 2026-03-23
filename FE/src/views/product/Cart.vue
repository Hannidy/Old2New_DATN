<template>
  <div class="bg-light min-vh-100 pb-5">
    <AppHeader />

    <main class="container main-content py-4">
      <div class="d-flex align-items-center mb-4">
        <h2 class="fw-bold mb-0">🛒 Giỏ hàng của bạn</h2>
        <span class="ms-3 badge bg-dark px-3 py-2 rounded-pill">Sản phẩm độc bản</span>
      </div>

      <div v-if="cartItems.length > 0" class="row g-4">
        <div class="col-lg-8">
          <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
            <div class="card-body p-0">
              <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                  <thead class="bg-white border-bottom">
                    <tr>
                      <th class="ps-4 py-3 border-0 text-secondary small text-uppercase">Sản phẩm</th>
                      <th class="py-3 border-0 text-secondary small text-uppercase text-center">Số lượng</th>
                      <th class="py-3 border-0 text-secondary small text-uppercase text-center">Đơn giá</th>
                      <th class="py-3 border-0 text-secondary small text-uppercase text-end pe-4">Thành tiền</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="(item, index) in cartItems" :key="item.id">
                      <td class="ps-4 py-4">
                        <div class="d-flex align-items-center">
                          <img :src="item.hinhAnh || 'https://via.placeholder.com/80'" 
                               class="rounded-3 border shadow-sm" 
                               style="width: 75px; height: 75px; object-fit: cover;">
                          <div class="ms-3">
                            <h6 class="mb-1 fw-bold text-dark text-truncate" style="max-width: 250px;">
                              {{ item.tenSanPham }}
                            </h6>
                            <button @click="removeItem(index)" class="btn btn-link btn-sm text-danger p-0 text-decoration-none">
                              <small><i class="bi bi-trash"></i> Xóa khỏi giỏ</small>
                            </button>
                          </div>
                        </div>
                      </td>
                      <td class="text-center">
                        <span class="badge border text-dark bg-white px-3 py-2 fw-normal">1</span>
                      </td>
                      <td class="text-center fw-semibold text-muted">
                        {{ formatCurrency(item.gia) }}
                      </td>
                      <td class="text-end pe-4 fw-bold text-danger fs-5">
                        {{ formatCurrency(item.gia) }}
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
          <router-link to="/" class="btn btn-outline-dark btn-sm mt-4 px-4 rounded-pill">
            ← Tiếp tục mua sắm
          </router-link>
        </div>

        <div class="col-lg-4">
          <div class="card border-0 shadow-sm rounded-4 sticky-summary">
            <div class="card-body p-4">
              <h5 class="fw-bold mb-4 border-bottom pb-2">Tóm tắt đơn hàng</h5>
              
              <div class="mb-4">
                <h6 class="fw-bold mb-2">📍 Giao hàng đến:</h6>
                <select v-model="selectedAddressId" @change="calculateShippingFee" class="form-select border-primary shadow-sm">
                  <option value="null" disabled>-- Chọn địa chỉ nhận hàng --</option>
                  <option v-for="addr in userAddresses" :key="addr.diaChiId" :value="addr.diaChiId">
                    {{ addr.diaChiChiTiet }}
                  </option>
                </select>
                <small v-if="userAddresses.length === 0" class="text-danger d-block mt-1">
                  Bạn chưa có địa chỉ. Vui lòng vào Hồ sơ để thêm!
                </small>
              </div>

              <div class="d-flex justify-content-between mb-2">
                <span class="text-muted">Tổng tiền hàng:</span>
                <span class="fw-bold">{{ formatCurrency(totalPrice) }}</span>
              </div>
              
              <div class="d-flex justify-content-between mb-3 border-bottom pb-3">
                <span class="text-muted">Phí vận chuyển (GHN):</span>
                <span v-if="isCalculatingFee" class="text-primary spinner-border spinner-border-sm" role="status"></span>
                <span v-else-if="shippingFee > 0" class="fw-bold text-success">+ {{ formatCurrency(shippingFee) }}</span>
                <span v-else class="fw-bold text-warning">Chưa tính</span>
              </div>

              <div class="mb-4">
                <div class="d-flex justify-content-between align-items-center mb-1">
                  <span class="fw-bold fs-5">Tổng thanh toán:</span>
                  <span class="fw-bold fs-3 text-danger">{{ formatCurrency(totalPrice + shippingFee) }}</span>
                </div>
              </div>

              <div class="mb-4 border-top pt-3">
                <h6 class="fw-bold mb-3">Phương thức thanh toán</h6>
                <div class="form-check mb-2">
                  <input class="form-check-input" type="radio" v-model="paymentMethod" value="COD" id="cod">
                  <label class="form-check-label" for="cod">Thanh toán khi nhận hàng (COD)</label>
                </div>
                <div class="form-check">
                  <input class="form-check-input" type="radio" v-model="paymentMethod" value="VNPAY" id="vnpay">
                  <label class="form-check-label text-primary fw-bold" for="vnpay">Ví VNPAY / Thẻ Ngân hàng</label>
                </div>
              </div>

              <button @click="checkout" :disabled="!selectedAddressId || isCalculatingFee" class="btn btn-danger w-100 py-3 fw-bold fs-5 shadow rounded-3">
                XÁC NHẬN THANH TOÁN
              </button>
            </div>
          </div>
        </div>
      </div>

      <div v-else class="text-center py-5 bg-white rounded-4 shadow-sm border">
        <div class="display-1 mb-4">🛒</div>
        <h3 class="fw-bold">Giỏ hàng đang trống!</h3>
        <router-link to="/" class="btn btn-danger px-5 py-2 rounded-pill fw-bold shadow-sm">
          QUAY LẠI MUA SẮM
        </router-link>
      </div>
    </main>
    <AppFooter />
  </div>
</template>

<script setup>
import axios from 'axios';
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import AppHeader from '@/layouts/Header.vue';
import AppFooter from '@/layouts/Footer.vue';

const route = useRoute();
const router = useRouter();

const cartItems = ref([]);
const paymentMethod = ref('VNPAY'); 

// --- CÁC BIẾN MỚI CHO GIAO HÀNG NHANH ---
const userAddresses = ref([]);
const selectedAddressId = ref("null");
const shippingFee = ref(0);
const isCalculatingFee = ref(false);

const loadCart = () => {
  const savedCart = localStorage.getItem('cart');
  if (savedCart) {
    cartItems.value = JSON.parse(savedCart);
  }
};

const formatCurrency = (val) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(val || 0);
};

const removeItem = (index) => {
  if (confirm("Bạn muốn bỏ sản phẩm này khỏi giỏ?")) {
    cartItems.value.splice(index, 1);
    localStorage.setItem('cart', JSON.stringify(cartItems.value));
    calculateShippingFee(); // Xóa đi thì tính lại tiền ship (vì giảm cân nặng)
  }
};

const totalPrice = computed(() => {
  return cartItems.value.reduce((acc, item) => acc + item.gia, 0);
});

// ==========================================
// 💥 HÀM MỚI: LẤY DANH SÁCH ĐỊA CHỈ TỪ BACKEND
// ==========================================
const fetchUserAddresses = async () => {
  const storedUser = localStorage.getItem('user');
  if (!storedUser) return;
  const token = JSON.parse(storedUser).token || JSON.parse(storedUser).accessToken;
  const userId = JSON.parse(storedUser).id || JSON.parse(storedUser).nguoiDungId;

  try {
    const response = await axios.get(`http://localhost:8080/api/dia-chi/nguoi-dung/${userId}`, {
      headers: { Authorization: `Bearer ${token}` }
    });
    userAddresses.value = response.data;
    
    // Tự động chọn địa chỉ mặc định nếu có
    const defaultAddr = userAddresses.value.find(a => a.diaChiMacDinh === 1);
    if (defaultAddr) {
      selectedAddressId.value = defaultAddr.diaChiId;
      calculateShippingFee(); // Gọi API tính phí ship ngay lập tức
    }
  } catch (error) {
    console.error("Lỗi lấy địa chỉ:", error);
  }
};

// ==========================================
// 💥 HÀM MỚI: GỌI API GHN ĐỂ TÍNH PHÍ SHIP
// ==========================================
const calculateShippingFee = async () => {
  if (selectedAddressId.value === "null" || cartItems.value.length === 0) return;
  
  isCalculatingFee.value = true;
  const storedUser = localStorage.getItem('user');
  const token = JSON.parse(storedUser).token || JSON.parse(storedUser).accessToken;

  // Tìm địa chỉ mà khách hàng vừa chọn trong dropdown
  const selectedAddr = userAddresses.value.find(a => a.diaChiId === selectedAddressId.value);
    console.log("Địa chỉ khách chọn:", selectedAddr);
    // Check kỹ xem nó có bị rỗng không
  if (!selectedAddr || !selectedAddr.huyenCode || !selectedAddr.phuongXaId) {
      alert("Địa chỉ này chưa có Mã Huyện hoặc Xã chuẩn! Vui lòng tạo lại địa chỉ.");
      isCalculatingFee.value = false;
      return;
  }
  try {
    const res = await axios.post('http://localhost:8080/api/ghn/fee', {
        fromDistrictId: 1454, // Kho mặc định của bạn ở Quận 12 (để nguyên)
        fromWardCode: "21211", 
        

        toDistrictId: selectedAddr.huyenCode, 
        toWardCode: selectedAddr.phuongXaId, 
        
        weight: 500 * cartItems.value.length 
    }, {
        headers: { Authorization: `Bearer ${token}` }
    });

    shippingFee.value = res.data.fee; // 🎯 CẬP NHẬT CON SỐ LÊN GIAO DIỆN!
  } catch (error) {
    console.error("Lỗi tính phí ship:", error);
    shippingFee.value = 0;
    alert("Không thể tính phí vận chuyển cho tuyến đường này!");
  } finally {
    isCalculatingFee.value = false;
  }
};


const checkout = async () => {
  if (selectedAddressId.value === "null") {
    alert("Vui lòng chọn địa chỉ giao hàng!");
    return;
  }

  const storedUser = localStorage.getItem('user');
  if (!storedUser) {
    alert("Vui lòng đăng nhập để thanh toán đơn hàng!");
    router.push('/login');
    return;
  }
  
  const currentUserId = JSON.parse(storedUser).id || JSON.parse(storedUser).nguoiDungId; 

  const chiTietDonHangs = cartItems.value.map(item => ({
    sanPhamId: item.id,
    soLuongMua: 1, 
    giaLucMua: item.gia
  }));

  const orderPayload = {
    nguoiDungId: currentUserId,
    diaChiId: selectedAddressId.value,
    tongTienHang: totalPrice.value,
    tongTienShip: shippingFee.value,
    tongThanhTien: totalPrice.value + shippingFee.value,
    phuongThucThanhToan: paymentMethod.value,
    chiTietDonHangs: chiTietDonHangs
  };

  try {
    const response = await axios.post('http://localhost:8080/api/don-hang/tao-don', orderPayload);
    
    // Ghi chú: Có thể Spring Boot trả về 200 hoặc 201 tùy bạn cấu hình
    if (response.status === 201 || response.status === 200) { 
      
      if (response.data.paymentUrl) {
         //  CHỈ CHUYỂN HƯỚNG SANG VNPAY - KHOAN XÓA GIỎ HÀNG!
         window.location.href = response.data.paymentUrl; 
      } else {
         //  NẾU LÀ COD THÌ XÓA GIỎ HÀNG VÀ BÁO THÀNH CÔNG
         cartItems.value = [];
         localStorage.removeItem('cart');
         alert("🎉 Đặt hàng COD thành công! Mã đơn: " + response.data.donHangId);
         router.push('/');
      }
    } 
  } catch (error) {
    alert("❌ Có lỗi xảy ra khi tạo đơn hàng. Vui lòng thử lại!");
  }
};

onMounted(() => {
  loadCart();
  fetchUserAddresses(); //  Gọi lấy địa chỉ ngay khi vào giỏ hàng
  window.scrollTo(0, 0);
});

onMounted(async () => {
  if (route.query.vnp_ResponseCode) {
    try {
      const response = await axios.get('http://localhost:8080/api/don-hang/vnpay-return', {
        params: route.query
      });
      if (response.data.status === 'success') {
        alert('🎉 CHÚC MỪNG! Thanh toán VNPAY thành công!');
        cartItems.value = [];
        localStorage.removeItem('cart');
      }
    } catch (error) {
      alert('❌ Giao dịch bị hủy hoặc thanh toán thất bại!');
    } finally {
      router.replace('/');
    }
  }
});
</script>

<style scoped>
/* Giữ nguyên CSS cũ của bạn */
.main-content { margin-top: 160px; }
.sticky-summary { position: sticky; top: 180px; }
.border-dashed { border: 1px dashed #dee2e6 !important; }
.table > tbody > tr { border-bottom: 1px solid #f8f9fa; }
@media (max-width: 991px) { .sticky-summary { position: static; } }
</style>