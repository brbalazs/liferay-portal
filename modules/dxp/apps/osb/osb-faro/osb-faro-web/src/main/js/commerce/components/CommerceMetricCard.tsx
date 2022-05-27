import BaseCard from 'cerebro-shared/components/base-card';
import Card from 'shared/components/Card';
import ErrorDisplay from 'shared/components/ErrorDisplay';
import React, {useState} from 'react';
import StatesRenderer from 'shared/components/states-renderer/StatesRenderer';
import Trend from 'shared/components/Trend';
import {ApolloError} from 'apollo-client';
import {Currencies} from 'commerce/utils/types';
import {getIcon, getStatsColor} from 'shared/util/metrics';
import {getRangeSelectorsFromQuery} from 'shared/util/util';
import {gql} from 'apollo-boost';
import {RawRangeSelectors} from 'shared/types';
import {sub} from 'shared/util/lang';
import {useParams} from 'react-router-dom';
import {useQuery} from '@apollo/react-hooks';

interface ICommerceMetricCardProps<TGraphQlData>
	extends React.HTMLAttributes<HTMLElement> {
	description: string;
	emptyTitle: string;
	label: string;
	mapper: (
		result: TGraphQlData
	) => {
		currencies: Currencies;
	};
	Query: typeof gql;
}

interface ICommerceMetricCardWithStatesRendererProps
	extends React.HTMLAttributes<HTMLElement> {
	empty?: boolean;
	emptyTitle: string;
	error?: ApolloError;
	loading?: boolean;
}

interface TGraphQlVariables extends RawRangeSelectors {
	channelId: string;
}

const CommerceCardWithStatesRenderer: React.FC<ICommerceMetricCardWithStatesRendererProps> = ({
	children,
	empty = false,
	emptyTitle,
	error,
	loading = false
}) => (
	<StatesRenderer empty={empty} error={!!error} loading={loading}>
		<StatesRenderer.Loading displayCard />
		<StatesRenderer.Empty
			description={Liferay.Language.get(
				'check-back-later-to-verify-if-data-has-been-received-from-your-data-sources'
			)}
			showIcon={false}
			title={emptyTitle}
		/>
		<StatesRenderer.Error apolloError={error}>
			<ErrorDisplay />
		</StatesRenderer.Error>
		<StatesRenderer.Success>{children}</StatesRenderer.Success>
	</StatesRenderer>
);

function CommerceMetricCard<TGraphQlData>({
	description,
	emptyTitle,
	label,
	mapper,
	Query
}: ICommerceMetricCardProps<TGraphQlData>): React.ReactElement {
	const {channelId, query} = useParams();
	const [rangeSelectors, setRangeSelectors] = useState<RawRangeSelectors>(
		getRangeSelectorsFromQuery(query)
	);
	const {data, error, loading} = useQuery<TGraphQlData, TGraphQlVariables>(
		Query,
		{
			variables: {
				channelId,
				...rangeSelectors
			}
		}
	);

	const result = mapper(data);
	const {trend, value} = result?.currencies?.['USD'] ?? {};

	return (
		<BaseCard
			className='commerce-card-root'
			label={label}
			legacyDropdownRangeKey={false}
			minHeight={298}
		>
			{({rangeSelectors}) => {
				setRangeSelectors(rangeSelectors);

				return (
					<Card.Body className='align-items-center justify-content-center'>
						<CommerceCardWithStatesRenderer
							empty={!result}
							emptyTitle={emptyTitle}
							error={error}
							loading={loading}
						>
							<h1 className='font-size-lg-3x font-weight-semibold mb-2'>
								{value}
							</h1>

							<div className='d-flex align-items-center mb-2'>
								<span className='font-size-sm-1x text-secondary'>
									{sub(
										Liferay.Language.get('x-vs-previous'),
										[
											<Trend
												className='d-inline'
												color={getStatsColor(
													trend?.trendClassification
												)}
												icon={getIcon(
													trend?.percentage
												)}
												key='TREND'
												label={`${trend?.percentage}%`}
											/>
										],
										false
									)}
								</span>
							</div>

							<p className='font-size-sm-1x text-center'>
								{description}
							</p>
						</CommerceCardWithStatesRenderer>
					</Card.Body>
				);
			}}
		</BaseCard>
	);
}

export default CommerceMetricCard;
