<template>
  <div class="bg-light min-vh-100 pb-5 font-sans text-dark position-relative">
    <header class="bg-white border-bottom mb-4">
      <div
        class="container d-flex align-items-center justify-content-between py-3"
      >
        <div class="d-flex align-items-center gap-4 flex-grow-1">
          <h1 class="h3 fw-bold mb-0 text-dark">Old2New</h1>
          <div class="input-group" style="max-width: 500px">
            <input
              type="text"
              class="form-control"
              placeholder="Tìm kiếm sản phẩm..."
            />
            <button
              class="btn btn-outline-secondary bg-light text-dark"
              type="button"
            >
              🔍
            </button>
          </div>
        </div>
        <div class="d-flex align-items-center gap-3">
          <button
            class="btn btn-light border-secondary border fw-medium btn-sm px-3"
          >
            Tìm nâng cao
          </button>
          <button class="btn btn-light border-0 fs-5 px-2">🛒</button>
          <button class="btn btn-light border-0 fs-5 px-2">🔔</button>
          <button class="btn btn-outline-dark fw-medium btn-sm px-4">
            Đăng bán
          </button>
        </div>
      </div>
    </header>

    <main class="container bg-white shadow-sm p-4 p-md-5 rounded">
      <h2 class="h3 fw-bold mb-4 text-dark">Đăng bán sản phẩm</h2>

      <form @submit.prevent="submitForm">
        <div class="mb-4">
          <input
            type="file"
            ref="fileInput"
            multiple
            accept="image/*"
            class="d-none"
            @change="handleFileChange"
          />
          <div
            class="mb-3 p-5 text-center bg-light rounded text-muted"
            style="
              border: 2px dashed #ced4da;
              cursor: pointer;
              transition: background-color 0.2s;
            "
            @click="triggerFileInput"
            onmouseover="this.style.backgroundColor = '#e2e6ea'"
            onmouseout="this.style.backgroundColor = '#f8f9fa'"
          >
            <h3 class="fs-4 fw-medium text-secondary mb-2">
              Tải hình ảnh/video
            </h3>
            <p class="small mb-0">Kéo thả hoặc nhấn để chọn file từ thiết bị</p>
          </div>
          <p v-if="errors.images" class="text-danger small mt-1 fw-bold">
            {{ errors.images }}
          </p>

          <div
            v-if="previewImages.length > 0"
            class="d-flex flex-wrap gap-3 p-3 border rounded bg-white mt-2"
          >
            <div
              v-for="(img, index) in previewImages"
              :key="index"
              class="position-relative"
            >
              <img
                :src="img"
                class="img-thumbnail object-fit-cover shadow-sm"
                style="width: 120px; height: 120px"
              />
              <button
                type="button"
                class="btn btn-danger btn-sm position-absolute top-0 end-0 m-1 rounded-circle d-flex align-items-center justify-content-center"
                style="width: 24px; height: 24px; padding: 0"
                @click="removeImage(index)"
              >
                &times;
              </button>
            </div>
          </div>
        </div>

        <div class="mb-4 space-y-3">
          <div class="mb-3">
            <label class="form-label fw-bold text-dark">Danh mục</label>
            <select
              v-model="form.category"
              required
              class="form-select border-secondary text-muted"
            >
              <option value="" disabled>Chọn danh mục chi tiết nhất</option>
              <option
                v-for="cat in flattenedCategories"
                :key="cat.id"
                :value="cat.id"
              >
                {{ cat.name }}
              </option>
            </select>
            <p v-if="errors.category" class="text-danger small mt-1 fw-bold">
              {{ errors.category }}
            </p>
          </div>

          <div class="mb-3">
            <label class="form-label fw-bold text-dark">Tên sản phẩm</label>
            <input
              type="text"
              v-model="form.name"
              required
              class="form-control border-secondary"
              placeholder="Ví dụ: Áo khoác da thật..."
            />
            <p v-if="errors.name" class="text-danger small mt-1 fw-bold">
              {{ errors.name }}
            </p>
          </div>

          <div class="mb-3">
            <label class="form-label fw-bold text-dark">Giá sản phẩm</label>
            <div class="input-group">
              <input
                type="number"
                v-model="form.price"
                required
                min="10000"
                class="form-control border-secondary"
                placeholder="Nhập giá bán..."
              />
              <span class="input-group-text bg-white border-secondary"
                >VNĐ</span
              >
            </div>
            <p v-if="errors.price" class="text-danger small mt-1 fw-bold">
              {{ errors.price }}
            </p>
          </div>

          <div class="mb-3">
            <label class="form-label fw-bold text-dark">Mô tả sản phẩm</label>
            <textarea
              v-model="form.description"
              rows="5"
              class="form-control border-secondary"
              placeholder="Mô tả chi tiết tình trạng, xuất xứ..."
            ></textarea>
          </div>
        </div>

        <hr class="my-5" />

        <h3 class="h4 fw-bold mb-4 text-dark">
          Thông tin Vận chuyển & Tình trạng
        </h3>

        <div class="mb-4 p-3 border rounded bg-light-subtle">
          <label class="form-label fw-bold text-primary fs-5 mb-3"
            ><i class="bi bi-geo-alt-fill"></i> Kho lấy hàng (Dành cho
            Shipper)</label
          >

          <div
            v-if="shopAddresses.length > 0"
            class="d-flex align-items-center gap-2"
          >
            <select
              v-model="form.selectedShopAddressId"
              class="form-select border-primary shadow-sm"
              style="height: 45px"
            >
              <option value="" disabled>
                -- Chọn kho hàng để giao cho vận chuyển --
              </option>
              <option
                v-for="addr in shopAddresses"
                :key="addr.id"
                :value="addr.id"
              >
                {{ addr.diaChiChiTiet }}
                {{ addr.isDefault === 1 ? "(Mặc định)" : "" }}
              </option>
            </select>
            <button
              type="button"
              class="btn btn-outline-primary fw-bold text-nowrap"
              style="height: 45px"
              @click="openAddressModal"
            >
              <i class="bi bi-plus-lg"></i> Thêm kho
            </button>
          </div>

          <div
            v-else
            class="alert alert-warning mb-0 border-warning d-flex justify-content-between align-items-center"
          >
            <span
              ><strong>Cửa hàng chưa có kho hàng!</strong> Vui lòng thiết lập để
              tiếp tục.</span
            >
            <button
              type="button"
              class="btn btn-warning fw-bold text-dark shadow-sm"
              @click="openAddressModal"
            >
              <i class="bi bi-plus-circle me-1"></i> Thêm kho hàng
            </button>
          </div>
          <p
            v-if="errors.shippingAddress"
            class="text-danger small mt-2 fw-bold"
          >
            <i class="bi bi-exclamation-triangle-fill"></i>
            {{ errors.shippingAddress }}
          </p>
        </div>
        <div class="mb-4">
          <label class="form-label small text-dark fw-medium d-block mb-2"
            >Phí vận chuyển</label
          >
          <div class="form-check form-check-inline">
            <input
              class="form-check-input border-secondary"
              type="radio"
              v-model="form.shippingPayer"
              value="buyer"
              id="buyer"
            />
            <label class="form-check-label small" for="buyer"
              >Người mua trả</label
            >
          </div>
          <div class="form-check form-check-inline">
            <input
              class="form-check-input border-secondary"
              type="radio"
              v-model="form.shippingPayer"
              value="seller"
              id="seller"
            />
            <label class="form-check-label small" for="seller"
              >Người bán trả</label
            >
          </div>
        </div>

        <h4 class="h6 fw-bold text-secondary mb-3 mt-4">
          Kích thước (Dùng để tính phí GHN)
        </h4>
        <div class="row g-3 mb-4">
          <div class="col-6 col-md-3">
            <label class="form-label small text-dark fw-medium"
              >Chiều dài (cm)</label
            >
            <input
              type="number"
              v-model="form.dimensions.length"
              class="form-control border-secondary"
            />
          </div>
          <div class="col-6 col-md-3">
            <label class="form-label small text-dark fw-medium"
              >Chiều rộng (cm)</label
            >
            <input
              type="number"
              v-model="form.dimensions.width"
              class="form-control border-secondary"
            />
          </div>
          <div class="col-6 col-md-3">
            <label class="form-label small text-dark fw-medium"
              >Chiều cao (cm)</label
            >
            <input
              type="number"
              v-model="form.dimensions.height"
              class="form-control border-secondary"
            />
          </div>
          <div class="col-6 col-md-3">
            <label class="form-label small text-dark fw-medium"
              >Cân nặng (g) <span class="text-danger">*</span></label
            >
            <input
              type="number"
              v-model="form.weight"
              class="form-control border-secondary"
              required
            />
          </div>
        </div>

        <h4 class="h6 fw-bold text-secondary mb-3 mt-4">
          Tình trạng <span class="text-danger">*</span>
        </h4>
        <div class="row g-2">
          <div
            class="col-12 col-md"
            v-for="cond in conditions"
            :key="cond.title"
          >
            <input
              type="radio"
              class="btn-check"
              :id="'cond-' + cond.title"
              :value="cond.title"
              v-model="form.condition"
            />
            <label
              class="btn btn-outline-dark w-100 h-100 p-3 text-center d-flex flex-column align-items-center justify-content-center"
              :for="'cond-' + cond.title"
            >
              <span
                class="fw-bold small mb-2 d-block w-100 border-bottom border-secondary pb-1"
                >{{ cond.title }}</span
              >
              <span
                style="font-size: 11px; line-height: 1.2"
                class="opacity-75"
                >{{ cond.desc }}</span
              >
            </label>
          </div>
        </div>

        <div class="mt-5 pt-4 border-top d-flex justify-content-center gap-3">
          <button
            type="button"
            class="btn btn-outline-dark fw-medium px-4 bg-white"
            @click="router.push('/')"
          >
            Trở lại
          </button>
          <button
            type="submit"
            class="btn btn-danger fw-bold px-5 py-2 fs-5 rounded-pill shadow"
            :disabled="isSubmitting"
          >
            <span
              v-if="isSubmitting"
              class="spinner-border spinner-border-sm me-2"
            ></span>
            {{ isSubmitting ? "Đang xử lý..." : "Xác nhận Đăng Bán" }}
          </button>
        </div>
      </form>
    </main>

    <div
      v-if="showAddressModal"
      class="modal fade show d-block"
      tabindex="-1"
      style="background: rgba(0, 0, 0, 0.6)"
    >
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg rounded-4 overflow-hidden">
          <div class="modal-header bg-primary border-bottom-0">
            <h5 class="modal-title fw-bold text-white">
              <i class="bi bi-shop"></i> Thêm kho lấy hàng mới
            </h5>
            <button
              type="button"
              class="btn-close btn-close-white"
              @click="showAddressModal = false"
            ></button>
          </div>
          <div class="modal-body p-4 bg-light">
            <p class="text-muted small mb-4">
              Vui lòng nhập chính xác để Shipper GHN đến đúng kho lấy hàng.
            </p>

            <div class="mb-3">
              <label class="form-label fw-bold small text-dark"
                >Tỉnh / Thành phố <span class="text-danger">*</span></label
              >
              <select
                class="form-select border-secondary"
                v-model="addressForm.province"
                @change="fetchDistricts"
                :disabled="isLoadingGHN"
              >
                <option value="" disabled>-- Chọn Tỉnh / Thành phố --</option>
                <option
                  v-for="prov in provincesList"
                  :key="prov.ProvinceID"
                  :value="prov"
                >
                  {{ prov.ProvinceName }}
                </option>
              </select>
            </div>

            <div class="mb-3">
              <label class="form-label fw-bold small text-dark"
                >Quận / Huyện <span class="text-danger">*</span></label
              >
              <select
                class="form-select border-secondary"
                v-model="addressForm.district"
                @change="fetchWards"
                :disabled="!addressForm.province || isLoadingGHN"
              >
                <option value="" disabled>-- Chọn Quận / Huyện --</option>
                <option
                  v-for="dist in districtsList"
                  :key="dist.DistrictID"
                  :value="dist"
                >
                  {{ dist.DistrictName }}
                </option>
              </select>
            </div>

            <div class="mb-3">
              <label class="form-label fw-bold small text-dark"
                >Phường / Xã <span class="text-danger">*</span></label
              >
              <select
                class="form-select border-secondary"
                v-model="addressForm.ward"
                :disabled="!addressForm.district || isLoadingGHN"
              >
                <option value="" disabled>-- Chọn Phường / Xã --</option>
                <option
                  v-for="ward in wardsList"
                  :key="ward.WardCode"
                  :value="ward"
                >
                  {{ ward.WardName }}
                </option>
              </select>
            </div>

            <div class="mb-3">
              <label class="form-label fw-bold small text-dark"
                >Số nhà, tên đường <span class="text-danger">*</span></label
              >
              <input
                type="text"
                class="form-control border-secondary"
                v-model="addressForm.detail"
                placeholder="Ví dụ: 123 Đường Quang Trung..."
                required
              />
            </div>
          </div>
          <div class="modal-footer border-top bg-white">
            <button
              type="button"
              @click="showAddressModal = false"
              class="btn btn-outline-secondary px-4"
            >
              Hủy
            </button>
            <button
              type="button"
              @click="saveShopAddress"
              class="btn btn-primary px-4 fw-bold shadow-sm"
              :disabled="
                isSavingAddress || !addressForm.ward || !addressForm.detail
              "
            >
              <span
                v-if="isSavingAddress"
                class="spinner-border spinner-border-sm me-2"
              ></span>
              {{ isSavingAddress ? "Đang lưu..." : "Lưu địa chỉ kho" }}
            </button>
          </div>
        </div>
      </div>
    </div>

    <div
      v-if="showOtpModal"
      class="modal fade show d-block"
      tabindex="-1"
      style="background: rgba(0, 0, 0, 0.6)"
    >
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow">
          <div class="modal-header border-bottom">
            <h5 class="modal-title fw-bold">Xác minh số điện thoại</h5>
            <button
              type="button"
              class="btn-close"
              @click="showOtpModal = false"
            ></button>
          </div>
          <div class="modal-body p-4">
            <p class="text-muted small mb-4">
              Bạn cần xác minh SĐT để bảo vệ an toàn cho các giao dịch.
            </p>
            <div class="mb-4">
              <label class="form-label fw-bold small text-dark"
                >Số điện thoại</label
              >
              <div class="input-group">
                <input
                  type="text"
                  v-model="form.phone"
                  placeholder="Ví dụ: 0987654321"
                  class="form-control border-secondary"
                />
                <button
                  type="button"
                  @click="requestOtp"
                  :disabled="isRequestingOtp"
                  class="btn btn-dark"
                >
                  <span
                    v-if="isRequestingOtp"
                    class="spinner-border spinner-border-sm me-1"
                  ></span>
                  {{ isRequestingOtp ? "Đang gửi..." : "Lấy mã" }}
                </button>
              </div>
            </div>
            <div
              id="recaptcha-container"
              class="d-flex justify-content-center mb-3"
            ></div>
            <div v-if="confirmationResult" class="mb-3">
              <label class="form-label fw-bold text-success small"
                >Mã OTP (có hiệu lực 5 phút)</label
              >
              <input
                type="text"
                v-model="form.otpCode"
                placeholder="Nhập 6 số..."
                class="form-control border-success text-center fw-bold fs-4"
                style="letter-spacing: 0.3em"
              />
            </div>
          </div>
          <div
            class="modal-footer border-top bg-light justify-content-end gap-2 p-3"
          >
            <button
              type="button"
              @click="showOtpModal = false"
              class="btn btn-outline-secondary bg-white px-4"
            >
              Hủy
            </button>
            <button
              type="button"
              @click="confirmAndSubmit"
              class="btn btn-dark px-4"
              :disabled="isSubmitting"
            >
              <span
                v-if="isSubmitting"
                class="spinner-border spinner-border-sm me-2"
              ></span>
              {{ isSubmitting ? "Đang xử lý..." : "Xác nhận & Đăng" }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, nextTick, onMounted, computed } from "vue";
