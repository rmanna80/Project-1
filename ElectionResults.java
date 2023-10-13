import java.awt.image.TileObserver;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.*;

/*
 Author: Ryan Manna
 Date:

 REFLECTION ANSWERS:
 1. An obstacle that I encountered on this project was trying to translate all the ideas that I had in my head onto the computer.
 I had everything that I wanted in pseudocode, but when it came down to putting it into code I found that it was hard.
 What helped me was taking a step back every once in a while, taking a 10-minute break, distracting myself with something
 else and then coming back to the code with a clear mind.
 2. One thing that I learned about the software development process was that you can overcomplicate the simple things a lot.
 I felt like since I was so excited to work on the project I wanted to get everything done and do a lot of work in one day,
 but I felt like that was harder, and what I should've done was set up goals for myself every day to get parts of it done instead
 of the whole thing at once.
 3. I feel like there are some benefits of the ranked choice voting process and also some negatives. The positives are that
 there is a majority rule where it leads to fewer outcomes, there can be a higher turnout of the votes, and it is very cost-efficient for the election.
 The negatives are sometimes people can make mistakes on their ballots leading to a higher turnout rate than expected, counting-the
 ballots especially when it comes to having a higher file it can be more time-consuming, it can also be complicated for the voters to understand
 which can lead to different errors on the ballots.


 */
public class ElectionResults {

    // the main method works as follows:
    // - provided code (leave this code as is):
    //   - prompts user for file name containing ballot data
    //   - reads data into array (one array item per line in file)
    //   - runs any testing code that you have written
    // - code you need to write:
    //   - execute the Ranked Choice Voting process as outlined
    //     in the project description document by calling the other
    //     methods that you will implement in this project
    public static void main(String[] args) {
        // Establish console Scanner for console input
        Scanner console = new Scanner(System.in);

        // Determine the file name containing the ballot data
        System.out.print("Ballots file: ");
        String fileName = console.nextLine();

        // Read the file contents into an array.  Each array
        // entry corresponds to a line in the file.
        String[] fileContents = getFileContents(fileName);

        // ***********************************************
        // Your code below here: execute the RCV process,
        // ensuring to make use of the remaining methods
        // ***********************************************
        ArrayList<Ballot> ballots = convert(fileContents);
        HashMap<String, Integer> tally = tallies(ballots);

        while (true) {
            printCounts(tally); //call the method printCounts
            Result result = analyze(tally); // call analyze
            if (result.isWinner()) { // now i have to check id candidate is winner
                System.out.println();
                System.out.println(result.getName() + " wins!"); // if winner then i have to print their name
                System.out.println();
                break;
            } else { // now i have to check id candidate is winner
                System.out.println();
                System.out.println("Remove " + result.getName()); // if loser i have to remove them
                System.out.println();
                remove(result.getName(), ballots);
                tally = tallies(ballots);
            }
        }

        printPercentages(tally, ballots.size()); // call method to get the tally and size fo the ballots


    }

    // Create your methods below here
    public static ArrayList<Ballot> convert(String[] fileContents){
        ArrayList<Ballot> ballots = new ArrayList<>();

        //go through the file
        for(String line : fileContents) {
            String[] candidateNames = line.split(","); // split it where there is a ','
            Ballot ballot = new Ballot();
            //go through each candidate name
            for(String candidateName : candidateNames){
                ballot.addCandidate(candidateName); // add the candidate name to the new ballot
            }
            ballots.add(ballot); // add the new ballot to the origional ballot
        }
        return ballots;
    }
    public static HashMap<String, Integer> tallies(ArrayList<Ballot> ballots) {
        System.out.println();
        System.out.println("Vote Tallies");
        //create a new hashmap to store the vote tallies
        HashMap<String, Integer> tally = new HashMap<>();
        // iterate through each Ballot object in ballots ArrayList
        for (Ballot ballot : ballots) {
            //for each ballot it retrieves the currentchoice candidate
            //checks the name obtained from the Ballot that is in tally
            String candidate = ballot.getCurrentChoice();
            // if the candidate is already  in the tally it increments by 1 but if the candidate
            //is not in the mao it initalizes their vote count to 1
            tally.put(candidate, tally.getOrDefault(candidate, 0) + 1);
        }
        //this contains the tally of votes for each candidate
        return tally;
    }

    public static int countTotalVotes(HashMap<String, Integer> voteTallies){

        int totalVotes = 0; // starting from 0
        for(int count : voteTallies.values()){ // for each vote  that the candidate recieived
            totalVotes += count; // i am adding it onto teh totalVotes
        }
        return totalVotes;
    }

    public static Result analyze(HashMap<String, Integer> totalTallies){
        int numtotalVotes = countTotalVotes(totalTallies); // calling the method countTotalVotes for the totalVotes
        int numOfVotesNeeded = numtotalVotes / 2 + 1; // initalising what is the numebr that each candidate needs to in order to win

        String winner = null;
        String loser = null;
        boolean winnerFound = false;

        for(String candidate : totalTallies.keySet()){
            int votes = totalTallies.get(candidate);
            if(votes >= numOfVotesNeeded){
                winner = candidate;
                winnerFound = true;
                break;
            } else {
                if(loser == null || votes < totalTallies.get(loser)){
                    loser = candidate;
                }
            }
        }
        if(winnerFound){
            return new Result(winner, true);
        } else {
            return new Result(loser, false);
        }
    }

    public static void printCounts(HashMap<String, Integer> votes2){
        for(String candidate : votes2.keySet()){ // for each candidate in the hashmao
            System.out.println(candidate + ": " + votes2.get(candidate)); //printing teh votes that they recieved
        }
    }

    public static void remove(String name, ArrayList<Ballot> ballots) {
        Iterator<Ballot> it = ballots.iterator();
// intalize the iterator to iterate through the ballot
        while (it.hasNext()) { //while true
            // go through each one thats what .next is allowing me to do
            Ballot ballot = it.next();
            // remove the loser
            ballot.removeCandidate(name);

            if (ballot.isExhausted()) { // if nobody wins
                it.remove(); // remove the ballot itself
            }
        }
    }

    public static void printPercentages(HashMap<String, Integer> votes3, int totalVotes){ // intalize int totalVotes in parameters instead of doing it in the method
        System.out.println("Vote Pervantages");
        for(String candidate : votes3.keySet()){// for each candiate in the hashmap
            int votes = votes3.get(candidate); // i have to get the candidate
            double percentage = (double) votes / totalVotes * 100; // calculate the percantage
            System.out.println(String.format("%.1f%% %s", percentage, candidate));
        }
    }

    

    // DO NOT edit the methods below. These are provided to help you get started.
    public static String[] getFileContents(String fileName) {

        // first pass: determine number of lines in the file
        Scanner file = getFileScanner(fileName);
        int numLines = 0;
        while (file.hasNextLine()) {
            file.nextLine();
            numLines++;
        }

        // create array to hold the number of lines counted
        String[] contents = new String[numLines];

        // second pass: read each line into array
        file = getFileScanner(fileName);
        for (int i = 0; i < numLines; i++) {
            contents[i] = file.nextLine();
        }

        return contents;
    }


    public static Scanner getFileScanner(String fileName) {
        try {
            FileInputStream textFileStream = new FileInputStream(fileName);
            Scanner inputFile = new Scanner(textFileStream);
            return inputFile;
        }
        catch (IOException ex) {
            System.out.println("Warning: could not open " + fileName);
            return null;
        }
    }
}
