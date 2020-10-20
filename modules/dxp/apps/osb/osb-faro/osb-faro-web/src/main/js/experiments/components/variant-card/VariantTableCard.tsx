import BasePage from 'shared/components/base-page';
import columns from './variant-columns';
import getVariantTableMapper from 'experiments/hocs/mappers/experiment-variant-table-mapper';
import React, {useContext} from 'react';
import Table from 'shared/components/table';
import {CLASSNAME} from './constants';
import {EXPERIMENT_QUERY} from 'experiments/queries/ExperimentQuery';
import {SafeResults} from 'shared/hoc/util';
import {Status} from 'experiments/util/types';
import {useQuery} from '@apollo/react-hooks';

type Variant = {
	changes: number;
	confidenceLevel: number;
	control: boolean;
	dxpVariantId: string;
	dxpVariantName: string;
	improvementChance: number;
	improvementLift: number;
	metricRangeEnd: number;
	metricRangeStart: number;
	probabilityToWin: number;
	trafficSplit: number;
	uniqueVisitors: number;
};

export interface VariantCardIProps extends React.HTMLAttributes<HTMLElement> {
	bestVariant: Variant;
	data: Array<Variant>;
	metric: string;
	metricUnit: string;
	status: Status;
	winnerDXPVariantId: string;
}

const VariantTableCard = () => {
	const {
		router: {
			params: {id: experimentId}
		}
	} = useContext(BasePage.Context);

	const result = useQuery(EXPERIMENT_QUERY, {
		variables: {experimentId}
	});

	return (
		<SafeResults {...result}>
			{props => {
				const {
					bestVariant,
					data,
					metric,
					metricUnit,
					status,
					winnerDXPVariantId
				}: VariantCardIProps = getVariantTableMapper(props);

				return (
					<div className={`${CLASSNAME}-table`}>
						<Table
							columns={columns({
								bestVariant,
								metric,
								metricUnit,
								status,
								winnerDXPVariantId
							})}
							defaultSort={{
								field: 'dxpVariantName'
							}}
							headingNowrap={false}
							internalSort
							items={data}
							nowrap={false}
							rowIdentifier='dxpVariantId'
						/>
					</div>
				);
			}}
		</SafeResults>
	);
};

export default VariantTableCard;
