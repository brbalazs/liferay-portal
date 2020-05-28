import FaroConstants from 'shared/util/constants';
import {getVariables, safeResultToProps} from 'shared/util/mappers';
import {sub} from 'shared/util/lang';

const {
	pagination: {orderDescending}
} = FaroConstants;

const getTableMapper = getItems => {
	const mapResultToProps = safeResultToProps((result, _, {metricLabel}) => {
		const items = getItems(result);

		return items.length
			? {
					items
			  }
			: {
					empty: true,
					emptyMessage: sub(
						Liferay.Language.get('empty-message-metric'),
						[metricLabel]
					)
			  };
	});

	const mapPropsToOptions = ({
		activeTabId,
		filters,
		rangeSelectors,
		router: {params},
		tabConfig
	}) => {
		const {variables} = getVariables({
			filters,
			params,
			rangeSelectors
		});

		const activeTabConfig = tabConfig.find(
			({tabId}) => tabId === activeTabId
		);

		return {
			variables: {
				...variables,
				sort: {
					column: activeTabConfig.orderByField,
					type: orderDescending.toUpperCase()
				}
			}
		};
	};

	return {
		options: mapPropsToOptions,
		props: mapResultToProps
	};
};

export default getTableMapper;
