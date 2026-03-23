<template>
  <div class="container py-5 bg-light min-vh-100">

      <div class="d-flex align-items-center justify-content-between mb-4">
        <nav aria-label="breadcrumb">
          <ol class="breadcrumb mb-0 bg-transparent p-0">
            <li class="breadcrumb-item">
              <router-link to="/" class="text-decoration-none text-muted">
                <i class="bi bi-house-door"></i> Trang chủ
              </router-link>
            </li>
            <li class="breadcrumb-item active text-danger" aria-current="page">Hồ sơ cá nhân</li>
          </ol>
        </nav>

        <button @click="router.push('/')" class="btn btn-sm btn-outline-secondary rounded-pill px-3 shadow-sm">
          <i class="bi bi-arrow-left"></i> Quay lại trang chủ
        </button>
      </div>

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

              <div class="menu-item text-danger fw-bold" @click="router.push('/my-products')">
                <span class="icon">🏪</span> Hồ sơ Shop
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
            
            <div class="card text-white mb-5 rounded-4 border-0 shadow-sm" style="background: linear-gradient(135deg, #2b2d42 0%, #8d99ae 100%);">
              <div class="card-body d-flex justify-content-between align-items-center p-4">
                <div>
                  <h6 class="mb-2 text-white-50 fw-medium text-uppercase tracking-wide" style="font-size: 0.85rem; letter-spacing: 1px;">Số dư Ví OLD2NEW</h6>
                  <h2 class="mb-0 fw-bold display-6">{{ formatCurrency(walletBalance) }}</h2>
                </div>
                <div class="opacity-50" style="font-size: 3.5rem;">
                  💳
                </div>
              </div>
              <div class="card-footer border-0 bg-transparent py-3 px-4 d-flex justify-content-between align-items-center" style="background: rgba(255,255,255,0.1) !important;">
                <small class="text-white-50">Sử dụng số dư này để rút tiền về tài khoản ngân hàng</small>
                <button class="btn btn-sm btn-light fw-bold rounded-pill px-4">Rút tiền</button>
              </div>
            </div>
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
                      <div class="input-group">
                        <input type="tel" v-model="userProfile.soDienThoai" class="form-control" placeholder="Nhập SĐT để xác minh...">
                        <button v-if="!isVerifiedLocal" 
                              type="button" 
                              class="btn btn-danger btn-sm px-3 fw-bold" 
                              @click="openOtpModal">
                              XÁC MINH NGAY
                      </button>
                        <span v-else class="input-group-text bg-white text-success border-1 fw-bold">
                          ✅ ĐÃ XÁC MINH
                        </span>
                      </div>
                    </div>
                  </div>
                    <div v-if="showOtpModal" class="modal fade show d-block" style="background: rgba(0,0,0,0.6); z-index: 1060;">
                    <div class="modal-dialog modal-dialog-centered">
                      <div class="modal-content border-0 shadow">
                        <div class="modal-header">
                          <h5 class="modal-title fw-bold">Xác minh SĐT Hồ sơ</h5>
                          <button type="button" class="btn-close" @click="showOtpModal = false"></button>
                        </div>
                        <div class="modal-body p-4">
                          <p class="small text-muted mb-3">Hệ thống sẽ gửi mã xác minh đến SĐT: <b>{{ userProfile.soDienThoai }}</b></p>
                          
                          <div id="recaptcha-profile" class="d-flex justify-content-center mb-3"></div>
                          
                          <button v-if="!confirmationResult" @click="requestOtpProfile" :disabled="isRequestingOtp" class="btn btn-dark w-100 mb-3">
                            {{ isRequestingOtp ? 'Đang gửi...' : 'GỬI MÃ XÁC MINH' }}
                          </button>

                          <div v-if="confirmationResult">
                            <label class="form-label small fw-bold">Nhập mã OTP 6 số</label>
                            <input type="text" v-model="otpCode" class="form-control text-center fs-4 fw-bold mb-3" placeholder="------" maxlength="6">
                            <button class="btn btn-primary w-100 fw-bold" @click="verifyAndSaveLocal">XÁC NHẬN MÃ</button>
                          </div>
                        </div>
                      </div>
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
                      <button class="btn btn-sm btn-outline-primary" @click="editAddress(addr)">Sửa</button>
                      <button class="btn btn-sm btn-outline-danger" @click="deleteAddress(addr.diaChiId)">Xóa</button>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <form v-if="showAddressForm" @submit.prevent="saveAddress" class="mt-4 p-4 border rounded bg-light">
              <h5 class="fw-bold mb-4 text-primary">
                {{ isEditing ? '✏️ Cập nhật địa chỉ' : '📍 Thêm địa chỉ mới' }}
              </h5>
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
                    <option v-for="tinh in listProvinces" :key="tinh.ProvinceID" :value="tinh.ProvinceID">{{ tinh.ProvinceName }}</option>
                  </select>
                </div>
                <div class="col-md-4">
                  <label class="form-label small fw-bold">Quận / Huyện</label>
                  <select v-model="addressForm.huyenCode" @change="fetchWards" class="form-select" :disabled="!addressForm.tinhCode" required>
                    <option value="" disabled>Chọn Quận / Huyện</option>
                    <option v-for="huyen in listDistricts" :key="huyen.DistrictID" :value="huyen.DistrictID">{{ huyen.DistrictName }}</option>
                  </select>
                </div>
                <div class="col-md-4">
                  <label class="form-label small fw-bold">Phường / Xã</label>
                  <select v-model="addressForm.phuongCode" class="form-select" :disabled="!addressForm.huyenCode" required>
                    <option value="" disabled>Chọn Phường / Xã</option>
                    <option v-for="xa in listWards" :key="xa.WardCode" :value="xa.WardCode">{{ xa.WardName }}</option>
                  </select>
                </div>
              </div>

              <div class="mb-3">
                <label class="form-label small fw-bold">Địa chỉ cụ thể</label>
                <textarea v-model="addressForm.diaChiChiTiet" class="form-control" rows="2" placeholder="Số nhà, tên đường..." required></textarea>
              </div>

              <div class="form-check mb-4">
                <input class="form-check-input" type="checkbox" v-model="addressForm.isDefault" id="defaultAddress">
                <label class="form-check-label text-muted small" for="defaultAddress">Đặt làm địa chỉ mặc định</label>
              </div>

              <div class="d-flex justify-content-end gap-2">
                <button type="button" class="btn btn-outline-secondary" @click="cancelAddressForm">Trở lại</button>
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
import { ref, onMounted , nextTick } from 'vue';
import { getAuth, RecaptchaVerifier, signInWithPhoneNumber } from "firebase/auth";
import { initializeApp } from "firebase/app";
import { useRouter } from 'vue-router';
import axios from 'axios';

