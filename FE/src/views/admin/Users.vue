<template>
  <div class="users-management">
    <div class="card border-0 shadow-sm rounded-4 p-4">
      <h5 class="fw-bold mb-4">👥 Quản lý người dùng hệ thống</h5>
      
      <div class="table-responsive">
        <table class="table table-hover align-middle">
          <thead class="table-light">
            <tr>
              <th>ID</th>
              <th>Họ và Tên</th>
              <th>Email / SĐT</th>
              <th>Trạng thái</th>
              <th class="text-center">Thao tác</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in users" :key="user.nguoiDungId">
              <td>#{{ user.nguoiDungId }}</td>
              <td class="fw-bold">{{ user.hoVaTen }}</td>
              <td>
                <div>{{ user.email }}</div>
                <small class="text-muted">{{ user.soDienThoai }}</small>
              </td>
                <td>
                  <span :class="(user.trangThaiNguoiMua === 'HOAT_DONG' || !user.trangThaiNguoiMua) ? 'badge bg-success' : 'badge bg-danger'">
                    {{ (user.trangThaiNguoiMua === 'HOAT_DONG' || !user.trangThaiNguoiMua) ? 'Đang hoạt động' : 'Đang bị khóa' }}
                  </span>
                </td>

                <td class="text-center">
                  <button v-if="user.trangThaiNguoiMua === 'HOAT_DONG' || !user.trangThaiNguoiMua" 
                          class="btn btn-outline-danger btn-sm" @click="handleToggle(user)">
                    <i class="bi bi-lock-fill"></i> Khóa
                  </button>
                  <button v-else 
                          class="btn btn-outline-success btn-sm" @click="handleToggle(user)">
                    <i class="bi bi-unlock-fill"></i> Mở khóa
                  </button>
                </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const users = ref([]);

const fetchUsers = async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/admin/users');
    users.value = res.data;
  } catch (error) {
    console.error("Lỗi lấy danh sách user:", error);
  }
};

// Trong script setup của Users.vue
const handleToggle = async (user, newStatus) => {
  const action = newStatus === 1 ? 'Mở khóa' : 'Khóa';
  if (confirm(`Bạn có chắc chắn muốn ${action} tài khoản ${user.hoVaTen}?`)) {
    try {
      // Gửi status là 1 (mở) hoặc 0 (khóa)
      await axios.put(`http://localhost:8080/api/admin/users/${user.nguoiDungId}/toggle-status?status=${newStatus}`);
      
      // Cập nhật lại giao diện ngay lập tức để người dùng thấy thay đổi
      user.isEnable = newStatus; 
      alert(action + " thành công!");
    } catch (error) {
      alert("Lỗi khi thực hiện thao tác!");
    }
  }
};

onMounted(fetchUsers);
</script>