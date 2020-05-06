import autobind from 'autobind-decorator';
import Button from './Button';
import Modal from './modal';
import React from 'react';
import {noop} from 'lodash';
import {PropTypes} from 'prop-types';

class TestModal extends React.Component {
	static defaultProps = {
		onClose: noop,
		title: 'Modal'
	};

	static propTypes = {
		onClose: PropTypes.func,
		title: PropTypes.string
	};

	@autobind
	handleClose() {
		this.props.onClose();
	}

	render() {
		return (
			<Modal
				{...this.props}
				className={
					this.props.className ? ` ${this.props.className}` : ''
				}
			>
				<Modal.Header
					onClose={this.handleClose}
					title={this.props.title}
				/>

				<Modal.Body inlineScroller>
					<h4>{'Modal Body'}</h4>
				</Modal.Body>

				<Modal.Footer>
					<Button onClick={this.handleClose}>{'Cancel'}</Button>
					<Button>{'Submit'}</Button>
				</Modal.Footer>
			</Modal>
		);
	}
}

export default TestModal;
