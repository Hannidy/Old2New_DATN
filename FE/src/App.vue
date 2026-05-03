<script setup>
import { RouterView } from 'vue-router'
import { reactive, provide } from 'vue';
import ChatPopup from '@/components/ChatPopup.vue';

const chatState = reactive({
  isOpen: false,
  chatId: '',
  receiverName: ''
});

const openChat = (id, name) => {
  chatState.chatId = id;
  chatState.receiverName = name;
  chatState.isOpen = true;
};

provide('globalChat', { openChat });
</script>

<template>
  <!-- RouterView giúp hiển thị nội dung các trang như HomeView, ShopView... -->
  <RouterView/>

  <!-- ChatPopup với v-model để đồng bộ đóng mở -->
  <ChatPopup v-model:isOpen="chatState.isOpen" v-model:chatId="chatState.chatId" v-model:receiverName="chatState.receiverName"/>
</template>

<style>
/* Đảm bảo toàn app không bị lỗi vỡ khung khi hiện popup */
#app {
  position: relative;
  min-height: 100vh;
}
</style>