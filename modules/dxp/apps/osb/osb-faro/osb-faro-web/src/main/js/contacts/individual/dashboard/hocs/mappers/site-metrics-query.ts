import {BAR_CHART} from 'shared/components/Chart';
import {
	CHART_DATA_ID_1,
	CHART_DATA_ID_2,
	LANG_MAP
} from '../../components/ActiveIndividualsChart';
import {getDate} from 'shared/util/date';
import {getSafeRangeSelectors} from 'shared/util/util';
import {Map} from 'immutable';
import {safeResultToProps} from 'shared/util/mappers';

export const mapPropsToOptions = ({channelId, interval, rangeSelectors}) => ({
	variables: {
		channelId,
		interval,
		...getSafeRangeSelectors(rangeSelectors)
	}
});

export const mapResultToProps = safeResultToProps(
	({
		site: {anonymousVisitorsMetric, knownVisitorsMetric, visitorsMetric}
	}) => ({
		data: [
			{
				data: knownVisitorsMetric.histogram.map(({value}) => value),
				id: CHART_DATA_ID_1,
				name: LANG_MAP[CHART_DATA_ID_1],
				type: BAR_CHART
			},
			{
				data: anonymousVisitorsMetric.histogram.map(({value}) => value),
				id: CHART_DATA_ID_2,
				name: LANG_MAP[CHART_DATA_ID_2],
				type: BAR_CHART
			},
			{
				data: visitorsMetric.histogram.map(({key}) => getDate(key)),
				id: 'x'
			}
		],
		dateKeysIMap: Map(
			visitorsMetric.histogram.map(({key, valueKey}) => [
				getDate(key),
				valueKey.split('/').map(getDate)
			])
		)
	})
);
