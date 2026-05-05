<template>
  <div class="bg-light min-vh-100 pb-5">
    <div class="container py-5" style="margin-top: 80px">
      <div class="d-flex align-items-center justify-content-between mb-4">
        <nav aria-label="breadcrumb">
          <ol class="breadcrumb mb-0 bg-transparent p-0">
            <li class="breadcrumb-item">
              <router-link to="/" class="text-decoration-none text-muted"><i class="bi bi-house-door"></i> Trang
                chủ</router-link>
            </li>
            <li class="breadcrumb-item active text-danger" aria-current="page">
              Đơn hàng của tôi
            </li>
          </ol>
        </nav>
        <button @click="router.push('/')" class="btn btn-sm btn-outline-secondary rounded-pill px-3 shadow-sm">
          <i class="bi bi-arrow-left"></i> Quay lại trang chủ
        </button>
      </div>

      <div class="row">
        <div class="col-md-3 mb-4">
          <UserSidebar activeTab="orders" />
        </div>

        <div class="col-md-9">
          <div class="bg-white rounded shadow-sm p-4 p-md-4">

            <!-- 🔥 TIÊU ĐỀ THEO DESIGN MỚI -->
            <div class="custom-title-border">
              <h2 class="h6 fw-bold mb-0 text-dark text-uppercase d-flex align-items-center gap-2">
                📦 Quản lý đơn hàng của bạn
              </h2>
            </div>

            <!-- 🔥 BẢNG ĐƠN HÀNG THEO DESIGN MỚI -->
            <div class="table-responsive mt-3">
              <table class="custom-table">
                <thead>
                  <tr>
                    <th class="text-center">Mã Đơn</th>
                    <th class="text-center">Ngày Đặt</th>
                    <th>Tổng Tiền</th>
                    <th class="text-center">Trạng Thái Thanh Toán</th>
                    <th>Phương Thức</th>
                    <th class="text-center">Trạng Thái Giao</th>
                    <th class="text-center">Hành Động</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-if="orders.length === 0">
                    <td colspan="7" class="empty-message text-muted py-5 text-center">
                      <div class="fs-1 mb-2">🛍️</div>
                      Bạn chưa có đơn hàng nào.
                    </td>
                  </tr>

                  <tr v-for="order in paginatedOrders" :key="order.donHangId">
                    <td class="text-center fw-bold text-dark">
                      #{{ order.donHangId }}
                    </td>

                    <td class="text-center">
                      <div class="date-box">
                        <span class="d-block text-dark">{{ formatTime(order.ngayTao) }}</span>
                        <span class="d-block text-muted">{{ formatDateOnly(order.ngayTao) }}</span>
                      </div>
                    </td>

                    <td class="fw-bold text-dark">
                      {{ formatCurrency(order.tongThanhTien) }}
                    </td>

                    <td class="text-center">
                      <span class="status-badge"
                        :class="order.trangThaiThanhToan === 'DA_THANH_TOAN' ? 'badge-success-custom' : 'badge-warning-custom'">
                        {{ order.trangThaiThanhToan === "DA_THANH_TOAN" ? "Đã thanh toán" : "Chưa thanh toán" }}
                      </span>
                    </td>

                    <div class="payment-method-box"
                      :class="order.phuongThucThanhToan !== 'COD' && order.phuongThucThanhToan !== 'Tiền mặt' ? 'bank-transfer' : 'cod'">

                      <div class="icon-wrapper">
                        <!-- Icon Ngân hàng cho Chuyển khoản -->
                        <svg v-if="order.phuongThucThanhToan !== 'COD' && order.phuongThucThanhToan !== 'Tiền mặt'"
                          width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"
                          stroke-linecap="round" stroke-linejoin="round">
                          <path d="M3 21h18M3 10h18M5 10v11M19 10v11M12 10v11M7 10v11M17 10v11M2 7l10-5 10 5v3H2V7z" />
                        </svg>

                        <!-- Icon Tờ tiền cho COD -->
                        <svg v-else width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                          stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                          <rect x="2" y="6" width="20" height="12" rx="2" />
                          <circle cx="12" cy="12" r="2" />
                          <path d="M6 12h.01M18 12h.01" />
                        </svg>
                      </div>

                      <div class="payment-info">
                        <strong class="pay-title">
                          {{ order.phuongThucThanhToan !== 'COD' && order.phuongThucThanhToan !== 'Tiền mặt' ? 'Chuyển khoản' : 'Tiền mặt' }}
                        </strong>
                      </div>
                    </div>

                    <td class="text-center">
                      <span class="status-badge badge-info-custom">
                        {{ order.trangThaiDonHang || "CHO_XAC_NHAN" }}
                      </span>
                    </td>

                    <td class="text-center">
                      <div class="d-flex flex-column gap-2 align-items-center">
                        <button class="btn-action-custom w-100" @click="viewDetails(order.donHangId)">
                          👁 Xem Chi Tiết
                        </button>

                        <!-- Đánh giá shop -->
                        <button v-if="order.trangThaiDonHang === 'HOAN_THANH' && !order.daDanhGia"
                          class="btn-action-custom w-100" style="background-color: #28a745;"
                          @click="openReviewModal(order)">
                          ⭐ Đánh giá
                        </button>

                        <!-- Trả hàng -->
                        <button v-if="order.trangThaiDonHang === 'DA_GIAO'" class="btn-action-custom w-100"
                          style="background-color: #6c757d;" @click="openReturnModal(order)">
                          🔄 Trả Hàng
                        </button>
                      </div>
                    </td>
                  </tr>
                </tbody>
              </table>

              <!-- Phân trang -->
              <div v-if="totalPages > 1" class="pagination-wrapper mt-4">
                <button @click="prevPage" :disabled="currentPage === 1" class="btn-page">« Trước</button>
                <button v-for="page in totalPages" :key="page" @click="goToPage(page)"
                  :class="['btn-page', { active: currentPage === page }]">
                  {{ page }}
                </button>
                <button @click="nextPage" :disabled="currentPage === totalPages" class="btn-page">Sau »</button>
              </div>

            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- BẮT ĐẦU MODAL CHI TIẾT ĐƠN HÀNG STYLE OREKA MỚI -->
    <div v-if="isModalOpen" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content shopee-modal">
        <div class="modal-header">
          <div class="header-left">
            <button class="back-btn" @click="closeModal">❮ TRỞ LẠI</button>
          </div>
          <div class="header-right">
            <span>MÃ ĐƠN HÀNG. O2N{{ selectedOrder?.donHangId }}</span> |
            <span class="status-text">{{ selectedOrder?.trangThaiDonHang || 'CHỜ XÁC NHẬN' }}</span>
          </div>
        </div>

        <div class="modal-body oreka-body" v-if="selectedOrder">

          <div class="oreka-section">
            <div class="oreka-order-header">
              <div class="shop-name"><i class="bi bi-shop me-2"></i> Thông tin đơn hàng</div>
              <div class="order-date">{{ formatDate(selectedOrder.ngayTao) }}</div>
            </div>

            <div class="oreka-product-list">
              <div v-if="!selectedOrder.chiTietDonHangs || selectedOrder.chiTietDonHangs.length === 0"
                class="text-center text-muted py-4">
                <i class="bi bi-box-seam fs-3 d-block mb-2"></i>
                <em>Dữ liệu sản phẩm không tồn tại (Đơn test cũ)</em>
              </div>

              <div v-else v-for="item in selectedOrder.chiTietDonHangs" :key="item.chiTietId"
                class="oreka-product-item">
                <div class="product-img-box">
                  <img :src="item.hinhAnh ? item.hinhAnh : 'https://via.placeholder.com/80?text=Chua+Co+Anh'"
                    alt="Sản phẩm" class="product-img">
                </div>
                <div class="product-details">
                  <h4 class="product-name">{{ item.tenSanPham }}</h4>
                  <p class="product-meta">Số lượng: {{ item.soLuongMua }}</p>
                  <p class="product-price">Giá: <strong>{{ formatCurrency(item.giaLucMua) }}</strong></p>
                </div>
              </div>
            </div>
          </div>

          <div class="oreka-grid-2col">

            <div class="oreka-col">
              <h5 class="oreka-title">Thông tin vận chuyển:</h5>
              <div class="shipping-info">
                <p class="mb-3 text-muted" style="font-size: 13px;">Chi tiết vận chuyển:</p>

                <div class="vertical-timeline">
                  <div class="v-timeline-item completed">
                    <span class="v-dot"></span>
                    <div class="v-content">Người mua đã đặt hàng</div>
                  </div>

                  <div v-if="selectedOrder.trangThaiThanhToan === 'DA_THANH_TOAN'" class="v-timeline-item completed">
                    <span class="v-dot"></span>
                    <div class="v-content">Đã xác nhận thanh toán</div>
                  </div>

                  <div
                    v-if="selectedOrder.trangThaiDonHang === 'DANG_GIAO' || selectedOrder.trangThaiDonHang === 'DA_GIAO' || selectedOrder.trangThaiDonHang === 'HOAN_THANH'"
                    class="v-timeline-item completed">
                    <span class="v-dot"></span>
                    <div class="v-content text-primary fw-bold">Đang giao cho ĐVVC</div>
                  </div>

                  <div
                    v-if="selectedOrder.trangThaiDonHang === 'DA_GIAO' || selectedOrder.trangThaiDonHang === 'HOAN_THANH'"
                    class="v-timeline-item completed">
                    <span class="v-dot"></span>
                    <div class="v-content text-success fw-bold">Giao hàng thành công</div>
                  </div>

                  <div v-if="selectedOrder.trangThaiDonHang === 'DA_HUY'" class="v-timeline-item cancel">
                    <span class="v-dot"></span>
                    <div class="v-content text-danger">Đơn hàng đã bị hủy</div>
                  </div>
                </div>
              </div>
            </div>

            <div class="oreka-col border-left-col">

              <h5 class="oreka-title">Địa chỉ gửi hàng (Shop):</h5>
              <div class="address-text-box mb-4">
                <span class="d-block text-muted">
                  <i class="bi bi-geo-alt-fill me-1 text-secondary"></i>
                  {{ selectedOrder.diaChiCuaHang || 'Shop chưa cập nhật địa chỉ' }}
                </span>
              </div>

              <h5 class="oreka-title">Địa chỉ giao hàng:</h5>
              <div class="address-text-box mb-4">
                <strong class="d-block mb-1" style="font-size: 14px;">{{ selectedOrder.diaChiGiaoHang?.split(' | ')[0]
                  || 'Khách hàng' }}</strong>
                <span class="d-block text-muted mb-1">{{ selectedOrder.diaChiGiaoHang?.split(' | ')[1] || '' }}</span>
                <span class="d-block text-muted">{{ selectedOrder.diaChiGiaoHang?.split(' | ')[2] ||
                  selectedOrder.diaChiGiaoHang }}</span>
              </div>

              <h5 class="oreka-title mt-4">Chi tiết thanh toán:</h5>
              <div class="payment-summary-box">
                <div class="summary-line">
                  <span class="text-muted">Tổng tiền hàng:</span>
                  <span>{{ formatCurrency(selectedOrder.tongTienHang) }}</span>
                </div>
                <div class="summary-line">
                  <span class="text-muted">Phí vận chuyển:</span>
                  <span>{{ formatCurrency(selectedOrder.tongTienShip) }}</span>
                </div>
                <div class="summary-line total mt-2 pt-2 border-top">
                  <span class="d-flex align-items-center gap-1">
                    <i v-if="selectedOrder.phuongThucThanhToan === 'COD'" class="bi bi-cash-coin text-warning"></i>
                    <i v-else class="bi bi-credit-card-2-front text-primary"></i>
                    {{ selectedOrder.phuongThucThanhToan }}
                  </span>
                  <span class="text-danger fw-bold fs-5">{{ formatCurrency(selectedOrder.tongThanhTien) }}</span>
                </div>
              </div>
            </div>

          </div>
        </div>
      </div>
    </div>

    <!-- MODAL TRẢ HÀNG -->
    <div v-if="isReturnModalOpen" class="modal-overlay" @click.self="closeReturnModal">
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
              <option value="Hàng bị lỗi / Không hoạt động">Hàng bị lỗi / Không hoạt động</option>
              <option value="Giao sai sản phẩm / Phân loại">Giao sai sản phẩm / Phân loại</option>
              <option value="Hàng bị bể vỡ do vận chuyển">Hàng bị bể vỡ do vận chuyển</option>
              <option value="Hàng khác với mô tả">Hàng khác với mô tả</option>
            </select>
          </div>

          <div class="form-group">
            <label>Mô tả chi tiết tình trạng</label>
            <textarea v-model="returnForm.moTaChiTiet" rows="3"
              placeholder="Ví dụ: Áo bị rách ở phần tay, màn hình bật không lên..." class="form-control"></textarea>
          </div>

          <div class="form-group">
            <label>Hình ảnh chứng cứ <span class="text-danger">*</span></label>
            <input type="file" @change="handleFileUpload($event, 'image')" accept="image/*" class="form-control"
              :disabled="isUploadingImage" required />
            <div v-if="isUploadingImage" class="upload-status">⏳ Đang tải ảnh lên mây...</div>
            <img v-if="returnForm.hinhAnhBangChung" :src="returnForm.hinhAnhBangChung" class="preview-img"
              alt="Bằng chứng" />
          </div>

          <div class="form-group">
            <label>Video chứng cứ (Tùy chọn)</label>
            <input type="file" @change="handleFileUpload($event, 'video')" accept="video/*" class="form-control"
              :disabled="isUploadingVideo" />
            <div v-if="isUploadingVideo" class="upload-status">⏳ Đang tải video lên mây...</div>
            <video v-if="returnForm.videoBangChung" :src="returnForm.videoBangChung" controls
              class="preview-video"></video>
          </div>

          <div class="return-actions">
            <button type="button" class="btn-cancel" @click="closeReturnModal">Hủy</button>
            <button type="submit" class="btn-submit" :disabled="isUploadingImage || isUploadingVideo">Gửi Yêu
              Cầu</button>
          </div>
        </form>
      </div>
    </div>

    <!-- MODAL ĐÁNH GIÁ SHOP -->
    <div v-if="isReviewModalOpen" class="modal-overlay" @click.self="closeReviewModal">
      <div class="modal-content shopee-modal" style="max-width: 450px; border-radius: 20px;">
        <div class="modal-header border-0 pb-0 justify-content-between">
          <h4 class="fw-bold text-success mb-0">Đánh giá trải nghiệm</h4>
          <button class="btn-close" @click="closeReviewModal"></button>
        </div>

        <div class="modal-body text-center pt-3">
          <div class="mb-3">
            <i class="bi bi-shop fs-1 text-success"></i>
            <p class="text-muted small mt-2">Duy ơi, hãy chia sẻ cảm nhận của bạn về Shop nhé!</p>
          </div>

          <div class="star-rating fs-1 mb-4">
            <span v-for="star in 5" :key="star" @click="reviewForm.soSao = star"
              :class="star <= reviewForm.soSao ? 'text-warning' : 'text-secondary'"
              style="cursor: pointer; transition: transform 0.2s; display: inline-block;"
              onmouseover="this.style.transform='scale(1.2)'" onmouseout="this.style.transform='scale(1)'">
              ★
            </span>
          </div>

          <div class="form-group text-start px-2">
            <label class="small fw-bold mb-2">Lời bình luận của Duy:</label>
            <textarea v-model="reviewForm.binhLuan" class="form-control border-success shadow-none" rows="4"
              style="border-radius: 12px;"
              placeholder="Ví dụ: Shop giao hàng nhanh, sản phẩm còn rất mới..."></textarea>
          </div>
        </div>

        <div class="modal-footer border-0 justify-content-center pb-4">
          <button class="btn btn-light px-4 rounded-pill fw-bold" @click="closeReviewModal">Để sau</button>
          <button class="btn btn-success px-4 rounded-pill shadow-sm fw-bold" @click="submitReview"
            :disabled="!reviewForm.soSao">
            Gửi Đánh Giá Ngay
          </button>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import UserSidebar from "@/layouts/UserSidebar.vue";

