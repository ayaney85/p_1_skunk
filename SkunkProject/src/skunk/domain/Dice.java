package skunk.domain;
import edu.princeton.cs.introcs.StdOut;

/**
 * Dice represents a single pair of rollable Die objects, randomly generating
 * sums of their two values
 * 
 * This is a Javadoc comment: add more to your finished class below
 * 
 * @author eric
 *
 */

//**********************************************************

public class Dice
{
	public static final int NUM_TRIALS = 360;
	
	
	// Instance fields (variables) may be declared anywhere in class body
	// Convention: put at top
	private int lastRoll;
	private Die die1;
	private Die die2;

	//**********************************************************
	// Constructors (object initializers) also can be declared anywhere
	// Convention: after instance fields/variables
	public Dice()
	{
		// initialize instance variables die1 and die2 by
		// creating a new instance of each

		this.die1 = new Die();
		this.die2 = new Die();
		this.roll();
	}

	
	// overloaded constructor
	//public Dice(Die die1, Die die2) 
	//{
	//	this.die1 = die1;
	//	this.die2 = die2;
	//}

	//**********************************************************
	// Instance methods can also be declared anywhere in body of class
	// One convention: after the constructors
	//**********************************************************
	
	public int getLastRoll()
	{
		return this.lastRoll;
	}

	//**********************************************************
	
	public void roll()
	{
		// Roll each of die1, die2, sum their last rolls,
		// then set Dice.lastRoll to this value
		this.die1.roll();
		this.die2.roll();
		this.lastRoll = this.die1.getLastRoll() + this.die2.getLastRoll();
	}

	//**********************************************************
	
	public Die getPointerDie1()
	{
		return this.die1;
	}
	
	public Die getPointerDie2()
	{
		return this.die2;
	}
	
	
	//**********************************************************
	// the following method converts the internals of
	// this Dice object, and returns a descriptive String:
	//
	// Roll of 7 => 4 + 3
	//
	//**********************************************************
	
	public String toString()
	{
		return "Dice with last roll: " + getLastRoll() + " => " + die1.getLastRoll() + " + " + die2.getLastRoll();
	}


	//**********************************************************
	// static methods can go anywhere - but at end is one convention
	//**********************************************************
	
	public static void main(String[] args)
	{
		// simulate repeated rolls of Dice, counting the many double skunks
		
		Dice dice = new Dice();
		int doubleSkunkCount = 0;

		for (int i = 0; i < NUM_TRIALS; i++)
		{
			dice.roll();
			StdOut.println(dice);

			if (dice.getLastRoll() == 2)
				doubleSkunkCount++;
		}

		StdOut.println("Actual count: " + doubleSkunkCount);
		StdOut.println("Expected count: " + (NUM_TRIALS / 36.0));
		
		if( args.length > 0)
			args[0] = "Dice class counting double skunk count in simulation.";
	}
}
