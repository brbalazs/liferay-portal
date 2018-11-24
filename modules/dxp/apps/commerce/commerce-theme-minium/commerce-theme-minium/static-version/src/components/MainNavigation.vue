<template>
  <div class="main-navigation">
    <a href="#" class="main-link is-active">
      <div class="main-link__icon"><minium-icon /></div>
      <div class="main-link__label">Dashboard</div>
    </a>
    <a href="#" class="main-link">
      <div class="main-link__icon"><minium-icon /></div>
      <div class="main-link__label">Products</div>
    </a>
    <a href="#" class="main-link">
      <div class="main-link__icon"><minium-icon /></div>
      <div class="main-link__label">Parts Catalog</div>
    </a>
    <a href="#" class="main-link">
      <div class="main-link__icon has-notification"><minium-icon /></div>
      <div class="main-link__label">Orders</div>
    </a>
    <div class="main-navigation__submenu is-open">
      <a href="#" class="main-link main-link--sub">
        <div class="main-link__label">Pending Orders</div>
      </a>
      <a href="#" class="main-link main-link--sub is-active">
        <div class="main-link__label">Placed Orders</div>
      </a>
      <a href="#" class="main-link main-link--sub">
        <div class="main-link__label">Back Orders</div>
      </a>
      <a href="#" class="main-link main-link--sub">
        <div class="main-link__label">Shipments</div>
      </a>
      <a href="#" class="main-link main-link--sub">
        <div class="main-link__label">
          Pending Actions
          <div class="notification-badge">6</div>
        </div>
      </a>
    </div>
    <a href="#" class="main-link">
      <div class="main-link__icon"><minium-icon /></div>
      <div class="main-link__label">Account Management</div>
    </a>
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
