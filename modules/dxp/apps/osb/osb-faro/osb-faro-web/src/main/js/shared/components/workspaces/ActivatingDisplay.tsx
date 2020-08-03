import Button from 'shared/components/Button';
import Constants from 'shared/util/constants';
import React from 'react';
import Sheet from 'shared/components/Sheet';
import urlConstants from 'shared/util/url-constants';
import WorkspacesBasePage from 'shared/components/workspaces/BasePage';
import {fetchProjectState} from 'shared/actions/projects';
import {withPolling} from 'shared/hoc';

const {projectStates} = Constants;

const ActivatingDisplay = () => (
	<WorkspacesBasePage title={Liferay.Language.get('activating-workspace')}>
		<Sheet>
			<Sheet.Header className='mb-4'>
				<h3 className='title'>
					{Liferay.Language.get('your-workspace-is-being-activated')}
				</h3>
			</Sheet.Header>
			<Sheet.Body>
				<Sheet.Section>
					<p>
						{Liferay.Language.get(
							'this-process-will-take-a-couple-minutes'
						)}
					</p>
					<p>
						{Liferay.Language.get(
							'in-the-meantime-check-out-our-documentation-to-get-familiar-with-the-features'
						)}
					</p>
				</Sheet.Section>
			</Sheet.Body>

			<Sheet.Footer divider={false}>
				<Button
					display='primary'
					externalLink
					href={urlConstants.DOCUMENTATION_ADMIN_LINK}
				>
					{Liferay.Language.get('check-out-docs')}
				</Button>
			</Sheet.Footer>
		</Sheet>
	</WorkspacesBasePage>
);

export default withPolling(
	fetchProjectState,
	({state}) => state !== projectStates.activating
)(ActivatingDisplay);
