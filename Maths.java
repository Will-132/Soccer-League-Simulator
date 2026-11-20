public class Maths
{
   
   public static double odds(double elo1, double elo2)
   {
      return   1.0/(1.0 + Math.pow(10, (elo2-elo1)/25.0));
   }

   public static double[] lambdas(double eloA, double eloB)
   {
      double gap = eloA - eloB;
      return new double[] { 1.35 + gap / 30.0, 1.35 - gap / 30.0 };
   }


   public static int poissonGoals(double lambda)
   {
      double[] probability = new double[6];
      double total = 0;
   
      for (int i = 0; i < 6; i++)  // For 0-5 goals
      {
         probability[i] = Math.pow(lambda, i) * Math.exp(-lambda) / factorial(i);
         total += probability[i];
      }
   
      double roll = Math.random() * total;   // Gives odds and accounts for cutoff of goals
      for (int j = 0; j < 6; j++)
      {
         roll -= probability[j];           
         if (roll < 0)
         {
            return j;
         }
      }
      return 5;
   }
   


   private static double factorial(int n)
   {
      double result = 1;
      for (int i = 1; i <= n; i++)
      {
         result *= i;
      }
         return result;
   }



   
}
