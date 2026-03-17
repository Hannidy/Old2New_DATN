<template>
  <div class="dashboard-page">
    <div class="row g-4 mb-4">
      <div class="col-md-3">
        <div class="card border-0 shadow-sm rounded-4 p-3 bg-primary text-white">
          <div class="d-flex align-items-center">
            <div class="icon-box bg-white bg-opacity-25 rounded-3 p-3 me-3">
              <i class="bi bi-people fs-3"></i>
            </div>
            <div>
              <h6 class="mb-0 opacity-75">Người dùng</h6>
              <h3 class="fw-bold mb-0">{{ stats.totalUsers }}</h3>
            </div>
          </div>
        </div>
      </div>

      <div class="col-md-3">
        <div class="card border-0 shadow-sm rounded-4 p-3 bg-success text-white">
          <div class="d-flex align-items-center">
            <div class="icon-box bg-white bg-opacity-25 rounded-3 p-3 me-3">
              <i class="bi bi-box-seam fs-3"></i>
            </div>
            <div>
              <h6 class="mb-0 opacity-75">Sản phẩm</h6>
              <h3 class="fw-bold mb-0">{{ stats.totalProducts }}</h3>
            </div>
          </div>
        </div>
      </div>

      <div class="col-md-3">
        <div class="card border-0 shadow-sm rounded-4 p-3 bg-warning text-dark">
          <div class="d-flex align-items-center">
            <div class="icon-box bg-dark bg-opacity-10 rounded-3 p-3 me-3">
              <i class="bi bi-cart-check fs-3"></i>
            </div>
            <div>
              <h6 class="mb-0 opacity-75">Đơn hàng</h6>
              <h3 class="fw-bold mb-0">{{ stats.totalOrders }}</h3>
            </div>
          </div>
        </div>
      </div>

      <div class="col-md-3">
        <div class="card border-0 shadow-sm rounded-4 p-3 bg-danger text-white">
          <div class="d-flex align-items-center">
            <div class="icon-box bg-white bg-opacity-25 rounded-3 p-3 me-3">
              <i class="bi bi-currency-dollar fs-3"></i>
            </div>
            <div>
              <h6 class="mb-0 opacity-75">Doanh thu</h6>
              <h3 class="fw-bold mb-0">
                {{ stats.totalRevenue ? stats.totalRevenue.toLocaleString('vi-VN') : 0 }} ₫
              </h3>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="card border-0 shadow-sm rounded-4 p-4">
      <h5 class="fw-bold mb-4">Thống kê đơn hàng hàng tuần</h5>
      <div style="height: 400px;">
        <Bar v-if="loaded" :data="chartData" :options="chartOptions" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
// 🔥 Chỉ import Bar và BarElement
import { Bar } from 'vue-chartjs';
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale } from 'chart.js';

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale);

const loaded = ref(false);
const stats = ref({ totalUsers: 0, totalProducts: 0, totalOrders: 0, totalRevenue: 0 });

// 📊 Dữ liệu biểu đồ cột
const chartData = ref({
  labels: ['Thứ 2', 'Thứ 3', 'Thứ 4', 'Thứ 5', 'Thứ 6', 'Thứ 7', 'CN'],
  datasets: [{
    label: 'Số đơn hàng',
    backgroundColor: '#dc3545',
    borderRadius: 6, // Bo góc cột cho đẹp
    data: [10, 25, 15, 40, 30, 50, 36] 
  }]
});

const chartOptions = { responsive: true, maintainAspectRatio: false };

const fetchStats = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/admin/stats');
    stats.value = response.data;
    loaded.value = true;
  } catch (error) {
    console.error("Lỗi:", error);
  }
};

// 🔥 SỬA CHỖ NÀY: Phải gọi fetchStats, không gọi hàm của trang khác!
onMounted(() => {
  fetchStats(); 
});
</script>

<style scoped>
.icon-box { width: 60px; height: 60px; display: flex; align-items: center; justify-content: center; }
</style>