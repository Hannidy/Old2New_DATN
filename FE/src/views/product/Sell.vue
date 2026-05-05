<template>
  <div class="bg-light min-vh-100 pb-5 font-sans text-dark position-relative">
    <div class="header" style="padding: 55px;">
      <AppHeader />
    </div>
    <div style="padding-top: 20px;"></div>

    <main class="container bg-white shadow-sm p-4 p-md-5 rounded mt-4">
      <h2 class="h3 fw-bold mb-4 text-dark">Đăng bán sản phẩm</h2>

      <form @submit.prevent="handlePublishAction">
        <!-- 1. PHẦN TẢI ẢNH -->
        <div class="mb-4">
          <input type="file" ref="fileInput" multiple accept="image/*" class="d-none" @change="handleFileChange" />
          <div class="mb-3 p-5 text-center bg-light rounded text-muted" 
               style="border: 2px dashed #ced4da; cursor: pointer;" 
               @click="triggerFileInput">
            <h3 class="fs-4 fw-medium text-secondary mb-2">Tải hình ảnh/video</h3>
            <p class="small mb-0">Nhấn để chọn file từ thiết bị</p>
          </div>
          
          <div v-if="previewImages.length > 0" class="d-flex flex-wrap gap-3 p-3 border rounded bg-white mt-2">
            <div v-for="(img, index) in previewImages" :key="index" class="position-relative">
              <img :src="img" class="img-thumbnail object-fit-cover shadow-sm" style="width: 120px; height: 120px" />
              <button type="button" 
                class="btn btn-danger btn-sm position-absolute top-0 end-0 m-1 rounded-circle d-flex align-items-center justify-content-center"
                style="width: 24px; height: 24px; padding: 0" @click="removeImage(index)">
                &times;
              </button>
            </div>
          </div>
          <p v-if="errors.images" class="text-danger small mt-1 fw-bold">{{ errors.images }}</p>
        </div>

        <!-- 2. THÔNG TIN SẢN PHẨM -->
        <div class="mb-4 space-y-3">
          <div class="mb-3">
            <label class="form-label fw-bold text-dark">Danh mục</label>
            <select v-model="form.category" required class="form-select border-secondary">
              <option value="" disabled>Chọn danh mục chi tiết nhất</option>
              <option v-for="cat in flattenedCategories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
            </select>
          </div>

          <div class="mb-3">
            <label class="form-label fw-bold text-dark">Tên sản phẩm</label>
            <input type="text" v-model="form.name" required class="form-control border-secondary" placeholder="Ví dụ: Áo khoác da thật..." />
          </div>

          <div class="mb-3">
            <label class="form-label fw-bold text-dark">Giá sản phẩm</label>
            <div class="input-group">
              <input type="number" v-model="form.price" required min="10000" class="form-control border-secondary" />
              <span class="input-group-text bg-white border-secondary">VNĐ</span>
            </div>
          </div>

          <div class="mb-3">
            <label class="form-label fw-bold text-dark">Mô tả sản phẩm</label>
            <textarea v-model="form.description" rows="5" class="form-control border-secondary"></textarea>
          </div>
        </div>

        <hr class="my-5" />

        <h3 class="h4 fw-bold mb-4 text-dark">Thông tin Vận chuyển & Tình trạng</h3>

        <!-- 3. KHO LẤY HÀNG -->
        <div class="mb-4 p-3 border rounded bg-light-subtle">
          <label class="form-label fw-bold text-primary fs-5 mb-3"><i class="bi bi-geo-alt-fill"></i> Kho lấy hàng (Dành cho Shipper)</label>
          <div class="d-flex align-items-center gap-2">
            <select v-model="form.selectedShopAddressId" class="form-select border-primary shadow-sm" style="height: 45px" required>
              <option value="" disabled>-- Chọn kho hàng --</option>
              <option v-for="addr in shopAddresses" :key="addr.id || addr.diaChiId" :value="addr.id || addr.diaChiId">
                {{ addr.diaChiChiTiet }}
              </option>
            </select>
            <button type="button" class="btn btn-primary fw-bold text-nowrap" style="height: 45px" @click="openAddressModal">Thêm kho</button>
          </div>
        </div>

        <!-- 4. KÍCH THƯỚC & CÂN NẶNG -->
        <div class="row g-3 mb-4">
          <div class="col-6 col-md-3">
            <label class="form-label small fw-medium">Chiều dài (cm)</label>
            <input type="number" v-model="form.dimensions.length" class="form-control" />
          </div>
          <div class="col-6 col-md-3">
            <label class="form-label small fw-medium">Chiều rộng (cm)</label>
            <input type="number" v-model="form.dimensions.width" class="form-control" />
          </div>
          <div class="col-6 col-md-3">
            <label class="form-label small fw-medium">Chiều cao (cm)</label>
            <input type="number" v-model="form.dimensions.height" class="form-control" />
          </div>
          <div class="col-6 col-md-3">
            <label class="form-label small fw-medium">Cân nặng (g) *</label>
            <input type="number" v-model="form.weight" class="form-control" required />
          </div>
        </div>

        <!-- 5. TÌNH TRẠNG -->
        <div class="row g-2 mb-4">
          <div class="col-12 col-md" v-for="cond in conditions" :key="cond.title">
            <input type="radio" class="btn-check" :id="'cond-' + cond.title" :value="cond.title" v-model="form.condition" />
            <label class="btn btn-outline-dark w-100 h-100 p-3 text-center d-flex flex-column" :for="'cond-' + cond.title">
              <span class="fw-bold small mb-2 border-bottom pb-1">{{ cond.title }}</span>
              <span style="font-size: 11px">{{ cond.desc }}</span>
            </label>
          </div>
        </div>

        <div class="mt-5 d-flex justify-content-center">
          <button type="submit" class="btn btn-danger fw-bold px-5 py-2 fs-5 rounded-pill shadow" :disabled="isSubmitting">
            <span v-if="isSubmitting" class="spinner-border spinner-border-sm me-2"></span>
            Xác nhận Đăng Bán
          </button>
        </div>
      </form>
    </main>

    <!-- 🔥 MODAL XÁC MINH SĐT & OTP ĐƠN GIẢN (KHÔNG DÙNG FIREBASE) -->
    <div v-if="showConfirmModal" class="modal fade show d-block" tabindex="-1" style="background: rgba(0, 0, 0, 0.6); z-index: 10000;">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg rounded-4">
          <div class="modal-header border-bottom-0 pb-0">
            <h5 class="modal-title fw-bold text-dark">Xác minh đăng bài</h5>
            <button type="button" class="btn-close" @click="showConfirmModal = false"></button>
          </div>
          <div class="modal-body p-4">
            <div class="mb-3">
              <label class="form-label fw-bold small text-dark">Số điện thoại liên hệ *</label>
              <input type="text" v-model="form.phone" class="form-control border-secondary fw-bold" placeholder="0987654321" />
            </div>
            <div class="mb-3">
              <label class="form-label fw-bold small text-danger">Mã OTP xác nhận (Test: 123456) *</label>
              <input type="text" v-model="form.otpCode" class="form-control text-center fw-bold fs-3 border-danger" maxlength="6" autofocus />
            </div>
          </div>
          <div class="modal-footer border-top-0 pt-0 d-flex gap-2">
            <button type="button" @click="showConfirmModal = false" class="btn btn-outline-secondary px-4">Hủy</button>
            <button type="button" @click="submitForm" class="btn btn-danger px-4 fw-bold shadow" :disabled="isSubmitting">
              Xác nhận & Đăng
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 🔥 MODAL THÊM KHO LẤY HÀNG MỚI (DÙNG API GHN TỪ CODE CŨ CỦA BẠN) -->
    <div v-if="showAddressModal" class="modal fade show d-block" tabindex="-1" style="background: rgba(0, 0, 0, 0.6); z-index: 10000;">
      <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg">
          <div class="modal-header text-white pb-3 pt-3" style="background-color: #1a73e8;">
            <h5 class="modal-title fw-bold">📍 Thêm kho lấy hàng mới</h5>
            <button type="button" class="btn-close btn-close-white" @click="showAddressModal = false"></button>
          </div>
          <div class="modal-body p-4">
            <p class="text-muted small mb-4">Vui lòng nhập chính xác để Shipper GHN đến đúng kho lấy hàng.</p>

            <div class="row g-3 mb-4">
              <!-- Chọn Tỉnh/Thành -->
              <div class="col-md-4">
                <label class="form-label fw-bold small text-dark">Tỉnh / Thành phố <span class="text-danger">*</span></label>
                <select class="form-select border-secondary" v-model="addressForm.province" @change="fetchDistricts" :disabled="isLoadingGHN">
                  <option value="">Chọn Tỉnh / Thành phố</option>
                  <option v-for="prov in provincesList" :key="prov.ProvinceID" :value="prov">{{ prov.ProvinceName }}</option>
                </select>
              </div>
              
              <!-- Chọn Quận/Huyện -->
              <div class="col-md-4">
                <label class="form-label fw-bold small text-dark">Quận / Huyện <span class="text-danger">*</span></label>
                <select class="form-select border-secondary" v-model="addressForm.district" @change="fetchWards" :disabled="!addressForm.province || isLoadingGHN">
                  <option value="">Chọn Quận / Huyện</option>
                  <option v-for="dist in districtsList" :key="dist.DistrictID" :value="dist">{{ dist.DistrictName }}</option>
                </select>
              </div>

              <!-- Chọn Phường/Xã -->
              <div class="col-md-4">
                <label class="form-label fw-bold small text-dark">Phường / Xã <span class="text-danger">*</span></label>
                <select class="form-select border-secondary" v-model="addressForm.ward" :disabled="!addressForm.district || isLoadingGHN">
                  <option value="">Chọn Phường / Xã</option>
                  <option v-for="ward in wardsList" :key="ward.WardCode" :value="ward">{{ ward.WardName }}</option>
                </select>
              </div>
            </div>

            <div class="mb-2">
              <label class="form-label fw-bold small text-dark">Số nhà, tên đường cụ thể <span class="text-danger">*</span></label>
              <textarea v-model="addressForm.detail" class="form-control border-secondary" rows="3" placeholder="Ví dụ: 123 Đường Quang Trung..."></textarea>
            </div>
          </div>
          <div class="modal-footer bg-light border-top-0 pt-3 pb-3">
            <button type="button" class="btn btn-outline-secondary px-4 bg-white" @click="showAddressModal = false">Hủy</button>
            <button type="button" class="btn text-white px-4 fw-bold" style="background-color: #1a73e8;" @click="saveNewAddress" :disabled="isSavingAddress">
              <span v-if="isSavingAddress" class="spinner-border spinner-border-sm me-2"></span>
              Lưu Địa Chỉ
            </button>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { reactive, ref, onMounted, computed } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import AppHeader from '@/layouts/Header.vue';

