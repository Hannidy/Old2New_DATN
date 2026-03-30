<template>
  <div class="bg-light min-vh-100 pb-5 font-sans text-dark position-relative">
    
    <header class="bg-white border-bottom mb-4">
      <div class="container d-flex align-items-center justify-content-between py-3">
        <div class="d-flex align-items-center gap-4 flex-grow-1">
          <h1 class="h3 fw-bold mb-0 text-dark">Old2New</h1>
          
          <div class="input-group" style="max-width: 500px;">
            <input type="text" class="form-control" placeholder="Tìm kiếm sản phẩm...">
            <button class="btn btn-outline-secondary bg-light text-dark" type="button">🔍</button>
          </div>
        </div>
        
        <div class="d-flex align-items-center gap-3">
          <button class="btn btn-light border-secondary border fw-medium btn-sm px-3">Tìm nâng cao</button>
          <button class="btn btn-light border-0 fs-5 px-2">🛒</button>
          <button class="btn btn-light border-0 fs-5 px-2">🔔</button>
          <button class="btn btn-outline-dark fw-medium btn-sm px-4">Đăng bán</button>
        </div>
      </div>
    </header>

    <main class="container bg-white shadow-sm p-4 p-md-5 rounded">
      <h2 class="h3 fw-bold mb-4 text-dark">Đăng bán</h2>

      <form @submit.prevent="submitForm">
        
        <div class="mb-4">
          <input 
            type="file" 
            ref="fileInput" 
            multiple 
            accept="image/*" 
            class="d-none" 
            @change="handleFileChange"
          >
          
          <div 
            class="mb-3 p-5 text-center bg-light rounded text-muted" 
            style="border: 2px dashed #ced4da; cursor: pointer; transition: background-color 0.2s;"
            @click="triggerFileInput"
            onmouseover="this.style.backgroundColor='#e2e6ea'"
            onmouseout="this.style.backgroundColor='#f8f9fa'"
          >
            <h3 class="fs-4 fw-medium text-secondary mb-2">Tải hình ảnh/video</h3>
            <p class="small mb-0">Kéo thả hoặc nhấn để chọn file từ thiết bị</p>
          </div>

          <div v-if="previewImages.length > 0" class="d-flex flex-wrap gap-3 p-3 border rounded bg-white">
            <div v-for="(img, index) in previewImages" :key="index" class="position-relative">
              <img :src="img" class="img-thumbnail object-fit-cover shadow-sm" style="width: 120px; height: 120px;">
              <button 
                type="button" 
                class="btn btn-danger btn-sm position-absolute top-0 end-0 m-1 rounded-circle d-flex align-items-center justify-content-center" 
                style="width: 24px; height: 24px; padding: 0;" 
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
            <select v-model="form.category" required class="form-select border-secondary text-muted">
              <option value="" disabled>Chọn danh mục chi tiết nhất</option>
              
              <option v-for="cat in flattenedCategories" :key="cat.id" :value="cat.id">
                {{ cat.name }}
              </option>
              
            </select>
          </div>

          <!-- Lấy cả danh mục con đoạn code bên dưới  -->
          <!-- <div class="mb-3">
            <label class="form-label fw-bold text-dark">Danh mục</label>
            <select v-model="form.category" required class="form-select border-secondary text-muted">
              <option value="" disabled>Chọn danh mục phù hợp</option>
              
              <optgroup v-for="parent in categories" :key="parent.id" :label="parent.name">
                
                <option v-for="child in parent.children" :key="child.id" :value="child.id">
                  {{ child.name }}
                </option>

                <option v-if="!parent.children || parent.children.length === 0" :value="parent.id">
                  {{ parent.name }}
                </option>

              </optgroup>
              
            </select>
          </div> -->

          <div class="mb-3">
            <label class="form-label fw-bold text-dark">Tên sản phẩm</label>
            <input type="text" v-model="form.name" required class="form-control border-secondary">
          </div>

          <div class="mb-3">
            <label class="form-label fw-bold text-dark">Giá sản phẩm</label>
            <div class="input-group">
              <input type="number" v-model="form.price" required min="10000" class="form-control border-secondary" placeholder="Nhập giá bán...">
              <span class="input-group-text bg-white border-secondary">VNĐ</span>
            </div>
          </div>

          <div class="mb-3">
            <label class="form-label fw-bold text-dark">Mô tả sản phẩm</label>
            <textarea v-model="form.description" rows="5" class="form-control border-secondary"></textarea>
          </div>
        </div>

        <hr class="my-5">

        <h3 class="h4 fw-bold mb-4 text-dark">Thông tin khác</h3>

        <div class="mb-4">
          <h4 class="h6 fw-bold text-secondary mb-3">Vận chuyển</h4>
          <div class="mb-3">
            <label class="form-label small text-dark fw-medium">Địa chỉ lấy hàng</label>
            <input type="text" v-model="form.shippingAddress" class="form-control border-secondary">
          </div>
          <div class="mb-4">
            <label class="form-label small text-dark fw-medium d-block mb-2">Phí vận chuyển</label>
            <div class="form-check form-check-inline">
              <input class="form-check-input border-secondary" type="radio" v-model="form.shippingPayer" value="buyer" id="buyer">
              <label class="form-check-label small" for="buyer">Người mua trả</label>
            </div>
            <div class="form-check form-check-inline">
              <input class="form-check-input border-secondary" type="radio" v-model="form.shippingPayer" value="seller" id="seller">
              <label class="form-check-label small" for="seller">Người bán trả</label>
            </div>
          </div>

          <h4 class="h6 fw-bold text-secondary mb-3 mt-4">Kích thước</h4>
          <div class="row g-3 mb-4">
            <div class="col-6 col-md-3">
              <label class="form-label small text-dark fw-medium">Chiều dài (cm)</label>
              <input type="number" v-model="form.dimensions.length" class="form-control border-secondary">
            </div>
            <div class="col-6 col-md-3">
              <label class="form-label small text-dark fw-medium">Chiều rộng (cm)</label>
              <input type="number" v-model="form.dimensions.width" class="form-control border-secondary">
            </div>
            <div class="col-6 col-md-3">
              <label class="form-label small text-dark fw-medium">Chiều cao (cm)</label>
              <input type="number" v-model="form.dimensions.height" class="form-control border-secondary">
            </div>
            <div class="col-6 col-md-3">
              <label class="form-label small text-dark fw-medium">Cân nặng (g)</label>
              <input type="number" v-model="form.weight" class="form-control border-secondary">
            </div>
          </div>

          <h4 class="h6 fw-bold text-secondary mb-3 mt-4">Tình trạng</h4>
          <div class="row g-2">
            <div class="col-12 col-md" v-for="cond in conditions" :key="cond.title">
              <input type="radio" class="btn-check" :id="'cond-'+cond.title" :value="cond.title" v-model="form.condition">
              <label class="btn btn-outline-dark w-100 h-100 p-3 text-center d-flex flex-column align-items-center justify-content-center" :for="'cond-'+cond.title">
                <span class="fw-bold small mb-2 d-block w-100 border-bottom border-secondary pb-1">{{ cond.title }}</span>
                <span style="font-size: 11px; line-height: 1.2;" class="opacity-75">{{ cond.desc }}</span>
              </label>
            </div>
          </div>
        </div>

        <div class="mt-5 pt-4 border-top d-flex justify-content-center gap-3">
          <button type="button" class="btn btn-outline-dark fw-medium px-4 bg-white"><router-link to="/">Trở lại</router-link></button>
          <button type="submit" class="btn btn-dark fw-medium px-5" :disabled="isSubmitting">
            {{ isSubmitting ? 'Đang xử lý...' : 'Đăng' }}
          </button>
        </div>
      </form>
    </main>

    <div v-if="showOtpModal" class="modal fade show d-block" tabindex="-1" style="background: rgba(0,0,0,0.6);">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow">
          <div class="modal-header border-bottom">
            <h5 class="modal-title fw-bold">Xác minh số điện thoại</h5>
            <button type="button" class="btn-close" @click="showOtpModal = false"></button>
          </div>
          <div class="modal-body p-4">
            <p class="text-muted small mb-4">Bạn cần xác minh SĐT để bảo vệ an toàn cho các giao dịch.</p>
            <div class="mb-4">
              <label class="form-label fw-bold small text-dark">Số điện thoại</label>
              <div class="input-group">
                <input type="text" v-model="form.phone" placeholder="Ví dụ: 0987654321" class="form-control border-secondary">
                <button type="button" @click="requestOtp" :disabled="isRequestingOtp" class="btn btn-dark">
                  {{ isRequestingOtp ? 'Đang gửi...' : 'Lấy mã' }}
                </button>
              </div>
            </div>
            <div id="recaptcha-container" class="d-flex justify-content-center mb-3"></div>
            <div v-if="confirmationResult" class="mb-3">
              <label class="form-label fw-bold text-success small">Mã OTP (có hiệu lực 5 phút)</label>
              <input type="text" v-model="form.otpCode" placeholder="Nhập 6 số..." class="form-control border-success text-center fw-bold fs-4" style="letter-spacing: 0.3em;">
            </div>
          </div>
          <div class="modal-footer border-top bg-light justify-content-end gap-2 p-3">
            <button type="button" @click="showOtpModal = false" class="btn btn-outline-secondary bg-white px-4">Hủy</button>
            <button type="button" @click="confirmAndSubmit" class="btn btn-dark px-4" :disabled="isSubmitting">
              {{ isSubmitting ? 'Đang xử lý...' : 'Xác nhận & Đăng' }}
            </button>
          </div>
        </div>
      </div>
    </div>
    
  </div>
