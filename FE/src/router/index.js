import { createRouter, createWebHistory } from 'vue-router'

// Import các trang của bạn
import Login from '../views/auth/Login.vue' // Cập nhật đường dẫn theo thư mục thực tế của bạn
import Register from '../views/auth/Register.vue' // Cập nhật đường dẫn theo thư mục thực tế của bạn
import ForgotPassword from '../views/auth/ForgotPassword.vue'
// Import trang chủ (nếu bạn chưa có file này thì tạo tạm nhé)
import HomeView from '../views/home/HomeView.vue' 

import Users from '../views/admin/Users.vue'

// Thêm dòng này vào khu vực import
import Sell from '../views/product/Sell.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path: '/register',
      name: 'register',
      component: Register
    },
    {
      path: '/forgot-password',
      name: 'forgot-password',
      component: ForgotPassword
    },
    {
      path: '/admin/users',
      name: 'admin-users',
      component: Users
    },
    {
      path: '/dang-ban',
      name: 'Sell',
      component: Sell
    }
  ]
})

export default router