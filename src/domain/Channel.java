package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Channel {

	private String name;
	private List<User> members;
	private int channelID;
	private static int channelIDCounter = 0;
	
	public Channel(String name) {
		setName(name);
		setMembers(new ArrayList<User>());
		setChannelID(channelIDCounter);
	}
	
	public Channel(List<User> members) {
		setMembers(members);
	}
	
	public Channel(Channel channel) {
		setMembers(channel.getMembers());
	}

	public List<User> getMembers() {
		return members;
	}

	public void setMembers(List<User> members) {
		this.members = members;
	}

	public int getChannelID() {
		return channelID;
	}

	public void setChannelID(int channelID) {
		channelIDCounter++;
		this.channelID = channelID;
	}
	
	public void addMembers(List<User> membersToAdd) {
		List<User> newList = Stream.concat(getMembers().stream(), membersToAdd.stream()).collect(Collectors.toList());
		setMembers(newList);
	}
	
	public void removeMembers(List<User> membersToRemove) {
		List<User> newList = getMembers().stream().filter(e -> !membersToRemove.contains(e)).collect (Collectors.toList());
		setMembers(newList);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
