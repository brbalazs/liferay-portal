import getTableMapper from './mappers/table';
import SitesTopPagesQuery from '../queries/SitesTopPagesQuery';
import {graphql} from '@apollo/react-hoc';
import {metricsListColumns} from 'shared/util/table-columns';
import {NameCell} from 'shared/components/table/cell-components';
import {sub} from 'shared/util/lang';
import {withTableTabs} from './TableTabs';

const ROW_IDENTIFIER = ['assetId', 'assetTitle'];

const ASSET_TITLE_COLUMN = {
	cellRenderer: NameCell,
	cellRendererProps: {
		nameKey: 'assetTitle',
		renderSecondaryInfo: ({assetId}) => assetId
	},
	className: 'table-cell-expand',
	label: `${Liferay.Language.get('page-title')}
			|
			${Liferay.Language.get('canonical-url')}`,
	sortable: false
};

const DEFAULT_METRIC_COLUMN = {
	sortable: false,
	title: true
};

/**
 * HOC
 * @description Site Top Pages
 */
const withSiteTopPages = () =>
	graphql(
		SitesTopPagesQuery,
		getTableMapper(result => result.pages.assetMetrics)
	);

const Tabs = [
	{
		getColumns: () => [
			ASSET_TITLE_COLUMN,
			{
				...DEFAULT_METRIC_COLUMN,
				...metricsListColumns.visitorsMetric,
				accessor: 'visitorsMetric.value'
			}
		],
		orderByField: 'visitorsMetric',
		rowIdentifier: ROW_IDENTIFIER,
		tabId: 'visitorsMetric',
		title: sub(Liferay.Language.get('visited-x'), [
			Liferay.Language.get('pages')
		])
	},
	{
		getColumns: () => [
			ASSET_TITLE_COLUMN,
			{
				...DEFAULT_METRIC_COLUMN,
				...metricsListColumns.entrancesMetric,
				accessor: 'entrancesMetric.value'
			}
		],
		orderByField: 'entrancesMetric',
		rowIdentifier: ROW_IDENTIFIER,
		tabId: 'entryMetric',
		title: Liferay.Language.get('entrance-pages')
	},
	{
		getColumns: () => [
			ASSET_TITLE_COLUMN,
			{
				...DEFAULT_METRIC_COLUMN,
				...metricsListColumns.exitRateMetric,
				accessor: 'exitRateMetric.value'
			}
		],
		orderByField: 'exitRateMetric',
		rowIdentifier: ROW_IDENTIFIER,
		tabId: 'exitMetric',
		title: Liferay.Language.get('exit-pages')
	}
];

export default withTableTabs(withSiteTopPages, Tabs);
