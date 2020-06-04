import BasePage from 'settings/components/BasePage';
import React from 'react';
import RecommendationList from '../hocs/RecommendationList';
import {RouterType} from 'shared/types';

interface IRecommendationsProps {
	router: RouterType;
}

const Recommendations: React.FC<IRecommendationsProps> = ({router}) => {
	const {groupId} = router.params;

	return (
		<BasePage
			groupId={groupId}
			pageDescription={Liferay.Language.get(
				'create-and-train-machine-learning-models-to-use-in-your-recommendations'
			)}
			pageTitle={Liferay.Language.get('recommendations')}
		>
			<RecommendationList groupId={groupId} router={router} />
		</BasePage>
	);
};

export default Recommendations;
