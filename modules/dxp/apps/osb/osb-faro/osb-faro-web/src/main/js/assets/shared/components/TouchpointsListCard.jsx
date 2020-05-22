import BasePage from 'shared/components/base-page';
import React from 'react';
import Table from 'shared/components/table';
import TextTruncate from 'shared/components/TextTruncate';
import {getUrl} from 'shared/util/urls';
import {Link} from 'react-router-dom';
import {PropTypes} from 'prop-types';
import {Routes} from 'shared/util/router';

const CLASSNAME = 'analytics-touchpoints-list';

/**
 * Touchpoint List Card
 * @class
 */
class TouchpointsListCard extends React.Component {
	static contextType = BasePage.Context;

	static defaultProps = {
		items: []
	};

	static propTypes = {
		items: PropTypes.arrayOf(
			PropTypes.shape({
				title: PropTypes.string,
				touchpoint: PropTypes.string
			})
		)
	};

	constructor(props) {
		super(props);

		this._elementRef = React.createRef();
	}

	/**
	 * Get Touchpoint URL
	 * @description Get url to navigate in a dashboard
	 * @param {string} title
	 * @param {string} touchpoint
	 */
	getUrl(title, touchpoint) {
		const {params, query} = this.context.router;

		const router = {
			params: {
				...params,
				title,
				touchpoint: encodeURIComponent(touchpoint)
			},
			query
		};

		return getUrl(Routes.SITES_TOUCHPOINTS_OVERVIEW, router);
	}

	/**
	 * Render Title Column
	 * @param {object} param0
	 */
	renderTitleColumn({title, touchpoint}) {
		const url = this.getUrl(title, touchpoint);

		return (
			<td className='table-cell-expand'>
				<Link
					className='font-weight-semibold text-truncate-inline text-dark'
					to={url}
				>
					<TextTruncate title={title} />
				</Link>
			</td>
		);
	}

	/**
	 * Render Touchpoint Column
	 * @param {object} param0
	 */
	renderTouchpointColumn({title, touchpoint}) {
		const url = this.getUrl(title, touchpoint);

		return (
			<td className='table-cell-expand'>
				<Link className='text-secondary text-truncate-inline' to={url}>
					<TextTruncate title={touchpoint} />
				</Link>
			</td>
		);
	}

	/**
	 * Lifecycle Render - ReactJS
	 */
	render() {
		const {items} = this.props;

		const tableColumns = [
			{
				accessor: 'title',
				cellRenderer: ({data}) => this.renderTitleColumn(data),
				label: Liferay.Language.get('page-name'),
				sortable: false,
				title: true
			},
			{
				accessor: 'url',
				cellRenderer: ({data}) => this.renderTouchpointColumn(data),
				label: Liferay.Language.get('url'),
				sortable: false
			}
		];

		return (
			<div className={CLASSNAME} ref={this._elementRef}>
				<Table
					className='table-hover'
					columns={tableColumns}
					items={items}
					rowIdentifier={['touchpoint', 'title']}
				/>
			</div>
		);
	}
}

export default TouchpointsListCard;
