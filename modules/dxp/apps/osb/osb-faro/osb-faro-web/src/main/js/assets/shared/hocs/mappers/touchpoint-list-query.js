import {getVariables, safeResultToProps} from 'shared/util/mappers';

const mapResultToProps = safeResultToProps(({assetPages}) => {
	const items =
		assetPages &&
		assetPages.map(({assetId, assetTitle}) => ({
			title: assetTitle ? assetTitle : assetId,
			touchpoint: assetId
		}));

	if (!items || items.length === 0) {
		return {
			empty: true,
			emptyMessage: Liferay.Language.get('empty-message-pages-card')
		};
	}

	return {
		items
	};
});

/**
 * Map Props to Options
 * @param {object} param0 props
 * @param {object} param1 context
 */
const mapPropsToOptions = ({
	assetType,
	filters,
	rangeSelectors,
	router: {params}
}) => {
	const {variables} = getVariables({filters, params, rangeSelectors});

	return {
		variables: {
			...variables,
			assetType: assetType.toUpperCase()
		}
	};
};

export {mapPropsToOptions, mapResultToProps};
