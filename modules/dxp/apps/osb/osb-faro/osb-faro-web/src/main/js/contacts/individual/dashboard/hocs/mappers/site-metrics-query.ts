import moment from 'moment';
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
		data: anonymousVisitorsMetric.histogram.reduce(
			(acc, {key, value}, i) => [
				...acc,
				{
					anonymousVisitors: value,
					intervalInitDate: moment.utc(key).valueOf(),
					knownVisitors: knownVisitorsMetric.histogram[i].value,
					visitors: visitorsMetric.histogram[i].value
				}
			],
			[]
		),
		dateKeysIMap: Map(
			visitorsMetric.histogram.map(({key, valueKey}) => [
				moment.utc(key).valueOf(),
				valueKey
					.split('/')
					.map(valueKeyHalf => moment.utc(valueKeyHalf).valueOf())
			])
		)
	})
);
