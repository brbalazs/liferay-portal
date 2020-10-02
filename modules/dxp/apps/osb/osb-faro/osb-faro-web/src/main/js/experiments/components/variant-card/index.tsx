import Card from 'shared/components/Card';
import ClayNavigationBar from '@clayui/navigation-bar';
import columns from './variant-columns';
import React from 'react';
import Table from 'shared/components/table';
import {CLASSNAME} from './constants';
import {LegendData} from 'shared/components/Legend';
import {Status} from 'experiments/util/types';
import {useStateValue} from 'experiments/state';

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
	label: string;
	legend?: Array<LegendData>;
	mediansData?: any;
	metric: string;
	metricUnit: string;
	status: Status;
	winnerDXPVariantId: string;
}

const VariantCard: React.FC<VariantCardIProps> = ({
	bestVariant,
	children,
	data,
	label,
	metric,
	metricUnit,
	status,
	winnerDXPVariantId
}) => {
	const [{variantChartTriggered}, dispatch]: any = useStateValue();

	return (
		<Card className={CLASSNAME}>
			<Card.Header>
				<Card.Title>{label}</Card.Title>
			</Card.Header>

			<Card.Body>
				<div className={`${CLASSNAME}-charts`}>
					<ClayNavigationBar
						triggerLabel={
							variantChartTriggered === 'medians'
								? Liferay.Language.get('medians')
								: Liferay.Language.get('per-day')
						}
					>
						<ClayNavigationBar.Item
							active={variantChartTriggered === 'medians'}
						>
							<button
								className='btn btn-unstyled btn-block btn-sm nav-link'
								onClick={() =>
									dispatch({
										newAction: 'medians',
										type: 'setVariantChartTriggered'
									})
								}
								type='button'
							>
								{Liferay.Language.get('medians')}
							</button>
						</ClayNavigationBar.Item>
						<ClayNavigationBar.Item
							active={variantChartTriggered === 'per-day'}
						>
							<button
								className='btn btn-unstyled btn-block btn-sm nav-link'
								onClick={() =>
									dispatch({
										newAction: 'per-day',
										type: 'setVariantChartTriggered'
									})
								}
								type='button'
							>
								{Liferay.Language.get('per-day')}
							</button>
						</ClayNavigationBar.Item>
					</ClayNavigationBar>

					{children}
				</div>
			</Card.Body>

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
		</Card>
	);
};

export default VariantCard;
