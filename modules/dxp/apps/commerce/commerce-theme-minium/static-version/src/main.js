import Vue from "vue";
import App from "./App.vue";

import "./styles/minium.scss";
import "./styles/test.scss";

Vue.config.productionTip = false;

new Vue({
  render: h => h(App)
}).$mount("#app");

new IntersectionObserver(
  entries => {
    document
      .getElementById("minium")
      .classList.toggle("is-scrolled", !entries[0].isIntersecting);
  },
  {
    root: document.querySelector(".js-scroll-area"),
    rootMargin: "10px",
    threshold: 1.0
  }
).observe(document.querySelector("[name=minium-top]"));
