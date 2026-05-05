<template>
  <div class="bg-light min-vh-100">
    <AppHeader />

    <main class="container main-content py-4">
      <div class="d-flex align-items-center justify-content-between mb-3">
        <nav aria-label="breadcrumb">
          <ol class="breadcrumb mb-0 bg-transparent p-0">
            <li class="breadcrumb-item">
              <router-link to="/" class="text-decoration-none text-muted">
                <i class="bi bi-house-door"></i> Trang chủ
              </router-link>
            </li>
            <li class="breadcrumb-item active text-danger" aria-current="page">Gian hàng người bán</li>
          </ol>
        </nav>

        <button @click="router.push('/')" class="btn btn-sm btn-outline-secondary rounded-pill px-3 shadow-sm">
          <i class="bi bi-arrow-left"></i> Quay lại trang chủ
        </button>
      </div>

      <section v-if="seller"
        class="shop-header bg-white rounded-4 shadow-sm p-4 mb-4 border-top border-5 border-danger">
        <div class="row align-items-center">
          <div class="col-md-auto text-center">
            <div class="position-relative d-inline-block">
              <img :src="seller.anhDaiDien || 'https://via.placeholder.com/150'"
                class="rounded-circle border border-3 border-white shadow"
                style="width: 130px; height: 130px; object-fit: cover;" alt="Avatar Shop" />
              <span class="position-absolute bottom-0 end-0 bg-success border border-2 border-white rounded-circle p-2"
                title="Đang hoạt động"></span>
            </div>
          </div>

          <div class="col-md ms-md-4 mt-3 mt-md-0">
            <h2 class="fw-bold text-dark mb-1">{{ seller.hoVaTen }}</h2>
            <div class="d-flex flex-wrap gap-3 mb-3 text-muted small">
              <span><i class="bi bi-calendar3"></i> Tham gia: {{ formatDate(seller.ngayTao) }}</span>
              <span><i class="bi bi-geo-alt"></i> Khu vực: Việt Nam</span>
            </div>

            <div class="d-flex gap-2">
              <div class="stats-card bg-light px-3 py-2 rounded-3 border text-center">
                <div class="fw-bold text-danger">{{ products.length }}</div>
                <div class="text-muted" style="font-size: 0.75rem;">Sản phẩm</div>
              </div>
              <div class="stats-card bg-light px-3 py-2 rounded-3 border text-center">
                <div class="fw-bold text-primary">
                  {{ seller?.soSaoTrungBinh || '0.0' }} <span style="color: #ffc107;">★</span>
                </div>
                <div class="text-muted" style="font-size: 0.75rem;">Đánh giá</div>
              </div>
            </div>
          </div>

          <div class="col-md-auto mt-4 mt-md-0 d-flex flex-column gap-2">
            <button class="btn btn-danger px-4 fw-bold shadow-sm">
              <i class="bi bi-plus-lg"></i> THEO DÕI
            </button>
            <button class="btn btn-outline-danger" @click="startChatWithSeller">
              <i class="bi bi-chat-dots"></i> CHAT NGAY
            </button>
          </div>
        </div>
      </section>

   <section>
        <div class="d-flex justify-content-between align-items-end mb-4 border-start border-4 border-danger ps-3">
          <div>
            <h4 class="fw-bold text-dark mb-0">Tất cả sản phẩm</h4>
            <small class="text-muted">Danh sách mặt hàng đang được rao bán</small>
          </div>
          <div class="d-flex gap-2">
            <select class="form-select form-select-sm shadow-sm" style="width: 160px;">
              <option selected>Mới nhất</option>
              <option>Giá: Thấp đến Cao</option>
              <option>Giá: Cao đến Thấp</option>
            </select>
          </div>
        </div>

        <div v-if="isLoading" class="text-center py-5">
          <div class="spinner-border text-danger" role="status"></div>
          <p class="mt-2 text-muted">Đang tải gian hàng...</p>
        </div>

        <!-- 🔥 ĐÃ SỬA: Đổi paginatedProducts thành displayProducts -->
        <div v-else-if="displayProducts && displayProducts.length > 0">
          <div class="row row-cols-2 row-cols-md-3 row-cols-lg-5 g-3">
            <div v-for="item in paginatedProducts" :key="item.id" class="col">
              <div class="card h-100 shadow-sm product-card border-0 position-relative">
                <span class="position-absolute top-0 start-0 badge bg-warning text-dark m-2 shadow-sm"
                  style="z-index: 10;">
                  {{ item.tinhTrang || 'Đồ cũ' }}
                </span>

                <div @click="goToDetail(item.id)" class="bg-light border-bottom overflow-hidden cursor-pointer"
                  style="height: 200px;">
                  <img :src="item.hinhAnh || 'https://via.placeholder.com/300'"
                    class="w-100 h-100 object-fit-cover transition-img" alt="Sản phẩm">
                </div>

                <div class="card-body p-3 d-flex flex-column">
                  <h6 @click="goToDetail(item.id)" class="card-title text-dark text-truncate mb-2 cursor-pointer"
                    :title="item.tenSanPham">
                    {{ item.tenSanPham }}
                  </h6>
                  <div class="mt-auto">
                    <div class="d-flex justify-content-between align-items-center mb-2">
                      <span class="text-danger fw-bold fs-6">{{ formatCurrency(item.gia) }}</span>
                    </div>

                    <button v-if="String(currentUserId) === String(seller?.nguoiDungId || item.nguoiDungId)"
                      class="btn btn-secondary btn-sm w-100 fw-bold py-2 opacity-75" style="cursor: not-allowed;"
                      disabled>
                      <i class="bi bi-person-check"></i> SẢN PHẨM CỦA BẠN
                    </button>

                    <button v-else @click="addToCart(item)"
                      class="btn btn-outline-danger btn-sm w-100 fw-bold py-2 hover-up">
                      <i class="bi bi-cart-plus"></i> Thêm vào giỏ
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- THANH PHÂN TRANG -->
          <nav v-if="totalPages > 1" class="mt-5 d-flex justify-content-center">
            <ul class="pagination shadow-sm">
              <li class="page-item" :class="{ disabled: currentPage === 1 }">
                <button class="page-link" @click="changePage(currentPage - 1)">Trước</button>
              </li>
              <li v-for="page in totalPages" :key="page" class="page-item" :class="{ active: currentPage === page }">
                <button class="page-link" @click="changePage(page)">{{ page }}</button>
              </li>
              <li class="page-item" :class="{ disabled: currentPage === totalPages }">
                <button class="page-link" @click="changePage(currentPage + 1)">Sau</button>
              </li>
            </ul>
          </nav>
        </div>

        <!-- THÔNG BÁO RỖNG NẰM TRONG v-else CUỐI CÙNG -->
        <div v-else class="text-center py-5 bg-white rounded-4 shadow-sm border mt-4">
          <div class="display-1 text-muted opacity-25 mb-3">📦</div>
          <h5 class="fw-bold text-secondary">Chưa có sản phẩm nào</h5>
          <p class="text-muted">Người bán này hiện chưa đăng tải mặt hàng nào lên gian hàng.</p>
          <router-link to="/" class="btn btn-primary px-4">Quay về Trang chủ</router-link>
        </div>
      </section>

      <!-- SECTION BÌNH LUẬN & ĐÁNH GIÁ -->
      <section class="mt-5 bg-white rounded-4 shadow-sm p-4 border">
        <div class="d-flex align-items-center gap-2 mb-4 border-bottom pb-3">
          <h4 class="fw-bold mb-0">Khách hàng nói gì về {{ seller?.hoVaTen }}</h4>
          <span class="badge bg-primary rounded-pill">{{ sellerReviews.length }} nhận xét</span>
        </div>

        <div v-if="sellerReviews.length > 0" class="review-list">
          <div v-for="rev in sellerReviews" :key="rev.danhGiaId" class="review-item border-bottom py-3">
            <div class="d-flex gap-3">
              <!-- Avatar người mua (mặc định hoặc từ DB) -->
              <div
                class="rounded-circle bg-secondary text-white d-flex align-items-center justify-content-center fw-bold"
                style="width: 45px; height: 45px; flex-shrink: 0;">
                {{ rev.nguoiMua?.hoVaTen?.charAt(0).toUpperCase() || 'U' }}
              </div>

              <div class="flex-grow-1">
                <div class="d-flex justify-content-between align-items-start mb-1">
                  <h6 class="fw-bold mb-0 text-dark">{{ rev.nguoiMua?.hoVaTen || 'Người dùng ẩn danh' }}</h6>
                  <small class="text-muted">{{ formatDate(rev.createdAt) }}</small>
                </div>

                <!-- 🔥 Nút Xóa: Chỉ hiện nếu người đang xem là người đã đánh giá -->
                <button v-if="isMyReview(rev.nguoiMua?.nguoiDungId || rev.nguoiMua?.id)"
                  @click="handleDeleteReview(rev.danhGiaId)" class="btn btn-sm p-0 border-0 text-danger ms-2">
                  <i class="bi bi-trash"></i> Xóa
                </button>

                <!-- Hiển thị sao của từng bình luận -->
                <div class="text-warning small mb-2">
                  <span v-for="s in 5" :key="s">
                    {{ s <= rev.soSao ? '★' : '☆' }} </span>
                </div>

                <p class="text-muted mb-2" style="font-size: 0.95rem; line-height: 1.5;">
                  "{{ rev.binhLuan }}"
                </p>

                <!-- Ghi chú sản phẩm khách đã mua -->
                <div class="bg-light p-2 rounded border-start border-3 border-secondary small">
                  <i class="bi bi-bag-check me-1"></i> Đã mua: <strong>{{ rev.sanPham?.tenSanPham }}</strong>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div v-else class="text-center py-4">
          <p class="text-muted">Shop chưa có bình luận nào từ khách hàng.</p>
        </div>
      </section>

    </main>

    <AppFooter />
  </div>
