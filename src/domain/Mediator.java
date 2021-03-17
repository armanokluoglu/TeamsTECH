package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import data_access.InputOutputOperations;
import utilities.UserType;

public class Mediator implements IMediator {

	private User currentUser;
	private List<Team> teams;
	private List<User> users;
	private InputOutputOperations io;

	public Mediator() {
		setTeams(new ArrayList<Team>());
	}

	public Mediator(List<Team> teams, List<User> users, InputOutputOperations io) {
		setTeams(teams);
		setUsers(users);
		setIO(io);
	}

	public Mediator(Mediator mediator) {
		setTeams(mediator.getTeams());
	}

	@Override
	public List<Team> getTeams() {
		return this.teams;
	}

	private void setTeams(List<Team> teams) {
		this.teams = teams;
	}

	@Override
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<User> getAllStudents() {
		List<User> allStudents = new ArrayList<User>();
		for (Team team : getTeams()) {
			List<User> allStudentsInTeam = getAllStudentsInTeam(team.getTeamID());
			allStudents = Stream.concat(allStudents.stream(), allStudentsInTeam.stream()).collect(Collectors.toList());
		}
		return allStudents;
	}

	public List<User> getAllInstructors() {
		List<User> allInstructors = new ArrayList<User>();
		for (Team team : getTeams()) {
			List<User> allInstructorsInTeam = getAllInstructorsInTeam(team.getTeamID());
			allInstructors = Stream.concat(allInstructors.stream(), allInstructorsInTeam.stream()).collect(Collectors.toList());
		}
		return allInstructors;
	}

	public List<User> getAllAssistants() {
		List<User> allAssistants = new ArrayList<User>();
		for (Team team : getTeams()) {
			List<User> allAssistantsInTeam = getAllAssistantsInTeam(team.getTeamID());
			allAssistants = Stream.concat(allAssistants.stream(), allAssistantsInTeam.stream()).collect(Collectors.toList());
		}
		return allAssistants;
	}

	@Override
	public Team getTeam(String teamID) {
		for (Team team : getTeams()) {
			if(team.getTeamID().equals(teamID)) {
				return team;
			}
		}
		return null;
	}

	@Override
	public void addTeam(Team team) {
		List<Team> teams = getTeams();
		teams.add(team);
	}

	@Override
	public Team removeTeam(String teamID) {
		Team teamToRemove = getTeam(teamID);
		List<Team> teams = getTeams();
		teams.remove(teamToRemove);
		return teamToRemove;
	}

	@Override
	public Team addMembersToPrivateChannelOfTeam(String teamID, int channelID, int[] memberIDs) {
		Team team = getTeam(teamID);
		team.addMembersToPrivateChannel(channelID, memberIDs);
		return team;
	}
	
	@Override
	public Team removeMembersFromPrivateChannelOfTeam(String teamID, int channelID, int[] memberIDs) {
		Team team = getTeam(teamID);
		team.removeMembersFromPrivateChannel(channelID, memberIDs);
		return team;
	}

	@Override
	public Team addMeetingChannelToTeam(String teamID, Channel newChannel) {
		Team team = getTeam(teamID);
		team.addMeetingChannel(newChannel);
		return team;
	}

	@Override
	public Channel removeMeetingChannelFromTeam(String teamID, int channelID) {
		Team team = getTeam(teamID);
		return team.removeMeetingChannel(channelID);
	}

	@Override
	public Team addMemberToChannelOfTeam(String teamID, int channelID, int userID) {
		Team team = getTeam(teamID);
		team.addMemberToChannel(channelID, userID);
		return team;
	}

	@Override
	public User removeMemberFromChannelOfTeam(String teamID, int channelID, int userID) {
		Team team = getTeam(teamID);
		return team.removeMemberFromChannel(channelID, userID);
	}

	@Override
	public Team addMemberToTeam(String teamID, User newMember) {
		Team team = getTeam(teamID);
		team.addMember(newMember);
		return team;
	}

	@Override
	public User removeMemberFromTeam(String teamID, int userID) {
		Team team = getTeam(teamID);
		team.removeMember(userID);
		return team;
	}

	@Override
	public Team elevateMemberToTeamOwnerOfTeam(String teamID, int userID) {
		Team team = getTeam(teamID);
		team.elevateMember(userID);
		return team;
	}

	@Override
	public List<User> getAllStudentsInTeam(String teamID) {
		return getAllUsersOfTypeInTeam(UserType.STUDENT, teamID);
	}

	@Override
	public List<User> getAllInstructorsInTeam(String teamID) {
		return getAllUsersOfTypeInTeam(UserType.INSTRUCTOR, teamID);
	}

	@Override
	public List<User> getAllAssistantsInTeam(String teamID) {
		return getAllUsersOfTypeInTeam(UserType.TEACHING_ASSISTANT, teamID);
	}
	
	private List<User> getAllUsersOfTypeInTeam(UserType type, String teamID) {
		Team team = getTeam(teamID);
		List<User> allUsersOfType = new ArrayList<User>();
		for (User user : team.getMembers()) {
			if(user.getUserType() == type) {
				allUsersOfType.add(user);
			}
		}
		return allUsersOfType;
	}

	@Override
	public List<User> fillUserList(String path) {
		return getIO().fillUserList(path);
	}

	@Override
	public Team findTeamById(String id) {
		for(Team team: teams){
			if(team.getTeamID().equals(id))
				return team;
		}
		return null;
	}

	@Override
	public User findUserById(int id) {
		for(User user: users){
			if(user.getUserID() == id)
				return user;
		}
		return null;
	}

	@Override
	public Channel findChannelByTeamIdAndChannelName(String teamId, String channelName) {
		Team team = findTeamById(teamId);
		if(team!=null)
			return team.getChannel(channelName);
		return null;
	}

	@Override
	public List<Team> getAllTeamsForUser(int userId){
		User user = findUserById(userId);
		List<Team> userTeams = new ArrayList<>();
		if(user!=null){
			for(Team team:teams){
				if(team.containsUser(user))
					userTeams.add(team);
			}
		}
		return teams;
	}
	
	@Override
	public User getUserFromEmailAndPassword(String email, String password) {
		List<User> users = getUsers();
		for (User user : users) {
			if(user.getEmail().equals(email)) {
				if(user.getPassword().equals(password)) {
					return user;
				}
			}
		}
		return null;
	}

	@Override
	public InputOutputOperations getIO() {
		return io;
	}
	
	@Override
	public void setIO(InputOutputOperations io) {
		this.io = io;
	}
	
	@Override
	public User getCurrentUser() {
		return currentUser;
	}
	
	@Override
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
	
	@Override
	public List<Team> getTeamsWithCurrentUser() {
		List<Team> teamsWithCurrentUser = new ArrayList<Team>();
		List<Team> teams = getTeams();
		for (Team team : teams) {
			if(team.containsUser(getCurrentUser())) {
				teamsWithCurrentUser.add(team);
			}
		}
		return teamsWithCurrentUser;
	}
}
