import ChannelsMenu from '../channels-menu';
import getCN from 'classnames';
import Icon from '../Icon';
import React from 'react';
import SidebarItem from './SidebarItem';
import UserDropdown from 'shared/components/user-dropdown';
import {ACCOUNTS, Routes, SEGMENTS, toRoute} from 'shared/util/router';
import {DEVELOPER_MODE} from 'shared/util/constants';
import {Link, matchPath} from 'react-router-dom';
import {PropTypes} from 'prop-types';
import {User} from 'shared/util/records';

export default class Sidebar extends React.Component {
	static defaultProps = {
		channels: [],
		collapsed: false,
		currentUser: {}
	};

	static propTypes = {
		activePathname: PropTypes.string,
		channelId: PropTypes.string,
		collapsed: PropTypes.bool,
		currentUser: PropTypes.instanceOf(User).isRequired,
		groupId: PropTypes.string.isRequired,
		onToggle: PropTypes.func
	};

	getSidebarSections() {
		const {channelId, groupId} = this.props;

		return [
			{
				items: [
					{
						icon: 'ac-page',
						label: Liferay.Language.get('sites'),
						route: Routes.SITES,
						url: toRoute(Routes.SITES, {channelId, groupId})
					},
					{
						icon: 'ac-assets',
						label: Liferay.Language.get('assets'),
						route: Routes.ASSETS,
						url: toRoute(Routes.ASSETS, {channelId, groupId})
					}
				],
				label: Liferay.Language.get('touchpoints')
			},
			{
				items: [
					{
						icon: 'ac-segment',
						label: Liferay.Language.get('segments'),
						route: Routes.CONTACTS_LIST_SEGMENT,
						url: toRoute(Routes.CONTACTS_LIST_ENTITY, {
							channelId,
							groupId,
							type: SEGMENTS
						})
					},
					{
						icon: 'ac-account',
						label: Liferay.Language.get('accounts'),
						route: Routes.CONTACTS_LIST_ACCOUNT,
						url: toRoute(Routes.CONTACTS_LIST_ENTITY, {
							channelId,
							groupId,
							type: ACCOUNTS
						})
					},
					{
						icon: 'ac-individual',
						label: Liferay.Language.get('individuals'),
						route: Routes.CONTACTS_INDIVIDUALS,
						url: toRoute(Routes.CONTACTS_INDIVIDUALS, {
							channelId,
							groupId
						})
					}
				],
				label: Liferay.Language.get('people')
			},
			{
				items: [
					{
						icon: 'ac-test',
						label: Liferay.Language.get('tests'),
						route: Routes.TESTS,
						url: toRoute(Routes.TESTS, {channelId, groupId})
					}
				],
				label: Liferay.Language.get('optimize')
			}
		];
	}

	getUserMenuItems() {
		const {currentUser} = this.props;

		return [
			{
				items: [
					{
						externalLink: true,
						label: Liferay.Language.get('sign-out'),
						url: Routes.LOGOUT
					},
					{
						label: Liferay.Language.get('switch-workspaces'),
						url: Routes.BASE
					}
				],
				subheaderLabel: currentUser.emailAddress
			}
		];
	}

	render() {
		const {
			activePathname,
			channelId,
			channels,
			className,
			collapsed,
			currentUser,
			groupId,
			onToggle
		} = this.props;

		const classes = getCN('sidebar-root', {collapsed});

		return (
			<div className={getCN(classes, className)}>
				<div className='sidebar-header'>
					<Link
						className='sidebar-header-logo'
						to={toRoute(Routes.SITES, {channelId, groupId})}
					>
						<Icon
							className='logo'
							monospaced={false}
							size='md'
							symbol='ac-logo'
						/>
					</Link>

					<ChannelsMenu
						channels={channels}
						defaultChannelId={channelId}
						groupId={groupId}
					/>
				</div>

				<div className='sidebar-body'>
					{this.getSidebarSections().map(
						({items, label}, sectionIndex) => (
							<div className='section' key={sectionIndex}>
								<h5 className='section-title'>{label}</h5>

								<ul className='nav-list'>
									{items.map(
										(
											{icon, label, route, url},
											itemIndex
										) => (
											<SidebarItem
												active={
													!!matchPath(
														activePathname,
														{
															path: route
														}
													)
												}
												href={url}
												icon={icon}
												key={itemIndex}
												label={label}
											/>
										)
									)}
								</ul>
							</div>
						)
					)}
				</div>

				<div className='sidebar-footer'>
					<div className='divider' />

					<ul className='nav-list'>
						<UserDropdown
							className='user-dropdown-root'
							containerElement='li'
							menuItems={this.getUserMenuItems()}
							userName={currentUser.name}
						/>

						<SidebarItem
							active={
								!!matchPath(activePathname, {
									path: Routes.SETTINGS
								})
							}
							href={toRoute(Routes.SETTINGS_DATA_SOURCE_LIST, {
								groupId
							})}
							icon='cog'
							label={Liferay.Language.get('settings')}
						/>

						{DEVELOPER_MODE && (
							<SidebarItem
								active={
									!!matchPath(activePathname, {
										path: Routes.UI_KIT
									})
								}
								href={toRoute(Routes.UI_KIT, {
									channelId,
									groupId
								})}
								icon='code'
								label='UI Kit'
							/>
						)}

						<SidebarItem
							icon={collapsed ? 'angle-right' : 'angle-left'}
							onClick={onToggle}
						/>
					</ul>
				</div>
			</div>
		);
	}
}