</template>

<script setup>
import { reactive, ref, nextTick, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router'; 
import { initializeApp } from "firebase/app";
import { getAuth, RecaptchaVerifier, signInWithPhoneNumber } from "firebase/auth";
import AppHeader from '@/layouts/Header.vue';

// 🔥 Ổ KHÓA CHỐNG DOUBLE-CLICK (CHỐNG ĐẺ SINH ĐÔI)
const isSubmitting = ref(false);

const router = useRouter();
let currentUserId = null;

const errors = reactive({
  category: '', name: '', price: '', images: '',
  shippingAddress: '', length: '', width: '', height: '', weight: ''
});

const categories = ref([]);

const flattenedCategories = computed(() => {
  const result = [];
  const flatten = (cats, prefix = '') => {
    if (!cats) return;
    for (const cat of cats) {
      result.push({
        id: cat.danhMucId || cat.id,
        name: prefix + (cat.tenDanhMuc || cat.name)
      });
      if (cat.children && cat.children.length > 0) {
        flatten(cat.children, prefix + '- '); 
      }
    }
  };
  flatten(categories.value);
  return result;
});

const fetchCategories = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/categories/tree'); 
    if (response.ok) {
      categories.value = await response.json();
    }
  } catch (error) {
    console.error("Lỗi khi lấy danh sách danh mục:", error);
  }
};

