package view;

import java.util.List;
import java.util.Scanner;

import data_access.InputOutputOperations;
import domain.Channel;
import domain.IMediator;
import domain.Mediator;
import domain.Team;
import domain.User;
import utilities.Date;
import utilities.UnauthorizedUserOperationException;
import utilities.UserType;

public class TeamsTECH {
	//dilekozturk@iyte.edu.tr 1a2b
	public void start() {
		InputOutputOperations inputOutputOperations = new InputOutputOperations();
        List<User> users = inputOutputOperations.fillUserList("xxx");
        List<Team> teams = inputOutputOperations.fillTeamList("ccc");
		IMediator med = new Mediator(teams, users, inputOutputOperations);
		Scanner scanner = new Scanner(System.in);
		System.out.println("=========================================");
		System.out.println("=============== TeamsTECH ===============");
		System.out.println("=========================================\n");
		login(med, scanner);
		UserType userType = med.getCurrentUser().getUserType();
		mainMenu(med, scanner, userType);
	}
	
	private void login(IMediator med, Scanner scanner) {
		System.out.println("================= Login =================");
		System.out.print("Please enter your E-Mail adress: ");
		String email = scanner.next();
		System.out.print("Please enter your password: ");
		String password = scanner.next();
		User currentUser = med.getUserFromEmailAndPassword(email, password);
		System.out.println("Welcome " + currentUser.getName() + "!");
		System.out.println("What would you like to do?");
		med.setCurrentUser(currentUser);
		return;
	}
	
	private void mainMenu(IMediator med, Scanner scanner, UserType userType) {
		System.out.println();
		System.out.println("1. Print the teams you're a member of.");
		System.out.println("2. Open a team.");
		System.out.println("3. Join a team.");
		System.out.println("4. Leave a team.");
		if(userType == UserType.TEACHING_ASSISTANT) {
			System.out.println("5. Delete a team.");
		} else if(userType == UserType.INSTRUCTOR) {
			System.out.println("5. Delete a team.");
			System.out.println("6. Create a team.");
		}
		System.out.println("9. Log out.");
		System.out.print("Please enter a number: ");
		int choice = scanner.nextInt();
		System.out.println();
		switch(choice) {
			case 1:
				printTeams(med, scanner, userType);
				break;
			case 2:
				teamMenu(med, scanner, userType);
				break;
			case 3:
				joinTeamMenu(med, scanner, userType);
				break;
			case 4:
				leaveTeamMenu(med, scanner, userType);
				break;
			case 5:
				deleteTeamMenu(med, scanner, userType);
				break;
			case 6:
				createTeamMenu(med, scanner, userType);
				break;
			case 9:
				start();
				break;
			default:
				System.out.println("Please choose a valid option.");
				mainMenu(med, scanner, userType);
		}
	}
	
	private void leaveTeamMenu(IMediator med, Scanner scanner, UserType userType) {
		System.out.print("Please enter the ID of the team you want to leave: ");
		String teamToLeave = scanner.next();
		Team leftTeam = med.removeMemberFromTeam(teamToLeave, med.getCurrentUser().getUserID());
		System.out.println("Left team " + leftTeam.getTeamName() + ".");
		mainMenu(med, scanner, userType);
	}
	
	private void joinTeamMenu(IMediator med, Scanner scanner, UserType userType) {
		System.out.print("Please enter the ID of the team you want to join: ");
		String teamToJoin = scanner.next();
		Team joinedTeam = med.addMemberToTeam(teamToJoin, med.getCurrentUser());
		System.out.println("Joined team " + joinedTeam.getTeamName() + ".");
		mainMenu(med, scanner, userType);
	}
	
	private void printTeams(IMediator med, Scanner scanner, UserType userType) {
		List<Team> teams = med.getTeamsWithCurrentUser();
		System.out.println("Teams you're a member of:");
		for (Team team : teams) {
			System.out.println(team.getTeamID() + ": " + team.getTeamName());
		}
		mainMenu(med, scanner, userType);
	}
	
	private void deleteTeamMenu(IMediator med, Scanner scanner, UserType userType) {
		if(userType == UserType.STUDENT) {
			System.out.println("Please choose a valid option.");
			mainMenu(med, scanner, userType);
		}
		System.out.print("Please enter the ID of the team you want to delete: ");
		String teamToDelete = scanner.next();
		Team removedTeam;
		try {
			removedTeam = med.removeTeam(teamToDelete);
			System.out.println("Deleted team " + removedTeam.getTeamName() + ".");
		} catch (UnauthorizedUserOperationException e) {
			System.out.println("You don't have permission to do that. Please choose a different option.");
		}
		mainMenu(med, scanner, userType);
	}
	
	private void createTeamMenu(IMediator med, Scanner scanner, UserType userType) {
		if(userType == UserType.STUDENT || userType == UserType.TEACHING_ASSISTANT) {
			System.out.println("Please choose a valid option.");
			mainMenu(med, scanner, userType);
		}
		System.out.print("Please enter a team ID: ");
		String teamID = scanner.next();
		System.out.print("Please enter a team name: ");
		String teamName = scanner.next();
		try {
			Team createdTeam = med.createTeam(teamName, teamID);
			System.out.println("Created team " + createdTeam.getTeamName() + " with the ID " + createdTeam.getTeamID() + ".");
		} catch (UnauthorizedUserOperationException e) {
			System.out.println("You don't have permission to do that. Please choose a different option.");
		}
		mainMenu(med, scanner, userType);
	}
	
