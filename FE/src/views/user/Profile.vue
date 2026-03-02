<template>
  <div class="container py-5 bg-light min-vh-100">
    <div class="row">
      
      <div class="col-md-3 mb-4">
        <div class="bg-white rounded shadow-sm p-3">
          <div class="d-flex align-items-center gap-3 mb-4 border-bottom pb-3">
            
            <div class="position-relative cursor-pointer" @click="triggerAvatarUpload" title="Nhấn để đổi ảnh đại diện">
              <img v-if="userProfile.anhDaiDien" :src="userProfile.anhDaiDien" class="avatar-circle object-fit-cover shadow-sm" />
              <div v-else class="avatar-circle bg-dark text-white fw-bold fs-4 d-flex align-items-center justify-content-center shadow-sm">
                {{ userProfile.hoVaTen ? userProfile.hoVaTen.charAt(0).toUpperCase() : 'U' }}
              </div>
              <span class="position-absolute bottom-0 end-0 bg-white border rounded-circle d-flex align-items-center justify-content-center" style="width: 20px; height: 20px; font-size: 10px;">📷</span>
            </div>
            <input type="file" ref="avatarInput" class="d-none" accept="image/*" @change="handleAvatarChange">

            <div>
              <div class="fw-bold text-dark">{{ userProfile.hoVaTen || 'Khách hàng' }}</div>
              <div class="small text-muted text-truncate" style="max-width: 150px;">
                <span class="text-secondary">✎ Sửa hồ sơ</span>
              </div>
            </div>
          </div>

          <div class="menu-list">
            <div :class="['menu-item', currentTab === 'profile' ? 'active' : '']" @click="currentTab = 'profile'">
              <span class="icon">👤</span> Hồ sơ của tôi
            </div>
            <div :class="['menu-item', currentTab === 'address' ? 'active' : '']" @click="currentTab = 'address'">
              <span class="icon">📍</span> Sổ địa chỉ
            </div>
            <div class="menu-item" @click="router.push('/quan-ly-don-hang')">
              <span class="icon">📦</span> Đơn mua
            </div>
          </div>
        </div>
      </div>

      <div class="col-md-9">
        <div class="bg-white rounded shadow-sm p-4 p-md-5">
          
          <div v-if="currentTab === 'profile'">
            <h2 class="h4 fw-bold mb-1 text-dark">Hồ sơ của tôi</h2>
            <p class="text-muted small mb-4 pb-3 border-bottom">Quản lý thông tin hồ sơ để bảo mật tài khoản</p>

            <form @submit.prevent="saveProfile" class="profile-form">
              <div class="row mb-4 align-items-center">
                <div class="col-md-3 text-md-end text-muted fw-medium small">Email đăng nhập</div>
                <div class="col-md-8">
                  <input type="email" v-model="userProfile.email" class="form-control bg-light text-muted" disabled>
                </div>
              </div>
              <div class="row mb-4 align-items-center">
                <div class="col-md-3 text-md-end text-muted fw-medium small">Tên hiển thị</div>
                <div class="col-md-8">
                  <input type="text" v-model="userProfile.hoVaTen" class="form-control" required>
                </div>
              </div>
              <div class="row mb-4 align-items-center">
                <div class="col-md-3 text-md-end text-muted fw-medium small">Số điện thoại</div>
                <div class="col-md-8">
                  <input type="tel" v-model="userProfile.soDienThoai" class="form-control">
                </div>
              </div>
              <div class="row mb-4 align-items-center">
                <div class="col-md-3 text-md-end text-muted fw-medium small">Giới tính</div>
                <div class="col-md-8 d-flex gap-4">
                  <label class="d-flex align-items-center gap-2 cursor-pointer"><input type="radio" v-model="userProfile.gioiTinh" value="Nam"> Nam</label>
                  <label class="d-flex align-items-center gap-2 cursor-pointer"><input type="radio" v-model="userProfile.gioiTinh" value="Nữ"> Nữ</label>
                  <label class="d-flex align-items-center gap-2 cursor-pointer"><input type="radio" v-model="userProfile.gioiTinh" value="Khác"> Khác</label>
                </div>
              </div>
              <div class="row mb-4 align-items-center">
                <div class="col-md-3 text-md-end text-muted fw-medium small">Ngày sinh</div>
                <div class="col-md-8">
                  <input type="date" v-model="userProfile.ngaySinh" class="form-control">
                </div>
              </div>

              <div class="row mt-5">
                <div class="col-md-3"></div>
                <div class="col-md-8 d-flex gap-3">
                  <button type="submit" class="btn btn-dark px-4 py-2 fw-bold" :disabled="isSaving">
                    {{ isSaving ? 'Đang lưu...' : 'LƯU THAY ĐỔI' }}
                  </button>
                  <button type="button" class="btn btn-outline-secondary px-4 py-2 fw-bold" @click="router.push('/')">
                    🏠 QUAY LẠI TRANG CHỦ
                  </button>
                </div>
              </div>
            </form>
          </div>

          <div v-if="currentTab === 'address'">
            <div class="d-flex justify-content-between align-items-center mb-1 border-bottom pb-3">
              <div>
                <h2 class="h4 fw-bold mb-1 text-dark">Địa chỉ của bạn</h2>
                <p class="text-muted small mb-0">Quản lý địa chỉ giao hàng và lấy hàng</p>
              </div>
              <button class="btn btn-primary fw-bold" @click="showAddressForm = true" v-if="!showAddressForm">
                + Thêm địa chỉ mới
              </button>
            </div>

            <div v-if="!showAddressForm" class="mt-4">
              <div v-if="userAddresses.length === 0" class="text-center py-5 border rounded bg-light">
                <span class="fs-1">📍</span>
                <p class="text-muted mt-2">Bạn chưa có địa chỉ nào. Hãy thêm mới nhé!</p>
              </div>

              <div v-else class="address-list">
                <div v-for="addr in userAddresses" :key="addr.diaChiId" 
                     class="p-3 mb-3 border rounded bg-white shadow-sm position-relative"
                     :class="{'border-danger border-2': addr.diaChiMacDinh === 1}">
                  
                  <div class="d-flex justify-content-between align-items-start">
                    <div>
                      <div class="fw-bold text-dark fs-5 mb-1 d-flex align-items-center gap-2">
                        {{ addr.diaChiChiTiet.split(' | ')[0] }}
                        <span v-if="addr.diaChiMacDinh === 1" class="badge bg-danger ms-2" style="font-size: 10px;">Mặc định</span>
                      </div>
                      <div class="text-muted">
                        {{ addr.diaChiChiTiet.split(' | ')[1] || addr.diaChiChiTiet }}
                      </div>
                    </div>
                    
                    <div class="d-flex gap-2">
                      <button class="btn btn-sm btn-outline-primary" @click="alert('Sẽ làm chức năng Sửa sau nhé!')">Sửa</button>
                      <button class="btn btn-sm btn-outline-danger" @click="alert('Sẽ làm chức năng Xóa sau nhé!')">Xóa</button>
                    </div>
                  </div>

                </div>
              </div>
            </div>

            <form v-if="showAddressForm" @submit.prevent="saveAddress" class="mt-4 p-4 border rounded bg-light">
              
              <div class="row mb-3">
                <div class="col-md-6">
                  <label class="form-label small fw-bold">Tên người nhận</label>
                  <input type="text" v-model="addressForm.tenNguoiNhan" class="form-control" placeholder="Nhập tên đầy đủ..." required>
                </div>
                <div class="col-md-6">
                  <label class="form-label small fw-bold">Số điện thoại</label>
                  <input type="text" v-model="addressForm.soDienThoai" class="form-control" placeholder="Nhập số điện thoại..." required>
                </div>
              </div>

              <div class="row mb-3">
                <div class="col-md-4">
                  <label class="form-label small fw-bold">Tỉnh / Thành phố</label>
                  <select v-model="addressForm.tinhCode" @change="fetchDistricts" class="form-select" required>
                    <option value="" disabled>Chọn Tỉnh / Thành</option>
                    <option v-for="tinh in listProvinces" :key="tinh.code" :value="tinh.code">{{ tinh.name }}</option>
                  </select>
                </div>
                <div class="col-md-4">
                  <label class="form-label small fw-bold">Quận / Huyện</label>
                  <select v-model="addressForm.huyenCode" @change="fetchWards" class="form-select" :disabled="!addressForm.tinhCode" required>
                    <option value="" disabled>Chọn Quận / Huyện</option>
                    <option v-for="huyen in listDistricts" :key="huyen.code" :value="huyen.code">{{ huyen.name }}</option>
                  </select>
                </div>
                <div class="col-md-4">
                  <label class="form-label small fw-bold">Phường / Xã</label>
                  <select v-model="addressForm.phuongCode" class="form-select" :disabled="!addressForm.huyenCode" required>
                    <option value="" disabled>Chọn Phường / Xã</option>
                    <option v-for="xa in listWards" :key="xa.code" :value="xa.code">{{ xa.name }}</option>
                  </select>
                </div>
              </div>

              <div class="mb-3">
                <label class="form-label small fw-bold">Địa chỉ cụ thể</label>
                <textarea v-model="addressForm.diaChiChiTiet" class="form-control" rows="2" placeholder="Số nhà, tên đường..." required></textarea>
              </div>

              <div class="form-check mb-4">
                <input class="form-check-input" type="checkbox" v-model="addressForm.isDefault" id="defaultAddress">
                <label class="form-check-label text-muted small" for="defaultAddress">
                  Đặt làm địa chỉ mặc định
                </label>
              </div>

              <div class="d-flex justify-content-end gap-2">
                <button type="button" class="btn btn-outline-secondary" @click="showAddressForm = false">Trở lại</button>
                <button type="submit" class="btn btn-info text-white fw-bold px-4">Lưu Địa Chỉ</button>
              </div>
            </form>

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
const currentTab = ref('profile'); // Biến quản lý Tab ('profile' hoặc 'address')
const isSaving = ref(false);
let currentUserId = null;

