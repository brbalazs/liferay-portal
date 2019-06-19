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
				case e.pathname.includes('/folder'):
					actions.getFolder(props.foldersApiEndpoint, e.pathname.replace(/\/folder\/?/, ''));
					break;
				case e.pathname.includes('/area'):
					actions.getArea(props.areaApiEndpoint, e.pathname.replace(/\/area\/?/, ''));
					break;
				default:
					break;
			}
		});
	}

	function initialize() {
		initializeUrlListener();
		if (
			window.location.pathname.indexOf('/folder') > -1 ||
			window.location.pathname.indexOf('/area') > -1
		) {
			props.history.push(window.location.pathname.replace(props.basename, ''));
		}
		if (props.spritemap) {
			actions.setSpritemap(props.spritemap);
			actions.setBasename(props.basename || '/')
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
			props.history.push('/error');
		}
	});

	return (
		<div className="content">
			<Router history={props.history}>
				<Breadcrumbs data={state.app.breadcrumbs} />
					{state.app.loading ? (
						<Loading />
					) : (
						<Switch>
							<Route exact path="/" component={BaseContainer} />
							<Route exact path="/error" component={ErrorMessage} />
							<Route path="/folder" component={FolderViewer} />
							<Route path="/area" component={AreaViewer} />
						</Switch>
					)}
			</Router>
		</div>
	);
}

export default PartFinder;