const router = useRouter();
const orders = ref([]);
const selectedOrder = ref(null);
const isModalOpen = ref(false);
const currentUser = ref(null);

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

const isReviewModalOpen = ref(false);
const selectedOrderForReview = ref(null);
const reviewForm = ref({ soSao: 0, binhLuan: "" });

const openReviewModal = (order) => {
  selectedOrderForReview.value = order;
  isReviewModalOpen.value = true;
};

const closeReviewModal = () => {
  isReviewModalOpen.value = false;
  reviewForm.value = { soSao: 0, binhLuan: "" };
};

const submitReview = async () => {
  try {
    if (!currentUser.value) {
      alert("Vui lòng đăng nhập để đánh giá!");
      return;
    }

    const firstProduct = selectedOrderForReview.value.chiTietDonHangs?.[0];

    const payload = {
      sanPham: { sanPhamId: firstProduct.sanPhamId },
      nguoiMua: { nguoiDungId: currentUser.value.nguoiDungId || currentUser.value.id },
      soSao: reviewForm.value.soSao,
      binhLuan: reviewForm.value.binhLuan,
      createdAt: new Date().toISOString()
    };

    await axios.post("http://localhost:8080/api/products/reviews", payload);

    alert("🎉 Đã gửi đánh giá thành công!");

    if (selectedOrderForReview.value) {
      selectedOrderForReview.value.daDanhGia = true;
    }

    closeReviewModal();
    fetchOrders();
  } catch (error) {
    console.error("Lỗi khi gửi đánh giá:", error);
    alert("Không thể gửi đánh giá, Bạn thử lại nhé!");
  }
};

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

