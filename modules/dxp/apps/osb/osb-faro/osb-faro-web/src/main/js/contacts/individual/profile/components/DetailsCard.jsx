import Avatar from 'shared/components/Avatar';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import getCN from 'classnames';
import Icon from 'shared/components/Icon';
import moment from 'moment';
import React from 'react';
import {Individual} from 'shared/util/records';
import {PropTypes} from 'prop-types';
import {Routes, toRoute} from 'shared/util/router';
import {sub} from 'shared/util/lang';

export default class DetailsCard extends React.PureComponent {
	static defaultProps = {
		entity: new Individual()
	};

	static propTypes = {
		channelId: PropTypes.string,
		entity: PropTypes.instanceOf(Individual),
		groupId: PropTypes.string.isRequired
	};

	render() {
		const {channelId, className, entity, groupId} = this.props;

		const individual = entity.toJS();

		const {
			dateCreated,
			id,
			name,
			properties: {email, jobTitle}
		} = individual;

		return (
			<Card className={getCN('individual-details-card-root', className)}>
				<Card.Body>
					<Avatar circle entity={individual} size='xl' />

					{name && <h4>{name}</h4>}

					{jobTitle && <div className='job-title'>{jobTitle}</div>}

					{email && <div className='email'>{email}</div>}

					{dateCreated && (
						<div className='first-seen'>
							{sub(Liferay.Language.get('first-seen-x'), [
								moment(dateCreated).format('LL')
							])}
						</div>
					)}
				</Card.Body>

				<Card.Footer>
					<Button
						display='link'
						href={toRoute(Routes.CONTACTS_INDIVIDUAL_DETAILS, {
							channelId,
							groupId,
							id
						})}
						size='sm'
					>
						{Liferay.Language.get('view-all-details')}

						<Icon symbol='angle-right' />
					</Button>
				</Card.Footer>
			</Card>
		);
	}
}
