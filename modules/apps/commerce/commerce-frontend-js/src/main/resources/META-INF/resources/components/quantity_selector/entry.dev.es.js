import launcher from './entry.es';

import '../../styles/main.scss';

launcher('quantity-selector', 'quantity-selector-root-id', {
	// allowedQuantities: [3, 5, 10, 15],
	// disabled: true,
	inputName: 'test-name',
	maxQuantity: 10000,
	minQuantity: 2,
	multipleQuantity: 2,
	quantity: 10,
	size: 'large',
	spritemap: './assets/icons.svg',
	// style: 'simple'
});
