import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import getCN from 'classnames';
import React from 'react';
import {PropTypes} from 'prop-types';

export default class CardTabs extends React.Component {
	static defaultProps = {
		tabs: []
	};

	static propTypes = {
		activeTabId: PropTypes.oneOfType([PropTypes.number, PropTypes.string])
			.isRequired,
		onChange: PropTypes.func,
		tabs: PropTypes.array
	};

	@autobind
	handleEmitOnChange({onClick, tabId}) {
		const {onChange} = this.props;

		return () => {
			onClick && onClick();

			onChange && onChange(tabId);
		};
	}

	render() {
		const {activeTabId, className, tabs} = this.props;

		return (
			<ul className={getCN('card-tabs-root', className)}>
				{tabs.map(({onClick, secondaryInfo, tabId, tabUrl, title}) => (
					<li
						className={getCN('card-tab', {
							active: activeTabId === tabId
						})}
						key={tabId}
					>
						<Button
							display='unstyled'
							href={tabUrl}
							onClick={this.handleEmitOnChange({
								onClick,
								tabId
							})}
						>
							<span className='title'>{title}</span>

							<div>{secondaryInfo}</div>
						</Button>
					</li>
				))}
			</ul>
		);
	}
}
