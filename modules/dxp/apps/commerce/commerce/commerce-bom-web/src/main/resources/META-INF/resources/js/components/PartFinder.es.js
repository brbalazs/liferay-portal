import React, { useContext, useEffect, useMemo, useState } from 'react';

import { Route, Router, Switch } from 'react-router-dom';

import AreaViewer from './areas/AreaViewer.es';
import BaseContainer from './BaseContainer.es';
import Breadcrumbs from './Breadcrumbs.es';
import Connector from '../utilities/data_connectors/Connector.es';
import ErrorMessage from './ErrorMessage.es';
import FolderViewer from './FolderViewer.es';
import history from '../utilities/history.es';
import Loading from './Loading.es';
import { StoreContext } from './StoreContext.es';

export function PartFinder(props) {

	const [initialized, setInitialized] = useState(false);
	const {state, actions} = useContext(StoreContext);

	const connector = useMemo(() => new Connector(props.connectorSettings), props.connectorSettings);

	function initializeUrlListener() {
		return history.listen(e => {
			if (e.pathname.indexOf('/folder') > -1) {
				actions.getFolder(props.foldersApiEndpoint + e.pathname.replace(/\/folder/, ''));
			}
			if (e.pathname.indexOf('/area') > -1) {
				actions.getArea(props.areaApiEndpoint + e.pathname.replace(/\/area/, ''));
			}
		});
	}

	function initialize() {
		initializeUrlListener();
		if (
			window.location.pathname.indexOf('/folder') > -1 ||
			window.location.pathname.indexOf('/area') > -1
		) {
			history.push(window.location.pathname);
		}
		if (props.spritemap) {
			actions.setSpritemap(props.spritemap);
		}
		setInitialized(true);
	}

	useEffect(() => {
		if (!initialized) {
			initialize();
		}
		if (
			state.app.error &&
			!(window.location.pathname.indexOf('/error') > -1)
		) {
			history.push('/error');
		}
	});

	return (
		<div className="content">
			<Router history={history}>
				<Breadcrumbs data={state.app.breadcrumbs} />
					{state.app.loading ? (
						<Loading />
					) : (
						<Switch>
							<Route component={BaseContainer} exact path="/" />
							<Route component={ErrorMessage} exact path="/error" />
							<Route component={FolderViewer} path="/folder" />
							<Route component={AreaViewer} path="/area" />
						</Switch>
					)}
			</Router>
		</div>
	);
}

export default PartFinder;