import { useRouter } from "vue-router";
import { initializeApp } from "firebase/app";
import {
  getAuth,
  RecaptchaVerifier,
  signInWithPhoneNumber,
} from "firebase/auth";
import axios from "axios";
// (Nếu em xài AppHeader trong layouts thì bỏ comment ra nhé, ở trên em không thấy import AppHeader nên chị giả định có cấu hình global hoặc em tự import)
// import AppHeader from '@/layouts/Header.vue';

const router = useRouter();
const isSubmitting = ref(false);
let currentUserId = null;

// =========================================================
// QUẢN LÝ TRẠNG THÁI ĐỊA CHỈ KHO & API GHN
// =========================================================
const shopAddresses = ref([]);
const showAddressModal = ref(false);
const isSavingAddress = ref(false);
const isLoadingGHN = ref(false);

// Token GHN thật của Dương
const GHN_TOKEN = import.meta.env.VITE_GHN_TOKEN;
const GHN_SHOP_ID = import.meta.env.VITE_GHN_SHOP_ID;
const GHN_BASE_URL = import.meta.env.VITE_GHN_URL;

const provincesList = ref([]);
const districtsList = ref([]);
const wardsList = ref([]);

const addressForm = reactive({
  province: "",
  district: "",
  ward: "",
  detail: "",
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
      headers: {
        Token: GHN_TOKEN,
      },
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
    const res = await axios.get(
      `${GHN_BASE_URL}/master-data/district?province_id=${addressForm.province.ProvinceID}`,
      {
        headers: {
          Token: GHN_TOKEN,
        },
      },
    );
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
    const res = await axios.get(
      `${GHN_BASE_URL}/master-data/ward?district_id=${addressForm.district.DistrictID}`,
      {
        headers: {
          Token: GHN_TOKEN,
        },
      },
    );
    wardsList.value = res.data.data;
  } catch (error) {
    console.error("Lỗi lấy Xã:", error);
  } finally {
    isLoadingGHN.value = false;
  }
};

