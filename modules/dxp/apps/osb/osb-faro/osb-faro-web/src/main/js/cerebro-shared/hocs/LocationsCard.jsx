import BaseCard from 'cerebro-shared/components/base-card';
import Card from 'shared/components/Card';
import GeoMap from 'cerebro-shared/components/GeoMapCard';
import React from 'react';
import {compose} from 'redux';
import {HOC_CARD_PROPTYPES} from 'shared/util/proptypes';
import {PropTypes} from 'prop-types';
import {withError} from 'shared/hoc/util';

/**
 * HOC
 * @description Locations Card Loading
 */
const withLoading = () => Component => ({loading, ...props}) => (
	<Component loading={loading} {...props} />
);

/**
 * HOC
 * @description Locations Card Empty
 */
const withEmpty = () => Component => ({empty, ...props}) => (
	<Component empty={empty} {...props} />
);

/**
 * HOC
 * @description Locations Card Data
 */
const withLocationsCard = (withLocations, withCountries) => {
	const LocationsGeoMap = compose(
		withLocations(),
		withCountries(),
		withLoading(),
		withError({page: false}),
		withEmpty()
	)(GeoMap);

	LocationsGeoMap.propTypes = HOC_CARD_PROPTYPES;

	const defaultProps = {
		className: 'analytics-locations-card',
		metricLabel: Liferay.Language.get('views')
	};

	const propTypes = {
		metricLabel: PropTypes.string
	};

	const LocationsCard = ({
		className,
		label,
		legacyDropdownRangeKey,
		metricLabel
	}) => (
		<BaseCard
			className={className}
			label={label}
			legacyDropdownRangeKey={legacyDropdownRangeKey}
			minHeight={536}
		>
			{({filters, rangeSelectors, router}) => (
				<Card.Body>
					<LocationsGeoMap
						filters={filters}
						height={400}
						metricLabel={metricLabel}
						rangeSelectors={rangeSelectors}
						router={router}
						width='calc(60% - 2rem)'
					/>
				</Card.Body>
			)}
		</BaseCard>
	);

	LocationsCard.defaultProps = defaultProps;
	LocationsCard.propTypes = propTypes;

	return LocationsCard;
};

export {withLocationsCard};
export default withLocationsCard;
