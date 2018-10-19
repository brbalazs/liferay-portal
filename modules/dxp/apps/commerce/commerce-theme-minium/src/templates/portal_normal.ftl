<!DOCTYPE html>
<#include init />
<html class="${root_css_class}" dir="<@liferay.language key="lang.dir" />" lang="${w3c_language_id}">
<head>
	<title>${the_title} - ${company_name}</title>
	<meta content="initial-scale=1.0, width=device-width" name="viewport" />
	<@liferay_util["include"] page=top_head_include />
</head>
<body class="${css_class}">
	<@liferay_ui["quick-access"] contentId="#main-content" />
	<@liferay_util["include"] page=body_top_include />
	<@liferay.control_menu />


	<div id="minium" class="minium minium-frame (has-search)">
		<div class="minium-frame__sidebar">
			<div class="minium-sidebar">
				<div class="minium-sidebar__start">
					<div class="minium-logo">
						<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 129 37">
							<g fill="none" fill-rule="evenodd">
								<path class="minium-logo__type" d="M62.115 27.34c.07.305-.046.61-.28.61h-2.553c-.773 0-1.218-.54-1.218-1.242v-5.152c0-1.172-.796-1.687-1.757-1.687-.773 0-1.475.398-2.107 1.03v4.895a7.69 7.69 0 0 0 .187 1.546c.07.305-.047.609-.281.609h-2.553c-.773 0-1.218-.539-1.218-1.241v-5.153c0-1.171-.796-1.686-1.756-1.686-.773 0-1.5.398-2.109 1.03v6.581a.47.47 0 0 1-.468.468h-2.95c-.259 0-.446-.21-.446-.468V16.824c0-.258.187-.469.445-.469h2.202c.327.211.655.61.913.937.843-.585 1.92-1.077 3.185-1.077 1.476 0 2.88.585 3.794 1.663.937-.82 2.272-1.663 3.935-1.663 2.552 0 4.848 1.733 4.848 4.637v4.942a7.69 7.69 0 0 0 .187 1.546zm4.89-14.123c-1.101 0-2.015-.89-2.015-2.014 0-1.1.914-1.99 2.014-1.99 1.101 0 2.015.89 2.015 1.99a2.014 2.014 0 0 1-2.015 2.014zm1.475 3.138a.47.47 0 0 1 .468.469v10.633c0 .304-.187.491-.445.491h-2.201c-.773 0-1.218-.538-1.218-1.264v-9.86c0-.258.187-.469.468-.469h2.928zM84.025 27.34c.071.304-.046.609-.28.609h-2.553c-.773 0-1.218-.539-1.218-1.242v-5.152c0-1.148-.797-1.71-1.733-1.71-.773 0-1.5.328-2.132.89v6.745a.47.47 0 0 1-.469.468h-2.95c-.258 0-.446-.21-.446-.468V16.824c0-.258.188-.469.445-.469h2.202c.328.211.656.586.914.937.843-.609 1.92-1.077 3.185-1.077 2.553 0 4.848 1.71 4.848 4.637v4.942a7.69 7.69 0 0 0 .187 1.546zm5.03-14.123c-1.1 0-2.014-.89-2.014-2.014 0-1.1.913-1.99 2.014-1.99 1.101 0 2.014.89 2.014 1.99a2.014 2.014 0 0 1-2.014 2.014zm1.476 3.138a.47.47 0 0 1 .468.469v10.633c0 .304-.187.491-.445.491h-2.202c-.772 0-1.217-.538-1.217-1.264v-9.86c0-.258.187-.469.468-.469h2.928zm14.89 0c.257 0 .444.21.444.468V27.48c0 .257-.187.468-.445.468h-2.342a9.168 9.168 0 0 1-.773-.937c-.843.61-1.92 1.077-3.185 1.077-2.53 0-4.848-1.732-4.848-4.637v-4.988a7.69 7.69 0 0 0-.187-1.546c-.07-.304.047-.609.281-.609h2.553c.773 0 1.218.539 1.218 1.242v5.199c0 1.171.796 1.687 1.756 1.687.773 0 1.5-.399 2.108-1.031v-6.581a.47.47 0 0 1 .469-.469h2.95zm22.946 10.985c.07.305-.046.61-.28.61h-2.553c-.773 0-1.218-.54-1.218-1.242v-5.152c0-1.172-.797-1.687-1.757-1.687-.773 0-1.475.398-2.108 1.03v4.895a7.69 7.69 0 0 0 .188 1.546c.07.305-.047.609-.281.609h-2.553c-.773 0-1.218-.539-1.218-1.241v-5.153c0-1.171-.796-1.686-1.757-1.686-.772 0-1.499.398-2.108 1.03v6.581a.47.47 0 0 1-.468.468h-2.951c-.258 0-.445-.21-.445-.468V16.824c0-.258.187-.469.445-.469h2.201c.328.211.656.61.914.937.843-.585 1.92-1.077 3.185-1.077 1.476 0 2.88.585 3.794 1.663.937-.82 2.272-1.663 3.934-1.663 2.553 0 4.848 1.733 4.848 4.637v4.942a7.69 7.69 0 0 0 .188 1.546z" fill="#5C5E5E"/>
								<path class="minium-logo__mark" d="M16.37 31.265L5.31 24.878V15.17l9.733 5.62a2.654 2.654 0 0 0 2.655 0l9.733-5.62v9.708L16.37 31.265zm0-25.545l8.408 4.853-8.408 4.854-8.407-4.854L16.37 5.72zm16.369 4.816a3.555 3.555 0 0 0-.014-.238c-.004-.024-.005-.047-.008-.07a2.38 2.38 0 0 0-.1-.448l-.016-.057a2.722 2.722 0 0 0-.082-.21c-.008-.016-.016-.036-.026-.054a2.636 2.636 0 0 0-.238-.414c-.011-.016-.024-.032-.034-.048a2.594 2.594 0 0 0-.14-.175l-.041-.043c-.04-.041-.079-.084-.12-.123-.02-.017-.038-.037-.059-.055a2.978 2.978 0 0 0-.16-.134c-.018-.015-.039-.028-.057-.041a3.21 3.21 0 0 0-.2-.132l-.032-.02L17.698.357a2.653 2.653 0 0 0-2.655 0L1.328 8.275l-.03.018-.073.046a2.978 2.978 0 0 0-.125.084c-.02.016-.042.03-.061.045A2.887 2.887 0 0 0 .88 8.6l-.062.057c-.04.04-.079.08-.115.12l-.044.045a2.858 2.858 0 0 0-.138.174c-.012.017-.025.033-.036.05a2.626 2.626 0 0 0-.346.676l-.017.059a2.262 2.262 0 0 0-.045.161l-.02.08a2.428 2.428 0 0 0-.053.434L0 10.538 0 10.573v15.838c0 .948.507 1.824 1.328 2.299l13.715 7.919a2.654 2.654 0 0 0 2.655 0l13.714-7.92a2.655 2.655 0 0 0 1.329-2.298V10.573l-.002-.037z" fill="#B2C6EC"/>
							</g>
						</svg>
					</div>
				</div>
				<div class="minium-sidebar__middle">
					<div class="main-navigation">
						<a href="#" class="main-link is-active">
							<div class="main-link__icon">
								<svg class="minium-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
									<rect fill="currentColor" x="5" y="5" width="90" height="90" rx="10" ry="10" />
								</svg>
							</div>
							<div class="main-link__label">Dashboard</div>
						</a>
						<a href="#" class="main-link">
							<div class="main-link__icon">
								<svg class="minium-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
									<rect fill="currentColor" x="5" y="5" width="90" height="90" rx="10" ry="10" />
								</svg>
							</div>
							<div class="main-link__label">Products</div>
						</a>
						<a href="#" class="main-link">
							<div class="main-link__icon">
								<svg class="minium-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
									<rect fill="currentColor" x="5" y="5" width="90" height="90" rx="10" ry="10" />
								</svg>
							</div>
							<div class="main-link__label">Parts Catalog</div>
						</a>
						<a href="#" class="main-link">
							<div class="main-link__icon has-notification">
								<svg class="minium-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
									<rect fill="currentColor" x="5" y="5" width="90" height="90" rx="10" ry="10" />
								</svg>
							</div>
							<div class="main-link__label">Orders</div>
						</a>
						<div class="main-navigation__submenu is-open">
							<a href="#" class="main-link main-link--sub">
								<div class="main-link__label">Pending Orders</div>
							</a>
							<a href="#" class="main-link main-link--sub is-active">
								<div class="main-link__label">Placed Orders</div>
							</a>
							<a href="#" class="main-link main-link--sub">
								<div class="main-link__label">Back Orders</div>
							</a>
							<a href="#" class="main-link main-link--sub">
								<div class="main-link__label">Shipments</div>
							</a>
							<a href="#" class="main-link main-link--sub">
								<div class="main-link__label">
									Pending Actions
									<div class="notification-badge">6</div>
								</div>
							</a>
						</div>
						<a href="#" class="main-link">
							<div class="main-link__icon">
								<svg class="minium-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
									<rect fill="currentColor" x="5" y="5" width="90" height="90" rx="10" ry="10" />
								</svg>
							</div>
							<div class="main-link__label">Account Management</div>
						</a>
					</div>
				</div>
				<div class="minium-sidebar__end">
					<div class="user-nav">
						<div class="user-nav__menu">
							<a href="#" class="main-link main-link--sub is-active">
								<div class="main-link__label">My Profile</div>
							</a>
							<a href="#" class="main-link main-link--sub">
								<div class="main-link__label">Wish List</div>
							</a>
							<a href="#" class="main-link main-link--sub">
								<div class="main-link__label">
									Notification
									<div class="notification-badge">6</div>
								</div>
							</a>
							<a href="#" class="main-link main-link--sub">
								<div class="main-link__label">Logout</div>
							</a>
						</div>
						<div class="user-nav__avatar has-notification">
							<img src="http://placehold.it/100" alt="">
						</div>
						<div class="user-nav__name">John Doe</div>
					</div>
				</div>
			</div>
		</div>

		<div class="minium-frame__topbar">
			<div class="minium-topbar">
				<div class="minium-topbar__start">
					<a href="#" class="minium-topbar__button">
						<svg class="minium-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
							<rect fill="currentColor" x="5" y="5" width="90" height="90" rx="10" ry="10" />
						</svg>
						Back
					</a>
					<label class="minium-topbar__button (is-active)" for="minium-search-input">
						<svg class="minium-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
							<rect fill="currentColor" x="5" y="5" width="90" height="90" rx="10" ry="10" />
						</svg>
					</label>
				</div>
				<div class="minium-topbar__middle">
					<div class="minium-tabs">
						<a href="#" class="minium-tabs__tab is-active"><em>Pending</em> (32)</a>
						<a href="#" class="minium-tabs__tab"><em>Placed</em></a>
						<a href="#" class="minium-tabs__tab"><em>Back Order</em></a>
					</div>
				</div>
				<div class="minium-topbar__end">
					<div class="minium-dropdown (is-open)">
						<a href="#" class="minium-topbar__button">
							<div class="account-selector">
								<div class="account-selector__image"><img src="http://placehold.it/100" alt=""></div>
								<div class="account-selector__title">Select Account & Order</div>
								<div class="account-selector__info">No order selected</div>
							</div>
							<svg class="minium-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
								<rect fill="currentColor" x="5" y="5" width="90" height="90" rx="10" ry="10" />
							</svg>
						</a>
						<div class="minium-dropdown__curtain">
							<div class="account-switcher is-visible">
								<div class="account-switcher__section">
									<div class="minium-search">
										<div class="minium-search__input">
											<input type="text" placeholder="Search Acconts…">
										</div>
										<a href="#" class="minium-search__button">
											<svg class="minium-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
												<rect fill="currentColor" x="5" y="5" width="90" height="90" rx="10" ry="10" />
											</svg>
										</a>
									</div>
								</div>
								<div class="account-switcher__section account-switcher__section--fill">
									<div class="account-list">
										<div class="account-list__title">Select Account...</div>
										<a href="#" class="account-list__item u-hoverable">
											<img src="http://placehold.it/100" alt="">
											<span>Forward Auto Service</span>
										</a>
									</div>
								</div>
							</div>
							<div class="account-switcher (is-visible)">
								<a href="#" class="account-switcher__back">
									<svg class="minium-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
										<rect fill="currentColor" x="5" y="5" width="90" height="90" rx="10" ry="10" />
									</svg>
									Back
								</a>
								<div class="account-switcher__section">
									<div class="account-switcher__title">
										Forward Auto Service
									</div>
								</div>
								<div class="account-switcher__section">
									<div class="minium-search">
										<div class="minium-search__input">
											<input type="text" placeholder="Search Order…">
										</div>
										<a href="#" class="minium-search__button">
											<svg class="minium-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
												<rect fill="currentColor" x="5" y="5" width="90" height="90" rx="10" ry="10" />
											</svg>
										</a>
									</div>
								</div>
								<div class="account-switcher__section account-switcher__section--fill account-switcher__section--padded">
									<table class="small-table">
										<thead>
											<tr>
												<th>Select Order</th>
												<th class="u-tac">Status</th>
												<th class="u-tar">Last Modified</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td>FA987563</td>
												<td class="u-tac">
													<div class="minium-dot minium-dot--good"></div>
												</td>
												<td class="u-tar">09.01.18</td>
											</tr>
										</tbody>
									</table>
								</div>
								<div class="account-switcher__section">
									<a href="#" class="minium-button minium-button--block">Create new order</a>
								</div>
							</div>
						</div>
					</div>
					<a href="#" class="minium-topbar__button (is-disabled)">
						<svg class="minium-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
							<rect fill="currentColor" x="5" y="5" width="90" height="90" rx="10" ry="10" />
						</svg>
					</a>
					<div class="minium-cart (is-open)">
						<a href="#" class="minium-topbar__button minium-cart__close">
							<svg class="minium-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
								<rect fill="currentColor" x="5" y="5" width="90" height="90" rx="10" ry="10" />
							</svg>
						</a>
						<div class="minium-cart__top">
							<div><strong>149</strong> Products</div>
							<a href="#" class="minium-link">View details</a>
						</div>
						<div class="minium-cart__content">
							<div class="minium-cart__item minium-item u-hoverable">
								<img class="minium-item__image" src="http://placehold.it/100" alt="">
								<div class="minium-item__content">
									ASGJGGP<br>
									Lorem, ipsum dolor sit amet consectetur adipisicing elit. Necessitatibus, praesentium.<br>
									<strong>$ 560.00</strong>
								</div>
								<div class="minium-item__actions">
									<div class="quantity-selector">
										<a href="#" class="quantity-selector__btn">-</a>
										<input type="number" class="quantity-selector__input" :value="Math.round(Math.random() * 100)">
										<a href="#" class="quantity-selector__btn">+</a>
										<a href="#" class="quantity-selector__remove"></a>
									</div>
								</div>
							</div>
						</div>
						<div class="minium-cart__footer">
							<dl class="minium-cart__totals">
								<dt>Units</dt><dd>77 of 14 Items</dd>
								<dt>Subtotal</dt><dd>$ 32,000.00</dd>
								<dt>Discount</dt><dd>0% Off</dd>
								<dt>Grand Total</dt><dd><big>$ 32,000.00</big></dd>
							</dl>
							<a href="#" class="minium-button minium-button--block">Submit</a>
						</div>
					</div>
				</div>
				<div class="minium-topbar__search">
					<div class="minium-search">
						<div class="minium-search__input">
							<input type="text" placeholder="Search Product Name, SKU, Client…">
						</div>
						<a href="#" class="minium-search__button">
							<svg class="minium-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
								<rect fill="currentColor" x="5" y="5" width="90" height="90" rx="10" ry="10" />
							</svg>
						</a>
					</div>
				</div>
			</div>
		</div>

		<div class="minium-frame__content js-scroll-area">
			<a name="minium-top"></a>
			<div class="minium-content">
				<div style="height: 2000px; background: white; padding-top: 40px; text-align: center; border-radius: 8px; color: #9D9E9E; font-size: 11px; text-transform: uppercase; letter-spacing: 1.1px;">
					<#if selectable>
						<@liferay_util["include"] page=content_include />
					<#else>
						${portletDisplay.recycle()}
						${portletDisplay.setTitle(the_title)}

						<@liferay_theme["wrap-portlet"] page="portlet.ftl">
							<@liferay_util["include"] page=content_include />
						</@>
					</#if>
				</div>
			</div>
		</div>

		<div class="minium-frame__toolbar (is-visible)">
		</div>

		<div class="minium-frame__cta (is-visible)">
			<a href="#" class="minium-button minium-button--big minium-button--outline">
				Cancel
			</a>
			<a href="#" class="minium-button minium-button--big">
				Done
			</a>
		</div>

		<div class="minium-frame__dock (is-visible)">
			Products
			<a href="#" class="minium-button">Compare</a>
		</div>

		<div class="minium-frame__overlay (is-visible)">
			<#--  <minium-suggestions />  -->
		</div>

		<div class="minium-frame__tray">
		</div>

		<div class="minium-frame__modal (is-visible)">
			<div class="minium-modal">
				<a href="#" class="minium-modal__close">
					<svg class="minium-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
						<rect fill="currentColor" x="5" y="5" width="90" height="90" rx="10" ry="10" />
					</svg>
				</a>
				<div class="minium-modal__content">
					<div class="minium-modal__title">Modal title</div>
					<div style="color: #9D9E9E; font-size: 11px; text-transform: uppercase; letter-spacing: 1.1px;">
						👉 Modal content HERE! 👈
					</div>
				</div>
			</div>
		</div>
	</div>


	<@liferay_util["include"] page=body_bottom_include />
	<@liferay_util["include"] page=bottom_include />
	<!-- inject:js -->
	<!-- endinject -->
</body>
</html>
