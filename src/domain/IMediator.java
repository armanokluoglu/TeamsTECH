package domain;

import java.util.List;

import data_access.InputOutputOperations;

public interface IMediator {
	public Team getTeam(String teamID);
	public void addTeam(Team team);
	public Team removeTeam(String teamID);
	public Team addMembersToPrivateChannelOfTeam(String teamID, int channelID, int[] memberIDs);
	public Team removeMembersFromPrivateChannelOfTeam(String teamID, int channelID, int[] memberIDs);
	public Team addMeetingChannelToTeam(String teamID, Channel newChannel);
	public Channel removeMeetingChannelFromTeam(String teamID, int channelID);
	public Team addMemberToChannelOfTeam(String teamID, int channelID, int userID);
	public User removeMemberFromChannelOfTeam(String teamID, int channelID, int userID);
	public Team addMemberToTeam(String teamID, User newMember);
	public Team removeMemberFromTeam(String teamID, int userID);
	public Team elevateMemberToTeamOwnerOfTeam(String teamID, int userID);
	public List<User> getAllStudentsInTeam(String teamID);
	public List<User> getAllInstructorsInTeam(String teamID);
	public List<User> getAllAssistantsInTeam(String teamID);
	public List<User> fillUserList(String path);

	public Team findTeamById(String id);
	public User findUserById(int id);
	public Channel findChannelByTeamIdAndChannelName(String teamId,String channelName);
	public List<Team> getAllTeamsForUser(int userId);
	public List<User> getUsers();
	public List<Team> getTeams();
	public User getCurrentUser();
	public void setCurrentUser(User currentUser);
	public InputOutputOperations getIO();
	public void setIO(InputOutputOperations io);
	public User getUserFromEmailAndPassword(String email, String password);
	
	public List<Team> getTeamsWithCurrentUser();
}
