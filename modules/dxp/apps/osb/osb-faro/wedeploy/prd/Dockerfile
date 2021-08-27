FROM wedeploy/dxpcloud-liferay-dxp:7.1.0-ga1-fp11-2.4.0

RUN mkdir -p $LIFERAY_HOME/tomcat/lib/org/apache/catalina/util && \
    echo "server.info=" > $LIFERAY_HOME/tomcat/lib/org/apache/catalina/util/ServerInfo.properties

ADD . /wedeploy-container

RUN rm \
	"$LIFERAY_HOME/osgi/marketplace/Liferay Plugin for OAuth 2.0 - API.lpkg" \
	"$LIFERAY_HOME/osgi/marketplace/Liferay Plugin for OAuth 2.0 - Impl.lpkg"