import * as API from 'shared/api';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import FaroConstants from 'shared/util/constants';
import getCN from 'classnames';
import Icon from 'shared/components/Icon';
import ListGroup from 'shared/components/list-group';
import React from 'react';
import {compose, withEmpty, withRequest} from 'shared/hoc';
import {getFormattedTitle} from 'shared/components/NoResultsDisplay';
import {Link} from 'react-router-dom';
import {NAME} from 'shared/util/pagination';
import {PropTypes} from 'prop-types';
import {Routes, toRoute} from 'shared/util/router';

const {orderAscending} = FaroConstants.pagination;

const ITEMS_PER_CARD = 6;

function fetchInterestData({groupId, id}) {
	return API.interests.search({
		contactsEntityId: id,
		delta: ITEMS_PER_CARD,
		groupId,
		orderByFields: [
			{
				fieldName: NAME,
				orderBy: orderAscending,
				system: true
			}
		]
	});
}

export const InterestsList = ({channelId, groupId, id, interests}) => (
	<ListGroup className='results-container' noBorder>
		{interests.map(({name}) => (
			<ListGroup.Item className='interest' key={name}>
				<ListGroup.ItemTitle className='text-truncate'>
					{name ? (
						<Link
							to={toRoute(
								Routes.CONTACTS_INDIVIDUAL_INTEREST_DETAILS,
								{
									channelId,
									groupId,
									id,
									interestId: name
								}
							)}
						>
							{name}
						</Link>
					) : (
						name
					)}
				</ListGroup.ItemTitle>
			</ListGroup.Item>
		))}
	</ListGroup>
);

const ListWithInterests = compose(
	withRequest(
		fetchInterestData,
		data => ({interests: data.items, total: data.total}),
		{
			page: false
		}
	),
	withEmpty({
		spacer: true,
		title: getFormattedTitle(Liferay.Language.get('interests'))
	})
)(InterestsList);

export default class InterestsCard extends React.PureComponent {
	static propTypes = {
		channelId: PropTypes.string,
		entity: PropTypes.object.isRequired,
		groupId: PropTypes.string.isRequired
	};

	render() {
		const {
			props: {
				channelId,
				className,
				entity: {id},
				groupId
			}
		} = this;

		const classes = getCN('individual-interests-card-root', className);

		return (
			<Card className={classes}>
				<Card.Header>
					<Card.Title>
						{Liferay.Language.get('current-interests')}
					</Card.Title>
				</Card.Header>

				<ListWithInterests
					channelId={channelId}
					groupId={groupId}
					id={id}
				/>

				<Card.Footer>
					<Button
						display='link'
						href={toRoute(Routes.CONTACTS_INDIVIDUAL_INTERESTS, {
							channelId,
							groupId,
							id
						})}
						size='sm'
					>
						{Liferay.Language.get('view-all-interests')}

						<Icon symbol='angle-right' />
					</Button>
				</Card.Footer>
			</Card>
		);
	}
}
