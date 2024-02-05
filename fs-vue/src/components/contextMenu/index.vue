<template>
  <div class="context-menu" v-if="visible" :style="{ top: `${position.y}px`, left: `${position.x}px` }">
    <ul>
      <li v-for="item in items" :key="item.id" @click="handleItemClick(item)">
        <SvgIcon :name="item.icon" :size="18"></SvgIcon>{{ item.label }}
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { ref, defineExpose } from 'vue';

/**
 * 页面中引入一个contextMenu
 * 必须将该组件放在根template下的第一级元素下
 * 默认需要给 y 值减系统header的高度
 *
 */

/** 菜单元素 */
interface MenuItem {
  id: number;
  label: string;
  event: () => {},
  icon: string,
}

const visible = ref( false );
const position = ref( { x: 0, y: 0 } );
const items = ref<MenuItem[]>( [] );

/**
 * 显示菜单
 * @param event
 * @param menuItems
 */
const show = ( event: MouseEvent, menuItems: MenuItem[] ) => {
  event.preventDefault();
  position.value = { x: event.clientX, y: event.clientY - 50 };
  items.value = menuItems;
  visible.value = true;
};

/**
 * 隐藏菜单
 */
const hide = () => {
  visible.value = false;
  position.value = { x: 0, y: 0 };
  items.value = [];
};

/**
 * 触发元素点击事件
 * @param item
 */
const handleItemClick = ( item: MenuItem ) => {
  item.event();
  hide();
};

defineExpose( { show, hide } );
</script>

<style scoped lang="scss">
.context-menu {
  position: absolute;
  background-color: #fff;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  border-radius: 5px;
  z-index: 999;
}

.context-menu ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.context-menu li {
  padding: 8px 16px;
  line-height: 30px;
  display: flex;
  align-items: center;
  //justify-content: center;
  cursor: pointer;

  i {
    margin-right: 5px;
  }
}

.context-menu li:hover {
  background-color: #f0f0f0;
  border-radius: 5px;
}
</style>
