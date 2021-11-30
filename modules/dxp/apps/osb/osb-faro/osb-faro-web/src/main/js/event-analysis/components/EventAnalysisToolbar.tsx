import Button from 'shared/components/Button';
import Form from 'shared/components/form';
import React from 'react';
import TitleEditor from 'shared/components/TitleEditor';

const EventAnalysisToolbar: React.FC<
	React.HTMLAttributes<HTMLElement>
> = () => (
	<Form
		initialValues={{
			name: ''
		}}
		// TODO: LRAC-9841 Create query to add new event analysis
		onSubmit={() => {}}
	>
		<div className='event-analysis-toolbar-root'>
			<div className='event-analysis-toolbar-left-content'>
				<TitleEditor
					name='name'
					placeholder={Liferay.Language.get('unnamed-report')}
				/>
			</div>

			<div className='event-analysis-toolbar-right-content'>
				<Button.Group>
					<Button.GroupItem>
						<Button disabled display='primary' size='sm'>
							{Liferay.Language.get('save-analysis')}
						</Button>
					</Button.GroupItem>

					<Button.GroupItem>
						{/* TODO: return to list when click cancel */}
						<Button size='sm'>
							{Liferay.Language.get('cancel')}
						</Button>
					</Button.GroupItem>
				</Button.Group>
			</div>
		</div>
	</Form>
);

export default EventAnalysisToolbar;
