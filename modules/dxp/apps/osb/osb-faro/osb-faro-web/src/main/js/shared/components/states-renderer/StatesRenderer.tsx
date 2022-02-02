import Loading, {ILoadingProps} from 'shared/pages/Loading';
import NoResultsDisplay, {
	IconProps,
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

interface IStatesRendererChildren {
	Empty?: FC<INoResultsDisplayProps>;
	Error?: FC<React.HTMLAttributes<HTMLElement>>;
	Loading?: FC<ILoadingProps & {children?: React.ReactElement}>;
	Success?: FC<React.HTMLAttributes<HTMLElement>>;
}

interface ISwitcherComponent {
	show?: boolean;
}

const ICON_PROPS: IconProps = {
	border: false,
	size: Sizes.XXXLarge,
	symbol: 'ac-satellite'
};

const StatesRendererContext = createContext<IStatesRendererContextProps>({
	empty: false,
	error: false,
	loading: false
});

const EmptyState: FC<INoResultsDisplayProps & ISwitcherComponent> = ({
	children,
	description,
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
				description={description}
				icon={ICON_PROPS}
				title={title}
				{...otherProps}
			/>
		))
	);
};

const ErrorState: FC<
	React.HTMLAttributes<HTMLElement> & ISwitcherComponent
> = ({children, show = true}) => {
	const {empty, error, loading} = useContext(StatesRendererContext);

	return !empty && error && !loading && show && <>{children}</>;
};

const LoadingState: FC<
	ILoadingProps & {children?: React.ReactElement} & ISwitcherComponent
> = ({children, show = true, ...otherProps}) => {
	const {empty, error, loading} = useContext(StatesRendererContext);

	return (
		!empty &&
		!error &&
		loading &&
		show &&
		(children || <Loading {...otherProps} />)
	);
};

const StatesRenderer: FC<IStatesRendererContextProps> &
	IStatesRendererChildren = ({children, empty, error, loading}) => (
	<StatesRendererContext.Provider value={{empty, error, loading}}>
		{children}
	</StatesRendererContext.Provider>
);

const SuccessState: FC<
	React.HTMLAttributes<HTMLElement> & ISwitcherComponent
> = ({children, show = true}) => {
	const {empty, error, loading} = useContext(StatesRendererContext);

	return !empty && !error && !loading && show && <>{children}</>;
};

StatesRenderer.Empty = EmptyState;
StatesRenderer.Error = ErrorState;
StatesRenderer.Loading = LoadingState;
StatesRenderer.Success = SuccessState;

export default StatesRenderer;
