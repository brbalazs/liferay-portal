<div class="minium-frame__topbar">
	<div class="minium-topbar">
		<div class="minium-topbar__start">
			<#if back_url?has_content>
				<a class="minium-topbar__button" href="${back_url}">
					<svg class="minium-icon">
						<use href="${themeDisplay.getPathThemeImages()}/commerce-icons.svg#back" />
					</svg>
					${languageUtil.get(locale, "back")}
				</a>
			</#if>
			<label class="minium-topbar__button (is-active) js-toggle-search" for="minium-search-input">
				<svg class="minium-icon">
					<use href="${themeDisplay.getPathThemeImages()}/commerce-icons.svg#search" />
				</svg>
			</label>
		</div>

		<div class="minium-topbar__middle">
			<#if show_top_menu>
				<@site_navigation_menu_sub_navigation default_preferences=freeMarkerPortletPreferences.getPreferences("portletSetupPortletDecoratorId", "barebone") />
			</#if>
		</div>

		<div class="minium-topbar__end">
			<div class="minium-topbar__account-selector-wrapper">
				<@liferay_commerce_ui["account-selector"] />
			</div>

			<div class="minium-topbar__cart-wrapper">
				<@liferay_commerce_cart["mini-cart"] />
			</div>
		</div>

		<div class="minium-topbar__search">
			<@liferay_commerce_ui["search-bar"] id="search-bar" />
		</div>
	</div>
</div>