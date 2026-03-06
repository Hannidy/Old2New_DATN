<template>
  <div class="admin-container bg-light min-vh-100 p-4">
    <div class="d-flex justify-content-between align-items-center mb-4 border-bottom pb-3">
      <div>
        <h2 class="h3 fw-bold text-dark mb-1">Quản lý danh mục</h2>
        <p class="text-muted small mb-0">Thêm, sửa, xóa các danh mục sản phẩm trên hệ thống</p>
      </div>
      
      <div class="d-flex gap-3">
        <div class="input-group" style="width: 250px;">
          <input type="text" class="form-control" placeholder="Tìm danh mục...">
          <button class="btn btn-dark" type="button">🔍</button>
        </div>
        <button class="btn btn-primary fw-bold px-4" @click="openModal(null)">
          + Thêm Danh Mục
        </button>
      </div>
    </div>

    <div class="bg-white rounded shadow-sm overflow-hidden">
      <table class="table table-hover align-middle mb-0">
        <thead class="table-light">
          <tr>
            <th class="py-3 px-4" style="width: 80px;">ID</th>
            <th class="py-3">Tên Danh Mục</th>
            <th class="py-3">Danh Mục Cha</th>
            <th class="py-3 text-end px-4">Hành Động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="categories.length === 0">
            <td colspan="4" class="text-center py-5 text-muted">
              <div class="spinner-border text-secondary spinner-border-sm me-2" role="status"></div>
              Đang tải dữ liệu hoặc chưa có danh mục nào...
            </td>
          </tr>
          
          <tr v-for="cat in categories" :key="cat.id">
            <td class="px-4 fw-bold text-secondary">#{{ cat.id }}</td>
            
            <td>
              <span class="fw-semibold text-dark">{{ cat.name }}</span>
            </td>
            
            <td>
              <span v-if="cat.parentName" class="badge bg-info text-dark">{{ cat.parentName }}</span>
              <span v-else class="text-muted small fst-italic">-- Trống (Danh mục gốc) --</span>
            </td>
            
            <td class="text-end px-4">
              <button class="btn btn-sm btn-outline-primary fw-bold me-2" @click="openModal(cat)">
                ✏️ Sửa
              </button>
              <button class="btn btn-sm btn-outline-danger fw-bold" @click="deleteCategory(cat.id, cat.name)">
                🗑️ Xóa
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="showModal" class="modal fade show d-block" tabindex="-1" style="background: rgba(0,0,0,0.6);">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow">
          <div class="modal-header border-bottom">
            <h5 class="modal-title fw-bold">{{ isEditing ? 'Sửa Danh Mục' : 'Thêm Danh Mục Mới' }}</h5>
            <button type="button" class="btn-close" @click="closeModal"></button>
          </div>
          
          <div class="modal-body p-4">
            <form @submit.prevent="saveCategory">
              <div class="mb-3">
                <label class="form-label fw-bold small text-dark">Tên danh mục <span class="text-danger">*</span></label>
                <input type="text" v-model="form.name" required class="form-control border-secondary" placeholder="Nhập tên danh mục...">
              </div>

              <div class="mb-4">
                <label class="form-label fw-bold small text-dark">Thuộc danh mục cha (Tùy chọn)</label>
                <select v-model="form.parentId" class="form-select border-secondary">
                  <option :value="null">-- Không có (Làm danh mục gốc) --</option>
                  <option v-for="parent in parentOptions" :key="parent.id" :value="parent.id">
                    {{ parent.name }}
                  </option>
                </select>
                <div class="form-text small">Nếu chọn, danh mục này sẽ nằm bên trong danh mục cha.</div>
              </div>

              <div class="d-flex justify-content-end gap-2 mt-4 pt-3 border-top">
                <button type="button" class="btn btn-outline-secondary px-4" @click="closeModal">Hủy</button>
                <button type="submit" class="btn btn-primary px-4 fw-bold">
                  {{ isEditing ? 'Cập Nhật' : 'Lưu Danh Mục' }}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';

