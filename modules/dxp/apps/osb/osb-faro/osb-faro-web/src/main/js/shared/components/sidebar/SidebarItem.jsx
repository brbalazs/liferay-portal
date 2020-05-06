import Button from '../Button';
import getCN from 'classnames';
import Icon from '../Icon';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import {PropTypes} from 'prop-types';

export default class SidebarItem extends React.Component {
	static defaultProps = {
		label: ''
	};

	static propTypes = {
		active: PropTypes.bool,
		href: PropTypes.string,
		icon: PropTypes.string,
		label: PropTypes.string
	};

	render() {
		const {
			active,
			className,
			href,
			icon,
			label,
			...otherProps
		} = this.props;

		const classes = getCN('sidebar-item-root', className, {
			active
		});

		return (
			<li
				{...omitDefinedProps(otherProps, SidebarItem.propTypes)}
				className={classes}
			>
				<Button className='link' display='link' href={href}>
					<span className='link-content-wrapper'>
						<span className='icon-wrapper'>
							<Icon monospaced={false} symbol={icon} />
						</span>

						<span className='item-label'>{label}</span>
					</span>
				</Button>
			</li>
		);
	}
}
