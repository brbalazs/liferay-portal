import AlertFeed from 'shared/components/AlertFeed';
import autobind from 'autobind-decorator';
import BundleRouter from './route-middleware/BundleRouter';
import ChannelProvider from 'shared/context/channel';
import client from 'shared/apollo/client';
import configureStore from 'shared/store/configure-store';
import ErrorPage from 'shared/pages/ErrorPage';
import Loading from './shared/pages/Loading';
import ModalRenderer from 'shared/components/ModalRenderer';
import pathToRegexp from 'path-to-regexp';
import React, {lazy, Suspense} from 'react';
import RouteNotFound from 'shared/components/RouteNotFound';
import Tooltip from 'shared/components/Tooltip';
import UnassignedSegmentsProvider from 'shared/context/unassignedSegments';
import {ApolloProvider} from '@apollo/react-components';
import {ApolloProvider as ApolloProviderHooks} from '@apollo/react-hooks';
import {ClayIconSpriteContext} from '@clayui/icon';
import {ClayLinkContext} from '@clayui/link';
import {close, modalTypes, open} from 'shared/actions/modals';
import {connect, Provider} from 'react-redux';
import {
	Link,
	Route,
	BrowserRouter as Router,
	Switch,
	withRouter
} from 'react-router-dom';
import {loadState, saveState} from 'shared/store/local-storage';
import {OnboardingContext} from 'shared/context/onboarding';
import {PropTypes} from 'prop-types';
import {Routes} from 'shared/util/router';
import {setBackURL} from 'shared/actions/settings';
import {spritemap} from 'shared/util/constants';
import {throttle} from 'lodash';
import {WarningStripeContext} from 'shared/context/WarningStripe';

// App Routes with Sidebar

const AppSidebarRoutes = lazy(() =>
	import(
		/* webpackChunkName: "AppSidebarRoutes" */ './shared/pages/AppSidebarRoutes'
	)
);

// Workspaces

const AddWorkspace = lazy(() =>
	import(/* webpackChunkName: "AddWorkspace" */ './shared/pages/AddWorkspace')
);
const SelectWorkspaceAccount = lazy(() =>
	import(
		/* webpackChunkName: "SelectWorkspaceAccount" */ './shared/pages/SelectWorkspaceAccount'
	)
);
const Workspaces = lazy(() =>
	import(/* webpackChunkName: "Workspaces" */ './shared/pages/Workspaces')
);

// Settings

const Settings = lazy(() => import('./settings/pages/Settings'));

// Other

const OAuthReceive = lazy(() =>
	import(
		/* webpackChunkName: "OAuthReceive" */ './settings/pages/OAuthReceive'
	)
);

const SETTINGS_PATH_REGEX = pathToRegexp(Routes.SETTINGS, null, {end: false});

@withRouter
@connect(null)
class RoutesContainer extends React.Component {
	static propTypes = {
		dispatch: PropTypes.func.isRequired
	};

	componentDidUpdate(prevProps) {
		if (this.props.location !== prevProps.location) {
			this.onRouteChanged();
		}
	}

	onRouteChanged() {
		if (!SETTINGS_PATH_REGEX.test(window.location.pathname)) {
			this.props.dispatch(
				setBackURL(
					`${window.location.pathname}${window.location.search}`
				)
			);
		}
	}

	render() {
		const {children, location} = this.props;

		return location && location.state && location.state.notFoundError ? (
			<ErrorPage />
		) : (
			children
		);
	}
}

export default class App extends React.Component {
	static defaultProps = {
		setup: true
	};

	static propTypes = {
		setup: PropTypes.bool
	};

	state = {
		onboardingTriggered: false,
		showWarningStripe: true
	};

	constructor(props) {
		super(props);

		this._store = configureStore(loadState());

		this._store.subscribe(
			throttle(() => saveState(this._store.getState()), 1000)
		);
	}