const goToPage = (page) => { currentPage.value = page; };
const prevPage = () => { if (currentPage.value > 1) currentPage.value--; };
const nextPage = () => { if (currentPage.value < totalPages.value) currentPage.value++; };

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

    const apiUrl = type === "image" ? "http://localhost:8080/api/media/upload-image" : "http://localhost:8080/api/media/upload-video";

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
  try {
    if (!currentUser.value) return;
    const currentId = currentUser.value.nguoiDungId || currentUser.value.id;

    const res = await axios.get(`http://localhost:8080/api/don-hang/danh-sach/${currentId}`);
    const ordersData = res.data;

    let myReviews = [];
    try {
      const reviewRes = await axios.get(`http://localhost:8080/api/products/reviews/buyer/${currentId}`);
      myReviews = reviewRes.data;
    } catch (revErr) {
      console.warn("Chưa lấy được review:", revErr);
    }

    orders.value = ordersData.map(order => {
      const hasReviewed = myReviews.some(rev =>
        order.chiTietDonHangs && order.chiTietDonHangs.some(detail => detail.sanPhamId === rev.sanPham?.sanPhamId)
      );
      return { ...order, daDanhGia: hasReviewed };
    });

  } catch (error) {
    console.error("Lỗi tải đơn hàng:", error);
  }
};

