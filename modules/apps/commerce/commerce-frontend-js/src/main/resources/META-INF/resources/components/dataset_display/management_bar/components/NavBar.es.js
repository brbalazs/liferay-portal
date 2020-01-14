import React from 'react';
import getAppContext from './Context.es';
import FiltersDropdown from './FiltersDropdown.es';
import MainSearch from './MainSearch.es';
import CreationMenu from './CreationMenu.es';

import PropTypes from 'prop-types';

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
				<div className="navbar-form navbar-form-autofit navbar-overlay navbar-overlay-sm-down pl-0">
					{mainFilter ? <MainSearch /> : null}
				</div>
				{(props.creationMenuItems && props.creationMenuItems.length) ? (
					<CreationMenu items={props.creationMenuItems}/>
				) : null}
			</div>
		</nav>
	);
};

NavBar.propTypes = {
	creationMenuItems: PropTypes.array
}

NavBar.defaultProps = {
	creationMenuItems: []
}

export default NavBar;