onMounted(async () => {
  const storedUserStr = localStorage.getItem('user');
  if (!storedUserStr) {
    alert("Vui lòng đăng nhập!");
    router.push('/login'); 
    return;
  }
  
  const storedUser = JSON.parse(storedUserStr);
  currentUserId = storedUser.id || storedUser.nguoiDungId;

  try {
    const token = storedUser.token || storedUser.accessToken || localStorage.getItem('token');
    const response = await fetch(`http://localhost:8080/api/nguoi-dung/${currentUserId}`, {
      headers: { 'Authorization': `Bearer ${token}` }
    });
    
    if (response.ok) {
      const dbUser = await response.json();
      storedUser.soDienThoai = dbUser.soDienThoai;
      localStorage.setItem('user', JSON.stringify(storedUser));
    }
  } catch (error) {
    console.error("Lỗi đồng bộ:", error);
  }

  fetchCategories();
});

const firebaseConfig = {
  apiKey: "AIzaSyCy2BDZO1nKsU43fl8LqdAgix92z_G-26E",
  authDomain: "old2new-e8ef2.firebaseapp.com",
  projectId: "old2new-e8ef2",
  storageBucket: "old2new-e8ef2.firebasestorage.app",
  messagingSenderId: "761431606910",
  appId: "1:761431606910:web:379c565c0e096d16a2fa2f",
  measurementId: "G-VW201BGYC5"
};

