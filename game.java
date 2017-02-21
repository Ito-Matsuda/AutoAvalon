//import java.util.Random;
import java.util.*;

/*
 * Rules to "Avalon" may be found here --> http://upload.snakesandlattes.com/rules/r/ResistanceAvalon.pdf
 * WHICH FUNCTIONS WORK?
 * 	randomInt, printer, main, beginGame, generate7Player, generateSinglePlayer,turn1-5choose, trustChanger,
 *  chooseMerlin,chooseMordred,endGame,checkPlayer, randomNumber, loop2Choose, loop2ChooseBad, rejection
 *  questHandler, roundWin
 * WHAT IN THE CLASS WORKS?
 * 	all of em, to do operations on the array use the changeArray funct in the genericPlayer class
 * 	
 * 	A lot of the code is just comments
 * -- info -- means strikethrough, idea was had but not expanded / dont like anymore
 * 
 * TODO
 * 	Modify / improve rejection method 
 *  IMPLEMENT the go to next turn thing, / who wins thing
 *	IMPLEMENT PERCIVAL CHOOSING FOR TURN 4 AND 5
 * 
 * NOTES --> 10/10 to reduce code duplication, make the turn functions call a choosing thing depending
 * 			on number of victories
 * 			Example) if(badGuyWins == 0) badNoWins(); else if (badGuyWins == 2) callInMerlinDotJpeg();
 * 		--> Do not forget to reset your playersChosen array after each decision goes through
 * 
 * NOTABLE ERRORS
 * 		Currently in turn2Choose an error has been brought up where the decision of choosing
 * 		the same people as last -successful- turn may be funky
 * 
 * 		*** INCORPORATE THE MONTY HALL PROBLEM INTO GUESSING MORDRED --is this even possible? ***
 * 		^^ After giving it a thought, the Monty Hall problem is situational, it rides on the idea
 * 		   it wouldnt work if they picked the person not Morgana	
 * CHANGELOG
 * 	Finished chooseMordred,chooseMorgana to decent quality
 */

public class game {
	// Array below is meant to store all of the player objects
	static genericPlayer playerz[] = new genericPlayer[7]; // Should this be static? else gives an error
	static int playersChosen[] = new int[7]; // a "1" in the array means that person at index was chosen
	final static int turns = 5;
	static int goodGuyWins = 0; // Number of times good guys have won
	static int badGuyWins = 0; // Number of times bad guys have won
	static int x = 0;
	static int takenUp[] = new int[7];
	// All helper functions are up here

	/**
	 * Generates a random number given a max and a min The purpose of this is to
	 * assign a "number" to each player. If that number is taken up , then it
	 * goes to the next one
	 * 
	 * @param min
	 *            for current (7 player) array purposes should be "0"
	 * @param max
	 *            for current (7 player) array purposes should be "6"
	 * @return
	 */
	public static int randomInt(int min, int max) {
		Random rand = new Random();
		int randomNum;
		// We need it to execute at least once
		do {
			randomNum = rand.nextInt((max - min) + 1) + min;
		} while (takenUp[randomNum] != 0); // While you have not found something
											// taken up
		takenUp[randomNum] = 1;// The array now has something in it and that
								// index is used
		return randomNum;
	}

	/**
	 * This is just to print results for testing things
	 */
	public static void printer() {
		System.out.println("Testing changing things like character and array");
		playerz[0].setCharacter("HELLOOO");
		int changedArray[] = { 1, 2, 3, 4, 5, 6, 7 };
		playerz[0].setSuspects(changedArray);
		// How to do operations on the array? make a function in genericPlayer
		playerz[0].changeArray(3, 38);
		playerz[0].printCharacterAspects();
	}

	public static void main(String args[]) {
		beginGame(7);
	}

	/**
	 * This will begin the game and assign the roles for now, will always be
	 * called with "7"
	 * 
	 * @param numberOfPlayers
	 *            --> Players in the game
	 */
	public static void beginGame(int numberOfPlayers) {
		if (numberOfPlayers == 7) {
			generate7Player();
		} // End if for seven players
	} // End begin game

