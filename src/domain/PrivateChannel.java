package domain;

import java.util.ArrayList;
import java.util.List;

public class PrivateChannel extends Channel {

	private User channelOwner;
	
	public PrivateChannel(User channelOwner) {
		super(new ArrayList<User>());
		setChannelOwner(channelOwner);
	}
	
	public PrivateChannel(User channelOwner, List<User> members) {
		super(members);
		setChannelOwner(channelOwner);
	}

	public PrivateChannel(PrivateChannel privateChannel) {
		super(privateChannel.getMembers());
		setChannelOwner(privateChannel.getChannelOwner());
	}
	
	public User getChannelOwner() {
		return channelOwner;
	}

	public void setChannelOwner(User channelOwner) {
		this.channelOwner = channelOwner;
	}
	
}
