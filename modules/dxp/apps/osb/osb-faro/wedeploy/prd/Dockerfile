FROM wedeploy/dxpcloud-liferay-dxp:7.1.0-ga1-fp3-1.2

ADD . /wedeploy-container

RUN rm \
	"$LIFERAY_HOME/osgi/marketplace/Liferay Plugin for OAuth 2.0 - API.lpkg" \
	"$LIFERAY_HOME/osgi/marketplace/Liferay Plugin for OAuth 2.0 - Impl.lpkg"