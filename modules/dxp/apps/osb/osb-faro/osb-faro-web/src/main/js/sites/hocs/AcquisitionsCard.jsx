import AcquisitionsQuery from 'shared/queries/AcquisitionsQuery';
import {ACQUISITION_LABEL_MAP} from 'shared/util/lang';
import {AcquisitionTypes, CompositionTypes} from 'shared/util/constants';
import {compositionListColumns} from 'shared/util/table-columns';
import {
	getMapResultToProps,
	mapCardPropsToOptions
} from './mappers/composition-query';
import {graphql} from '@apollo/react-hoc';
import {withTableTabs} from './TableTabs';

const {Channel, Referrer, SourceMedium} = AcquisitionTypes;

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
		props: getMapResultToProps(CompositionTypes.Acquisitions)
	});

const Tabs = [
	{
		getColumns: getColumnsFn(Channel),
		rowIdentifier: ROW_IDENTIFIER,
		tabId: Channel,
		title: Liferay.Language.get('channels')
	},
	{
		getColumns: getColumnsFn(SourceMedium),
		rowIdentifier: ROW_IDENTIFIER,
		tabId: SourceMedium,
		title: Liferay.Language.get('source-medium')
	},
	{
		getColumns: getColumnsFn(Referrer),
		rowIdentifier: ROW_IDENTIFIER,
		tabId: Referrer,
		title: Liferay.Language.get('referrers')
	}
];

export default withTableTabs(withAcquisitions, Tabs, {rowBordered: false});
