import AssetsTouchpointQuery from '../queries/AssetsTouchpointQuery';
import BaseCard from 'cerebro-shared/components/base-card';
import React from 'react';
import TouchpointsListCard from '../components/TouchpointsListCard';
import {compose} from 'redux';
import {graphql} from '@apollo/react-hoc';
import {HOC_CARD_PROPTYPES} from 'shared/util/proptypes';
import {
	mapPropsToOptions,
	mapResultToProps
} from './mappers/touchpoint-list-query';
import {PropTypes} from 'prop-types';
import {withEmpty} from 'cerebro-shared/hocs/utils';
import {withError, withLoading} from 'shared/hoc';

const TouchpointListWithData = compose(
	graphql(AssetsTouchpointQuery, {
		options: mapPropsToOptions,
		props: mapResultToProps
	}),
	withLoading({alignCenter: true, page: false}),
	withError({page: false}),
	withEmpty({emptyTitle: Liferay.Language.get('empty-message-pages-card')})
)(TouchpointsListCard);

TouchpointListWithData.propTypes = HOC_CARD_PROPTYPES;

const propTypes = {
	assetType: PropTypes.string
};

const defaultProps = {
	className: 'analytics-touchpoints-list-card'
};

const TouchpointsListBaseCard = ({assetType, className, label}) => (
	<BaseCard className={className} label={label} minHeight={536}>
		{({filters, rangeSelectors, router}) => (
			<TouchpointListWithData
				assetType={assetType}
				filters={filters}
				rangeSelectors={rangeSelectors}
				router={router}
			/>
		)}
	</BaseCard>
);

TouchpointsListBaseCard.propTypes = propTypes;
TouchpointsListBaseCard.defaultProps = defaultProps;

export default TouchpointsListBaseCard;
