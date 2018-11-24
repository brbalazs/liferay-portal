<template>
  <div class="minium-search">
    <div class="minium-search__input">
      <input type="text" :placeholder=placeholder>
    </div>
    <a href="#" class="minium-search__button">
      <minium-icon />
    </a>
  </div>
</template>

<script>
import MiniumIcon from "./MiniumIcon";

export default {
  props: {
    placeholder: String
  },
  components: {
    MiniumIcon
  }
};
</script>
