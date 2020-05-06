import autobind from 'autobind-decorator';
import Button from './Button';
import getCN from 'classnames';
import Modal from './modal';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import Promise from 'metal-promise';
import React from 'react';
import {noop} from 'lodash';
import {PropTypes} from 'prop-types';

class ConfirmationModal extends React.Component {
	static defaultProps = {
		cancelMessage: Liferay.Language.get('cancel'),
		closeAfterSubmit: true,
		modalVariant: '',
		onClose: noop,
		onSubmit: noop,
		submitButtonDisplay: 'primary',
		submitMessage: Liferay.Language.get('continue'),
		title: Liferay.Language.get('confirm')
	};

	static propTypes = {
		cancelMessage: PropTypes.string,
		closeAfterSubmit: PropTypes.bool,
		message: PropTypes.any,
		modalVariant: PropTypes.string,
		onClose: PropTypes.func,
		onSubmit: PropTypes.func,
		submitButtonDisplay: PropTypes.string,
		submitMessage: PropTypes.string,
		title: PropTypes.string,
		titleIcon: PropTypes.string
	};

	state = {
		submitting: false
	};

	@autobind
	handleSubmit() {
		const {closeAfterSubmit, onClose, onSubmit} = this.props;

		this.setState({
			submitting: true
		});

		const submitVal = onSubmit();

		if (submitVal instanceof Promise) {
			submitVal
				.then(() => {
					this.setState({
						submitting: false
					});

					closeAfterSubmit && onClose();
				})
				.catch(() => {
					this.setState({
						submitting: false
					});
				});
		} else {
			this.setState({
				submitting: false
			});

			closeAfterSubmit && onClose();
		}
	}

	render() {
		const {
			cancelMessage,
			className,
			message,
			modalVariant,
			onClose,
			submitButtonDisplay,
			submitMessage,
			title,
			titleIcon,
			...otherProps
		} = this.props;

		return (
			<Modal
				{...omitDefinedProps(otherProps, ConfirmationModal.propTypes)}
				className={getCN('confirmation-modal-root', className, {
					[modalVariant]: modalVariant
				})}
			>
				<Modal.Header
					iconSymbol={titleIcon}
					onClose={onClose}
					title={title}
				/>

				<Modal.Body>{message}</Modal.Body>

				<Modal.Footer>
					<Button onClick={onClose}>{cancelMessage}</Button>

					<Button
						display={submitButtonDisplay}
						loading={this.state.submitting}
						onClick={this.handleSubmit}
					>
						{submitMessage}
					</Button>
				</Modal.Footer>
			</Modal>
		);
	}
}

export default ConfirmationModal;
