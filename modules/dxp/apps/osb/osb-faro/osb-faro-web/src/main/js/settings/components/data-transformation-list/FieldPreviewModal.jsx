import getCN from 'classnames';
import ListGroup from 'shared/components/list-group';
import Modal from 'shared/components/modal';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import {get, noop} from 'lodash';
import {PropTypes} from 'prop-types';
import {sub} from 'shared/util/lang';

class FieldPreviewModal extends React.Component {
	static defaultProps = {
		onClose: noop
	};

	static propTypes = {
		dataSourceFn: PropTypes.func.isRequired,
		fieldName: PropTypes.string,
		onClose: PropTypes.func,
		sourceName: PropTypes.string
	};

	state = {
		fieldData: []
	};

	componentDidMount() {
		this.getFieldData();
	}

	getFieldData() {
		this.props.dataSourceFn().then(fieldData =>
			this.setState({
				fieldData: get(fieldData, [0, 'values'], [])
			})
		);
	}

	render() {
		const {
			props: {className, fieldName, onClose, sourceName, ...otherProps},
			state: {fieldData}
		} = this;

		return (
			<Modal
				{...omitDefinedProps(otherProps, FieldPreviewModal.propTypes)}
				className={getCN('field-preview-modal-root', className)}
				size='lg'
			>
				<Modal.Header
					onClose={onClose}
					title={sub(Liferay.Language.get('field-preview-x'), [
						sourceName
					])}
				/>

				<ListGroup>
					<ListGroup.Item flex header>
						<ListGroup.ItemField>{fieldName}</ListGroup.ItemField>
					</ListGroup.Item>

					{fieldData.map((item, index) => (
						<ListGroup.Item key={index}>
							<ListGroup.ItemField>{item}</ListGroup.ItemField>
						</ListGroup.Item>
					))}
				</ListGroup>
			</Modal>
		);
	}
}

export default FieldPreviewModal;
