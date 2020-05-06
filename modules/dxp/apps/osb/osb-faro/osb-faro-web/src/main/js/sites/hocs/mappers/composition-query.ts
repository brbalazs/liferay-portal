import FaroConstants, {LAST_30_DAYS} from 'shared/util/constants';
import {COMPOSITION_LABEL_MAP, sub} from 'shared/util/lang';
import {get} from 'lodash';
import {safeResultToProps} from 'shared/util/mappers';

const {
	pagination: {delta: defaultDelta}
} = FaroConstants;

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

const mapPropsToOptions: object = ({router: {query}}) => {
	const delta = parseInt(get(query, 'delta', defaultDelta));
	const page = parseInt(get(query, 'page', 1));

	return {
		variables: {
			rangeKey: parseInt(get(query, 'rangeKey', LAST_30_DAYS)),
			size: delta,
			start: (page - 1) * delta
		}
	};
};

const mapCardPropsToOptions: object = ({
	activeTabId,
	rangeKey,
	router: {params}
}) => ({
	variables: {
		activeTabId,
		channelId: get(params, 'channelId'),
		rangeKey: parseInt(rangeKey),
		size: 5,
		start: 0
	}
});

export {getMapResultToProps, mapCardPropsToOptions, mapPropsToOptions};