const fetchShopAddresses = async (token) => {
  try {
    const res = await axios.get(
      `http://localhost:8080/api/shops/my-shop/addresses/${currentUserId}`,
      {
        headers: { Authorization: `Bearer ${token}` },
      },
    );
    shopAddresses.value = res.data || [];

    // Tự động chọn địa chỉ mặc định
    const defaultAddr = shopAddresses.value.find((a) => a.isDefault === 1);
    if (defaultAddr) form.selectedShopAddressId = defaultAddr.id;
  } catch (error) {
    console.error("Lỗi lấy danh sách kho:", error);
  }
};

const saveShopAddress = async () => {
  isSavingAddress.value = true;
  try {
    // 🔥 1. Cập nhật cách lấy Token "Bọc thép" chuẩn xác 100%
    const storedUserStr = localStorage.getItem("user");
    const storedUser = storedUserStr ? JSON.parse(storedUserStr) : {};
    const token =
      storedUser.token ||
      storedUser.accessToken ||
      localStorage.getItem("token");

    // Chặn lại ngay nếu không tìm thấy Token
    if (!token) {
      alert("Lỗi bảo mật: Không tìm thấy Token đăng nhập!");
      isSavingAddress.value = false;
      return;
    }

    const fullAddressString = `${addressForm.detail}, ${addressForm.ward.WardName}, ${addressForm.district.DistrictName}, ${addressForm.province.ProvinceName}`;
    const payload = {
      huyenCode: addressForm.district.DistrictID,
      phuongXaId: addressForm.ward.WardCode,
      diaChiChiTiet: fullAddressString,
    };

    // 🔥 2. Gửi request kèm Token chuẩn
    const response = await axios.post(
      `http://localhost:8080/api/shops/my-shop/address/${currentUserId}`,
      payload,
      {
        headers: { Authorization: `Bearer ${token}` },
      },
    );

    if (response.status === 200 || response.status === 201) {
      await fetchShopAddresses(token); // Tải lại danh sách
      showAddressModal.value = false;
      errors.shippingAddress = "";
      alert("✅ Đã thêm kho hàng thành công!");
    }
  } catch (error) {
    console.error("Lỗi 403 hoặc lỗi mạng:", error);
    alert(
      "❌ Lỗi 403: Backend từ chối quyền truy cập! Vui lòng kiểm tra lại Spring Security.",
    );
  } finally {
    isSavingAddress.value = false;
  }
};

