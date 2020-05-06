FROM liferay/portal-snapshot:7.1.x-201811281240-5dc481e

USER root

ENV FARO_DEFAULT_WE_DEPLOY_KEY=asahc2bd8faaa9fe43aaaad2e3fe4662e8a1.us-west-1.lfr.cloud \
	FARO_DEMO_CREATOR_METHOD=snapshot \
	FARO_PROJECT_ID=FARO-DEV-liferay \
	JAVA_OPTS=-Djava.rmi.server.hostname="localhost -Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n" \
	OSB_API_TOKEN=lwn8C5iAlUi2bvaqSm \
	OSB_API_URL=https://web-uat.liferay.com \
	OSB_ASAH_BACKEND_URL=https://osbasahbackend-{weDeployKey} \
	OSB_ASAH_MONOLITH_URL=https://osbasahmonolith-{weDeployKey} \
	OSB_ASAH_PUBLISHER_URL=https://osbasahpublisher-{weDeployKey} \
	OSB_ASAH_TOKEN=9ZPgFSdEntvRPjgRcstSgkUCBzKyjz4hbuaGbdW9h3BHcJbZLuuSqd6BpFLhRErS

EXPOSE 8000 8080 11311

HEALTHCHECK --interval=3s --timeout=3s CMD curl -f http://localhost:8080 || exit 1

COPY common/resources/portal-ext.properties $LIFERAY_HOME/
COPY common/resources/setenv.sh $LIFERAY_HOME/tomcat/bin/
COPY common/resources/system-ext.properties $LIFERAY_HOME/tomcat/webapps/ROOT/WEB-INF/classes
COPY common/resources/urlrewrite.xml $LIFERAY_HOME/tomcat/webapps/ROOT/WEB-INF/

# Configs

COPY common/resources/configs $LIFERAY_HOME/osgi/configs/

# Log4j

COPY common/resources/log4j $LIFERAY_HOME/osgi/log4j/

# WAR

COPY *.war $LIFERAY_HOME/osgi/war/

# Modules

COPY *.jar $LIFERAY_HOME/osgi/modules/