// ==========================================
// LOGIC TAB 1: HỒ SƠ
// ==========================================
const userProfile = ref({
  hoVaTen: '', email: '', soDienThoai: '', gioiTinh: '', ngaySinh: '', anhDaiDien: ''
});
const avatarInput = ref(null);

onMounted(() => {
  const storedUser = localStorage.getItem('user');
  if (!storedUser) {
    alert("Vui lòng đăng nhập!");
    router.push('/login');
    return;
  }
  const userData = JSON.parse(storedUser);
  currentUserId = userData.id || userData.nguoiDungId;
  
  fetchUserData();
  fetchProvinces(); // Lấy sẵn data Tỉnh/Thành cho Tab 2
  fetchUserAddresses();
});

const fetchUserData = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/api/nguoi-dung/${currentUserId}`);
    const data = response.data;
    userProfile.value = { ...data, gioiTinh: data.gioiTinh || 'Khác' };
    
    if (data.ngaySinh) {
      if (Array.isArray(data.ngaySinh)) {
        const [y, m, d] = data.ngaySinh;
        userProfile.value.ngaySinh = `${y}-${String(m).padStart(2, '0')}-${String(d).padStart(2, '0')}`;
      } else {
        userProfile.value.ngaySinh = data.ngaySinh.split('T')[0];
      }
    }
  } catch (error) {
    console.error("Lỗi lấy thông tin:", error);
  }
};

const saveProfile = async () => {
  isSaving.value = true;
  try {
    const response = await axios.put(`http://localhost:8080/api/nguoi-dung/${currentUserId}`, userProfile.value);
    if (response.status === 200) {
      alert("🎉 Cập nhật hồ sơ thành công!");
      const storedUser = JSON.parse(localStorage.getItem('user'));
      storedUser.hoVaTen = userProfile.value.hoVaTen;
      localStorage.setItem('user', JSON.stringify(storedUser));
      window.location.reload();
    }
  } catch (error) {
    alert("Cập nhật thất bại.");
  } finally {
    isSaving.value = false;
  }
};

