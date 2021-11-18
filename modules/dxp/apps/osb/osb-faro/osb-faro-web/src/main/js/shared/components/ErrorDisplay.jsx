import Button from 'shared/components/Button';
import getCN from 'classnames';
import NoResultsDisplay from 'shared/components/NoResultsDisplay';
import React from 'react';
import {PropTypes} from 'prop-types';

export default class ErrorDisplay extends React.Component {
	static defaultProps = {
		buttonLabel: Liferay.Language.get('reload'),
		message: Liferay.Language.get('an-unexpected-error-occurred'),
		spacer: false
	};

	static propTypes = {
		buttonLabel: PropTypes.string,
		message: PropTypes.string,
		onReload: PropTypes.func,
		spacer: PropTypes.bool
	};

	render() {
		const {buttonLabel, className, message, onReload, spacer} = this.props;

		return (
			<NoResultsDisplay
				className={getCN(
					'error-display-root',
					'flex-grow-1',
					{'error-spacer': spacer},
					className
				)}
				title={message}
			>
				{onReload && (
					<Button onClick={() => onReload()}>{buttonLabel}</Button>
				)}
			</NoResultsDisplay>
		);
	}
}
