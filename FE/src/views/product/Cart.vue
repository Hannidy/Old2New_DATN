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
              
              <div class="d-flex justify-content-between mb-3">
                <span class="text-muted">Tổng sản phẩm:</span>
                <span class="fw-bold">{{ cartItems.length }} món</span>
              </div>
              
              <div class="d-flex justify-content-between mb-4 border-bottom pb-3">
                <span class="text-muted">Số đơn hàng sẽ tạo:</span>
                <span class="text-primary fw-bold">{{ cartItems.length }} đơn riêng</span>
              </div>

              <div class="mb-4">
                <div class="d-flex justify-content-between align-items-center mb-1">
                  <span class="fw-bold fs-5">Tổng số tiền:</span>
                  <span class="fw-bold fs-3 text-danger">{{ formatCurrency(totalPrice) }}</span>
                </div>
                <small class="text-muted d-block text-end">* Chưa bao gồm phí ship từng món</small>
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

              <button @click="checkout" class="btn btn-danger w-100 py-3 fw-bold fs-5 shadow rounded-3">
                XÁC NHẬN THANH TOÁN
              </button>
              
              <div class="mt-4 p-3 bg-light rounded-3 border-dashed">
                <p class="small text-secondary mb-0">
                  <i class="bi bi-info-circle-fill"></i> 
                  Hệ thống sẽ tự động tạo đơn hàng riêng cho từng món đồ để bạn dễ dàng theo dõi hành trình vận chuyển.
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div v-else class="text-center py-5 bg-white rounded-4 shadow-sm border">
        <div class="display-1 mb-4">🛒</div>
        <h3 class="fw-bold">Giỏ hàng đang trống!</h3>
        <p class="text-muted mb-4">Đừng để lỡ những món đồ cũ chất lượng nhé.</p>
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

const route = useRoute();   // Để đọc đường link URL
const router = useRouter(); // Để chuyển trang

const cartItems = ref([]);

const paymentMethod = ref('VNPAY'); // Mặc định để VNPAY 

// 1. Tải giỏ hàng từ localStorage
const loadCart = () => {
  const savedCart = localStorage.getItem('cart');
  if (savedCart) {
    cartItems.value = JSON.parse(savedCart);
  }
};

// 2. Định dạng tiền VND
const formatCurrency = (val) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(val || 0);
};

// 3. Xóa sản phẩm
const removeItem = (index) => {
  if (confirm("Bạn muốn bỏ sản phẩm này khỏi giỏ?")) {
    cartItems.value.splice(index, 1);
    localStorage.setItem('cart', JSON.stringify(cartItems.value));
  }
};

// 4. Tính tổng tiền
const totalPrice = computed(() => {
  return cartItems.value.reduce((acc, item) => acc + item.gia, 0);
});

// Thanh toán VNPAY VS COD
const checkout = async () => {
  if (cartItems.value.length === 0) {
    alert("Giỏ hàng của bạn đang trống!");
    return;
  }

  // ==========================================
  // BƯỚC 1: LẤY ID NGƯỜI DÙNG TỪ LOCAL STORAGE
  // ==========================================
  const storedUser = localStorage.getItem('user');
  
  if (!storedUser) {
    alert("Vui lòng đăng nhập để thanh toán đơn hàng!");
    router.push('/login'); // Chưa đăng nhập thì đá văng ra trang Login
    return;
  }
  
  const userData = JSON.parse(storedUser);
  // Lấy chính xác ID của người đang đăng nhập (như tài khoản Duy Tuấn của bạn là 18)
  const currentUserId = userData.id || userData.nguoiDungId; 

  // ==========================================
  // BƯỚC 2: CHUẨN BỊ DỮ LIỆU GỬI XUỐNG BACKEND
  // ==========================================
  // Chuyển đổi giỏ hàng sang định dạng OrderItemDto của Spring Boot
  const chiTietDonHangs = cartItems.value.map(item => ({
    sanPhamId: item.id,
    soLuongMua: 1, // Hàng độc bản luôn là 1
    giaLucMua: item.gia
  }));

  // Gom tất cả thành OrderRequestDto
  const orderPayload = {
    nguoiDungId: currentUserId, // ĐÃ ĐỔI THÀNH ID ĐỘNG CỦA NGƯỜI ĐĂNG NHẬP!
    diaChiId: 1,    // (Phần chọn địa chỉ sau này mình làm giao diện thì sẽ đổi thành động sau)
    tongTienHang: totalPrice.value,
    tongTienShip: 30000, // Giả sử phí ship đồng giá 30k
    tongThanhTien: totalPrice.value + 30000,
    phuongThucThanhToan: paymentMethod.value,
    chiTietDonHangs: chiTietDonHangs
  };

  try {
    // ==========================================
    // BƯỚC 3: GỌI API VÀ XỬ LÝ PHẢN HỒI
    // ==========================================
    const response = await axios.post('http://localhost:8080/api/don-hang/tao-don', orderPayload);

    if (response.status === 201) {
      // Xóa giỏ hàng
      cartItems.value = [];
      localStorage.removeItem('cart');

      // KIỂM TRA NẾU CÓ LINK VNPAY THÌ CHUYỂN HƯỚNG
      if (response.data.paymentUrl) {
         window.location.href = response.data.paymentUrl; // Bắn khách qua VNPAY
      } else {
         alert("🎉 Đặt hàng COD thành công! Mã đơn: " + response.data.donHangId);
         router.push('/');
      }
    } 

  } catch (error) {
    console.error("Lỗi khi đặt hàng:", error);
    alert("❌ Có lỗi xảy ra khi tạo đơn hàng. Vui lòng thử lại!");
  }
};




onMounted(() => {
  loadCart();
  window.scrollTo(0, 0);
});

  onMounted(async () => {
  // Khi trang vừa mở lên, kiểm tra xem trên URL có chữ vnp_ResponseCode không
  if (route.query.vnp_ResponseCode) {
    try {
      // Gửi toàn bộ cục URL đó xuống cho Spring Boot xử lý
      const response = await axios.get('http://localhost:8080/api/don-hang/vnpay-return', {
        params: route.query
      });

      if (response.data.status === 'success') {
        alert('🎉 CHÚC MỪNG! Thanh toán VNPAY thành công. Đơn hàng đã được xác nhận!');
        
        // Xóa sạch giỏ hàng
        cartItems.value = [];
        localStorage.removeItem('cart');
      }
    } catch (error) {
      alert('❌ Giao dịch bị hủy hoặc thanh toán thất bại!');
    } finally {
      // Dọn dẹp cái link dài ngoằng cho sạch sẽ và đá về trang chủ
      router.replace('/');
    }
  }
});


</script>

<style scoped>
.main-content {
  margin-top: 160px; /* Chừa chỗ cho Header Fixed */
}
.sticky-summary {
  position: sticky;
  top: 180px;
}
.border-dashed {
  border: 1px dashed #dee2e6 !important;
}
.table > tbody > tr {
  border-bottom: 1px solid #f8f9fa;
}
@media (max-width: 991px) {
  .sticky-summary { position: static; }
}
</style>