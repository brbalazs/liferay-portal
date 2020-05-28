import Constants from 'shared/util/constants';
import {COMPOSITION_LABEL_MAP, sub} from 'shared/util/lang';
import {get} from 'lodash';
import {getSafeRangeSelectors} from 'shared/util/util';
import {safeResultToProps} from 'shared/util/mappers';

const {
	pagination: {delta: defaultDelta}
} = Constants;

const getMapResultToProps = (compositionBagName: string) =>
	safeResultToProps(
		({
			[compositionBagName]: {compositions, maxCount, total, totalCount}
		}) => ({
			empty: !total,
			emptyMessage: sub(Liferay.Language.get('empty-message-metric'), [
				COMPOSITION_LABEL_MAP[compositionBagName] ||
					Liferay.Language.get('items')
			]),
			items: compositions,
			maxCount,
			total,
			totalCount
		})
	);

const mapPropsToOptions: object = ({
	rangeSelectors,
	router: {params, query}
}) => {
	const delta = parseInt(get(query, 'delta', defaultDelta));
	const page = parseInt(get(query, 'page', 1));

	return {
		variables: {
			channelId: get(params, 'channelId'),
			size: delta,
			start: (page - 1) * delta,
			...getSafeRangeSelectors(rangeSelectors)
		}
	};
};

const mapCardPropsToOptions: object = ({
	activeTabId,
	rangeSelectors,
	router: {params}
}) => ({
	variables: {
		activeTabId,
		channelId: get(params, 'channelId'),
		size: 5,
		start: 0,
		...getSafeRangeSelectors(rangeSelectors)
	}
});

export {getMapResultToProps, mapCardPropsToOptions, mapPropsToOptions};
