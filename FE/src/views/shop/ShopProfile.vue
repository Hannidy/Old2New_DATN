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
            Hồ sơ cửa hàng
          </li>
        </ol>
      </nav>
      <button
        @click="router.push('/')"
        class="btn btn-sm btn-outline-secondary rounded-pill px-3 shadow-sm"
      >
        <i class="bi bi-arrow-left"></i> Quay lại trang chủ
      </button>
    </div>

    <div class="row">
      <div class="col-md-3 mb-4">
        <UserSidebar activeTab="shop" />
      </div>

      <div class="col-md-9">
        <div class="bg-white rounded shadow-sm p-4 p-md-5">
          <h2 class="h4 fw-bold mb-1 text-dark">Hồ sơ Cửa hàng</h2>
          <p class="text-muted small mb-4 pb-3 border-bottom">
            Quản lý thông tin và thương hiệu cửa hàng của bạn
          </p>

          <div v-if="loading" class="text-center py-5">
            <div class="spinner-border text-primary" role="status"></div>
          </div>

          <div v-else>
            <div v-if="shopData.trangThaiCuaHang !== undefined" class="mb-4">
              <div
                v-if="shopData.trangThaiCuaHang === 1"
                class="alert alert-success d-flex align-items-center border-0 shadow-sm"
              >
                <i class="bi bi-check-circle-fill fs-4 me-3 text-success"></i>
                <div>
                  <h6 class="fw-bold mb-1">Cửa hàng đang hoạt động tốt</h6>
                  <span class="small"
                    >Sản phẩm của bạn đang được hiển thị bình thường trên hệ
                    thống.</span
                  >
                </div>
              </div>
              <div
                v-else
                class="alert alert-danger d-flex align-items-center border-0 shadow-sm"
              >
                <i
                  class="bi bi-exclamation-triangle-fill fs-4 me-3 text-danger"
                ></i>
                <div>
                  <h6 class="fw-bold mb-1">
                    Cửa hàng đang bị tạm khóa hoặc hạn chế!
                  </h6>
                  <span class="small fw-medium">Lý do vi phạm: </span>
                  <span class="small">{{
                    shopData.moTaViPham ||
                    "Vui lòng liên hệ Admin để biết thêm chi tiết."
                  }}</span>
                </div>
              </div>
            </div>

            <form @submit.prevent="updateShopInfo" class="profile-form">
              <div class="row mb-4 align-items-center">
                <div class="col-md-3 text-md-end text-muted fw-medium small">
                  Ảnh đại diện
                </div>
                <div class="col-md-8">
                  <div class="position-relative d-inline-block">
                    <img
                      :src="
                        shopData.anhDaiDien || 'https://via.placeholder.com/150'
                      "
                      class="rounded-circle border shadow-sm object-fit-cover"
                      style="width: 100px; height: 100px"
                    />
                    <label
                      class="position-absolute bottom-0 end-0 bg-white rounded-circle shadow-sm border p-1"
                      style="cursor: pointer"
                    >
                      <i
                        class="bi bi-camera-fill text-primary"
                        style="font-size: 0.9rem"
                      ></i>
                      <input
                        type="file"
                        class="d-none"
                        @change="onFileChange"
                        accept="image/*"
                      />
                    </label>
                  </div>
                </div>
              </div>

              <div class="row mb-4 align-items-center">
                <div class="col-md-3 text-md-end text-muted fw-medium small">
                  Tên cửa hàng <span class="text-danger">*</span>
                </div>
                <div class="col-md-8">
                  <input
                    type="text"
                    v-model="shopData.tenShop"
                    class="form-control"
                    required
                  />
                </div>
              </div>

              <div class="row mb-4 align-items-start">
                <div
                  class="col-md-3 text-md-end text-muted fw-medium small mt-2"
                >
                  Giới thiệu
                </div>
                <div class="col-md-8">
                  <textarea
                    v-model="shopData.gioiThieu"
                    class="form-control"
                    rows="4"
                  ></textarea>
                </div>
              </div>

              <div class="row mb-4 align-items-start">
                <div
                  class="col-md-3 text-md-end text-muted fw-medium small mt-2"
                >
                  Kho lấy hàng
                </div>
                <div class="col-md-8">
                  <div
                    class="p-3 bg-light border rounded-3 border-secondary border-opacity-25"
                  >
                    <div
                      class="d-flex justify-content-between align-items-center mb-2"
                    >
                      <label class="fw-bold mb-0 text-dark small">
                        <i class="bi bi-geo-alt-fill text-danger me-1"></i> Kho
                        mặc định
                      </label>
                      <button
                        type="button"
                        class="btn btn-sm btn-outline-primary fw-bold"
                        @click="showAddressManagerModal = true"
                      >
                        Cập nhật chi tiết
                      </button>
                    </div>

                    <div v-if="defaultAddress" class="mt-2 text-dark">
                      <span class="badge bg-danger mb-1">Mặc định</span>
                      <p class="mb-0 fw-medium small text-secondary">
                        {{ defaultAddress.diaChiChiTiet }}
                      </p>
                    </div>
                    <div v-else class="mt-2 text-muted fst-italic small">
                      Chưa thiết lập kho hàng.
                    </div>
                  </div>
                </div>
              </div>

              <div class="row mt-5">
                <div class="col-md-3"></div>
                <div class="col-md-8 d-flex gap-3">
                  <button
                    type="submit"
                    class="btn btn-dark px-4 py-2 fw-bold shadow-sm"
                    :disabled="saving || shopData.trangThaiCuaHang === 0"
                  >
                    <span
                      v-if="saving"
                      class="spinner-border spinner-border-sm me-2"
                    ></span>
                    {{ saving ? "Đang lưu..." : "LƯU THAY ĐỔI" }}
                  </button>
                  <button
                    type="button"
                    class="btn btn-outline-secondary px-4 py-2 fw-bold"
                    @click="router.push('/')"
                  >
                    🏠 QUAY LẠI TRANG CHỦ
                  </button>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>

    <div
      v-if="showAddressManagerModal"
      class="modal fade show d-block"
      style="background: rgba(0, 0, 0, 0.5); z-index: 1050"
    >
      <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content border-0 rounded-4 shadow">
          <div class="modal-header bg-light border-bottom-0">
            <h5 class="modal-title fw-bold">Sổ địa chỉ kho hàng</h5>
            <button
              type="button"
              class="btn-close"
              @click="showAddressManagerModal = false"
            ></button>
          </div>
          <div class="modal-body p-4">
            <button
              class="btn btn-primary fw-bold mb-4"
              @click="openAddressModal"
            >
              <i class="bi bi-plus-lg me-1"></i> Thêm địa chỉ kho mới
            </button>
            <div
              v-if="shopAddresses.length === 0"
              class="text-center text-muted py-3"
            >
              Bạn chưa có địa chỉ kho nào.
            </div>
            <div class="list-group">
              <div
                v-for="addr in shopAddresses"
                :key="addr.id"
                class="list-group-item list-group-item-action d-flex justify-content-between align-items-center p-3"
              >
                <div class="pe-3">
                  <div class="fw-bold text-dark mb-1">
                    <span
                      v-if="addr.isDefault === 1"
                      class="badge bg-danger me-2"
                      >Mặc định</span
                    >
                  </div>
                  <p class="mb-0 text-secondary small">
                    {{ addr.diaChiChiTiet }}
                  </p>
                </div>

                <div
                  class="d-flex flex-column gap-2"
                  style="min-width: 140px; text-align: right"
                >
                  <button
                    v-if="addr.isDefault !== 1"
                    class="btn btn-sm btn-outline-success fw-bold"
                    @click="setDefault(addr.id)"
                  >
                    Đặt mặc định
                  </button>
                  <div class="d-flex gap-2 justify-content-end">
                    <button
                      class="btn btn-sm btn-outline-primary flex-grow-1"
                      @click="editAddress(addr)"
                    >
                      Sửa
                    </button>
                    <button
                      class="btn btn-sm btn-outline-danger flex-grow-1"
                      @click="deleteAddress(addr.id)"
                    >
                      Xóa
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div
      v-if="showAddressModal"
      class="modal fade show d-block"
      style="background: rgba(0, 0, 0, 0.6); z-index: 1060"
    >
      <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content border-0 rounded-4 shadow-lg">
          <div class="modal-header bg-primary text-white border-bottom-0">
            <h5 class="modal-title fw-bold">
              {{
                isEditing
                  ? "✏️ Cập nhật kho lấy hàng"
                  : "📍 Thêm kho lấy hàng mới"
              }}
            </h5>
            <button
              type="button"
              class="btn-close btn-close-white"
              @click="showAddressModal = false"
            ></button>
          </div>
          <form @submit.prevent="saveShopAddress">
            <div class="modal-body p-4 bg-light">
              <div class="row mb-3">
                <div class="col-md-4">
                  <label class="form-label small fw-bold"
                    >Tỉnh / Thành phố <span class="text-danger">*</span></label
                  >
                  <select
                    class="form-select border-secondary"
                    v-model="addressForm.province"
                    @change="fetchDistricts"
                    required
                  >
                    <option value="" disabled>Chọn Tỉnh / Thành</option>
                    <option
                      v-for="p in provincesList"
                      :key="p.ProvinceID"
                      :value="p"
                    >
                      {{ p.ProvinceName }}
                    </option>
                  </select>
                </div>
                <div class="col-md-4">
                  <label class="form-label small fw-bold"
                    >Quận / Huyện <span class="text-danger">*</span></label
                  >
                  <select
                    class="form-select border-secondary"
                    v-model="addressForm.district"
                    @change="fetchWards"
                    :disabled="!addressForm.province"
                    required
                  >
                    <option value="" disabled>Chọn Quận / Huyện</option>
                    <option
                      v-for="d in districtsList"
                      :key="d.DistrictID"
                      :value="d"
                    >
                      {{ d.DistrictName }}
                    </option>
                  </select>
                </div>
                <div class="col-md-4">
                  <label class="form-label small fw-bold"
                    >Phường / Xã <span class="text-danger">*</span></label
                  >
                  <select
                    class="form-select border-secondary"
                    v-model="addressForm.ward"
                    :disabled="!addressForm.district"
                    required
                  >
                    <option value="" disabled>Chọn Phường / Xã</option>
                    <option v-for="w in wardsList" :key="w.WardCode" :value="w">
                      {{ w.WardName }}
                    </option>
                  </select>
                </div>
              </div>

              <div class="mb-3">
                <label class="form-label small fw-bold"
                  >Số nhà, tên đường cụ thể
                  <span class="text-danger">*</span></label
                >
                <textarea
                  class="form-control border-secondary"
                  v-model="addressForm.detail"
                  rows="2"
                  placeholder="Ví dụ: 123 Đường Quang Trung..."
                  required
                ></textarea>
              </div>
            </div>

            <div class="modal-footer border-top bg-white">
              <button
                type="button"
                class="btn btn-outline-secondary px-4"
                @click="showAddressModal = false"
              >
                Hủy
              </button>
              <button
                type="submit"
                class="btn btn-primary px-4 fw-bold shadow-sm"
                :disabled="isSavingAddr"
              >
                <span
                  v-if="isSavingAddr"
                  class="spinner-border spinner-border-sm me-2"
                ></span>
                Lưu Địa Chỉ
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";
import UserSidebar from "@/layouts/UserSidebar.vue";

