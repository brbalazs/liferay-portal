<template>
  <div class="account-list">
    <div class="account-list__title">Select Account...</div>
    <a href="#" class="account-list__item u-hoverable" v-for="n in 10" :key="n">
      <img src="/avatar.jpg" alt="">
      <span>Forward Auto Service</span>
    </a>
  </div>
</template>
