import Icon from 'shared/components/Icon';
import Modal from 'shared/components/modal';
import React from 'react';

interface IIssueSubmittedProps {
	onClose: () => void;
	onNext?: (increment?: number) => void;
}

const IssueSubmitted: React.FC<IIssueSubmittedProps> = ({onClose}) => (
	<>
		<Modal.Header
			className='title-modal'
			onClose={onClose}
			title={Liferay.Language.get('issue-submitted')}
		/>

		<Modal.Body className='d-flex flex-column align-items-center'>
			<Icon className='mb-5' size='xxxl' symbol='ac-no-sites' />

			<p className='sub-title'>
				{Liferay.Language.get('message-recieved')}
			</p>

			<p className='description mb-5'>
				{Liferay.Language.get(
					'thanks-for-your-contribution-well-look-in-to-this-as-soon-as-possible'
				)}
			</p>
		</Modal.Body>
	</>
);

export default IssueSubmitted;
