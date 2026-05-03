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
          <li class="breadcrumb-item active text-danger" aria-current="page">
            Hồ sơ cá nhân
          </li>
        </ol>
      </nav>

      <button @click="router.push('/')" class="btn btn-sm btn-outline-secondary rounded-pill px-3 shadow-sm">
        <i class="bi bi-arrow-left"></i> Quay lại trang chủ
      </button>
    </div>

    <div class="row">
      <div class="col-md-3 mb-4">
        <UserSidebar :activeTab="currentTab" @changeTab="handleTabChange" />
      </div>

      <div class="col-md-9">
        <div class="bg-white rounded shadow-sm p-4 p-md-5">
          <div v-if="currentTab === 'profile'">
            <div class="card text-white mb-5 rounded-4 border-0 shadow-sm" style="
                background: linear-gradient(135deg, #2b2d42 0%, #8d99ae 100%);
              ">
              <div class="card-body d-flex justify-content-between align-items-center p-4">
                <div>
                  <h6 class="mb-2 text-white-50 fw-medium text-uppercase tracking-wide"
                    style="font-size: 0.85rem; letter-spacing: 1px">
                    Số dư Ví OLD2NEW
                  </h6>
                  <h2 class="mb-0 fw-bold display-6">
                    {{ formatCurrency(walletBalance) }}
                  </h2>
                </div>
                <div class="opacity-50" style="font-size: 3.5rem">💳</div>
              </div>
              <div
                class="card-footer border-0 bg-transparent py-3 px-4 d-flex justify-content-between align-items-center"
                style="background: rgba(255, 255, 255, 0.1) !important">
                <small class="text-white-50">Sử dụng số dư này để rút tiền về tài khoản ngân hàng</small>
                <button @click="$router.push('/rut-tien')" class="btn btn-sm btn-light fw-bold rounded-pill px-4">
                  Rút tiền
                </button>
              </div>
            </div>

            <h2 class="h4 fw-bold mb-1 text-dark">Hồ sơ của tôi</h2>
            <p class="text-muted small mb-4 pb-3 border-bottom">
              Quản lý thông tin hồ sơ để bảo mật tài khoản
            </p>

            <form @submit.prevent="saveProfile" class="profile-form">
              <!-- 🔥 ẢNH ĐẠI DIỆN GIỮ NGUYÊN -->
              <div class="row mb-4 align-items-center">
                <div class="col-md-3 text-md-end text-muted fw-medium small">
                  Ảnh đại diện
                </div>
                <div class="col-md-8">
                  <div class="position-relative d-inline-block">
                    <img v-if="userProfile.anhDaiDien" :src="userProfile.anhDaiDien"
                      class="rounded-circle border shadow-sm object-fit-cover" style="width: 100px; height: 100px" />
                    <div v-else
                      class="rounded-circle bg-dark text-white fw-bold fs-1 d-flex align-items-center justify-content-center shadow-sm"
                      style="width: 100px; height: 100px">
                      {{ userProfile.hoVaTen ? userProfile.hoVaTen.charAt(0).toUpperCase() : "U" }}
                    </div>

                    <label class="position-absolute bottom-0 end-0 bg-white rounded-circle shadow-sm border p-1"
                      style="cursor: pointer">
                      <i class="bi bi-camera-fill text-primary" style="font-size: 0.9rem"></i>
                      <input type="file" class="d-none" @change="handleAvatarChange" accept="image/*" />
                    </label>
                  </div>
                </div>
              </div>

              <div class="row mb-4 align-items-center">
                <div class="col-md-3 text-md-end text-muted fw-medium small">
                  Email đăng nhập
                </div>
                <div class="col-md-8">
                  <input type="email" v-model="userProfile.email" class="form-control bg-light text-muted" disabled />
                </div>
              </div>

              <div class="row mb-4 align-items-center">
                <div class="col-md-3 text-md-end text-muted fw-medium small">
                  Tên hiển thị
                </div>
                <div class="col-md-8">
                  <input type="text" v-model="userProfile.hoVaTen" class="form-control" required />
                </div>
              </div>

              <!-- 🔥 SỐ ĐIỆN THOẠI (Xác minh nội bộ) -->
              <div class="row mb-4 align-items-center">
                <div class="col-md-3 text-md-end text-muted fw-medium small">
                  Số điện thoại
                </div>
                <div class="col-md-8">
                  <div class="input-group">
                    <input type="tel" v-model="userProfile.soDienThoai" class="form-control"
                      placeholder="Nhập SĐT để xác minh..." />
                    <button v-if="!isVerifiedLocal" type="button" class="btn btn-danger btn-sm px-3 fw-bold"
                      @click="openOtpModal">
                      XÁC MINH NGAY
                    </button>
                    <span v-else class="input-group-text bg-white text-success border-1 fw-bold">
                      ✅ ĐÃ XÁC MINH
                    </span>
                  </div>
                </div>
              </div>

              <div class="row mb-4 align-items-center">
                <div class="col-md-3 text-md-end text-muted fw-medium small">
                  Giới tính
                </div>
                <div class="col-md-8 d-flex gap-4">
                  <label class="d-flex align-items-center gap-2 cursor-pointer"><input type="radio"
                      v-model="userProfile.gioiTinh" value="Nam" />
                    Nam</label>
                  <label class="d-flex align-items-center gap-2 cursor-pointer"><input type="radio"
                      v-model="userProfile.gioiTinh" value="Nữ" />
                    Nữ</label>
                  <label class="d-flex align-items-center gap-2 cursor-pointer"><input type="radio"
                      v-model="userProfile.gioiTinh" value="Khác" />
                    Khác</label>
                </div>
              </div>

              <div class="row mb-4 align-items-center">
                <div class="col-md-3 text-md-end text-muted fw-medium small">
                  Ngày sinh
                </div>
                <div class="col-md-8">
                  <input type="date" v-model="userProfile.ngaySinh" class="form-control" />
                </div>
              </div>

              <!-- 🔥 ĐỊA CHỈ GIỮ NGUYÊN -->
              <div class="row mb-4 align-items-start">
                <div class="col-md-3 text-md-end text-muted fw-medium small mt-2">
                  Địa chỉ giao nhận
                </div>
                <div class="col-md-8">
                  <div class="p-3 bg-light border rounded-3 border-secondary border-opacity-25">
                    <div class="d-flex justify-content-between align-items-center mb-2">
                      <label class="fw-bold mb-0 text-dark small">
                        <i class="bi bi-geo-alt-fill text-danger me-1"></i> Địa
                        chỉ mặc định
                      </label>
                      <button type="button" class="btn btn-sm btn-outline-primary fw-bold" @click="openAddressManager">
                        Cập nhật chi tiết
                      </button>
                    </div>

                    <div v-if="defaultAddress" class="mt-2 text-dark">
                      <span class="badge bg-danger mb-1">Mặc định</span>
                      <div class="fw-bold small">
                        {{ defaultAddress.diaChiChiTiet.split(" | ")[0] }}
                      </div>
                      <p class="mb-0 fw-medium small text-secondary">
                        {{ defaultAddress.diaChiChiTiet.split(" | ")[1] || defaultAddress.diaChiChiTiet }}
                      </p>
                    </div>
                    <div v-else class="mt-2 text-muted fst-italic small">
                      Bạn chưa thiết lập địa chỉ nào.
                    </div>
                  </div>
                </div>
              </div>

              <div class="row mt-5">
                <div class="col-md-3"></div>
                <div class="col-md-8 d-flex gap-3">
                  <button type="submit" class="btn btn-dark px-4 py-2 fw-bold" :disabled="isSaving">
                    {{ isSaving ? "Đang lưu..." : "LƯU THAY ĐỔI" }}
                  </button>
                  <button type="button" class="btn btn-outline-secondary px-4 py-2 fw-bold" @click="router.push('/')">
                    🏠 QUAY LẠI TRANG CHỦ
                  </button>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>

    <!-- 🔥 MODAL XÁC MINH OTP NỘI BỘ (Thay thế Firebase) -->
    <div v-if="showOtpModal" class="modal fade show d-block" style="background: rgba(0, 0, 0, 0.6); z-index: 1060">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg rounded-4">
          <div class="modal-header border-bottom-0 pb-0">
            <h5 class="modal-title fw-bold">Xác minh Số điện thoại</h5>
            <button type="button" class="btn-close" @click="showOtpModal = false"></button>
          </div>
          <div class="modal-body p-4 text-center">
            <p class="small text-muted mb-4">Vui lòng nhập mã OTP để xác nhận (Mặc định: <b>123456</b>)</p>
            
            <div class="mb-3">
              <input type="text" v-model="otpCodeInput" class="form-control text-center fs-2 fw-bold border-danger py-2"
                placeholder="------" maxlength="6" autofocus />
            </div>
          </div>
          <div class="modal-footer border-top-0 pt-0">
            <button class="btn btn-danger w-100 py-2 fw-bold shadow" @click="verifyAndSaveLocal">
              XÁC NHẬN MÃ
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 🔥 CÁC MODAL ĐỊA CHỈ (Giữ nguyên) -->
    <div v-if="showAddressManagerModal" class="modal fade show d-block"
      style="background: rgba(0, 0, 0, 0.5); z-index: 1050">
      <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content border-0 rounded-4 shadow">
          <div class="modal-header bg-light border-bottom-0">
            <h5 class="modal-title fw-bold">Sổ địa chỉ của bạn</h5>
            <button type="button" class="btn-close" @click="showAddressManagerModal = false"></button>
          </div>
          <div class="modal-body p-4">
            <button class="btn btn-primary fw-bold mb-4" @click="openAddNewAddress">
              <i class="bi bi-plus-lg me-1"></i> Thêm địa chỉ mới
            </button>

            <div v-if="userAddresses.length === 0" class="text-center text-muted py-3">
              Bạn chưa có địa chỉ nào.
            </div>

            <div class="list-group">
              <div v-for="addr in userAddresses" :key="addr.diaChiId"
                class="list-group-item list-group-item-action d-flex justify-content-between align-items-center p-3">
                <div class="pe-3">
                  <div class="fw-bold text-dark mb-1">
                    {{ addr.diaChiChiTiet.split(" | ")[0] }}
                    <span v-if="addr.diaChiMacDinh === 1" class="badge bg-danger ms-2">Mặc định</span>
                  </div>
                  <p class="mb-0 text-secondary small">
                    {{ addr.diaChiChiTiet.split(" | ")[1] || addr.diaChiChiTiet }}
                  </p>
                </div>
                <div class="d-flex flex-column gap-2" style="min-width: 140px; text-align: right">
                  <button v-if="addr.diaChiMacDinh !== 1" class="btn btn-sm btn-outline-success fw-bold"
                    @click="setDefault(addr.diaChiId)">Đặt mặc định</button>
                  <div class="d-flex gap-2 justify-content-end">
                    <button class="btn btn-sm btn-outline-primary flex-grow-1" @click="editAddress(addr)">Sửa</button>
                    <button class="btn btn-sm btn-outline-danger flex-grow-1" @click="deleteAddress(addr.diaChiId)">Xóa</button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="showAddressForm" class="modal fade show d-block" style="background: rgba(0, 0, 0, 0.6); z-index: 1060">
      <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content border-0 rounded-4 shadow-lg">
          <form @submit.prevent="saveAddress">
            <div class="modal-header bg-primary text-white border-bottom-0">
              <h5 class="modal-title fw-bold">
                {{ isEditing ? "✏️ Cập nhật địa chỉ" : "📍 Thêm địa chỉ mới" }}
              </h5>
              <button type="button" class="btn-close btn-close-white" @click="cancelAddressForm"></button>
            </div>
            <div class="modal-body p-4 bg-light">
              <div class="row mb-3">
                <div class="col-md-6">
                  <label class="form-label small fw-bold">Tên người nhận *</label>
                  <input type="text" v-model="addressForm.tenNguoiNhan" class="form-control border-secondary" required />
                </div>
                <div class="col-md-6">
                  <label class="form-label small fw-bold">Số điện thoại *</label>
                  <input type="text" v-model="addressForm.soDienThoai" class="form-control border-secondary" required />
                </div>
              </div>
              <div class="row mb-3">
                <div class="col-md-4">
                  <label class="form-label small fw-bold">Tỉnh / Thành phố *</label>
                  <select v-model="addressForm.tinhCode" @change="fetchDistricts" class="form-select border-secondary" required>
                    <option v-for="tinh in listProvinces" :key="tinh.ProvinceID" :value="tinh.ProvinceID">{{ tinh.ProvinceName }}</option>
                  </select>
                </div>
                <div class="col-md-4">
                  <label class="form-label small fw-bold">Quận / Huyện *</label>
                  <select v-model="addressForm.huyenCode" @change="fetchWards" class="form-select border-secondary" :disabled="!addressForm.tinhCode" required>
                    <option v-for="huyen in listDistricts" :key="huyen.DistrictID" :value="huyen.DistrictID">{{ huyen.DistrictName }}</option>
                  </select>
                </div>
                <div class="col-md-4">
                  <label class="form-label small fw-bold">Phường / Xã *</label>
                  <select v-model="addressForm.phuongCode" class="form-select border-secondary" :disabled="!addressForm.huyenCode" required>
                    <option v-for="xa in listWards" :key="xa.WardCode" :value="xa.WardCode">{{ xa.WardName }}</option>
                  </select>
                </div>
              </div>
              <div class="mb-3">
                <label class="form-label small fw-bold">Số nhà, tên đường cụ thể *</label>
                <textarea v-model="addressForm.diaChiChiTiet" class="form-control border-secondary" rows="2" required></textarea>
              </div>
              <div class="form-check mb-2 mt-3">
                <input class="form-check-input" type="checkbox" v-model="addressForm.isDefault" id="defaultAddress" />
                <label class="form-check-label text-dark fw-medium small" for="defaultAddress">Đặt làm địa chỉ mặc định</label>
              </div>
            </div>
            <div class="modal-footer border-top bg-white">
              <button type="button" class="btn btn-outline-secondary px-4" @click="cancelAddressForm">Hủy</button>
              <button type="submit" class="btn btn-primary px-4 fw-bold shadow-sm">Lưu Địa Chỉ</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useRouter, useRoute } from "vue-router";
