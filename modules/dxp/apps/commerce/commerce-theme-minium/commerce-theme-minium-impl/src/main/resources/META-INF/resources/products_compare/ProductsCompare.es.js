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

				return this._handleAddProduct(toggledProduct);
				
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
		return new Promise((resolve) => {
			setTimeout(
				() => {
					this.products = this.products.filter(
						(el) => el.id !== product.id
					);
					Liferay.fire('productRemovedFromCompare', product.id);
					resolve(this.products);
				},
				500
			)
		})
	}

	_handleAddProduct(product){
		return this._addProduct(product)
			.then(() => {
				return this._updateCompareGlobalState();
			})
	}

	_handleRemoveProduct(product){
		return this._removeProduct(product)
			.then(() => {
				return this._updateCompareGlobalState();
			})
	}

	_updateCompareGlobalState(){
		if(this.products.length < 4){
			return Liferay.fire('compareIsAvailable');
		}
		return Liferay.fire('compareIsUnavailable');
	}

	_updateProductVisibility(id, toState = 'visible') {
		return new Promise((resolve) => {
			setTimeout(
				() => {
					return this.products = this.products.map(
						(el) => {
							return el.id === id
								? {
									id: el.id,
									thumbnail: el.thumbnail,
									visibility: toState === 'visible' ? 'showing' : 'hiding'
								}
								: el
						}
					)
				},
				100
			);
			return setTimeout(
				() => {
					this.products = this.products.map((el) => {
						return el.id === id
							? {
								id: el.id,
								thumbnail: el.thumbnail,
								visibility: toState
							}
							: el
						}
					)
					return resolve(this.products)
				}, 
				400
			);
		})
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