const router = useRouter();
const isSubmitting = ref(false);
const showConfirmModal = ref(false);
const showAddressModal = ref(false);

const storedUser = JSON.parse(localStorage.getItem("user") || "{}");
const currentUserId = storedUser.id || storedUser.nguoiDungId;

// ==========================================
// FORM SẢN PHẨM & XỬ LÝ ẢNH
// ==========================================
const form = reactive({
  category: "",
  name: "",
  price: null,
  description: "",
  selectedShopAddressId: "",
  dimensions: { length: null, width: null, height: null },
  weight: null,
  condition: "Mới",
  phone: localStorage.getItem("verifiedPhone") || storedUser.soDienThoai || "",
  otpCode: "",
  firebaseToken: "BYPASS_OTP"
});

const errors = reactive({ images: "" });
const previewImages = ref([]);
const selectedFiles = ref([]);
const fileInput = ref(null);
const categories = ref([]);
const shopAddresses = ref([]);

const triggerFileInput = () => fileInput.value.click();
const handleFileChange = (e) => {
  const files = Array.from(e.target.files);
  files.forEach(f => {
    selectedFiles.value.push(f);
    previewImages.value.push(URL.createObjectURL(f));
  });
};
const removeImage = (i) => {
  selectedFiles.value.splice(i, 1);
  previewImages.value.splice(i, 1);
};

