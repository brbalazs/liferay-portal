import {getVariables, safeResultToProps} from 'shared/util/mappers';
import {OrderByDirections} from 'shared/util/constants';
import {sub} from 'shared/util/lang';

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
					column: activeTabConfig.tabId,
					type: OrderByDirections.Descending
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
