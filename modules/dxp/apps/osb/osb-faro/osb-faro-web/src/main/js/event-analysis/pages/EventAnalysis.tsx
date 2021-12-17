import * as breadcrumbs from 'shared/util/breadcrumbs';
import BasePage from 'shared/components/base-page';
import EventAnalysisEditor from '../components/event-analysis-editor';
import EventAnalysisToolbar from '../components/EventAnalysisToolbar';
import Form from 'shared/components/form';
import NavigationWarning from 'shared/components/NavigationWarning';
import React, {useContext, useRef, useState} from 'react';
import Spinner from 'shared/components/Spinner';
import useEventAnalysisData from 'event-analysis/hooks/useEventAnalysisData';
import useSaveEventAnalysis from 'event-analysis/hooks/useSaveEventAnalysis';
import withCurrentUser from 'shared/hoc/WithCurrentUser';
import {addAlert} from 'shared/actions/alerts';
import {Alert, RangeSelectors} from 'shared/types';
import {
	AttributesContext,
	AttributesProvider
} from '../components/event-analysis-editor/context/attributes';
import {CalculationTypes, Event} from 'event-analysis/utils/types';
import {close, modalTypes, open} from 'shared/actions/modals';
import {compose, withRangeKey} from 'shared/hoc';
import {connect} from 'react-redux';
import {DEVELOPER_MODE} from 'shared/util/constants';
import {Formik} from 'formik';
import {getSafeRangeSelectors} from 'shared/util/util';
import {Modal} from 'shared/types';
import {omit} from 'lodash';
import {Routes, toRoute} from 'shared/util/router';
import {useHistory, useParams} from 'react-router-dom';
import {User} from 'shared/util/records';
import {WithRangeKeyProps} from 'shared/hoc/WithRangeKey';

interface IEventAnalysisProps
	extends WithRangeKeyProps,
		React.HTMLAttributes<HTMLElement> {
	compareToPrevious?: boolean;
	event?: Event;
	name?: string;
	currentUser: User;
	open: Modal.open;
	close: Modal.close;
	addAlert: Alert.AddAlert;
	eventAnalysisId?: string;
}

export const EventAnalysis: React.FC<IEventAnalysisProps> = ({
	addAlert,
	close,
	compareToPrevious: initialCompareToPrevious = false,
	currentUser,
	event: initialEvent = null,
	eventAnalysisId = null,
	name: initialName = '',
	open,
	rangeSelectors: initialRangeSelectors
}) => {
	const history = useHistory();
	const {channelId, groupId} = useParams();
	const [compareToPrevious, setCompareToPrevious] = useState<boolean>(
		initialCompareToPrevious
	);
	const [event, setEvent] = useState<Event>(initialEvent);
	const [rangeSelectors, setRangeSelectors] = useState<RangeSelectors>(
		initialRangeSelectors
	);
	const [type, setType] = useState<CalculationTypes>(CalculationTypes.Total);

	const {breakdownOrder, breakdowns, filterOrder, filters} = useContext(
		AttributesContext
	);

	const _formRef = useRef<Formik>();

	const saveEventAnalysis = useSaveEventAnalysis(eventAnalysisId);

	const handleSubmit = ({name}) => {
		const {setSubmitting} = _formRef.current.getFormikActions();

		open(
			modalTypes.LOADING_MODAL,
			{
				message: Liferay.Language.get('this-will-only-take-a-moment'),
				title: Liferay.Language.get('updating')
			},
			{closeOnBlur: false}
		);

		saveEventAnalysis({
			analysisType: type,
			channelId,
			compareToPrevious,
			eventAnalysisBreakdowns: breakdownOrder.map(breakdownId =>
				omit(breakdowns[breakdownId], 'id')
			),
			eventAnalysisFilters: filterOrder.map(filterId =>
				omit(filters[filterId], 'id')
			),
			eventDefinitionId: event.id,
			name,
			userId: currentUser.id,
			userName: currentUser.name,
			...getSafeRangeSelectors(rangeSelectors)
		})
			.then(() => {
				setSubmitting(false);

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
			.catch(({message}) => {
				addAlert({
					alertType: Alert.Types.Error,
					message
				});

				setSubmitting(false);

				close();
			});
	};

	return (
		<Form
			initialValues={{
				name: initialName
			}}
			onSubmit={handleSubmit}
			ref={_formRef}
		>
			{({handleSubmit, isSubmitting}) => {
				// TODO: Implement isValid logic
				const isValid = true;

				// TODO: Implement hasChanges logic
				const hasChanges = true;

				return (
					<Form.Form onSubmit={handleSubmit}>
						<NavigationWarning when={hasChanges && !isSubmitting} />

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
								<BasePage.SubHeader>
									<EventAnalysisToolbar isValid={isValid} />
								</BasePage.SubHeader>
							)}

							<BasePage.Body>
								<EventAnalysisEditor
									channelId={channelId}
									compareToPrevious={compareToPrevious}
									event={event}
									onCompareToPreviousChange={
										setCompareToPrevious
									}
									onEventChange={setEvent}
									onRangeSelectorsChange={setRangeSelectors}
									onTypeChange={setType}
									rangeSelectors={rangeSelectors}
									type={type}
								/>
							</BasePage.Body>
						</BasePage>
					</Form.Form>
				);
			}}
		</Form>
	);
};

const EditEventAnalysis: React.FC<IEventAnalysisProps> = props => {
	const {
		attributesState,
		loading,
		...eventAnalysisData
	} = useEventAnalysisData(props.eventAnalysisId);

	if (loading) {
		return <Spinner alignCenter key='LOADING_DISPLAY' />;
	}

	return (
		<AttributesProvider initialState={attributesState}>
			<EventAnalysis {...props} {...eventAnalysisData} />
		</AttributesProvider>
	);
};

const EventAnalysisWrapper: React.FC<IEventAnalysisProps> = props => {
	const {id: eventAnalysisId} = useParams();

	if (eventAnalysisId) {
		return (
			<EditEventAnalysis {...props} eventAnalysisId={eventAnalysisId} />
		);
	}

	return (
		<AttributesProvider>
			<EventAnalysis {...props} />
		</AttributesProvider>
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
)(EventAnalysisWrapper);
