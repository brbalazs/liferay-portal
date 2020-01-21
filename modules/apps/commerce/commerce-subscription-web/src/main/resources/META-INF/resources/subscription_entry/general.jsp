<div class="row">
    <div class="col-12 mb-4">

        <commerce-ui:panel
                elementClasses="flex-fill"
                title='<%= LanguageUtil.get(request, "reference-order")%>'
        >

            <div class="col-md-4">
                <!--
                    actionLabel = order ID link
                    actionTargetId ? nothing
                    actionURL = admin order detail
                    title
                -->
                <commerce-ui:info-box
                        actionLabel='<%= LanguageUtil.get(request, (billingAddress == null) ? "add" : "edit") %>'
                        actionTargetId=""
                        actionUrl="<%= editBillingAddressURL %>"
                        elementClasses="py-3"
                        title='<%= LanguageUtil.get(request, "order-id") %>'
                >
                    <!-- span class="text-muted">
                    <liferay-ui:message key="click-add-to-insert" />
                    </span -->
                </commerce-ui:info-box>
            </div>

            <div class="col-md-4">
                <!--
                    actionLabel = payment method link
                    actionTargetId ? nothing
                    actionURL = admin order -> payment methods detail
                    title
                -->
                <commerce-ui:info-box
                        actionLabel='<%= LanguageUtil.get(request, (billingAddress == null) ? "add" : "edit") %>'
                        actionTargetId=""
                        actionUrl="<%= editBillingAddressURL %>"
                        elementClasses="py-3"
                        title='<%= LanguageUtil.get(request, "payment-method") %>'
                >
                    <!-- span class="text-muted">
                    <liferay-ui:message key="click-add-to-insert" />
                    </span -->
                </commerce-ui:info-box>
            </div>

            <div class="col-md-4">
                <!--
                actionLabel = none
                actionTargetId ? nothing
                actionURL = none
                title
                -->
                <commerce-ui:info-box
                        actionLabel='<%= LanguageUtil.get(request, (billingAddress == null) ? "add" : "edit") %>'
                        actionTargetId=""
                        actionUrl="<%= editBillingAddressURL %>"
                        elementClasses="py-3"
                        title='<%= LanguageUtil.get(request, "payment-status") %>'
                >
                    <!-- span class="text-muted">
                    <liferay-ui:message key="click-add-to-insert" />
                    </span -->
                </commerce-ui:info-box>
            </div>

        </commerce-ui:panel>

    </div>

    <div class="col-12">
        <%-- collapsable --%>
        <commerce-ui:panel
                bodyClasses="p-0"
                title='<%= LanguageUtil.get(request, "payment-subscription") %>'
        >
            <!-- FORM 1 -->
            <div class="row">
                <div class="col-md-6">

                </div>
                <div class="col-md-6">

                </div>
            </div>

            <div class="row">
                <div class="col-md-6">

                </div>
                <div class="col-md-6">

                </div>
            </div>

        </commerce-ui:panel>
    </div>

    <div class="col-12">
        <%-- collapsable --%>
        <commerce-ui:panel
                bodyClasses="p-0"
                title='<%= LanguageUtil.get(request, "delivery-subscription") %>'
        >
            <!-- FORM 2 -->
            <div class="row">
                <div class="col-md-6">

                </div>
                <div class="col-md-6">

                </div>
            </div>

            <div class="row">
                <div class="col-md-6">

                </div>
                <div class="col-md-6">

                </div>
            </div>

        </commerce-ui:panel>
    </div>

    <div class="col-12">
        <commerce-ui:panel
                bodyClasses="p-0"
                title='<%= LanguageUtil.get(request, "items")%>'
        >
            <commerce-ui:dataset-display
                    contextParams="<%= contextParams %>"
                    dataProviderKey="<%= CommerceSubscriptionEntryClayTable.NAME %>"
                    id="<%= CommerceSubscriptionEntryClayTable.NAME %>"
                    itemsPerPage="<%= 10 %>"
                    namespace="<%= renderResponse.getNamespace() %>"
                    pageNumber="<%= 1 %>"
                    portletURL="<%= commerceOrderEditDisplayContext.getCommerceOrderItemsPortletURL() %>"
                    style="stacked"
            />
        </commerce-ui:panel>
    </div>
</div>