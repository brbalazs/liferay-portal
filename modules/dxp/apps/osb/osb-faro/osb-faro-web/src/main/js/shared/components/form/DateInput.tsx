import DateInput from '../DateInput';
import getCN from 'classnames';
import HelpBlock from './HelpBlock';
import Label from './Label';
import React from 'react';
import {FieldProps} from 'formik';
import {isNumber} from 'lodash';

interface IFormDateInputProps
	extends FieldProps,
		React.HTMLAttributes<HTMLElement> {
	info: string;
	inline: boolean;
	label: string;
	required?: boolean;
	width: number;
}

const FormDateInput: React.FC<IFormDateInputProps> = ({
	className,
	field,
	form,
	info,
	inline = false,
	label,
	required = false,
	width
}) => {
	const {name} = field;

	const handleChange = (value): void => {
		const {setFieldValue} = form;

		setFieldValue(name, value);
	};

	const error = form.errors[name];
	const touched = form.touched[name];

	const classes = getCN('form-date-input-root', className, {
		'form-inline-group': inline,
		'has-error': error && touched,
		'has-success': !error && touched
	});

	const style = isNumber(width)
		? {flexBasis: `${width}%`, flexGrow: 0}
		: undefined;

	return (
		<div className={classes} style={style}>
			{label && (
				<Label htmlFor={name} info={info} required={required}>
					{label}
				</Label>
			)}

			<DateInput {...field} id={name} onChange={handleChange} />

			<HelpBlock name={name} />
		</div>
	);
};

export default FormDateInput;