// =========================================================
// LOGIC SẢN PHẨM & ẢNH
// =========================================================
const errors = reactive({
  category: "",
  name: "",
  price: "",
  images: "",
  shippingAddress: "",
});
const categories = ref([]);

const flattenedCategories = computed(() => {
  const result = [];
  const flatten = (cats, prefix = "") => {
    if (!cats) return;
    for (const cat of cats) {
      result.push({
        id: cat.danhMucId || cat.id,
        name: prefix + (cat.tenDanhMuc || cat.name),
      });
      if (cat.children && cat.children.length > 0)
        flatten(cat.children, prefix + "- ");
    }
  };
  flatten(categories.value);
  return result;
});

const fetchCategories = async () => {
  try {
    const response = await axios.get(
      "http://localhost:8080/api/categories/tree",
    );
    categories.value = response.data;
  } catch (e) {
    console.error(e);
  }
};

onMounted(async () => {
  const storedUserStr = localStorage.getItem("user");
  if (!storedUserStr) {
    alert("Vui lòng đăng nhập!");
    router.push("/login");
    return;
  }

  const storedUser = JSON.parse(storedUserStr);
  currentUserId = storedUser.id || storedUser.nguoiDungId;
  const token = storedUser.token || storedUser.accessToken;

  // Gọi API lấy sđt nếu Backend có hỗ trợ (Dùng try-catch bọc kỹ để không văng lỗi đỏ)
  try {
    const response = await axios.get(
      `http://localhost:8080/api/nguoi-dung/${currentUserId}`,
      {
        headers: { Authorization: `Bearer ${token}` },
      },
    );
    if (response.status === 200 && response.data) {
      storedUser.soDienThoai = response.data.soDienThoai;
      localStorage.setItem("user", JSON.stringify(storedUser));
    }
  } catch (error) {
    // Chỉ in log nhẹ nhàng màu vàng để nhắc nhở Dev, không quăng lỗi đỏ làm hoảng hốt
    console.warn(
      "Lưu ý: Không thể đồng bộ số điện thoại từ Backend (Có thể API chưa sẵn sàng).",
      error.message,
    );
  }

  fetchCategories();
  fetchShopAddresses(token); // 🚀 Nạp danh sách kho hàng lên Dropdown
});

