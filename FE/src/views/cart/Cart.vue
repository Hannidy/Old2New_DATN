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
                      <th class="ps-4 py-3 text-center" style="width: 50px;">
                        <input class="form-check-input fs-5" type="checkbox" v-model="selectAll">
                      </th>
                      <th class="py-3 text-secondary small text-uppercase">Sản phẩm</th>
                      <th class="py-3 text-secondary small text-uppercase text-center">Số lượng</th>
                      <th class="py-3 text-secondary small text-uppercase text-center">Đơn giá</th>
                      <th class="py-3 text-secondary small text-uppercase text-end pe-4">Thành tiền</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="(item, index) in cartItems" :key="item.id">
                      <td class="ps-4 py-4 text-center">
                        <input class="form-check-input fs-5" type="checkbox" :value="item.id" v-model="selectedItems">
                      </td>
                      <td class="py-4">
                        <div class="d-flex align-items-center">
                          <img :src="item.hinhAnh || 'https://via.placeholder.com/80'" class="rounded-3 border shadow-sm" style="width: 75px; height: 75px; object-fit: cover;">
                          <div class="ms-3">
                            <h6 class="mb-1 fw-bold text-dark text-truncate" style="max-width: 250px;">{{ item.tenSanPham }}</h6>
                            <button @click="removeItem(index)" class="btn btn-link btn-sm text-danger p-0 text-decoration-none">
                              <small><i class="bi bi-trash"></i> Xóa khỏi giỏ</small>
                            </button>
                          </div>
                        </div>
                      </td>
                      <td class="text-center">1</td>
                      <td class="text-center fw-semibold">{{ formatCurrency(item.gia) }}</td>
                      <td class="text-end pe-4 fw-bold text-danger fs-5">{{ formatCurrency(item.gia) }}</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
          
          <div class="d-flex justify-content-between align-items-center mt-4">
            <router-link to="/" class="btn btn-outline-dark btn-sm px-4 rounded-pill">← Tiếp tục mua sắm</router-link>
            <button @click="clearCart" class="btn btn-outline-danger btn-sm px-4 rounded-pill">
              <i class="bi bi-trash-fill"></i> Xóa tất cả giỏ hàng
            </button>
          </div>
        </div>

        <div class="col-lg-4">
          <div class="card border-0 shadow-sm rounded-4 sticky-summary">
            <div class="card-body p-4">
              <h5 class="fw-bold mb-4 border-bottom pb-2">Tóm tắt đơn hàng</h5>
              
              <div class="mb-4">
                <h6 class="fw-bold mb-2">📍 Giao hàng đến:</h6>
                <select v-model="selectedAddressId" @change="calculateShippingFee" class="form-select border-primary">
                  <option value="null" disabled>-- Chọn địa chỉ nhận hàng --</option>
                  <option v-for="addr in userAddresses" :key="addr.diaChiId" :value="addr.diaChiId">{{ addr.diaChiChiTiet }}</option>
                </select>
              </div>

              <div class="d-flex justify-content-between mb-2">
                <span class="text-muted">Đã chọn:</span>
                <span class="fw-bold">{{ selectedItems.length }} sản phẩm</span>
              </div>
              <div class="d-flex justify-content-between mb-2">
                <span class="text-muted">Tổng tiền hàng:</span>
                <span class="fw-bold">{{ formatCurrency(totalPrice) }}</span>
              </div>
              <div class="d-flex justify-content-between mb-3 border-bottom pb-3">
                <span class="text-muted">Phí vận chuyển:</span>
                <span v-if="isCalculatingFee" class="spinner-border spinner-border-sm"></span>
                <span v-else class="fw-bold text-success">+ {{ formatCurrency(shippingFee) }}</span>
              </div>

              <div class="mb-4">
                <div class="d-flex justify-content-between align-items-center">
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
                  <input class="form-check-input" type="radio" v-model="paymentMethod" value="VIETQR" id="vietqr">
                  <label class="form-check-label text-primary fw-bold" for="vietqr">Chuyển khoản</label>
                </div>
              </div>

              <button @click="checkout" :disabled="!selectedAddressId || isCalculatingFee || selectedItems.length === 0" class="btn btn-danger w-100 py-3 fw-bold fs-5 shadow rounded-3">
                <span v-if="isProcessing" class="spinner-border spinner-border-sm me-2"></span>
                XÁC NHẬN ĐẶT HÀNG
              </button>
            </div>
          </div>
        </div>
      </div>

      <div v-else class="text-center py-5 bg-white rounded-4 shadow-sm">
        <div class="display-1 mb-4">🛒</div>
        <h3 class="fw-bold">Giỏ hàng đang trống!</h3>
        <router-link to="/" class="btn btn-danger px-5 py-2 rounded-pill">QUAY LẠI MUA SẮM</router-link>
      </div>
    </main>
    <AppFooter />
  </div>
</template>

