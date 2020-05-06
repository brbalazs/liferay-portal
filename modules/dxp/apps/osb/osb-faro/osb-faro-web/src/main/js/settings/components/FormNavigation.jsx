import Button from 'shared/components/Button';
import React from 'react';
import {PropTypes} from 'prop-types';

class FormNavigation extends React.Component {
	static defaultProps = {
		enableNext: false,
		submitMessage: Liferay.Language.get('next-step')
	};

	static propTypes = {
		cancelHref: PropTypes.string,
		enableNext: PropTypes.bool,
		onNextStep: PropTypes.func,
		onPreviousStep: PropTypes.func,
		submitMessage: PropTypes.string,
		submitting: PropTypes.bool
	};

	render() {
		const {
			cancelHref,
			enableNext,
			onNextStep,
			onPreviousStep,
			submitMessage,
			submitting
		} = this.props;

		return (
			<div
				className={`form-navigation-root${
					this.props.className ? ` ${this.props.className}` : ''
				}`}
			>
				{onPreviousStep && (
					<Button
						display='secondary'
						key='previousStep'
						onClick={onPreviousStep}
					>
						{Liferay.Language.get('previous-step')}
					</Button>
				)}

				<Button className='cancel' href={cancelHref}>
					{Liferay.Language.get('cancel')}
				</Button>

				<Button
					disabled={!enableNext}
					display='primary'
					loading={submitting}
					onClick={onNextStep}
					type={onNextStep ? 'button' : 'submit'}
				>
					{submitMessage}
				</Button>
			</div>
		);
	}
}

export default FormNavigation;
