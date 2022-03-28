import Icon from 'shared/components/Icon';
import React from 'react';

interface ITrendProps extends React.HTMLAttributes<HTMLDivElement> {
	color: string;
	icon?: string;
	label: string;
}

const CLASSNAME = 'analytics-trend';

const Trend: React.FC<ITrendProps> = ({color, icon, label}) => (
	<div className={CLASSNAME} style={{color}}>
		{icon && <Icon symbol={icon} />}
		<span className={`${CLASSNAME}-percent mb-0`}>{label}</span>
	</div>
);

export default Trend;
