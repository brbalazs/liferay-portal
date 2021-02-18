import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import debounce from 'shared/util/debounce-decorator';
import EntityList from 'shared/components/EntityList';
import ErrorDisplay from 'shared/components/ErrorDisplay';
import getCN from 'classnames';
import NoResultsDisplay, {
	getFormattedTitle
} from 'shared/components/NoResultsDisplay';
import React from 'react';
import SearchInput from 'shared/components/SearchInput';
import Spinner from 'shared/components/Spinner';
import {autoCancel, hasRequest} from 'shared/util/request-decorator';
import {hasChanges} from 'shared/util/react';
import {PropTypes} from 'prop-types';

@hasRequest
export default class AssociatedSegmentsCard extends React.Component {
	static propTypes = {
		channelId: PropTypes.string,
		dataSourceFn: PropTypes.func.isRequired,
		groupId: PropTypes.string.isRequired,
		id: PropTypes.string.isRequired,
		pageUrl: PropTypes.string.isRequired
	};

	state = {
		error: false,
		items: [],
		loading: true,
		searchValue: ''
	};

	componentDidMount() {
		this.handleFetchSegments();
	}

	componentDidUpdate(prevProps, prevState) {
		if (hasChanges(prevState, this.state, 'searchValue')) {
			this.handleFetchSegments();
		}
	}

	componentWillUnmount() {
		this.handleFetchSegments.cancel();
	}

	@debounce(250)
	@autoCancel
	handleFetchSegments() {
		const {
			props: {channelId, dataSourceFn, groupId, id},
			state: {searchValue}
		} = this;

		this.setState({
			loading: true
		});

		return dataSourceFn({channelId, groupId, id, searchValue})
			.then(({items}) => {
				this.setState({
					error: false,
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

	@autobind
	handleSearch(value) {
		this.setState({
			searchValue: value
		});
	}

	renderList() {
		const {
			props: {channelId, groupId},
			state: {error, items, loading}
		} = this;

		if (error) {
			return (
				<ErrorDisplay
					key='ERROR_DISPLAY'
					onReload={this.handleFetchSegments}
					spacer
				/>
			);
		} else if (items.length) {
			return (
				<EntityList
					channelId={channelId}
					groupId={groupId}
					items={items}
				/>
			);
		} else if (!loading) {
			return (
				<NoResultsDisplay
					spacer
					title={getFormattedTitle(Liferay.Language.get('segments'))}
				/>
			);
		}
	}

	render() {
		const {
			props: {className, pageUrl},
			state: {loading, searchValue}
		} = this;

		return (
			<Card className={getCN('associated-segments-card-root', className)}>
				<Card.Header>
					<Card.Title>
						{Liferay.Language.get('associated-segments')}
					</Card.Title>
				</Card.Header>

				<Card.Body>
					<SearchInput
						onChange={this.handleSearch}
						value={searchValue}
					/>
				</Card.Body>

				<div className='content'>
					{loading && <Spinner overlay />}

					{this.renderList()}
				</div>

				<Card.Footer>
					<Button
						display='link'
						href={pageUrl}
						icon='angle-right'
						iconAlignment='right'
						size='sm'
					>
						{Liferay.Language.get('view-all-segments')}
					</Button>
				</Card.Footer>
			</Card>
		);
	}
}
