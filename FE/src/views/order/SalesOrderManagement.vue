<template>
  <!-- Đổi thành d-flex flex-column để đẩy Footer xuống đáy màn hình -->
  <div class="bg-light min-vh-100 d-flex flex-column pb-5">
    <!-- 🔥 ĐÃ THÊM HEADER VÀO ĐÂY -->
    <AppHeader />

    <!-- Thêm flex-grow-1 để phần nội dung chiếm hết khoảng trống, đẩy Footer xuống đáy -->
    <div class="container py-5 flex-grow-1" style="margin-top: 130px">
      <!-- Nút Quay lại để điều hướng tốt hơn -->
      <div class="d-flex align-items-center justify-content-between mb-4">
        <nav aria-label="breadcrumb">
          <ol class="breadcrumb mb-0 bg-transparent p-0">
            <li class="breadcrumb-item">
              <router-link to="/" class="text-decoration-none text-muted">
                <i class="bi bi-house-door"></i> Trang chủ
              </router-link>
            </li>
            <li class="breadcrumb-item active text-danger" aria-current="page">
              Đơn hàng khách mua
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
        <!-- 🔥 THÊM SIDEBAR ĐỂ ĐỒNG BỘ VỚI CÁC TRANG KHÁC -->
        <div class="col-md-3 mb-4">
          <UserSidebar activeTab="sales-orders" />
        </div>

        <div class="col-md-9">
          <div class="bg-white rounded shadow-sm p-4 p-md-4">
            <div class="border-bottom pb-3 mb-4">
              <h2
                class="h5 fw-bold mb-1 text-primary d-flex align-items-center gap-2"
              >
                🛍️ Quản lý Đơn Bán
              </h2>
              <p class="text-muted small mb-0">
                Danh sách các đơn hàng khách đã đặt mua sản phẩm của bạn.
              </p>
            </div>

            <div class="table-wrapper">
              <table class="order-table">
                <thead>
                  <tr>
                    <th>Mã Đơn</th>
                    <th>Ngày Đặt</th>
                    <th>Tổng Tiền</th>
                    <th>Trạng Thái Giao</th>
                    <th>Hành Động</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-if="orders.length === 0">
                    <td colspan="5" class="empty-message">
                      Hiện chưa có khách nào đặt mua sản phẩm của bạn.
                    </td>
                  </tr>
                  <tr v-for="order in paginatedOrders" :key="order.donHangId">
                    <td>
                      <strong>O2N-{{ order.donHangId }}</strong>
                    </td>
                    <td>{{ formatDate(order.ngayTao) }}</td>
                    <td class="price">
                      {{ formatCurrency(order.tongThanhTien) }}
                    </td>
                    <td>
                      <span
                        :class="[
                          'badge',
                          getStatusClass(order.trangThaiDonHang),
                        ]"
                      >
                        {{ formatStatus(order.trangThaiDonHang) }}
                      </span>
                    </td>
                    <td>
                      <div class="action-buttons">
                        <button
                          v-if="
                            !order.trangThaiDonHang ||
                            order.trangThaiDonHang === 'CHO_XAC_NHAN'
                          "
                          class="btn-action btn-confirm"
                          @click="
                            updateOrderStatus(order.donHangId, 'DANG_GIAO')
                          "
                        >
                          📦 Xác nhận & Giao Hàng
                        </button>

                        <button
                          v-if="
                            !order.trangThaiDonHang ||
                            order.trangThaiDonHang === 'CHO_XAC_NHAN'
                          "
                          class="btn-action btn-cancel"
                          @click="updateOrderStatus(order.donHangId, 'DA_HUY')"
                        >
                          ❌ Từ chối
                        </button>

                        <span
                          v-if="order.trangThaiDonHang === 'DANG_GIAO'"
                          class="text-success fw-bold"
                          >🚚 Đang vận chuyển</span
                        >
                        <span
                          v-if="order.trangThaiDonHang === 'DA_HUY'"
                          class="text-danger fw-bold"
                          >Đã hủy bỏ</span
                        >

                        <button
                          v-if="
                            [
                              'YEU_CAU_TRA_HANG',
                              'DANG_HOAN_HANG',
                              'DA_HOAN_TIEN',
                            ].includes(order.trangThaiDonHang)
                          "
                          class="btn-action btn-dispute"
                          @click="openDisputeModal(order.donHangId)"
                        >
                          ⚖️ Chi tiết Trả Hàng
                        </button>

                        <span
                          v-if="order.trangThaiDonHang === 'DA_HOAN_TIEN'"
                          class="text-secondary fw-bold"
                        >
                          Đã hoàn tiền cho khách</span
                        >
                      </div>
                    </td>
                  </tr>
                </tbody>
              </table>

              <!-- Phân Trang -->
              <div v-if="totalPages > 1" class="pagination-wrapper">
                <button
                  @click="prevPage"
                  :disabled="currentPage === 1"
                  class="btn-page"
                >
                  « Trước
                </button>

                <button
                  v-for="page in totalPages"
                  :key="page"
                  @click="goToPage(page)"
                  :class="['btn-page', { active: currentPage === page }]"
                >
                  {{ page }}
                </button>

                <button
                  @click="nextPage"
                  :disabled="currentPage === totalPages"
                  class="btn-page"
                >
                  Sau »
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- MODAL THẨM ĐỊNH -->
    <div
      v-if="isDisputeModalOpen"
      class="modal-overlay"
      @click.self="closeDisputeModal"
    >
      <div class="modal-content dispute-modal">
        <h3 class="dispute-title">⚖️ Xử lý Yêu Cầu Trả Hàng</h3>

        <div v-if="isLoadingDispute" class="loading-text">
          ⏳ Đang tải bằng chứng...
        </div>

        <div v-else-if="disputeData" class="dispute-body">
          <p><strong>Mã đơn hàng:</strong> O2N-{{ disputeData.donHangId }}</p>
          <p>
            <strong>Ngày yêu cầu:</strong>
            {{ formatDate(disputeData.ngayYeuCau) }}
          </p>
          <hr />

          <div class="reason-box">
            <p>
              <strong>Lý do:</strong>
              <span class="text-danger">{{ disputeData.lyDo }}</span>
            </p>
            <p>
              <strong>Mô tả chi tiết:</strong>
              {{ disputeData.moTaChiTiet || "Không có" }}
            </p>
          </div>

          <div class="evidence-box">
            <h4>📸 Bằng chứng (Hình ảnh/Video)</h4>
            <div class="media-container">
              <img
                v-if="disputeData.hinhAnhBangChung"
                :src="disputeData.hinhAnhBangChung"
                class="evidence-img"
                alt="Ảnh bằng chứng"
              />
              <video
                v-if="disputeData.videoBangChung"
                :src="disputeData.videoBangChung"
                controls
                class="evidence-video"
              ></video>
            </div>
          </div>

          <div class="dispute-actions">
            <template v-if="disputeData.trangThai === 'CHO_XU_LY'">
              <button class="btn-reject" @click="handleDispute('TU_CHOI')">
                ❌ Từ Chối Trả Hàng
              </button>
              <button class="btn-accept" @click="handleDispute('DONG_Y')">
                ✅ Đồng Ý Nhận Lại Hàng
              </button>
            </template>

            <template v-else-if="disputeData.trangThai === 'DA_DUYET'">
              <button
                class="btn-refund"
                style="width: 100%; padding: 12px; font-size: 16px"
                @click="confirmReturnReceipt(disputeData.donHangId)"
              >
                📦 Đã nhận lại hàng & Hoàn tiền cho khách
              </button>
            </template>

            <div
              v-else
              class="text-center w-100"
              style="
                padding: 10px;
                background: #e9ecef;
                border-radius: 4px;
                color: #495057;
                font-weight: bold;
              "
            >
              🔒 Yêu cầu trả hàng này đã khép lại.
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 🔥 ĐÃ THÊM FOOTER VÀO ĐÂY -->
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";