const conditions = [
  { title: "Mới", desc: "Hàng mới kèm mác..." },
  { title: "Như mới", desc: "Hàng mới chưa mác..." },
  { title: "Tốt", desc: "Đã qua sử dụng tốt." },
  { title: "Trung bình", desc: "Đầy đủ chức năng." },
  { title: "Kém", desc: "Nhiều sai sót." }
];

const flattenedCategories = computed(() => {
  const result = [];
  const flatten = (cats, prefix = "") => {
    cats.forEach(c => {
      result.push({ id: c.id || c.danhMucId, name: prefix + (c.name || c.tenDanhMuc) });
      if (c.children) flatten(c.children, prefix + "- ");
    });
  };
  flatten(categories.value);
  return result;
});

// ==========================================
// LOGIC THÊM KHO BẰNG API GHN (LẤY TỪ CODE CŨ CỦA BẠN)
// ==========================================
const isSavingAddress = ref(false);
const isLoadingGHN = ref(false);

const GHN_TOKEN = import.meta.env.VITE_GHN_TOKEN || "THAY_TOKEN_GHN_CUA_BAN_VAO_DAY";
const GHN_BASE_URL = import.meta.env.VITE_GHN_URL || "https://dev-online-gateway.ghn.vn/shiip/public-api";

const provincesList = ref([]);
const districtsList = ref([]);
const wardsList = ref([]);

