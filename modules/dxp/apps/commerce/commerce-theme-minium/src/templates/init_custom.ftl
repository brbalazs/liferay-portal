<#assign
	copyright = getterUtil.getString(themeDisplay.getThemeSetting("copyright"))
	show_top_menu = getterUtil.getBoolean(themeDisplay.getThemeSetting("show-top-menu"))
/>

<#macro site_navigation_menu_main default_preferences = "">
	<@liferay_portlet["runtime"]
		defaultPreferences=default_preferences
		instanceId="siteNavigationMenuPortlet_main"
		portletName="com_liferay_site_navigation_menu_web_portlet_SiteNavigationMenuPortlet"
	/>
</#macro>

<#macro site_navigation_menu_sub_navigations
	default_preferences = ""
	instance_id="siteNavigationMenuPortlet_sub_navigations">

	<@liferay_portlet["runtime"]
		defaultPreferences=default_preferences
		instanceId=instance_id
		portletName="com_liferay_site_navigation_menu_web_portlet_SiteNavigationMenuPortlet"
	/>
</#macro>