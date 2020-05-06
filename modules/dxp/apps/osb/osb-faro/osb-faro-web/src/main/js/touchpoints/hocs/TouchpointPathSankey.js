import Sankey from '../components/Sankey';
import TouchpointPathQuery from '../queries/TouchpointPathQuery';
import {compose} from 'redux';
import {graphql} from '@apollo/react-hoc';
import {HOC_CARD_PROPTYPES} from 'shared/util/proptypes';
import {
	mapPropsToOptions,
	mapResultToProps
} from './mappers/touchpoint-path-query';
import {withError} from 'cerebro-shared/hocs/utils';
import {withLoading} from 'shared/hoc';

const TouchpointPathSankeyWithData = compose(
	graphql(TouchpointPathQuery, {
		options: mapPropsToOptions,
		props: mapResultToProps
	}),
	withLoading({alignCenter: true, page: false}),
	withError()
)(Sankey);

TouchpointPathSankeyWithData.propTypes = HOC_CARD_PROPTYPES;

export default TouchpointPathSankeyWithData;
