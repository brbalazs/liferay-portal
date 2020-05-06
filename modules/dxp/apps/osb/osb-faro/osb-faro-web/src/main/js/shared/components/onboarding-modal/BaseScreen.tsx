import getCN from 'classnames';
import React from 'react';
import {ClayButtonWithIcon} from '@clayui/button';

interface IBaseScreenProps extends React.HTMLAttributes<HTMLElement> {
	onClose: () => void;
	title?: string;
}

const BaseScreen: React.FC<IBaseScreenProps> = ({
	children,
	className,
	onClose,
	title
}) => (
	<div
		className={getCN(
			'onboarding-base-screen-root d-flex flex-column flex-grow-1 justify-content-between',
			className
		)}
	>
		<div className='header'>
			<div className='d-flex justify-content-end'>
				<ClayButtonWithIcon
					borderless
					className='close-button'
					displayType='secondary'
					onClick={onClose}
					small
					symbol='times'
				/>
			</div>

			{title && (
				<span className='title d-flex justify-content-center'>
					{title}
				</span>
			)}
		</div>

		{children}
	</div>
);

export default BaseScreen;