// --- LOGIC AVATAR ---
const triggerAvatarUpload = () => {
  if (avatarInput.value) avatarInput.value.click();
};
const handleAvatarChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    // 1. Hiển thị ảnh tạm thời lên giao diện
    userProfile.value.anhDaiDien = URL.createObjectURL(file);
    alert("Đã chọn ảnh! Tính năng tải ảnh lên DB sẽ được Backend hỗ trợ sau nhé.");
    // GHI CHÚ: Sau này bạn gọi API upload File y hệt như đăng bán sản phẩm ở đây.
  }
};

// ==========================================
// LOGIC TAB 2: SỔ ĐỊA CHỈ (API CẤP TỈNH/HUYỆN/XÃ)
// ==========================================
const showAddressForm = ref(false);

// Thêm mảng này để chứa danh sách địa chỉ lấy từ DB
const userAddresses = ref([]);

// Hàm lấy danh sách địa chỉ của User
const fetchUserAddresses = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/api/dia-chi/nguoi-dung/${currentUserId}`);
    userAddresses.value = response.data;
  } catch (error) {
    console.error("Lỗi lấy danh sách địa chỉ:", error);
  }
};

const addressForm = ref({
  tenNguoiNhan: '', soDienThoai: '', tinhCode: '', huyenCode: '', phuongCode: '', diaChiChiTiet: '', isDefault: false
});

// Các mảng chứa dữ liệu từ API
const listProvinces = ref([]);
const listDistricts = ref([]);
const listWards = ref([]);

// 1. Gọi API lấy danh sách Tỉnh/Thành
const fetchProvinces = async () => {
  try {
    const response = await axios.get('https://provinces.open-api.vn/api/p/');
    listProvinces.value = response.data;
  } catch (error) { console.error("Lỗi lấy Tỉnh:", error); }
};

// 2. Gọi API lấy danh sách Quận/Huyện (Dựa vào Tỉnh đã chọn)
const fetchDistricts = async () => {
  addressForm.value.huyenCode = ''; // Reset Huyện và Xã
  addressForm.value.phuongCode = '';
  listWards.value = []; 
  try {
    const response = await axios.get(`https://provinces.open-api.vn/api/p/${addressForm.value.tinhCode}?depth=2`);
    listDistricts.value = response.data.districts;
  } catch (error) { console.error("Lỗi lấy Huyện:", error); }
};

