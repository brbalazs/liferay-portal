import AssetsListCard from '../components/AssetsListCard';
import AssetsQuery from '../queries/AssetsQuery';
import BaseCard from 'cerebro-shared/components/base-card';
import React from 'react';
import {compose} from 'redux';
import {graphql} from '@apollo/react-hoc';
import {HOC_CARD_PROPTYPES} from 'shared/util/proptypes';
import {
	mapPropsToOptions,
	mapResultToProps
} from './mappers/touchpoint-assets-list-query';
import {withEmpty} from 'cerebro-shared/hocs/utils';
import {withError, withLoading} from 'shared/hoc';

const AssetsListWithData = compose(
	graphql(AssetsQuery, {
		options: mapPropsToOptions,
		props: mapResultToProps
	}),
	withLoading({alignCenter: true, page: false}),
	withError({page: false}),
	withEmpty({emptyTitle: Liferay.Language.get('empty-message-assets-card')})
)(AssetsListCard);

AssetsListWithData.propTypes = HOC_CARD_PROPTYPES;

const defaultProps = {
	className: 'analytics-assets-list-card'
};

const AssetsListBaseCard = ({className, label}) => (
	<BaseCard
		className={className}
		label={label}
		legacyDropdownRangeKey={false}
		minHeight={536}
	>
		{({filters, rangeSelectors, router}) => (
			<AssetsListWithData
				filters={filters}
				rangeSelectors={rangeSelectors}
				router={router}
			/>
		)}
	</BaseCard>
);

AssetsListBaseCard.defaultProps = defaultProps;

export default AssetsListBaseCard;
