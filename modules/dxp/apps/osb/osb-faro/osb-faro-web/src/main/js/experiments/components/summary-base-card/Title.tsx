import getCN from 'classnames';
import React from 'react';

interface SummaryBaseCardTitleIProps extends React.HTMLAttributes<HTMLElement> {
	label: string;
}

const SummaryBaseCardTitle: React.FC<SummaryBaseCardTitleIProps> = ({
	className,
	label
}) => {
	const classes = getCN('font-weight-bold', className);

	return <h3 className={classes}>{label}</h3>;
};

export default SummaryBaseCardTitle;
