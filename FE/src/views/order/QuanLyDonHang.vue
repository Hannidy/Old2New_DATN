<template>
  <div class="bg-light min-vh-100 pb-5">
    <div class="container py-5" style="margin-top: 80px">
      <div class="d-flex align-items-center justify-content-between mb-4">
        <nav aria-label="breadcrumb">
          <ol class="breadcrumb mb-0 bg-transparent p-0">
            <li class="breadcrumb-item">
              <router-link to="/" class="text-decoration-none text-muted"
                ><i class="bi bi-house-door"></i> Trang chủ</router-link
              >
            </li>
            <li class="breadcrumb-item active text-danger" aria-current="page">
              Đơn hàng của tôi
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
          <UserSidebar activeTab="orders" />
        </div>

        <div class="col-md-9">
          <div class="bg-white rounded shadow-sm p-4 p-md-5">
            <div class="border-bottom pb-3 mb-4">
              <h2 class="h4 fw-bold mb-1 text-dark">Quản lý đơn hàng</h2>
              <p class="text-muted small mb-0">
                Theo dõi và quản lý các đơn hàng bạn đã mua
              </p>
            </div>

            <div class="table-wrapper mt-0 shadow-none border">
              <table class="order-table">
                <thead class="bg-light">
                  <tr>
                    <th>Mã Đơn</th>
                    <th>Ngày Đặt</th>
                    <th>Tổng Tiền</th>
                    <th>Thanh Toán</th>
                    <th>Trạng Thái Giao</th>
                    <th>Hành Động</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-if="orders.length === 0">
                    <td colspan="6" class="empty-message text-muted py-5">
                      <div class="fs-1 mb-2">🛍️</div>
                      Bạn chưa có đơn hàng nào.
                    </td>
                  </tr>
                  <tr v-for="order in orders" :key="order.donHangId">
                    <td>
                      <strong>#{{ order.donHangId }}</strong>
                    </td>
                    <td>{{ formatDate(order.ngayTao) }}</td>
                    <td class="price">
                      {{ formatCurrency(order.tongThanhTien) }}
                    </td>
                    <td>
                      <span
                        :class="[
                          'badge',
                          order.trangThaiThanhToan === 'DA_THANH_TOAN'
                            ? 'success'
                            : 'warning',
                        ]"
                      >
                        {{
                          order.trangThaiThanhToan === "DA_THANH_TOAN"
                            ? "Đã thanh toán"
                            : "Chưa thanh toán"
                        }}
                      </span>
                      <br /><small class="text-muted"
                        >({{ order.phuongThucThanhToan }})</small
                      >
                    </td>
                    <td>
                      <span class="badge info">{{
                        order.trangThaiDonHang || "Chờ xác nhận"
                      }}</span>
                    </td>
                    <td>
                      <div class="action-buttons">
                        <button
                          class="btn-view"
                          @click="viewDetails(order.donHangId)"
                        >
                          👁️ Xem Chi Tiết
                        </button>
                        <button
                          v-if="order.trangThaiDonHang === 'DA_GIAO'"
                          class="btn-return"
                          @click="openReturnModal(order)"
                        >
                          🔄 Trả Hàng
                        </button>
                      </div>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="isModalOpen" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content shopee-modal">
        <div class="modal-header">
          <div class="header-left">
            <button class="back-btn" @click="closeModal">❮ TRỞ LẠI</button>
          </div>
          <div class="header-right">
            <span>MÃ ĐƠN HÀNG. O2N{{ selectedOrder?.donHangId }}</span> |
            <span class="status-text">{{
              selectedOrder?.trangThaiDonHang || "CHỜ XÁC NHẬN"
            }}</span>
          </div>
        </div>

        <div class="modal-body" v-if="selectedOrder">
          <div class="stepper-wrapper">
            <div class="stepper-item completed">
              <div class="step-counter">📝</div>
              <div class="step-name">Đơn Hàng Đã Đặt</div>
            </div>
            <div
              :class="[
                'stepper-item',
                selectedOrder.trangThaiThanhToan === 'DA_THANH_TOAN'
                  ? 'completed'
                  : '',
              ]"
            >
              <div class="step-counter">💳</div>
              <div class="step-name">Đã Xác Nhận Thanh Toán</div>
            </div>
            <div
              :class="[
                'stepper-item',
                selectedOrder.trangThaiDonHang === 'DANG_GIAO' ||
                selectedOrder.trangThaiDonHang === 'DA_GIAO'
                  ? 'completed'
                  : '',
              ]"
            >
              <div class="step-counter">🚚</div>
              <div class="step-name">Đã Giao Cho ĐVVC</div>
            </div>
            <div
              :class="[
                'stepper-item',
                selectedOrder.trangThaiDonHang === 'DA_GIAO' ? 'completed' : '',
              ]"
            >
              <div class="step-counter">⭐</div>
              <div class="step-name">Đơn Hàng Đã Hoàn Thành</div>
            </div>
          </div>

          <div class="address-section border border-light-subtle">
            <h3 class="section-title">Địa Chỉ Nhận Hàng</h3>
            <div class="address-content">
              <div class="user-info" v-if="selectedOrder.diaChiGiaoHang">
                <strong>{{
                  selectedOrder.diaChiGiaoHang.split(" | ")[0] || "Khách hàng"
                }}</strong
                ><br />
                <span class="phone-text">{{
                  selectedOrder.diaChiGiaoHang.split(" | ")[1] ||
                  "Chưa cập nhật SĐT"
                }}</span
                ><br />
                <span class="address-text">{{
                  selectedOrder.diaChiGiaoHang.split(" | ")[2] ||
                  selectedOrder.diaChiGiaoHang
                }}</span>
              </div>
            </div>
          </div>

          <div class="product-section border border-light-subtle">
            <table class="product-table">
              <tbody>
                <tr
                  v-for="item in selectedOrder.chiTietDonHangs"
                  :key="item.chiTietId"
                >
                  <td class="product-info">
                    <div class="product-name">{{ item.tenSanPham }}</div>
                    <div class="product-qty">x{{ item.soLuongMua }}</div>
                  </td>
                  <td class="product-price">
                    <span class="sale-price">{{
                      formatCurrency(item.giaLucMua)
                    }}</span>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <div class="summary-section">
            <div class="summary-row">
              <span>Tổng tiền hàng</span>
              <span>{{ formatCurrency(selectedOrder.tongTienHang) }}</span>
            </div>
            <div class="summary-row">
              <span>Phí vận chuyển</span>
              <span>{{ formatCurrency(selectedOrder.tongTienShip) }}</span>
            </div>
            <div class="summary-row total-row">
              <span>Thành tiền</span>
              <span class="final-price">{{
                formatCurrency(selectedOrder.tongThanhTien)
              }}</span>
            </div>
            <div class="payment-method-row">
              <span class="payment-method-text"
                >Phương thức Thanh toán:
                <strong>{{ selectedOrder.phuongThucThanhToan }}</strong></span
              >
            </div>
          </div>
        </div>
      </div>
    </div>

    <div
      v-if="isReturnModalOpen"
      class="modal-overlay"
      @click.self="closeReturnModal"
    >
      <div class="modal-content return-modal">
        <h3 class="return-title">Yêu Cầu Trả Hàng / Hoàn Tiền</h3>
        <p class="return-subtitle">
          Mã đơn: <strong>#{{ orderToReturn?.donHangId }}</strong>
        </p>

        <form @submit.prevent="submitReturnRequest" class="return-form">
          <div class="form-group">
            <label>Lý do trả hàng <span class="text-danger">*</span></label>
            <select v-model="returnForm.lyDo" required class="form-control">
              <option value="" disabled>-- Chọn lý do --</option>
              <option value="Hàng bị lỗi / Không hoạt động">
                Hàng bị lỗi / Không hoạt động
              </option>
              <option value="Giao sai sản phẩm / Phân loại">
                Giao sai sản phẩm / Phân loại
              </option>
              <option value="Hàng bị bể vỡ do vận chuyển">
                Hàng bị bể vỡ do vận chuyển
              </option>
              <option value="Hàng khác với mô tả">Hàng khác với mô tả</option>
            </select>
          </div>

          <div class="form-group">
            <label>Mô tả chi tiết tình trạng</label>
            <textarea
              v-model="returnForm.moTaChiTiet"
              rows="3"
              placeholder="Ví dụ: Áo bị rách ở phần tay, màn hình bật không lên..."
              class="form-control"
            ></textarea>
          </div>

          <div class="form-group">
            <label>Hình ảnh chứng cứ <span class="text-danger">*</span></label>
            <input
              type="file"
              @change="handleFileUpload($event, 'image')"
              accept="image/*"
              class="form-control"
              :disabled="isUploadingImage"
              required
            />
            <div v-if="isUploadingImage" class="upload-status">
              ⏳ Đang tải ảnh lên mây...
            </div>
            <img
              v-if="returnForm.hinhAnhBangChung"
              :src="returnForm.hinhAnhBangChung"
              class="preview-img"
              alt="Bằng chứng"
            />
          </div>

          <div class="form-group">
            <label>Video chứng cứ (Tùy chọn)</label>
            <input
              type="file"
              @change="handleFileUpload($event, 'video')"
              accept="video/*"
              class="form-control"
              :disabled="isUploadingVideo"
            />
            <div v-if="isUploadingVideo" class="upload-status">
              ⏳ Đang tải video lên mây...
            </div>
            <video
              v-if="returnForm.videoBangChung"
              :src="returnForm.videoBangChung"
              controls
              class="preview-video"
            ></video>
          </div>

          <div class="return-actions">
            <button type="button" class="btn-cancel" @click="closeReturnModal">
              Hủy
            </button>
            <button
              type="submit"
              class="btn-submit"
              :disabled="isUploadingImage || isUploadingVideo"
            >
              Gửi Yêu Cầu
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import UserSidebar from "@/layouts/UserSidebar.vue";

