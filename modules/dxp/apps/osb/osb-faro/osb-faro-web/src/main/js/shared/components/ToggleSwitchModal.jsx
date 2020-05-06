import autobind from 'autobind-decorator';
import Button from './Button';
import Form from './form';
import getCN from 'classnames';
import Modal from './modal';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import ToggleSwitch from './ToggleSwitch';
import {every, noop} from 'lodash';
import {PropTypes} from 'prop-types';

export default class ToggleSwitchModal extends React.Component {
	static defaultProps = {
		onClose: noop,
		onSubmit: noop
	};

	static propTypes = {
		items: PropTypes.array.isRequired,
		message: PropTypes.string,
		onClose: PropTypes.func,
		onSubmit: PropTypes.func,
		title: PropTypes.string,
		toggleAllMessage: PropTypes.string
	};

	state = {
		checkAll: false
	};

	constructor(props) {
		super(props);

		this._formRef = React.createRef();
	}

	@autobind
	handleFormChange(event) {
		const {checked, name} = event.target;

		const {values} = this._formRef.current.getFormikBag();

		this.setState({checkAll: every({...values, [name]: checked}, Boolean)});
	}

	@autobind
	handleSelectAllChange(event) {
		const {checked} = event.target;

		const {setFieldValue} = this._formRef.current.getFormikActions();
		const {values} = this._formRef.current.getFormikBag();

		Object.keys(values).map(key => setFieldValue(key, checked));

		this.setState({checkAll: checked});
	}

	render() {
		const {
			className,
			items,
			message,
			onClose,
			onSubmit,
			title,
			toggleAllMessage,
			...otherProps
		} = this.props;

		const {checkAll} = this.state;

		return (
			<Modal
				{...omitDefinedProps(otherProps, ToggleSwitchModal.propTypes)}
				className={getCN('toggle-switch-modal-root', className)}
			>
				{title && <Modal.Header onClose={onClose} title={title} />}

				<Modal.Body>{message}</Modal.Body>

				{toggleAllMessage && (
					<div className='toggle-all'>
						<ToggleSwitch
							checked={checkAll}
							label={toggleAllMessage}
							name='toggleAll'
							onChange={this.handleSelectAllChange}
						/>
					</div>
				)}

				<Form
					initialValues={items.reduce((acc, item) => {
						acc[item] = false;

						return acc;
					}, {})}
					onSubmit={onSubmit}
					ref={this._formRef}
				>
					{({handleSubmit}) => (
						<Form.Form
							onChange={this.handleFormChange}
							onSubmit={handleSubmit}
						>
							{items.map(item => (
								<Form.Group key={item}>
									<Form.ToggleSwitch
										label={item}
										name={item}
									/>
								</Form.Group>
							))}

							<Modal.Footer>
								<Button onClick={onClose}>
									{Liferay.Language.get('cancel')}
								</Button>

								<Button display='primary' type='submit'>
									{Liferay.Language.get('done')}
								</Button>
							</Modal.Footer>
						</Form.Form>
					)}
				</Form>
			</Modal>
		);
	}
}
