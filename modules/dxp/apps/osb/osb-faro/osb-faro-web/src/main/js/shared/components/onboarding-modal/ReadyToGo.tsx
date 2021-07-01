import BaseScreen from './BaseScreen';
import Button from 'shared/components/Button';
import Modal from '../modal';
import React from 'react';

interface IReadyToGoProps {
	onClose: () => void;
}

const ReadyToGo: React.FC<IReadyToGoProps> = ({onClose}) => (
	<BaseScreen className='ready-to-go' onClose={onClose}>
		<Modal.Body className='d-flex flex-column align-items-center'>
			{/* TODO: LRAC-7427 Adjust SVGs with Linear Gradients */}
			<div className='ac-ready-to-use' />

			<span className='title d-flex justify-content-center'>
				{Liferay.Language.get('youre-ready-to-go')}
			</span>

			<span className='description'>
				{Liferay.Language.get(
					'your-workspace-is-all-set-up!-we-recommend-adding-people-data-in-the-future-to-enrich-profile-data-and-know-your-audience-better'
				)}
			</span>

			<Button display='primary' onClick={onClose}>
				{Liferay.Language.get('get-started')}
			</Button>
		</Modal.Body>
	</BaseScreen>
);

export default ReadyToGo;
