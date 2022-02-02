import getCN from 'classnames';
import React, {FC} from 'react';
import Spinner from '../components/Spinner';

export interface ILoadingProps extends React.HTMLAttributes<HTMLDivElement> {
	fadeIn?: boolean;
}

const Loading: FC<ILoadingProps> = ({className, fadeIn = true}) => (
	<div className={getCN('loading-root', className)}>
		<Spinner fadeIn={fadeIn} />
	</div>
);

export default Loading;