import axios from "axios";
import UserSidebar from "@/layouts/UserSidebar.vue";

// 🔥 ĐÃ GỠ BỎ FIREBASE IMPORTS

const router = useRouter();
const route = useRoute();
const currentTab = ref("profile");
const isSaving = ref(false);
let currentUserId = null;

// 🔥 LOGIC XÁC MINH NỘI BỘ
const showOtpModal = ref(false);
const otpCodeInput = ref("");
const isVerifiedLocal = ref(localStorage.getItem("isPhoneVerified") === "true");

const openOtpModal = () => {
  if (!userProfile.value.soDienThoai) return alert("Vui lòng nhập SĐT trước!");
  otpCodeInput.value = "";
  showOtpModal.value = true;
};

const verifyAndSaveLocal = () => {
  // Kiểm tra mã OTP cố định
  if (otpCodeInput.value === "123456") {
    localStorage.setItem("isPhoneVerified", "true");
    localStorage.setItem("verifiedPhone", userProfile.value.soDienThoai);
    isVerifiedLocal.value = true;
    showOtpModal.value = false;
    alert("🎉 Xác minh số điện thoại thành công!");
  } else {
    alert("❌ Mã xác minh không chính xác. Vui lòng thử lại!");
  }
};

const walletBalance = ref(0);

