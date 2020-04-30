import ErrorDisplay from 'shared/components/ErrorDisplay';
import ErrorPage from 'shared/pages/ErrorPage';
import LoadingPage from 'shared/pages/Loading';
import NoResultsDisplay from 'shared/components/NoResultsDisplay';
import React from 'react';
import Spinner from 'shared/components/Spinner';
import {compose} from 'redux';
import {get, omit} from 'lodash';

/**
 * HOC for ErrorDisplay.
 * @param {Object} - Options object to pass as props to ErrorDisplay
 * @returns {Function} Returns the ErrorDisplay or Wrapped Component.
 */
export const withError = (options = {}) => Component => ({
	error,
	errorProps = {},
	pageDisplay = true,
	refetch,
	...otherProps
}) => {
	const otherOptions = omit(options, 'page');

	if (error) {
		return get(options, 'page', pageDisplay) ? (
			<ErrorPage {...errorProps} {...otherOptions} />
		) : (
			<ErrorDisplay
				onReload={refetch}
				spacer
				{...errorProps}
				{...otherOptions}
			/>
		);
	}

	return <Component refetch={refetch} {...otherProps} />;
};

/**
 * HOC for NoResultsDisplay.
 * @param {Object} - Options object to pass as props to NoResultsDisplay.
 * @returns {Function} Returns the NoResultsDisplay or WrappedComponent.
 */
export const withEmpty = (options = {}) => Component => ({
	data,
	error,
	loading,
	total,
	...props
}) => {
	if (((data && data.total === 0) || total === 0) && !loading && !error) {
		return <NoResultsDisplay {...options} />;
	}

	return (
		<Component
			data={data}
			error={error}
			loading={loading}
			total={total}
			{...props}
		/>
	);
};

/**
 * HOC for Loading display.
 * @param {Object} - Options object to pass as props to Loading component.
 * @returns {Function} Returns the Loading or WrappedComponent.
 */
export const withLoading = (options = {}) => Component => ({
	alignCenter = false,
	className,
	data,
	inline = false,
	loading,
	page = true,
	...otherProps
}) => {
	if (loading) {
		return get(options, 'page', page) ? (
			<LoadingPage className={className} key='LOADING' />
		) : (
			<Spinner
				alignCenter={get(options, 'alignCenter', alignCenter)}
				className={className}
				inline={get(options, 'inline', inline)}
				key='SPINNER'
				spacer={!get(options, 'inline', inline)}
			/>
		);
	}

	return <Component className={className} data={data} {...otherProps} />;
};

/**
 * HOC for displaying results.
 */
export const SafeResults = compose(
	withLoading(),
	withError()
)(({children, data}) => children(data));

export const WrapSafeResults = compose(
	withLoading(),
	withError()
)(({children}) => children);