// 2. Khai báo Firebase Config (Duy dùng đúng bộ Config của Duy nhé)
const firebaseConfig = {
  apiKey: "AIzaSyCy2BDZO1nKsU43fl8LqdAgix92z_G-26E",
  authDomain: "old2new-e8ef2.firebaseapp.com",
  projectId: "old2new-e8ef2",
  storageBucket: "old2new-e8ef2.firebasestorage.app",
  messagingSenderId: "761431606910",
  appId: "1:761431606910:web:379c565c0e096d16a2fa2f",
  measurementId: "G-VW201BGYC5"
};

// 3. 🔥 QUAN TRỌNG: Khởi tạo biến auth ở đây để các hàm bên dưới nhận diện được
const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
auth.languageCode = 'vi'; // Thiết lập tiếng Việt cho reCAPTCHA


const router = useRouter();
const currentTab = ref('profile'); 
const isSaving = ref(false);
let currentUserId = null;

// Xác minh số điện thoại nếu đã xác minh bên Profile thì không cần xác minh nữa 
const showOtpModal = ref(false); // Điều khiển ẩn/hiện Modal
const isRequestingOtp = ref(false); // Trạng thái đang gửi mã
const confirmationResult = ref(null); // Lưu kết quả xác thực từ Firebase
const otpCode = ref(''); // Lưu mã 6 số người dùng nhập
const isVerifiedLocal = ref(localStorage.getItem('isPhoneVerified') === 'true');

// 🔥 BIẾN VÀ HÀM CHO VÍ TIỀN
const walletBalance = ref(0);

const formatCurrency = (val) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(val || 0);
};

const fetchWalletBalance = async () => {
  try {
    const storedUser = JSON.parse(localStorage.getItem('user'));
    if (!storedUser) return;
    
    // Sử dụng getAuthHeaders() để gửi kèm token xuống Backend
    const response = await axios.get(`http://localhost:8080/api/vi-tien/so-du/${currentUserId}`, getAuthHeaders());
    walletBalance.value = response.data.soDu || 0;
  } catch (error) {
    console.error("Lỗi lấy số dư ví:", error);
    walletBalance.value = 0;
  }
};


