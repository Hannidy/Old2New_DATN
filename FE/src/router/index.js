import { createRouter, createWebHistory } from 'vue-router'

// Import các trang của bạn
import Login from '../views/auth/Login.vue' 

import Register from '../views/auth/Register.vue' 

import ForgotPassword from '../views/auth/ForgotPassword.vue'

import HomeView from '../views/home/HomeView.vue' 

import Users from '../views/admin/Users.vue'

import Sell from '../views/product/Sell.vue'

import QuanLyDonHang from '@/views/order/QuanLyDonHang.vue'

import QuanLyDonBan from '@/views/order/QuanLyDonBan.vue'

import ProductDetail from '../views/product/ProductDetail.vue'

import Cart from '../views/product/Cart.vue'

import Profile from '../views/user/Profile.vue'

import ShopView from '../views/product/ShopView.vue'

import MyProductView from '../views/product/MyProductView.vue'
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
    },
    {
      path: '/product/:id',
      name: 'ProductDetail',
      component: ProductDetail
    },
    {
      path: '/cart',
      name: 'Cart',
      component: Cart
    },
    {
      path: '/quan-ly-don-hang',
      name: 'QuanLyDonHang',
      component: QuanLyDonHang 
    },
    {
      path: '/quan-ly-don-ban',
      name: 'QuanLyDonBan',
      component: QuanLyDonBan
    },
    {
      path: '/profile',
      name: 'Profile',
      component: Profile
    },
    // Thêm vào mảng routes trong file router/index.js hoặc tương đương
    {
      path: '/shop/:id',
      name: 'Shop',
      component: ShopView // Đảm bảo đường dẫn file đúng
    },
    // Thêm vào mảng routes của bạn
  {
    path: '/my-products',
    name: 'MyProducts',
    component: MyProductView 
  }
  ]
})

export default router