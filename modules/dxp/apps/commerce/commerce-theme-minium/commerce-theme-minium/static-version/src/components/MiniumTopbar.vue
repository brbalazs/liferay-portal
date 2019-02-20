<template>
  <div class="commerce-topbar">
    <div class="commerce-topbar__start">
      <a href="#" class="commerce-topbar__button">
        <commerce-icon/>Back
      </a>
      <label class="commerce-topbar__button (is-active) js-toggle-search" for="commerce-search-input">
        <commerce-icon/>
      </label>
    </div>
    <div class="commerce-topbar__middle">
      <minium-tabs></minium-tabs>
    </div>
    <div class="commerce-topbar__end">
      <div class="commerce-topbar__account-selector-wrapper"></div>
      <div class="commerce-topbar__cart-wrapper"></div>
    </div>
    <div class="commerce-topbar__search" id="commerce-search">
      <!-- <commerce-search placeholder="Search Product Name, SKU, Client…" id="commerce-search-input"/> -->
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