const router = useRouter();
const orders = ref([]);
const selectedOrder = ref(null);
const isModalOpen = ref(false);

const isReturnModalOpen = ref(false);
const orderToReturn = ref(null);
const returnForm = ref({
  lyDo: "",
  moTaChiTiet: "",
  hinhAnhBangChung: "",
  videoBangChung: "",
});
const isUploadingImage = ref(false);
const isUploadingVideo = ref(false);

const handleFileUpload = async (event, type) => {
  const file = event.target.files[0];
  if (!file) return;

  const formData = new FormData();
  formData.append("file", file);

  try {
    if (type === "image") isUploadingImage.value = true;
    if (type === "video") isUploadingVideo.value = true;

    const storedUserData = JSON.parse(localStorage.getItem("user"));
    const token = storedUserData.token || storedUserData.accessToken;

    const apiUrl =
      type === "image"
        ? "http://localhost:8080/api/media/upload-image"
        : "http://localhost:8080/api/media/upload-video";

    const response = await axios.post(apiUrl, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
        Authorization: `Bearer ${token}`,
      },
    });

    if (type === "image") returnForm.value.hinhAnhBangChung = response.data.url;
    else returnForm.value.videoBangChung = response.data.url;
  } catch (error) {
    console.error("Lỗi upload file:", error);
    alert("Upload thất bại! Vui lòng kiểm tra lại mạng hoặc dung lượng file.");
  } finally {
    if (type === "image") isUploadingImage.value = false;
    if (type === "video") isUploadingVideo.value = false;
  }
};