	/**
	 * Generate the 7 players afaik this function looks long, but is ok For now,
	 * do not worry about it being 25+ lines long, possible split up where you
	 * determine where the people are positioned but we should know them anyways
	 * Possible bug --> having themselves as a 100 -not as a -1- may lead to
	 * funny results ie)Players deducing that they themselves, are actually evil
	 * when they are not
	 */
	public static void generate7Player() {
		/*
		 * Set their positions, important as bad guys must know who is on their
		 * side Merlin must know the identity of the bad guys Percival must know
		 * the identity of MORGANA, and Merlin
		 */
		int baddiePosn, mordredPosn, morganaPosn, merlinPosn, percivalPosn, goodiePosn, goodie2Posn;
		// Idea is to generate baddies before making Merlin so he knows who they are,
		// And generate Merlin before Percival
		baddiePosn = randomInt(0, 6);
		mordredPosn = randomInt(0, 6);
		morganaPosn = randomInt(0, 6);
		merlinPosn = randomInt(0, 6);
		percivalPosn = randomInt(0, 6);
		goodiePosn = randomInt(0, 6);
		goodie2Posn = randomInt(0, 6);
		// Have the default array
		int sussArray[] = { 100, 100, 100, 100, 100, 100, 100 };
		// Generate the players
		// Bad guys use the alliance attribute to tell each other apart
		generateSinglePlayer("Bad1", 1, sussArray, baddiePosn);
		generateSinglePlayer("Mordred", 1, sussArray, mordredPosn);
		generateSinglePlayer("Morgana", 1, sussArray, morganaPosn);
		generateSinglePlayer("Good1", 0, sussArray, goodiePosn);
		generateSinglePlayer("Good2", 0, sussArray, goodie2Posn);
		// Change up the array according to Percival / Merlin
		// Merlin knows who's bad --> may refuse to put them on the last position
		int merlinArray[] = { 100, 100, 100, 100, 100, 100, 100 };
		merlinArray[baddiePosn] = -100;
		merlinArray[mordredPosn] = -100;
		merlinArray[morganaPosn] = -100;
		generateSinglePlayer("Merlin", 0, merlinArray, merlinPosn);
		// Reset the array for Percival (does not know who's bad, just who's Morgana / Merlin)
		int perciArray[] = { 100, 100, 100, 100, 100, 100, 100 };
		perciArray[merlinPosn] = -150; // Percival does not know which is bad
		perciArray[morganaPosn] = -150; // ^
		generateSinglePlayer("Percival", 0, perciArray, percivalPosn);

		System.out.println("Succesfully generated all characters\nCharacters are:");
		for (int i = 0; i < 7; i++) {
			playerz[i].printCharacterAspects();
		}
		// printer();
		// goThroughTurns();
	} // End beginGame7Player

	/**
	 * Called by the methods that need the players to be generated
	 * ie)generate7Player Creates them and puts them in an array
	 * 
	 * @param name
	 *            --> Name of the Player
	 * @param identity
	 *            --> 0 = good, 1 = bad
	 * @param sussArray
	 *            --> Suspect Array
	 * @param playerPosn
	 *            --> Where they are "seated"
	 */
	public static void generateSinglePlayer(String name, int identity, int[] suspArray, int playerPosn) {
		genericPlayer player = new genericPlayer(name, identity, suspArray);
		playerz[playerPosn] = player;
		// System.out.println("Generated player" + playerPosn);
	}

	/**
	 *  Handles the control flow of the entire program
	 * 		Special case: Turn4 Bad guys need 2 "fails" to win
	 * MUST IMPLEMENT CASE WHERE THEY REJECT -->if its the last person, kings
	 * counter blah blah need a while loop for the select players process
	 */
	public static void goThroughTurns() {
		int whoIsKing = 0; // Number for who is KING
		boolean noReject = false;
		for (int turnCount = 1; turnCount <= turns; turnCount++) { // 1,2,3,4,5
			while (noReject == false) { 
				selectPlayers(whoIsKing, turnCount);
				// Step 1 select the players to go on the mission
				// Step 2 Reject players etc influence stuff
				noReject = rejection(4,turnCount); // 4 because 4 is majority in 7 player
				
				if (whoIsKing == 6) { 
					// If somehow nobody can agree on a team comp (2 healers pls)
					whoIsKing = 0; // Reset King powers to first one
					// In this case, I'd say that if this is reached, then an
					// infinite loop may occur
					System.out.println("This really should not be reached...");
					System.exit(0); // Get outta here
				}
				whoIsKing++; // If not out of the loop, then put the next person as the King
				Arrays.fill(playersChosen, 0); // Re-initialize everything to zero
				System.out.println("The quest-doers have been reset.");
			} // End the rejection loop
			
			// Step 3, check the results of the quest, remember, turn 4 needs 2 fails
			
			// Step 4, Check the count of good and bad guy wins
			if (goodGuyWins == 3) { // If good guy count == 3 --> chooseMerlin
				System.out.println("Good guys win! Bad must snipe Merlin");
				chooseMerlin();
			} else if (badGuyWins == 3) { // else if bad guy count == 3 chooseMordred
				System.out.println("Bad guys win! Good must snipe Mordred");
				chooseMordred();
			}
			// Step 5, reset the "selection" array to all zeros, as if no one was chosen
			Arrays.fill(playersChosen, 0); // Chosen array now has no one in it it went through
			System.out.println("The quest went through, chosen is now zero");
		} // End turns loop
	} // End goThroughTurns

