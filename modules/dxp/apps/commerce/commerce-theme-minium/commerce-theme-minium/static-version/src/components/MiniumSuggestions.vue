<template>
  <div class="minium-suggestions">
    <div class="minium-suggestions__wrapper">
      <div class="minium-suggestions__item u-hoverable">
        Search "keyword" in Pending Orders
      </div>
      <div class="minium-suggestions__item u-hoverable">
        Search "keyword" in All Content
      </div>
      <div class="minium-suggestions__label">
        Pending Orders
      </div>
      <div class="minium-suggestions__item u-hoverable" v-for="n in 3" :key="n">
        <div class="minium-item minium-item--search">
          <img class="minium-item__image" src="http://placehold.it/100" alt="">
          <div class="minium-item__content">
            Vestibulum Marmitta commodo urna<br>
            AR385672
          </div>
        </div>
      </div>
      <div class="minium-suggestions__item u-hoverable">
        Search "keyword" in Pending Orders
      </div>
      <div class="minium-suggestions__label">
        Catalog
      </div>
      <div class="minium-suggestions__item u-hoverable" v-for="n in 3" :key="n + 3">
        <div class="minium-item minium-item--search">
          <img class="minium-item__image" src="http://placehold.it/100" alt="">
          <div class="minium-item__content">
            Vestibulum Marmitta commodo urna<br>
            AR385672
          </div>
        </div>
      </div>
      <div class="minium-suggestions__item u-hoverable">
        Search "keyword" in Catalog
      </div>
    </div>
    <div class="minium-suggestions__hints">
      <minium-icon /> to navigate
      <minium-icon /> to select
      <minium-icon /> to dismiss
    </div>
  </div>
</template>

<script>
import MiniumIcon from "./MiniumIcon";

export default {
  components: {
    MiniumIcon
  }
};
</script>
