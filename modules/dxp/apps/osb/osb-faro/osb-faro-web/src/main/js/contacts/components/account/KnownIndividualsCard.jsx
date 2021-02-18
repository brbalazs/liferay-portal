import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import ErrorDisplay from 'shared/components/ErrorDisplay';
import getCN from 'classnames';
import NoResultsDisplay, {
	getFormattedTitle
} from 'shared/components/NoResultsDisplay';
import React from 'react';
import Table from 'shared/components/table';
import {autoCancel, hasRequest} from 'shared/util/request-decorator';
import {individualsListColumns} from 'shared/util/table-columns';
import {PropTypes} from 'prop-types';
import {Routes, toRoute} from 'shared/util/router';

@hasRequest
export default class KnownIndividualsCard extends React.Component {
	static propTypes = {
		channelId: PropTypes.string,
		dataSourceFn: PropTypes.func.isRequired,
		groupId: PropTypes.string.isRequired,
		id: PropTypes.string.isRequired
	};

	state = {
		error: false,
		items: [],
		loading: true
	};

	componentDidMount() {
		this.handleFetchItems();
	}

	@autoCancel
	@autobind
	handleFetchItems() {
		const {channelId, dataSourceFn, groupId, id} = this.props;

		this.setState({
			error: false,
			loading: true
		});

		return dataSourceFn({channelId, groupId, id})
			.then(({items}) => {
				this.setState({
					items,
					loading: false
				});
			})
			.catch(err => {
				if (!err.IS_CANCELLATION_ERROR) {
					this.setState({
						error: true,
						loading: false
					});
				}
			});
	}

	renderTable() {
		const {
			props: {channelId, groupId},
			state: {error, items, loading}
		} = this;

		if (error) {
			return (
				<ErrorDisplay
					key='ERROR_DISPLAY'
					onReload={this.handleFetchItems}
					spacer
				/>
			);
		} else if (!loading && !items.length) {
			return (
				<NoResultsDisplay
					key='NO_RESULTS_DISPLAY'
					spacer
					title={getFormattedTitle(
						Liferay.Language.get('individuals')
					)}
				/>
			);
		} else {
			return (
				<Table
					columns={[
						individualsListColumns.getNameJobTitle({
							channelId,
							groupId
						})
					]}
					items={items}
					loading={loading}
					rowIdentifier='id'
				/>
			);
		}
	}

	render() {
		const {channelId, className, groupId, id} = this.props;

		return (
			<Card className={getCN('known-individuals-card-root', className)}>
				<Card.Header>
					<Card.Title>
						{Liferay.Language.get('known-individuals')}
					</Card.Title>
				</Card.Header>

				{this.renderTable()}

				<Card.Footer>
					<Button
						display='link'
						href={toRoute(Routes.CONTACTS_ACCOUNT_INDIVIDUALS, {
							channelId,
							groupId,
							id
						})}
						icon='angle-right'
						iconAlignment='right'
						size='sm'
					>
						{Liferay.Language.get('view-all-individuals')}
					</Button>
				</Card.Footer>
			</Card>
		);
	}
}