	/**
	 * Purpose of this function is to determine the players that will go on the
	 * quest Always chooses themselves at least (because I know I'm good, I
	 * choose me) --Should work in parallel with a "rejection" system-- nah
	 * 
	 * @param whosChoosing
	 *            --> Who is deciding on who goes on the mission
	 * @param whichTurn
	 *            --> Number of players chosen depends on which turn it is Turn
	 *            Number 1, 2, 3, 4, 5 7 Players --> 2, 3, 3, 4, 4
	 */
	public static void selectPlayers(int whosChoosing, int whichTurn) {
		if (whichTurn == 1) {
			System.out.println("Turn 1 Good:" + goodGuyWins + "\nBad:" + badGuyWins + "\nSelecting adventurers:");
			playersChosen[whosChoosing] = 1; // 1 Means chosen, also choose themselves
			System.out.println("Player " + whosChoosing + "has elected themselves for adventure!");
			// could call something here to see if anyone rejects but it's the first round
			turn1Choose(whosChoosing,7);
		} else if (whichTurn == 2) {
			System.out.println("Turn 2 Good:" + goodGuyWins + "\nBad:" + badGuyWins + "\nSelecting adventurers:");
			playersChosen[whosChoosing] = 1;
			System.out.println("Player " + whosChoosing + "has elected themselves for adventure!");
			// again, could call some sort of rejection function but it's the
			// second turn
			turn2Choose(whosChoosing,7);
		} else if (whichTurn == 3) { // have to start checking which team is in
										// danger of winning
			System.out.println("Turn 3 Good:" + goodGuyWins + "\nBad:" + badGuyWins + "\nSelecting adventurers:");
			playersChosen[whosChoosing] = 1;
			System.out.println("Player " + whosChoosing + "has elected themselves for adventure!");
			/*
			 * call some sort of rejection function, ie) if good guys are going
			 * to win, baddies want to reject a good person (want at least one
			 * bad guy to ruin the lives) or if baddies about to win --> let
			 * Merlin try and convince
			 * 
			 * dont forget to change the "1" back to a zero if rejected by
			 * majority
			 */
			turn3Choose(whosChoosing,7);
		} else if (whichTurn == 4) { // must check who's in danger of winning,
										// needs 2 defeats
			System.out.println("Turn 4 Good:" + goodGuyWins + "\nBad:" + badGuyWins + "\nSelecting adventurers:");
			playersChosen[whosChoosing] = 1;
			System.out.println("Player " + whosChoosing + "has elected themselves for adventure!");
			// same rejection idea as whichTurn 3
			turn4Choose(whosChoosing,7);
		} else if (whichTurn == 5) { // must check who's in danger of winning,
										// needs 1 defeat
			System.out.println("Turn 5 Good:" + goodGuyWins + "\nBad:" + badGuyWins + "\nSelecting adventurers:");
			playersChosen[whosChoosing] = 1;
			System.out.println("Player " + whosChoosing + "has elected themselves for adventure!");
			// same rejection idea as whichTurn 3
			turn5Choose(whosChoosing,7);
		}
	} // End selectPlayers

