/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.oauth2.provider.scope;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation can be used to declare that a method on a JAX-RS resource
 * must only be executed if the incoming request is authorized for the scopes
 * given in the value of the annotation.<br /> <br /> When used on JAX-RS
 * resource class, all methods without the annotation will inherit the resource
 * class annotation.
 *
 * @author Carlos Sierra Andrés
 * @see    RequiresNoScope
 * @review
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresScope {

	/**
	 * @return whether the returned scopes in {@link RequiresScope#value()} all
	 *         need to be authorized or only one of them. Defaults to
	 *         <code>true</code>, which means all the specified scopes need to
	 *         be authorized.
	 * @review
	 */
	boolean allNeeded() default true;

	/**
	 * @return The list of scopes that the request needs to be authorized for in
	 *         order to execute this method
	 * @review
	 */
	String[] value();

}