package day1.aggregation;

import java.util.ArrayList;
import  java.util.List;


public class Team {
    private String name;
    private List<Player> players;
    public Team(String name) {
        this.name = name;
        this.players = new ArrayList<>();
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Player> getPlayers() {
        return players;
    }
    public void addPlayer(Player player) {
        this.players.add(player);
    }
    public void removePlayer(Player player) {
        this.players.remove(player);
    }
}

