<template>
  <span class="material-symbols" :style="style" @click="emits('click')">{{ icon }}</span>
</template>

<script setup lang="ts">
import { computed, useAttrs } from "vue";

import "@styles/fonts.css";

const props = defineProps<{
  icon: string
  size?: number
}>();

const emits = defineEmits(["click"]);

const attrs = useAttrs();

const aliases = {
  addImage: "add_photo_alternate",
  calendar: "event",
  cancel: "cancel",
  checkboxIndeterminate: "indeterminate_check_box",
  checkboxOff: "check_box_outline_blank",
  checkboxOn: "check_box",
  clear: "cancel",
  close: "close",
  collapse: "keyboard_arrow_up",
  complete: "check",
  delete: "delete",
  delimiter: "fiber_manual_record",
  download: "download",
  dropdown: "arrow_drop_down",
  edit: "edit",
  error: "warning",
  expand: "keyboard_arrow_down",
  eyeDropper: "colorize",
  file: "attach_file",
  filter: "filter_alt",
  first: "first_page",
  info: "info",
  last: "last_page",
  loading: "cached",
  menu: "menu",
  minus: "remove",
  next: "chevron_right",
  open: "open_in_new",
  plus: "add",
  prev: "chevron_left",
  radioOff: "radio_button_unchecked",
  radioOn: "radio_button_checked",
  ratingEmpty: "star_border",
  ratingFull: "star",
  ratingHalf: "star_half",
  sortAsc: "arrow_upward",
  sortDesc: "arrow_downward",
  subgroup: "arrow_drop_down",
  success: "check_circle",
  treeviewCollapse: "arrow_drop_down",
  treeviewExpand: "arrow_right",
  upload: "upload",
  unfold: "unfold_more",
  view: "visibility",
  warning: "priority_high",
};


const icon = computed<string>(() => {
  if (aliases[props.icon]) {
    return aliases[props.icon];
  } else {
    return props.icon;
  }
});

const style = computed(() => ({
  "font-size": `${props.size ? props.size : 24}px`,
  "cursor": (attrs.onClick ? "pointer" : "default")
}));
</script>
