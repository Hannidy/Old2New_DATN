<template>
  <div class="admin-container bg-light min-vh-100 p-4">
    <div class="d-flex justify-content-between align-items-center mb-4 border-bottom pb-3">
      <div>
        <h2 class="h3 fw-bold text-dark mb-1">Quản lý người dùng</h2>
        <p class="text-muted small mb-0">Theo dõi và quản lý tài khoản trên hệ thống</p>
      </div>
      
      <div class="input-group" style="width: 300px;">
        <input type="text" class="form-control" placeholder="Tìm kiếm email, tên...">
        <button class="btn btn-dark" type="button">🔍</button>
      </div>
    </div>

    <div class="bg-white rounded shadow-sm overflow-hidden">
      <table class="table table-hover align-middle mb-0">
        <thead class="table-light">
          <tr>
            <th class="py-3 px-4">ID</th>
            <th class="py-3">Người Dùng</th>
            <th class="py-3">Liên Hệ</th>
            <th class="py-3 text-center">Vai Trò</th>
            <th class="py-3 text-center">Trạng Thái</th>
            <th class="py-3 text-end px-4">Hành Động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="users.length === 0">
            <td colspan="6" class="text-center py-5 text-muted">Đang tải dữ liệu...</td>
          </tr>
          
          <tr v-for="user in users" :key="user.nguoiDungId" :class="{'table-danger opacity-75': user.trangThaiNguoiMua === 'BI_KHOA'}">
            <td class="px-4 fw-bold text-secondary">#{{ user.nguoiDungId || user.id }}</td>
            
            <td>
              <div class="d-flex align-items-center gap-3">
                <img v-if="user.anhDaiDien" :src="user.anhDaiDien" class="rounded-circle object-fit-cover border" style="width: 40px; height: 40px;">
                <div v-else class="rounded-circle bg-dark text-white d-flex align-items-center justify-content-center fw-bold" style="width: 40px; height: 40px;">
                  {{ user.hoVaTen ? user.hoVaTen.charAt(0).toUpperCase() : 'U' }}
                </div>
                <span class="fw-semibold text-dark">{{ user.hoVaTen || 'Chưa cập nhật' }}</span>
              </div>
            </td>
            
            <td>
              <div class="small text-dark">{{ user.email }}</div>
              <div class="small text-muted">{{ user.soDienThoai || 'Chưa cập nhật SĐT' }}</div>
            </td>
            
            <td class="text-center">
               <span class="badge" :class="user.vaiTroId === 1 ? 'bg-primary' : 'bg-secondary'">
                 {{ user.vaiTroId === 1 ? 'ADMIN' : 'USER' }}
               </span>
            </td>
            
            <td class="text-center">
              <span class="badge" :class="user.trangThaiNguoiMua === 'BI_KHOA' ? 'bg-danger' : 'bg-success'">
                {{ user.trangThaiNguoiMua === 'BI_KHOA' ? 'ĐÃ KHÓA' : 'HOẠT ĐỘNG' }}
              </span>
            </td>
            
            <td class="text-end px-4">
              <button 
                v-if="user.vaiTroId !== 1" 
                class="btn btn-sm fw-bold"
                :class="user.trangThaiNguoiMua === 'BI_KHOA' ? 'btn-outline-success' : 'btn-outline-danger'"
                @click="toggleUserStatus(user.nguoiDungId || user.id, user.trangThaiNguoiMua)"
              >
                {{ user.trangThaiNguoiMua === 'BI_KHOA' ? '🔓 Mở Khóa' : '🔒 Khóa' }}
              </button>
              <span v-else class="text-muted small fst-italic">Không thể khóa Admin</span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const users = ref([]);

onMounted(() => {
  fetchUsers();
});

const fetchUsers = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/admin/users');
    users.value = response.data;
  } catch (error) {
    console.error("Lỗi lấy danh sách User:", error);
  }
};

const toggleUserStatus = async (userId, currentStatus) => {
  const actionName = currentStatus === 'BI_KHOA' ? 'MỞ KHÓA' : 'KHÓA';
  
  // Hộp thoại xác nhận (Cực kỳ chuyên nghiệp, tránh Admin bấm nhầm)
  if (!confirm(`⚠️ Bạn có chắc chắn muốn ${actionName} tài khoản #${userId} này không?`)) {
    return;
  }

  try {
    // Gọi API đã viết ở Bước 1
    const response = await axios.put(`http://localhost:8080/api/admin/users/${userId}/toggle-status`);
    alert("✅ " + response.data.message);
    
    // Refresh lại bảng dữ liệu để thấy sự thay đổi
    fetchUsers(); 
  } catch (error) {
    console.error("Lỗi cập nhật trạng thái:", error);
    alert("❌ Có lỗi xảy ra. Vui lòng thử lại!");
  }
};
</script>

<style scoped>
.admin-container { font-family: 'Inter', sans-serif; }
.table th { font-weight: 600; text-transform: uppercase; font-size: 0.75rem; color: #6c757d; letter-spacing: 0.5px; }
.badge { padding: 6px 10px; border-radius: 6px; font-weight: 600; font-size: 0.75rem; letter-spacing: 0.5px;}
.table-hover tbody tr:hover { background-color: #f8f9fa; transition: 0.2s; }
</style>