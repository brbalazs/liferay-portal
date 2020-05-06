import React from 'react';
import TimeRangeQuery from 'shared/queries/TimeRangeQuery';
import {getRangeKeyFromTimeRange} from 'shared/util/util';
import {Query} from '@apollo/react-components';

const WrappedPageComponent = ({Component, ...props}) => (
	<Query query={TimeRangeQuery}>
		{({data, loading}) => {
			if (loading) return null;

			const {timeRange} = data;

			const rangeKey = getRangeKeyFromTimeRange(timeRange);

			return <Component {...props} rangeKey={rangeKey} />;
		}}
	</Query>
);

export default WrappedPageComponent;