const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
auth.languageCode = 'vi'; 

const form = reactive({
  category: '', name: '', price: null, description: '',
  shippingAddress: '', shippingPayer: 'buyer', 
  dimensions: { length: null, width: null, height: null },
  weight: null, condition: 'Mới', phone: '', otpCode: ''
});

const fileInput = ref(null); 
const selectedFiles = ref([]); 
const previewImages = ref([]); 

const triggerFileInput = () => {
  if (fileInput.value) fileInput.value.click();
};

const handleFileChange = (event) => {
  const files = Array.from(event.target.files);
  files.forEach(file => {
    selectedFiles.value.push(file); 
    previewImages.value.push(URL.createObjectURL(file)); 
  });
  event.target.value = ''; 
};

const removeImage = (index) => {
  selectedFiles.value.splice(index, 1);
  previewImages.value.splice(index, 1);
};

const showOtpModal = ref(false);
const isRequestingOtp = ref(false);
const confirmationResult = ref(null); 

const conditions = [
  { value: 1, title: 'Mới', desc: 'Hàng mới kèm mác, chưa mở hộp/bao bì, chưa qua sử dụng.' },
  { value: 2, title: 'Như mới', desc: 'Hàng mới kèm mác, đã mở bao bì/hộp, chưa qua sử dụng.' },
  { value: 3, title: 'Tốt', desc: 'Đã qua sử dụng, tính năng đầy đủ, hoạt động tốt.' },
  { value: 4, title: 'Trung bình', desc: 'Hàng đã qua sử dụng, đầy đủ chức năng. Lỗi nhỏ.' },
  { value: 5, title: 'Kém', desc: 'Đã qua sử dụng. Nhiều sai sót. Có thể bị hư hỏng.' }
];

const validateForm = () => {
  let isValid = true;
  Object.keys(errors).forEach(key => errors[key] = '');

  if (selectedFiles.value.length === 0) { errors.images = 'Vui lòng tải ảnh!'; isValid = false; }
  if (!form.category) { errors.category = 'Chọn danh mục!'; isValid = false; }
  if (!form.name) { errors.name = 'Nhập tên!'; isValid = false; }
  if (!form.price || form.price < 10000) { errors.price = 'Giá >= 10k!'; isValid = false; }

  if (!isValid) window.scrollTo({ top: 0, behavior: 'smooth' });
  return isValid;
};

// ==========================================
// 🔥 LUỒNG GỌI API ĐĂNG BÁN ĐÃ ĐƯỢC BỌC Ổ KHÓA
// ==========================================

const submitForm = async () => {
  if (!validateForm()) return;

  // 🔒 Kiểm tra xem ổ khóa có đang đóng không. Nếu đang đóng (đang xử lý) thì chặn click tiếp
  if (isSubmitting.value) return; 

  const isVerified = localStorage.getItem('isPhoneVerified') === 'true'; 
  const storedUser = JSON.parse(localStorage.getItem('user') || '{}');
  const hasPhone = !!(storedUser.soDienThoai || localStorage.getItem('verifiedPhone'));

  if (isVerified || hasPhone) {
    // ✅ TH 1: Đã xác minh -> Đóng khóa và gọi hàm đăng
    isSubmitting.value = true;
    await confirmAndSubmitNoOtp(); 
    // Mở khóa được xử lý bên trong hàm confirmAndSubmitNoOtp
  } else {
    // ❌ TH 2: Chưa xác minh -> Mở Modal OTP
    showOtpModal.value = true;
    await nextTick();
    setupRecaptcha();
  }
};

