import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import Input from 'shared/components/Input';
import InputList from 'shared/components/InputList';
import Modal from 'shared/components/modal';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import {noop} from 'lodash';
import {PropTypes} from 'prop-types';
import {validateEmail} from 'shared/util/email-validators';

export default class InviteUsersModal extends React.Component {
	static defaultProps = {
		onClose: noop,
		onSubmit: noop
	};

	static propTypes = {
		onClose: PropTypes.func,
		onSubmit: PropTypes.func
	};

	state = {
		emails: [],
		inputValue: ''
	};

	@autobind
	handleEmailsChange(emails) {
		this.setState({
			emails
		});
	}

	@autobind
	handleInputChange(value) {
		this.setState({
			inputValue: value.trim()
		});
	}

	@autobind
	handleSubmit() {
		const {
			props: {onSubmit},
			state: {emails, inputValue}
		} = this;

		if (
			(emails.length && !inputValue) ||
			(inputValue && validateEmail(inputValue))
		) {
			onSubmit(emails).then(users => {
				analytics.track(
					'Invited Users',
					{
						userIds: users.map(({id}) => id)
					},
					{ip: '0'}
				);
			});
		}
	}

	render() {
		const {
			props: {className, onClose, ...otherProps},
			state: {emails, inputValue}
		} = this;

		return (
			<Modal
				{...omitDefinedProps(otherProps, InviteUsersModal.propTypes)}
				className={`invite-users-modal-root${
					className ? ` ${className}` : ''
				}`}
				size='lg'
			>
				<Modal.Header
					onClose={onClose}
					title={Liferay.Language.get('invite-users')}
				/>

				<Modal.Body>
					<div className='description form-text'>
						{Liferay.Language.get(
							'enter-the-email-addresses-of-the-people-you-would-like-to-invite-to-analytics-cloud.-separate-each-address-by-space-or-comma'
						)}
					</div>

					<Input.Group>
						<InputList
							autoFocus
							errorMessage={Liferay.Language.get(
								'please-enter-a-valid-email-address'
							)}
							inputValue={inputValue}
							items={emails}
							onInputChange={this.handleInputChange}
							onItemsChange={this.handleEmailsChange}
							placeholder={Liferay.Language.get(
								'enter-email-address'
							)}
							validateOnBlur
							validationFn={validateEmail}
						/>
					</Input.Group>
				</Modal.Body>

				<Modal.Footer>
					<Button onClick={onClose}>
						{Liferay.Language.get('cancel')}
					</Button>

					<Button
						disabled={
							(!inputValue && !emails.length) ||
							(!!inputValue && !validateEmail(inputValue))
						}
						display='primary'
						onClick={this.handleSubmit}
					>
						{Liferay.Language.get('send')}
					</Button>
				</Modal.Footer>
			</Modal>
		);
	}
}