// 3. Gọi API lấy danh sách Phường/Xã (Dựa vào Huyện đã chọn)
const fetchWards = async () => {
  addressForm.value.phuongCode = ''; // Reset Xã
  try {
    const response = await axios.get(`https://provinces.open-api.vn/api/d/${addressForm.value.huyenCode}?depth=2`);
    listWards.value = response.data.wards;
  } catch (error) { console.error("Lỗi lấy Xã:", error); }
};

// 4. Bấm lưu địa chỉ (Gắn API Spring Boot)
const saveAddress = async () => {
  // Lấy ra chuỗi tên của Tỉnh, Huyện, Xã
  const tenTinh = listProvinces.value.find(p => p.code === addressForm.value.tinhCode)?.name;
  const tenHuyen = listDistricts.value.find(d => d.code === addressForm.value.huyenCode)?.name;
  const tenXa = listWards.value.find(w => w.code === addressForm.value.phuongCode)?.name;
  
  // Tên + SĐT + Chi tiết + Xã + Huyện + Tỉnh
  const tenVaSdt = `${addressForm.value.tenNguoiNhan} - ${addressForm.value.soDienThoai}`;
  const diaChiDayDu = `${tenVaSdt} | ${addressForm.value.diaChiChiTiet}, ${tenXa}, ${tenHuyen}, ${tenTinh}`;
  
  // SỬA LẠI ĐÚNG ĐOẠN NÀY
  const payload = {
    nguoiDungId: currentUserId,
    phuongXaId: Number(addressForm.value.phuongCode), // Ép kiểu sang số nguyên (Integer)
    diaChiChiTiet: diaChiDayDu,
    diaChiMacDinh: addressForm.value.isDefault ? 1 : 0
  };

  try {
    const response = await axios.post('http://localhost:8080/api/dia-chi', payload);
    alert("🎉 " + response.data.message);
    
    // Đóng form thêm địa chỉ
    showAddressForm.value = false;
    
    // Gọi hàm load lại danh sách địa chỉ lên màn hình
    fetchUserAddresses(); 
    
  } catch (error) {
    console.error("Lỗi lưu địa chỉ:", error);
    alert("❌ Có lỗi xảy ra khi lưu địa chỉ!");
  }
};

</script>

<style scoped>
.avatar-circle { width: 50px; height: 50px; border-radius: 50%; }
.menu-list { display: flex; flex-direction: column; gap: 10px; }
.menu-item { padding: 10px 15px; border-radius: 6px; cursor: pointer; color: #555; transition: all 0.2s ease; font-weight: 500; }
.menu-item .icon { margin-right: 8px; }
.menu-item:hover { background-color: #f8f9fa; color: #007bff; }
.menu-item.active { color: #ee4d2d; font-weight: bold; background-color: #fff5f5; border-left: 4px solid #ee4d2d;}
.profile-form .form-control, .form-select { padding: 10px 15px; border-radius: 4px; border: 1px solid #ced4da; }
.profile-form .form-control:focus, .form-select:focus { border-color: #80bdff; box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25); }
.cursor-pointer { cursor: pointer; }
</style>