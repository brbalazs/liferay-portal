import * as breadcrumbs from 'shared/util/breadcrumbs';
import BasePage from 'shared/components/base-page';
import EventAnalysisEditor from '../components/event-analysis-editor';
import EventAnalysisToolbar from '../components/EventAnalysisToolbar';
import Form from 'shared/components/form';
import NavigationWarning from 'shared/components/NavigationWarning';
import React, {useCallback, useContext, useMemo, useState} from 'react';
import withCurrentUser from 'shared/hoc/WithCurrentUser';
import {addAlert} from 'shared/actions/alerts';
import {Alert, RangeSelectors} from 'shared/types';
import {ApolloError} from 'apollo-client';
import {AttributesContext} from '../components/event-analysis-editor/context/attributes';
import {
	Breakdowns,
	CalculationTypes,
	Event,
	Filters
} from 'event-analysis/utils/types';
import {close, modalTypes, open} from 'shared/actions/modals';
import {compose, withRangeKey} from 'shared/hoc';
import {connect} from 'react-redux';
import {
	CreateEventAnalysisMutation,
	EventAnalysisMutationData,
	EventAnalysisMutationVariables,
	UpdateEventAnalysisMutation
} from 'event-analysis/queries/EventAnalysisQuery';
import {DEVELOPER_MODE} from 'shared/util/constants';
import {getSafeRangeSelectors} from 'shared/util/util';
import {GraphQLError} from 'graphql';
import {hasChanges} from 'shared/util/react';
import {Modal} from 'shared/types';
import {omit} from 'lodash';
import {Routes, toRoute} from 'shared/util/router';
import {useHistory, useParams} from 'react-router-dom';
import {useMutation} from '@apollo/react-hooks';
import {User} from 'shared/util/records';
import {WithRangeKeyProps} from 'shared/hoc/WithRangeKey';

enum MessageKeys {
	NameCannotBeBlank = 'name-cannot-be-blank',
	NameIsAlreadyUsed = 'name-is-already-used'
}

interface Error extends ApolloError {
	graphQLErrors: ReadonlyArray<
		GraphQLError & {
			messageKey: keyof MessageKeys;
		}
	>;
}

const ERRORS = {
	[MessageKeys.NameCannotBeBlank]: Liferay.Language.get(
		'name-cannot-be-blank'
	),
	[MessageKeys.NameIsAlreadyUsed]: Liferay.Language.get(
		'name-is-already-used'
	)
};

function hasChangesFn<T>(prev: T = null, next: T = null, ...keys: string[]) {
	const emptyPrev = !prev || !Object.keys(prev).length;
	const emptyNext = !next || !Object.keys(next).length;

	if (emptyPrev && emptyNext) {
		return false;
	} else if ((emptyPrev && !emptyNext) || (!emptyPrev && emptyNext)) {
		return true;
	}

	return hasChanges<T>(prev, next, ...keys);
}

interface IEventAnalysisProps
	extends WithRangeKeyProps,
		React.HTMLAttributes<HTMLElement> {
	addAlert: Alert.AddAlert;
	breakdowns?: Breakdowns;
	close: Modal.close;
	compareToPrevious?: boolean;
	currentUser: User;
	event?: Event;
	filters?: Filters;
	name?: string;
	open: Modal.open;
}

