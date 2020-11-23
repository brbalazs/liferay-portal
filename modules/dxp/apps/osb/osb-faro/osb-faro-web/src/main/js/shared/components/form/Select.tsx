import getCN from 'classnames';
import HelpBlock from './HelpBlock';
import Label from './Label';
import React from 'react';
import Select from '../Select';
import {FieldProps} from 'formik';

interface IFormSelectProps
	extends FieldProps,
		React.HTMLAttributes<HTMLElement> {
	disabled: boolean;
	inline?: boolean;
	label: React.ReactNode;
	popover: {
		content: React.ReactNode;
		title: React.ReactNode;
	};
	required?: boolean;
}

const FormSelect: React.FC<IFormSelectProps> = ({
	children,
	className,
	field,
	form,
	inline = false,
	label,
	popover,
	required = false,
	...otherProps
}) => {
	const {disabled} = otherProps;
	const {name} = field;

	const error = form.errors[name];
	const touched = form.touched[name];

	const classes = getCN(className, {
		'form-inline-group': inline,
		'has-error': touched && error,
		'has-success': touched && !error && !disabled
	});

	return (
		<div className={classes}>
			{label && (
				<Label htmlFor={name} popover={popover} required={required}>
					{label}
				</Label>
			)}

			<Select {...otherProps} {...field} id={name} name={name}>
				{children}
			</Select>

			<HelpBlock name={name} />
		</div>
	);
};

export default Object.assign(FormSelect, {Item: Select.Item});
