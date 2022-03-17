import getCN from 'classnames';
import Loading, {ILoadingProps} from 'shared/pages/Loading';
import NoResultsDisplay, {
	INoResultsDisplayProps
} from 'shared/components/NoResultsDisplay';
import React, {createContext, FC, useContext} from 'react';
import {Sizes} from 'shared/util/constants';

export interface IStatesRendererContextProps
	extends React.HTMLAttributes<HTMLElement> {
	empty?: boolean;
	error?: boolean;
	loading?: boolean;
}

interface ISwitcherComponent {
	show?: boolean;
}

interface IEmptyStateProps extends INoResultsDisplayProps, ISwitcherComponent {
	displayCard?: boolean;
}

interface IErrorStateProps
	extends React.HTMLAttributes<HTMLElement>,
		ISwitcherComponent {}

interface ILoadingStateProps extends ILoadingProps, ISwitcherComponent {
	children?: React.ReactElement;
}

interface ISuccessStateProps
	extends React.HTMLAttributes<HTMLElement>,
		ISwitcherComponent {}

const StatesRendererContext = createContext<IStatesRendererContextProps>({
	empty: false,
	error: false,
	loading: false
});

const EmptyState: FC<IEmptyStateProps> = ({
	children,
	description,
	displayCard = false,
	icon,
	show = true,
	title,
	...otherProps
}) => {
	const {empty, error, loading} = useContext(StatesRendererContext);

	return (
		empty &&
		!error &&
		!loading &&
		show &&
		(children || (
			<NoResultsDisplay
				className={getCN({
					'display-card': displayCard
				})}
				description={description}
				icon={{
					border: false,
					size: Sizes.XXXLarge,
					symbol: 'ac-satellite',
					...icon
				}}
				title={title}
				{...otherProps}
			/>
		))
	);
};

const ErrorState: FC<IErrorStateProps> = ({children, show = true}) => {
	const {empty, error, loading} = useContext(StatesRendererContext);

	return !empty && error && !loading && show && <>{children}</>;
};

const LoadingState: FC<ILoadingStateProps> = ({
	children,
	show = true,
	...otherProps
}) => {
	const {empty, error, loading} = useContext(StatesRendererContext);

	return (
		!empty &&
		!error &&
		loading &&
		show &&
		(children || <Loading {...otherProps} />)
	);
};

const StatesRenderer: FC<IStatesRendererContextProps> & {
	Empty?: FC<IEmptyStateProps>;
	Error?: FC<IErrorStateProps>;
	Loading?: FC<ILoadingStateProps>;
	Success?: FC<ISuccessStateProps>;
} = ({children, empty, error, loading}) => (
	<StatesRendererContext.Provider value={{empty, error, loading}}>
		{children}
	</StatesRendererContext.Provider>
);

const SuccessState: FC<ISuccessStateProps> = ({children, show = true}) => {
	const {empty, error, loading} = useContext(StatesRendererContext);

	return !empty && !error && !loading && show && <>{children}</>;
};

StatesRenderer.Empty = EmptyState;
StatesRenderer.Error = ErrorState;
StatesRenderer.Loading = LoadingState;
StatesRenderer.Success = SuccessState;

export default StatesRenderer;