	/**
	 * Choose one more player
	 * 		Could put a seperate if for Merlin so that he chooses a goodie
	 * 			To build trust up with other goodies
	 * @param whoChooses
	 *            --> Whos choosin
	 * @param gameMode
	 *            --> 7 player game mode, 8 player etc doesnt matter for now
	 */
	public static void turn1Choose(int whoChooses, int gameMode) {
		if (playerz[whoChooses].getAlliance() == 0) { // Good guy chooses arbitrarily
			loop2Choose(1,whoChooses,0);
			// Choose one more player, probably chooses first one they run into
			// In the first turn, everyone claims they are good anyways
		} // End good guy statement
		else { // They are a baddie
			loop2ChooseBad(1,whoChooses,0);
			//Choose one other person, zero baddies
		} // End bad guy statment
	} // End turn1Choose

	/**
	 * Select two more players 
	 * @param whoChooses
	 * @param gameMode --> doesnt matter right now
	 */
	public static void turn2Choose(int whoChooses, int gameMode){
		// SO THAT the other statement is even reachable, for the FIRST TURN
		// bad guy may fail 1st just for giggles (probably a 70% chance) to create chaos
		// At this point, good and bad should be playing same strategy
			if(badGuyWins == 1){ // bad guys for some reason won first round?
				loop2Choose(2,whoChooses,80);
				// 80 because nani, howd we lose first round, insta - distrust
				
			} // End bad guys first victory conditional
			else{ // Good guys won blessed
				// Should theoretically choose the same people as last time
				loop2Choose(2,whoChooses,104);
				// Chooses the same people as the last mission -their suspect woulda increased
				/*
				 * POSSIBLE ERROR
				 * What happens if it's Merlin's / Percival's turn to choose???
				 * Their suspect arrays have negative numbers so what if those negative numbers
				 * Coincide with the people chosen last turn???
				 */
			}  
	} // End turn2Choose
	
	/**
	 * Choose 2 other adventureres
	 * @param whosChooses --> who has kingsToken
	 * @param gameMode --> does nothing yet
	 */
	public static void turn3Choose(int whoChooses, int gameMode){
		// Third round so get ready
			if(badGuyWins == 2){ // bad guys verge of victory
				if(playerz[whoChooses].getAlliance() == 0){
					loop2Choose(2,whoChooses,55);
				}
				else if (playerz[whoChooses].getAlliance() == 1){ // Bad guy has to choose
						// Given how I choose to implement Merlin "ai" -not really ai tbh-
						// Whoever the bad guy chooses may not matter because Merlin may override it
						// So in this case, bad guy may choose other baddie (may or may not be Mordred)
						loop2ChooseBad(2,whoChooses,1);
						// Merlin should stop this mission unless Mordred is known for sure i guess
						}
				// Could have a percival seperate statement here, 
				// but he can still choose the other 2 good guys w/o deciding between morg and merl
				
				} // End badGuy2Wins
			else if (badGuyWins == 1){ // It's tied
				loop2Choose(2,whoChooses,100);
				// 100 kinda arbitrary, but still indicates trustworthiness as baddies
				// would have some sort of negative number
				// Even if a bad guy, still chooses good guys for "trust" building
				// The bad guy would fail it anyways
			}
			else{ /* badGuys have somehow not won a single game
				  Should have a case for Merlin??? where he only selects goodies? They should all agree
				  If so then it's technically an insta victory for good guys as long as 
				  none of the good guys think Merlin is bad lul
				  */
				if (playerz[whoChooses].getCharacterN().equals("Merlin")){
					loop2Choose(2,whoChooses,0);
					// Select 2 players, 0 just in case for some reason Merlin suspects people
					// Baddies will always be < -100 so thats not a worry
				}// End Merlin "ensuring" victory
				else if (playerz[whoChooses].getAlliance() == 0){
					loop2Choose(2,whoChooses,130);
					// Select 2 players, 130 currently an arbitrary number for choosing who's in
					// Should be synonymous with the "rejection function" 
				}
			}
	} // End turn3Choose
	