// =========================================================
// FIREBASE OTP & SUBMIT FORM
// =========================================================
const firebaseConfig = {
  apiKey: "AIzaSyCy2BDZO1nKsU43fl8LqdAgix92z_G-26E",
  authDomain: "old2new-e8ef2.firebaseapp.com",
  projectId: "old2new-e8ef2",
  storageBucket: "old2new-e8ef2.firebasestorage.app",
  messagingSenderId: "761431606910",
  appId: "1:761431606910:web:379c565c0e096d16a2fa2f",
  measurementId: "G-VW201BGYC5",
};

const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
auth.languageCode = "vi";

const form = reactive({
  category: "",
  name: "",
  price: null,
  description: "",
  selectedShopAddressId: "",
  shippingPayer: "buyer",
  dimensions: { length: null, width: null, height: null },
  weight: null,
  condition: "Mới",
  phone: "",
  otpCode: "",
});

const fileInput = ref(null);
const selectedFiles = ref([]);
const previewImages = ref([]);

const triggerFileInput = () => fileInput.value.click();

const handleFileChange = (event) => {
  const files = Array.from(event.target.files);
  files.forEach((file) => {
    selectedFiles.value.push(file);
    previewImages.value.push(URL.createObjectURL(file));
  });
  event.target.value = "";
};

