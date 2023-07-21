/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.workflow.task.web.internal.notifications;

import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.UserNotificationEvent;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserNotificationEventLocalService;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Html;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.workflow.WorkflowHandler;
import com.liferay.portal.kernel.workflow.WorkflowHandlerRegistryUtil;
import com.liferay.portal.kernel.workflow.WorkflowTask;
import com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil;
import com.liferay.portal.workflow.task.web.internal.permission.WorkflowTaskPermissionChecker;

import java.lang.reflect.Field;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Inácio Nery
 */
@PrepareForTest(
	{WorkflowHandlerRegistryUtil.class, WorkflowTaskManagerUtil.class}
)
@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor(
	{
		"com.liferay.portal.kernel.workflow.WorkflowHandlerRegistryUtil",
		"com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil"
	}
)
public class WorkflowTaskUserNotificationHandlerTest extends PowerMockito {

	@Before
	public void setUp() throws Exception {
		setUpHtmlUtil();
		setUpJSONFactoryUtil();
		setUpThemeDisplay();
		setUpUserNotificationEventLocalService();
		setUpWorkflowTaskManagerUtil();
		setUpWorkflowTaskPermissionChecker();
		setUpWorkflowHandlerRegistryUtil();

		_notificationMessage = RandomTestUtil.randomString();
	}

	@Test
	public void testInvalidWorkflowTaskIdShouldReturnBlankBody()
		throws Exception {

		UserNotificationEvent userNotificationEvent = mockUserNotificationEvent(
			_INVALID_WORKFLOW_TASK_ID);

		Assert.assertEquals(
			StringPool.BLANK,
			_workflowTaskUserNotificationHandler.getBody(
				userNotificationEvent, _serviceContext));
	}

	@Test
	public void testInvalidWorkflowTaskIdShouldReturnLink() throws Exception {
		UserNotificationEvent userNotificationEvent = mockUserNotificationEvent(
			_VALID_ENTRY_CLASS_NAME, _INVALID_WORKFLOW_TASK_ID);

		Assert.assertEquals(
			_VALID_LINK,
			_workflowTaskUserNotificationHandler.getLink(
				userNotificationEvent, _serviceContext));
	}

	@Test
	public void testNullWorkflowTaskIdShouldReturnBlankLink() throws Exception {
		UserNotificationEvent userNotificationEvent = mockUserNotificationEvent(
			_VALID_ENTRY_CLASS_NAME, 0L);

		Assert.assertEquals(
			StringPool.BLANK,
			_workflowTaskUserNotificationHandler.getLink(
				userNotificationEvent, _serviceContext));
	}

	@Test
	public void testNullWorkflowTaskIdShouldReturnBody() throws Exception {
		UserNotificationEvent userNotificationEvent = mockUserNotificationEvent(
			0);

		Assert.assertEquals(
			_notificationMessage,
			_workflowTaskUserNotificationHandler.getBody(
				userNotificationEvent, _serviceContext));
	}

	@Test
	public void testValidWorkflowTaskIdShouldReturnBody() throws Exception {
		UserNotificationEvent userNotificationEvent = mockUserNotificationEvent(
			_VALID_WORKFLOW_TASK_ID);

		Assert.assertEquals(
			_notificationMessage,
			_workflowTaskUserNotificationHandler.getBody(
				userNotificationEvent, _serviceContext));
	}

	@Test
	public void testValidWorkflowTaskIdShouldReturnLink() throws Exception {
		UserNotificationEvent userNotificationEvent = mockUserNotificationEvent(
			_VALID_ENTRY_CLASS_NAME, _VALID_WORKFLOW_TASK_ID);

		Assert.assertEquals(
			_VALID_LINK,
			_workflowTaskUserNotificationHandler.getLink(
				userNotificationEvent, _serviceContext));
	}

	protected UserNotificationEvent mockUserNotificationEvent(
		long workflowTaskId) {

		return mockUserNotificationEvent(null, workflowTaskId);
	}

	protected UserNotificationEvent mockUserNotificationEvent(
		String entryClassName, Long workflowTaskId) {

		UserNotificationEvent userNotificationEvent = mock(
			UserNotificationEvent.class);

		JSONObject jsonObject = _jsonFactory.createJSONObject();

		jsonObject.put("entryClassName", entryClassName);
		jsonObject.put("notificationMessage", _notificationMessage);
		jsonObject.put("workflowTaskId", workflowTaskId);

		when(
			userNotificationEvent.getPayload()
		).thenReturn(
			jsonObject.toJSONString()
		);

		return userNotificationEvent;
	}

