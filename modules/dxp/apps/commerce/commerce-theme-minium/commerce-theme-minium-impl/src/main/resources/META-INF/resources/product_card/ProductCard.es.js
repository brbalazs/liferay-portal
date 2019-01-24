'use strict';

import template from './ProductCard.soy';
import Component from 'metal-component';
import Soy, {Config} from 'metal-soy';

class ProductCard extends Component {

	attached() {
		Liferay.on(
			'accountSelected',
			(data) => {
				this.accountId = data.accountId;
			}
		);

		Liferay.on(
			'productRemovedFromCompare',
			(data) => {
				if (data.id === this.productId) {
					this.isInCompare = false;
				}
			}
		);
		Liferay.on(
			'compareIsAvailable',
			() => {
				this.isCompareAvailable = true;
			}
		);
		Liferay.on(
			'compareIsUnavailable',
			() => {
				this.isCompareAvailable = false;
			}
		);
	}

	_handleRemoveProduct() {
		return this.emit(
			'removeProduct',
			{
				id: this.productId
			}
		);
	}

	_handleRemoveProduct() {
		const formData = new FormData();

		formData.append(this.compareContentNamespace + 'cpDefinitionId', this.productId);
		formData.append(this.compareContentNamespace + this.productId + 'Compare', false);

		return fetch(
			this.editCompareProductActionURL,
			{
				body: formData,
				credentials: 'include',
				method: 'post'
			}
		)
		.then(
			() => {
				if (Liferay.SPA) {
					Liferay.SPA.app.navigate(window.location.href);
				} else {
					window.location.href = window.location.href;
				}
				return Liferay.SPA
			}
		);
	}

	_handleCompareCheckbox(evt) {
		evt.preventDefault();
		this.isInCompare = !this.isInCompare;

		return Liferay.fire(
			'toggleProductToCompare',
			{
				id: this.productId,
				thumbnail: this.pictureUrl
			}
		);
	}
};

Soy.register(ProductCard, template);

ProductCard.STATE = {
	accountId: Config.oneOfType(
		[
			Config.string(),
			Config.number()
		]
	),
	availability: Config.string()
		.oneOf(
			[
				'inStock',
				'available',
				'notAvailable'
			]
		)
		.value('inStock'),
	cartAPI: Config.string(),
	categories: Config.array(
		Config.shapeOf(
			{
				name: Config.string().required(),
				link: Config.string().required()
			}
		)
	),
	compareContentNamespace: Config.string(),
	description: Config.string(),
	detailsLink: Config.string(),
	editCompareProductActionURL: Config.string(),
	isInCompare: Config.bool(),
	isCompareAvailable: Config.bool().value(true),
	isCompareCheckboxVisible: Config.bool(),
	isDeleteButtonVisible: Config.bool(),
	minQuantity: Config.number(),
	name: Config.string().required(),
	orderId: Config.oneOfType(
		[
			Config.string(),
			Config.number()
		]
	),
	pictureUrl: Config.string(),
	price: Config.shapeOf(
		{
			formattedPrice: Config.string().required(),
			formattedPromoPrice: Config.string()
		}
	),
	productId: Config.oneOfType(
		[
			Config.string(),
			Config.number()
		]
	).required(),
	settings: Config.shapeOf(
		{
			allowedOptions: Config.array(Config.number()),
			maxQuantity: Config.number(),
			minQuantity: Config.number(),
			multipleQuantities: Config.number()
		}
	).value({}),
	sku: Config.string(),
	skuId: Config.oneOfType(
		[
			Config.string(),
			Config.number()
		]
	),
	spritemap: Config.string()

};

export {ProductCard};
export default ProductCard;