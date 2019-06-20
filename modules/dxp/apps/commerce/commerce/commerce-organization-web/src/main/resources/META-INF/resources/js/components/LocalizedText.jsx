import React, { Fragment } from 'react'

function convertString(string) {
	try {
		return window.Liferay.Language.get(string);
	} catch (error) {
		console.log(error);
		return string;
	}
}

export default function LocalizedText(props) {
	return(
		<Fragment>
			{ convertString(props.children) }
		</Fragment>
	)
}
