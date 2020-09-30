import BasePage from 'shared/components/base-page';
import getVariantMapper from 'experiments/hocs/mappers/experiment-variant-card-mapper';
import React, {useContext} from 'react';
import VariantCard from 'experiments/components/variant-card/withChart';
import WrappedSafeComponent from 'cerebro-shared/hocs/WrappedSafeComponent';
import {EXPERIMENT_QUERY} from 'experiments/queries/ExperimentQuery';
import {useQuery} from '@apollo/react-hooks';

interface IWithVarianCardProps extends React.HTMLAttributes<HTMLElement> {
	label?: string;
}

const withVarianCard: React.FC<IWithVarianCardProps> = ({label}) => {
	const {
		router: {
			params: {id: experimentId}
		}
	} = useContext(BasePage.Context);

	const result = useQuery(EXPERIMENT_QUERY, {
		variables: {experimentId}
	});

	return (
		<WrappedSafeComponent
			{...result}
			loadingWithContainer
			mapper={getVariantMapper}
		>
			{props => <VariantCard {...props} label={label} />}
		</WrappedSafeComponent>
	);
};

export default withVarianCard;