	/**
	 *  Choose 3 other adventurers
	 * @param whosChooses --> who has kingsToken
	 * @param gameMode --> does nothing yet
	 * iirc bad guys need 2 rejects here?
	 */
	public static void turn4Choose(int whoChooses, int gameMode){
		if(badGuyWins == 2){
			if(playerz[whoChooses].getCharacterN().equals("Merlin")){
				// Merlin pulls out the stops
				loop2Choose(3,whoChooses,0);
			}
			// Needs a percival statement where he GOMBLES it on Merlin / Morg
			else if (playerz[whoChooses].getAlliance() == 0){ // (mostly) Regular goodie
				loop2Choose(3,whoChooses,65); 
				// Should be mostly same number as turn3Choose where baddies have 2 wins
				// Should be slightly higher because that means they survived the third round
			}
			else{
				// They be bad
				loop2ChooseBad(3, whoChooses,2);
			}
		}
		else{ // Bad guys 1, good guys 2
			if(playerz[whoChooses].getCharacterN().equals("Merlin")){
				// WIN WIN WIN
				// Should really be a trigger for baddies to snipe out merlin though
				loop2Choose(3,whoChooses,0);
			}
			else if (playerz[whoChooses].getAlliance() == 0){ 
				// (mostly) Regular goodie, Percival should operate under same logic
				// 
				loop2Choose(3,whoChooses,110); 
				// Should be slightly above 100 or slightly below 100 depending
				// on how I decide to implement what takes more or gives more to trust
			}
			else{
				// They be bad
				loop2ChooseBad(3, whoChooses,0);
				// Cause CHAOS?!? --> only in real life but hey we can try and simulate
			}
		} // End bad guys decision
	} // End turn4Choose
	
	/**
	 * Choose 3 other adventurers
	 * @param whoChooses --> who has kingsToken
	 * @param gameMode --> does nothing yet
	 */
	public static void turn5Choose(int whoChooses, int gameMode){
		// It's game 5 for Evo 2017 it can and will go either way
		// No need for bad guy wins == 2 because it's 2-2
		
		if(playerz[whoChooses].getCharacterN().equals("Merlin")){
			// Always try and go for W, unless have faith in sniping Morg
			loop2Choose(3,whoChooses,0);
		}
		// Like turn4Choose, should be a percival statement b/c he "knows" at least one good guy
		else if (playerz[whoChooses].getAlliance() == 0){
			loop2Choose(3,whoChooses,50);
		}
		else if(playerz[whoChooses].getAlliance() == 1){
			// LOSE LOSE LOSE
			loop2ChooseBad(3,whoChooses,0);
			/* If you want it to be better / more realistic 
			 * should make a method where the bad guy takes input
			 * From the other people.
			 */ 
		}
		
	} // End turn5Choose
	
	/**
	 * Will handle the logic for quests 
	 * IE) this many baddies = this many fails
	 * IE) this turn, then blah blah
	 * 
	 * @param turnNumber --> the current turn
	 */
	public static void questHandler(int turnNumber){
		// int numGood = 0; number of good people doesnt really matter
		int numBad = 0; // Have all the power since good can only choose success
		int random;
		// Get the identities of the people in the chosen array
		for (int i = 0; i < playersChosen.length; i++){
			if (playersChosen[i] == 1){
				if (playerz[i].getAlliance() == 1){
					numBad++;
				}
			}
		} // End the loop to look for em
		if(turnNumber == 1){
			random = randomNumber(1,100);
			if (numBad == 1){
				if (random > 85){ 
						// The rng element where the bad guy decides to lose first round
					roundWin(1);
				}
				else{
					roundWin(0);
				}
			} // End baddies
			else{
				roundWin(0);
			}
		} // end turn1
		// Only special case for turn 4
		else if (turnNumber == 4){
			if(numBad >= 2){
				roundWin(1);
			}
			else{
				roundWin(0);
			}
		}
		// Special case for baddies about to lose / about to win but not turn 4
		else if (goodGuyWins == 2 || badGuyWins == 2){
			if (numBad >= 1){
				roundWin(1);
			}
		}
		else{ // Made it through
			if(numBad >=1){
				random = randomNumber(1,100);
				if (random < 69){
					roundWin(1);
				}
			}
			else{
			roundWin(0);
			}
		}
	} // End questHandler
	
