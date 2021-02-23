import AcquisitionsQuery from '../queries/AcquisitionsQuery';
import FaroConstants from 'shared/util/constants';
import {ACQUISITION_LABEL_MAP} from 'shared/util/lang';
import {compositionListColumns} from 'shared/util/table-columns';
import {
	getMapResultToProps,
	mapCardPropsToOptions
} from './mappers/composition-query';
import {graphql} from '@apollo/react-hoc';
import {withTableTabs} from './TableTabs';

const {
	acquisitionTypes: {channel, referrer, sourceMedium},
	compositionTypes: {acquisitions}
} = FaroConstants;

const ROW_IDENTIFIER = 'name';

const getColumnsFn = acquisitionType => {
	const label = ACQUISITION_LABEL_MAP[acquisitionType];

	return ({maxCount, totalCount}) => [
		compositionListColumns.getName({label, maxWidth: 200, tooltip: true}),
		compositionListColumns.getRelativeMetricBar({
			label: Liferay.Language.get('sessions'),
			maxCount,
			totalCount
		}),
		compositionListColumns.getPercentOf({
			metricName: Liferay.Language.get('sessions'),
			totalCount
		})
	];
};

const withAcquisitions = () =>
	graphql(AcquisitionsQuery, {
		options: mapCardPropsToOptions,
		props: getMapResultToProps(acquisitions)
	});

const Tabs = [
	{
		getColumns: getColumnsFn(channel),
		rowIdentifier: ROW_IDENTIFIER,
		tabId: channel,
		title: Liferay.Language.get('channels')
	},
	{
		getColumns: getColumnsFn(sourceMedium),
		rowIdentifier: ROW_IDENTIFIER,
		tabId: sourceMedium,
		title: Liferay.Language.get('source-medium')
	},
	{
		getColumns: getColumnsFn(referrer),
		rowIdentifier: ROW_IDENTIFIER,
		tabId: referrer,
		title: Liferay.Language.get('referrers')
	}
];

export default withTableTabs(withAcquisitions, Tabs, {rowBordered: false});
