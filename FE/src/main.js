import { createApp } from 'vue'
import App from './App.vue'
import router from './router' // Đảm bảo đường dẫn này đúng
// 1. Import thư viện Google Login
import vue3GoogleLogin from 'vue3-google-login'


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
  clientId: '904446046208-auq2g5onijmpe413ac5t1qctoj27lmbn.apps.googleusercontent.com'
})