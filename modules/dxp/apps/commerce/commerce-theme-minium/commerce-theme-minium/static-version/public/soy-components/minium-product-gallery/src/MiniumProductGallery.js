import template from "./MiniumProductGallery.soy.js";
import Component from "metal-component";
import Soy from "metal-soy";

class MiniumProductGallery extends Component {
	handleThumbClick(e) {
		this.selected = parseInt(e.delegateTarget.dataset.index, 10);
	}
}

Soy.register(MiniumProductGallery, template);

MiniumProductGallery.STATE = {
	images: {
		value: []
	},
	selected: {
		value: 0
	}
};

export { MiniumProductGallery };
export default MiniumProductGallery;
