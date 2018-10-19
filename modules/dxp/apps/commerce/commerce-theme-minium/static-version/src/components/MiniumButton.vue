<template>
  <a
    href="#"
    :class="[
      'minium-button',
      big && 'minium-button--big',
      small && 'minium-button--small',
      outline && 'minium-button--outline'
    ]"
  >
    <slot />
  </a>
</template>

<script>
export default {
  props: {
    block: Boolean,
    big: Boolean,
    outline: Boolean,
    small: Boolean,
    text: String
  }
};
</script>
