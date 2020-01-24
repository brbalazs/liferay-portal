'use strict';

import Component from 'metal-component';
import Soy, {Config} from 'metal-soy';

import template from './ProductsCompareItem.soy';

class ProductsCompareItem extends Component {
	_hideItem() {
		this.emit('updateProductVisibility', this.id, 'hidden');
		return setTimeout(() => {
			return this.emit('removeProduct', {
				id: this.id
			});
		}, 300);
	}

	_handleRemoveProduct() {
		return this._hideItem();
	}
}

Soy.register(ProductsCompareItem, template);

ProductsCompareItem.STATE = {
	id: Config.oneOfType([Config.string(), Config.number()]),
	spritemap: Config.string().required(),
	thumbnail: Config.string(),
	visibility: Config.oneOf(['showing', 'visible', 'hiding', 'hidden'])
};

export {ProductsCompareItem};
export default ProductsCompareItem;