let userId = null;
const storedUser = localStorage.getItem("user");

if (storedUser) {
  const userData = JSON.parse(storedUser);
  userId = userData.nguoiDungId || userData.id;
}

const fetchOrders = async () => {
  if (!userId) {
    router.push("/login");
    return;
  }
  try {
    const response = await axios.get(
      `http://localhost:8080/api/don-hang/danh-sach/${userId}`,
    );
    orders.value = response.data;
  } catch (error) {
    console.error("Lỗi khi tải danh sách đơn hàng:", error);
  }
};

const viewDetails = async (donHangId) => {
  try {
    const response = await axios.get(
      `http://localhost:8080/api/don-hang/chi-tiet/${donHangId}`,
    );
    selectedOrder.value = response.data;
    isModalOpen.value = true;
  } catch (error) {
    console.error("Lỗi khi tải chi tiết đơn hàng:", error);
  }
};

const closeModal = () => {
  isModalOpen.value = false;
  selectedOrder.value = null;
};

const openReturnModal = (order) => {
  orderToReturn.value = order;
  returnForm.value = {
    lyDo: "",
    moTaChiTiet: "",
    hinhAnhBangChung: "",
    videoBangChung: "",
  };
  isReturnModalOpen.value = true;
};

const closeReturnModal = () => {
  isReturnModalOpen.value = false;
  orderToReturn.value = null;
};