	protected WorkflowHandler mockWorkflowHandler() throws PortalException {
		WorkflowHandler workflowHandler = mock(WorkflowHandler.class);

		when(
			workflowHandler.getURLEditWorkflowTask(
				_INVALID_WORKFLOW_TASK_ID, _serviceContext)
		).thenReturn(
			_VALID_LINK
		);

		when(
			workflowHandler.getURLEditWorkflowTask(
				_VALID_WORKFLOW_TASK_ID, _serviceContext)
		).thenReturn(
			_VALID_LINK
		);

		return workflowHandler;
	}

	protected void setUpHtmlUtil() {
		HtmlUtil htmlUtil = new HtmlUtil();

		htmlUtil.setHtml(_html);

		when(
			_html.escape(Matchers.anyString())
		).then(
			new Answer<String>() {

				@Override
				public String answer(InvocationOnMock invocationOnMock)
					throws Throwable {

					return invocationOnMock.getArgumentAt(0, String.class);
				}

			}
		);
	}

	protected void setUpJSONFactoryUtil() {
		JSONFactoryUtil jsonFactoryUtil = new JSONFactoryUtil();

		jsonFactoryUtil.setJSONFactory(_jsonFactory);
	}

	protected void setUpThemeDisplay() {
		ThemeDisplay themeDisplay = mock(ThemeDisplay.class);

		when(
			_serviceContext.getThemeDisplay()
		).thenReturn(
			themeDisplay
		);

		when(
			themeDisplay.getSiteGroupId()
		).thenReturn(
			RandomTestUtil.randomLong()
		);
	}

	protected void setUpUserNotificationEventLocalService() throws Exception {
		UserNotificationEventLocalService userNotificationEventLocalService =
			mock(UserNotificationEventLocalService.class);

		_workflowTaskUserNotificationHandler.
			setUserNotificationEventLocalService(
				userNotificationEventLocalService);
	}

	protected void setUpWorkflowHandlerRegistryUtil() throws PortalException {
		mockStatic(WorkflowHandlerRegistryUtil.class);

		when(
			WorkflowHandlerRegistryUtil.getWorkflowHandler(
				Matchers.eq(_INVALID_ENTRY_CLASS_NAME))
		).thenReturn(
			null
		);

		WorkflowHandler workflowHandler = mockWorkflowHandler();

		when(
			WorkflowHandlerRegistryUtil.getWorkflowHandler(
				Matchers.eq(_VALID_ENTRY_CLASS_NAME))
		).thenReturn(
			workflowHandler
		);
	}

	protected void setUpWorkflowTaskManagerUtil() throws PortalException {
		mockStatic(WorkflowTaskManagerUtil.class);

		when(
			WorkflowTaskManagerUtil.fetchWorkflowTask(
				Matchers.anyLong(), Matchers.eq(_INVALID_WORKFLOW_TASK_ID))
		).thenReturn(
			null
		);

		WorkflowTask workflowTask = mock(WorkflowTask.class);

		when(
			WorkflowTaskManagerUtil.fetchWorkflowTask(
				Matchers.anyLong(), Matchers.eq(_VALID_WORKFLOW_TASK_ID))
		).thenReturn(
			workflowTask
		);
	}

	protected void setUpWorkflowTaskPermissionChecker() throws Exception {
		WorkflowTaskPermissionChecker workflowTaskPermissionChecker = mock(
			WorkflowTaskPermissionChecker.class);

		when(
			workflowTaskPermissionChecker.hasPermission(
				Matchers.anyLong(), Matchers.any(WorkflowTask.class),
				Matchers.any(PermissionChecker.class))
		).thenReturn(
			true
		);

		Field field = ReflectionUtil.getDeclaredField(
			_workflowTaskUserNotificationHandler.getClass(),
			"_workflowTaskPermissionChecker");

		field.set(
			_workflowTaskUserNotificationHandler,
			workflowTaskPermissionChecker);
	}

	private static final String _INVALID_ENTRY_CLASS_NAME =
		RandomTestUtil.randomString();

	private static final Long _INVALID_WORKFLOW_TASK_ID =
		RandomTestUtil.randomLong();

	private static final String _VALID_ENTRY_CLASS_NAME =
		RandomTestUtil.randomString();

	private static final String _VALID_LINK = RandomTestUtil.randomString();

	private static final Long _VALID_WORKFLOW_TASK_ID =
		RandomTestUtil.randomLong();

	@Mock
	private Html _html;

	private final JSONFactory _jsonFactory = new JSONFactoryImpl();
	private String _notificationMessage;

	@Mock
	private ServiceContext _serviceContext;

	private final WorkflowTaskUserNotificationHandler
		_workflowTaskUserNotificationHandler =
			new WorkflowTaskUserNotificationHandler();

}