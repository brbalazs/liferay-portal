import PropTypes from 'prop-types';
import React from 'react';

import ActiveViewSelector from './ActiveViewSelector.es';
import getAppContext from './Context.es';
import CreationMenu from './CreationMenu.es';
import FiltersDropdown from './FiltersDropdown.es';
import MainSearch from './MainSearch.es';

function NavBar(props) {
	const {state} = getAppContext();

	const mainFilter = state.filters.find(f => f.main);

	return (
		<nav className="management-bar management-bar-light navbar navbar-expand-md border-bottom">
			<div className="container-fluid container-fluid-max-xl">
				{state.filters.length > 1 ? (
					<div className="navbar-nav mr-2">
						<FiltersDropdown />
					</div>
				) : null}
				<div className="navbar-form navbar-overlay-sm-down pl-0">
					{mainFilter ? <MainSearch /> : null}
				</div>
				<div className="navbar-form navbar-form-autofit navbar-overlay navbar-overlay-sm-down pl-0">
					{props.views && props.views.length > 1 ? (
						<ActiveViewSelector
							activeView={props.activeView}
							setActiveView={props.setActiveView}
							views={props.views}
						/>
					) : null}
				</div>
				{props.creationMenuItems && props.creationMenuItems.length ? (
					<CreationMenu items={props.creationMenuItems} />
				) : null}
			</div>
		</nav>
	);
}

NavBar.propTypes = {
	activeView: PropTypes.number,
	creationMenuItems: PropTypes.array,
	setActiveView: PropTypes.func,
	views: PropTypes.arrayOf(
		PropTypes.shape({
			icon: PropTypes.string.isRequired,
			label: PropTypes.string.isRequired
		})
	)
};

NavBar.defaultProps = {
	creationMenuItems: []
};

export default NavBar;
