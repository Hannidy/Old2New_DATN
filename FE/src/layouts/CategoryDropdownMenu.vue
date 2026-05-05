<template>
  <ul class="main-menu shadow-sm rounded-bottom">
    <li
      v-for="category in categories"
      :key="category.id"
      class="menu-item border-bottom-dashed"
    >
      <a
        href="#"
        @click.prevent="$emit('select', category.id)"
        class="d-flex justify-content-between align-items-center text-decoration-none py-2 px-3"
      >
        {{ category.name }}
        <span
          v-if="category.children && category.children.length > 0"
          class="text-muted small"
          >›</span
        >
      </a>

      <ul
        class="sub-menu shadow-sm"
        v-if="category.children && category.children.length > 0"
      >
        <li
          v-for="child in category.children"
          :key="child.id"
          class="menu-item-child"
        >
          <a
            href="#"
            @click.prevent="$emit('select', child.id)"
            class="d-flex justify-content-between align-items-center text-decoration-none py-2 px-3"
          >
            {{ child.name }}
            <span
              v-if="child.children && child.children.length > 0"
              class="text-muted small"
              >›</span
            >
          </a>

          <ul
            class="sub-menu-3 shadow-sm"
            v-if="child.children && child.children.length > 0"
          >
            <li
              v-for="grandchild in child.children"
              :key="grandchild.id"
              class="menu-item-grandchild"
            >
              <a
                href="#"
                @click.prevent="$emit('select', grandchild.id)"
                class="d-flex justify-content-between align-items-center text-decoration-none py-2 px-3"
              >
                {{ grandchild.name }}
                <span
                  v-if="grandchild.children && grandchild.children.length > 0"
                  class="text-muted small"
                  >›</span
                >
              </a>

              <ul
                class="sub-menu-4 shadow-sm"
                v-if="grandchild.children && grandchild.children.length > 0"
              >
                <li
                  v-for="greatgrandchild in grandchild.children"
                  :key="greatgrandchild.id"
                >
                  <a
                    href="#"
                    @click.prevent="$emit('select', greatgrandchild.id)"
                    class="text-decoration-none py-2 px-3 d-block"
                    >{{ greatgrandchild.name }}</a
                  >
                </li>
              </ul>
            </li>
          </ul>
        </li>
      </ul>
    </li>
  </ul>
</template>

<script setup>
defineProps({
  categories: {
    type: Array,
    required: true,
  },
});
defineEmits(["select"]);
</script>

<style scoped>
/* Copy nguyên xi đoạn CSS quản lý main-menu, sub-menu từ Header.vue sang đây */
.main-menu {
  display: none;
  position: absolute;
  top: 100%;
  left: 0;
  margin: 0;
  padding: 0;
  background-color: white;
  border: 1px solid #eee;
  border-top: none;
  min-width: 250px;
  list-style: none;
  z-index: 1050;
  border-radius: 0 0 8px 8px;
}
.border-bottom-dashed {
  border-bottom: 1px dashed #f0f0f0;
}
.menu-item {
  position: relative;
}
.menu-item > a {
  color: #444;
  font-size: 0.95rem;
}
.menu-item:hover > a {
  background-color: #fcfcfc;
  color: #007bff;
}
.sub-menu {
  display: none;
  position: absolute;
  top: 0;
  left: 100%;
  margin: 0;
  padding: 0;
  background-color: white;
  border: 1px solid #eee;
  min-width: 240px;
  min-height: 100%;
  list-style: none;
  z-index: 1060;
}
.menu-item:hover .sub-menu {
  display: block;
}
.menu-item-child {
  position: relative;
}
.menu-item-child > a {
  color: #555;
  font-size: 0.9rem;
  border-bottom: 1px solid #f9f9f9;
}
.menu-item-child:hover > a {
  background-color: #f8f9fa;
  color: #007bff;
}
.sub-menu-3 {
  display: none;
  position: absolute;
  top: 0;
  left: 100%;
  margin: 0;
  padding: 0;
  background-color: white;
  border: 1px solid #eee;
  min-width: 220px;
  min-height: 100%;
  list-style: none;
  z-index: 1070;
}
.menu-item-child:hover .sub-menu-3 {
  display: block;
}
.menu-item-grandchild {
  position: relative;
}
.menu-item-grandchild > a {
  color: #666;
  font-size: 0.85rem;
  border-bottom: 1px solid #f9f9f9;
}
.menu-item-grandchild > a:hover {
  color: #007bff;
  background-color: #f8f9fa;
}
.sub-menu-4 {
  display: none;
  position: absolute;
  top: 0;
  left: 100%;
  margin: 0;
  padding: 0;
  background-color: white;
  border: 1px solid #eee;
  min-width: 220px;
  min-height: 100%;
  list-style: none;
  z-index: 1080;
}
.menu-item-grandchild:hover .sub-menu-4 {
  display: block;
}
.sub-menu-4 a {
  color: #777;
  font-size: 0.8rem;
  border-bottom: 1px solid #f9f9f9;
}
.sub-menu-4 a:hover {
  color: #007bff;
  background-color: #f8f9fa;
}
</style>
