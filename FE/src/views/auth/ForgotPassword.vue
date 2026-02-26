<template>
  <div class="auth-container">
    <div class="auth-box">
      <h2>Quên Mật Khẩu</h2>
      <p class="subtitle">Khôi phục quyền truy cập tài khoản</p>

      <form v-if="!isOtpSent" @submit.prevent="handleSendOtp" class="auth-form">
        <p style="font-size: 0.9rem; color: #555; margin-bottom: 1rem; text-align: center;">
          Vui lòng nhập địa chỉ email bạn đã đăng ký. Chúng tôi sẽ gửi một mã OTP gồm 6 chữ số đến email của bạn.
        </p>

        <div class="form-group">
          <label>Email của bạn</label>
          <input type="email" v-model="form.email" required placeholder="Nhập địa chỉ email" />
        </div>

        <button type="submit" class="btn-primary" :disabled="isLoading">
          {{ isLoading ? 'Đang gửi mã...' : 'Gửi mã OTP' }}
        </button>
      </form>

      <form v-else @submit.prevent="handleResetPassword" class="auth-form">
        <p style="font-size: 0.9rem; color: #28a745; margin-bottom: 1rem; text-align: center; font-weight: bold;">
          Mã OTP đã được gửi đến: {{ form.email }}
        </p>

        <div class="form-group">
          <label>Mã xác nhận (OTP)</label>
          <input type="text" v-model="form.otp" required placeholder="Nhập mã 6 số trong email" maxlength="6" />
        </div>

        <div class="form-group">
          <label>Mật khẩu mới</label>
          <input type="password" v-model="form.matKhauMoi" required placeholder="Nhập mật khẩu mới" />
        </div>

        <button type="submit" class="btn-primary" :disabled="isLoading">
          {{ isLoading ? 'Đang xử lý...' : 'Xác nhận đổi mật khẩu' }}
        </button>
        
        <button type="button" @click="isOtpSent = false" class="btn-secondary" style="margin-top: 10px;">
          Quay lại nhập Email
        </button>
      </form>

      <div class="auth-links">
        <p>Nhớ ra mật khẩu rồi? <router-link to="/login">Đăng nhập</router-link></p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();
const isLoading = ref(false);
const isOtpSent = ref(false); // Biến để chuyển đổi giữa Bước 1 và Bước 2

const form = ref({
  email: '',
  otp: '',
  matKhauMoi: ''
});

// Hàm gọi API gửi mã OTP (Bước 1)
const handleSendOtp = async () => {
  isLoading.value = true;
  try {
    const response = await axios.post('http://localhost:8080/api/auth/forgot-password', {
      email: form.value.email
    });

    if (response.data.includes("Thành công")) {
      alert("Đã gửi mã OTP thành công! Vui lòng kiểm tra hộp thư đến (hoặc thư rác) của bạn.");
      isOtpSent.value = true; // Chuyển sang giao diện nhập OTP
    } else {
      alert(response.data); // Báo lỗi nếu email chưa đăng ký
    }
  } catch (error) {
    console.error("Lỗi:", error);
    alert('Không thể kết nối đến máy chủ!');
  } finally {
    isLoading.value = false;
  }
};

// Hàm gọi API xác nhận đổi mật khẩu (Bước 2)
const handleResetPassword = async () => {
  isLoading.value = true;
  try {
    const response = await axios.post('http://localhost:8080/api/auth/reset-password', form.value);

    if (response.data.includes("Thành công")) {
      alert("Đổi mật khẩu thành công! Hãy đăng nhập bằng mật khẩu mới nhé.");
      router.push('/login'); // Đổi pass xong thì đẩy về trang Login
    } else {
      alert(response.data); // Báo lỗi sai mã, hết hạn mã...
    }
  } catch (error) {
    console.error("Lỗi:", error);
    alert('Không thể kết nối đến máy chủ!');
  } finally {
    isLoading.value = false;
  }
};
</script>

<style scoped>
/* Copy y nguyên style từ trang Login sang cho đồng bộ */
.auth-container { display: flex; justify-content: center; align-items: center; min-height: 100vh; background-color: #f0f2f5; font-family: 'Segoe UI', sans-serif; }
.auth-box { background: white; padding: 2.5rem; border-radius: 12px; box-shadow: 0 8px 24px rgba(0,0,0,0.1); width: 100%; max-width: 450px; }
.auth-box h2 { text-align: center; margin-bottom: 0.5rem; color: #333; }
.subtitle { text-align: center; color: #666; margin-bottom: 2rem; }
.form-group { margin-bottom: 1.2rem; }
.form-group label { display: block; margin-bottom: 0.5rem; font-weight: 500; color: #444; }
.form-group input { width: 100%; padding: 10px 15px; border: 1px solid #ddd; border-radius: 6px; font-size: 1rem; box-sizing: border-box; }
.form-group input:focus { border-color: #007bff; outline: none; }
.btn-primary { width: 100%; padding: 12px; background-color: #007bff; color: white; border: none; border-radius: 6px; font-size: 1.1rem; font-weight: 600; cursor: pointer; margin-top: 1rem; }
.btn-primary:hover { background-color: #0056b3; }
.btn-primary:disabled { background-color: #99c2ff; cursor: not-allowed; }
.btn-secondary { width: 100%; padding: 10px; background-color: transparent; color: #6c757d; border: 1px solid #6c757d; border-radius: 6px; font-weight: 600; cursor: pointer; }
.btn-secondary:hover { background-color: #f8f9fa; }
.auth-links { margin-top: 1.5rem; text-align: center; font-size: 0.95rem; }
.auth-links a { color: #007bff; text-decoration: none; font-weight: 600; }
</style>