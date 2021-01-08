import Breadcrumbs from 'shared/components/Breadcrumbs';
import Button from 'shared/components/Button';
import Dropdown from 'shared/components/Dropdown';
import getCN from 'classnames';
import Nav from 'shared/components/Nav';
import NotificationAlertList from '../NotificationAlertList';
import React from 'react';
import Row from './Row';
import TextTruncate from 'shared/components/TextTruncate';
import useModalNotifications from 'shared/hooks/useModalNotifications';
import {close, open} from 'shared/actions/modals';
import {connect} from 'react-redux';
import {getMatchedRoute, setUriQueryValues, toRoute} from 'shared/util/router';
import {Modal} from 'shared/types';
import {noop, pickBy} from 'lodash';

type NavBarItem = {
	exact: boolean;
	label: string;
	route: string;
};

interface INavBarProps extends React.HTMLAttributes<HTMLDivElement> {
	items: NavBarItem[];
	routeParams?: object;
	routeQueries?: object;
}

const NavBar: React.FC<INavBarProps> = ({
	items,
	onClick = noop,
	routeParams = {},
	routeQueries = {}
}) => {
	const matchedRoute = getMatchedRoute(items);

	return (
		<Nav className='page-subnav' display='underline'>
			{items.map(({label, route}) => (
				<Nav.Item
					active={matchedRoute === route}
					href={setUriQueryValues(
						pickBy(routeQueries),
						toRoute(route, routeParams)
					)}
					key={label}
					onClick={onClick}
				>
					<span className='title'>{label}</span>
				</Nav.Item>
			))}
		</Nav>
	);
};

interface Action extends React.ButtonHTMLAttributes<HTMLButtonElement> {
	label: string;
}

interface IPageActionsProps {
	actions: Action[];
	actionsDisplayLimit: number;
	label?: string;
}

const PageActions: React.FC<IPageActionsProps> = ({
	actions = [],
	actionsDisplayLimit = 1,
	label = ''
}) => {
	const triggerDisplayProps = label.length
		? {
				label
		  }
		: {icon: 'ellipsis-v'};

	return (
		<>
			{actions.length <= actionsDisplayLimit &&
				actions.map(({label, ...props}) => (
					<Button key={label} {...props}>
						{label}
					</Button>
				))}

			{actions.length > actionsDisplayLimit && (
				<Dropdown
					{...triggerDisplayProps}
					align='bottomRight'
					buttonProps={{
						display: label.length ? 'primary' : 'unstyled'
					}}
					showCaret={false}
				>
					{actions.map(({label, ...props}) => (
						<Dropdown.Item key={label} {...props}>
							{label}
						</Dropdown.Item>
					))}
				</Dropdown>
			)}
		</>
	);
};

const Section: React.FC<React.HTMLAttributes<HTMLDivElement>> = ({
	children,
	className
}) => <div className={getCN('header-section', className)}>{children}</div>;

interface ITitleSectionProps extends React.HTMLAttributes<HTMLDivElement> {
	subtitle?: React.ReactNode | string;
	title?: string;
}

const TitleSection: React.FC<ITitleSectionProps> = ({
	children,
	className,
	subtitle,
	title
}) => (
	<Section className={getCN('title-section', className, {subtitle})}>
		<span className='align-items-center d-flex'>
			<h1 className='title text-truncate'>
				<TextTruncate title={title} />
			</h1>

			{children}
		</span>

		{subtitle && <div className='subtitle'>{subtitle}</div>}
	</Section>
);

type Breadcrumb = {
	active?: boolean;
	href?: string;
	label: string;
	id?: string;
};

interface IHeaderProps extends React.HTMLAttributes<HTMLDivElement> {
	breadcrumbs: Breadcrumb[];
	close: Modal.close;
	groupId: string;
	open: Modal.open;
}

const Header: React.FC<IHeaderProps> & {
	NavBar: typeof NavBar;
	PageActions: typeof PageActions;
	Section: typeof Section;
	TitleSection: typeof TitleSection;
} = ({breadcrumbs, children, close, groupId, open}) => {
	useModalNotifications(close, groupId, open);

	return (
		<header className='header-root'>
			<div className='header-container'>
				{breadcrumbs && (
					<Row>
						<Breadcrumbs items={breadcrumbs} />
					</Row>
				)}

				{children}
			</div>

			<NotificationAlertList groupId={groupId} stripe />
		</header>
	);
};

Header.NavBar = NavBar;
Header.PageActions = PageActions;
Header.Section = Section;
Header.TitleSection = TitleSection;

export default connect(
	null,
	{close, open}
)(Header);

export {NavBar, PageActions, Section, TitleSection};
