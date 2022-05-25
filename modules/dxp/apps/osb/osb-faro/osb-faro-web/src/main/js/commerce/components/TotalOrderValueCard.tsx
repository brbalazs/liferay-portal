import CommerceMetricCard from './CommerceMetricCard';
import CommerceTotalOrderValueQuery, {
	CommerceTotalOrderValueData
} from 'commerce/queries/TotalOrderValueQuery';
import React from 'react';

const TotalOrderValueCard = () => (
	<CommerceMetricCard<CommerceTotalOrderValueData>
		description={Liferay.Language.get('value-of-placed-orders')}
		label={Liferay.Language.get('total-order-value')}
		mapper={result => result?.commerceTotalOrderValue}
		Query={CommerceTotalOrderValueQuery}
	/>
);

export default TotalOrderValueCard;
