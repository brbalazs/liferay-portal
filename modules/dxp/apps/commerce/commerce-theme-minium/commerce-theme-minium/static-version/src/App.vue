<template>
  <div id="minium" class="minium minium-frame (has-search)">
    <div class="minium-frame__sidebar">
      <minium-sidebar />
    </div>

    <div class="minium-frame__topbar">
      <minium-topbar />
    </div>

    <div class="minium-frame__content js-scroll-area">
      <a name="minium-top"></a>
      <div class="minium-content">
        <div style="height: 200px; background: white; padding-top: 40px; text-align: center; border-radius: 8px; color: #9D9E9E; font-size: 11px; text-transform: uppercase; letter-spacing: 1.1px;">
          👉 Page content HERE! 👈

        </div>
        <br>
        <br>
        <div id="test-table"></div>
      </div>
    </div>

    <div class="minium-frame__toolbar (is-visible)">
      
    </div>

    <div class="minium-frame__cta is-visible">
      <minium-button big outline>Cancel</minium-button>
      <minium-button big>Done</minium-button>
    </div>

    <div class="minium-frame__dock (is-visible)">
      Products
      <a href="#" class="minium-button">Compare</a>
    </div>

    <div class="minium-frame__overlay (is-visible)">
      <minium-suggestions />
    </div>

    <div class="minium-frame__tray">
      <minium-toast>
        Toast message message<br><a href="#" class="minium-link">Go to link</a>
      </minium-toast>
    </div>

    <div class="minium-frame__modal (is-visible)">
      <div class="minium-modal">
        <a href="#" class="minium-modal__close">
          <minium-icon />
        </a>
        <div class="minium-modal__content">
          <div class="minium-modal__title">Modal title</div>
          <div style="color: #9D9E9E; font-size: 11px; text-transform: uppercase; letter-spacing: 1.1px;">
            👉 Modal content HERE! 👈
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import MiniumButton from "./components/MiniumButton";
import MiniumIcon from "./components/MiniumIcon";
import MiniumSidebar from "./components/MiniumSidebar";
import MiniumSuggestions from "./components/MiniumSuggestions";
import MiniumToast from "./components/MiniumToast";
import MiniumTopbar from "./components/MiniumTopbar";

export default {
  components: {
    MiniumButton,
    MiniumIcon,
    MiniumSidebar,
    MiniumSuggestions,
    MiniumToast,
    MiniumTopbar
  }
};
</script>
