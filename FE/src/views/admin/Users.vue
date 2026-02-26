<template>
  <div class="admin-container">
    <div class="header-section">
      <h2>Quản Lý Người Dùng</h2>
    </div>

    <div class="table-wrapper">
      <table v-if="users.length > 0" class="user-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Họ và Tên</th>
            <th>Email</th>
            <th>Số Điện Thoại</th>
            <th>Vai Trò</th>
            <th>Hành Động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.nguoiDungId">
            <td>#{{ user.nguoiDungId }}</td>
            <td class="font-medium">{{ user.hoVaTen || 'Chưa cập nhật' }}</td>
            <td>{{ user.email }}</td>
            <td>{{ user.soDienThoai || 'Chưa cập nhật' }}</td>
            <td>
              <span class="badge" :class="getRoleClass(user.vaiTro?.tenVaiTro)">
                {{ user.vaiTro?.tenVaiTro || 'Chưa có' }}
              </span>
            </td>
            <td>
              <button class="btn-action edit">Mở</button>
              <button class="btn-action delete">Khóa</button>
            </td>
          </tr>
        </tbody>
      </table>
      
      <div v-else class="empty-state">
        <p>Đang tải dữ liệu từ Spring Boot hoặc danh sách trống...</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

// Biến lưu trữ danh sách người dùng
const users = ref([]);

// Hàm gọi API từ Spring Boot
const fetchUsers = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/admin/users');
    users.value = response.data;
    console.log("Dữ liệu lấy về:", response.data);
  } catch (error) {
    console.error('Lỗi kết nối Backend:', error);
    alert('Không thể kết nối đến Backend. Hãy chắc chắn Spring Boot đang chạy ở port 8080 nhé!');
  }
};

// Hàm tạo màu sắc cho các vai trò khác nhau
const getRoleClass = (roleName) => {
  if (!roleName) return 'badge-default';
  const role = roleName.toUpperCase();
  if (role === 'ADMIN') return 'badge-danger';
  if (role === 'SELLER') return 'badge-warning';
  return 'badge-primary'; // Dành cho USER thường
};

// Tự động chạy hàm fetchUsers khi trang vừa load lên
onMounted(() => {
  fetchUsers();
});
</script>

<style scoped>
.admin-container {
  padding: 2rem;
  font-family: 'Segoe UI', sans-serif;
  background-color: #f8f9fa;
  min-height: 100vh;
}

.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.header-section h2 {
  color: #333;
  margin: 0;
}


.table-wrapper {
  background: white;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0,0,0,0.05);
  overflow: hidden;
}

.user-table {
  width: 100%;
  border-collapse: collapse;
  text-align: left;
}

.user-table th, .user-table td {
  padding: 15px 20px;
  border-bottom: 1px solid #eee;
}

.user-table th {
  background-color: #f8f9fa;
  color: #555;
  font-weight: 600;
}

.user-table tr:hover {
  background-color: #fdfdfd;
}

.font-medium {
  font-weight: 500;
  color: #222;
}

/* Các nhãn Vai trò (Badge) */
.badge {
  padding: 5px 10px;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 600;
}
.badge-primary { background-color: #e0f3ff; color: #007bff; }
.badge-warning { background-color: #fff3cd; color: #856404; }
.badge-danger { background-color: #f8d7da; color: #721c24; }
.badge-default { background-color: #e2e3e5; color: #383d41; }

/* Nút hành động */
.btn-action {
  border: none;
  background: none;
  cursor: pointer;
  font-weight: 600;
  margin-right: 10px;
}
.btn-action.edit { color: #007bff; }
.btn-action.delete { color: #dc3545; }

.empty-state {
  padding: 3rem;
  text-align: center;
  color: #666;
}
</style>