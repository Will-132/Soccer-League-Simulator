import java.util.*;

public class LeagueSimulator
{
    
    public static LeagueSimulator getTeamsFromUser(Scanner scanner)
    {
         List<Team> teams = new ArrayList<>();

        
        
        System.out.println("How many teams are in your league?");
        int numTeams = scanner.nextInt();
        scanner.nextLine();
        System.out.println();

        System.out.println("How many times does each team play each other? (1 = single round-robin, 2 = home & away)");
        int matchesPerOpponent = scanner.nextInt();
        scanner.nextLine();

        System.out.println("How many teams make the playoffs? ");
        int playoffSpots = scanner.nextInt();
        scanner.nextLine();

        System.out.println("How many teams get relegated? ");
        int relegationSpots = scanner.nextInt();
        scanner.nextLine();
        System.out.println();
        

        for (int i = 1; i <= numTeams; i++)
        {
            System.out.println("Input team #" + i + "'s name: ");
            String name = scanner.nextLine().trim();
            

            System.out.println("Input " + name + "'s elo (0-100): ");
            double elo = scanner.nextDouble();
            scanner.nextLine();


            while (!(elo <= 100 && elo >= 0))
            {
                System.out.println("Invalid — enter " + name + "'s elo (0-100): ");
                elo = scanner.nextDouble();
                scanner.nextLine();
            }

            System.out.println();
            teams.add(new Team(name, elo));
            
            
            
        }
        return new LeagueSimulator(teams, matchesPerOpponent, playoffSpots, relegationSpots);
        
    }



    

    private List<Team> teams;
    private int matchesPerOpponent;
    private int playoffSpots;
    private int relegationSpots;

    public LeagueSimulator(List<Team> teams, int matchesPerOpponent, int playoffSpots, int relegationSpots)
    {
        this.teams = teams;
        this.matchesPerOpponent = matchesPerOpponent;
        this.playoffSpots = playoffSpots;
        this.relegationSpots = relegationSpots;
    }


    


    public void simulateSeason()
    {
        for (Team t : teams)
        {
            t.reset();
        }

        for (int round = 0; round < matchesPerOpponent; round++)
        {
            for (int i = 0; i < teams.size(); i++)
            {
                for (int j = i + 1; j < teams.size(); j++)
                {
                    Team teamA = teams.get(i);
                    Team teamB = teams.get(j);

                    double[] ls = Maths.lambdas(teamA.getCurrentElo(), teamB.getCurrentElo());


                    

                    int goalsA = Maths.poissonGoals(ls[0]);
                    int goalsB = Maths.poissonGoals(ls[1]);
               
                    teamA.match(goalsA, goalsB);
                    teamB.match(goalsB, goalsA);
                
                    double expectedA = Maths.odds(teamA.getCurrentElo(), teamB.getCurrentElo());
                    double expectedB = Maths.odds(teamB.getCurrentElo(), teamA.getCurrentElo());


                    double actualA = 0;
                    if (goalsA > goalsB)
                    {
                        actualA = 1.0;
                    }
                    if (goalsA == goalsB)
                    {
                        actualA = 0.5;
                    } 
                    if (goalsA < goalsB)
                    {
                        actualA = 0.0;
                    }

                    double actualB = 0;
                    if (goalsB > goalsA)
                    {
                        actualB = 1.0;
                    }
                    if (goalsB == goalsA)
                    {
                        actualB = 0.5;
                    } 
                    if (goalsB < goalsA)
                    {
                        actualB = 0.0;
                    }

                    teamA.updateElo((expectedA), actualA);
                    teamB.updateElo((expectedB), actualB);
                    
                }
            }
        }
        
        teams.sort(Comparator.comparingInt(Team::getPoints)
        .thenComparingInt(Team::getGoalDifference)
        .thenComparingInt(Team::getGoalsScored)
        .reversed());

    }

    public static int getNumRuns(Scanner scanner)
    {
        System.out.println("How many simulations? ");
        return scanner.nextInt();
    }

