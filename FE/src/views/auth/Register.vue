<template>
  <div class="auth-container">
    <div class="auth-box">
      <h2>Đăng Ký Tài Khoản</h2>
      <p class="subtitle">Tham gia nền tảng OLD2NEW ngay hôm nay!</p>

      <form @submit.prevent="handleRegister" class="auth-form">
        <div class="form-group">
          <label>Họ và Tên</label>
          <input type="text" v-model="form.hoVaTen" required placeholder="Nhập họ và tên của bạn" />
        </div>

        <div class="form-group">
          <label>Số điện thoại</label>
          <input type="text" v-model="form.soDienThoai" required placeholder="Nhập số điện thoại" />
        </div>

        <div class="form-group">
          <label>Email</label>
          <input type="email" v-model="form.email" required placeholder="Nhập địa chỉ email" />
        </div>

        <div class="form-group">
          <label>Mật khẩu</label>
          <input type="password" v-model="form.matKhau" required placeholder="Tạo mật khẩu" />
        </div>

        <button type="submit" class="btn-primary" :disabled="isLoading">
          {{ isLoading ? 'Đang xử lý...' : 'Đăng Ký' }}
        </button>
      </form>

      <div class="auth-links">
        <p>Đã có tài khoản? <router-link to="/login">Đăng nhập ngay</router-link></p>
      </div>

      <!-- Đăng ký băng google -->
        <div class="divider">
          <span>Hoặc</span>
        </div>

        <div class="google-btn-wrapper">
          <GoogleLogin :callback="handleGoogleLogin" />
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

const form = ref({
  hoVaTen: '',
  soDienThoai: '',
  email: '',
  matKhau: ''
});

const handleRegister = async () => {
  isLoading.value = true;
  try {
    // Gọi API Đăng ký từ Spring Boot
    const response = await axios.post('http://localhost:8080/api/auth/register', form.value);
    
    // Spring Boot trả về chuỗi String (thành công hoặc lỗi)
    alert(response.data); 

    if (response.data === "Đăng ký tài khoản thành công!") {
      // Đăng ký thành công thì chuyển hướng sang trang Đăng nhập
      router.push('/login');
    }
  } catch (error) {
    console.error(error);
    alert('Lỗi kết nối đến máy chủ!');
  } finally {
    isLoading.value = false;
  }
};

// Hàm xử lý Đăng nhập Google hoàn chỉnh
const handleGoogleLogin = async (googleResponse) => {
  isLoading.value = true;
  try {
    // 1. Lấy mã Token do Google cấp
    const token = googleResponse.credential;
    
    // 2. Gửi mã Token này xuống cho Spring Boot kiểm tra
    const response = await axios.post('http://localhost:8080/api/auth/google', {
      credential: token
    });

    // 3. Xử lý kết quả trả về (Giống hệt như Đăng nhập Local)
    if (response.data.thongBao === "Thành công") {
      alert("Đăng nhập Google thành công! Chào " + response.data.hoVaTen);
      
      const role = response.data.tenVaiTro;
      
      // Lưu vào cuốn sổ LocalStorage để F5 không bị văng
      localStorage.setItem('user', JSON.stringify({
        id: response.data.nguoiDungId,
        hoVaTen: response.data.hoVaTen,
        email: response.data.email,
        vaiTro: role
      }));
      
      // Phân quyền chuyển trang
      if (role === 'ADMIN') {
        router.push('/admin/users');
      } else if (role === 'SELLER') {
        router.push('/seller/dashboard');
      } else {
        router.push('/');
      }
      
    } else {
      alert(response.data.thongBao); // Báo lỗi nếu mã Google không hợp lệ
    }
  } catch (error) {
    console.error("Lỗi gọi API Google:", error);
    alert('Lỗi kết nối đến máy chủ khi đăng nhập Google!');
  } finally {
    isLoading.value = false;
  }
};

</script>

<style scoped>
/* Dùng chung CSS cho cả 2 trang cho đồng bộ */
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f0f2f5;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}
.auth-box {
  background: white;
  padding: 2.5rem;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0,0,0,0.1);
  width: 100%;
  max-width: 450px;
}
.auth-box h2 {
  text-align: center;
  margin-bottom: 0.5rem;
  color: #333;
}
.subtitle {
  text-align: center;
  color: #666;
  margin-bottom: 2rem;
}
.form-group {
  margin-bottom: 1.2rem;
}
.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #444;
}
.form-group input {
  width: 100%;
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 1rem;
  box-sizing: border-box; /* Sửa lỗi tràn viền */
}
.form-group input:focus {
  border-color: #007bff;
  outline: none;
}
.btn-primary {
  width: 100%;
  padding: 12px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  margin-top: 1rem;
}
.btn-primary:hover {
  background-color: #0056b3;
}
.btn-primary:disabled {
  background-color: #99c2ff;
  cursor: not-allowed;
}
.auth-links {
  margin-top: 1.5rem;
  text-align: center;
  font-size: 0.95rem;
}
.auth-links a {
  color: #007bff;
  text-decoration: none;
  font-weight: 600;
}

/* Đăng nhập gg */
.divider { text-align: center; margin: 1.5rem 0; position: relative; }
.divider::before { content: ""; position: absolute; left: 0; top: 50%; width: 40%; height: 1px; background: #ddd; }
.divider::after { content: ""; position: absolute; right: 0; top: 50%; width: 40%; height: 1px; background: #ddd; }
.divider span { color: #888; font-size: 0.9rem; background: white; padding: 0 10px; }
.google-btn-wrapper { display: flex; justify-content: center; }

</style>