const removeImage = (index) => {
  selectedFiles.value.splice(index, 1);
  previewImages.value.splice(index, 1);
};

const showOtpModal = ref(false);
const isRequestingOtp = ref(false);
const confirmationResult = ref(null);

const conditions = [
  {
    value: 1,
    title: "Mới",
    desc: "Hàng mới kèm mác, chưa mở hộp/bao bì, chưa qua sử dụng.",
  },
  {
    value: 2,
    title: "Như mới",
    desc: "Hàng mới kèm mác, đã mở bao bì/hộp, chưa qua sử dụng.",
  },
  {
    value: 3,
    title: "Tốt",
    desc: "Đã qua sử dụng, tính năng đầy đủ, hoạt động tốt.",
  },
  {
    value: 4,
    title: "Trung bình",
    desc: "Hàng đã qua sử dụng, đầy đủ chức năng. Lỗi nhỏ.",
  },
  {
    value: 5,
    title: "Kém",
    desc: "Đã qua sử dụng. Nhiều sai sót. Có thể bị hư hỏng.",
  },
];

const validateForm = () => {
  let isValid = true;
  Object.keys(errors).forEach((key) => (errors[key] = ""));

  if (selectedFiles.value.length === 0) {
    errors.images = "Vui lòng tải ảnh sản phẩm!";
    isValid = false;
  }
  if (!form.category) {
    errors.category = "Vui lòng chọn danh mục!";
    isValid = false;
  }
  if (!form.name) {
    errors.name = "Vui lòng nhập tên sản phẩm!";
    isValid = false;
  }
  if (!form.price || form.price < 10000) {
    errors.price = "Giá bán phải từ 10,000đ trở lên!";
    isValid = false;
  }

  // 🔥 BẮT LỖI CHƯA CHỌN KHO HÀNG
  if (!form.selectedShopAddressId) {
    errors.shippingAddress =
      "Vui lòng chọn 1 kho lấy hàng (hoặc thêm kho mới) để Shipper có thể đến lấy!";
    isValid = false;
  }

  if (!isValid) window.scrollTo({ top: 0, behavior: "smooth" });
  return isValid;
};

