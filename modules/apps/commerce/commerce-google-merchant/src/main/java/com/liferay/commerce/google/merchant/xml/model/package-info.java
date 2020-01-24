@XmlSchema(
	elementFormDefault = XmlNsForm.QUALIFIED,
	xmlns = {
		@XmlNs(namespaceURI="http://base.google.com/ns/1.0", prefix="g"),
	}
)
package com.liferay.commerce.google.merchant.xml.model;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;