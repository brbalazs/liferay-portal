import BaseEventAnalysisPage from './BaseEventAnalysisPage';
import React from 'react';
import {AttributesProvider} from 'event-analysis/components/event-analysis-editor/context/attributes';

const EventAnalysisCreate: React.FC<React.HTMLAttributes<HTMLElement>> = () => (
	<AttributesProvider>
		<BaseEventAnalysisPage />
	</AttributesProvider>
);

export default EventAnalysisCreate;
