import React, {Component} from 'react';
import PropTypes from 'prop-types';

import {bindAll} from 'lodash';

import PaneHeader from 'components/PaneHeader';
import MembersList from 'components/MembersList';
import {LIST_BY} from 'constants';
import {fetchData} from 'utils';

const {USERS, ACCOUNTS} = LIST_BY;

let membersListCopy = [];

function fetchMembers(orgId, listBy) {
	// TODO logic for pagination
	/* return fetch('/api/members/members.json')
		.then(response => response.json()); */

	return fetchData(orgId)
		.then(response => response.json())
		.then(data => {
			const {
				id,
				name: orgName,
				total: totalSubOrg,
				userList,
				accountList
			} = data;

			const whichMembers = listBy === USERS ?
				'user' : 'account';

			return {
				id,
				orgName,
				totalSubOrg,
				totalUsers: userList.total,
				totalAccounts: accountList.total,
				members: data[`${whichMembers}List`][`${whichMembers}s`]

			};
		})
}

function filterMembers(name, members) {
	return members.filter(
		member => member.name
			.toLowerCase()
			.includes(name.toLowerCase())
	);
}

function shouldPaneOpen(id, members) {
	return !!id && members && members.length;
}

class MembersPane extends Component {
	constructor(props) {
		super(props);

		this.state = {
			id: 0,
			searchQuery: '',
			listBy: USERS,
			isLoading: true
		};

		bindAll(
			this,
			'handleListBy',
			'handleLookUp',
			'handleUpdate'
		);
	}

	componentDidMount() {
		const {id} = this.props;
		const {listBy} = this.state;

		this.handleUpdate(id, listBy);
	}

	componentDidUpdate(prevProps, prevState) {
		const {id} = this.props;
		const {listBy} = this.state;

		if (id !== prevProps.id || listBy !== prevState.listBy) {
			this.handleUpdate(id, listBy)
		}
	}

	handleListBy(listBy) {
		this.setState(() => ({
			listBy
		}));
	}

	handleLookUp(e) {
		const name = e.target.value;
		const {id} = this.props;
		const fromState = !!name && name.length ?
			filterMembers(name, this.state.members) : membersListCopy;

		if (fromState.length) {
			this.setState(() => ({
				members: fromState
			}))
		}
		else {
			// TODO: API's should have a search endpoint / searchable
			fetchMembers(id)
				.then(({results}) => {
					this.setState(() => {
						if (!!results && results.length) {
							const fromFetch = filterMembers(name, results);

							return fromFetch.length ?
								{members: fromFetch} :
								{members: membersListCopy}
						}

						return {members: membersListCopy};
					});
				})
		}
	}

	handleUpdate(id, listBy) {
		fetchMembers(id, listBy)
			.then((data) => {
				this.setState(state => Object.assign(
					state,
					data,
					{
						isLoading: false
					}), () => {
					membersListCopy = this.state.members;
				});
			})
			.catch(() => {});
	}

	render() {
		const {
				orgName,
				members,
				totalSubOrg,
				totalUsers,
				totalAccounts,
				listBy,
				id
			} = this.state,
			paneClasses = `pane${(shouldPaneOpen(id, members)) ? ' pane-open' : ''}`;

		return (
			<div className={paneClasses}>
				<PaneHeader
					orgName={orgName}
					totalSubOrg={totalSubOrg}
					totalUsers={totalUsers}
					totalAccounts={totalAccounts}
					listBy={this.state.listBy}
					onViewSelected={this.handleListBy}
					onLookUp={this.handleLookUp}
				/>

				{<MembersList
					listBy={listBy}
					members={members}
					isLoading={this.state.isLoading}
				/>}
			</div>
		);
	}
}

PropTypes.defaultProps = {
	data: {},
	id: 0
};

MembersPane.propTypes = {
	data: PropTypes.object,
	id: PropTypes.number
};

export default MembersPane;
