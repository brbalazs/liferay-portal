'use strict';

import template from './ProductCard.soy';
import Component from 'metal-component';
import Soy, {Config} from 'metal-soy';

class ProductCard extends Component {};

Soy.register(ProductCard, template);

ProductCard.STATE = {
	productId: Config.string(),
	availability: Config.string().oneOf([
		'inStock',
		'available',
		'notAvailable'
	]).value('inStock'),
	sku: Config.string().required(),
	pictureUrl: Config.string(),
	name: Config.string().required(),
	categories: Config.array(
		Config.shapeOf({
			name: Config.string().required(),
			link: Config.string().required()
		})
	),
	price: Config.shapeOf({
		formattedPrice: Config.string().required(),
		formattedPromoPrice: Config.string()
	}),
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
	).value({})
};

export {ProductCard};
export default ProductCard;