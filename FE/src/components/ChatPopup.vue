<template>
  <div class="chat-system-container">
    <!-- 1. BONG BÓNG CHAT -->
    <div class="chat-bubble shadow-lg" @click="toggleMainList">
      <i class="bi bi-chat-dots-fill text-white fs-2"></i>
  <!-- Badge thông báo nếu có tin nhắn mới -->
  <span v-if="hasNewMessage" class="badge-notification"></span>
    </div>

    <!-- 2. DANH SÁCH HỘI THOẠI -->
    <div v-if="isMainListOpen" class="chat-list-panel shadow-lg rounded-4 animate__animated animate__fadeInUp">
      <div class="panel-header bg-danger text-white p-3 rounded-top-4 d-flex justify-content-between">
        <span class="fw-bold">Tin nhắn</span>
        <button @click="isMainListOpen = false" class="btn-close btn-close-white"></button>
      </div>
      <div class="panel-body p-0 overflow-auto">
        <div class="admin-contact p-3 border-bottom bg-light cursor-pointer" @click="chatWithAdmin">
           <div class="d-flex align-items-center gap-2">
              <div class="avatar bg-warning text-dark">A</div>
              <div class="fw-bold">Hỗ trợ từ Admin</div>
           </div>
        </div>
        
        <div v-for="chat in conversations" :key="chat.id" 
             @click="openSpecificChat(chat)" 
             class="chat-item p-3 border-bottom cursor-pointer">
          <div class="d-flex align-items-center gap-3">
            <!-- CHỖ SỬA 1: Thêm kiểm tra receiverName trước khi dùng charAt -->
            <div class="avatar bg-secondary text-white">
              {{ chat.receiverName ? chat.receiverName.charAt(0).toUpperCase() : '?' }}
            </div>
            <div class="flex-grow-1">
              <div class="fw-bold small">{{ chat.receiverName || 'Người dùng hệ thống' }}</div>
              <div class="text-muted extra-small text-truncate">{{ chat.lastMessage }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 3. HỘP THOẠI CHAT CHI TIẾT -->
    <div v-if="isOpen" class="chat-window shadow-lg border rounded-top-4 overflow-hidden bg-white animate__animated animate__zoomIn">
      <div class="chat-header bg-danger text-white p-2 d-flex justify-content-between align-items-center">
        <div class="d-flex align-items-center gap-2">
          <button @click="backToList" class="btn btn-sm text-white p-0 me-1"><i class="bi bi-chevron-left"></i></button>
          <!-- CHỖ SỬA 2: Hiển thị tên an toàn -->
          <span class="fw-bold small">{{ receiverName || 'Đang tải...' }}</span>
        </div>
        <button @click="closeChat" class="btn btn-sm text-white p-0"><i class="bi bi-x-lg"></i></button>
      </div>
      <div class="chat-body p-2" id="popup-messages">
        <div v-for="msg in messages" :key="msg.id" 
             :class="['message-bubble mb-2 p-2 rounded-3', msg.senderId === currentUserId ? 'bg-danger text-white ms-auto' : 'bg-light text-dark me-auto']">
          {{ msg.text }}
        </div>
      </div>
      <div class="chat-footer p-2 border-top">
        <div class="input-group input-group-sm">
          <input v-model="newMessage" @keyup.enter="sendMessage" type="text" class="form-control border-0 bg-light" placeholder="Aa...">
          <button @click="sendMessage" class="btn btn-danger"><i class="bi bi-send"></i></button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick } from 'vue';
import { db } from "@/firebase";
import { ref as dbRef, onValue, push, serverTimestamp } from "firebase/database";

const props = defineProps(['chatId', 'receiverName', 'isOpen']);
const emit = defineEmits(['update:isOpen', 'update:chatId', 'update:receiverName']);

const isMainListOpen = ref(false);
const conversations = ref([]);
const messages = ref([]);
const newMessage = ref("");
const currentUserId = ref(null);
const hasNewMessage = ref(false); 

onMounted(() => {
  initUserChat();
});


// Hàm khởi tạo thông tin chat
const initUserChat = () => {
  const user = JSON.parse(localStorage.getItem("user"));
  if (user) {
    currentUserId.value = String(user.id || user.nguoiDungId);
    loadMyConversations();
  } else {
    // Nếu không có user thì reset hết dữ liệu chat
    currentUserId.value = null;
    conversations.value = [];
    messages.value = [];
    emit('update:isOpen', false);
  }
};

// Theo dõi sự thay đổi của localStorage (khi login/logout)
watch(() => localStorage.getItem("user"), () => {
  initUserChat();
}, { deep: true });


