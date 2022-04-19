import Icon from 'shared/components/Icon';
import Input from 'shared/components/Input';
import Label from 'shared/components/Label';
import React, {useState} from 'react';
import {Sizes} from 'shared/util/constants';

const req = require.context('../../../images', false, /\.svg$/);

const icons = req.keys().map((name, id) => ({
	id,
	name: name.replace('./', '').replace('.svg', '')
}));

const IconLibraryKit = () => {
	const [value, setValue] = useState('');
	const filteredIcons = icons.filter(
		({name}) => !value || name.includes(value)
	);

	return (
		<>
			<div>
				<Input
					onInput={({target: {value}}) => setValue(value)}
					placeholder='filter by icon name'
				/>
			</div>

			<div className='row'>
				{filteredIcons.map(({id, name}) => (
					<div
						className='col-sm-3 py-4 d-flex align-items-center justify-content-center'
						key={id}
					>
						<div className='text-center'>
							<div className='mb-3'>
								<Icon size={Sizes.XLarge} symbol={name} />
							</div>

							<Label
								className='d-block m-0'
								display='secondary'
								size='lg'
							>
								{name}
							</Label>
						</div>
					</div>
				))}
			</div>
		</>
	);
};

export default IconLibraryKit;
