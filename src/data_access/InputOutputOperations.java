package data_access;

import domain.*;
import utilities.Date;
import utilities.DayOfTheWeek;
import utilities.UserType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InputOutputOperations {

	public List<User> fillUserList(String path) {

		List<User> users = new ArrayList<>();
		List<List<String>> rows = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader("userList.csv"))) {
			String line = br.readLine();
			List<String> columnNames = new ArrayList<>();
			for(String s: line.split(","))
				columnNames.add(s);
			rows.add(columnNames);

			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				UserType type;
				if(values[0].equals("Student"))
					type = UserType.STUDENT;
				else if(values[0].equals("Teaching"))
					type = UserType.TEACHING_ASSISTANT;
				else
					type = UserType.INSTRUCTOR;
				String name = values[1];
				User user;
				if(values.length<3 || values[2].equals(""))
					user = new User(type,name);
				else
					user = new User(type,name,Integer.parseInt(values[2]));
				if(values.length>4 && !values[4].equals(""))
					user.setPassword(values[4]);
				users.add(user);
				List<String> userString = new ArrayList<>();
				userString.add(user.getUserType().toString());
				userString.add(user.getName());
				userString.add(String.valueOf(user.getUserID()));
				userString.add(user.getEmail());
				userString.add(user.getPassword());
				for(int i =5;i<values.length;i++)
					userString.add(values[i]);
				rows.add(userString);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileWriter csvWriter = null;
		try {
			csvWriter = new FileWriter("new.csv");
			for (List<String> rowData : rows) {
				csvWriter.append(String.join(",", rowData));
				csvWriter.append("\n");
			}
			csvWriter.flush();
			csvWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return users;
	}

	public List<Team> fillTeamList(String path) {
		List<Team> teams = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader("teamList.csv"))) {
			String line;
			br.readLine();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");

				String name = values[0];
				String teamId = values[1];

				String channelName = values[2];
				String date = values[3];
				String[] splitStr = date.split("\\s+");
				Date meetingDate = stringToDate(splitStr);
				Channel channel = new MeetingChannel(channelName,meetingDate);

				Channel channel1 = null;
				if(values.length>4){
					String channel2Name = values[4];
					Date date1;
					if(!values[5].equals("")){
						date1 = stringToDate(values[3].split("\\s+"));
						((MeetingChannel)channel1).setMeetingDate(date1);
					}
					channel1 = new MeetingChannel(channel2Name);
					if(!values[6].equals((""))){
						String[] splitStr2 = values[6].split(";");
						List<String> ids = new ArrayList<>();
						for(String s: splitStr2){
							s = s.replace(String.valueOf('"'), "");
							s = s.replace(" ", "");
							ids.add(s);
						}
					}
				}
				List<User> owners = new ArrayList<>();
				List<User> members = new ArrayList<>();
				List<Channel> channels = new ArrayList<>();
				if(channel1!=null)
					channels.add(channel1);
				Team team = new Team(name,teamId,owners,channel,channels,members);
				teams.add(team);
				for(String s: values){
					System.out.print(s + "| ");
				}
				System.out.println();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return teams;
	}

	public void matchUsersAndTeams(List<User> users, List<Team> teams, IMediator mediator){

		try (BufferedReader br = new BufferedReader(new FileReader("new.csv"))) {
			String line;
			br.readLine();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				int userId = Integer.parseInt(values[2]);
				User user = mediator.findUserById(userId);

				if(values.length>5){
					for(int i=5;i<values.length;i++){
						Team team = mediator.getTeam(values[i]);
						if(user.getUserType().equals(UserType.INSTRUCTOR) || user.getUserType().equals(UserType.TEACHING_ASSISTANT)){
							mediator.addMemberToTeam(team.getTeamID(),user);
							mediator.elevateMemberToTeamOwnerOfTeam(team.getTeamID(),user.getUserID());
							mediator.addMemberToChannelOfTeam(team.getTeamID(),team.getDefaultMeetingChannel().getChannelID(),user.getUserID());
						}else{
							mediator.addMemberToTeam(team.getTeamID(),user);
							mediator.addMemberToChannelOfTeam(team.getTeamID(),team.getDefaultMeetingChannel().getChannelID(),user.getUserID());
						}
					}
				}
			}
			List<ParticipantChannel> participantsAndChannels = getParticipantAndChannelFromTeamList("sf");
			for(ParticipantChannel participantChannel:participantsAndChannels){
				Channel channel = mediator.findChannelByTeamIdAndChannelName(participantChannel.getTeamId(),participantChannel.getChannelName());
				if(channel!=null)
					mediator.addMemberToChannelOfTeam(participantChannel.getTeamId(),channel.getChannelID(),participantChannel.getParticipantId());
			}
			System.out.println("");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Date stringToDate(String[] s){
		String day = s[0];
		String time = s[1];
		DayOfTheWeek dayOfTheWeek;
		if(day.equals("Monday"))
			dayOfTheWeek = DayOfTheWeek.MONDAY;
		else if(day.equals("Tuesday"))
			dayOfTheWeek = DayOfTheWeek.TUESDAY;
		else if(day.equals("Wednesday"))
			dayOfTheWeek = DayOfTheWeek.WEDNESDAY;
		else if(day.equals("Thursday"))
			dayOfTheWeek = DayOfTheWeek.THURSDAY;
		else if(day.equals("Friday"))
			dayOfTheWeek = DayOfTheWeek.FRIDAY;
		else if(day.equals("Saturday"))
			dayOfTheWeek = DayOfTheWeek.SATURDAY;
		else
			dayOfTheWeek = DayOfTheWeek.SUNDAY;

		String[] splitStr = s[1].split(":");
		int hour = Integer.parseInt(splitStr[0]);
		int minute = Integer.parseInt(splitStr[1]);

		Date date = new Date(dayOfTheWeek,hour,minute);
		return date;
	}

	private List<ParticipantChannel> getParticipantAndChannelFromTeamList(String path){

		List<ParticipantChannel> participantChannels = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader("teamList.csv"))) {
			String line;
			br.readLine();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				Channel channel1 = null;
				String teamId = values[1];
				if(values.length>4){
					String channelName = values[4];
					channel1 = new MeetingChannel(channelName);
					Date date1;
					if(!values[5].equals("")){
						date1 = stringToDate(values[3].split("\\s+"));
						((MeetingChannel)channel1).setMeetingDate(date1);
					}
					if(!values[6].equals((""))){
						String[] splitStr2 = values[6].split(";");
						List<String> ids = new ArrayList<>();
						for(String s: splitStr2){
							s = s.replace(String.valueOf('"'), "");
							s = s.replace(" ", "");
							ids.add(s);
						}
						for(String id: ids){
							ParticipantChannel participantChannel = new ParticipantChannel();
							participantChannel.setTeamId(teamId);
							participantChannel.setChannelName(channelName);
							participantChannel.setParticipantId(Integer.parseInt(id));
							participantChannels.add(participantChannel);
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return participantChannels;


//		for(ParticipantChannel participantChannel:participantChannels){
//			iMediator.addMemberToChannelOfTeam(participantChannel.teamId,participantChannel.channelId,participantChannel.participantId);
//		}
	}

	private class ParticipantChannel{

		private String channelName;
		private String teamId;
		private int participantId;

		public String getChannelName() {
			return channelName;
		}

		public void setChannelName(String channelName) {
			this.channelName = channelName;
		}

		public String getTeamId() {
			return teamId;
		}

		public void setTeamId(String teamId) {
			this.teamId = teamId;
		}

		public int getParticipantId() {
			return participantId;
		}

		public void setParticipantId(int participantId) {
			this.participantId = participantId;
		}
	}

	public void outputUserCsv(IMediator mediator){
		List<List<String>> rows = new ArrayList<>();
		List<String> columnNames = Arrays.asList(new String[]{"User Type","User Name","User ID","Email","Password","Team ID"});
		rows.add(columnNames);
		List<User> users = mediator.getUsers();
		for(User user: users){
			List<String> rowData = new ArrayList<>();
			rowData.add(String.valueOf(user.getUserType()));
			rowData.add(user.getName());
			rowData.add(String.valueOf(user.getUserID()));
			rowData.add(user.getEmail());
			rowData.add(user.getPassword());
			List<Team> teams = mediator.getAllTeamsForUser(user.getUserID());
			String teamsString = "";
			for(Team team: teams)
				teamsString+=team.getTeamID();
			rowData.add(teamsString);
			rows.add(rowData);
		}

		FileWriter csvWriter = null;
		try {
			csvWriter = new FileWriter("usersNew.csv");
			for (List<String> rowData : rows) {
				csvWriter.append(String.join(",", rowData));
				csvWriter.append("\n");
			}
			csvWriter.flush();
			csvWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void outputTeamsCsv(IMediator mediator){
		List<List<String>> rows = new ArrayList<>();
		List<String> columnNames = Arrays.asList(new String[]{"Team Name","Team ID","Default Channel","Default Meeting Day and Time","Meeting Channel","Meeting Day and Time","Participant ID"});
		rows.add(columnNames);
		List<Team> teams = mediator.getTeams();
		for(Team team: teams){
			List<String> rowData = new ArrayList<>();
			rowData.add(team.getTeamName());
			rowData.add(team.getTeamID());
			rowData.add(team.getDefaultMeetingChannel().getName());
			rowData.add(team.getDefaultMeetingChannel().getMeetingDate().toString());
			rowData.add()
		}

		FileWriter csvWriter = null;
		try {
			csvWriter = new FileWriter("usersNew.csv");
			for (List<String> rowData : rows) {
				csvWriter.append(String.join(",", rowData));
				csvWriter.append("\n");
			}
			csvWriter.flush();
			csvWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
