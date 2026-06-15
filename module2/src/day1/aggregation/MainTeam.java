package day1.aggregation;

public class MainTeam {
    public static void main(String[] args) {
        Team team1 = new Team("Team 1");
        team1.addPlayer(new Player("dsh", 24));
        team1.addPlayer(new Player("wdqvi", 25));
        team1.addPlayer(new Player("bsj", 26));
        team1.addPlayer(new Player("wqfu", 27));
        team1.addPlayer(new Player("nkwxs", 28));

        System.out.println(team1.getPlayers());
        team1.removePlayer(team1.getPlayers().get(2));
        System.out.println(team1.getPlayers()); //after removing
    }
}
