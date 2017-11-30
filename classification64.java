package biocomputation;

import static biocomputation.classification2000.enhance;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class classification64 {

    //Global integers leave em alone
    static int numberofNines = 0;
    static int pop = 500;//number of individuals in a given population
    static int N = 70;//Number of Genes
    static int mP = 10;//Mutation Probablity
    static int generations = 1000; //Number of generations/iterations
    static int row32L = 5;//Number of bits located in the input variable component of a data1 row
    static int row64L = 6;//^^ but six and data2
    static int indoRules = 10;//Number of 'rules' generated by each individual in the population.
    static int indoRules64 = 12;
    static individual[] population = new individual[pop];
    static individual[] offspring = new individual[pop];
    static individual fittest = new individual();
    static individual worst = new individual();
    
    public static String enhance = "src/biocomputation/output";

    public static void main(String[] args) throws UnsupportedEncodingException, IOException {

        row64[] input64 = getData();

        int i, j, y, g = 0;

        //Running totals for the given fitness of a population. I am only storing
        //the first and final fitness totals to draw a contrast/illustrate how well
        //the selection process works.
        /*  individual[] population = new individual[Pop];
        individual[] offspring = new individual[Pop];
        individual[] crossoverKids = new individual[Pop];*/
        individual fittestIndividual = new individual();
        int highestFitness = 0;
        
        for (g = 0; g < generations; g++) {
            int averageFitness = 0;
            int lowestFitness = 2000;

            int initialFT = 0;
            int currentFT = 0;
            int offspringFT = 0;

            if (g == 0) {
                //GENERATE A POPULATION
                //Create a new array of individuals called population. This is then
                //populated in the nested loops below.

                for (i = 0; i < pop; i++) {
                    //Create a new individual at the start of each initial loop. This individuals
                    //genes are then randomly assigned in the nested loop, either to 1 or 0
                    population[i] = new individual();

                    for (j = 0; j < N; j++) {

                        int k = random();
                        population[i].setGenes(j, k);

                    }
                    population[i].setFitness(0);
                }

                //This loop assesses the 'fitness' of each individual population. This is measured
                //by the total number of 1's vs 0's. The more 1's, the higher a given individuals fitness
                for (i = 0; i < pop; i++) {

                    individual rando = population[i];
                    population[i].fitness = getFunked64(rando, input64);
                    initialFT += population[i].getFitness();

                }

                System.out.println("--------------------------------------------------------------------------------------");
                System.out.println("Generation Number " + g + ". The Population size is " + pop + " and the chromosome length is " + N);
                System.out.println("The total fitness of the initial population is: " + initialFT);
            } else if (g != 0) {

                for (int k = 0; k < pop; k++) {
                    for (int m = 0; m < N; m++) {
                        population[k].genes[m] = offspring[k].genes[m];
                    }
                    population[k].fitness = offspring[k].fitness;
                }
                System.out.println("--------------------------------------------------------------------------------------");
                System.out.println("Generation Number " + g + ". The Population size is " + pop + " and the chromosome length is " + N);
                // System.out.println("The total fitness of the initial population is: " + initialFT);

            }
            //----------------------------------
            //TOURNAMENT SELECTION

            //This is tournament selection. Two individuals are chosen at near-random to
            // go head to head. The winner, or the individuals with the highest fitness ranking
            // is copied into an offspring array. This is done until the size of the offspring array
            //matches that of the original population array. Previously did this in a function/method
            //but it didn't work so now I'm scared
            for (i = 0; i < pop; i++) {
                int parent1 = randomRange(0, pop - 1);
                int parent2 = randomRange(0, pop - 1);

                int p1Fitness = population[parent1].getFitness();
                int p2Fitness = population[parent2].getFitness();

                if (p1Fitness >= p2Fitness) {
                    offspring[i] = population[parent1];
                } else {
                    offspring[i] = population[parent2];
                }
            }
            //---------------------------------------------

            //Check the fitness after tournament selection
            for (i = 0; i < pop; i++) {
                offspring[i].fitness = getFunked64(offspring[i], input64);
                currentFT += offspring[i].getFitness();

            }

            System.out.println("The total fitness of the offspring population after tournament selection is: " + currentFT);

            //------------------------------------------------
            //CROSSOVER
            //
            //Crossing over pairs of individuals; selecting a random point,
            //and swapping the genes of the two individuals after that point
            // ie point = 4, the first 4 genes of each individual stay the same but
            //those thereafter are swapped.
            offspring = crossoverExecute(offspring);

            //--------------------------------
            //MUTATION
            //Each gene, when passed in to the mutation method has a 1/pM chance of
            //mutating. The mutated gene replaces the original gene. pM is set to a value
            //somewhere between the length of the chromosome of an individual (sequence of genes)
            //and the size of the population. Initially, these were both ten.
            for (int x = 0; x < pop; x++) {
                int[] genes = offspring[x].genes;
                //int[] newGenes = new int[genes.length];

                genes = bitwiseMutation(genes);
                for (y = 0; y < genes.length; y++) {
                    offspring[x].setGenes(y, genes[y]);
                }
            }
            //----------------------------------------------
            //FITNESS REASSESSMENT
            int worstCounter = 0;
            for (i = 0; i < pop; i++) {
                offspring[i].fitness = getFunked64(offspring[i], input64);
                //Check if the current individual is the fittest.
                if (highestFitness < offspring[i].getFitness()) {
                    highestFitness = offspring[i].getFitness();
                    for (int k = 0; k < N; k++) {
                        fittest.genes[k] = offspring[i].genes[k];
                        if (k == N - 1) {
                            fittest.fitness = offspring[i].fitness;
                        }
                    }

                } else if (lowestFitness > offspring[i].getFitness()){
                    lowestFitness = offspring[i].getFitness();
                     for (int k = 0; k < N; k++) {
                        worst.genes[k] = offspring[i].genes[k];
                        if (k == N - 1) {
                            worst.fitness = offspring[i].fitness;
                        }
                    }
                     worstCounter = i;
                }
                offspringFT += offspring[i].getFitness();
            }
            averageFitness = offspringFT / pop;
            //System.out.println("");
            System.out.println("The total fitness of the offspring population after crossover and mutation is: " + offspringFT);
            System.out.println("The highest fitness value of any individual in the offspring population is : " + highestFitness);
            System.out.println("The average fitness of the offspring population after crossover and mutation is: " + averageFitness);
            System.out.println("The lowest fitness of the offspring population after crossover and mutation is: " + lowestFitness);
            System.out.println("--------------------------------------------------------------------------------------");
            System.out.println("");
            
               for (int k = 0; k < N; k++) {
                        offspring[worstCounter].genes[k] = fittest.getGenes(k);
                        if (k == N - 1) {
                            offspring[worstCounter].fitness = fittest.fitness;
                        }
                    }

              writeFittest("2", generations, highestFitness, g, pop, N);
            writeAverage("2", generations, averageFitness, g, pop, N);
            writeLowest("2", generations, lowestFitness, g, pop, N);
            //---------------------------------------------------   
        }//End of big for(g=0.. loop
        System.out.println("The highest fitness of any individual in any generation is: " + fittest.getFitness() + "\n");
        System.out.println("The fittest individuals array contents : \n");

        getFunked64PRINT(fittest, input64);

        System.out.println(numberofNines);
    }

    /**
     * Reads in the data from the text file into an array that is used
     * throughout the algorithm
     *
     * @param input64 
     */
    public static row64[] getData() {
        
        row64[] input64 = new row64[64];
        
        Scanner sc64 = new Scanner(classification64.class.getResourceAsStream("data2.txt"));
        for (int i = 0; i < 64; i++) {
            String line = sc64.nextLine();
            String[] parts = line.split(" ");

            int[] parsed = new int[row64L];

            //convert the input variable from a string to an integer
            for (int x = 0; x < row64L; x++) {
                parsed[x] = Integer.parseInt(String.valueOf(parts[0].charAt(x)));
            }

            input64[i] = new row64(parsed, Integer.parseInt(parts[1]));  
            System.out.println(Arrays.toString(input64[i].var) + " " + input64[i].predicted);  
        }
        
        return input64;
    }

    //MUTATION FUNCTIONS
    public static int[] bitwiseMutation(int[] genes) {

        //the luck variable is used as the probability,
        //it will be a number between 0 - 10, and if that number is 7
        //the gene at position y is mutated. The value of luck changes
        //with each iteration.
        int length = genes.length;

        int[] mutantGenes = new int[length];

        for (int y = 1; y < length; y++) {
            int luck = randomRange(0, 500);
            if (luck == 1) {
                //Mutate(invert) the genes if luck is a lady
                numberofNines++;

                switch (genes[y]) {
                    case 0:
                        int luck2 = random();
                        if (luck2 == 1) {
                            mutantGenes[y - 1] = 1;
                        } else if (!(y % 7 == 0)) {
                            mutantGenes[y - 1] = 2;
                        } else {
                            mutantGenes[y - 1] = 1;
                        }
                        break;
                    case 1:
                        int luck3 = random();
                        if (luck3 == 1) {
                            mutantGenes[y - 1] = 0;
                        } else if (!(y % 7 == 0)) {
                            mutantGenes[y - 1] = 2;
                        } else {
                            mutantGenes[y - 1] = 0;
                        }
                        break;
                    default:
                        int luck4 = random();
                        if (luck4 == 1) {
                            mutantGenes[y - 1] = 0;
                        } else {
                            mutantGenes[y - 1] = 1;
                        }
                        break;
                }
            } //if luck is not a lady, the genes don't change, so assign
            //the original gene value at position y  to the 
            // corresponding position in the return array.
            else {
                mutantGenes[y - 1] = genes[y - 1];
            }
        }
        return mutantGenes;

    }

    //CROSSOVER FUNCTIONS
    /**
     * Takes in two individuals genes and crosses them over
     *
     * @param parent1 First array of genes
     * @param parent2 Second array of genes
     * @return returns the crossed over array of genes
     */
    private static int[][] crossover(int[] parent1, int[] parent2) {
        int[][] children = new int[2][N];
        int crossPoint = randomRange(1, N - 1);

        int[] temp = parent1.clone();
        for (int i = crossPoint; i < N; i++) {
            parent1[i] = parent2[i];
            parent2[i] = temp[i];
        }

        children[0] = parent1;
        children[1] = parent2;

        return children;
    }

    /**
     * Takes in an array of individuals and crosses over the genes of individual
     * i and i + 1.
     *
     * @param offspring input population
     * @return Returns an array of newly crossed over individuals.
     */
    public static individual[] crossoverExecute(individual[] offspring) {
        individual[] crossy = new individual[pop];
        for (int i = 0; i < pop; i++) {
            crossy[i] = new individual();
        }

        for (int i = 0; i < pop; i += 2) {

            //Retrieve arrays of genes so as to pass them into the crossover method
            int[] p0 = offspring[i].genes;
            int[] p1 = offspring[i + 1].genes;

            int[][] kids = crossover(p0, p1);

            //The tails have been swapped, new children formed
            crossy[i].genes = kids[0];
            crossy[i + 1].genes = kids[1];

        }
        return crossy;
    }

    //RANDOM GENERATORS   
    /**
     * Generates a random number within the given range.
     *
     * @param min Lower range.
     * @param max Upper range.
     * @return Returns random number between min and max.
     */
    private static int randomRange(int min, int max) {
        
        
       Random r = new Random();
       return r.nextInt(max - min) + min;
        
        
    }

    /**
     * Generates a 0 or a 1 randomly
     *
     * @return o or 1
     */
    public static int random() {
        if (Math.random() < 0.5) {
            return 1;
        }
        return 0;
    }

    //GET FUNKED
    private static int getFunked64(individual rando, row64[] inputData64) {
        int sFitness = 0;
        int g = 0;
        individual nrando = rando;
        row64[] rules = new row64[indoRules];

        //feed in the genes to make some rules
        for (int i = 0; i < indoRules; i++) {
            int[] tempArray = new int[6];
            int pv = 0;
            for (int j = 0; j < 7; j++) {

                if (j == 6) {
                    pv = nrando.genes[g++];

                } else {
                    tempArray[j] = nrando.genes[g++];

                }
            }
            rules[i] = new row64(tempArray, pv);
        }

      
        for (int i = 0; i < inputData64.length; i++) {
            for (int j = 0; j < indoRules; j++) {
                if (compareRules(rules[j].var, inputData64[i].var)) {//if the contents of each array i.e. the rules match
                    if (inputData64[i].getPredicted() == rules[j].getPredicted()) {
                        sFitness++;
                    }
                    break;
                }
            }
        }

        return sFitness;
    }

    private static int getFunked64PRINT(individual rando, row64[] inputData64) {
        int sFitness = 0;
        int g = 0;
        individual nrando = rando;
        row64[] rules = new row64[indoRules];

        //feed in the genes to make some rules
        for (int i = 0; i < indoRules; i++) {
            int[] tempArray = new int[6];
            int pv = 0;
            for (int j = 0; j < 7; j++) {

                if (j == 6) {
                    pv = nrando.genes[g++];

                } else {
                    tempArray[j] = nrando.genes[g++];

                }
            }
            rules[i] = new row64(tempArray, pv);
        }

     
       
        for (int i = 0; i < inputData64.length; i++) {
            for (int j = 0; j < indoRules; j++) {
                if (compareRules(rules[j].var, inputData64[i].var)) {//if the contents of each array i.e. the rules match
                    if (inputData64[i].getPredicted() == rules[j].getPredicted()) {
                        sFitness++;
                    }
                    break;
                }
            }
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(Arrays.toString(rules[i].var) + " " + rules[i].predicted);

        }

        return sFitness;
    }

  
    

    /**
     * Checks to see if a rule generate by the algorithm matches with a rule
     * stored in the data files
     *
     * @param a Rule generated by the GA.
     * @param b Rule stored in the data file.
     * @return Returns true if all 6 items in the arrays match with each other.
     */
    public static boolean compareRules(int[] a, int[] b) {

        int length = a.length;
        int count = 0;

        //Checking for a '2' as '2' is a wild card.
        for (int i = 0; i < length; i++) {
            if (a[i] == b[i] || a[i] == 2) {
                count++;
            }
        }

        //Returns true if all 6 items match, false otherwise
        return count == 6;
    }
    
       public static void writeFittest(String file, int numberOfGenerations, int fittestIndividual, int currentGenNumber, int popSize, int chromosomeSize) throws IOException {
        String suffix = numberOfGenerations + "-" + popSize + "-" + chromosomeSize + ".csv";
        
        if("1".equals(file)){
            
            file = enhance + "data32-HFIT" + suffix;
        }
        else if("2".equals(file)){
            if(currentGenNumber == generations - 1) {
                file = enhance + "data64-FINALHFIT" + suffix;
            }
            else{
            file = enhance + "data64-HFIT" + suffix;
            }
        }
        else if("3".equals(file)){
            if(currentGenNumber == generations - 1) {
                file = enhance + "data2000-FINALHFIT" + suffix;
            }
            else{
            file = enhance + "data2000-HFIT" + suffix;
            }
        }
 
        String str = String.valueOf(currentGenNumber) + "," + String.valueOf(fittestIndividual);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.append(str + "\n");
        }
    }
       public static void writeAverage(String file, int numberOfGenerations, int averageFitness, int currentGenNumber, int popSize, int chromosomeSize) throws IOException {
        String suffix = numberOfGenerations + "-" + popSize + "-" + chromosomeSize + ".csv";
        
        if("1".equals(file)){
            file = enhance + "data32-AVFIT" + suffix;
        }
        else if("2".equals(file)){
            file = enhance + "data64-AVFIT" + suffix;
        }
        else if("3".equals(file)){
            file = enhance + "data2000-AVFIT" + suffix;
        }
 
        String str = String.valueOf(currentGenNumber) + "," + String.valueOf(averageFitness);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.append(str + "\n");
        }
    }
        public static void writeLowest(String file, int numberOfGenerations, int lowestFitness, int currentGenNumber, int popSize, int chromosomeSize) throws IOException {
        String suffix = numberOfGenerations + "-" + popSize + "-" + chromosomeSize + ".csv";
        
        if("1".equals(file)){
            file = enhance + "data32-LOFIT" + suffix;
        }
        else if("2".equals(file)){
            file = enhance + "data64-LOFIT" + suffix;
        }
        else if("3".equals(file)){
            file = enhance + "data2000-LOFIT" + suffix;
        }
 
        String str = String.valueOf(currentGenNumber) + "," + String.valueOf(lowestFitness);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.append(str + "\n");
        }
    }

      


}