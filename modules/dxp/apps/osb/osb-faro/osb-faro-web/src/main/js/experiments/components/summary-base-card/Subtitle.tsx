import React from 'react';

interface SummaryBaseCardSubtitle extends React.HTMLAttributes<HTMLElement> {
	label: string;
}

const SummaryBaseCardSubtitle: React.FC<SummaryBaseCardSubtitle> = ({
	label
}) => <div className='font-size-sm-1x mb-2 text-uppercase'>{label}</div>;

export default SummaryBaseCardSubtitle;
