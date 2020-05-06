import Body from './Body';
import Header from '../summary-base-card/Header';
import React from 'react';
import Subtitle from '../summary-base-card/Subtitle';
import SummaryBaseCard from '../summary-base-card';
import Title from '../summary-base-card/Title';
import {
	Header as HeaderType,
	Setup,
	Status,
	Summary
} from '../summary-base-card/types';

interface SummaryCardDraftIProps extends React.HTMLAttributes<HTMLElement> {
	header: HeaderType;
	setup: Setup;
	status: Status;
	summary: Summary;
}

const SummaryCardDraft: React.FC<SummaryCardDraftIProps> = ({
	header,
	setup,
	status,
	summary
}) => (
	<SummaryBaseCard status={status}>
		<SummaryBaseCard.Header
			cardModals={header.cardModals}
			modals={header.modals}
		>
			<Header Description={header.Description} title={header.title} />
		</SummaryBaseCard.Header>
		<SummaryBaseCard.Body>
			<Body {...setup} />
		</SummaryBaseCard.Body>
		{summary.description && (
			<SummaryBaseCard.Footer>
				<Title className='mb-4' label={summary.title} />

				{summary.subtitle && <Subtitle label={summary.subtitle} />}

				<p className='mb-4'>{summary.description}</p>
			</SummaryBaseCard.Footer>
		)}
	</SummaryBaseCard>
);

export default SummaryCardDraft;
