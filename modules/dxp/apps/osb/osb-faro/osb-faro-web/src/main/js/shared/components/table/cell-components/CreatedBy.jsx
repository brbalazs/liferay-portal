import React from 'react';
import {formatUTCDateFromUnix} from 'shared/util/date';
import {PropTypes} from 'prop-types';
import {sub} from 'shared/util/lang';

export default class CreatedByCell extends React.Component {
	static propTypes = {
		data: PropTypes.shape({
			dateModified: PropTypes.number,
			userName: PropTypes.string
		})
	};

	render() {
		const {
			data: {dateModified, userName}
		} = this.props;

		return (
			<td
				className={
					this.props.className ? ` ${this.props.className}` : ''
				}
			>
				<b>{userName}</b>
				<div className='text-secondary'>
					<em>
						{sub(Liferay.Language.get('last-edited-x'), [
							formatUTCDateFromUnix(dateModified, 'M/D/YY')
						])}
					</em>
				</div>
			</td>
		);
	}
}
