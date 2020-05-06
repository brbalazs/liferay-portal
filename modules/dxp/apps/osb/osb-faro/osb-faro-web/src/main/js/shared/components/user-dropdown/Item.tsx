import ClayDropDown from '@clayui/drop-down';
import React from 'react';
import {Link} from 'react-router-dom';
import {toRoute} from 'shared/util/router';

interface IUserMenuDropdownItem
	extends React.ComponentProps<typeof ClayDropDown.Item> {
	externalLink: boolean;
	label: string;
	url: string;
}

const renderLinkElement = ({externalLink, label, url}) => {
	if (externalLink) {
		return (
			<a className='dropdown-item' href={url}>
				{label}
			</a>
		);
	} else {
		return (
			<Link className='dropdown-item' to={toRoute(url)}>
				{label}
			</Link>
		);
	}
};

const UserMenuDropdownItem: React.FC<IUserMenuDropdownItem> = ({
	className,
	externalLink,
	label,
	url
}) => (
	<ClayDropDown.Item className={className}>
		{renderLinkElement({externalLink, label, url})}
	</ClayDropDown.Item>
);
export default UserMenuDropdownItem;