const submitReturnRequest = async () => {
  if (!returnForm.value.lyDo) return alert("Vui lòng chọn lý do trả hàng!");

  const requestData = {
    donHangId: orderToReturn.value.donHangId,
    lyDo: returnForm.value.lyDo,
    moTaChiTiet: returnForm.value.moTaChiTiet,
    hinhAnhBangChung: returnForm.value.hinhAnhBangChung,
    videoBangChung: returnForm.value.videoBangChung,
  };

  try {
    const storedUserData = JSON.parse(localStorage.getItem("user"));
    const token = storedUserData.token || storedUserData.accessToken;

    const response = await axios.post(
      "http://localhost:8080/api/don-hang/yeu-cau-tra-hang",
      requestData,
      {
        headers: { Authorization: `Bearer ${token}` },
      },
    );

    alert(response.data.message || "Đã gửi yêu cầu trả hàng thành công!");
    closeReturnModal();
    fetchOrders();
  } catch (error) {
    alert(
      "Có lỗi xảy ra: " +
        (error.response?.data?.error || "Vui lòng thử lại sau!"),
    );
  }
};

const formatCurrency = (value) => {
  if (!value) return "0 ₫";
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  }).format(value);
};

const formatDate = (dateString) => {
  if (!dateString) return "";
  const date = new Date(dateString);
  return date.toLocaleString("vi-VN");
};

onMounted(() => fetchOrders());
</script>

<style scoped>
.table-wrapper {
  overflow-x: auto;
  border-radius: 8px;
  background: white;
}
.order-table {
  width: 100%;
  border-collapse: collapse;
  text-align: left;
}
.order-table th {
  padding: 15px;
  border-bottom: 2px solid #eaeaea;
  color: #555;
  text-transform: uppercase;
  font-size: 0.85rem;
}
.order-table td {
  padding: 15px;
  border-bottom: 1px solid #eaeaea;
  vertical-align: middle;
  color: #333;
}
.price {
  color: #ee4d2d;
  font-weight: bold;
}
.badge {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: bold;
  color: white;
  display: inline-block;
  text-align: center;
}
.badge.success {
  background-color: #28a745;
}
.badge.warning {
  background-color: #ffc107;
  color: #333;
}
.badge.info {
  background-color: #17a2b8;
}
.action-buttons {
  display: flex;
  gap: 8px;
  align-items: center;
}
.btn-view {
  background-color: #ee4d2d;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  transition: 0.2s;
  font-weight: bold;
  font-size: 0.9rem;
}
.btn-view:hover {
  background-color: #d73a27;
}

/* Modal chung */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  backdrop-filter: blur(2px);
}
.modal-content {
  max-height: 90vh;
  overflow-y: auto;
}