const addressForm = reactive({
  province: "",
  district: "",
  ward: "",
  detail: ""
});

const openAddressModal = () => {
  addressForm.province = "";
  addressForm.district = "";
  addressForm.ward = "";
  addressForm.detail = "";
  districtsList.value = [];
  wardsList.value = [];
  showAddressModal.value = true;
  if (provincesList.value.length === 0) fetchProvinces();
};

const fetchProvinces = async () => {
  isLoadingGHN.value = true;
  try {
    const res = await axios.get(`${GHN_BASE_URL}/master-data/province`, {
      headers: { Token: GHN_TOKEN }
    });
    provincesList.value = res.data.data;
  } catch (error) {
    console.error("Lỗi lấy Tỉnh:", error);
  } finally {
    isLoadingGHN.value = false;
  }
};

const fetchDistricts = async () => {
  if (!addressForm.province) return;
  addressForm.district = "";
  addressForm.ward = "";
  wardsList.value = [];
  isLoadingGHN.value = true;
  try {
    const res = await axios.get(`${GHN_BASE_URL}/master-data/district?province_id=${addressForm.province.ProvinceID}`, {
      headers: { Token: GHN_TOKEN }
    });
    districtsList.value = res.data.data;
  } catch (error) {
    console.error("Lỗi lấy Huyện:", error);
  } finally {
    isLoadingGHN.value = false;
  }
};

const fetchWards = async () => {
  if (!addressForm.district) return;
  addressForm.ward = "";
  isLoadingGHN.value = true;
  try {
    const res = await axios.get(`${GHN_BASE_URL}/master-data/ward?district_id=${addressForm.district.DistrictID}`, {
      headers: { Token: GHN_TOKEN }
    });
    wardsList.value = res.data.data;
  } catch (error) {
    console.error("Lỗi lấy Xã:", error);
  } finally {
    isLoadingGHN.value = false;
  }
};