</template>

<script setup>
import { ref, onMounted, computed, inject } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import AppHeader from '@/layouts/Header.vue';
import AppFooter from '@/layouts/Footer.vue';


import { db } from "@/firebase";
import { ref as dbRef, set, get } from "firebase/database";





const route = useRoute();
const router = useRouter();
const seller = ref(null);
const products = ref([]);
const isLoading = ref(true);
const currentUserId = ref(null);
const sellerReviews = ref([]);
const globalChat = inject('globalChat');


const currentPage = ref(1);
const itemsPerPage = 10; //  Muốn hiện bao nhiêu món 1 trang thì sửa ở đây


const startChatWithSeller = async () => {
  // 1. Kiểm tra đăng nhập
  if (!currentUserId.value) {
    alert("Duy ơi, bạn cần đăng nhập để chat nhé!");
    return;
  }

  const sellerId = String(seller.value?.nguoiDungId);
  const myId = String(currentUserId.value);

  // 2. Không cho tự chat với chính mình
  if (myId === sellerId) {
    alert("🔒 Bạn không thể tự chat với chính mình!");
    return;
  }

  try {
    // 3. Tạo chatId duy nhất
    const chatId = Number(myId) < Number(sellerId) ? `${myId}_${sellerId}` : `${sellerId}_${myId}`;

    // 4. Kiểm tra/Tạo hội thoại trên Firebase (để lưu danh sách chat)
    const chatRef = dbRef(db, `conversations/${myId}/${chatId}`);
    const snapshot = await get(chatRef);

    if (!snapshot.exists()) {
      const timestamp = Date.now();
      const myName = JSON.parse(localStorage.getItem('user'))?.hoVaTen || "Người mua";

      await set(dbRef(db, `conversations/${myId}/${chatId}`), {
        receiverId: sellerId,
        receiverName: seller.value.hoVaTen,
        lastMessage: "Bắt đầu cuộc trò chuyện...",
        timestamp: timestamp
      });

      await set(dbRef(db, `conversations/${sellerId}/${chatId}`), {
        receiverId: myId,
        receiverName: myName,
        lastMessage: "Bắt đầu cuộc trò chuyện...",
        timestamp: timestamp
      });
    }

    // 🔥 BƯỚC QUAN TRỌNG: Gọi ô chat Popup hiện lên ngay tại trang hiện tại
    globalChat.openChat(chatId, seller.value.hoVaTen);

  } catch (error) {
    console.error("Lỗi khi mở chat:", error);
    alert("Có lỗi xảy ra, Duy thử lại sau nhé!");
  }
};


