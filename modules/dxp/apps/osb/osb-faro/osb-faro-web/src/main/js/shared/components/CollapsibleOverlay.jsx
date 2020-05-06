import Button from 'shared/components/Button';
import getCN from 'classnames';
import Icon from 'shared/components/Icon';
import React from 'react';
import {PropTypes} from 'prop-types';

export default class CollapsibleOverlay extends React.Component {
	static defaultProps = {
		title: '',
		visible: false
	};

	static propTypes = {
		onClose: PropTypes.func.isRequired,
		title: PropTypes.oneOfType([PropTypes.string, PropTypes.array]),
		visible: PropTypes.bool
	};

	render() {
		const {children, onClose, title, visible} = this.props;

		const classNames = getCN('collapsible-overlay-root', {
			hidden: !visible
		});

		return (
			<div className={classNames} hidden={!visible}>
				<div className='content-wrapper'>
					<div className='header'>
						<h3>{title}</h3>

						<Button
							aria-label={Liferay.Language.get('close')}
							display='unstyled'
							onClick={onClose}
						>
							<Icon symbol='times' />
						</Button>
					</div>

					{children}
				</div>
			</div>
		);
	}
}
