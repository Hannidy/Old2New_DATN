<template>
  <div class="container-fluid chat-wrapper py-3">
    <div class="row g-0 shadow rounded-4 overflow-hidden bg-white border" style="height: 80vh;">
      
      <!-- CỘT TRÁI: DANH SÁCH CUỘC HỘI THOẠI -->
      <div class="col-md-4 border-end bg-light">
        <div class="p-3 border-bottom bg-white d-flex justify-content-between align-items-center">
          <h5 class="fw-bold mb-0">Tin nhắn</h5>
        </div>
        <div class="list-group list-group-flush overflow-auto" style="height: calc(80vh - 60px);">
          <div v-if="conversations.length === 0" class="text-center mt-5 text-muted small">
            Chưa có cuộc hội thoại nào.
          </div>
          <button v-for="chat in conversations" :key="chat.id" 
                  @click="selectConversation(chat)"
                  :class="['list-group-item list-group-item-action border-0 p-3', 
                           activeChat?.id === chat.id ? 'bg-primary-subtle' : '']">
            <div class="d-flex align-items-center">
              <div class="rounded-circle bg-secondary text-white me-3 d-flex align-items-center justify-content-center fw-bold" style="width: 45px; height: 45px;">
                {{ chat.receiverName ? chat.receiverName.charAt(0).toUpperCase() : '?' }}
              </div>
              <div class="flex-grow-1 overflow-hidden">
                <h6 class="mb-0 fw-bold">{{ chat.receiverName || 'Người dùng hệ thống' }}</h6>
                <small class="text-muted text-truncate d-block">{{ chat.lastMessage }}</small>
              </div>
            </div>
          </button>
        </div>
      </div>

      <!-- CỘT PHẢI: NỘI DUNG TIN NHẮN -->
      <div class="col-md-8 d-flex flex-column bg-white">
        <template v-if="activeChat">
          <!-- Header chat -->
          <div class="p-3 border-bottom d-flex align-items-center bg-white shadow-sm">
            <div class="rounded-circle bg-primary text-white me-2 d-flex align-items-center justify-content-center fw-bold" style="width: 35px; height: 35px; font-size: 0.8rem;">
              {{ activeChat.receiverName ? activeChat.receiverName.charAt(0).toUpperCase() : '?' }}
            </div>
            <h6 class="fw-bold mb-0">{{ activeChat.receiverName }}</h6>
          </div>

          <!-- Danh sách tin nhắn -->
          <div class="flex-grow-1 p-3 overflow-auto d-flex flex-column gap-2 bg-light" id="message-container" style="background-image: url('https://www.transparenttextures.com/patterns/cubes.png');">
            <div v-for="msg in messages" :key="msg.id" 
                 :class="['p-2 rounded-3 shadow-sm', 
                          msg.senderId === currentUserId ? 'align-self-end bg-primary text-white' : 'align-self-start bg-white text-dark']"
                 style="max-width: 70%; word-break: break-word;">
              {{ msg.text }}
              <div :class="['small mt-1', msg.senderId === currentUserId ? 'text-white-50' : 'text-muted']" style="font-size: 0.65rem; text-align: right;">
                {{ formatTime(msg.timestamp) }}
              </div>
            </div>
          </div>

          <!-- Ô nhập tin nhắn -->
          <div class="p-3 border-top bg-white">
            <div class="input-group">
              <input v-model="newMessage" @keyup.enter="sendMessage" 
                     type="text" class="form-control rounded-pill border bg-light px-3" 
                     placeholder="Nhập tin nhắn...">
              <button @click="sendMessage" class="btn btn-primary rounded-circle ms-2 d-flex align-items-center justify-content-center" style="width: 45px; height: 45px;">
                <i class="bi bi-send-fill"></i>
              </button>
            </div>
          </div>
        </template>

        <!-- Trạng thái trống -->
        <div v-else class="h-100 d-flex flex-column align-items-center justify-content-center text-muted bg-light">
          <i class="bi bi-chat-heart display-1 opacity-25"></i>
          <p class="mt-3 fw-bold">Chọn một người bạn để trò chuyện nhé Duy!</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from "vue";