const loadMyConversations = () => {
  if (!currentUserId.value) return;
  const listRef = dbRef(db, `conversations/${currentUserId.value}`);
  onValue(listRef, (snapshot) => {
    const data = snapshot.val();
    // Chuyển object sang array và sắp xếp theo thời gian mới nhất lên đầu
    conversations.value = data ? Object.keys(data).map(key => ({ id: key, ...data[key] }))
      .sort((a, b) => (b.timestamp || 0) - (a.timestamp || 0)) : [];
  });
};

const toggleMainList = () => { 
  isMainListOpen.value = !isMainListOpen.value; 
  if (isMainListOpen.value) emit('update:isOpen', false); // Đóng cửa sổ chat chi tiết khi mở danh sách
};

const openSpecificChat = (chat) => {
  emit('update:chatId', chat.id);
  emit('update:receiverName', chat.receiverName);
  emit('update:isOpen', true);
  isMainListOpen.value = false;
};

const chatWithAdmin = async () => {
  const ADMIN_ACCOUNT_ID = "18"; // Đổi từ 1 sang 18 cho đúng ID của Duy
  const myId = String(currentUserId.value); // Đây là ID 19 của Duy
  
  if (myId === ADMIN_ACCOUNT_ID) return;

  const myName = JSON.parse(localStorage.getItem('user'))?.hoVaTen || "Khách hàng";
  // Tạo chatId dựa trên 18 và 19
  const chatId = Number(myId) < Number(ADMIN_ACCOUNT_ID) ? `${myId}_${ADMIN_ACCOUNT_ID}` : `${ADMIN_ACCOUNT_ID}_${myId}`;

  const updates = {};
  // Ghi vào hộp thư của User 19
  updates[`conversations/${myId}/${chatId}`] = {
    receiverId: ADMIN_ACCOUNT_ID,
    receiverName: "Admin Hỗ Trợ",
    lastMessage: "Đang kết nối với Admin...",
    timestamp: Date.now()
  };
  // Ghi vào hộp thư của Admin 18 -> ĐỂ ADMIN THẤY DANH SÁCH
  updates[`conversations/${ADMIN_ACCOUNT_ID}/${chatId}`] = {
    receiverId: myId,
    receiverName: myName,
    lastMessage: "Cần hỗ trợ mới",
    timestamp: Date.now()
  };

  const { update, ref: dbRefRoot } = await import("firebase/database");
  await update(dbRefRoot(db), updates);

  openSpecificChat({ id: chatId, receiverName: "Admin Hỗ Trợ" });
};

const backToList = () => {
  emit('update:isOpen', false);
  isMainListOpen.value = true;
};

const closeChat = () => emit('update:isOpen', false);

watch(() => props.chatId, (newId) => {
  if (newId) {
    const msgRef = dbRef(db, `messages/${newId}`);
    onValue(msgRef, (snapshot) => {
      const data = snapshot.val();
      messages.value = data ? Object.keys(data).map(key => ({ id: key, ...data[key] })) : [];
      scrollToBottom();
    });
  }
});

const sendMessage = async () => {
  if (!newMessage.value.trim() || !props.chatId) return;
  const textToSend = newMessage.value.trim();
  newMessage.value = ""; // Xóa input ngay lập tức để tạo cảm giác mượt mà
  
  await push(dbRef(db, `messages/${props.chatId}`), {
    senderId: currentUserId.value,
    text: textToSend,
    timestamp: serverTimestamp()
  });
};

const scrollToBottom = () => {
  nextTick(() => {
    const container = document.getElementById("popup-messages");
    if (container) container.scrollTop = container.scrollHeight;
  });
};
</script>

<style scoped>
/* Giữ nguyên phần Style của Duy, rất đẹp rồi! */
.chat-system-container { position: fixed; bottom: 20px; right: 20px; z-index: 9999; font-family: sans-serif; }
.chat-bubble { width: 60px; height: 60px; background: #dc3545; border-radius: 50%; display: flex; align-items: center; justify-content: center; cursor: pointer; transition: 0.3s; }
.chat-bubble:hover { transform: scale(1.1); }
.chat-list-panel, .chat-window { position: absolute; bottom: 70px; right: 0; width: 320px; height: 450px; display: flex; flex-direction: column; background: white; border: 1px solid #ddd; }
.avatar { width: 40px; height: 40px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-weight: bold; flex-shrink: 0; }
.chat-body { flex: 1; overflow-y: auto; background: #f8f9fa; }
.message-bubble { max-width: 80%; font-size: 0.9rem; width: fit-content; word-break: break-word; }
.extra-small { font-size: 0.75rem; }
.cursor-pointer { cursor: pointer; transition: 0.2s; }
.chat-item:hover { background: #f1f1f1; }
.badge-dot { position: absolute; top: 5px; right: 5px; width: 12px; height: 12px; background: #28a745; border-radius: 50%; border: 2px solid white; }
</style>