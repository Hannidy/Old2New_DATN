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
              <th>Vai trò</th>
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
                <span :class="user.vaiTro?.tenVaiTro === 'ADMIN' ? 'badge bg-dark' : 'badge bg-secondary'">
                  {{ user.vaiTro?.tenVaiTro || 'USER' }}
                </span>
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

const handleToggle = async (user) => {
  if (confirm(`Thay đổi trạng thái tài khoản ${user.hoVaTen}?`)) {
    try {
      const res = await axios.put(`http://localhost:8080/api/admin/users/${user.nguoiDungId}/toggle-status`);
      
      // ✅ CẬP NHẬT TRỰC TIẾP TRÊN UI:
      // Tìm vị trí user trong mảng và thay đổi trạng thái ngay lập tức
      const newStatus = user.trangThaiNguoiMua === 'BI_KHOA' ? 'HOAT_DONG' : 'BI_KHOA';
      user.trangThaiNguoiMua = newStatus;
      
      alert("Cập nhật thành công!");
    } catch (error) {
      console.error(error);
      alert("Lỗi thao tác!");
    }
  }
};

onMounted(() => {
  fetchUsers(); // Đối với trang Users
})
</script>