// 1. Hàm mở Modal
const openOtpModal = async () => {
  showOtpModal.value = true;
  await nextTick(); // Đợi Modal render xong mới dựng reCAPTCHA
  setupRecaptchaProfile();
};

// 2. Khởi tạo reCAPTCHA cho Profile
const setupRecaptchaProfile = () => {
  if (!window.recaptchaVerifierProfile) {
    window.recaptchaVerifierProfile = new RecaptchaVerifier(auth, 'recaptcha-profile', {
      'size': 'normal'
    });
    window.recaptchaVerifierProfile.render();
  }
};

// 3. Hàm gửi mã OTP (Quan trọng: Phải gắn vào nút Lấy Mã)
const requestOtpProfile = async () => {
  if (!userProfile.value.soDienThoai) return alert("Vui lòng nhập SĐT!");
  let phone = userProfile.value.soDienThoai;
  let phoneNumber = phone.startsWith('0') ? '+84' + phone.substring(1) : phone;

  isRequestingOtp.value = true;
  try {
    // 🔥 Sử dụng recaptcha-profile làm chìa khóa
    const appVerifier = window.recaptchaVerifierProfile;
    const result = await signInWithPhoneNumber(auth, phoneNumber, appVerifier);
    confirmationResult.value = result;
    alert("🚀 Mã OTP đã được gửi đến máy bạn!");
  } catch (error) {
    console.error(error);
    alert("Lỗi gửi mã OTP. Hãy tải lại trang hoặc kiểm tra SĐT!");
    if (window.recaptchaVerifierProfile) window.recaptchaVerifierProfile.render();
  } finally {
    isRequestingOtp.value = false;
  }
};

// 4. Hàm xác nhận mã và Lưu dấu "Đã xác minh"
const verifyAndSaveLocal = async () => {
  if (!otpCode.value) return alert("Vui lòng nhập mã OTP!");
  try {
    const result = await confirmationResult.value.confirm(otpCode.value);
    if (result.user) {
      // ✅ BƯỚC QUAN TRỌNG: Lưu dấu xác minh vào LocalStorage
      localStorage.setItem('isPhoneVerified', 'true');
      localStorage.setItem('verifiedPhone', userProfile.value.soDienThoai);
      
      // Cập nhật trạng thái hiển thị ngay lập tức
      isVerifiedLocal.value = true;
      showOtpModal.value = false;
      
      alert("🎉 Xác minh thành công! Từ giờ bạn có thể đăng bán mà không cần OTP nữa.");
    }
  } catch (error) {
    console.error("Lỗi xác minh:", error);
    alert("❌ Mã xác minh không chính xác!");
  }
};



// ==========================================
// BẢO MẬT: HÀM TỰ ĐỘNG LẤY TOKEN GẮN VÀO API
// ==========================================
const getAuthHeaders = () => {
  let token = null;

  // 1. Lục tìm trong cục 'user' (như cũ)
  const userStr = localStorage.getItem('user');
  if (userStr) {
    const userData = JSON.parse(userStr);
    token = userData.token || userData.accessToken || userData.jwt; 
  }

  // 2. Nếu trong 'user' không có, lục tìm xem có ai giấu nó ở ngoài không!
  if (!token) {
    token = localStorage.getItem('token') || localStorage.getItem('accessToken') || localStorage.getItem('jwt');
  }

  // 3. Nếu tìm thấy thì tự tin xuất trình thẻ
  if (token) {
    return { headers: { Authorization: `Bearer ${token}` } };
  }

  // 4. Nếu tới đây mà vẫn không có thì... bó tay!
  console.warn("⚠️ BÁO ĐỘNG ĐỎ: ĐỒNG ĐỘI CỦA BẠN ĐÃ QUÊN LƯU TOKEN Ở TRANG LOGIN!");
  return {}; 
};


// ==========================================
// LOGIC TAB 1: HỒ SƠ NGƯỜI DÙNG
// ==========================================
const userProfile = ref({ hoVaTen: '', email: '', soDienThoai: '', gioiTinh: '', ngaySinh: '', anhDaiDien: '' });
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
  fetchProvinces();    
  fetchUserAddresses();
  fetchWalletBalance(); // 🔥 Gọi hàm lấy Ví khi trang vừa load xong
});