// 🔥 IMPORT HEADER, SIDEBAR VÀ FOOTER VÀO COMPONENT
import AppHeader from "@/layouts/Header.vue";
import UserSidebar from "@/layouts/UserSidebar.vue";
import AppFooter from "@/layouts/Footer.vue";

const router = useRouter();
const orders = ref([]);
let currentUserId = null;

// Các biến cho Modal Thẩm định
const isDisputeModalOpen = ref(false);
const isLoadingDispute = ref(false);
const disputeData = ref(null);

onMounted(() => {
  const storedUser = localStorage.getItem("user");
  if (!storedUser) {
    alert("Vui lòng đăng nhập để xem đơn bán!");
    router.push("/login");
    return;
  }

  currentUserId =
    JSON.parse(storedUser).id || JSON.parse(storedUser).nguoiDungId;
  fetchSellerOrders();
});

const currentPage = ref(1);
const itemsPerPage = ref(5);

const totalPages = computed(() => {
  return Math.ceil(orders.value.length / itemsPerPage.value);
});

const paginatedOrders = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage.value;
  const end = start + itemsPerPage.value;
  return orders.value.slice(start, end);
});

const goToPage = (page) => {
  currentPage.value = page;
};
const prevPage = () => {
  if (currentPage.value > 1) currentPage.value--;
};
const nextPage = () => {
  if (currentPage.value < totalPages.value) currentPage.value++;
};

