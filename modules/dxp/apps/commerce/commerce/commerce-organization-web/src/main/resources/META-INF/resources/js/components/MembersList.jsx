import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {LIST_BY} from 'constants';
import Member from 'components/Member';

const {
	USERS,
	ACCOUNTS
} = LIST_BY;

let fakeDataChanges = 0;

function generateDummyMembersList() {
	const membersList = [];

	for (let i = 0; i < 100; i++) {
		membersList.push(
			<Member key={i}/>
		);
	}

	return membersList;
}

class MembersList extends Component {
	constructor(props) {
		super(props);

		this.state = {
			isLoading: true,
			listBy: USERS,
			data: null,
			total: 0
		};
	}

	fakeFetchMembers(listBy) {
		setTimeout(() => {
			fakeDataChanges++;


		}, 2000);
	}

	shouldComponentUpdate(nextProps, nextState) {
		/* const needsUpdate = this.props.listBy !== nextProps.listBy ||
			this.props.orgId !== nextProps.orgId;

		if (needsUpdate) {
			const {
				orgId,
				listBy
			} = nextProps;

			this.fetchMembers(orgId, listBy)
				.then(payload => ({
					isLoading: true,
					listBy,
					payload
				}))
		}

		return needsUpdate; */
		return true;
	}

	render() {
		const {
			members,
			listBy,
			isLoading
		} = this.props;



		return (
			<div className='pane-members-list'>
				{isLoading &&
				<div className='is-loading'>
					<span className='spinner fas fa-circle-notch'></span>
				</div>
				}

				{!isLoading && members.length &&
				<ul>
					{
						members.map((member, index) => {
							return (
								<Member
									key={index}
									member={member}
								/>
							);
						})
					}
				</ul>
				}
			</div>
		);
	}
}

MembersList.defaultProps = {};

MembersList.propTypes = {};

export default MembersList;
