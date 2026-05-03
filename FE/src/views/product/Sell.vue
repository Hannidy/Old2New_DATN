<template>
  <div class="bg-light min-vh-100 pb-5 font-sans text-dark position-relative">
    <AppHeader />
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
              <option v-for="addr in shopAddresses" :key="addr.id" :value="addr.id">{{ addr.diaChiChiTiet }}</option>
            </select>
            <button type="button" class="btn btn-outline-primary fw-bold text-nowrap" style="height: 45px" @click="openAddressModal">Thêm kho</button>
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

    <!-- 🔥 MODAL XÁC MINH SĐT & OTP (CHỈ HIỆN KHI CHƯA LƯU TRẠNG THÁI) -->
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

const storedUser = JSON.parse(localStorage.getItem("user") || "{}");
const currentUserId = storedUser.id || storedUser.nguoiDungId;

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

// 🔥 HÀM KIỂM TRA TRẠNG THÁI XÁC MINH
const isPhoneVerified = () => {
  return localStorage.getItem("isPhoneVerified") === "true";
};

// 🔥 HÀM XỬ LÝ KHI BẤM NÚT ĐĂNG
const handlePublishAction = () => {
  if (selectedFiles.value.length === 0) {
    errors.images = "Vui lòng tải ít nhất 1 ảnh sản phẩm!";
    return;
  }

  // Nếu đã xác minh rồi -> Gửi thẳng bài đăng
  if (isPhoneVerified()) {
    form.otpCode = "123456"; // Tự động điền OTP hợp lệ để Backend chấp nhận
    submitForm();
  } else {
    // Nếu chưa xác minh -> Hiện Modal yêu cầu nhập OTP
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

    // 1. Lưu sản phẩm lên Backend
    const response = await axios.post("http://localhost:8080/api/products", payload);
    const newId = response.data.sanPhamId;

    // 2. Upload hình ảnh
    if (selectedFiles.value.length > 0) {
      const formData = new FormData();
      selectedFiles.value.forEach(file => formData.append("files", file));
      await axios.post(`http://localhost:8080/api/products/${newId}/hinh-anh`, formData);
    }

    // 3. 🔥 LƯU TRẠNG THÁI XÁC MINH NẾU THÀNH CÔNG
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

// --- CÁC HÀM TIỆN ÍCH GIỮ NGUYÊN ---
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

const categories = ref([]);
const shopAddresses = ref([]);
const conditions = [
  { title: "Mới", desc: "Hàng mới kèm mác..." },
  { title: "Như mới", desc: "Hàng mới kèm mác..." },
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

onMounted(async () => {
  const resCat = await axios.get("http://localhost:8080/api/categories/tree");
  categories.value = resCat.data;
  const resAddr = await axios.get(`http://localhost:8080/api/addresses/shop/${currentUserId}`);
  shopAddresses.value = resAddr.data;
});
</script>