const formatCurrency = (val) => {
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  }).format(val || 0);
};

const getAuthHeaders = () => {
  const userStr = localStorage.getItem("user");
  const token = userStr ? (JSON.parse(userStr).token || JSON.parse(userStr).accessToken) : localStorage.getItem("token");
  return token ? { headers: { Authorization: `Bearer ${token}` } } : {};
};

const fetchWalletBalance = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/api/vi-tien/so-du/${currentUserId}`, getAuthHeaders());
    walletBalance.value = response.data.soDu || 0;
  } catch (error) {
    console.error("Lỗi lấy số dư ví:", error);
  }
};

const userProfile = ref({
  hoVaTen: "",
  email: "",
  soDienThoai: "",
  gioiTinh: "Khác",
  ngaySinh: "",
  anhDaiDien: "",
});

// ĐỊA CHỈ
const showAddressManagerModal = ref(false);
const showAddressForm = ref(false);
const userAddresses = ref([]);
const isEditing = ref(false);
const editingId = ref(null);

const addressForm = ref({
  tenNguoiNhan: "",
  soDienThoai: "",
  tinhCode: "",
  huyenCode: "",
  phuongCode: "",
  diaChiChiTiet: "",
  isDefault: false,
});

const listProvinces = ref([]);
const listDistricts = ref([]);
const listWards = ref([]);

const ghnToken = "d7acb48b-030e-11f1-a3d6-dac90fb956b5";
const ghnConfig = { headers: { Token: ghnToken } };

const defaultAddress = computed(() => {
  return userAddresses.value.find((addr) => addr.diaChiMacDinh === 1) || null;
});

const openAddressManager = () => { showAddressManagerModal.value = true; };

const openAddNewAddress = () => {
  isEditing.value = false;
  editingId.value = null;
  addressForm.value = { tenNguoiNhan: "", soDienThoai: "", tinhCode: "", huyenCode: "", phuongCode: "", diaChiChiTiet: "", isDefault: false };
  listDistricts.value = [];
  listWards.value = [];
  showAddressForm.value = true;
};

onMounted(() => {
  const storedUser = localStorage.getItem("user");
  if (!storedUser) {
    router.push("/login");
    return;
  }
  const userData = JSON.parse(storedUser);
  currentUserId = userData.id || userData.nguoiDungId;

  fetchUserData();
  fetchProvinces();
  fetchUserAddresses();
  fetchWalletBalance();

  if (route.query.tab === "address") openAddressManager();
});

const handleTabChange = (tab) => {
  if (tab === "address") openAddressManager();
  else currentTab.value = tab;
};

const fetchUserData = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/api/nguoi-dung/${currentUserId}`, getAuthHeaders());
    const data = response.data;
    userProfile.value = { ...data, gioiTinh: data.gioiTinh || "Khác" };
    if (data.ngaySinh) {
      userProfile.value.ngaySinh = Array.isArray(data.ngaySinh) 
        ? `${data.ngaySinh[0]}-${String(data.ngaySinh[1]).padStart(2, "0")}-${String(data.ngaySinh[2]).padStart(2, "0")}`
        : data.ngaySinh.split("T")[0];
    }
  } catch (error) { console.error("Lỗi lấy thông tin:", error); }
};