const router = useRouter();
const currentUser = ref({});
const shopData = ref({
  id: null,
  tenShop: "",
  anhDaiDien: "",
  gioiThieu: "",
  trangThaiCuaHang: 1,
  moTaViPham: "",
});
const shopAddresses = ref([]);
const loading = ref(true);
const saving = ref(false);

const showAddressManagerModal = ref(false);
const showAddressModal = ref(false);

const isEditing = ref(false);
const editingId = ref(null);

const defaultAddress = computed(() => {
  return shopAddresses.value.find((addr) => addr.isDefault === 1) || null;
});

const GHN_TOKEN =
  import.meta.env.VITE_GHN_TOKEN || "d7acb48b-030e-11f1-a3d6-dac90fb956b5";
const GHN_BASE_URL =
  import.meta.env.VITE_GHN_URL ||
  "https://online-gateway.ghn.vn/shiip/public-api";
const provincesList = ref([]);
const districtsList = ref([]);
const wardsList = ref([]);
const isSavingAddr = ref(false);
const addressForm = reactive({
  province: "",
  district: "",
  ward: "",
  detail: "",
});

const loadShopProfile = async () => {
  const userStr = localStorage.getItem("user");
  if (!userStr) {
    router.push("/login");
    return;
  }
  currentUser.value = JSON.parse(userStr);
  const userId = currentUser.value.id || currentUser.value.nguoiDungId;
  const token = currentUser.value.token || currentUser.value.accessToken;

  try {
    const res = await axios.get(
      `http://localhost:8080/api/shops/my-shop/${userId}`,
      {
        headers: { Authorization: `Bearer ${token}` },
      },
    );
    shopData.value = res.data;

    const addrRes = await axios.get(
      `http://localhost:8080/api/addresses/shop/${userId}`,
      {
        headers: { Authorization: `Bearer ${token}` },
      },
    );
    shopAddresses.value = addrRes.data || [];
  } catch (e) {
    console.error(e);
  } finally {
    loading.value = false;
  }
};