const EventAnalysis: React.FC<IEventAnalysisProps> = ({
	addAlert,
	breakdowns: initialBreakdowns,
	close,
	compareToPrevious: initialCompareToPrevious = false,
	currentUser,
	event: initialEvent = null,
	filters: initialFilters,
	name: initialName = '',
	open,
	rangeSelectors: initialRangeSelectors
}) => {
	const history = useHistory();
	const {channelId, groupId, id: eventAnalysisId = null} = useParams();

	const [compareToPrevious, setCompareToPrevious] = useState<boolean>(
		initialCompareToPrevious
	);
	const [event, setEvent] = useState<Event>(initialEvent);
	const [rangeSelectors, setRangeSelectors] = useState<RangeSelectors>(
		initialRangeSelectors
	);
	const [submitted, setSubmitted] = useState<boolean>(false);
	const [type, setType] = useState<CalculationTypes>(CalculationTypes.Total);

	const {breakdownOrder, breakdowns, filterOrder, filters} = useContext(
		AttributesContext
	);

	const Mutation = eventAnalysisId
		? UpdateEventAnalysisMutation
		: CreateEventAnalysisMutation;

	const [saveEventAnalysis] = useMutation<
		EventAnalysisMutationData,
		EventAnalysisMutationVariables
	>(Mutation);

	const handleSubmit = ({name}, {setSubmitting}) => {
		open(
			modalTypes.LOADING_MODAL,
			{
				message: Liferay.Language.get('this-will-only-take-a-moment'),
				title: eventAnalysisId
					? Liferay.Language.get('creating')
					: Liferay.Language.get('updating')
			},
			{closeOnBlur: false}
		);

		saveEventAnalysis({
			variables: {
				analysisType: type,
				channelId,
				compareToPrevious,
				eventAnalysisBreakdowns: breakdownOrder.map(breakdownId =>
					omit(breakdowns[breakdownId], 'id')
				),
				eventAnalysisFilters: filterOrder.map(filterId =>
					omit(filters[filterId], 'id')
				),
				eventAnalysisId,
				eventDefinitionId: event.id,
				name,
				userId: currentUser.id,
				userName: currentUser.name,
				...getSafeRangeSelectors(rangeSelectors)
			}
		})
			.then(() => {
				setSubmitting(false);
				setSubmitted(true);

				close();

				history.push(
					toRoute(Routes.EVENT_ANALYSIS, {
						channelId,
						groupId
					})
				);

				addAlert({
					alertType: Alert.Types.Success,
					message: Liferay.Language.get(
						'the-analysis-was-saved-successfully'
					)
				});
			})
			.catch(({graphQLErrors}: Error) => {
				setSubmitting(false);
				setSubmitted(false);

				addAlert({
					alertType: Alert.Types.Error,
					message: ERRORS[graphQLErrors[0].messageKey],
					timeout: false
				});

				close();
			});
	};

	const breakdownsChanged: boolean = useMemo(
		() => hasChangesFn<Breakdowns>(initialBreakdowns, breakdowns, 'id'),
		[initialBreakdowns, breakdowns]
	);

	const compareToPreviousChanged: boolean = useMemo(
		() => initialCompareToPrevious !== compareToPrevious,
		[initialCompareToPrevious, compareToPrevious]
	);

	const eventChanged: boolean = useMemo(
		() => hasChangesFn<Event>(initialEvent, event, 'id'),
		[initialEvent, event]
	);

	const filtersChanged: boolean = useMemo(
		() => hasChangesFn<Filters>(initialFilters, filters, 'id'),
		[initialFilters, filters]
	);

	const nameChanged: (name: string) => boolean = useCallback(
		name => initialName !== name,
		[initialName]
	);

	const rangeSelectorsChanged: boolean = useMemo(
		() =>
			hasChangesFn<RangeSelectors>(
				initialRangeSelectors,
				rangeSelectors,
				'rangeStart',
				'rangeKey',
				'rangeEnd'
			),
		[initialRangeSelectors, rangeSelectors]
	);

	return (
		<BasePage
			className='create-event-analysis-root'
			documentTitle={Liferay.Language.get('events')}
		>
			<BasePage.Header
				breadcrumbs={[
					breadcrumbs.getHome({
						channelId,
						groupId,
						label: Liferay.Language.get('home')
					})
				]}
				groupId={groupId}
			>
				<BasePage.Header.TitleSection
					title={Liferay.Language.get('events')}
				/>
			</BasePage.Header>

			{/* TODO: LRAC-9959 Remove condition after deleting feature flag */}
			{DEVELOPER_MODE && (
				<Form
					initialValues={{
						name: initialName
					}}
					onSubmit={handleSubmit}
				>
					{({handleSubmit, isSubmitting, values: {name}}) => {
						const hasChanges =
							breakdownsChanged ||
							compareToPreviousChanged ||
							eventChanged ||
							filtersChanged ||
							nameChanged(name) ||
							rangeSelectorsChanged;

						return (
							<Form.Form onSubmit={handleSubmit}>
								<NavigationWarning
									when={
										!submitted &&
										hasChanges &&
										!isSubmitting
									}
								/>

								<BasePage.SubHeader>
									<EventAnalysisToolbar
										isValid={
											!!name &&
											!!event?.id &&
											hasChanges &&
											!isSubmitting
										}
									/>
								</BasePage.SubHeader>
							</Form.Form>
						);
					}}
				</Form>
			)}

			<BasePage.Body>
				<EventAnalysisEditor
					channelId={channelId}
					compareToPrevious={compareToPrevious}
					event={event}
					onCompareToPreviousChange={setCompareToPrevious}
					onEventChange={setEvent}
					onRangeSelectorsChange={setRangeSelectors}
					onTypeChange={setType}
					rangeSelectors={rangeSelectors}
					type={type}
				/>
			</BasePage.Body>
		</BasePage>
	);
};

export default compose(
	connect(null, {
		addAlert,
		close,
		open
	}),
	withCurrentUser,
	withRangeKey
)(EventAnalysis);
