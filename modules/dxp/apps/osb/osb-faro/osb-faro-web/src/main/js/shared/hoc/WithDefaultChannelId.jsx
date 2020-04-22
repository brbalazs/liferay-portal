import ErrorPage from 'shared/pages/ErrorPage';
import FaroConstants from 'shared/util/constants';
import React from 'react';
import withAction from './WithAction';
import {compose} from 'redux';
import {fetchDefaultChannelId} from '../actions/preferences';
import {matchPath} from 'react-router-dom';
import {RemoteData} from '../util/records';
import {Routes} from 'shared/util/router';

const {
	preferencesScopes: {user}
} = FaroConstants;

export default compose(
	withAction(
		({groupId}) => fetchDefaultChannelId(groupId),

		state =>
			state.getIn(
				['preferences', user, 'defaultChannelId'],
				new RemoteData()
			),
		{propName: 'defaultChannelId'}
	),
	WrappedComponent => ({channels, location, ...otherProps}) => {
		const hasChannel = matchPath(location.pathname, {
			path: Routes.CHANNEL
		});

		if (
			hasChannel &&
			hasChannel.params.channelId &&
			channels.filter(({id}) => id === hasChannel.params.channelId)
				.length == 0
		) {
			return <ErrorPage />;
		} else {
			return (
				<WrappedComponent
					{...otherProps}
					channels={channels}
					location={location}
				/>
			);
		}
	}
);
