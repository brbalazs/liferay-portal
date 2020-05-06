import getFiltersMapper from 'cerebro-shared/hocs/mappers/filter';
import globalFilterAssetQuery from 'shared/queries/globalFilterAssetQuery';
import {graphql} from '@apollo/react-hoc';
import {withFilterComponent} from 'cerebro-shared/hocs/Filter';

/**
 * HOC
 * @description Documents And Media Filter
 */
const withDocumentsAndMediaFilter = () =>
	graphql(
		globalFilterAssetQuery('document', 'downloadsMetric'),
		getFiltersMapper(result => result.document.downloadsMetric)
	);

export default withFilterComponent(withDocumentsAndMediaFilter);
