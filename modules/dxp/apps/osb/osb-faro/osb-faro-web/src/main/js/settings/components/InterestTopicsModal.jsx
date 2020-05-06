import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import getCN from 'classnames';
import Input from 'shared/components/Input';
import InputList from 'shared/components/InputList';
import Modal from 'shared/components/modal';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import {COMMA, ENTER} from 'shared/util/key-constants';
import {noop} from 'lodash';
import {PropTypes} from 'prop-types';

//	regex to validate words with accents and avoid special characters
const KEYWORD_VALIDATOR = /[\w\u00C0-\u00ff]+/;

export default class InterestTopicsModal extends React.Component {
	static defaultProps = {
		onClose: noop,
		onSubmit: noop
	};

	static propTypes = {
		onClose: PropTypes.func,
		onSubmit: PropTypes.func
	};

	state = {
		inputValue: '',
		keywords: []
	};

	@autobind
	handleKeywordsChange(keywords) {
		this.setState({
			keywords
		});
	}

	@autobind
	handleInputChange(value) {
		this.setState({
			inputValue: value.toLowerCase()
		});
	}

	@autobind
	handleSubmit() {
		const {
			props: {onSubmit},
			state: {inputValue, keywords}
		} = this;

		if (
			(keywords.length && !inputValue) ||
			(inputValue && this.validateKeyword(inputValue))
		) {
			onSubmit(this.state.keywords);
		}
	}

	validateKeyword(value) {
		return KEYWORD_VALIDATOR.test(value);
	}

	render() {
		const {
			props: {className, onClose, ...otherProps},
			state: {inputValue, keywords}
		} = this;

		const classes = getCN('invite-users-modal-root', className);

		return (
			<Modal
				{...omitDefinedProps(otherProps, InterestTopicsModal.propTypes)}
				className={classes}
				size='lg'
			>
				<Modal.Header
					onClose={onClose}
					title={Liferay.Language.get('insert-keywords')}
				/>

				<Modal.Body>
					<Input.Group>
						<InputList
							autoFocus
							errorMessage={Liferay.Language.get(
								'please-enter-a-valid-keyword'
							)}
							inputValue={inputValue}
							items={keywords}
							keyCodesToSplit={[COMMA, ENTER]}
							onInputChange={this.handleInputChange}
							onItemsChange={this.handleKeywordsChange}
							placeholder={Liferay.Language.get('enter-keyword')}
							validateOnBlur
							validationFn={this.validateKeyword}
						/>
					</Input.Group>
					<div className='description form-text'>
						{Liferay.Language.get(
							'use-comma-or-enter-to-add-several-keywords'
						)}
					</div>
				</Modal.Body>

				<Modal.Footer>
					<Button onClick={onClose}>
						{Liferay.Language.get('cancel')}
					</Button>

					<Button
						disabled={!inputValue && !keywords.length}
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
