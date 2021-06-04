import Card from 'shared/components/Card';
import React from 'react';

interface BaseCardHeaderIProps extends React.HTMLAttributes<HTMLElement> {
	label: string;
}

const BaseCardHeader: React.FC<BaseCardHeaderIProps> = ({label}) => (
	<Card.Header className=''>
		<Card.Title>{label}</Card.Title>
	</Card.Header>
);

export default BaseCardHeader;