const fetchSellerOrders = async () => {
  try {
    const storedUser = JSON.parse(localStorage.getItem("user"));
    const token = storedUser.token || storedUser.accessToken;

    const response = await axios.get(
      `http://localhost:8080/api/don-hang/danh-sach-ban/${currentUserId}`,
      {
        headers: { Authorization: `Bearer ${token}` },
      },
    );
    orders.value = response.data;
  } catch (error) {
    console.error("Lỗi khi tải danh sách đơn bán:", error);
  }
};

const updateOrderStatus = async (donHangId, newStatus) => {
  const confirmMsg =
    newStatus === "DANG_GIAO"
      ? "Xác nhận đã đóng gói và giao cho đơn vị vận chuyển?"
      : "Bạn có chắc chắn muốn từ chối (hủy) đơn hàng này?";

  if (confirm(confirmMsg)) {
    try {
      const storedUser = JSON.parse(localStorage.getItem("user"));
      const token = storedUser.token || storedUser.accessToken;

      await axios.put(
        `http://localhost:8080/api/don-hang/cap-nhat-trang-thai/${donHangId}?trangThaiMoi=${newStatus}`,
        null,
        {
          headers: { Authorization: `Bearer ${token}` },
        },
      );

      alert("Cập nhật trạng thái thành công!");
      fetchSellerOrders();
    } catch (error) {
      console.error("Lỗi cập nhật trạng thái:", error);
      alert("Cập nhật thất bại. Vui lòng thử lại!");
    }
  }
};

const openDisputeModal = async (donHangId) => {
  isDisputeModalOpen.value = true;
  isLoadingDispute.value = true;
  disputeData.value = null;

  try {
    const storedUser = JSON.parse(localStorage.getItem("user"));
    const token = storedUser.token || storedUser.accessToken;

    const response = await axios.get(
      `http://localhost:8080/api/don-hang/yeu-cau-tra-hang/${donHangId}`,
      {
        headers: { Authorization: `Bearer ${token}` },
      },
    );

    disputeData.value = response.data;
  } catch (error) {
    console.error("Lỗi lấy thông tin trả hàng:", error);
    alert("Không thể tải dữ liệu bằng chứng!");
    closeDisputeModal();
  } finally {
    isLoadingDispute.value = false;
  }
};

const closeDisputeModal = () => {
  isDisputeModalOpen.value = false;
};

const handleDispute = async (action) => {
  const msg =
    action === "DONG_Y"
      ? "Xác nhận ĐỒNG Ý cho khách trả hàng? Đơn hàng sẽ chuyển sang chờ bạn nhận lại hàng."
      : "Bạn chắc chắn muốn TỪ CHỐI yêu cầu này? Đơn hàng sẽ được tính là giao thành công.";

  if (confirm(msg)) {
    try {
      const storedUser = JSON.parse(localStorage.getItem("user"));
      const token = storedUser.token || storedUser.accessToken;

      const requestBody = {
        donHangId: disputeData.value.donHangId,
        hanhDong: action,
      };

      await axios.post(
        "http://localhost:8080/api/don-hang/xu-ly-tra-hang",
        requestBody,
        {
          headers: { Authorization: `Bearer ${token}` },
        },
      );

      alert("Đã xử lý yêu cầu thành công!");
      closeDisputeModal();
      fetchSellerOrders();
    } catch (error) {
      console.error("Lỗi xử lý yêu cầu:", error);
      alert("Có lỗi xảy ra, vui lòng thử lại!");
    }
  }
};

