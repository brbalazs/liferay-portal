import Label from './Label';
import RadioGroup from '../RadioGroup';
import React from 'react';
import {FieldProps} from 'formik';

interface IFormRadioGroupProps
	extends FieldProps,
		React.HTMLAttributes<HTMLElement> {
	label: string;
	onChange: (value: any) => void;
}

const FormRadioGroup: React.FC<IFormRadioGroupProps> = ({
	children,
	className,
	field,
	form,
	label,
	onChange
}) => {
	const {name, value: checked} = field;

	const handleChange = value => {
		const {setFieldValue} = form;

		setFieldValue(name, value);

		if (onChange) {
			onChange(value);
		}
	};

	return (
		<div className={className}>
			{label && <Label htmlFor={name}>{label}</Label>}

			<RadioGroup
				{...field}
				checked={checked}
				id={name}
				name={name}
				onChange={handleChange}
			>
				{children}
			</RadioGroup>
		</div>
	);
};

export default Object.assign(FormRadioGroup, {
	Option: RadioGroup.Option,
	Subsection: RadioGroup.Subsection
});
