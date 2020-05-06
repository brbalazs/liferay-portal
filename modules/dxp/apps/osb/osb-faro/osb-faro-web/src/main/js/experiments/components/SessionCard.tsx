import Card from 'shared/components/Card';
import ClayButton from '@clayui/button';
import Icon from 'shared/components/Icon';
import React from 'react';
import {useStateValue} from 'experiments/state';

const CLASSNAME = 'analytics-session-card';

interface ISessionCardProps extends React.HTMLAttributes<HTMLElement> {
	label?: string;
}

const SessionCard: React.FC<ISessionCardProps> = ({children, label}) => {
	const [{sessionViewTriggered}, dispatch]: any = useStateValue();

	return (
		<Card className={CLASSNAME} minHeight={405}>
			<Card.Header className='align-items-center d-flex justify-content-between'>
				<Card.Title>{label}</Card.Title>

				<ClayButton.Group>
					<ClayButton
						className={
							sessionViewTriggered === 'total' ? 'active' : ''
						}
						displayType='secondary'
						onClick={() =>
							dispatch({
								newAction: 'total',
								type: 'changeSessionView'
							})
						}
						small
					>
						<Icon className='mr-2' symbol='session-single-chart' />{' '}
						{Liferay.Language.get('total')}
					</ClayButton>
					<ClayButton
						className={
							sessionViewTriggered === 'per-variant'
								? 'active'
								: ''
						}
						displayType='secondary'
						onClick={() =>
							dispatch({
								newAction: 'per-variant',
								type: 'changeSessionView'
							})
						}
						small
					>
						<Icon
							className='mr-2'
							symbol='session-multiple-chart'
						/>

						{Liferay.Language.get('per-variant')}
					</ClayButton>
				</ClayButton.Group>
			</Card.Header>

			<Card.Body>{children}</Card.Body>
		</Card>
	);
};

export default SessionCard;