const confirmReturnReceipt = async (donHangId) => {
  const msg =
    "Kiểm tra kỹ: Bạn đã nhận lại đúng sản phẩm và nguyên vẹn chưa?\n\nBấm OK, hệ thống sẽ chốt đơn và HOÀN TIỀN lại vào ví của Người Mua!";

  if (confirm(msg)) {
    try {
      const storedUser = JSON.parse(localStorage.getItem("user"));
      const token = storedUser.token || storedUser.accessToken;

      await axios.put(
        `http://localhost:8080/api/don-hang/xac-nhan-hoan-hang/${donHangId}`,
        null,
        {
          headers: { Authorization: `Bearer ${token}` },
        },
      );

      alert("✅ Đã chốt đơn hoàn hàng và hoàn tiền cho khách thành công!");
      fetchSellerOrders();
    } catch (error) {
      console.error("Lỗi xác nhận hoàn hàng:", error);
      alert(
        "Có lỗi xảy ra: " +
          (error.response?.data?.error || "Vui lòng thử lại!"),
      );
    }
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
  return new Date(dateString).toLocaleString("vi-VN");
};

const formatStatus = (status) => {
  const map = {
    CHO_XAC_NHAN: "Chờ Xác Nhận",
    DANG_GIAO: "Đang Giao Hàng",
    DA_GIAO: "Đã Giao Thành Công",
    DA_HUY: "Đã Hủy",
    YEU_CAU_TRA_HANG: "Khách Yêu Cầu Trả",
    DANG_HOAN_HANG: "Đang Trả Hàng",
  };
  return map[status] || status || "Chờ Xác Nhận";
};

const getStatusClass = (status) => {
  if (status === "DANG_GIAO") return "info";
  if (status === "DA_GIAO") return "success";
  if (status === "DA_HUY" || status === "YEU_CAU_TRA_HANG") return "danger";
  if (status === "DANG_HOAN_HANG") return "warning";
  return "warning";
};
</script>

<style scoped>
.table-wrapper {
  overflow-x: auto;
  margin-top: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  border-radius: 8px;
  background: white;
}
.order-table {
  width: 100%;
  border-collapse: collapse;
  text-align: left;
}
.order-table th {
  background-color: #f8f9fa;
  padding: 15px;
  border-bottom: 2px solid #eaeaea;
  color: #555;
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
  font-size: 16px;
}
.badge {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: bold;
  color: white;
  display: inline-block;
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
.badge.danger {
  background-color: #dc3545;
}
.empty-message {
  text-align: center;
  color: #888;
  padding: 30px;
}
.action-buttons {
  display: flex;
  gap: 10px;
  align-items: center;
}
.btn-action {
  padding: 8px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
  transition: 0.2s;
  font-size: 13px;
}
.btn-confirm {
  background-color: #28a745;
  color: white;
}
.btn-confirm:hover {
  background-color: #218838;
  transform: translateY(-1px);
}
.btn-cancel {
  background-color: #f8f9fa;
  color: #dc3545;
  border: 1px solid #dc3545;
}
.btn-cancel:hover {
  background-color: #dc3545;
  color: white;
}

.text-danger {
  color: #dc3545;
}
.text-warning {
  color: #f39c12;
}
.btn-dispute {
  background-color: #dc3545;
  color: white;
  box-shadow: 0 2px 5px rgba(220, 53, 69, 0.3);
  animation: pulse 2s infinite;
}
.btn-dispute:hover {
  background-color: #c82333;
}

@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
  100% {
    transform: scale(1);
  }
}

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
.dispute-modal {
  width: 95%;
  max-width: 600px;
  padding: 30px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.2);
}
.dispute-title {
  margin-top: 0;
  color: #dc3545;
  font-size: 20px;
  border-bottom: 1px solid #eaeaea;
  padding-bottom: 10px;
}
.loading-text {
  text-align: center;
  font-style: italic;
  color: #888;
  padding: 20px;
}

.reason-box {
  background: #fffcf5;
  padding: 15px;
  border-radius: 4px;
  border: 1px dashed #faebd7;
  margin-bottom: 20px;
}
.reason-box p {
  margin: 5px 0;
  font-size: 14px;
}

.evidence-box h4 {
  margin-top: 0;
  font-size: 16px;
  color: #333;
}
.media-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
  align-items: center;
  background: #f8f9fa;
  padding: 15px;
  border-radius: 4px;
}
.evidence-img {
  max-width: 100%;
  max-height: 300px;
  border-radius: 4px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  object-fit: cover;
}
.evidence-video {
  max-width: 100%;
  max-height: 300px;
  border-radius: 4px;
  background: #000;
}

.dispute-actions {
  display: flex;
  justify-content: space-between;
  gap: 10px;
  margin-top: 25px;
  border-top: 1px solid #eaeaea;
  padding-top: 20px;
}
.btn-reject {
  flex: 1;
  padding: 12px;
  background: white;
  color: #dc3545;
  border: 1px solid #dc3545;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
  transition: 0.2s;
}
.btn-reject:hover {
  background: #f8d7da;
}
.btn-accept {
  flex: 1;
  padding: 12px;
  background: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
  transition: 0.2s;
}
.btn-accept:hover {
  background: #218838;
}

.btn-refund {
  background-color: #fd7e14;
  color: white;
  box-shadow: 0 2px 4px rgba(253, 126, 20, 0.3);
}
.btn-refund:hover {
  background-color: #e86e04;
  transform: translateY(-1px);
}
.text-secondary {
  color: #6c757d;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 20px;
  padding: 10px 0;
}

.btn-page {
  padding: 8px 14px;
  border: 1px solid #dee2e6;
  background-color: white;
  color: #007bff;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
  transition: all 0.2s;
}

.btn-page:hover:not(:disabled) {
  background-color: #e9ecef;
}

.btn-page.active {
  background-color: #007bff;
  color: white;
  border-color: #007bff;
}

.btn-page:disabled {
  color: #6c757d;
  background-color: #f8f9fa;
  cursor: not-allowed;
  border-color: #dee2e6;
}
</style>