const saveNewAddress = async () => {
  if (!addressForm.province || !addressForm.district || !addressForm.ward || !addressForm.detail) {
    alert("Vui lòng điền đầy đủ thông tin địa chỉ!");
    return;
  }

  // Ghép chuỗi địa chỉ hoàn chỉnh
  const fullAddress = `${addressForm.detail}, ${addressForm.ward.WardName}, ${addressForm.district.DistrictName}, ${addressForm.province.ProvinceName}`;

  isSavingAddress.value = true;
  try {
    // 🔥 ĐÃ SỬA: Payload giờ đã khớp 100% với ShopAddressRequestDto của Java
    const payload = {
      huyenCode: Number(addressForm.district.DistrictID), // Ép kiểu số (Integer)
      phuongXaId: String(addressForm.ward.WardCode),      // Ép kiểu chuỗi (String)
      diaChiChiTiet: fullAddress
    };

    // Gọi API lưu địa chỉ
    const res = await axios.post(`http://localhost:8080/api/addresses/shop/${currentUserId}`, payload);
    
    // Đẩy địa chỉ mới vào list và tự động chọn
    const newAddress = res.data;
    shopAddresses.value.push(newAddress);
    form.selectedShopAddressId = newAddress.id || newAddress.diaChiId;

    showAddressModal.value = false;
    alert("Đã thêm kho hàng mới thành công!");

  } catch (error) {
    alert("Không thể lưu địa chỉ: " + (error.response?.data?.message || error.response?.data?.error || "Lỗi server"));
  } finally {
    isSavingAddress.value = false;
  }
};  


// ==========================================
// LOGIC OTP ĐƠN GIẢN & SUBMIT FORM SẢN PHẨM
// ==========================================
const isPhoneVerified = () => {
  return localStorage.getItem("isPhoneVerified") === "true";
};

const handlePublishAction = () => {
  if (selectedFiles.value.length === 0) {
    errors.images = "Vui lòng tải ít nhất 1 ảnh sản phẩm!";
    return;
  }

  // Bắt buộc chọn kho
  if (!form.selectedShopAddressId) {
    alert("Vui lòng chọn hoặc thêm kho lấy hàng trước khi đăng bán!");
    return;
  }

  if (isPhoneVerified()) {
    form.otpCode = "123456";
    submitForm();
  } else {
    showConfirmModal.value = true;
  }
};

const submitForm = async () => {
  if (isSubmitting.value) return;
  isSubmitting.value = true;

  try {
    const payload = {
      nguoiDungId: currentUserId,
      danhMucId: form.category,
      tenSanPham: form.name,
      gia: form.price,
      moTaSp: form.description,
      tinhTrang: form.condition,
      trongLuongGram: form.weight,
      chieuDaiCm: form.dimensions.length,
      chieuRongCm: form.dimensions.width,
      chieuCaoCm: form.dimensions.height,
      soDienThoai: form.phone,
      otpCode: form.otpCode,
      firebaseToken: form.firebaseToken,
      idDiaChiCuaHang: form.selectedShopAddressId
    };

    const response = await axios.post("http://localhost:8080/api/products", payload);
    const newId = response.data.sanPhamId;

    if (selectedFiles.value.length > 0) {
      const formData = new FormData();
      selectedFiles.value.forEach(file => formData.append("files", file));
      await axios.post(`http://localhost:8080/api/products/${newId}/hinh-anh`, formData);
    }

    localStorage.setItem("isPhoneVerified", "true");
    localStorage.setItem("verifiedPhone", form.phone);

    alert("🎉 Đăng bán thành công! Trạng thái xác minh của bạn đã được lưu.");
    showConfirmModal.value = false;
    router.push("/");
  } catch (error) {
    alert("❌ Lỗi: " + (error.response?.data?.error || "Vui lòng kiểm tra lại thông tin và mã OTP!"));
  } finally {
    isSubmitting.value = false;
  }
};

// ==========================================
// MOUNT DATA LẦN ĐẦU
// ==========================================
onMounted(async () => {
  try {
    const resCat = await axios.get("http://localhost:8080/api/categories/tree");
    categories.value = resCat.data;
  } catch (err) {
    console.error("Lỗi fetch Danh mục:", err);
  }

  try {
    const resAddr = await axios.get(`http://localhost:8080/api/addresses/shop/${currentUserId}`);
    shopAddresses.value = resAddr.data;
  } catch (err) {
    console.error("Lỗi fetch Địa chỉ:", err);
  }
});
</script>