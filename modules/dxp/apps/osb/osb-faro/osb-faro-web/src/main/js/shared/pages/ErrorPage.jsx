import Button from 'shared/components/Button';
import React from 'react';
import {PropTypes} from 'prop-types';
import {Routes, toRoute} from 'shared/util/router';

export default class ErrorPage extends React.Component {
	static defaultProps = {
		href: toRoute(Routes.BASE),
		linkLabel: Liferay.Language.get('go-to-home'),
		message: Liferay.Language.get(
			'the-page-you-are-looking-for-does-not-exist'
		),
		subtitle: Liferay.Language.get('page-not-found'),
		title: '404'
	};

	static propTypes = {
		href: PropTypes.string,
		linkLabel: PropTypes.string,
		message: PropTypes.string,
		subtitle: PropTypes.string,
		title: PropTypes.string
	};

	render() {
		const {href, linkLabel, message, subtitle, title} = this.props;

		return (
			<div
				className={`error-page-root page-container${
					this.props.className ? ` ${this.props.className}` : ''
				}`}
			>
				<h1>{title}</h1>

				<h3>{subtitle}</h3>

				<p>{message}</p>

				<Button href={href}>{linkLabel}</Button>
			</div>
		);
	}
}
