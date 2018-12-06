<template>
  <div class="minium-search">
    <div class="minium-search__input">
      <input type="text" :placeholder="placeholder" :id="id">
    </div>
    <a href="#" class="minium-search__button">
      <minium-icon/>
    </a>
  </div>
</template>

<script>
import MiniumIcon from "./MiniumIcon";

export default {
  props: {
    id: String,
    placeholder: String
  },
  components: {
    MiniumIcon
  }
};
</script>
