import getCN from 'classnames';
import Icon from 'shared/components/Icon';
import React from 'react';
import {Sizes} from 'shared/util/constants';

interface IEmptyStateDashboardProps
	extends React.HTMLAttributes<HTMLDivElement> {
	description?: React.ReactNode | string;
	symbol?: string;
	title: string;
}

const EmptyStateDashboard: React.FC<IEmptyStateDashboardProps> = ({
	children,
	className,
	description,
	symbol,
	title
}) => (
	<div className={getCN(className, 'empty-state-dashboard-root')}>
		{symbol && <Icon size={Sizes.XXXLarge} symbol={symbol} />}

		<span className='title'>{title}</span>

		{description && <span className='secondary-info'>{description}</span>}

		{children}
	</div>
);

export default EmptyStateDashboard;
