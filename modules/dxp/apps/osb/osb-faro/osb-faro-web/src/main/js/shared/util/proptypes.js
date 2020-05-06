import {PropTypes} from 'prop-types';

export const HOC_CARD_PROPTYPES = {
	/**
	 * Filters
	 * @type {object}
	 */
	filters: PropTypes.object.isRequired,

	/**
	 * RangeKey
	 * @type {string}
	 */
	rangeKey: PropTypes.string.isRequired,

	/**
	 * Router
	 * @type {object}
	 */
	router: PropTypes.object.isRequired
};