// 🔥 HÀM XỬ LÝ UPLOAD ẢNH CỬA HÀNG
const onFileChange = async (event) => {
  const file = event.target.files[0];
  if (!file) return;

  // Hiển thị tạm ảnh vừa chọn lên giao diện
  shopData.value.anhDaiDien = URL.createObjectURL(file);

  const formData = new FormData();
  formData.append("file", file);

  try {
    const token = currentUser.value.token || currentUser.value.accessToken;

    // Gọi API upload chung của hệ thống
    const uploadRes = await axios.post(
      "http://localhost:8080/api/media/upload-image",
      formData,
      {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: `Bearer ${token}`,
        },
      },
    );

    // Lấy URL thực tế từ Cloudinary
    shopData.value.anhDaiDien = uploadRes.data.url;

    // Tự động gọi hàm lưu luôn để tiện lợi
    await updateShopInfo();
  } catch (error) {
    console.error("Lỗi upload ảnh cửa hàng:", error);
    alert("❌ Lỗi khi tải ảnh lên. Vui lòng thử lại!");
  }
};

const updateShopInfo = async () => {
  saving.value = true;
  const token = currentUser.value.token || currentUser.value.accessToken;
  try {
    await axios.put(
      `http://localhost:8080/api/shops/my-shop/${currentUser.value.id || currentUser.value.nguoiDungId}`,
      shopData.value,
      {
        headers: { Authorization: `Bearer ${token}` },
      },
    );
    alert("✅ Cập nhật hồ sơ cửa hàng thành công!");
  } catch (e) {
    alert("❌ Lỗi cập nhật!");
  } finally {
    saving.value = false;
  }
};

