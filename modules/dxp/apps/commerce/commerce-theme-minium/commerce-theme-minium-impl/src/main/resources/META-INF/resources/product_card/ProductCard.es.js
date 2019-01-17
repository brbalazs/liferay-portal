'use strict';

import template from './ProductCard.soy';
import Component from 'metal-component';
import Soy, {Config} from 'metal-soy';

class ProductCard extends Component {
<<<<<<< HEAD

	attached() {
		return Liferay.on(
			'productRemovedFromCompare',
			(data) => {
				if (data.id === data.sku) { // TODO: sku to be changed in productId
					this.isInCompare = false;
				}
			}
		);
	}

	_handleCompareCheckbox(evt) {
		evt.preventDefault();
		this.isInCompare = !this.isInCompare;

		return Liferay.fire('toggleProductToCompare', {
=======
<<<<<<< Updated upstream
	_handleCompareCheckbox(evt){
		Liferay.fire('toggleProductToCompare', {
>>>>>>> COMMERCE-686 compare table started
			id: this.sku,
=======

	attached(){
		Liferay.on(
			'productRemovedFromCompare',
			(data) => {
				if (data.id === data.productId) {
					this.isInCompare = false
				}
			}
		)
		Liferay.on(
			'compareIsAvailable',
			() => {
				return this.isCompareAvailable = true;
			}
		)
		Liferay.on(
			'compareIsUnavailable',
			() => {
				return this.isCompareAvailable = false;
			}
		)
	}

	_handleCompareCheckbox(evt){
		evt.preventDefault()
		this.isInCompare = !this.isInCompare;

		return Liferay.fire('toggleProductToCompare', {
			id: this.productId,
>>>>>>> Stashed changes
			thumbnail: this.pictureUrl
		});
	}
};

Soy.register(ProductCard, template);

ProductCard.STATE = {
<<<<<<< Updated upstream
	productId: Config.string(),
<<<<<<< HEAD
=======
	availability: Config.string().oneOf([
		'inStock',
		'available',
		'notAvailable'
	]).value('inStock'),
=======
	productId: Config.oneOfType(
		[
			Config.string(),
			Config.number()
		]
	).required(),
>>>>>>> COMMERCE-686 compare table started
	availability: Config.string()
		.oneOf(
			[
				'inStock',
				'available',
				'notAvailable'
			]
		)
		.value('inStock'),
<<<<<<< HEAD
=======
>>>>>>> Stashed changes
>>>>>>> COMMERCE-686 compare table started
	sku: Config.string().required(),
	pictureUrl: Config.string(),
	name: Config.string().required(),
	isInCompare: Config.bool(),
	categories: Config.array(
		Config.shapeOf(
			{
				name: Config.string().required(),
				link: Config.string().required()
			}
		)
	),
	price: Config.shapeOf(
		{
			formattedPrice: Config.string().required(),
			formattedPromoPrice: Config.string()
		}
	),
	description: Config.string(),
	spritemap: Config.string(),
	detailsLink: Config.string(),
	minQuantity: Config.number(),
	settings: Config.shapeOf(
		{
			allowedOptions: Config.array(Config.number()),
			maxQuantity: Config.number(),
			minQuantity: Config.number(),
			multipleQuantities: Config.number()
		}
	).value({}),
	isCompareAvailable: Config.bool().value(true)
};

export {ProductCard};
export default ProductCard;