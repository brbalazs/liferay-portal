import {COMPOSITION_LABEL_MAP, sub} from 'shared/util/lang';
import {getSafeRangeSelectors} from 'shared/util/util';
import {safeResultToProps} from 'shared/util/mappers';

const getMapResultToProps = (compositionBagName: string) =>
	safeResultToProps(
		({
			[compositionBagName]: {compositions, maxCount, total, totalCount}
		}: {
			[key: string]: {
				compositions: Array<any>;
				maxCount: number;
				total: number;
				totalCount: number;
			};
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
	channelId,
	delta,
	id,
	page,
	rangeSelectors
}) => ({
	variables: {
		channelId,
		id,
		size: delta,
		start: (page - 1) * delta,
		...getSafeRangeSelectors(rangeSelectors)
	}
});

const mapCardPropsToOptions: object = ({
	activeTabId,
	channelId,
	rangeSelectors
}) => ({
	variables: {
		activeTabId,
		channelId,
		size: 5,
		start: 0,
		...getSafeRangeSelectors(rangeSelectors)
	}
});

export {getMapResultToProps, mapCardPropsToOptions, mapPropsToOptions};
