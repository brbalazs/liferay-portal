import React, { useMemo, useState, useEffect, useContext } from 'react';

import { Router, Route, Switch } from 'react-router-dom';

import { StoreContext } from './StoreContext.es';
import FolderViewer from './FolderViewer.es';
import Loading from './Loading.es';
import Breadcrumbs from './Breadcrumbs.es';
import ErrorMessage from './ErrorMessage.es';
import BaseContainer from './BaseContainer.es';
import AreaViewer from './areas/AreaViewer.es';
import Connector from '../utilities/data_connectors/Connector.es';

export function PartFinder(props) {

	const [initialized, setInitialized] = useState(false);
	const { state, actions } = useContext(StoreContext);

	const connector  = useMemo(() => {
		if(props.connectorSettings) {
			return new Connector(props.connectorSettings)
		}
		return null
	}, props.connectorSettings)

	function initializeUrlListener() {
		return props.history.listen(e => {
			switch (true) {
				case e.pathname.includes('/folders'):
					actions.getFolder(props.foldersApiEndpoint, e.pathname.replace(/\/folders\/?/, ''));
					break;
				case e.pathname.includes('/areas'):
					actions.getArea(props.areaApiEndpoint, e.pathname.replace(/\/areas\/?/, ''));
					break;
				default:
					break;
			}
		});
	}

	function initialize() {
		initializeUrlListener();
		if (
			window.location.pathname.indexOf('/folders') > -1 ||
			window.location.pathname.indexOf('/areas') > -1
		) {
			props.history.push(window.location.pathname.replace(props.basename, ''));
		}
		actions.setSpritemap(props.spritemap);
		actions.setBasename(props.basename || '/');
		actions.setBasePathUrl(props.basePathUrl);
		setInitialized(true);
	}

	useEffect(() => {
		if (!initialized) {
			initialize();
		}
	});

	if(state.app.error) {
		return (<ErrorMessage />)
	}

	return (
		<div className="content">
			<Router history={props.history}>
				<Breadcrumbs data={state.app.breadcrumbs} />
					{state.app.loading ? (
						<Loading />
					) : (
						<Switch>
							<Route exact path="/" component={BaseContainer} />
							<Route path="/folders" component={FolderViewer} />
							<Route path="/areas" component={AreaViewer} />
						</Switch>
					)}
			</Router>
		</div>
	);
}

export default PartFinder;