// --- LOGIC GỌI API ---
const submitForm = async () => {
  if (!validateForm()) return;
  if (isSubmitting.value) return;

  const isVerified = localStorage.getItem("isPhoneVerified") === "true";
  const storedUser = JSON.parse(localStorage.getItem("user") || "{}");
  const hasPhone = !!(
    storedUser.soDienThoai || localStorage.getItem("verifiedPhone")
  );

  if (isVerified || hasPhone) {
    isSubmitting.value = true;
    await confirmAndSubmitNoOtp();
  } else {
    showOtpModal.value = true;
    await nextTick();
    setupRecaptcha();
  }
};

const confirmAndSubmitNoOtp = async () => {
  try {
    const storedUser = JSON.parse(localStorage.getItem("user"));
    const jwtToken = storedUser.token || storedUser.accessToken;
    const verifiedPhone =
      localStorage.getItem("verifiedPhone") || storedUser.soDienThoai;

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
      soDienThoai: verifiedPhone,
      idDiaChiCuaHang: form.selectedShopAddressId, // 🔥 Truyền ID địa chỉ cửa hàng xuống Backend
    };

    const response = await axios.post(
      "http://localhost:8080/api/products",
      payload,
      {
        headers: { Authorization: `Bearer ${jwtToken}` },
      },
    );

    const newId = response.data.sanPhamId || response.data.id;
    if (selectedFiles.value.length > 0) {
      const formData = new FormData();
      selectedFiles.value.forEach((file) => formData.append("files", file));
      await axios.post(
        `http://localhost:8080/api/products/${newId}/hinh-anh`,
        formData,
        {
          headers: { Authorization: `Bearer ${jwtToken}` },
        },
      );
    }

    alert(
      "🎉 Đăng tin thành công!\nSản phẩm của bạn đang chờ Admin kiểm duyệt.",
    );
    router.push("/");
  } catch (error) {
    console.error("Lỗi đăng bán:", error);
    alert("Có lỗi xảy ra khi đăng bài!");
  } finally {
    isSubmitting.value = false;
  }
};

