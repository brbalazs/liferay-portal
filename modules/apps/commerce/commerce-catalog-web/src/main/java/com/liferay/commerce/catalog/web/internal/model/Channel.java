package com.liferay.commerce.catalog.web.internal.model;

/**
 * @author Gianmarco Brunialti Masera
 */
public class Channel {

	public Channel(long channelId, String name, String type) {
		_channelId = channelId;
		_name = name;
		_type = type;
	}

	public long getChannelId() {
		return _channelId;
	}

	public String getName() {
		return _name;
	}

	public String getType() {
		return _type;
	}

	private final long _channelId;
	private final String _name;
	private final String _type;

}