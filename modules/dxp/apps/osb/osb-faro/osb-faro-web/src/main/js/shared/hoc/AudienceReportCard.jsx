import AudienceReport from 'shared/components/AudienceReport';
import BaseCard from 'cerebro-shared/components/base-card';
import Card from 'shared/components/Card';
import React from 'react';
import {compose} from 'redux';
import {HOC_CARD_PROPTYPES} from 'shared/util/proptypes';
import {withError, withLoading} from 'shared/hoc';

/**
 * HOC
 * @description Audience Report Card
 * @param {function} withAudienceReportCard
 */
const withAudienceReportCard = withData => {
	const AudienceReportWithData = compose(
		withData(),
		withLoading({alignCenter: true, page: false}),
		withError({page: false})
	)(AudienceReport);

	AudienceReportWithData.propTypes = HOC_CARD_PROPTYPES;

	const defaultProps = {
		className: 'analytics-audience-report-card'
	};

	const AudienceReportCard = ({
		className,
		knownIndividualsTitle,
		label,
		legacyDropdownRangeKey,
		segmentsTitle,
		uniqueVisitorsTitle,
		viewerMode
	}) => (
		<BaseCard
			className={className}
			label={label}
			legacyDropdownRangeKey={legacyDropdownRangeKey}
			minHeight={536}
		>
			{({filters, rangeSelectors, router}) => (
				<Card.Body>
					<AudienceReportWithData
						filters={filters}
						knownIndividualsTitle={knownIndividualsTitle}
						rangeSelectors={rangeSelectors}
						router={router}
						segmentsTitle={segmentsTitle}
						uniqueVisitorsTitle={uniqueVisitorsTitle}
						viewerMode={viewerMode}
					/>
				</Card.Body>
			)}
		</BaseCard>
	);

	AudienceReportCard.defaultProps = defaultProps;

	return AudienceReportCard;
};

export {withAudienceReportCard};
export default withAudienceReportCard;