// --- FIREBASE LOGIC ---
const setupRecaptcha = () => {
  if (!window.recaptchaVerifier) {
    window.recaptchaVerifier = new RecaptchaVerifier(
      auth,
      "recaptcha-container",
      { size: "normal" },
    );
    window.recaptchaVerifier.render();
  }
};

const requestOtp = async () => {
  if (!form.phone) return alert("Vui lòng nhập số điện thoại!");
  let cleanPhone = form.phone.replace(/\D/g, "");
  let phoneNumber = cleanPhone.startsWith("0")
    ? "+84" + cleanPhone.substring(1)
    : cleanPhone.startsWith("84")
      ? "+" + cleanPhone
      : "+84" + cleanPhone;

  isRequestingOtp.value = true;
  const appVerifier = window.recaptchaVerifier;

  try {
    const result = await signInWithPhoneNumber(auth, phoneNumber, appVerifier);
    confirmationResult.value = result;
    alert("Đã gửi mã SMS về điện thoại của bạn!");
  } catch (error) {
    alert("Lỗi gửi SMS. Vui lòng thử lại!");
    if (window.recaptchaVerifier) window.recaptchaVerifier.render();
  } finally {
    isRequestingOtp.value = false;
  }
};

const confirmAndSubmit = async () => {
  if (!form.otpCode || !confirmationResult.value)
    return alert("Vui lòng nhập mã OTP để xác nhận!");
  if (isSubmitting.value) return;

  isSubmitting.value = true;

  try {
    const result = await confirmationResult.value.confirm(form.otpCode);
    const firebaseToken = await result.user.getIdToken();

    const storedUser = JSON.parse(localStorage.getItem("user"));
    const jwtToken = storedUser.token || storedUser.accessToken;

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
      diaChiKhoId: form.selectedShopAddressId, // 🔥 Truyền ID Kho
      firebaseToken: firebaseToken,
    };

    const response = await axios.post(
      "http://localhost:8080/api/products",
      payload,
      {
        headers: { Authorization: `Bearer ${jwtToken}` },
      },
    );

    const newId = response.data.sanPhamId || response.data.id;
    if (selectedFiles.value.length > 0) {
      const formData = new FormData();
      selectedFiles.value.forEach((file) => formData.append("files", file));
      await axios.post(
        `http://localhost:8080/api/products/${newId}/hinh-anh`,
        formData,
        {
          headers: { Authorization: `Bearer ${jwtToken}` },
        },
      );
    }

    alert(
      "🎉 Đăng tin thành công!\nSản phẩm của bạn đang chờ Admin kiểm duyệt.",
    );
    showOtpModal.value = false;
    router.push("/");
  } catch (error) {
    console.error(error);
    alert("❌ Lỗi: Mã OTP không đúng, hết hạn hoặc kết nối thất bại!");
  } finally {
    isSubmitting.value = false;
  }
};
</script>

<style scoped>
/* Style tinh chỉnh gọn gàng */
.form-control:focus,
.form-select:focus {
  border-color: #dc3545;
  box-shadow: 0 0 0 0.25rem rgba(220, 53, 69, 0.25);
}
</style>