    public void runMonteCarlo(int numRuns)
    {
        for (int i = 0; i < numRuns; i++)
        {
            
            simulateSeason();
            
            // find the best team by: points, then GD, then goals scored
        Team champion = teams.get(0);
        for (Team t : teams)
        {
            if (t.getPoints() > champion.getPoints()) {
                champion = t;
            }
            else if (t.getPoints() == champion.getPoints()) // To account for tied points
            {
                if (t.getGoalDifference() > champion.getGoalDifference())  // Goal difference decides tiebreaker
                {
                    champion = t;
                }
                else if (t.getGoalDifference() == champion.getGoalDifference()
                        && t.getGoalsScored() > champion.getGoalsScored())
                {
                    champion = t;
                }
            }
        }
        champion.addChampionship();

        // top N make the playoffs
        for (int r = 0; r < playoffSpots; r++)
            {
                teams.get(r).addPlayoffAppearances();
            }
            
            // bottom N get relegated
            for (int r = teams.size() - relegationSpots; r < teams.size(); r++)
            {
                teams.get(r).addRelegations();
            }
    

            
        
        for (Team t : teams)          
        {                            
            t.addSeasonTotals();
        }
        }
        printStandings(numRuns);
    }

    public void printStandings(int numRuns)
    {
        System.out.println("\n" + "=".repeat(110));
        System.out.printf("%-24s | %-5s | %-7s | %-7s | %-6s | %-6s | %-9s | %-10s | %-11s%n",
        "Team", "Elo", "Avg Pts", "Avg GD", "GS", "GA", "Title %", "Playoff %", "Relegation %");

        System.out.println("-".repeat(110));
        
        teams.sort(Comparator.comparingDouble((Team t) -> t.getAveragePoints(numRuns))
        .thenComparingDouble(t -> t.getChampionships())
        .reversed());

        


        for (Team t : teams)
        {
            double champPct = t.getChampionships() * 100.0 / numRuns;
            double playoffPct = t.getPlayoffAppearances() * 100.0 / numRuns;
            double relegPct   = t.getRelegations() * 100.0 / numRuns;


            System.out.printf("%-24s | %5.1f | %7.1f | %+7.1f | %+6.2f | %6.2f | %9.2f%% | %10.2f%% | %9.2f%%%n",
            t.getName(), t.getInitialElo(),
            t.getAveragePoints(numRuns),
            t.getAverageGoalDifference(numRuns),
            t.getAverageGoalsScored(numRuns),      
            t.getAverageGoalsConceded(numRuns),   
            champPct, playoffPct, relegPct);
        }
    }

    public List<Team> getTeams()
    {
        return teams;
    }

   
    
    public static void run1v1(Team teamA, Team teamB, int numGames)
    {
        
        System.out.println(teamA.getName() + " vs " + teamB.getName());

        
        
        int winsA = 0;
        int winsB = 0;
        int draws = 0;
        int totalGoalsA = 0;
        int totalGoalsB = 0;

        
        for (int i = 0; i < numGames; i++)
        {
            double[] ls = Maths.lambdas(teamA.getInitialElo(), teamB.getInitialElo());
            int goalsA = Maths.poissonGoals(ls[0]);
            int goalsB = Maths.poissonGoals(ls[1]);
        
            totalGoalsA += goalsA;
            totalGoalsB += goalsB;

            if (goalsA > goalsB)
                winsA++;
            else if (goalsA == goalsB)
                draws++;
            else
                winsB++;
        }

        System.out.printf("%s wins: %.1f%% | draws: %.1f%% | %s wins: %.1f%%%n",
        teamA.getName(), winsA * 100.0 / numGames,
        draws * 100.0 / numGames,
        teamB.getName(), winsB * 100.0 / numGames);
        System.out.printf("Average score: %.2f - %.2f%n",
        totalGoalsA * 1.0 / numGames, totalGoalsB * 1.0 / numGames);     
    }

    public static void runMatchupMode(Scanner scanner)
    {
        System.out.println("\nFirst team's name: ");
        String nameA = scanner.nextLine().trim();

        System.out.println(nameA + "'s elo (0-100): ");
        double eloA = scanner.nextDouble();
        
        scanner.nextLine();
        while (!(eloA <= 100 && eloA >= 0))
        {
            System.out.println("Invalid — enter " + nameA + "'s elo (0-100): ");
            eloA = scanner.nextDouble();
            scanner.nextLine();
        }
        System.out.println();

        System.out.println("Second team's name: ");
        String nameB = scanner.nextLine().trim();

        System.out.println(nameB + "'s elo (0-100): ");
        double eloB = scanner.nextDouble();
        scanner.nextLine();
        while (!(eloB <= 100 && eloB >= 0))
        {
            System.out.println("Invalid — enter " + nameB + "'s elo (0-100): ");
            eloB = scanner.nextDouble();
            scanner.nextLine();
            }
        System.out.println();

        System.out.println("How many times should they play? ");
        int games = scanner.nextInt();
        System.out.println();

        Team teamA = new Team(nameA, eloA);
        Team teamB = new Team(nameB, eloB);

        run1v1(teamA, teamB, games);
    }


}
