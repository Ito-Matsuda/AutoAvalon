/*
 * CHANGELOG
 * -Got rid of isMerlin and isMordred do not need them
 * This is the generic things required for each player
 * 
 * Will need to create Mordred, Merlin, Percival, and Morgana classes
 * ^ I dont see why this is necessary just incorporate their knowledge into the suspects array
 */
public class genericPlayer {
	private String character; // Are they special? if not, will be "bad1" "good1"
	private int alliance; // 0 is good 1 is bad --> variable used mostly for the baddies
	private int[] suspects = new int[7];
	/*
	 * Suspects --> Each element in the array has a number (usually at 100)
	 * After the number hits zero (through pre-determined 'bad' moves) They will no longer elect for those dudes
	 * MAKE AN INTERFACE FOR CREATING SUSPECTS????	 
	 */
//	private int[] isMordred = new int[7]; // Same as suspects
//	private int[] isMerlin = new int[7]; // Same as suspects
	
	/**
	 * Only constructor
	 * @param character2 
	 * 	NA if generic, merlin if merlin etc
	 * @param alliance2
	 * 	0 is good, 1 is bad
	 * @param suspects2
	 * 	Above parameters help with "decisions"
	 * 	Each player has an array index associated with them, when their index hits
	 * 	 > 100 they are fully suspected
	 * 	However, if it is themselves, the number will be -100
	 */
	//public genericPlayer(String character2, int alliance2, int[] suspects2, int isMordred2[], int isMerlin2[]){
	public genericPlayer(String character2, int alliance2, int[] suspects2){
		setCharacter(character2);
		setAlliance(alliance2);
		setSuspects(suspects2);
//		setIsMordred(isMordred2);
//		setIsMerlin(isMerlin2);
	}
	
	// Begin the setters!
	/**
	 * Sets the character 'name'
	 * Does not need an exception because this is AUTOMATED
	 * Assumes everything that is generated is a-ok
	 * @param character2
	 */
	public void setCharacter(String character2){
		character = character2;
	}
	
	/**
	 * Sets the alliance for the individuals
	 * 0 is good, 1 is bad
	 * @param alliance2
	 */
	public void setAlliance(int alliance2){
		alliance = alliance2;
	}
	
	/**
	 * Details for the "suspects" array
	 * 
	 * Each player has a number assigned to them, that will be their spot in the array.
	 * When it comes to their own spot in their own array, it will be filled with a -1
	 * There should never be a -1 in any other case because we will subtract NOT by ones
	 * 
	 * Generic Good: All elements (except their own spot) filled with 100 for each misstep, -25
	 * 	Trusts Percival in the endgame always because he has a good chance at sniping Mordred
	 * Generic Bad: Other bad guys' index at -100 --> will always vote for pass unless it means losing
	 * 	OR if they know who Merlin is
	 *  
	 * 
	 * @param suspects2
	 */
	public void setSuspects(int[] suspects2){
		suspects = suspects2;
	}
	
	/**
	 * Changes the "confidence" amount that each person has for the other players
	 * 	Assumes that if they are losing confidence, then the passed in variable is negative (-25)
	 * @param index --> The person in question
	 * @param amountToChange --> The plus or minus amount of confidence 
	 */
	public void changeArray(int index, int amountToChange){
		suspects[index] = suspects[index] + amountToChange;
	}
//	/**
//	 * LOOK AT setSuspects
//	 * Same except 100 means that that player at that 
//	 * array index is Mordred 100 %
//	 * @param isMordred2
//	 */
//	public void setIsMordred(int[] isMordred2){
//		isMordred = isMordred2;
//	}
//	
//	/**
//	 * LOOK AT setSuspects
//	 * @param isMerlin2
//	 */
//	public void setIsMerlin(int[] isMerlin2){
//		isMerlin = isMerlin2;
//	}
	
	// END setters
	
	// BEGIN GETTERS (you just use these methods in your other classes)
	public String getCharacterN(){
		return character;
	}
	
	public int getAlliance(){
		return alliance;
	}
	
	public int[] getSuspects(){
		return suspects.clone();
	}
	
//	public int[] getIsMerlin(){
//		return isMerlin.clone();
//	}
//	public int[] getIsMordred(){
//		return isMordred.clone();
//	}
	
	// END GETTERS
	/**
	 * Returns a copy of the current object (is this what i want?)
	 */
	@Override
	public genericPlayer clone(){
		genericPlayer playCopy = null;
		// Should not need the "is..."
		//playCopy = new genericPlayer(character, alliance, suspects, isMerlin, isMordred);
		playCopy = new genericPlayer(character, alliance, suspects);
		return playCopy;
	}
	
	/**
	 * Purpose of this can be for error-checking (seeing if everything was set correctly)
	 */
	public void printCharacterAspects(){
		System.out.println("Character:" + getCharacterN() + "\nAllegiance:" + getAlliance());
		int whopped[] = getSuspects();
		for (int i = 0; i < whopped.length; i++ ){
			System.out.print(whopped[i] + ",");
		}
		System.out.print("\n"); // Make a newline
	} // End printCharacyerAspects
} // End genericPlayer.java