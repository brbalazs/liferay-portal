import React, { useState, useEffect, useContext } from 'react';

import { Router, Route, Switch } from 'react-router-dom';

import history from '../utilities/history.es';
import { StoreContext } from './StoreContext.es';
import FolderViewer from './FolderViewer.es';
import Loading from './Loading.es';
import Breadcrumbs from './Breadcrumbs.es';
import ErrorMessage from './ErrorMessage.es';
import BaseContainer from './BaseContainer.es';
import AreaViewer from './areas/AreaViewer.es';

export function PartFinder(props) {

	const [initialized, setInitialized] = useState(false);
	const { state, actions } = useContext(StoreContext);

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