	/**
	 * Prints out who won the round and increments counter
	 * After round win, should probably call something that has trustChange
	 * @param win --> Integer where 0 means good won, 1 means bad won
	 */
	public static void roundWin(int win){
		if(win == 0){
			System.out.println("Good guys win this round");
			goodGuyWins++;
		}
		else{
			System.out.println("Bad guys win this round");
			badGuyWins++;
		}
	} // End roundWin
	/**
	 * Purpose is to loop through the suspect array of who's choosing and choose
	 * the appropriate adventurers 
	 * @param numberToPick -> The amount of adventures to pick
	 * @param whoChooses -> Who has choice
	 * @param suspectAmount -> The allowable amount (or lack) of suspicion to go on the mission 
	 */
	public static void loop2Choose(int numberToPick, int whoChooses, int suspectAmount){
		int[] suspec = playerz[whoChooses].getSuspects(); // Pull out your suspect list
		int numberChosen = 0;
		for(int i = 0; i < 6; i++){
			if (numberChosen == numberToPick){
				break; // Get outta here you finished here
			}
			else if(i == whoChooses){
				continue; // You cannot select yourself (already have) to keep saying you good
			}
			else if (suspec[i] > suspectAmount){
				System.out.println("Player " + whoChooses + " has chosen player " + i 
						+ " for the adventure");
				playersChosen[i] = 1; // Select them
				numberChosen++; // Increment
				trustChanger(20, i, whoChooses);
			}
		}
	} // End loop2Choose
	
	/**
	 * Choosing but for bad guys, bad guys may want to choose other bad guys for a variety of reasons
	 * 	1) Build trust with the good guy who also went on the adventure
	 *  2) Have the enemy look like the bad guy yada yada
	 * @param numberToPick -> Amount of adventurers to pick
	 * @param whoChooses -> Who has choice
	 * @param numBaddy -> Number of baddies they want to choose
	 * 		Because as long as one bad guy exists, numBaddy can realistcally just be zero
	 * 		But you want to build up trust on early missions so optimal choice is to select
	 * 		Another baddie for early missions
	 */
	public static void loop2ChooseBad(int numberToPick, int whoChooses, int numBaddy){
		//This function may still elect to choose only good guys lul
		// int[] suspec = playerz[whoChooses].getSuspects(); useless because baddies go off Alliance
		int numberChosen = 0;
		int i = 0;
		int baddieChosen = 0;
		boolean baddiesChosen = false;
		while (baddiesChosen == false){
			// Check this first because if player wants to select no baddies this breaks immediately
			if (baddieChosen == numBaddy){
				baddiesChosen = true;
				break;
				// Get me outta here
			}
			if(i == whoChooses){
				i++; // Increment
				continue;
			}
			else if (playerz[i].getAlliance() == 0){
				i++; // Increment
				continue;
			}
			else if (playerz[i].getAlliance() == 1){
				// They want to choose baddies
				playersChosen[i] = 1;
				baddieChosen++;
				System.out.println("Player " + whoChooses + " has chosen player " + i 
						+ " for the adventure");
				numberChosen++;
				// No need to have trust changer here because it's bad choosing bad
				i++; // Increment
			}
		}
		// Choose the good guys
		for(i = 0; i < 6; i++){
			if (numberChosen == numberToPick){
				break; 
				// Exit the loop no more need to loop
			}
			if(i == whoChooses){
				continue;
				// Do not choose yourself
			}
			else if (playerz[i].getAlliance() == 1){
				// Do not want to choose baddies
				continue; // Skip over them
			}
			else if (playerz[i].getAlliance() == 0){
				playersChosen[i] = 1;
				numberChosen++;
				System.out.println("Player " + whoChooses + " has chosen player " + i 
						+ " for the adventure");
				// Have the person trust this guy
				trustChanger(15, i, whoChooses);
			}
		}
	} // End loop2ChooseBad
	
