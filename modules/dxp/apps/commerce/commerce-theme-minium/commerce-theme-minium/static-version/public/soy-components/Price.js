import template from "./Price.soy.js";
import Component from "metal-component";
import Soy from "metal-soy";

import "./Price";

class Price extends Component {}

Price.STATE = {
formattedPrice: {
	value: null
},
formattedPromoPrice: {
	value: null
},
additionalDiscountedClasses: {
	value: ""
},
additionalOldPriceClasses: {
	value: ""
},
additionalPriceClasses: {
	value: ""
}
};

Soy.register(Price, template);

export { Price };
export default Price;