// Hàm đăng bán TRỰC TIẾP (Không cần OTP)
const confirmAndSubmitNoOtp = async () => {
  try {
    const storedUser = localStorage.getItem('user');
    const jwtToken = JSON.parse(storedUser).token || JSON.parse(storedUser).accessToken;
    const verifiedPhone = localStorage.getItem('verifiedPhone') || form.phone;

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
      soDienThoai: verifiedPhone
    };

    const response = await fetch('http://localhost:8080/api/products', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${jwtToken}` },
      body: JSON.stringify(payload)
    });

    if (response.ok) {
      const data = await response.json();
      const newId = data.sanPhamId || data.id;
      if (selectedFiles.value.length > 0) {
        const formData = new FormData();
        selectedFiles.value.forEach(file => formData.append('files', file));
        await fetch(`http://localhost:8080/api/products/${newId}/hinh-anh`, {
          method: 'POST',
          headers: { 'Authorization': `Bearer ${jwtToken}` },
          body: formData
        });
      }
      
      alert("🎉 Đăng tin thành công!\nSản phẩm của bạn đang chờ Admin kiểm duyệt.");
      router.push('/'); 
    }
  } catch (error) {
    console.error("Lỗi:", error);
    alert("Có lỗi xảy ra khi đăng bài!");
  } finally {
    // 🔓 Dù lỗi hay thành công cũng phải mở khóa nút bấm
    isSubmitting.value = false;
  }
};

const setupRecaptcha = () => {
  if (!window.recaptchaVerifier) {
    window.recaptchaVerifier = new RecaptchaVerifier(auth, 'recaptcha-container', {
      'size': 'normal'
    });
    window.recaptchaVerifier.render();
  }
};

const requestOtp = async () => {
  if (!form.phone) return alert("Vui lòng nhập số điện thoại!");
  let cleanPhone = form.phone.replace(/\D/g, ''); 
  let phoneNumber = cleanPhone.startsWith('0') ? '+84' + cleanPhone.substring(1) : (cleanPhone.startsWith('84') ? '+' + cleanPhone : '+84' + cleanPhone);

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

// Hàm đăng bán SAU KHI XÁC NHẬN OTP
const confirmAndSubmit = async () => {
  if (!form.otpCode || !confirmationResult.value) return alert("Vui lòng nhập mã OTP để xác nhận!");
  
  // 🔒 Kiểm tra ổ khóa
  if (isSubmitting.value) return;

  isSubmitting.value = true; // Đóng khóa nút bấm
  
  try {
    const result = await confirmationResult.value.confirm(form.otpCode);
    const firebaseToken = await result.user.getIdToken();

    const storedUser = localStorage.getItem('user');
    const jwtToken = JSON.parse(storedUser).token || JSON.parse(storedUser).accessToken;

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
      firebaseToken: firebaseToken 
    };

    const response = await fetch('http://localhost:8080/api/products', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${jwtToken}` },
      body: JSON.stringify(payload)
    });

    if (response.ok) {
      const data = await response.json();
      const newId = data.sanPhamId || data.id;
      if (selectedFiles.value.length > 0) {
        const formData = new FormData();
        selectedFiles.value.forEach(file => formData.append('files', file));
        await fetch(`http://localhost:8080/api/products/${newId}/hinh-anh`, {
          method: 'POST',
          headers: { 'Authorization': `Bearer ${jwtToken}` },
          body: formData
        });
      }
      
      alert("🎉 Đăng tin thành công!\nSản phẩm của bạn đang chờ Admin kiểm duyệt.");
      showOtpModal.value = false;
      router.push('/'); 
    } else {
      const errorText = await response.text();
      alert("❌ Lỗi đăng bán: " + errorText);
    }
  } catch (error) {
    console.error(error);
    alert("Mã OTP bạn nhập không đúng hoặc đã hết hạn!");
  } finally {
    // 🔓 Dù lỗi hay thành công cũng phải mở khóa nút bấm
    isSubmitting.value = false;
  }
};
</script>