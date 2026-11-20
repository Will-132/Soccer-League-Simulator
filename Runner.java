import java.util.*;

public class Runner
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("1) Full league simulation");
        System.out.println("2) Head-to-head matchup");
        System.out.print("Choose mode: ");
        String input = scanner.nextLine().trim();
        
        while (!input.equals("1") && !input.equals("2"))
        {
            System.out.println("Error, please type either \"1\" or \"2\"");
            input = scanner.nextLine().trim();
        }

        if (input.equals("1"))
            {
                LeagueSimulator sim = LeagueSimulator.getTeamsFromUser(scanner);
                sim.runMonteCarlo(LeagueSimulator.getNumRuns(scanner));
            }
        else
        {
            LeagueSimulator.runMatchupMode(scanner);
        }
        
        
    }
}
