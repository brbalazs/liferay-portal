import Icon from 'shared/components/Icon';
import Modal from './modal';
import React from 'react';
import Spinner from './Spinner';
import {PropTypes} from 'prop-types';

class LoadingModal extends React.Component {
	static defaultProps = {
		message: Liferay.Language.get('loading')
	};

	static propTypes = {
		icon: PropTypes.string,
		message: PropTypes.string,
		title: PropTypes.string
	};

	render() {
		const {icon, message, title} = this.props;

		return (
			<Modal
				className={`loading-modal-root${
					this.props.className ? ` ${this.props.className}` : ''
				}`}
				size='sm'
			>
				{title && <h1 className='title'>{title}</h1>}

				<div className='icon-container'>
					{icon ? <Icon size='xl' symbol={icon} /> : <Spinner />}
				</div>

				{message && <p className='message'>{message}</p>}
			</Modal>
		);
	}
}

export default LoadingModal;
