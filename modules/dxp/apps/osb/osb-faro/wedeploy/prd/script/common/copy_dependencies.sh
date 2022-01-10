#!/bin/sh

cp -r /docker-init.d/resources/common/log4j $LIFERAY_HOME/osgi/log4j/
cp -r /docker-init.d/resources/common/portal-log4j-ext.xml $LIFERAY_HOME/tomcat/webapps/ROOT/WEB-INF/classes/META-INF
cp -r /docker-init.d/resources/common/system-ext.properties $LIFERAY_HOME/tomcat/webapps/ROOT/WEB-INF/classes/
cp -r /docker-init.d/resources/common/urlrewrite.xml $LIFERAY_HOME/tomcat/webapps/ROOT/WEB-INF/