const saveProfile = async () => {
  isSaving.value = true;
  try {
    const response = await axios.put(`http://localhost:8080/api/nguoi-dung/${currentUserId}`, userProfile.value, getAuthHeaders());
    if (response.status === 200) {
      alert("🎉 Cập nhật hồ sơ thành công!");
      const storedUser = JSON.parse(localStorage.getItem("user"));
      storedUser.hoVaTen = userProfile.value.hoVaTen;
      localStorage.setItem("user", JSON.stringify(storedUser));
    }
  } catch (error) { alert("Cập nhật thất bại."); } 
  finally { isSaving.value = false; }
};

const handleAvatarChange = async (event) => {
  const file = event.target.files[0];
  if (!file) return;
  userProfile.value.anhDaiDien = URL.createObjectURL(file);
  const formData = new FormData();
  formData.append("file", file);
  try {
    const response = await axios.post(`http://localhost:8080/api/nguoi-dung/${currentUserId}/avatar`, formData, {
      headers: { ...getAuthHeaders().headers, "Content-Type": "multipart/form-data" }
    });
    const newAvatarUrl = response.data.anhDaiDien;
    userProfile.value.anhDaiDien = newAvatarUrl;
    const storedUser = JSON.parse(localStorage.getItem("user"));
    storedUser.anhDaiDien = newAvatarUrl;
    localStorage.setItem("user", JSON.stringify(storedUser));
    alert("🎉 " + response.data.message);
    window.location.reload();
  } catch (error) { alert("❌ Lỗi khi tải ảnh lên server!"); }
};