/* CSS Modal Shopee */
.shopee-modal {
  width: 95%;
  max-width: 850px;
  padding: 0;
  background: #f5f5f5;
  border-radius: 8px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.2);
}
.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 18px 25px;
  background: white;
  border-bottom: 1px solid #eaeaea;
  position: sticky;
  top: 0;
  z-index: 10;
}
.back-btn {
  background: none;
  border: none;
  font-weight: bold;
  color: #ee4d2d;
  cursor: pointer;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 5px;
}
.back-btn:hover {
  opacity: 0.8;
}
.status-text {
  color: #ee4d2d;
  font-weight: bold;
  text-transform: uppercase;
  font-size: 15px;
}
.modal-body {
  padding: 25px;
  overflow-y: auto;
}
.stepper-wrapper {
  display: flex;
  justify-content: space-between;
  margin-bottom: 25px;
  background: white;
  padding: 35px 20px;
  border-radius: 4px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  position: relative;
}
.stepper-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;
  position: relative;
  z-index: 1;
}
.stepper-item::before {
  position: absolute;
  content: "";
  border-bottom: 4px solid #e0e0e0;
  width: 100%;
  top: 20px;
  left: -50%;
  z-index: -1;
}
.stepper-item:first-child::before {
  content: none;
}
.step-counter {
  width: 45px;
  height: 45px;
  background: #e0e0e0;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  color: white;
  font-size: 20px;
  margin-bottom: 12px;
  border: 4px solid white;
  box-shadow: 0 0 0 2px #e0e0e0;
}
.stepper-item.completed .step-counter {
  background: #26aa99;
  box-shadow: 0 0 0 2px #26aa99;
}
.stepper-item.completed::before {
  border-color: #26aa99;
}
.step-name {
  font-size: 13px;
  color: #888;
  text-align: center;
  font-weight: 500;
}
.stepper-item.completed .step-name {
  color: #26aa99;
  font-weight: bold;
}
.address-section {
  background: white;
  padding: 25px;
  border-radius: 4px;
  margin-bottom: 20px;
}
.section-title {
  margin-top: 0;
  font-size: 18px;
  color: #333;
  border-bottom: 1px solid #eaeaea;
  padding-bottom: 12px;
  margin-bottom: 15px;
  font-weight: 500;
}
.user-info strong {
  font-size: 18px;
  color: #333;
  margin-bottom: 8px;
  display: inline-block;
}
.phone-text {
  color: #888;
  font-size: 14px;
  margin-bottom: 8px;
  display: inline-block;
}
.address-text {
  color: #555;
  font-size: 15px;
  line-height: 1.6;
  display: block;
  margin-top: 5px;
}
.product-section {
  background: white;
  padding: 25px;
  border-radius: 4px;
  margin-bottom: 20px;
}
.product-table {
  width: 100%;
  border-collapse: collapse;
}
.product-info {
  padding: 20px 0;
  border-bottom: 1px dashed #eaeaea;
  text-align: left;
}
.product-name {
  font-size: 16px;
  color: #333;
  margin-bottom: 10px;
  font-weight: 500;
}
.product-qty {
  font-size: 14px;
  color: #888;
}
.product-price {
  text-align: right;
  vertical-align: middle;
  border-bottom: 1px dashed #eaeaea;
  padding: 20px 0;
}
.sale-price {
  color: #ee4d2d;
  font-weight: bold;
  font-size: 16px;
}
.summary-section {
  background: #fffcf5;
  padding: 25px;
  border-radius: 4px;
  border: 1px solid #faebd7;
}
.summary-row {
  display: flex;
  justify-content: flex-end;
  padding: 10px 0;
  font-size: 15px;
  color: #555;
}
.summary-row span:last-child {
  width: 200px;
  text-align: right;
}
.total-row {
  border-top: 1px dashed #eaeaea;
  margin-top: 15px;
  padding-top: 20px;
  align-items: center;
}
.final-price {
  color: #ee4d2d;
  font-size: 28px;
  font-weight: bold;
}
.payment-method-row {
  border-top: 1px solid #eaeaea;
  margin-top: 20px;
  padding-top: 20px;
  text-align: right;
  font-size: 14px;
  color: #555;
}
.payment-method-row strong {
  color: #333;
  font-size: 15px;
  margin-left: 5px;
}

.btn-return {
  background-color: #26aa99;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  transition: 0.2s;
  font-weight: bold;
  font-size: 0.9rem;
}
.btn-return:hover {
  background-color: #208d7f;
}

.return-modal {
  width: 95%;
  max-width: 500px;
  padding: 30px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.2);
}
.return-title {
  margin-top: 0;
  color: #ee4d2d;
  font-size: 20px;
  border-bottom: 1px solid #eaeaea;
  padding-bottom: 10px;
}
.return-subtitle {
  font-size: 14px;
  color: #555;
  margin-bottom: 20px;
}
.form-group {
  margin-bottom: 15px;
  text-align: left;
}
.form-group label {
  display: block;
  font-weight: 500;
  margin-bottom: 8px;
  color: #333;
  font-size: 14px;
}
.form-control {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
  font-family: inherit;
  box-sizing: border-box;
}
.form-control:focus {
  border-color: #ee4d2d;
  outline: none;
}
.return-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 25px;
}
.btn-cancel {
  padding: 10px 20px;
  background: #f5f5f5;
  color: #333;
  border: 1px solid #ccc;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
}
.btn-submit {
  padding: 10px 20px;
  background: #ee4d2d;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
}
.btn-submit:hover {
  background: #d73a27;
}
.upload-status {
  font-size: 13px;
  color: #ee4d2d;
  margin-top: 5px;
  font-weight: bold;
  font-style: italic;
}
.preview-img,
.preview-video {
  max-width: 100%;
  max-height: 200px;
  margin-top: 10px;
  border-radius: 4px;
  border: 1px solid #eaeaea;
}
</style>