// =======================
// STATE & BIẾN
// =======================
const categories = ref([]);
const showModal = ref(false);
const isEditing = ref(false);
const editingId = ref(null);

// Form data
const form = ref({
  name: '',
  parentId: null
});

// Lọc ra các danh mục gốc để làm "Danh mục cha" trong dropdown
// (Không cho phép một danh mục nhận chính nó làm cha để tránh lỗi vòng lặp)
const parentOptions = computed(() => {
  if (isEditing.value) {
    return categories.value.filter(c => c.id !== editingId.value);
  }
  return categories.value;
});

// =======================
// CALL API LẤY DỮ LIỆU
// =======================
onMounted(() => {
  fetchCategories();
});

const fetchCategories = async () => {
  try {
    // Gọi API lấy dạng phẳng (Flat) để hiển thị bảng cho dễ
    const response = await axios.get('http://localhost:8080/api/admin/categories');
    categories.value = response.data;
  } catch (error) {
    console.error("Lỗi lấy danh sách danh mục:", error);
  }
};

// =======================
// XỬ LÝ MODAL (THÊM / SỬA)
// =======================
const openModal = (category = null) => {
  if (category) {
    isEditing.value = true;
    editingId.value = category.id;
    form.value.name = category.name;
    form.value.parentId = category.parentId || null;
  } else {
    isEditing.value = false;
    editingId.value = null;
    form.value.name = '';
    form.value.parentId = null;
  }
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
};

// =======================
// GỌI API LƯU (THÊM/SỬA)
// =======================
const saveCategory = async () => {
  // Lấy token từ LocalStorage để gửi lên bảo mật
  const storedUser = localStorage.getItem('user');
  const token = storedUser ? (JSON.parse(storedUser).token || JSON.parse(storedUser).accessToken) : '';
  const config = { headers: { Authorization: `Bearer ${token}` } };

  const payload = {
    tenDanhMuc: form.value.name,
    chaId: form.value.parentId
  };

  try {
    if (isEditing.value) {
      await axios.put(`http://localhost:8080/api/admin/categories/${editingId.value}`, payload, config);
      alert("✅ Cập nhật danh mục thành công!");
    } else {
      await axios.post('http://localhost:8080/api/admin/categories', payload, config);
      alert("✅ Thêm danh mục mới thành công!");
    }
    closeModal();
    fetchCategories(); // Tải lại bảng
  } catch (error) {
    console.error("Lỗi lưu danh mục:", error);
    alert("❌ Lỗi khi lưu. Máy chủ từ chối!");
  }
};

// =======================
// GỌI API XÓA
// =======================
const deleteCategory = async (id, name) => {
  if (!confirm(`⚠️ CẢNH BÁO: Bạn có chắc chắn muốn xóa danh mục "${name}" không? Các sản phẩm thuộc danh mục này có thể bị ảnh hưởng!`)) {
    return;
  }

  const storedUser = localStorage.getItem('user');
  const token = storedUser ? (JSON.parse(storedUser).token || JSON.parse(storedUser).accessToken) : '';

  try {
    await axios.delete(`http://localhost:8080/api/admin/categories/${id}`, {
      headers: { Authorization: `Bearer ${token}` }
    });
    alert("✅ Xóa danh mục thành công!");
    fetchCategories();
  } catch (error) {
    console.error("Lỗi xóa danh mục:", error);
    alert("❌ Không thể xóa! Có thể danh mục này đang chứa sản phẩm hoặc danh mục con.");
  }
};
</script>

<style scoped>
.admin-container { font-family: 'Inter', sans-serif; }
.table th { font-weight: 600; text-transform: uppercase; font-size: 0.75rem; color: #6c757d; letter-spacing: 0.5px; }
.badge { padding: 6px 10px; border-radius: 6px; font-weight: 600; font-size: 0.75rem; letter-spacing: 0.5px;}
.table-hover tbody tr:hover { background-color: #f8f9fa; transition: 0.2s; }
</style>