const fetchUserAddresses = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/api/dia-chi/nguoi-dung/${currentUserId}`, getAuthHeaders());
    userAddresses.value = response.data;
  } catch (error) { console.error("Lỗi lấy địa chỉ:", error); }
};

const fetchProvinces = async () => {
  try {
    const res = await axios.get("https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province", ghnConfig);
    listProvinces.value = res.data.data.filter(p => !p.ProvinceName.toLowerCase().includes("test"));
  } catch (e) { console.error(e); }
};

const fetchDistricts = async () => {
  addressForm.value.huyenCode = "";
  addressForm.value.phuongCode = "";
  try {
    const res = await axios.get(`https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district?province_id=${addressForm.value.tinhCode}`, ghnConfig);
    listDistricts.value = res.data.data;
  } catch (e) { console.error(e); }
};

const fetchWards = async () => {
  addressForm.value.phuongCode = "";
  try {
    const res = await axios.get(`https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=${addressForm.value.huyenCode}`, ghnConfig);
    listWards.value = res.data.data;
  } catch (e) { console.error(e); }
};

const setDefault = async (id) => {
  try {
    await axios.put(`http://localhost:8080/api/dia-chi/${currentUserId}/default/${id}`, {}, getAuthHeaders());
    alert("✅ Đã đổi mặc định!");
    fetchUserAddresses();
  } catch (e) { alert("❌ Lỗi!"); }
};