const handleDeleteReview = async (reviewId) => {
  if (confirm("Duy chắc chắn muốn xóa đánh giá này chứ?")) {
    try {
      await axios.delete(`http://localhost:8080/api/products/reviews/${reviewId}`);
      alert("Đã xóa đánh giá thành công!");
      // Tải lại dữ liệu shop để cập nhật số sao và danh sách review
      fetchShopData();
    } catch (error) {
      console.error("Lỗi khi xóa:", error);
      alert("Không thể xóa đánh giá lúc này.");
    }
  }
};

const isMyReview = (reviewerId) => {
  if (!reviewerId || !currentUserId.value) return false;
  // Ép cả hai về chuỗi để so sánh cho chắc chắn Duy nhé
  return String(reviewerId) === String(currentUserId.value);
};

// Tính tổng số trang
const totalPages = computed(() => {
  return Math.ceil(displayProducts.value.length / itemsPerPage);
});

// Lấy danh sách sản phẩm theo trang hiện tại
const paginatedProducts = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return displayProducts.value.slice(start, end);
});

// Hàm chuyển trang
const changePage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page;
    // Cuộn lên đầu danh sách sản phẩm cho mượt
    window.scrollTo({ top: 300, behavior: 'smooth' }); // Cuộn lên đầu danh sách sản phẩm
  }
};

