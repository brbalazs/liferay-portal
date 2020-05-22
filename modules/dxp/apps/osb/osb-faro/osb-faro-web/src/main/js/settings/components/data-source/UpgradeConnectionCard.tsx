import Button from 'shared/components/Button';
import Panel from 'shared/components/Panel';
import React from 'react';

type Action = {
	label: string;
	onClick: () => void;
};

interface IUpgradeConnectionCardProps
	extends React.HTMLAttributes<HTMLElement> {
	action: Action;
	content: string;
	title: string;
}

const UpgradeConnectionCard: React.FC<IUpgradeConnectionCardProps> = ({
	action: {label, onClick},
	content,
	title
}) => (
	<Panel className='upgrade-connection-card-root' title={title}>
		<p>{content}</p>

		<Button onClick={onClick} size='sm'>
			{label}
		</Button>
	</Panel>
);

export default UpgradeConnectionCard;