const deleteAddress = async (id) => {
  if (!confirm("Xóa địa chỉ này?")) return;
  try {
    await axios.delete(`http://localhost:8080/api/dia-chi/${id}`, getAuthHeaders());
    fetchUserAddresses();
  } catch (e) { alert("❌ Lỗi!"); }
};

const editAddress = async (addr) => {
  isEditing.value = true;
  editingId.value = addr.diaChiId;
  const parts = addr.diaChiChiTiet.split(" | ");
  const namePhone = parts[0].split(" - ");
  addressForm.value.tenNguoiNhan = namePhone[0]?.trim();
  addressForm.value.soDienThoai = namePhone[1]?.trim();
  addressForm.value.diaChiChiTiet = parts[1]?.split(",")[0]?.trim();
  addressForm.value.isDefault = addr.diaChiMacDinh === 1;
  addressForm.value.tinhCode = ""; // Cần fetch lại theo logic GHN nếu muốn chuẩn
  showAddressForm.value = true;
};

const saveAddress = async () => {
  const tenTinh = listProvinces.value.find(p => p.ProvinceID === addressForm.value.tinhCode)?.ProvinceName;
  const tenHuyen = listDistricts.value.find(d => d.DistrictID === addressForm.value.huyenCode)?.DistrictName;
  const tenXa = listWards.value.find(w => w.WardCode === addressForm.value.phuongCode)?.WardName;
  const payload = {
    nguoiDungId: currentUserId,
    huyenCode: parseInt(addressForm.value.huyenCode),
    phuongXaId: addressForm.value.phuongCode,
    diaChiChiTiet: `${addressForm.value.tenNguoiNhan} - ${addressForm.value.soDienThoai} | ${addressForm.value.diaChiChiTiet}, ${tenXa}, ${tenHuyen}, ${tenTinh}`,
    diaChiMacDinh: addressForm.value.isDefault ? 1 : 0,
  };
  try {
    if (isEditing.value) await axios.put(`http://localhost:8080/api/dia-chi/${editingId.value}`, payload, getAuthHeaders());
    else await axios.post("http://localhost:8080/api/dia-chi", payload, getAuthHeaders());
    cancelAddressForm();
    fetchUserAddresses();
  } catch (e) { alert("❌ Lỗi lưu địa chỉ!"); }
};

const cancelAddressForm = () => {
  showAddressForm.value = false;
  isEditing.value = false;
};
</script>

<style scoped>
.profile-form .form-control,
.form-select {
  padding: 10px 15px;
  border-radius: 4px;
  border: 1px solid #ced4da;
}
.profile-form .form-control:focus,
.form-select:focus {
  border-color: #80bdff;
  box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
}
.cursor-pointer {
  cursor: pointer;
}
</style>