const viewDetails = async (donHangId) => {
  try {
    const response = await axios.get(`http://localhost:8080/api/don-hang/chi-tiet/${donHangId}`);
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

    const response = await axios.post("http://localhost:8080/api/don-hang/yeu-cau-tra-hang", requestData, {
      headers: { Authorization: `Bearer ${token}` },
    });

    alert(response.data.message || "Đã gửi yêu cầu trả hàng thành công!");
    closeReturnModal();
    fetchOrders();
  } catch (error) {
    alert("Có lỗi xảy ra: " + (error.response?.data?.error || "Vui lòng thử lại sau!"));
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

// Hàm định dạng Giờ riêng
const formatTime = (dateString) => {
  if (!dateString) return "";
  const date = new Date(dateString);
  return date.toLocaleTimeString("vi-VN", { hour12: false });
};

// Hàm định dạng Ngày riêng
const formatDateOnly = (dateString) => {
  if (!dateString) return "";
  const date = new Date(dateString);
  return date.toLocaleDateString("vi-VN");
};

onMounted(() => {
  const storedUser = localStorage.getItem('user');
  if (storedUser) {
    currentUser.value = JSON.parse(storedUser);
  }
  fetchOrders();
});
</script>
<style scoped>
/* ================= CSS MỚI CHO BẢNG & TIÊU ĐỀ ================= */
.custom-title-border {
  border-bottom: 2px solid #ee4d2d;
  padding-bottom: 12px;
  margin-bottom: 20px;
}

.custom-table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
}

.custom-table th {
  background-color: #f8f9fa;
  color: #555;
  font-weight: bold;
  font-size: 13px;
  padding: 15px 10px;
  border-bottom: none;
  text-align: center;
  /* Đảm bảo tiêu đề căn giữa */
}

.custom-table td {
  padding: 10px 10px;
  /* Giảm nhẹ padding dọc để khít hơn */
  border-bottom: 1px solid #f0f0f0;
  /* CHỐT CHẶN KHOẢNG TRẮNG: Ép nội dung luôn nằm giữa dòng */
  vertical-align: middle !important;
  font-size: 14px;
}

.custom-table tbody tr:hover {
  background-color: #fafafa;
}

.date-box {
  font-size: 13px;
  line-height: 1.4;
}

.status-badge {
  padding: 6px 14px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: bold;
  color: white;
  display: inline-block;
  white-space: nowrap;
}

.badge-warning-custom {
  background-color: #ffb800;
}

.badge-success-custom {
  background-color: #28a745;
}

.badge-info-custom {
  background-color: #17a2b8;
}

/* Container chung cho box phương thức */
.payment-method-box {
  display: inline-flex;
  align-items: center;
  /* Căn giữa icon và chữ theo chiều dọc */
  gap: 12px;
  /* Khoảng cách giữa icon và cụm chữ */
  padding: 8px 16px;
  border-radius: 12px;
  /* Bo góc tròn hơn giống ảnh mẫu */
  min-width: 180px;
  height: auto;
  /* Để chiều cao tự co giãn theo nội dung */
  font-family: sans-serif;
  border: 1px solid transparent;
  transition: all 0.3s ease;
}

/* Cụm text bên phải icon */
.payment-info {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.payment-info strong {
  font-size: 15px;
  font-weight: 700;
  line-height: 1.2;
}

.payment-info span {
  font-size: 11px;
  margin-top: 2px;
}

/* 1. Style cho Tiền mặt (COD) - Màu xanh lá */
.payment-method-box.cod {
  background-color: #f0fdf4;
  /* Nền xanh lá cực nhạt */
  border-color: #bbf7d0;
  /* Viền xanh lá nhạt */
}

.payment-method-box.cod strong {
  color: #166534;
  /* Chữ xanh lá đậm */
}

.payment-method-box.cod span {
  color: #15803d;
}

.payment-method-box.cod .icon-wrapper {
  background-color: #dcfce7;
  color: #16a34a;
  padding: 8px;
  border-radius: 50%;
  /* Icon nằm trong vòng tròn */
  display: flex;
}

/* 2. Style cho Chuyển khoản (Bank) - Màu xanh dương */
.payment-method-box.bank-transfer {
  background-color: #eff6ff;
  /* Nền xanh dương cực nhạt */
  border-color: #dbeafe;
  /* Viền xanh dương nhạt */
}

.payment-method-box.bank-transfer strong {
  color: #1e40af;
  /* Chữ xanh dương đậm */
}

.payment-method-box.bank-transfer span {
  color: #1d4ed8;
}

.payment-method-box.bank-transfer .icon-wrapper {
  background-color: #dbeafe;
  color: #2563eb;
  padding: 8px;
  border-radius: 50%;
  display: flex;
}

.icon-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;  /* Cố định kích thước vòng tròn */
  height: 32px;
  border-radius: 50%;
  flex-shrink: 0; /* Không cho vòng tròn bị bóp méo */
}

/* Màu icon cho Chuyển khoản */
.bank-transfer .icon-wrapper {
  background-color: #dbeafe;
  color: #2563eb;
}

/* Màu icon cho COD */
.cod .icon-wrapper {
  background-color: #dcfce7;
  color: #16a34a;
}

/* CSS MỚI ĐỂ GỘP NÚT HÀNH ĐỘNG (Xóa khoảng trắng dọc) */
.action-buttons-wrapper {
  display: flex;
  flex-direction: column;
  gap: 4px;
  /* Khoảng cách cực nhỏ giữa các nút */
  align-items: center;
}

.btn-action-custom {
  background-color: #f15832;
  color: white;
  border: none;
  padding: 6px 12px;
  /* Thu gọn nút */
  border-radius: 4px;
  font-weight: bold;
  font-size: 12px;
  cursor: pointer;
  transition: 0.2s;
  white-space: nowrap;
  min-width: 120px;
  margin: 0 !important;
  /* Xóa margin thừa gây hở dòng */
}

.btn-action-custom:hover {
  background-color: #d73a27;
}

/* Giữ nguyên các phần Modal và Timeline bên dưới không đổi */
.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  padding: 10px 0;
}

