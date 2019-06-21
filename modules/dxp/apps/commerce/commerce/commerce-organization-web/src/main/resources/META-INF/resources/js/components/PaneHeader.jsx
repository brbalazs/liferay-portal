import React, {Component} from 'react';
import PropTypes from 'prop-types';

import PaneOrgInfo from './PaneOrgInfo';
import PaneViewSelector from './PaneViewSelector';
import PaneSearchBar from './PaneSearchBar';

class PaneHeader extends Component {
	constructor() {
		super();

		this.state = {
			showMenu: false
		};

		_.bindAll(
			this,
			'hideMenu',
			'showMenu'
		);
	}

	hideMenu(e) {
		this.setState(() => ({
			showMenu: false
		}))
	}

	showMenu() {
		this.setState(state => ({
			showMenu: !state.showMenu
		}))
	}

	render() {
		const {
			orgName,
			totalSubOrg,
			listBy,
			onLookUp,
			onViewSelected,
			totalAccounts,
			totalUsers,
			spritemap
		} = this.props;

		return (
			<div className='pane-header'>
				<PaneOrgInfo
					orgName={orgName}
					childrenNo={totalSubOrg}
					showMenu={this.showMenu}
				/>

				<PaneViewSelector
					listBy={listBy}
					totalAccounts={totalAccounts}
					totalUsers={totalUsers}
					onViewSelected={onViewSelected}
				/>

				<PaneSearchBar
					onLookUp={onLookUp}
					spritemap={spritemap}
				/>
			</div>
		);
	}
}

PaneHeader.defaultProps = {
	data: {},
	listBy: 'users',
	onViewSelected: () => {},
	totalMembers: 0

};

PaneHeader.propTypes = {
	data: PropTypes.object,
	listBy: PropTypes.string,
	onViewSelected: PropTypes.func,
	totalMembers: PropTypes.number
};

export default PaneHeader;
