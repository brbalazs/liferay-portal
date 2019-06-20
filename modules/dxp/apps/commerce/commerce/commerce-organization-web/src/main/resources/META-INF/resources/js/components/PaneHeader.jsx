import React, {Component} from 'react';
import ContextualOptions from 'components/ContextualOptions';
import PropTypes from 'prop-types';

import PaneOrgInfo from 'components/PaneOrgInfo';
import PaneViewSelector from 'components/PaneViewSelector';
import PaneSearchBar from 'components/PaneSearchBar';

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
			id,
			orgName,
			totalSubOrg,
			listBy,
			onLookUp,
			onViewSelected,
			totalAccounts,
			totalUsers,
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
