import * as emailValidator from 'isemail';
import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import Input from 'shared/components/Input';
import InputList from 'shared/components/InputList';
import Modal from 'shared/components/modal';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import {noop} from 'lodash';
import {PropTypes} from 'prop-types';

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
		const {emails, inputValue} = this.state;

		if (
			(emails.length && !inputValue) ||
			(inputValue && this.validateEmail(inputValue))
		) {
			this.props.onSubmit(this.state.emails);
		}
	}

	validateEmail(value) {
		return emailValidator.validate(value);
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
							validationFn={this.validateEmail}
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
							(!!inputValue &&
								!emailValidator.validate(inputValue))
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
