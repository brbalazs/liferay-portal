'use strict';

import template from './ProductCard.soy';
import Component from 'metal-component';
import Soy, {Config} from 'metal-soy';

class ProductCard extends Component {

	_handleCheckboxCompareUpdate(newCompareState) {
		this.compareState = newCompareState;
		return this.compareState;
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
					}
					else {
						window.location.href = window.location.href;
					}
					return Liferay.SPA;
				}
			);
	}
}

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
				link: Config.string().required(),
				name: Config.string().required()
			}
		)
	),
	compareContentNamespace: Config.string(),
	compareState: Config.shapeOf(
		{
			compareAvailable: Config.bool(),
			inCompare: Config.bool()
		}
	).value(
		{
			checkboxVisible: true,
			compareAvailable: true,
			inCompare: false
		}
	),
	deleteButtonVisible: Config.bool(),
	description: Config.string(),
	detailsLink: Config.string(),
	editCompareProductActionURL: Config.string(),
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
			multipleQuantity: Config.number()
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