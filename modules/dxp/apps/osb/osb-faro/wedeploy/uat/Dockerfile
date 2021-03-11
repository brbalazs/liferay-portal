FROM wedeploy/dxpcloud-liferay-dxp:7.1.0-ga1-fp3-1.2

RUN mkdir -p $LIFERAY_HOME/tomcat/lib/org/apache/catalina/util && \
    echo "server.info=" > $LIFERAY_HOME/tomcat/lib/org/apache/catalina/util/ServerInfo.properties

# Bundle app source

ADD . /wedeploy-container