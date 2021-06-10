import Button from 'shared/components/Button';
import Dropdown from 'shared/components/Dropdown';
import Icon from 'shared/components/Icon';
import React from 'react';
import {PropTypes} from 'prop-types';

export default class RowActions extends React.Component {
	static defaultProps = {
		quickActions: []
	};

	static propTypes = {
		actions: PropTypes.array,
		quickActions: PropTypes.array
	};

	render() {
		const {actions, quickActions} = this.props;

		return (
			<>
				{!!quickActions.length && (
					<div className='quick-action-menu'>
						{quickActions.map(({iconSymbol, label, ...props}) => (
							<Button
								alt={label}
								aria-label={label}
								className='component-action quick-action-item'
								data-tooltip
								display='unstyled'
								key={label}
								title={label}
								{...props}
							>
								<Icon symbol={iconSymbol} />
							</Button>
						))}
					</div>
				)}

				{actions && (
					<Dropdown
						align='bottomRight'
						buttonProps={{
							className: 'component-action',
							display: 'unstyled'
						}}
						className='dropdown-action'
						icon='ellipsis-v'
						showCaret={false}
					>
						{actions.map(({label, ...props}) => (
							<Dropdown.Item hideOnClick key={label} {...props}>
								{label}
							</Dropdown.Item>
						))}
					</Dropdown>
				)}
			</>
		);
	}
}