	/**
	 * EDIT: Just use the public variable of playersChosen and use that index
	 * 	ie) if playersChosen(i) == 1; // 1 means chosen
	 * Cross check with everyone's arrays
	 * Would also need to be seperated by an if / else (for good and bad) if(alliance == 0) -->
	 * do these things, else --> This is needed because they take seperate
	 * actions. Bad likes bad, good dislikes bad
	 * <h1>
	 * *** Possible Error / Fix / Better way of doing this ***
	 * <br>
	 * 	Should probably have it so that once a person is rejected, 
	 *  the person choosing can choose another person
	 * </h1>
	 * <br>
	 * @param maxReject --> Number of rejections it takes to get rid of a person
	 * @param whatTurn --> used to determine the amount of "trust" it takes to NOT reject someone
	 * @return --> True if player not rejected
	 */
	public static boolean rejection(int maxReject, int whatTurn) {
		/*
		 *  (?)Before returning, should have an increment based on current(?)
		 *  Logic is to select the first player going on a mission, then get 
		 *  everybody's opinion on that one
		 *  Only need 2 for loops (NOT THREE)
		 *  TODO --> Add a behaviour when a person gets rejected,
		 *  Decision Should probably be different logic Merlin / Percival
		 *  The logic for a bad guy rejecting is meh
		 */
		// Trustometer, the amount of trust needed to not be rejected
		int trustometer = 0;
		int timesRejected = 0;
		int[] suspects = new int[7];
		int randomBehav;
		// Following trustometer things are for generic good guys, 
		if (badGuyWins == 0){
			trustometer = (100+(whatTurn*12)); 
			// Trust goes up by maybe 12 for each successful turn?
			// Example, if it's turn 3 and its 2 good guy wins, then need 136 trust?
		}
		else if (badGuyWins == 1){
			trustometer = (80+((whatTurn-1)*12));
			// Because baddies cannot start with a win, whatTurn - 1
			
		}
		else if (badGuyWins == 2){
			trustometer = (50+((whatTurn-1)*12));
			// At minimum turn 3
		}
		// Get the opinion of everyone about the one person
		for (int i = 0; i < 6; i++){ // Loop through the playersChosen array
			if (playersChosen[i] == 0){
				continue; // They were not chosen, go on to next person
			}
			for (int j = 0; j < 6; j++){ 
				// Loop through the playerz array to get their opinion on player i
				// Could put below if statement inside the for loop, but that just adds
				// more comparisons 
				if(playerz[j].getAlliance() == 0){ // If they are a guuci guy
					suspects = playerz[j].getSuspects();
					if (suspects[i] < trustometer){ // If the person is not trustworthy
						timesRejected++; // Strike!
						System.out.println("Player " + j + " has rejected player " 
								+ i + ".");
						trustChanger((-30), i, j);
						// Easier to lose trust, -30 might be too much
						// Call something to reduce trust between two people here?
						}
					else{
						System.out.println("Nah you player " + i +" seem like a good guy");
						// add build trust here just a little
						trustChanger(5, i, j);
					}
				}
				else{ // Not so guuci
					randomBehav = randomNumber(1,10);
					if(playerz[i].getAlliance() == 1 && randomBehav <=2){ // 20% chance to reject
						timesRejected++; // Strike!
						System.out.println("Player.. ps Im bad " + j + " has rejected player " 
								+ i + ".");
						// no reduce trust here (because both are bad guys)
						// instead have this build trust with the people who also disagree
						// ^ implement later, kinda difficult
						// maybe in a whoElseDisagrees function or something
					}
					else if(playerz[i].getAlliance() == 0 && randomBehav <= 2){ // 20% chance to reject good
						timesRejected++; // Strike!
						System.out.println("Player.. ps Im bad " + j + " has rejected player " 
								+ i + ".");
						trustChanger((-31),i,j);
						// -31 just for intuitions sake?
						// have rejected persons trust decreased towards this -bad- guy
					}
					else{
						System.out.println("Nah you player" + i + " seem like a good guy. PS "
								+ "im bad" + " I am player " + j);
						trustChanger(5,i,j);
						// add build trust
					}
				}
			} // End going through everyone
			if (timesRejected >= maxReject){
				System.out.println("Player " + i + " has been rejected by the majority."); 
				// Maybe instead return a number ?
				for (int j = 0; j <playerz.length; j++){
					trustChanger((-5),i,j);
					// This might be a little too much in tandem with the initial -30 from rejecting
				}
				return false; // Majority says no to person x to going on mission
			}
		} // End looping through playersChosen
		return true; // If not rejected
	} // End rejection

	/**
	 * Just a function to help with the 'chance' of the game
	 * @param min --> Min of range
	 * @param max --> Max of range
	 * @return
	 */
	public static int randomNumber(int min, int max){
		int range = (max - min) +1;
		int number = (int)(Math.random()*range)+min;
		return number;
	}
	
