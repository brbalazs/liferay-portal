import * as API from 'shared/api';
import Button from 'shared/components/Button';
import Form, {validateInputMessage} from 'shared/components/form';
import getCN from 'classnames';
import Modal from 'shared/components/modal';
import React from 'react';
import {DataSource} from 'shared/util/records';
import {SafeResults} from 'shared/hoc/util';
import {sub} from 'shared/util/lang';
import {useRequest} from 'shared/hooks';

interface IDeleteChannelModalProps extends React.HTMLAttributes<HTMLElement> {
	channelName: string;
	channelIds: Array<string>;
	groupId: string;
	onClose: () => void;
	onSubmit: () => void;
	title?: string;
}

const renderDataSourceMessage = (items: Array<DataSource>, total: number) => {
	if (!total) {
		return null;
	}

	const dataSourceNames = items.map(({name}) => name).join(', ');

	return (
		<p>
			{sub(
				Liferay.Language.get(
					'to-reconnect-to-analytics-cloud-with-x,-please-make-sure-x-has-been-updated-with-the-latest-fixpack'
				),
				[dataSourceNames]
			)}
		</p>
	);
};

const DeleteChannelModal: React.FC<IDeleteChannelModalProps> = ({
	channelName,
	channelIds,
	className,
	groupId,
	onClose,
	onSubmit,
	title = Liferay.Language.get('confirm')
}) => {
	const {data, error, loading, refetch} = useRequest(
		API.dataSource.fetchChannels,
		{
			channelIds,
			groupId
		}
	);

	return (
		<Modal
			className={getCN(
				'confirmation-modal-root',
				'modal-warning',
				className
			)}
		>
			<Form
				initialValues={{
					delete: ''
				}}
				onSubmit={onSubmit}
			>
				{({handleSubmit, isSubmitting, isValid}) => (
					<Form.Form onSubmit={handleSubmit}>
						<Modal.Header
							iconSymbol='warning-full'
							onClose={onClose}
							title={title}
						/>
						<SafeResults
							{...{data, error, loading}}
							onReload={refetch}
							page={false}
							pageDisplay={false}
							spacer
						>
							{({items, total}) => (
								<>
									<Modal.Body>
										<div className='text-secondary'>
											<p>
												<strong>
													{sub(
														Liferay.Language.get(
															'to-delete-x,-copy-the-sentence-below-to-confirm-your-intention-to-delete-property'
														),
														[channelName]
													)}
												</strong>
											</p>

											<p>
												{Liferay.Language.get(
													'this-will-result-in-the-complete-removal-of-this-property-and-its-historical-events.-you-will-not-be-able-to-undo-this-operation'
												)}
											</p>

											{renderDataSourceMessage(
												items,
												total
											)}
										</div>

										<div className='font-weight-bold mb-3'>
											{sub(
												Liferay.Language.get(
													'copy-the-following-x'
												),
												[
													<span
														className='font-weight-normal text-secondary'
														key='deletePropertyText'
													>
														{sub(
															Liferay.Language.get(
																'delete-x'
															),
															[channelName]
														)}
													</span>
												],
												false
											)}
										</div>

										<Form.Input
											autofocus
											name='delete'
											validate={validateInputMessage(sub(
												Liferay.Language.get('delete-x'),
												[channelName]
											) as string)}
										/>
									</Modal.Body>

									<Modal.Footer>
										<Button onClick={onClose}>
											{Liferay.Language.get('cancel')}
										</Button>

										<Button
											disabled={!isValid || isSubmitting}
											display='warning'
											loading={isSubmitting}
											type='submit'
										>
											{Liferay.Language.get('delete')}
										</Button>
									</Modal.Footer>
								</>
							)}
						</SafeResults>
					</Form.Form>
				)}
			</Form>
		</Modal>
	);
};

export default DeleteChannelModal;
