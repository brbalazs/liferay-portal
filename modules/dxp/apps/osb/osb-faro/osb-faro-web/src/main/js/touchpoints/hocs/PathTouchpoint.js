import AssetsQuery from '../queries/AssetsQuery';
import SankeyTouchpoint from '../components/SankeyTouchpoint';
import {compose} from 'redux';
import {graphql} from '@apollo/react-hoc';
import {
	mapPropsToOptions,
	mapResultToProps
} from './mappers/touchpoint-assets-list-query';

export default compose(
	graphql(AssetsQuery, {
		options: mapPropsToOptions,
		props: mapResultToProps
	})
)(SankeyTouchpoint);