	/**
	 * Basically the changeArray in the genericPlayer class
	 * @param amountChanged --> The amount of lost or gained trust
	 * @param defender --> The person's personal trust array
	 * @param offender --> The index id of who's getting their "100" lowered / upped
	 */
	public static void trustChanger(int amountChanged, int defender, int offender) {
		playerz[defender].changeArray(offender, amountChanged);
	}
	
	
	/**
	 * Method currently used for the endgame where the losing team must snipe
	 * the enemy leader -Aside- this can also eventually be used for Lady of the
	 * Lake
	 * 
	 * @param playerToCheck
	 *            --> Passed in player
	 * @return A string to tell the other function the identity of the passed in
	 *         player
	 */
	public static String checkPlayer(int playerToCheck) {
		return playerz[playerToCheck].getCharacterN();
	} // End checkPlayer

	/*
	 * END GAME THINGS
	 */

	/**
	 * This is the final function, this will end the program. Called after one
	 * of the parties win and based on who won, will call out Merlin or Mordred
	 * 
	 * @param A
	 *            boolean that tells us who won true if good, false if baddies
	 */
	public static void endGame(boolean whoWon) {
		if (whoWon == true) { // Good guys won
			int whosThere = chooseMerlin(); // One last chance to snipe Merlin
			if (checkPlayer(whosThere).equals("Merlin")) {
				System.out.println("Bad guys win off assasinating Merlin!");
				System.out.println("Merlin was indeed player " + whosThere);
				System.exit(0);
			} else { // They guessed wrong
				System.out.println("Bad guys could not kill Merlin \nGood guys win");
				System.exit(0);  
			}
		} // End good guys won conditional
		else { // Bad guys won
			int whosThere = chooseMordred();
			if (checkPlayer(whosThere).equals("Mordred")) {
				System.out.println("Good guys win off assasinating Mordred!");
				System.out.println("Mordred was indeed player " + whosThere);
				System.exit(0); 
			} else {
				System.out.println("Good guys could not kill Mordred \nBad guys win");
				System.exit(0); 
			}
		} // End bad guys win conditional
	} // End endGame

	/**
	 * This is the function to be executed iff the good guys win (ie three wins)
	 * Because this is simplified, it doesn't matter which bad guy "chooses"
	 * Merlin. Given the current way to determine who is Merlin, they should end
	 * up at the same result
	 * 
	 * @return an integer allowing a cross-check location in the genericPlayer
	 *         array
	 */
	public static int chooseMerlin() {
		// An integer to return to the function that called this one. Looks up
		// the int in the array
		int theDecision = 0;
		// Insert code here to have the bad guys choose Merlin
		// As of right now always guesses person 0
		// All baddies have exact same AI, therefore, they just one of them has to guess
		int theChooser=0;
		for (int i = 0; i <7; i++){ // Find the bad
			if (playerz[i].getAlliance() == 1){
				theChooser = i;
				break;
			}
		}
		int[] theArray = playerz[theChooser].getSuspects(); // get their opinion
		int greatestAmntSuspect = 100;
		for (int i = 0; i < 7; i++){
			if (playerz[i].getAlliance() == 0){
				if (greatestAmntSuspect > theArray[i]){
					greatestAmntSuspect = theArray[i]; // change the new lowest
					theDecision = i; 
				}
			}
		} // End the decision loop
		  // theDecision, should now be the most suspected person
	return theDecision;
	} // End chooseMerlin

	/**
	 * @see chooseMerlin()
	 * @return an integer allowing a cross-check location in the genericPlayer
	 *         array
	 */
	public static int chooseMordred() {
		int theDecision = 0;
		// Insert code here to have the good guys try and snipe Mordred
		// As of right now always guesses player 0
		// In 20xx, all bad guys speak the same amount, so no real indication of
		// Mordred
		// It falls on a 50/50 where Percival knows who Mordred is but not
		// identity of other 2
		int perci = 0;
		for (int i = 0; i < playerz.length; i++){
			if (playerz[i].getCharacterN().equals("Percival")){
				// Get percival so he can choose
				perci = i;
			}
		}
		int perciSus[] = playerz[perci].getSuspects();
		// rip me have to do another for loop
		for (int i = 0; i < perciSus.length; i++){
			if(perciSus[i] <0 && playerz[i].getAlliance() ==1){
				// This was morgana
				System.out.println("This player" + i + " is Morgana.");
			}
			else if (playerz[i].getAlliance() == 1){
				// If the person is not Morgana and is bad, just go for the fity fity
				return i;
			}
		}
		return theDecision;
	} // End chooseMordred
} // End game.java	