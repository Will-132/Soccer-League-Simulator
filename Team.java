public class Team
{
    private String name;
    private final double initialElo;
    private double currentElo;

    // Variables for keeping track of games
    private int gamesPlayed = 0;
    private int goalsScored = 0;
    private int goalsConceded = 0;
    private int goalDifference = 0;
    private int wins = 0;
    private int ties = 0;
    private int losses = 0;
    private int points = 0;

    // For keeping track of seasons
    private int championships = 0;
    private int playoffAppearances = 0;
    private int relegations = 0;
    private double totalPoints = 0;
    private double totalGoalDifference = 0;
    private double totalGoalsScored = 0;
    private double totalGoalsConceded = 0;
    


    public void reset()
    {
        gamesPlayed = 0;
        goalsScored = 0;
        goalsConceded = 0;
        wins = 0;
        ties = 0;
        losses = 0;
        points = 0;
        goalDifference = 0;
        currentElo = initialElo;
    }


       
    public void match(int goalsScored, int goalsConceded)
    {
        gamesPlayed++;
        this.goalsScored += goalsScored;
        this.goalsConceded += goalsConceded;
        goalDifference += (goalsScored - goalsConceded);
        
        if (goalsScored > goalsConceded)
        {
            wins++;
            points += 3;
        }
        else if (goalsScored == goalsConceded)
        {
            ties++;
            points++;
        }
        else if (goalsScored < goalsConceded)
        {
            losses++;
        }

        

    }

    public Team(String name, double initialElo) 
    {
        this.name = name;
        this.initialElo = initialElo;
        currentElo = initialElo;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public double getInitialElo()
    {
        return initialElo;
    }

    

    public double getCurrentElo()
    {
        return currentElo;
    }

    public void setCurrentElo(double currentElo)
    {
        this.currentElo = currentElo;
    }



    public int getGamesPlayed()
    {
        return gamesPlayed;
    }

    public int getGoalsScored()
    {
        return goalsScored;
    }
    
    public int getGoalsConceded()
    {
        return goalsConceded;
    }

    public int getGoalDifference()
    {
        return goalDifference;
    }
    
    public int getWins()
    {
        return wins;
    }
    
    public int getTies()
    {
        return ties;
    }
    
    public int getLosses()
    {
        return losses;
    }

    public int getPoints()
    {
        return points;
    }

    public void updateElo(double odds, double result)
    {
        currentElo += 5.0 * (result - odds);
        if (currentElo < 0)
        {
            currentElo = 0;
        }
        if (currentElo > 100)
        {
            currentElo = 100;
        }
    }


    public void addChampionship()
    {
        championships++;
    }

    public int getChampionships()
    {
        return championships;
    }

    public void addPlayoffAppearances()
    {
        playoffAppearances++;
    }
    public int getPlayoffAppearances()
    {
        return playoffAppearances;
    }

    public void addRelegations()
    {
        relegations++;
    }
    public int getRelegations()
    {
        return relegations;
    }

    public void addSeasonTotals()     
    {
        totalPoints += points;
        totalGoalDifference += goalDifference;
        totalGoalsScored += goalsScored;    
        totalGoalsConceded += goalsConceded;
    }

    public double getAveragePoints(int numRuns)
    {
        return totalPoints * 1.0 / numRuns;
    }

    public double getAverageGoalDifference(int numRuns)
    {
        return totalGoalDifference * 1.0 / numRuns;
    }

    public double getAverageGoalsScored(int numRuns)
    {
        return totalGoalsScored * 1.0 / numRuns;
    }

    public double getAverageGoalsConceded(int numRuns)
    {
        return totalGoalsConceded * 1.0 / numRuns;
    }


}
