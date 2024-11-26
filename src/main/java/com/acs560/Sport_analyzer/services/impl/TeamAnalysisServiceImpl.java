package com.acs560.Sport_analyzer.services.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acs560.Sport_analyzer.repositories.TeamRepository;
import com.acs560.Sport_analyzer.services.TeamAnalysisService;

import sports.acs560.performance_analyzer.models.Team;

@Service
public class TeamAnalysisServiceImpl implements TeamAnalysisService {

    @Autowired
    public TeamRepository teamRepository;

    @Override
    public double getMeanWins() {
        List<Team> teams = teamRepository.getTeams();
        return teams.stream()
                .mapToInt(Team::getWins)
                .average()
                .orElse(0);
    }

    @Override
    public double getMedianWins() {
        List<Team> teams = teamRepository.getTeams();
        List<Integer> wins = teams.stream()
                .map(Team::getWins)
                .sorted()
                .collect(Collectors.toList());

        int size = wins.size();
        if (size == 0) return 0;

        if (size % 2 == 0) {
            return (wins.get(size / 2 - 1) + wins.get(size / 2)) / 2.0;
        } else {
            return wins.get(size / 2);
        }
    }

    @Override
    public int getModeWins() {
        List<Team> teams = teamRepository.getTeams();
        Map<Integer, Long> frequencyMap = teams.stream()
                .collect(Collectors.groupingBy(Team::getWins, Collectors.counting()));

        return frequencyMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0);
    }

    @Override
    public double getAverageWinsByLeague(String league) {
        List<Team> teamsByLeague = teamRepository.getTeams().stream()
                .filter(team -> team.getLeague().equalsIgnoreCase(league))
                .collect(Collectors.toList());

        return teamsByLeague.stream()
                .mapToInt(Team::getWins)
                .average()
                .orElse(0);
    }

    @Override
    public double getAverageWinsByLeagueAndYear(String league, int year) {
        List<Team> teamsByLeagueAndYear = teamRepository.getTeams().stream()
                .filter(team -> team.getLeague().equalsIgnoreCase(league) && team.getYear() == year)
                .collect(Collectors.toList());

        return teamsByLeagueAndYear.stream()
                .mapToInt(Team::getWins)
                .average()
                .orElse(0);
    }
    
    public double getAverageWinsByYearRange(int startYear, int endYear) {
        // Filter teams by the specified year range
        List<Team> teamsByYearRange = teamRepository.getTeams().stream()
                .filter(team -> team.getYear() >= startYear && team.getYear() <= endYear)
                .collect(Collectors.toList());

        // Calculate and return the average number of wins for teams in that year range
        return teamsByYearRange.stream()
                .mapToInt(Team::getWins) // Get the wins for each team
                .average() // Calculate the average
                .orElse(0); // Return 0 if no teams are found in the year range
    }
    
    public List<Team> getTopTeamsByWins(int count) {
    	List<Team> teams = teamRepository.getTeams();
        System.out.println("Retrieved teams: " + teams);
        return teams.stream()
                .sorted(Comparator.comparingInt(Team::getWins).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }


    @Override
    public Team getTeamByName(String name) {
        return teamRepository.getTeams().stream()
                .filter(team -> team.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public void analyzeTeams() {
        System.out.println("Analyzing teams..");
        List<Team> teams = teamRepository.getTeams();
        System.out.println("Teams: " + teams.size());
        double meanWins = getMeanWins();
        System.out.println("Mean Wins: " + meanWins);

        double medianWins = getMedianWins();
        System.out.println("Median Wins: " + medianWins);

        int modeWins = getModeWins();
        System.out.println("Mode Wins: " + modeWins);

        String league = "Premier League";
        double averageWinsPremierLeague = getAverageWinsByLeague(league);
        System.out.println("Average Wins in " + league + ": " + averageWinsPremierLeague);

        int year = 2020;
        double averageWinsPremierLeague2020 = getAverageWinsByLeagueAndYear(league, year);
        System.out.println("Average Wins in " + league + " in " + year + ": " + averageWinsPremierLeague2020);
    }

	@Override
	public boolean addTeam(Team team) {
		// TODO Auto-generated method stub
		return teamRepository.addTeam(team);
	}

	@Override
	public boolean updateTeam(Team team) {
		Team existingTeam = getTeamByName(team.getName());
	    if (existingTeam != null) {
	        existingTeam.setWins(team.getWins());
	        existingTeam.setLosses(team.getLosses());
	        existingTeam.setWins(team.getWins());
	        existingTeam.setLeague(team.getLeague());
	        existingTeam.setYear(team.getYear());
	        return teamRepository.updateTeam(existingTeam);
	    } else {
	    	return teamRepository.addTeam(team);
	    }
	}

	@Override
	public boolean deleteTeam(Team team) {
		// TODO Auto-generated method stub
		Team existingTeam = getTeamByName(team.getName());
	    if (existingTeam != null) {
	        return teamRepository.deleteTeam(existingTeam);
	    }
		return false;
	}
	@Override
	public List<Team> getTeamsByYearRange(int startYear, int endYear) {
		return teamRepository.getTeams().stream()
	            .filter(team -> team.getYear() >= startYear && team.getYear() <= endYear)
	            .collect(Collectors.toList());
	}
}




