<template>
  <div class="minium-topbar">
    <div class="minium-topbar__start">
      <a href="#" class="minium-topbar__button">
        <minium-icon/>Back
      </a>
      <label class="minium-topbar__button (is-active) js-toggle-search" for="minium-search-input">
        <minium-icon/>
      </label>
    </div>
    <div class="minium-topbar__middle">
      <minium-tabs></minium-tabs>
    </div>
    <div class="minium-topbar__end">
      <div class="minium-topbar__account-selector-wrapper"></div>
      <div class="minium-topbar__cart-wrapper"></div>
    </div>
    <div class="minium-topbar__search" id="minium-search">
      <!-- <minium-search placeholder="Search Product Name, SKU, Client…" id="minium-search-input"/> -->
    </div>
  </div>
</template>

<script>
import AccountList from "./AccountList";
import MiniumIcon from "./MiniumIcon";
import MiniumSearch from "./MiniumSearch";
import MiniumTabs from "./MiniumTabs";
import SmallTable from "./SmallTable";

export default {
  components: {
    AccountList,
    MiniumIcon,
    MiniumSearch,
    MiniumTabs,
    SmallTable
  }
};
</script>