import { db } from "@/firebase";
import { ref as dbRef, push, onValue, update, serverTimestamp } from "firebase/database";
import { useRoute } from 'vue-router';

const route = useRoute();
const currentUserId = ref(null);
const activeChat = ref(null);
const newMessage = ref("");
const conversations = ref([]);
const messages = ref([]);

onMounted(() => {
  const user = JSON.parse(localStorage.getItem("user"));
  if (user) {
    // Lấy đúng ID dù backend trả về field name nào
    currentUserId.value = String(user.id || user.nguoiDungId || user.nguoidungId);
    loadConversations();
  }
});

// Trong ChatView.vue - phần hiển thị danh sách bên trái
const loadConversations = () => {
  // Lấy ID người đang đăng nhập (Lúc này là 18)
  const user = JSON.parse(localStorage.getItem("user"));
  const myId = String(user?.id || user?.nguoiDungId); 
  
  if (!myId) return;

  const listRef = dbRef(db, `conversations/${myId}`);
  onValue(listRef, (snapshot) => {
    const data = snapshot.val();
    if (data) {
      conversations.value = Object.keys(data)
        .map(key => ({ id: key, ...data[key] }))
        // Lọc để không hiện chính mình trong danh sách chat
        .filter(chat => String(chat.receiverId) !== myId) 
        .sort((a, b) => (b.timestamp || 0) - (a.timestamp || 0));
    }
  });
};
const selectConversation = (chat) => {
  activeChat.value = chat;
  loadMessages(chat.id);
};

const loadMessages = (chatId) => {
  const msgRef = dbRef(db, `messages/${chatId}`);
  onValue(msgRef, (snapshot) => {
    const data = snapshot.val();
    messages.value = data ? Object.keys(data).map(key => ({ id: key, ...data[key] })) : [];
    scrollToBottom();
  });
};

const sendMessage = async () => {
  if (!newMessage.value.trim() || !activeChat.value) return;

  const chatId = activeChat.value.id;
  const receiverId = activeChat.value.receiverId; // Quan trọng để cập nhật 2 bên
  const textToSend = newMessage.value.trim();
  
  // Xóa input ngay cho mượt
  newMessage.value = "";

  try {
    // 1. Đẩy tin nhắn vào Messages
    await push(dbRef(db, `messages/${chatId}`), {
      senderId: currentUserId.value,
      text: textToSend,
      timestamp: serverTimestamp()
    });

    // 2. Cập nhật tin nhắn cuối cùng cho CẢ 2 BÊN bằng lệnh update (nguyên tử)
    const updates = {};
    const chatInfo = {
      lastMessage: textToSend,
      timestamp: serverTimestamp()
    };

    // Cập nhật cho mình
    updates[`conversations/${currentUserId.value}/${chatId}/lastMessage`] = textToSend;
    updates[`conversations/${currentUserId.value}/${chatId}/timestamp`] = serverTimestamp();
    
    // Cập nhật cho người nhận (để họ cũng thấy tin nhắn mới nhất)
    if (receiverId) {
      updates[`conversations/${receiverId}/${chatId}/lastMessage`] = textToSend;
      updates[`conversations/${receiverId}/${chatId}/timestamp`] = serverTimestamp();
    }

    await update(dbRef(db), updates);
  } catch (error) {
    console.error("Lỗi gửi tin nhắn:", error);
  }
};

const scrollToBottom = () => {
  nextTick(() => {
    const container = document.getElementById("message-container");
    if (container) container.scrollTop = container.scrollHeight;
  });
};

const formatTime = (ts) => {
  if (!ts) return "";
  const date = new Date(ts);
  return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
};
</script>

<style scoped>
.chat-wrapper { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
.max-width-75 { max-width: 75%; }
.cursor-pointer { cursor: pointer; }
.list-group-item:hover { background-color: #f8f9fa; }
/* Tùy chỉnh thanh cuộn cho đẹp */
::-webkit-scrollbar { width: 5px; }
::-webkit-scrollbar-thumb { background: #ccc; border-radius: 10px; }
</style>