package skunk.domain;

public class PredictableDie
{
	private int[] theRolls;
	private int nextInt; //index
	private int lastRoll;
	
	//default constructor
	public PredictableDie()
	{
		
	}
	
	//Constructor
	public PredictableDie(int[] is)
	{
		this.theRolls = is;
		this.nextInt = 0;		
	}

	public void roll()
	{
		this.lastRoll = this.theRolls[ this.nextInt ];
		
		//increment
		this.nextInt++;
		
		//Wrap around if index value is greater than the length of the array.
		if( this.nextInt == this.theRolls.length )
		{
			this.nextInt = 0;
		}
	}

	public int getLastRoll()
	{
		return this.lastRoll;
	}
	
}