// --- QUẢN LÝ KHO HÀNG ---
const openAddressModal = () => {
  isEditing.value = false;
  editingId.value = null;
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
  try {
    const res = await axios.get(`${GHN_BASE_URL}/master-data/province`, {
      headers: { Token: GHN_TOKEN },
    });
    provincesList.value = res.data.data.filter(
      (p) => !p.ProvinceName.toLowerCase().includes("test"),
    );
  } catch (e) {
    console.error("Lỗi lấy Tỉnh:", e);
  }
};

const fetchDistricts = async () => {
  addressForm.district = "";
  addressForm.ward = "";
  wardsList.value = [];
  try {
    const res = await axios.get(`${GHN_BASE_URL}/master-data/district`, {
      headers: { Token: GHN_TOKEN },
      params: { province_id: addressForm.province.ProvinceID },
    });
    districtsList.value = res.data.data.filter(
      (d) => !d.DistrictName.toLowerCase().includes("test"),
    );
  } catch (e) {
    console.error("Lỗi lấy Huyện:", e);
  }
};

const fetchWards = async () => {
  addressForm.ward = "";
  try {
    const res = await axios.get(`${GHN_BASE_URL}/master-data/ward`, {
      headers: { Token: GHN_TOKEN },
      params: { district_id: addressForm.district.DistrictID },
    });
    wardsList.value = res.data.data;
  } catch (e) {
    console.error("Lỗi lấy Xã:", e);
  }
};

