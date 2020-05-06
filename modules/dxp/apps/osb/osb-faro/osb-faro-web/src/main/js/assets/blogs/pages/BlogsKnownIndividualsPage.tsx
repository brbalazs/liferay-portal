import KnownIndividualsListCard from '../hocs/KnownIndividualsListCard';
import React from 'react';

interface IBlogsKnownIndividualsPageProps {
	router: object;
}

const BlogsKnownIndividualsPage: React.FC<IBlogsKnownIndividualsPageProps> = ({
	router
}: IBlogsKnownIndividualsPageProps) => (
	<div className='row'>
		<div className='col-sm-12'>
			<KnownIndividualsListCard router={router} />
		</div>
	</div>
);

export default BlogsKnownIndividualsPage;
