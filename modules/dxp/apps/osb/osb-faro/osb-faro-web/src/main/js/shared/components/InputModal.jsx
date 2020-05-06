import autobind from 'autobind-decorator';
import Button from './Button';
import Input from './Input';
import Modal from './modal';
import React from 'react';
import {noop} from 'lodash';
import {PropTypes} from 'prop-types';

class InputModal extends React.Component {
	static defaultProps = {
		onSubmit: noop
	};

	static propTypes = {
		onClose: PropTypes.func,
		onSubmit: PropTypes.func,
		placeholder: PropTypes.string,
		title: PropTypes.string
	};

	constructor(props) {
		super(props);

		this._inputRef = React.createRef();
	}

	@autobind
	handleSubmit() {
		this.props.onSubmit(this._inputRef.current._elementRef.current.value);
	}

	render() {
		const {onClose, placeholder, title} = this.props;

		return (
			<Modal
				className={
					this.props.className ? ` ${this.props.className}` : ''
				}
				size='sm'
			>
				<Modal.Header onClose={onClose} title={title} />

				<Modal.Body>
					<Input placeholder={placeholder} ref={this._inputRef} />
				</Modal.Body>

				<Modal.Footer>
					<Button onClick={onClose}>
						{Liferay.Language.get('cancel')}
					</Button>

					<Button display='primary' onClick={this.handleSubmit}>
						{Liferay.Language.get('submit')}
					</Button>
				</Modal.Footer>
			</Modal>
		);
	}
}

export default InputModal;