	@autobind
	handleUserConfirmation(message, callback) {
		this._store.dispatch(
			open(modalTypes.CONFIRMATION_MODAL, {
				cancelMessage: Liferay.Language.get('stay-on-page'),
				message,
				modalVariant: 'modal-warning',
				onClose: () => {
					callback(false);

					this._store.dispatch(close());
				},
				onSubmit: () => {
					callback(true);
				},
				submitButtonDisplay: 'warning',
				submitMessage: Liferay.Language.get('leave-page'),
				title: Liferay.Language.get('unsaved-changes'),
				titleIcon: 'warning-full'
			})
		);
	}

	render() {
		const {onboardingTriggered, showWarningStripe} = this.state;

		return (
			<ApolloProvider client={client}>
				<ApolloProviderHooks client={client}>
					<Provider store={this._store}>
						<ClayIconSpriteContext.Provider value={spritemap}>
							<ClayLinkContext.Provider
								value={({children, href, ...otherProps}) => (
									<Link to={href} {...otherProps}>
										{children}
									</Link>
								)}
							>
								<UnassignedSegmentsProvider>
									<WarningStripeContext.Provider
										value={{
											setShowWarningStripe: value =>
												this.setState({
													showWarningStripe: value
												}),
											showWarningStripe
										}}
									>
										<OnboardingContext.Provider
											value={{
												onboardingTriggered,
												setOnboardingTriggered: () =>
													this.setState({
														onboardingTriggered: true
													})
											}}
										>
											<ChannelProvider>
												{/* eslint-disable react/jsx-handler-names */}
												<Router
													getUserConfirmation={
														this
															.handleUserConfirmation
													}
												>
													{/* eslint-enable react/jsx-handler-names */}
													<RoutesContainer>
														<AlertFeed />

														<Tooltip />

														<ModalRenderer />

														<Suspense
															fallback={
																<Loading />
															}
														>
															<Switch>
																<BundleRouter
																	data={
																		Workspaces
																	}
																	exact
																	path={
																		Routes.BASE
																	}
																/>

																<BundleRouter
																	data={
																		Workspaces
																	}
																	exact
																	path={
																		Routes.WORKSPACES
																	}
																/>

																<BundleRouter
																	data={
																		SelectWorkspaceAccount
																	}
																	exact
																	path={
																		Routes.WORKSPACE_ADD
																	}
																/>

																<BundleRouter
																	data={
																		AddWorkspace
																	}
																	exact
																	path={
																		Routes.WORKSPACE_ADD_TRIAL
																	}
																/>

																<BundleRouter
																	data={
																		AddWorkspace
																	}
																	exact
																	path={
																		Routes.WORKSPACE_ADD_WITH_CORP_PROJECT_UUID
																	}
																/>

																<BundleRouter
																	data={
																		SelectWorkspaceAccount
																	}
																	exact
																	path={
																		Routes.WORKSPACE_SELECT_ACCOUNT
																	}
																/>

																<BundleRouter
																	data={
																		OAuthReceive
																	}
																	exact
																	path={
																		Routes.OAUTH_RECEIVE
																	}
																/>

																<Route
																	component={
																		Loading
																	}
																	path={
																		Routes.LOADING
																	}
																/>

																<BundleRouter
																	data={
																		Settings
																	}
																	path={
																		Routes.SETTINGS
																	}
																/>

																<BundleRouter
																	data={
																		AppSidebarRoutes
																	}
																	path={
																		Routes.CHANNEL
																	}
																/>

																<RouteNotFound />
															</Switch>
														</Suspense>
													</RoutesContainer>
												</Router>
											</ChannelProvider>
										</OnboardingContext.Provider>
									</WarningStripeContext.Provider>
								</UnassignedSegmentsProvider>
							</ClayLinkContext.Provider>
						</ClayIconSpriteContext.Provider>
					</Provider>
				</ApolloProviderHooks>
			</ApolloProvider>
		);
	}
}
