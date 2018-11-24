<div class="minium-frame__topbar">
	<div class="minium-topbar">
		<div class="minium-topbar__start">
			<a class="minium-topbar__button" href="#">
				<svg class="minium-icon" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
					<rect fill="currentColor" height="90" rx="10" ry="10" width="90" x="5" y="5" />
				</svg>
				Back
			</a>

			<label class="minium-topbar__button (is-active)" for="minium-search-input">
				<svg class="minium-icon" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
					<rect fill="currentColor" height="90" rx="10" ry="10" width="90" x="5" y="5" />
				</svg>
			</label>
		</div>

		<div class="minium-topbar__middle">
			<#if show_top_menu>
				<@site_navigation_menu_sub_navigations
					default_preferences=freeMarkerPortletPreferences.getPreferences("portletSetupPortletDecoratorId", "barebone")
					instance_id="siteNavigationMenuPortlet_sub_navigations" + layout.getUuid()
				/>
			</#if>
		</div>

		<div class="minium-topbar__end">
			<div class="minium-dropdown (is-open)">
				<a class="minium-topbar__button" href="#">
					<div class="account-selector">
						<div class="account-selector__image"><img src="http://placehold.it/100" alt=""></div>
						<div class="account-selector__title">Select Account & Order</div>
						<div class="account-selector__info">No order selected</div>
					</div>

					<svg class="minium-icon" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
						<rect fill="currentColor" height="90" rx="10" ry="10" width="90" x="5" y="5" />
					</svg>
				</a>

				<div class="minium-dropdown__curtain">
					<div class="account-switcher is-visible">
						<div class="account-switcher__section">
							<div class="minium-search">
								<div class="minium-search__input">
									<input placeholder="Search Acconts…" type="text">
								</div>

								<a class="minium-search__button" href="#">
									<svg class="minium-icon" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
										<rect fill="currentColor" height="90" rx="10" ry="10" width="90" x="5" y="5" />
									</svg>
								</a>
							</div>
						</div>

						<div class="account-switcher__section account-switcher__section--fill">
							<div class="account-list">
								<div class="account-list__title">Select Account...</div>
								<a class="account-list__item u-hoverable" href="#">
									<img alt="" src="http://placehold.it/100">
									<span>Forward Auto Service</span>
								</a>
							</div>
						</div>
					</div>

					<div class="account-switcher (is-visible)">
						<a class="account-switcher__back" href="#">
							<svg class="minium-icon" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
								<rect fill="currentColor" height="90" rx="10" ry="10" width="90" x="5" y="5" />
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
									<input placeholder="Search Order…" type="text">
								</div>

								<a class="minium-search__button" href="#">
									<svg class="minium-icon" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
										<rect fill="currentColor" height="90" rx="10" ry="10" width="90" x="5" y="5" />
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

			<a class="minium-topbar__button (is-disabled)" href="#">
				<svg class="minium-icon" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
					<rect fill="currentColor" height="90" rx="10" ry="10" width="90" x="5" y="5" />
				</svg>
			</a>

			<div class="minium-cart (is-open)">
				<a class="minium-topbar__button minium-cart__close" href="#">
					<svg class="minium-icon" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
						<rect fill="currentColor" height="90" rx="10" ry="10" width="90" x="5" y="5" />
					</svg>
				</a>

				<div class="minium-cart__top">
					<div><strong>149</strong> Products</div>
					<a href="#" class="minium-link">View details</a>
				</div>

				<div class="minium-cart__content">
					<div class="minium-cart__item minium-item u-hoverable">
						<img alt="" class="minium-item__image" src="http://placehold.it/100">
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
					<input placeholder="Search Product Name, SKU, Client…" type="text">
				</div>

				<a class="minium-search__button" href="#">
					<svg class="minium-icon" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
						<rect fill="currentColor" height="90" rx="10" ry="10" width="90" x="5" y="5" />
					</svg>
				</a>
			</div>
		</div>
	</div>
</div>