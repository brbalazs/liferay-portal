import * as API from 'shared/api';
import autobind from 'autobind-decorator';
import Button from './Button';
import FaroConstants from 'shared/util/constants';
import Form, {toPromise, validatePattern, validateRequired} from './form';
import Modal from './modal';
import React from 'react';
import {noop, trim} from 'lodash';
import {PropTypes} from 'prop-types';
import {sequence} from 'shared/util/promise';

const {fieldContexts} = FaroConstants;

const TYPES = [
	{
		name: Liferay.Language.get('text'),
		value: 'Text'
	},
	{
		name: Liferay.Language.get('number'),
		value: 'Number'
	},
	{
		name: Liferay.Language.get('boolean'),
		value: 'Boolean'
	},
	{
		name: Liferay.Language.get('date'),
		value: 'Date'
	}
];

class CreateMappingModal extends React.Component {
	static defaultProps = {
		onClose: noop,
		onSubmit: noop
	};

	static propTypes = {
		groupId: PropTypes.string.isRequired,
		onClose: PropTypes.func,
		onSubmit: PropTypes.func
	};

	constructor(props) {
		super(props);

		this._cachedNameValues = new Map();
	}

	@autobind
	handleSubmit(formValues) {
		const {groupId, onSubmit} = this.props;

		API.fieldMappings
			.create({
				groupId,
				name: trim(formValues.name),
				type: formValues.type.value
			})
			.then(onSubmit)
			.catch(noop);
	}

	@autobind
	handleValidate(value) {
		const {groupId} = this.props;

		let error = '';

		if (this._cachedNameValues.has(value)) {
			error = this._cachedNameValues.get(value);
		} else {
			error = API.fieldMappings
				.search({
					context: fieldContexts.demographics,
					cur: 1,
					delta: 1,
					fieldName: value,
					groupId,
					orderByType: ''
				})
				.then(result => {
					if (result.total) {
						return Liferay.Language.get(
							'a-field-already-exists-with-that-name.-please-enter-a-different-name'
						);
					}

					return '';
				})
				.catch(() => Liferay.Language.get('could-not-validate'));

			return toPromise(error);
		}
	}

	render() {
		const {
			props: {onClose}
		} = this;

		return (
			<Modal
				className={`create-mapping-modal-root${
					this.props.className ? ` ${this.props.className}` : ''
				}`}
				size='lg'
			>
				<Modal.Header
					onClose={onClose}
					title={Liferay.Language.get('create-new-csv-field')}
				/>

				<Form
					initialValues={{name: '', type: {name: '', value: null}}}
					onSubmit={this.handleSubmit}
				>
					{({handleSubmit, isValid}) => (
						<Form.Form
							onChange={this.handleFormChange}
							onSubmit={handleSubmit}
						>
							<Form.Group autoFit>
								<Form.Input
									label={Liferay.Language.get(
										'new-field-name'
									)}
									name='name'
									placeholder={Liferay.Language.get(
										'enter-new-field-name'
									)}
									validate={sequence([
										validateRequired,
										validatePattern(
											/^[A-Za-z_][\w]{0,126}[A-Za-z0-9]$/,
											Liferay.Language.get(
												'field-name-must-start-with-a-letter-or-underscore-followed-by-at-most-127-letters-numbers-or-underscores'
											)
										),
										this.handleValidate
									])}
								/>

								<div>
									<label>
										{Liferay.Language.get('field-type')}
									</label>

									<Form.SearchableSelect
										buttonPlaceholder={Liferay.Language.get(
											'select'
										)}
										caretDouble
										items={TYPES}
										name='type'
										showSearch={false}
										validate={validateRequired}
									/>
								</div>
							</Form.Group>

							<Modal.Footer>
								<Button onClick={onClose}>
									{Liferay.Language.get('cancel')}
								</Button>

								<Button
									disabled={!isValid}
									display='primary'
									type='submit'
								>
									{Liferay.Language.get('create')}
								</Button>
							</Modal.Footer>
						</Form.Form>
					)}
				</Form>
			</Modal>
		);
	}
}

export default CreateMappingModal;