.btn-page {
  padding: 8px 14px;
  border: 1px solid #dee2e6;
  background-color: white;
  color: #ee4d2d;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
  transition: all 0.2s;
}

.btn-page:hover:not(:disabled) {
  background-color: #fff5f5;
}

.btn-page.active {
  background-color: #ee4d2d;
  color: white;
  border-color: #ee4d2d;
}

.btn-page:disabled {
  color: #6c757d;
  background-color: #f8f9fa;
  cursor: not-allowed;
  border-color: #dee2e6;
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

.oreka-body {
  padding: 20px 30px;
  overflow-y: auto;
}

.oreka-section {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  margin-bottom: 20px;
}

.oreka-order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px dashed #eaeaea;
  padding-bottom: 15px;
  margin-bottom: 15px;
}

.shop-name {
  font-weight: 700;
  color: #333;
  font-size: 15px;
}

.order-date {
  color: #888;
  font-size: 13px;
}

.oreka-product-item {
  display: flex;
  gap: 15px;
  padding: 10px 0;
  border-bottom: 1px solid #f5f5f5;
}

.oreka-product-item:last-child {
  border-bottom: none;
}

.product-img-box {
  width: 80px;
  height: 80px;
  flex-shrink: 0;
  border: 1px solid #eee;
  border-radius: 4px;
  overflow: hidden;
}

