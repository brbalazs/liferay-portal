import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { LIST_BY } from 'constants';

const {
	USERS,
	ACCOUNTS
} = LIST_BY;

function isSelected(listBy, callee) {
	return listBy === callee ? 'selected-pane': '';
}

function Tab(props) {
	const {
		viewName,
		onViewSelected,
		totalMembers,
		listBy
	} = props;

	return(
		<span
			className={isSelected(listBy, viewName)}
			onClick={onViewSelected.bind(this, viewName)}
			role='button' tabIndex='-1'>
			{`${viewName} (${totalMembers})`}
		</span>
	);
}

class PaneViewSelector extends Component {
	render() {
		const { totalAccounts, totalUsers, onViewSelected, listBy } = this.props;

		return(
			<div className='pane-list-selector'>
				{
					[USERS, ACCOUNTS].map((viewName, i) => {
						const totalMembers = viewName === USERS ?
							totalUsers : totalAccounts;

						return (
							<Tab
								key={i}
								viewName={viewName}
								onViewSelected={onViewSelected}
								totalMembers={totalMembers}
								listBy={listBy}
							/>
						);
					})
				}
			</div>
		);
	}
}

PaneViewSelector.defaultProps = {};

PaneViewSelector.propTypes = {};

export default PaneViewSelector;
