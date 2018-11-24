import template from "./Summary.soy.js";
import Component from "metal-component";
import Soy from "metal-soy";

import "./CartProduct";
import "./Loader";

class Summary extends Component {}

Summary.STATE = {
isLoading: {
	value: false
}
};

Soy.register(Summary, template);

export { Summary };
export default Summary;