import BaseScreen from './BaseScreen';
import ClayButton from '@clayui/button';
import Modal from '../modal';
import React from 'react';

interface IWelcomeProps {
	groupId?: string;
	onClose: () => void;
	onNext: (increment?: number) => void;
}

const Welcome: React.FC<IWelcomeProps> = ({onClose, onNext}) => (
	<BaseScreen
		className='welcome'
		onClose={onClose}
		title={Liferay.Language.get('welcome-to-analytics-cloud')}
	>
		<Modal.Body className='d-flex flex-column align-items-center'>
			{/* TODO: LRAC-7427 Adjust SVGs with Linear Gradients */}
			<div className='ac-setup' />

			<span className='description'>
				{Liferay.Language.get(
					'just-a-few-more-steps-to-set-up-your-workspace'
				)}
			</span>
		</Modal.Body>

		<Modal.Footer className='d-flex justify-content-center'>
			<ClayButton autoFocus className='wide' onClick={() => onNext()}>
				{Liferay.Language.get('next')}
			</ClayButton>
		</Modal.Footer>
	</BaseScreen>
);

export default Welcome;
