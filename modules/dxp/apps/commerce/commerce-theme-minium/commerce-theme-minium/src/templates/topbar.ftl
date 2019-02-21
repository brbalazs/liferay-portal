<div class="commerce-topbar">
	<div class="commerce-topbar__start">
		<#if back_url?has_content>
			<a class="commerce-topbar__button" href="${back_url}">
				<svg class="commerce-icon">
					<use href="${themeDisplay.getPathThemeImages()}/commerce-icons.svg#back" />
				</svg>
				${languageUtil.get(locale, "back")}
			</a>
		</#if>
		<label class="commerce-topbar__button (is-active) js-toggle-search" for="commerce-search-input">
			<svg class="commerce-icon">
				<use href="${themeDisplay.getPathThemeImages()}/commerce-icons.svg#search" />
			</svg>
		</label>
	</div>

	<div class="commerce-topbar__middle">
		<#if show_top_menu>
			<@site_navigation_menu_sub_navigation default_preferences=freeMarkerPortletPreferences.getPreferences("portletSetupPortletDecoratorId", "barebone") />
		</#if>
	</div>

	<div class="commerce-topbar__end">
		<div class="commerce-topbar__account-selector-wrapper">
			<@liferay_commerce_ui["account-selector"] />
		</div>

		<div class="commerce-topbar__cart-wrapper">
			<@liferay_commerce_ui["mini-cart"] />
		</div>
	</div>

	<div class="commerce-topbar__search">
		<@liferay_commerce_ui["search-bar"] id="search-bar" />
	</div>
</div>