'use strict';

import template from './ProductsCompare.soy';
import Component from 'metal-component';
import Soy, {Config} from 'metal-soy';

import './ProductsCompareItem.es';

class ProductsCompare extends Component {

	attached() {
		return Liferay.on(
			'toggleProductToCompare',
			(data) => {

				const toggledProduct = {
					id: data.id,
					thumbnail: data.thumbnail
				};

				const isIncluded = this.products.reduce(
					(acc, el) => {

					},
					false
				);

				if (isIncluded) {
					return this._removeProduct(toggledProduct);
				}

				return this._addProduct(toggledProduct);

			}
		);
	}

	_addProduct(product) {
		this.products = this.products.concat(
			{
				id: product.id,
				thumbnail: product.thumbnail,
				visibility: 'hidden'
			}
		);
		return this._updateProductVisibility(product.id, 'visible');
	}

	_removeProduct(product) {
		this._updateProductVisibility(product.id, 'hidden');
		return setTimeout(
			() => {
				this.products = this.products.filter(
					(el) => el.id !== product.id
				);
				return Liferay.fire('productRemovedFromCompare', product.id);
			},
			500
		);
	}

	_updateProductVisibility(id, toState = 'visible') {
		setTimeout(
			() => {
				return this.products = this.products.map(
					(el) => {
						return el.id === id ?
							{
								id: el.id,
								thumbnail: el.thumbnail,
								visibility: toState === 'visible' ? 'showing' : 'hiding'
							} :
							el;
					}
				);
			},
			100
		);
		return setTimeout(
			() => {
				return this.products = this.products.map(
					(el) => {
						return el.id === id ?
							{
								id: el.id,
								thumbnail: el.thumbnail,
								visibility: toState
							} :
							el;
					}
				);
			},
			400
		);
	}

	_submitCompare() {
		const idList = this.products.map(el => el.id);
		this.emit('submitCompare', idList);
	}
};

Soy.register(ProductsCompare, template);

ProductsCompare.STATE = {
	products: Config.array(
		Config.shapeOf({
			thumbnail: Config.string().required(),
			id: Config.oneOfType(
				[
					Config.string(),
					Config.number()
				]
			).required(),
			visibility: Config.string()
		})
	).value([]),
	spritemap: Config.string()
};

export {ProductsCompare};
export default ProductsCompare;