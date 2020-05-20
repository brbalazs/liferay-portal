import Button from 'shared/components/Button';
import Panel from 'shared/components/Panel';
import React from 'react';

type Action = {
	label: string;
	onClick: () => any;
};

interface IUpgradeConnectionCardProps
	extends React.HTMLAttributes<HTMLElement> {
	actions: Action[];
	content: string;
	title: string;
}

const UpgradeConnectionCard: React.FC<IUpgradeConnectionCardProps> = ({
	actions = [],
	content,
	title
}) => (
	<Panel className='upgrade-connection-card-root mb-4' title={title}>
		{
			<>
				<p>{content}</p>

				{!!actions.length &&
					actions.map(({label, ...props}) => (
						<Button key={label} size='sm' {...props}>
							{label}
						</Button>
					))}
			</>
		}
	</Panel>
);

export default UpgradeConnectionCard;