const editAddress = async (addr) => {
  isEditing.value = true;
  editingId.value = addr.id;

  const parts = addr.diaChiChiTiet.split(", ");
  const len = parts.length;
  if (len >= 3) {
    addressForm.detail = parts
      .slice(0, len - 3)
      .join(", ")
      .trim();
  } else {
    addressForm.detail = addr.diaChiChiTiet;
  }

  const tenTinhStr = parts[len - 1]?.trim();

  if (provincesList.value.length === 0) {
    await fetchProvinces();
  }

  const matchedProv = provincesList.value.find(
    (p) => p.ProvinceName === tenTinhStr,
  );
  if (matchedProv) {
    addressForm.province = matchedProv;

    try {
      const resD = await axios.get(`${GHN_BASE_URL}/master-data/district`, {
        headers: { Token: GHN_TOKEN },
        params: { province_id: matchedProv.ProvinceID },
      });
      districtsList.value = resD.data.data.filter(
        (d) => !d.DistrictName.toLowerCase().includes("test"),
      );

      const matchedDist = districtsList.value.find(
        (d) => d.DistrictID === addr.huyenCode,
      );
      addressForm.district = matchedDist || "";

      if (matchedDist) {
        const resW = await axios.get(`${GHN_BASE_URL}/master-data/ward`, {
          headers: { Token: GHN_TOKEN },
          params: { district_id: matchedDist.DistrictID },
        });
        wardsList.value = resW.data.data;

        const matchedWard = wardsList.value.find(
          (w) => w.WardCode === String(addr.phuongXaId),
        );
        addressForm.ward = matchedWard || "";
      }
    } catch (error) {
      console.error("Lỗi khi auto-load dữ liệu GHN:", error);
    }
  }

  showAddressModal.value = true;
};

const saveShopAddress = async () => {
  isSavingAddr.value = true;
  const token = currentUser.value.token || currentUser.value.accessToken;
  const fullAddr = `${addressForm.detail}, ${addressForm.ward.WardName}, ${addressForm.district.DistrictName}, ${addressForm.province.ProvinceName}`;

  const payload = {
    huyenCode: addressForm.district.DistrictID,
    phuongXaId: addressForm.ward.WardCode,
    diaChiChiTiet: fullAddr,
  };

  try {
    if (isEditing.value) {
      await axios.put(
        `http://localhost:8080/api/addresses/shop/${editingId.value}`,
        payload,
        { headers: { Authorization: `Bearer ${token}` } },
      );
      alert("✅ Cập nhật kho thành công!");
    } else {
      await axios.post(
        `http://localhost:8080/api/addresses/shop/${currentUser.value.id || currentUser.value.nguoiDungId}`,
        payload,
        { headers: { Authorization: `Bearer ${token}` } },
      );
      alert("✅ Thêm kho thành công!");
    }

    showAddressModal.value = false;
    loadShopProfile();
  } catch (e) {
    console.error(e);
    alert("❌ Lỗi lưu địa chỉ!");
  } finally {
    isSavingAddr.value = false;
  }
};

const setDefault = async (diaChiId) => {
  const token = currentUser.value.token || currentUser.value.accessToken;
  try {
    await axios.put(
      `http://localhost:8080/api/addresses/shop/${shopData.value.id}/default/${diaChiId}`,
      {},
      {
        headers: { Authorization: `Bearer ${token}` },
      },
    );
    loadShopProfile();
  } catch (e) {
    alert("❌ Lỗi đặt mặc định!");
  }
};

const deleteAddress = async (diaChiId) => {
  if (!confirm("Bạn có chắc chắn muốn xóa kho hàng này không?")) return;
  const token = currentUser.value.token || currentUser.value.accessToken;
  try {
    await axios.delete(`http://localhost:8080/api/addresses/shop/${diaChiId}`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    loadShopProfile();
  } catch (e) {
    alert("❌ Lỗi khi xóa kho!");
  }
};

onMounted(loadShopProfile);
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
.avatar-circle {
  width: 50px;
  height: 50px;
  border-radius: 50%;
}
.menu-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.menu-item {
  padding: 10px 15px;
  border-radius: 6px;
  cursor: pointer;
  color: #555;
  transition: all 0.2s ease;
  font-weight: 500;
}
.menu-item .icon {
  margin-right: 8px;
}
.menu-item:hover {
  background-color: #f8f9fa;
  color: #007bff;
}
.menu-item.active {
  color: #ee4d2d;
  font-weight: bold;
  background-color: #fff5f5;
  border-left: 4px solid #ee4d2d;
}
.cursor-pointer {
  cursor: pointer;
}
</style>
