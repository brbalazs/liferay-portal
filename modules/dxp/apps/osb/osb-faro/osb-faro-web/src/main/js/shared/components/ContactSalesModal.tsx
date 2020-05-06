import HubspotForm from 'shared/components/HubspotForm';
import Modal from './modal';
import React from 'react';
import {addAlert, alertTypes} from 'shared/actions/alerts';
import {connect} from 'react-redux';

const FORM_ID = '32cf039a-7a47-4461-82c5-e694d9f29057';
const PORTAL_ID = '252686';

interface IContactSalesModalProps extends React.HTMLAttributes<HTMLElement> {
	addAlert: (object) => Promise<any>;
	onClose: () => void;
}

const ContactSalesModal: React.FC<IContactSalesModalProps> = ({
	addAlert,
	onClose
}) => (
	<Modal>
		<Modal.Header
			onClose={onClose}
			title={Liferay.Language.get('contact-sales')}
		/>

		<Modal.Body>
			<HubspotForm
				css=''
				cssClass='hs-form-container'
				cssRequired=''
				formId={FORM_ID}
				onFormSubmitted={() => {
					addAlert({
						alertType: alertTypes.SUCCESS,
						message: Liferay.Language.get('success')
					});

					onClose();
				}}
				portalId={PORTAL_ID}
				submitButtonClass='btn btn-block btn-primary'
			/>
		</Modal.Body>
	</Modal>
);

export default connect(
	null,
	{addAlert}
)(ContactSalesModal);
