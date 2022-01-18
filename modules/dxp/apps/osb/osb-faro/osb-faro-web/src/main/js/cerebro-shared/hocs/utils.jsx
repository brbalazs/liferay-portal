import NoResultsDisplay, {
	getFormattedTitle
} from 'shared/components/NoResultsDisplay';
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
	emptyMessage,
	entityLabel,
	items,
	noResultsProps,
	noResultsRenderer,
	query,
	total,
	...otherProps
}) => {
	if (items && !items.length && (!!total || !!query) && !noResultsRenderer) {
		if (noResultsProps) {
			return <NoResultsDisplay {...noResultsProps} title={entityLabel} />;
		}

		return (
			<NoResultsDisplay
				{...noResultsProps}
				title={getFormattedTitle(entityLabel)}
			/>
		);
	} else if (items && !items.length && !total) {
		if (noResultsRenderer) {
			const NoResults = noResultsRenderer;

			return <NoResults />;
		}

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

	return (
		<Component
			{...otherProps}
			entityLabel={entityLabel}
			items={items}
			query={query}
			total={total}
		/>
	);
};

export {withEmpty, withError};
