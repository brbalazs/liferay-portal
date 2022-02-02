import getCN from 'classnames';
import Icon from 'shared/components/Icon';
import React from 'react';
import {Sizes} from 'shared/util/constants';
import {sub} from 'shared/util/lang';

const CLASSNAME = 'no-results';

export interface IconProps {
	border?: boolean;
	size?: Sizes;
	symbol: string;
}
export interface INoResultsDisplayProps
	extends React.HTMLAttributes<HTMLElement> {
	children?: React.ReactElement;
	description?: string | React.ReactNode;
	icon?: IconProps;
	primary?: boolean;
	spacer?: boolean;
	title?: string;
}

const NoResultsDisplay: React.FC<INoResultsDisplayProps> = ({
	children,
	className,
	description,
	icon,
	primary = false,
	spacer = false,
	title = getFormattedTitle(undefined, undefined),
	...otherProps
}) => {
	const classes = getCN(className, `${CLASSNAME}-root flex-grow-1`, {
		'no-results-primary': primary
	});

	return (
		<div {...otherProps} className={classes}>
			<div className={getCN(`${CLASSNAME}-content`, {spacer})}>
				{icon &&
					(() => {
						const {
							border = true,
							size = Sizes.XXLarge,
							symbol
						} = icon;
						const classes = getCN(`${CLASSNAME}-icon`, {
							[`${CLASSNAME}-icon-border`]: border
						});

						return (
							<div className={classes}>
								<Icon size={size} symbol={symbol} />
							</div>
						);
					})()}

				{title && <h4 className={`${CLASSNAME}-title`}>{title}</h4>}

				{description && (
					<p className={`${CLASSNAME}-description`}>{description}</p>
				)}

				{children}
			</div>
		</div>
	);
};

type getFormattedTitleType = (name?: string, title?: string) => string;

export const getFormattedTitle: getFormattedTitleType = (
	name = Liferay.Language.get('items').toLowerCase(),
	title = Liferay.Language.get('there-are-no-x-found')
) => sub(title, [name]) as string;

export default NoResultsDisplay;
