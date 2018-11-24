/* eslint-disable import/no-extraneous-dependencies */
import { storiesOf } from "@storybook/vue";
import "../styles/minium.scss";

import MiniumSearch from "../components/MiniumSearch.vue";

storiesOf("Search", module)
.add("default", () => ({
	components: { MiniumSearch },
	render() {
	  return <minium-search />;
	}
}))
.add("with placeholder", () => ({
	components: { MiniumSearch },
	render() {
	  return <minium-search placeholder="Placeholder" />;
	}
}));