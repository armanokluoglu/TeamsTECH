package view;

import data_access.InputOutputOperations;
import domain.IMediator;
import domain.Mediator;
import domain.Team;
import domain.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeamsTechApp {

    public static void main(String[] args) {

    	TeamsTECH app = new TeamsTECH();
        app.start();

//        InputOutputOperations inputOutputOperations = new InputOutputOperations();
//        List<User> users = inputOutputOperations.fillUserList("xxx");
//        List<Team> teams = inputOutputOperations.fillTeamList("ccc");
//        IMediator mediator = new Mediator(teams,users,inputOutputOperations);
//
//        inputOutputOperations.fillTeamChannels(mediator,"vvvv");
//        inputOutputOperations.matchUsersAndTeams(users,teams,mediator);
//        inputOutputOperations.outputUserCsv(mediator);
//        inputOutputOperations.outputChannelsCsv(mediator);
    }


}
