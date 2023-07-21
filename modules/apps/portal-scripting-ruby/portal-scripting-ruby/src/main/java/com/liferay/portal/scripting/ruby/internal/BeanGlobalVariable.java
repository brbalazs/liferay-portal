/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.scripting.ruby.internal;

import org.jruby.Ruby;
import org.jruby.javasupport.Java;
import org.jruby.javasupport.JavaObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.runtime.IAccessor;
import org.jruby.runtime.builtin.IRubyObject;

/**
 * @author Alberto Montero
 */
public class BeanGlobalVariable implements IAccessor {

	public BeanGlobalVariable(Ruby ruby, Object bean, Class<?> type) {
		_ruby = ruby;
		_type = type;

		_bean = JavaUtil.convertJavaToRuby(_ruby, bean, _type);

		if (_bean instanceof JavaObject) {
			_bean = Java.wrap(_ruby, _bean);
		}
	}

	@Override
	public IRubyObject getValue() {
		return _bean;
	}

	@Override
	public IRubyObject setValue(IRubyObject bean) {
		_bean = bean;

		return bean;
	}

	private IRubyObject _bean;
	private final Ruby _ruby;
	private final Class<?> _type;

}