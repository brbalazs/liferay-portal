<div class="minium-frame__sidebar">
	<div class="minium-sidebar">
		<div class="minium-sidebar__start">
			<div class="minium-logo">
				<a class="${logo_css_class}" href="${site_default_url}" title="<@liferay.language_format arguments="${site_name}" key="go-to-x" />">
					<#if show_site_name>
						<span class="text-truncate-inline">
							<span class="logo-text-sm text-truncate">${site_name}</span>
						</span>
					<#else>
						<img alt="${logo_description}" class="logo-image-sm" src="${site_logo}" />
					</#if>
				</a>
			</div>
		</div>

		<div class="minium-sidebar__middle">
			<@site_navigation_menu_main default_preferences=freeMarkerPortletPreferences.getPreferences("portletSetupPortletDecoratorId", "barebone") />
		</div>

		<div class="minium-sidebar__end">
			<div class="user-nav">
				<div class="user-nav__menu">
					<a class="is-active main-link main-link--sub" href="#">
						<div class="main-link__label">My Profile</div>
					</a>

					<a class="main-link main-link--sub" href="#">
						<div class="main-link__label">Wish List</div>
					</a>

					<a class="main-link main-link--sub" href="#">
						<div class="main-link__label">
							Notification
							<div class="notification-badge">6</div>
						</div>
					</a>

					<a class="main-link main-link--sub" href="#">
						<div class="main-link__label">Logout</div>
					</a>
				</div>

				<div class="user-nav__avatar has-notification">
					<img alt="" src="http://placehold.it/100">
				</div>

				<div class="user-nav__name">John Doe</div>
			</div>
		</div>
	</div>
</div>