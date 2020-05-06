import NoResultsDisplay from 'shared/components/NoResultsDisplay';
import React from 'react';

/**
 * HOC
 * @description Error
 */
const withError = () => Component => ({error, errorMessage, ...props}) => {
	if (error) {
		return (
			<NoResultsDisplay
				title={
					errorMessage ||
					Liferay.Language.get('sorry-an-error-occurred')
				}
			/>
		);
	}

	return <Component {...props} />;
};

/**
 * HOC
 * @description Empty
 * @param {object} options
 * @property {[string]} emptyDescription
 * @property {[string]} emptyTitle
 * @property {[boolean]} primary
 */

const withEmpty = ({
	emptyDescription,
	emptyTitle,
	primary
} = {}) => Component => ({
	empty,
	emptyMessage,
	noResultsProps,
	...otherProps
}) => {
	if (empty) {
		return (
			<NoResultsDisplay
				description={emptyDescription}
				primary={primary}
				title={
					emptyTitle ||
					emptyMessage ||
					Liferay.Language.get('empty-message')
				}
				{...noResultsProps}
			/>
		);
	}

	return <Component {...otherProps} />;
};

export {withEmpty, withError};
