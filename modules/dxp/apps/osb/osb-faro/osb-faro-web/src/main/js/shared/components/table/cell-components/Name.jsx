import getCN from 'classnames';
import React from 'react';
import TextTruncate from 'shared/components/TextTruncate';
import {Link} from 'react-router-dom';
import {noop} from 'lodash';
import {PropTypes} from 'prop-types';

export default class Name extends React.Component {
	static defaultProps = {
		nameKey: 'name',
		onClick: noop,
		routeFn: noop
	};

	static propTypes = {
		data: PropTypes.shape({
			name: PropTypes.string
		}).isRequired,
		disabled: PropTypes.bool,
		maxWidth: PropTypes.number,
		nameKey: PropTypes.string,
		onClick: PropTypes.func,
		renderIcon: PropTypes.func,
		renderSecondaryInfo: PropTypes.func,
		routeFn: PropTypes.func,
		tooltip: PropTypes.bool
	};

	renderSecondaryInfo() {
		const {data, renderSecondaryInfo} = this.props;

		return (
			!!renderSecondaryInfo && (
				<div className='secondary-info text-truncate'>
					{renderSecondaryInfo(data) || '-'}
				</div>
			)
		);
	}

	render() {
		const {
			className,
			data,
			disabled,
			maxWidth,
			nameKey,
			onClick,
			renderIcon,
			routeFn,
			tooltip
		} = this.props;

		const displayName = data[nameKey] || '-';

		const url = routeFn(this.props);

		const titleContent = tooltip ? (
			<TextTruncate title={displayName} />
		) : (
			displayName
		);

		return (
			<td className={getCN('name-cell-root', className)}>
				<div
					className='content-container'
					style={maxWidth && {maxWidth: `${maxWidth}px`}}
				>
					{!!renderIcon && (
						<div className='icon-container'>{renderIcon(data)}</div>
					)}

					<div className='text-truncate'>
						<div className='table-title text-truncate'>
							{disabled || !url ? (
								titleContent
							) : (
								<Link
									className='text-truncate'
									onClick={onClick}
									to={url}
								>
									{titleContent}
								</Link>
							)}
						</div>

						{this.renderSecondaryInfo()}
					</div>
				</div>
			</td>
		);
	}
}
