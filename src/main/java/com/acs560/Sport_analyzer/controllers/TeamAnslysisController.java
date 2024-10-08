package com.acs560.Sport_analyzer.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acs560.Sport_analyzer.models.Team;
import com.acs560.Sport_analyzer.services.TeamAnalysisService;



@RestController
@RequestMapping("/api/v1/analysis")
public class TeamAnslysisController {

    @Autowired
    private TeamAnalysisService teamAnalysisService;

    @GetMapping("/mean/wins")
    public ResponseEntity<Double> getMeanWins() {
        double meanWins = teamAnalysisService.getMeanWins();
        return ResponseEntity.ok(meanWins);
    }

    @GetMapping("/median/wins")
    public ResponseEntity<Double> getMedianWins() {
        double medianWins = teamAnalysisService.getMedianWins();
        return ResponseEntity.ok(medianWins);
    }

    @GetMapping("/mode/wins")
    public ResponseEntity<Integer> getModeWins() {
        int modeWins = teamAnalysisService.getModeWins();
        return ResponseEntity.ok(modeWins);
    }

    @GetMapping("/average/wins/{league}")
    public ResponseEntity<Double> getAverageWinsByLeague(@PathVariable String league) {
        double averageWins = teamAnalysisService.getAverageWinsByLeague(league);
        return ResponseEntity.ok(averageWins);
    }

    @GetMapping("/average/wins/{league}/{year}")
    public ResponseEntity<Double> getAverageWinsByLeagueAndYear(@PathVariable String league, @PathVariable int year) {
        double averageWins = teamAnalysisService.getAverageWinsByLeagueAndYear(league, year);
        return ResponseEntity.ok(averageWins);
    }
    

    @PostMapping("/add")
    public ResponseEntity<Team> addTeam(@RequestBody Team team) {
        if (teamAnalysisService.addTeam(team)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(team);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Team> updateTeam(@RequestBody Team team) {
        if (teamAnalysisService.updateTeam(team)) {
            return ResponseEntity.ok(team);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<Void> deleteTeam(@PathVariable String name) {
        Team team = teamAnalysisService.getTeamByName(name);
        if (team == null) {
            return ResponseEntity.notFound().build();
        }
        if (teamAnalysisService.deleteTeam(team)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }
}