<script setup>
import axios from 'axios';
import { ref, computed, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import AppHeader from '@/layouts/Header.vue';
import AppFooter from '@/layouts/Footer.vue';

const route = useRoute();
const router = useRouter();

const cartItems = ref([]);
const paymentMethod = ref('VIETQR');
const isProcessing = ref(false);
const userAddresses = ref([]);
const selectedAddressId = ref("null");
const shippingFee = ref(0);
const isCalculatingFee = ref(false);
const selectedItems = ref([]);

const formatCurrency = (val) => new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(val || 0);

const loadCart = async () => {
  const storedUser = JSON.parse(localStorage.getItem('user'));
  if (!storedUser) return;
  const userId = storedUser.id || storedUser.nguoiDungId;
  const token = storedUser.token || storedUser.accessToken;

  try {
    const response = await axios.get(`http://localhost:8080/api/cart/${userId}`, {
      headers: { Authorization: `Bearer ${token}` }
    });
    cartItems.value = response.data || [];
    
    // TỰ ĐỘNG CHỌN NẾU ĐI TỪ MUA NGAY
    const autoSelectId = route.query.autoSelect;
    if (autoSelectId) {
      selectedItems.value = [Number(autoSelectId)];
    } else {
      selectedItems.value = [];
    }
  } catch (error) { console.error(error); }
};

const clearCart = async () => {
  if (confirm("Xóa toàn bộ giỏ hàng?")) {
    const storedUser = JSON.parse(localStorage.getItem('user'));
    const userId = storedUser.id || storedUser.nguoiDungId;
    const token = storedUser.token || storedUser.accessToken;
    try {
      await axios.delete(`http://localhost:8080/api/cart/${userId}`, { headers: { Authorization: `Bearer ${token}` } });
      cartItems.value = [];
      selectedItems.value = [];
    } catch (error) { alert("Lỗi khi xóa giỏ hàng!"); }
  }
};

const selectAll = computed({
  get: () => cartItems.value.length > 0 && selectedItems.value.length === cartItems.value.length,
  set: (val) => { selectedItems.value = val ? cartItems.value.map(i => i.id) : []; }
});

const removeItem = async (index) => {
  if (confirm("Xóa sản phẩm này?")) {
    const idToRemove = cartItems.value[index].id;
    cartItems.value.splice(index, 1);
    selectedItems.value = selectedItems.value.filter(id => id !== idToRemove);
    
    const storedUser = JSON.parse(localStorage.getItem('user'));
    await axios.post(`http://localhost:8080/api/cart/${storedUser.id}`, cartItems.value, {
      headers: { Authorization: `Bearer ${storedUser.token}` }
    });
    calculateShippingFee();
  }
};

const totalPrice = computed(() => cartItems.value.filter(i => selectedItems.value.includes(i.id)).reduce((a, b) => a + b.gia, 0));

const fetchUserAddresses = async () => {
  const storedUser = JSON.parse(localStorage.getItem('user'));
  if (!storedUser) return;
  try {
    const res = await axios.get(`http://localhost:8080/api/dia-chi/nguoi-dung/${storedUser.id}`, {
      headers: { Authorization: `Bearer ${storedUser.token}` }
    });
    userAddresses.value = res.data;
    const def = res.data.find(a => a.diaChiMacDinh === 1);
    if (def) { selectedAddressId.value = def.diaChiId; calculateShippingFee(); }
  } catch (error) { console.error(error); }
};

const calculateShippingFee = async () => {
  if (selectedAddressId.value === "null" || selectedItems.value.length === 0) { shippingFee.value = 0; return; }
  isCalculatingFee.value = true;
  const addr = userAddresses.value.find(a => a.diaChiId === selectedAddressId.value);
  try {
    const res = await axios.post('http://localhost:8080/api/ghn/fee', {
      fromDistrictId: 1454, fromWardCode: "21211",
      toDistrictId: addr.huyenCode, toWardCode: addr.phuongXaId,
      weight: 500 * selectedItems.value.length
    }, { headers: { Authorization: `Bearer ${JSON.parse(localStorage.getItem('user')).token}` } });
    shippingFee.value = res.data.fee;
  } catch (error) { shippingFee.value = 0; } finally { isCalculatingFee.value = false; }
};

watch(selectedItems, () => calculateShippingFee());

const checkout = async () => {
  isProcessing.value = true;
  const storedUser = JSON.parse(localStorage.getItem('user'));
  const selectedOnes = cartItems.value.filter(i => selectedItems.value.includes(i.id));
  
  const payload = {
    nguoiDungId: storedUser.id,
    diaChiId: selectedAddressId.value,
    tongTienHang: totalPrice.value,
    tongTienShip: shippingFee.value,
    tongThanhTien: totalPrice.value + shippingFee.value,
    phuongThucThanhToan: paymentMethod.value,
    chiTietDonHangs: selectedOnes.map(i => ({ sanPhamId: i.id, soLuongMua: 1, giaLucMua: i.gia }))
  };

  try {
    const res = await axios.post('http://localhost:8080/api/don-hang/tao-don', payload);
    const remain = cartItems.value.filter(i => !selectedItems.value.includes(i.id));
    if (remain.length > 0) await axios.post(`http://localhost:8080/api/cart/${storedUser.id}`, remain, { headers: { Authorization: `Bearer ${storedUser.token}` } });
    else await axios.delete(`http://localhost:8080/api/cart/${storedUser.id}`, { headers: { Authorization: `Bearer ${storedUser.token}` } });

    if (res.data.qrUrl) router.push(`/thanh-toan/${res.data.donHangId}`);
    else { alert("Đặt hàng thành công!"); router.push('/quan-ly-don-hang'); }
  } catch (error) { alert("Lỗi khi tạo đơn hàng!"); } finally { isProcessing.value = false; }
};

onMounted(() => { loadCart(); fetchUserAddresses(); window.scrollTo(0, 0); });
</script>

<style scoped>
.main-content { margin-top: 160px; }
.sticky-summary { position: sticky; top: 180px; }
@media (max-width: 991px) { .sticky-summary { position: static; } }
</style>