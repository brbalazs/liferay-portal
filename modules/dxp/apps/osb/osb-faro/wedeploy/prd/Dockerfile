FROM liferaycloud/liferay-dxp:7.1.10-ga1-fp18-3.0.29

RUN mkdir -p ${LIFERAY_HOME}/tomcat/lib/org/apache/catalina/util && \
    echo "server.info=" > ${LIFERAY_HOME}/tomcat/lib/org/apache/catalina/util/ServerInfo.properties

ADD . /docker-init.d

RUN rm \
	"${LIFERAY_HOME}/osgi/marketplace/Liferay Foundation - Liferay Plugin for OAuth 2.0 - API.lpkg" \
	"${LIFERAY_HOME}/osgi/marketplace/Liferay Foundation - Liferay Plugin for OAuth 2.0 - Impl.lpkg" \
	"${LIFERAY_HOME}/osgi/marketplace/Liferay Foundation - Liferay Plugin for OAuth 2.0 - SPI.lpkg"