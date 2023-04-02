package fi.fabianadrian.mmstatbridge.stat;

public enum Statistic {
    KILLS("kills"),
    DEATHS("deaths"),
    GAMES_PLAYED("gamesplayed"),
    WINS("wins"),
    LOSES("loses"),
    HIGHEST_SCORE("highestscore");

    private final String name;

    Statistic(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
