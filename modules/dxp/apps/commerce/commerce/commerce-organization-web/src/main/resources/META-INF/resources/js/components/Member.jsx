import React from 'react';
import PropTypes from 'prop-types';


function Member(props) {
	const {
		name,
		jobTitle: role
	} = props.member;

	return(
		<li role="button" tabIndex="-1" className="member">
			<span className="member-picture"
				  style={{background: `url(${'https://cdn-images-1.medium.com/max/1200/1*YFq_5JI-G69nPN4xHl--vw.jpeg'}) center no-repeat #CCC`}}></span>
			<span className="member-data">
				<p className="member-data-name">{name}</p>
				<p>
					<span className="member-data-role">{role}</span>
				</p>
			</span>
		</li>
	);
}

Member.defaultProps = {
	pictureUrl: 'https://media.licdn.com/dms/image/C4D03AQHBDF4IQuqYvw/profile-displayphoto-shrink_200_200/0?e=1563408000&v=beta&t=32AHPawqDkJVVJBr-6RTqdthR5XYO1G-nizpKhK7Nrc',
	name: 'Beanie Pirate',
	role: 'Real graphic designer',
	location: 'Vimercate',
	tabIndex: 5
};

Member.propTypes = {
	pictureUrl: PropTypes.string,
	name: PropTypes.string,
	role: PropTypes.string,
	location: PropTypes.string,
	tabIndex: PropTypes.number
};

export default Member;
