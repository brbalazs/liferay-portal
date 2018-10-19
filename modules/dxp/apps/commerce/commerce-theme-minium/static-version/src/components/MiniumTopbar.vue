<template>
  <div class="minium-topbar">
    <div class="minium-topbar__start">
      <a href="#" class="minium-topbar__button">
        <minium-icon />
        Back
      </a>
      <label class="minium-topbar__button (is-active)" for="minium-search-input">
        <minium-icon />
      </label>
    </div>
    <div class="minium-topbar__middle">
      <minium-tabs></minium-tabs>
    </div>
    <div class="minium-topbar__end">
      <div class="minium-dropdown (is-open)">
        <a href="#" class="minium-topbar__button">
          <div class="account-selector">
            <div class="account-selector__image"><img src="/avatar.jpg" alt=""></div>
            <div class="account-selector__title">Select Account & Order</div>
            <div class="account-selector__info">No order selected</div>
          </div>
          <minium-icon />
        </a>
        <div class="minium-dropdown__curtain">
          <div class="account-switcher is-visible">
            <div class="account-switcher__section">
              <minium-search placeholder="Search Acconts…" />
            </div>
            <div class="account-switcher__section account-switcher__section--fill">
              <account-list />
            </div>
          </div>
          <div class="account-switcher (is-visible)">
            <a href="#" class="account-switcher__back">
              <minium-icon />
              Back
            </a>
            <div class="account-switcher__section">
              <div class="account-switcher__title">
                Forward Auto Service
              </div>
            </div>
            <div class="account-switcher__section">
              <minium-search placeholder="Search Order…" />
            </div>
            <div class="account-switcher__section account-switcher__section--fill account-switcher__section--padded">
              <small-table />
            </div>
            <div class="account-switcher__section">
              <a href="#" class="minium-button minium-button--block">Create new order</a>
            </div>
          </div>
        </div>
      </div>
      <a href="#" class="minium-topbar__button (is-disabled)">
        <minium-icon />
      </a>
      <div class="minium-cart (is-open)">
        <a href="#" class="minium-topbar__button minium-cart__close">
          <minium-icon />
        </a>
        <div class="minium-cart__top">
          <div><strong>149</strong> Products</div>
          <a href="#" class="minium-link">View details</a>
        </div>
        <div class="minium-cart__content">
          <div class="minium-cart__item minium-item u-hoverable" v-for="n in 25" :key="n">
            <img class="minium-item__image" src="http://placehold.it/100" alt="">
            <div class="minium-item__content">
              ASGJGGP<br>
              Lorem, ipsum dolor sit amet consectetur adipisicing elit. Necessitatibus, praesentium.<br>
              <strong>$ 560.00</strong>
            </div>
            <div class="minium-item__actions">
              <div class="quantity-selector">
                <a href="#" class="quantity-selector__btn">-</a>
                <input type="number" class="quantity-selector__input" :value="Math.round(Math.random() * 100)">
                <a href="#" class="quantity-selector__btn">+</a>
                <a href="#" class="quantity-selector__remove"></a>
              </div>
            </div>
          </div>
        </div>
        <div class="minium-cart__footer">
          <dl class="minium-cart__totals">
            <dt>Units</dt><dd>77 of 14 Items</dd>
            <dt>Subtotal</dt><dd>$ 32,000.00</dd>
            <dt>Discount</dt><dd>0% Off</dd>
            <dt>Grand Total</dt><dd><big>$ 32,000.00</big></dd>
          </dl>
          <a href="#" class="minium-button minium-button--block">Submit</a>
        </div>
      </div>
    </div>
    <div class="minium-topbar__search">
      <minium-search placeholder="Search Product Name, SKU, Client…" />
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