.product-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-details {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.product-details .product-name {
  font-size: 15px;
  font-weight: 600;
  color: #333;
  margin-bottom: 5px;
}

.product-meta {
  font-size: 13px;
  color: #777;
  margin-bottom: 5px;
}

.product-price strong {
  color: #ee4d2d;
  font-size: 14px;
}

.oreka-grid-2col {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  background: white;
  padding: 25px 20px;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.oreka-title {
  font-size: 16px;
  font-weight: 700;
  color: #333;
  margin-bottom: 15px;
  border-left: 3px solid #ee4d2d;
  padding-left: 8px;
  line-height: 1;
}

.border-left-col {
  border-left: 1px dashed #eaeaea;
  padding-left: 20px;
}

.vertical-timeline {
  position: relative;
  padding-left: 15px;
  border-left: 2px solid #eee;
  margin-left: 5px;
}

.v-timeline-item {
  position: relative;
  padding-bottom: 20px;
}

.v-timeline-item:last-child {
  padding-bottom: 0;
}

.v-dot {
  position: absolute;
  left: -21px;
  top: 0;
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: #ccc;
  border: 2px solid white;
  box-shadow: 0 0 0 1px #eee;
}

.v-timeline-item.completed .v-dot {
  background: #007bff;
  box-shadow: 0 0 0 1px #007bff;
}

.v-timeline-item.cancel .v-dot {
  background: #dc3545;
  box-shadow: 0 0 0 1px #dc3545;
}

.v-content {
  font-size: 14px;
  color: #555;
  padding-left: 10px;
  line-height: 1.2;
}

.address-text-box {
  font-size: 13px;
  line-height: 1.5;
}

.payment-summary-box {
  background: #fdfdfd;
  padding: 15px;
  border-radius: 6px;
  border: 1px solid #f0f0f0;
}

.summary-line {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 13px;
}

.summary-line:last-child {
  margin-bottom: 0;
}

.summary-line.total {
  align-items: center;
}

@media (max-width: 768px) {
  .oreka-grid-2col {
    grid-template-columns: 1fr;
  }

  .border-left-col {
    border-left: none;
    padding-left: 0;
    border-top: 1px dashed #eaeaea;
    padding-top: 20px;
    margin-top: 10px;
  }
}
</style>