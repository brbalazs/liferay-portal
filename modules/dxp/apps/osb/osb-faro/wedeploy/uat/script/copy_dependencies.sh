#!/bin/sh

cp -r /wedeploy-container/resources/log4j $LIFERAY_HOME/osgi/log4j/
cp -r /wedeploy-container/resources/portal-log4j-ext.xml $LIFERAY_HOME/tomcat/webapps/ROOT/WEB-INF/classes/META-INF
cp -r /wedeploy-container/resources/system-ext.properties $LIFERAY_HOME/tomcat/webapps/ROOT/WEB-INF/classes/
cp -r /wedeploy-container/resources/urlrewrite.xml $LIFERAY_HOME/tomcat/webapps/ROOT/WEB-INF/