import { createRouter, createWebHistory } from "vue-router";

// --- IMPORT CÁC TRANG CHÍNH ---
import HomeView from "../views/home/HomeView.vue";
import Login from "../views/auth/Login.vue";
import Register from "../views/auth/Register.vue";
import ForgotPassword from "../views/auth/ForgotPassword.vue";
import Profile from "../views/user/Profile.vue";

// --- IMPORT CÁC TRANG SẢN PHẨM & SHOP ---
import ShopView from "../views/product/ShopView.vue";
import ProductDetail from "../views/product/ProductDetail.vue";
import Sell from "../views/product/Sell.vue";
import MyProductView from "../views/product/MyProductView.vue";
import ShopProfile from "@/views/shop/ShopProfile.vue";

// --- IMPORT CÁC TRANG ĐƠN HÀNG & GIỎ HÀNG ---
import Cart from "../views/cart/Cart.vue";
import QuanLyDonHang from "@/views/order/QuanLyDonHang.vue";
import QuanLyDonBan from "@/views/order/QuanLyDonBan.vue";

// --- IMPORT CÁC TRANG GIAO DỊCH / THANH TOÁN ---
import Payment from "../views/paymentQR/payment.vue";
import WithdrawMoney from "../views/paymentQR/withdrawmoney.vue";

// --- IMPORT CÁC TRANG CHAT ---
import ChatView from "@/views/chat/ChatView.vue";

// --- IMPORT CÁC THÀNH PHẦN ADMIN ---
import AdminLayout from "@/layouts/AdminLayout.vue";
import Users from "../views/admin/Users.vue";
import Categories from "../views/admin/Categories.vue";
import VerifyProducts from "../views/admin/VerifyProducts.vue";
import Payments from "@/views/admin/Payments.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
    },

    // --- CỤM ROUTE ADMIN (LỒNG NHAU) ---
    {
      path: "/admin",
      component: AdminLayout,
      redirect: "/admin/thong-ke",
      children: [
        {
          path: "users",
          name: "admin-users",
          component: Users,
          meta: { title: "Quản lý người dùng" },
        },
        {
          path: "categories",
          name: "admin-categories",
          component: Categories,
          meta: { title: "Quản lý danh mục" },
        },
        {
          path: "thong-ke",
          name: "admin-dashboard",
          component: () => import("@/views/admin/Dashboard.vue"),
          meta: { title: "Thống kê tổng quan" },
        },
        {
          path: "verify-products",
          name: "admin-verify",
          component: VerifyProducts,
          meta: { title: "Kiểm duyệt tin đăng" },
        },
        {
          path: "payments",
          name: "admin-paymants",
          component: Payments,
          meta: { title: "Đối Soát Thanh Toán" },
        },
        {
          path: "chat-support",
          name: "admin-chat",
          component: () => import("@/views/chat/ChatView.vue"), // Dùng lại trang ChatView cũ
          meta: { title: "Tin nhắn hỗ trợ" },
        },
      ],
    },

    // --- CỤM ROUTE XÁC THỰC (AUTH) ---
    { path: "/login", name: "login", component: Login },
    { path: "/register", name: "register", component: Register },
    { path: "/forgot-password", name: "forgot-password", component: ForgotPassword },

    // --- CỤM ROUTE SẢN PHẨM & GIAN HÀNG ---
    { path: "/dang-ban", name: "Sell", component: Sell },
    { path: "/product/:id", name: "ProductDetail", component: ProductDetail },
    { path: "/shop/:id", name: "Shop", component: ShopView },
    { path: "/my-products", name: "MyProducts", component: MyProductView },
    { path: "/shop-profile", name: "ShopProfile", component: ShopProfile },
    { path: "/profile", name: "Profile", component: Profile },

    // --- CỤM ROUTE ĐƠN HÀNG & GIỎ HÀNG ---
    { path: "/cart", name: "Cart", component: Cart },
    { path: "/quan-ly-don-hang", name: "QuanLyDonHang", component: QuanLyDonHang },
    { path: "/quan-ly-don-ban", name: "QuanLyDonBan", component: QuanLyDonBan },

    // --- CỤM ROUTE THANH TOÁN ---
    { path: "/thanh-toan/:id", name: "Payment", component: Payment },
    { path: "/rut-tien", name: "WithdrawMoney", component: WithdrawMoney },

    // --- CỤM ROUTE CHAT (CHỈ GIỮ LẠI 1 CÁI DUY NHẤT) ---
    {
      path: "/chat",
      name: "Chat",
      component: ChatView,
      meta: { requiresAuth: true },
    },
  ],
});

// --- BẢO VỆ CỔNG (NAVIGATION GUARD) ---
router.beforeEach((to, from, next) => {
  const storedUser = localStorage.getItem("user");
  let userRole = null;

  if (storedUser) {
    const user = JSON.parse(storedUser);
    userRole = user.vaiTro || user.role || user.quyen;
  }

  if (to.path.startsWith("/admin")) {
    if (!storedUser || userRole !== "ADMIN") {
      alert("⛔ CẢNH BÁO: Bạn không có quyền truy cập khu vực Quản trị!");
      next("/");
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router;