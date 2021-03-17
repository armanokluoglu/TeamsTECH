package view;

import java.util.List;
import java.util.Scanner;

import data_access.InputOutputOperations;
import domain.IMediator;
import domain.Mediator;
import domain.Team;
import domain.User;
import utilities.UserType;

public class TeamsTECH {

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
		if(userType == UserType.STUDENT) {
			studentMenu(med, scanner);
		} else if(userType == UserType.INSTRUCTOR) {
			instructorMenu(med, scanner);
		} else if(userType == UserType.TEACHING_ASSISTANT) {
			teachingAssistantMenu(med, scanner);
		} else {
			scanner.close();
			throw new IllegalStateException("User type is not valid.");
		}
	}
	
	public void login(IMediator med, Scanner scanner) {
		System.out.println("================= Login =================");
		System.out.print("Please enter your E-Mail adress: ");
		String email = scanner.next();
		System.out.print("Please enter your password: ");
		String password = scanner.next();
		User currentUser = med.getUserFromEmailAndPassword(email, password);
		System.out.println("Welcome " + currentUser.getName() + "!");
		System.out.println("What would you like to do?");
		med.setCurrentUser(currentUser);
		//dilekozturk@iyte.edu.tr 1a2b
	}
	
	public void studentMenu(IMediator med, Scanner scanner) {
		System.out.println("1. Print the teams you're a member of.");
		System.out.println("2. Open a team.");
		System.out.println("3. Join a team.");
		System.out.println("4. Leave a team.");
		System.out.println("5. Log out.");
		System.out.print("Please enter a number between 1-4: ");
		int choice = scanner.nextInt();
		switch(choice) {
			case 1:
				List<Team> teams = med.getTeamsWithCurrentUser();
				for (Team team : teams) {
					System.out.println(team.getTeamID() + ": " + team.getTeamName());
				}
				break;
			case 2:
				
				break;
			case 3:
				System.out.print("Please enter the ID of the team you want to join: ");
				String teamToJoin = scanner.next();
				Team joinedTeam = med.addMemberToTeam(teamToJoin, med.getCurrentUser());
				System.out.println("Joined team " + joinedTeam.getTeamName() + ".");
				break;
			case 4:
				System.out.print("Please enter the ID of the team you want to leave: ");
				String teamToLeave = scanner.next();
				Team leftTeam = med.removeMemberFromTeam(teamToLeave, med.getCurrentUser().getUserID());
				System.out.println("Left team " + leftTeam.getTeamName() + ".");
				break;
			case 5:
				start();
				break;
			default:
				start();
		}
	}
	
	public void instructorMenu(IMediator med, Scanner scanner) {
		
	}

	public void teachingAssistantMenu(IMediator med, Scanner scanner) {
	
	}
}