onMounted(() => {
  // Lấy thông tin Duy đang đăng nhập
  const storedUser = localStorage.getItem('user');
  if (storedUser) {
    const user = JSON.parse(storedUser);
    currentUserId.value = user.id || user.nguoiDungId;
  }

  fetchShopData();
  window.scrollTo(0, 0);
});

// 1. Định dạng tiền tệ
const formatCurrency = (val) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(val || 0);
};

// 2. Định dạng ngày tháng
const formatDate = (dateStr) => {
  if (!dateStr) return 'N/A';
  return new Date(dateStr).toLocaleDateString('vi-VN', { year: 'numeric', month: 'long', day: 'numeric' });
};


// 3. Logic lọc sản phẩm theo trạng thái và quyền sở hữu
const displayProducts = computed(() => {
  if (!products.value.length) return [];

  return products.value.filter(p => {
    // Nếu là chủ shop (Duy tự vào xem shop của Duy): Hiện hết tất cả
    if (String(currentUserId.value) === String(seller.value?.nguoiDungId || p.nguoiDungId)) {
      return true;
    }
    // Nếu là khách vào xem: Chỉ hiện những sản phẩm đang bán
    return p.trangThai === 'DANG_BAN' || !p.trangThai;
  });
});

// 4. Lấy dữ liệu Shop từ Backend
const fetchShopData = async () => {
  const sellerId = route.params.id;
  isLoading.value = true;
  try {
    // Lấy thông tin Shop & Số sao trung bình
    const infoRes = await axios.get(`http://localhost:8080/api/products/seller/${sellerId}/info`);
    seller.value = infoRes.data;

    // Lấy danh sách sản phẩm
    const productsRes = await axios.get(`http://localhost:8080/api/products/seller/${sellerId}`);
    products.value = productsRes.data.content;

    // 🔥 MỚI: Lấy danh sách bình luận của khách cũ
    const reviewRes = await axios.get(`http://localhost:8080/api/products/seller/${sellerId}/reviews`);
    sellerReviews.value = reviewRes.data;

  } catch (error) {
    console.error("Lỗi khi tải dữ liệu Shop:", error);
  } finally {
    isLoading.value = false;
  }
};

// 5. Chuyển sang trang chi tiết
const goToDetail = (id) => {
  router.push(`/product/${id}`);
};

// 6. Thêm vào giỏ hàng
const addToCart = (product) => {
  const ownerId = seller.value?.nguoiDungId || product.nguoiDungId;

  if (String(currentUserId.value) === String(ownerId)) {
    alert("🔒 Duy ơi, bạn không thể tự thêm sản phẩm của chính mình vào giỏ hàng đâu!");
    return;
  }

  let cart = JSON.parse(localStorage.getItem('cart') || '[]');
  const existingItem = cart.find(item => item.id === product.id);

  if (existingItem) {
    alert(`Sản phẩm "${product.tenSanPham}" đã có trong giỏ hàng!`);
  } else {
    cart.push({
      id: product.id,
      tenSanPham: product.tenSanPham,
      gia: product.gia,
      hinhAnh: product.hinhAnh
    });
    localStorage.setItem('cart', JSON.stringify(cart));
    alert(`Đã thêm "${product.tenSanPham}" vào giỏ hàng!`);
  }
};
</script>

<style scoped>
.main-content {
  margin-top: 160px;
  min-height: 80vh;
}

.shop-header {
  transition: all 0.3s ease;
}

.stats-card {
  min-width: 80px;
}

.product-card {
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  overflow: hidden;
}

.product-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1) !important;
}

.product-card:hover .transition-img {
  transform: scale(1.08);
}

.transition-img {
  transition: transform 0.5s ease;
}

.cursor-pointer {
  cursor: pointer;
}

.hover-up:hover {
  transform: translateY(-2px);
  background-color: #dc3545;
  color: white !important;
  transition: all 0.2s ease;
}

.btn-secondary {
  background-color: #6c757d;
  border: none;
}

@media (max-width: 768px) {
  .main-content {
    margin-top: 115px;
  }

  .shop-header {
    padding: 1.5rem !important;
  }
}
</style>