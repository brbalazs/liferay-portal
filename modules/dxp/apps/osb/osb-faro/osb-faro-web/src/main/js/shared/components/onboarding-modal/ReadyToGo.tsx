import BaseScreen from './BaseScreen';
import ClayButton from '@clayui/button';
import getSVG from 'shared/util/svg';
import Modal from '../modal';
import React from 'react';

interface IReadyToGoProps {
	onClose: () => void;
}

const ReadyToGo: React.FC<IReadyToGoProps> = ({onClose}) => {
	const svg = getSVG('ac-ready-to-use');

	return (
		<BaseScreen
			className='ready-to-go'
			onClose={onClose}
			title={Liferay.Language.get('youre-ready-to-go')}
		>
			<Modal.Body className='d-flex flex-column align-items-center'>
				<svg className='ac-ready-to-use' viewBox={svg.viewBox}>
					<use xlinkHref={`#${svg.id}`} />
				</svg>

				<span className='description'>
					{Liferay.Language.get(
						'your-workspace-is-all-set-up!-we-recommend-adding-people-data-in-the-future-to-enrich-profile-data-and-know-your-audience-better'
					)}
				</span>
			</Modal.Body>

			<Modal.Footer className='d-flex justify-content-center'>
				<ClayButton className='wide' onClick={onClose}>
					{Liferay.Language.get('get-started')}
				</ClayButton>
			</Modal.Footer>
		</BaseScreen>
	);
};

export default ReadyToGo;
