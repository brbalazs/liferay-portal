import BarChartHTML from 'cerebro-shared/components/BarChartHTML';
import BaseCard from 'cerebro-shared/components/base-card';
import Card from 'shared/components/Card';
import FormMetricsQuery from '../queries/FormMetricsQuery';
import React from 'react';
import {compose} from 'redux';
import {graphql} from '@apollo/react-hoc';
import {HOC_CARD_PROPTYPES} from 'shared/util/proptypes';
import {
	mapPropsToOptions,
	mapResultToProps
} from './mappers/form-abandonment-query';
import {withEmpty, withError} from 'cerebro-shared/hocs/utils';
import {withLoading} from 'shared/hoc';

const FormAbandonmentWithData = compose(
	graphql(FormMetricsQuery, {
		options: mapPropsToOptions,
		props: mapResultToProps
	}),
	withLoading({alignCenter: true, page: false}),
	withError(),
	withEmpty({
		emptyTitle: Liferay.Language.get('empty-message-form-abandoment')
	})
)(BarChartHTML);

FormAbandonmentWithData.propTypes = HOC_CARD_PROPTYPES;

const defaultProps = {
	className: 'analytics-form-abandonment-card'
};

const FormAbandonmentCard = ({className, label}) => (
	<BaseCard className={className} label={label} minHeight={536}>
		{({filters, rangeSelectors, router}) => (
			<Card.Body>
				<FormAbandonmentWithData
					filters={filters}
					rangeSelectors={rangeSelectors}
					router={router}
				/>
			</Card.Body>
		)}
	</BaseCard>
);

FormAbandonmentCard.defaultProps = defaultProps;

export {FormAbandonmentCard};
export default FormAbandonmentCard;
