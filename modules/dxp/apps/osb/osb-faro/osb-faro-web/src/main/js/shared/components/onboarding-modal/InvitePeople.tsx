import * as API from 'shared/api';
import BaseScreen from './BaseScreen';
import Button from 'shared/components/Button';
import Constants from 'shared/util/constants';
import getSVG from 'shared/util/svg';
import Icon from 'shared/components/Icon';
import Input from 'shared/components/Input';
import InputList from 'shared/components/InputList';
import Modal from '../modal';
import React, {useState} from 'react';
import {addAlert} from 'shared/actions/alerts';
import {Alert} from 'shared/types';
import {connect} from 'react-redux';
import {validateEmail} from 'shared/util/email-validators';

const {userRoleNames} = Constants;

const TIMEOUT_INTERVAL = 1500;

interface IInvitePeopleProps {
	addAlert: Alert.AddAlert;
	dxpConnected: boolean;
	groupId: string;
	onClose: () => void;
	onNext: (increment?: number) => void;
}

const InvitePeople: React.FC<IInvitePeopleProps> = ({
	addAlert,
	dxpConnected,
	groupId,
	onClose,
	onNext
}) => {
	const [emails, setEmails] = useState([]);
	const [inputValue, setInputValue] = useState('');
	const [loading, setLoading] = useState(false);
	const [sent, setSent] = useState(false);

	const handleSubmit = () => {
		if (
			(emails.length && !inputValue) ||
			(inputValue && validateEmail(inputValue))
		) {
			setLoading(true);

			API.user
				.inviteMany({
					emailAddresses: emails,
					groupId,
					roleName: userRoleNames.member
				})
				.then(() => {
					setLoading(false);
					setSent(true);

					if (dxpConnected) {
						setTimeout(onNext, TIMEOUT_INTERVAL);
					}
				})
				.catch(() => {
					addAlert({
						alertType: Alert.Types.ERROR,
						message: Liferay.Language.get(
							'there-was-a-problem-sending-your-invites.-please-try-again'
						),
						timeout: false
					});

					setLoading(false);
				});
		}
	};

	const svg = getSVG('ac-invite');

	return (
		<BaseScreen
			className='invite-people'
			onClose={onClose}
			title={Liferay.Language.get('invite-people-to-workspace')}
		>
			<Modal.Body className='d-flex flex-column align-items-center flex-grow-1 justify-content-center'>
				<svg className='ac-invite' viewBox={svg.viewBox}>
					<use
						xlinkHref={`/o/osb-faro-web/dist/sprite.svg#${svg.id}`}
					/>
				</svg>

				{sent ? (
					<div className='success-info d-flex align-items-center'>
						<div>
							<Icon
								className='success-invert'
								symbol='check-circle-full'
							/>
						</div>

						<span className='success-message'>
							{Liferay.Language.get('invites-sent')}
						</span>
					</div>
				) : (
					<div className='add-emails'>
						<div>
							{Liferay.Language.get(
								'enter-the-email-addresses-of-the-people-you-would-like-to-invite'
							)}
						</div>

						<Input.Group>
							<Input.GroupItem>
								<InputList
									errorMessage={Liferay.Language.get(
										'please-enter-a-valid-email-address'
									)}
									inputValue={inputValue}
									items={emails}
									onInputChange={setInputValue}
									onItemsChange={setEmails}
									placeholder={Liferay.Language.get(
										'enter-email-address'
									)}
									validateOnBlur
									validationFn={validateEmail}
								/>
							</Input.GroupItem>
						</Input.Group>

						<div className='secondary-info'>
							{Liferay.Language.get(
								'you-can-set-each-users-role-under-user-management-in-settings'
							)}
						</div>
					</div>
				)}
			</Modal.Body>

			<Modal.Footer className='d-flex justify-content-end'>
				<Button
					disabled={sent}
					onClick={dxpConnected ? () => onNext() : onClose}
				>
					{Liferay.Language.get('skip')}
				</Button>

				<Button
					disabled={
						(!inputValue && !emails.length) ||
						(!!inputValue && !validateEmail(inputValue))
					}
					display='primary'
					loading={loading}
					onClick={
						sent
							? dxpConnected
								? () => onNext()
								: onClose
							: handleSubmit
					}
				>
					{sent
						? dxpConnected
							? Liferay.Language.get('next')
							: Liferay.Language.get('done')
						: Liferay.Language.get('send-invitations')}
				</Button>
			</Modal.Footer>
		</BaseScreen>
	);
};

export default connect(
	null,
	{addAlert}
)(InvitePeople);
