import Card from 'shared/components/Card';
import ClayNavigationBar from '@clayui/navigation-bar';
import MediansChart from 'experiments/components/variant-card/MediansChart';
import PerDayChart from 'experiments/components/variant-card/PerDayChart';
import React from 'react';
import VariantTableCard from 'experiments/components/variant-card/VariantTableCard';
import {CLASSNAME} from './constants';
import {useStateValue} from 'experiments/state';

interface VariantCardIProps {
	label: String;
}

const variantCard: React.FC<VariantCardIProps> = ({label}) => {
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

					{variantChartTriggered === 'medians' ? (
						<MediansChart />
					) : (
						variantChartTriggered === 'per-day' && <PerDayChart />
					)}
					<VariantTableCard />
				</div>
			</Card.Body>
		</Card>
	);
};

export default variantCard;
