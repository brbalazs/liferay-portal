import ErrorPage from 'shared/pages/ErrorPage';
import React from 'react';
import {matchPath} from 'react-router-dom';
import {Routes} from 'shared/util/router';

const checkValidChannel = WrappedComponent => ({
	channels,
	location,
	...otherProps
}) => {
	const hasChannel = matchPath(location.pathname, {path: Routes.CHANNEL});

	if (
		hasChannel &&
		hasChannel.params.channelId &&
		!channels.some(({id}) => id === hasChannel.params.channelId)
	) {
		return <ErrorPage />;
	}

	return (
		<WrappedComponent
			channels={channels}
			location={location}
			{...otherProps}
		/>
	);
};

export default checkValidChannel;
