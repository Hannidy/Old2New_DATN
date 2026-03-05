import { createApp } from 'vue'
import App from './App.vue'
import router from './router' // Đảm bảo đường dẫn này đúng
// 1. Import thư viện Google Login
import vue3GoogleLogin from 'vue3-google-login'

import axios from 'axios';


import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'


// 1. Tạo app trước
const app = createApp(App)

// 2. Cài đặt Router VÀO app (Phải làm bước này trước khi mount)
app.use(router)

// 3. Cuối cùng mới Mount (Chạy app)
app.mount('#app')

// 2. Cấu hình thư viện với mã Client ID của bạn
app.use(vue3GoogleLogin, {
  clientId: '140844929925-l3ejokfmnsv3n3nui1rvakjqbpvt2ghb.apps.googleusercontent.com'
})



// ==========================================
// CẤU HÌNH LỄ TÂN AXIOS (TỰ ĐỘNG GẮN TOKEN)
// ==========================================
axios.interceptors.request.use(
  (config) => {
    // Chỉ gắn Token cho API gọi về Spring Boot
    if (config.url && config.url.includes('localhost:8080')) {
      
      // 1. Lấy cục data 'user' từ LocalStorage
      const userStr = localStorage.getItem('user');
      
      if (userStr) {
        // 2. Dịch nó từ chuỗi JSON sang Object
        const userData = JSON.parse(userStr);
        
        // 3. Móc cái token từ trong bụng nó ra 
        // (Tùy backend của bạn trả về tên là gì, thường là 'token' hoặc 'accessToken')
        const token = userData.token || userData.accessToken; 
        
        // 4. Gắn thẻ VIP vào Header
        if (token) {
          config.headers['Authorization'] = `Bearer ${token}`;
        }
      }
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);