// 1. LẤY THÔNG TIN
const fetchUserData = async () => {
  try {
    // 🔥 Gắn Token trực tiếp vào đây để không bị lỗi 403
    const response = await axios.get(`http://localhost:8080/api/nguoi-dung/${currentUserId}`, getAuthHeaders());
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

// 2. LƯU THÔNG TIN (ĐÃ PHỤC HỒI)
const saveProfile = async () => {
  isSaving.value = true;
  try {
    // 🔥 Gắn Token trực tiếp vào đây
    const response = await axios.put(`http://localhost:8080/api/nguoi-dung/${currentUserId}`, userProfile.value, getAuthHeaders());
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

// 3. ĐỔI AVATAR (ĐÃ PHỤC HỒI)
const triggerAvatarUpload = () => { if (avatarInput.value) avatarInput.value.click(); };

const handleAvatarChange = async (event) => {
  const file = event.target.files[0];
  if (!file) return;

  userProfile.value.anhDaiDien = URL.createObjectURL(file);
  const formData = new FormData();
  formData.append('file', file);

  try {
    // Lấy token và ghép chung với Content-Type của file
    const authHeaders = getAuthHeaders();
    const config = {
      headers: {
        ...authHeaders.headers,
        'Content-Type': 'multipart/form-data'
      }
    };

    const response = await axios.post(`http://localhost:8080/api/nguoi-dung/${currentUserId}/avatar`, formData, config);
    
    const newAvatarUrl = response.data.anhDaiDien;
    userProfile.value.anhDaiDien = newAvatarUrl;

    const storedUser = JSON.parse(localStorage.getItem('user'));
    storedUser.anhDaiDien = newAvatarUrl;
    localStorage.setItem('user', JSON.stringify(storedUser));

    alert("🎉 " + response.data.message);
    window.location.reload();
  } catch (error) {
    console.error("Lỗi upload ảnh:", error);
    alert("❌ Lỗi khi tải ảnh lên server!");
  }
};


// ==========================================
// LOGIC TAB 2: SỔ ĐỊA CHỈ (THÊM, SỬA, XÓA VỚI GHN)
// ==========================================
const showAddressForm = ref(false);
const userAddresses = ref([]);

// 1. Thêm 2 biến này để theo dõi trạng thái Sửa
const isEditing = ref(false);
const editingId = ref(null);

const addressForm = ref({ tenNguoiNhan: '', soDienThoai: '', tinhCode: '', huyenCode: '', phuongCode: '', diaChiChiTiet: '', isDefault: false });

const listProvinces = ref([]);
const listDistricts = ref([]);
const listWards = ref([]);

const ghnToken = 'd7acb48b-030e-11f1-a3d6-dac90fb956b5'; // Token GHN của bạn
const ghnConfig = { headers: { Token: ghnToken } };

// --- CÁC HÀM GET DỮ LIỆU GIỮ NGUYÊN ---
const fetchUserAddresses = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/api/dia-chi/nguoi-dung/${currentUserId}`, getAuthHeaders());
    userAddresses.value = response.data;
  } catch (error) { console.error("Lỗi lấy danh sách địa chỉ:", error); }
};

const fetchProvinces = async () => { /* ... Giữ nguyên ... */ 
  try {
    const response = await axios.get('https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province', ghnConfig);
    listProvinces.value = response.data.data.filter(p => !p.ProvinceName.toLowerCase().includes('test') && !p.ProvinceName.toLowerCase().includes('02') && !p.ProvinceName.toLowerCase().includes('001') && !p.ProvinceName.toLowerCase().includes('alert'));
  } catch (error) { console.error("Lỗi lấy Tỉnh GHN:", error); }
};

const fetchDistricts = async () => { /* ... Giữ nguyên ... */ 
  addressForm.value.huyenCode = ''; addressForm.value.phuongCode = ''; listWards.value = []; 
  try {
    const response = await axios.get(`https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district?province_id=${addressForm.value.tinhCode}`, ghnConfig);
    listDistricts.value = response.data.data.filter(d => !d.DistrictName.toLowerCase().includes('test'));
  } catch (error) { console.error("Lỗi lấy Huyện GHN:", error); }
};

const fetchWards = async () => { /* ... Giữ nguyên ... */ 
  addressForm.value.phuongCode = ''; 
  try {
    const response = await axios.get(`https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=${addressForm.value.huyenCode}`, ghnConfig);
    listWards.value = response.data.data;
  } catch (error) { console.error("Lỗi lấy Xã GHN:", error); }
};

// ==========================================
// 💥 TÍNH NĂNG MỚI: XÓA ĐỊA CHỈ
// ==========================================
const deleteAddress = async (id) => {
  if (!confirm("⚠️ Bạn có chắc chắn muốn xóa địa chỉ này không?")) return;
  
  try {
    // Gọi API Xóa của Spring Boot
    await axios.delete(`http://localhost:8080/api/dia-chi/${id}`, getAuthHeaders());
    alert("✅ Đã xóa địa chỉ thành công!");
    fetchUserAddresses(); // Tải lại danh sách
  } catch (error) {
    console.error("Lỗi khi xóa:", error);
    alert("❌ Không thể xóa địa chỉ lúc này!");
  }
};

// ==========================================
// 💥 TÍNH NĂNG MỚI: BẤM NÚT SỬA
// ==========================================
const editAddress = (addr) => {
  isEditing.value = true;
  editingId.value = addr.diaChiId;
  
  // Mổ xẻ chuỗi "Tên - SĐT | Địa chỉ" để lấy lại data điền vào ô
  const parts = addr.diaChiChiTiet.split(' | ');
  const namePhone = parts[0].split(' - ');
  const details = parts[1] ? parts[1].split(', ') : [];
  
  // Đổ dữ liệu cũ vào Form
  addressForm.value.tenNguoiNhan = namePhone[0] ? namePhone[0].trim() : '';
  addressForm.value.soDienThoai = namePhone[1] ? namePhone[1].trim() : '';
  addressForm.value.diaChiChiTiet = details[0] || ''; // Chỉ lấy phần số nhà/đường
  addressForm.value.isDefault = addr.diaChiMacDinh === 1;
  
  // Xóa rỗng các ô Dropdown bắt khách chọn lại
  addressForm.value.tinhCode = '';
  addressForm.value.huyenCode = '';
  addressForm.value.phuongCode = '';
  listDistricts.value = [];
  listWards.value = [];
  
  showAddressForm.value = true;
};


// ==========================================
// 💥 HÀM LƯU / CẬP NHẬT ĐỊA CHỈ (ĐÃ CÓ HUYỆN)
// ==========================================
const saveAddress = async () => {
  const tenTinh = listProvinces.value.find(p => p.ProvinceID === addressForm.value.tinhCode)?.ProvinceName;
  const tenHuyen = listDistricts.value.find(d => d.DistrictID === addressForm.value.huyenCode)?.DistrictName;
  const tenXa = listWards.value.find(w => w.WardCode === addressForm.value.phuongCode)?.WardName;
  
  const tenVaSdt = `${addressForm.value.tenNguoiNhan} - ${addressForm.value.soDienThoai}`;
  const diaChiDayDu = `${tenVaSdt} | ${addressForm.value.diaChiChiTiet}, ${tenXa}, ${tenHuyen}, ${tenTinh}`;
  
  const payload = {
    nguoiDungId: currentUserId,
    
    // 🔥 ĐÃ BỔ SUNG DÒNG NÀY:
    huyenCode: parseInt(addressForm.value.huyenCode), 
    
    phuongXaId: addressForm.value.phuongCode, 
    diaChiChiTiet: diaChiDayDu,
    diaChiMacDinh: addressForm.value.isDefault ? 1 : 0
  };

  try {
    if (isEditing.value) {
      await axios.put(`http://localhost:8080/api/dia-chi/${editingId.value}`, payload, getAuthHeaders());
      alert("🎉 Cập nhật địa chỉ thành công!");
    } else {
      await axios.post('http://localhost:8080/api/dia-chi', payload, getAuthHeaders());
      alert("🎉 Thêm địa chỉ mới thành công!");
    }
    
    cancelAddressForm();
    fetchUserAddresses(); 
  } catch (error) {
    console.error("Lỗi lưu địa chỉ:", error);
    alert("❌ Có lỗi xảy ra khi lưu địa chỉ!");
  }
};

// Hàm hủy form (Gắn hàm này vào nút "Trở lại" trong HTML thay vì showAddressForm = false)
const cancelAddressForm = () => {
  showAddressForm.value = false;
  isEditing.value = false;
  editingId.value = null;
  addressForm.value = { tenNguoiNhan: '', soDienThoai: '', tinhCode: '', huyenCode: '', phuongCode: '', diaChiChiTiet: '', isDefault: false };
};
</script>

<style scoped>
/* CSS giữ nguyên, không thay đổi */
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