import React from 'react';
import {formatDateToTimeZone} from 'shared/util/date';
import {PropTypes} from 'prop-types';
import {sub} from 'shared/util/lang';

export default class CreatedByCell extends React.Component {
	static propTypes = {
		data: PropTypes.shape({
			dateModified: PropTypes.number,
			userName: PropTypes.string
		}),
		timeZoneId: PropTypes.string
	};

	render() {
		const {
			className,
			data: {dateModified, userName},
			timeZoneId
		} = this.props;

		return (
			<td className={className}>
				<b>{userName}</b>
				<div className='text-secondary'>
					<em>
						{sub(Liferay.Language.get('last-edited-x'), [
							formatDateToTimeZone(
								dateModified,
								'M/D/YY',
								timeZoneId
							)
						])}
					</em>
				</div>
			</td>
		);
	}
}
