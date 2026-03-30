import { createRouter, createWebHistory } from 'vue-router'

// Import các trang chính
import Login from '../views/auth/Login.vue' 
import Register from '../views/auth/Register.vue' 
import ForgotPassword from '../views/auth/ForgotPassword.vue'
import HomeView from '../views/home/HomeView.vue' 
import Sell from '../views/product/Sell.vue'
import QuanLyDonHang from '@/views/order/QuanLyDonHang.vue'
import QuanLyDonBan from '@/views/order/QuanLyDonBan.vue'
import ProductDetail from '../views/product/ProductDetail.vue'
import Cart from '../views/product/Cart.vue'
import Profile from '../views/user/Profile.vue'
import ShopView from '../views/product/ShopView.vue'
import MyProductView from '../views/product/MyProductView.vue'

// Import các thành phần Admin
import AdminLayout from '@/layouts/AdminLayout.vue';
import Users from '../views/admin/Users.vue'
import Categories from '../views/admin/Categories.vue'
import VerifyProducts from '../views/admin/VerifyProducts.vue'
import Payments from '@/views/admin/Payments.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    // ==========================================
    // CỤM ROUTE ADMIN (LỒNG NHAU)
    // ==========================================
    {
      path: '/admin',
      component: AdminLayout, // Đây là cái "khung" chứa Sidebar
      redirect: '/admin/thong-ke', // Khi vào /admin sẽ tự nhảy tới thống kê
      children: [
        {
          path: 'users',
          name: 'admin-users',
          component: Users,
          meta: { title: 'Quản lý người dùng' }
        },
        {
          path: 'categories',
          name: 'admin-categories',
          component: Categories,
          meta: { title: 'Quản lý danh mục' }
        },
        {
          path: 'thong-ke',
          name: 'admin-dashboard',
          // Duy có thể tạo file Dashboard.vue sau, giờ cứ dùng trang tạm
          component: () => import('@/views/admin/Dashboard.vue'), 
          meta: { title: 'Thống kê tổng quan' }
        },
        {
          path: 'verify-products',
          name: 'admin-verify',
          component: VerifyProducts,
          meta: { title: 'Kiểm duyệt tin đăng' }
        },
        {
        path: 'payments', 
        name: 'admin-paymants',
        component: Payments,
        meta: { title: 'Đối Xoát Thanh Toán' }
       }
      ]
    },
    // ==========================================
    // CÁC ROUTE NGƯỜI DÙNG KHÁC
    // ==========================================
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
    {
      path: '/shop/:id',
      name: 'Shop',
      component: ShopView 
    },
    {
      path: '/my-products',
      name: 'MyProducts',
      component: MyProductView 
    }
  ]
})

// Thêm "Bảo vệ cổng" (Navigation Guard) cho Router
router.beforeEach((to, from, next) => {
  // Lấy thông tin user từ LocalStorage
  const storedUser = localStorage.getItem('user');
  let userRole = null;

  if (storedUser) {
    const user = JSON.parse(storedUser);
    // Lưu ý: Tên biến 'vaiTro' hoặc 'role' phụ thuộc vào cách bạn lưu lúc Login
    userRole = user.vaiTro || user.role || user.quyen; 
  }

  // Nếu người dùng cố tình truy cập vào đường dẫn bắt đầu bằng "/admin"
  if (to.path.startsWith('/admin')) {
    
    // Nếu chưa đăng nhập, hoặc đăng nhập rồi mà vai trò là USER
    if (!storedUser || userRole !== 'ADMIN') {
      alert("⛔ CẢNH BÁO: Bạn không có quyền truy cập khu vực Quản trị!");
      next('/'); // Đá thẳng về trang chủ
    } else {
      next(); // Là ADMIN thì mở cửa cho vào
    }
    
  } else {
    next(); // Với các trang khác (Trang chủ, Giỏ hàng, Hồ sơ) thì cho đi bình thường
  }
});

export default router