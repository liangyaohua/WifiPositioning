import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class ScoreNN {

	private Scanner sc = null;
	private FileOutputStream fos;
	private PrintStream pw;

	public static void main(String[] args) 
	{
		ScoreNN scoreReader = new ScoreNN(args[0]);
		scoreReader.scoreIt();
	}

	public ScoreNN(String inFileName)
	{

		try {
			sc = new Scanner(new File(inFileName));
			fos = new FileOutputStream("scored_" + inFileName);
			pw = new PrintStream(fos);
			System.setOut(pw);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void scoreIt()
	{
		int[] buckets = new int[25];
		while(sc.hasNext())
		{
			String nextLine = sc.nextLine();
			String[] trueEsti = nextLine.split(";");
			double x1, x2, y1, y2, distance;

			x1 = Double.parseDouble(trueEsti[0].split(",")[0]);
			y1 = Double.parseDouble(trueEsti[0].split(",")[1]);
			x2 = Double.parseDouble(trueEsti[1].split(",")[0]);
			y2 = Double.parseDouble(trueEsti[1].split(",")[1]);

			distance = Misc.getEuclidianDistance(x1,x2,y1,y2,0,0);

			for(int i = 0; i < buckets.length; i++)
			{
				if(i <= distance && distance < (double)(i + 0.5))
				{
					buckets[i]++;
				}

				if((double)(i + 0.5) <= distance && distance < i + 1)
				{
					buckets[i]++;
				}
			}

		}

		for(int i = 0; i < buckets.length; i++)
		{
			System.out.println((double)(i + 1) / 2 + " " + buckets[i]);
		}
	}
}


