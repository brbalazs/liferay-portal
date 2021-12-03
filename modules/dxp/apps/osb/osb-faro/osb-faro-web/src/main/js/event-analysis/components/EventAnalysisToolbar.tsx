import Button from 'shared/components/Button';
import Form from 'shared/components/form';
import NavigationWarning from 'shared/components/NavigationWarning';
import React from 'react';
import TitleEditor from 'shared/components/TitleEditor';

interface IEventAnalysisToolbarProps {
	name: string;
	onSubmit: (form: {name: string}) => void;
}

const EventAnalysisToolbar: React.FC<IEventAnalysisToolbarProps> = ({
	name: initialName,
	onSubmit
}) => (
	<Form
		initialValues={{
			name: initialName
		}}
		onSubmit={onSubmit}
	>
		{({handleSubmit, isSubmitting, isValid, values: {name}}) => {
			const hasChanges = name !== initialName;

			return (
				<Form.Form
					className='event-analysis-toolbar-root'
					onSubmit={handleSubmit}
				>
					<NavigationWarning when={hasChanges && !isSubmitting} />

					<div className='event-analysis-toolbar-left-content'>
						<TitleEditor
							name='name'
							placeholder={Liferay.Language.get('unnamed-report')}
						/>
					</div>

					<div className='event-analysis-toolbar-right-content'>
						<Button.Group>
							<Button.GroupItem>
								<Button
									disabled={!isValid || !hasChanges}
									display='primary'
									size='sm'
									type='submit'
								>
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
				</Form.Form>
			);
		}}
	</Form>
);

export default EventAnalysisToolbar;