	private void printChannels(IMediator med, Scanner scanner, UserType userType, Team team) {
		List<Channel> channels = med.channelsContainingUserInTeam(med.getCurrentUser(), team);
		System.out.println("Channels you're a member of:");
		for (Channel channel : channels) {
			System.out.println(channel.getChannelID() + ": " + channel.getName());
		}
		teamMenu(med, scanner, userType);
	}
	
	private void printMembers(IMediator med, Scanner scanner, UserType userType, Team team) {
		List<User> members = team.getMembers();
		System.out.println("Members:");
		for (User user : members) {
			System.out.println(user.getUserID() + ": " + user.getUserName());
		}
		teamMenu(med, scanner, userType);
	}
	
	private void createChannelMenu(IMediator med, Scanner scanner, UserType userType, Team team, String type) {
		if(type == "private") {
			System.out.print("Please enter the name of the private channel you want to create: ");
			String privateChannelName = scanner.next();
			Channel createdPrivateChannel = med.addPrivateChannelToTeam(team, privateChannelName);
			System.out.println("Channel " + createdPrivateChannel.getName() + " created in team " + team.getTeamName() + ".");
		} else if(type == "meeting") {
			if(med.isUserOwnerOfTeam(med.getCurrentUser(), team)) {
				System.out.print("Please enter the name of the meeting channel you want to create: ");
				String meetingChannelName = scanner.next();
				System.out.print("Please enter the meeting date (e.g. Monday 13:30): ");
				String meetingDateString = scanner.next();
				Date meetingDate = new Date(meetingDateString);
				Channel createdMeetingChannel = med.addMeetingChannelToTeam(team, meetingChannelName, meetingDate);
				System.out.println("Channel " + createdMeetingChannel.getName() + " created in team " + team.getTeamName() + ".");
			} else {
				System.out.print("You don't have permission to create a meeting channel");
			}
		}
		teamMenu(med, scanner, userType);
	}
	
	private void deleteChannelMenu(IMediator med, Scanner scanner, UserType userType, Team team, String type) {
		if(type == "private") {
			System.out.print("Please enter the ID of the private channel you want to delete: ");
			int privateChannelId = scanner.nextInt();
			if(med.isUserOwnerOfChannel(med.getCurrentUser(), team, privateChannelId)) {
				Channel removedPrivateChannel = med.removePrivateChannelFromTeam(team, privateChannelId);
				System.out.println("Channel " + removedPrivateChannel.getName() + " removed from team " + team.getTeamName() + ".");
			} else {
				System.out.println("You are not the owner of this channel.");
			}
		} else if(type == "meeting") {
			if(med.isUserOwnerOfTeam(med.getCurrentUser(), team)) {
				System.out.print("Please enter the ID of the meeting channel you want to delete: ");
				int meetingChannelId = scanner.nextInt();
				Channel removedMeetingChannel = med.removeMeetingChannelFromTeam(team, meetingChannelId);
				System.out.println("Channel " + removedMeetingChannel.getName() + " removed from team " + team.getTeamName() + ".");
			} else {
				System.out.print("You don't have permission to delete a meeting channel");
			}
		}
		teamMenu(med, scanner, userType);
	}
	
	private void elevateUserMenu(IMediator med, Scanner scanner, UserType userType, Team team) {
		System.out.print("Enter the ID of the user you want to make team owner: ");
		int id = scanner.nextInt();
		if(med.isUserOwnerOfTeam(med.getCurrentUser(), team)) {
			med.elevateMemberToTeamOwnerOfTeam(team.getTeamID(), id);
		} else {
			System.out.println("You don't have permission to do that.");
		}
		teamMenu(med, scanner, userType);
	}
	
	private void teamMenu(IMediator med, Scanner scanner, UserType userType) {
		System.out.print("Please enter the ID of the team you want to open: ");
		String teamID = scanner.next();
		Team team = med.getTeam(teamID);
		if(team == null) {
			System.out.println("Incorrect ID.");
			teamMenu(med, scanner, userType);
		}
		if(!med.teamContainsUser(med.getCurrentUser(), team)) {
			System.out.println("You are not a member of this team.");
			teamMenu(med, scanner, userType);
		}
		System.out.println("\nWelcome to team " + team.getTeamName() + ".\n");
		
		System.out.println("What would you like to do?");
		System.out.println("1. Print all channels you're a member of.");
		System.out.println("2. Print all members of team.");
		System.out.println("3. Create a private channel.");
		System.out.println("4. Delete a private channel.");
		if(med.isUserOwnerOfTeam(med.getCurrentUser(), team)) {
			System.out.println("5. Create a meeting channel.");
			System.out.println("6. Delete a meeting channel.");
			System.out.println("7. Elevate a user to team owner status.");
		}
		System.out.println("8. Go back to the main menu.");
		System.out.println("9. Log out.");
		System.out.print("Please enter a number: ");
		int choice = scanner.nextInt();
		System.out.println();
		switch(choice) {
			case 1:
				printChannels(med, scanner, userType, team);
				break;
			case 2:
				printMembers(med, scanner, userType, team);
				break;
			case 3:
				createChannelMenu(med, scanner, userType, team, "private");
				break;
			case 4:
				deleteChannelMenu(med, scanner, userType, team, "private");
				break;
			case 5:
				createChannelMenu(med, scanner, userType, team, "meeting");
				break;
			case 6:
				deleteChannelMenu(med, scanner, userType, team, "meeting");
				break;
			case 7:
				elevateUserMenu(med, scanner, userType, team);
				break;
			case 8:
				mainMenu(med, scanner, userType);
				break;
			case 9:
				start();
				break;
			default:
				System.out.println("Please choose a valid option.");
				mainMenu(med, scanner, userType);
		}
	}
}
