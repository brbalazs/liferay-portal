import BlogsListCard from '../hocs/BlogsListCard';
import React from 'react';

interface IBlogsListPageProps {
	router: object;
}

const BlogsListPage: React.FC<IBlogsListPageProps> = ({router}) => (
	<div className='row'>
		<div className='col-sm-12'>
			<BlogsListCard router={router} />
		</div>
	</div>
);

export default BlogsListPage;
