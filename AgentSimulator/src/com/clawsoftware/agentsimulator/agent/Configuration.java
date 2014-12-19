package com.clawsoftware.agentsimulator.agent;

import com.clawsoftware.agentsimulator.Misc.Log;
import com.clawsoftware.agentsimulator.Misc.Misc;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;


/**
 *
 * This class contains all variables that define the parameters of a single
 * experiment. All variables can only be changed once!
 * 
 * @author Clemens Lode, clemens at lode.de, University Karlsruhe (TH)
 */
public class Configuration {

    /**
     * quadratic descent of reward for LCS
     */
    private static boolean useQuadraticReward;

    /**
     * The number of experiments that are calculated for each configuration
     */
    private static int numberOfExperiments = 10;

    /**
     * The number of problems that a single population of agents should be tested on
     */
    private static int numberOfProblems = 10;

    /**
     * The maximal number of steps executed in one trial in a multi-step problem.
     */
    private static int numberOfSteps = 500;

    /**
     * Grid size X
     */
    private static int maxX = 16;
    private static int halfMaxX = 8;

    /**
     * Grid size Y
     */
    private static int maxY = 16;
    private static int halfMaxY = 8;

    /**
     * Maximal distance to the goal agent where the agent gets rewarded
     */
    private static double rewardDistance = 3.0;

    /**
     * Sight range of the agents' sensors
     */
    private static double sightRange = 5.0;

    /**
     * Number of agents on the grid
     */
    private static int maxAgents = 16;

    /**
     * Memory of each agent, determines when to cancel the search for an event and reward the last action sets with 1 or 0
     */
    private static int maxStackSize = 128;

    /**
     * The probability of using a don't care symbol in an allele when covering
     */
    private static double coveringWildcardProbability = 0.1;

    /**
     * The probability for tournament selection
     */
    private static double tournamentProbability = 0.95;

    /**
     * Type of scenario we want to test
     * @see #RANDOM_SCENARIO
     * @see #MAZE_SCENARIO
     * @see #NON_TORUS_SCENARIO
     * @see #PILLAR_SCENARIO
     * @see #CROSS_SCENARIO
     * @see #ROOM_SCENARIO
     * @see #DIFFICULT_SCENARIO
     */
    private static int scenarioType = 0;

    public final static int RANDOM_SCENARIO = 0;
    public final static int MAZE_SCENARIO = 1;
    public final static int NON_TORUS_SCENARIO = 2;
    public final static int PILLAR_SCENARIO = 3;
    public final static int CROSS_SCENARIO = 4;
    public final static int ROOM_SCENARIO = 5;
    public final static int DIFFICULT_SCENARIO = 6;

    /**
     * start with maxPopSize random classifiers or with the empty set?
     */
    private static boolean randomStart = true;

    /**
     * use evolutionary algorithm on the action sets
     */
    private static boolean doEvolutionaryAlgorithm = true;

    /**
     * Percentage of the grid that should be filled with obstacles in the "Random Scenario"
     */
    private static double obstaclePercentage = 0.2;

    /**
     * Probability that two obstacles are connected in the random scenario
     */
    private static double obstacleConnectionFactor = 0.1;

    /**
     * The experience of a classifier required to be a subsumer
     */
    private static double thetaSubsumer = 20.0;

    /**
     * The error threshold (prediction error) under which the accuracy of a classifier is set to one and the classifier is used as a possible subsumer
     */
    private static double epsilon0 = 10.0;

    /**
     * The learning rate for updating fitness, prediction, prediction error, 
     * and action set size estimate in XCS's classifiers.
     */
    private static double beta = 0.1;

    /**
     * init value for the prediction of a classifier
     */
    private static double predictionInitialization = 0.1;

    /**
     * whether to use the average of all prediction values instead of a static value
     */
    private static boolean predictionInitializationAdaption = true;

    /**
     * init value for the prediction error of a classifier
     */
    private static double predictionErrorInitialization = 0.1;

    /**
     * init value for the fitness of a classifier
     */
    private static double fitnessInitialization = 0.5;

    /**
     * The maximal number of (micro-)classifiers of one rule set of an agent
     * If the maximum number is reached when a classifier was inserted determine which micro-classifier to delete
     * A deletion can be a removal of a whole classifier with numerosity = 1 or the decrease of the numberosity of a classifier by 1
     */
    private static int maxPopSize = 800;

    /**
     * The fraction of the mean fitness of the population below which the fitness of a classifier may be considered 
     * in its vote for deletion.
     */
    private static double delta = 0.1;

    /**
     * Specified the threshold over which the fitness of a classifier may be considered in its deletion probability.
     */
    private static double thetaDel = 20;

    /**
     * Determines whether to subsume the action set
     */
    private static boolean doActionSetSubsumption = false;

    /**
     * The fall of rate in the fitness evaluation
     */
    private static double alpha = 0.1;

    /**
     * The discount rate in multi-step problems.
     */
    private static double gamma = 0.95;

    /**
     * Specifies the exponent in the power function for the fitness evaluation
     */
    private static double nu = 0.1;

    /**
     * The threshold for the GA application in an action set (time between GA runs)
     */
    private static double thetaGA = 25.0;

    /**
     * The reduction of the prediction error when generating an offspring classifier
     */
    private static double predictionErrorReduction = 0.1;

    /**
     * The reduction of the fitness when generating an offspring classifier
     */
    private static double fitnessReduction = 0.1;

    /**
     * The probability of mutating one allele and the action in an offspring classifier
     */
    private static double mutationProbability = 0.1;

    /**
     * Whether to subsume new classifiers generated by the genetic algorithm
     */
    private static boolean doGASubsumption = true;

    private static boolean gifOutput = false;

    /**
     * Exploration probability modes:
     * - no exploration (0/100) (0) 
     * - always exploration (100/0) (1)
     * - switch between explore and exploit (50/50) (3)
     * - first explore, then exploit (50/50) (2)
     * - linear reduction of exploration probability (100..0/0..100)
     */


    public static final int RANDOM_ROULETTE_SELECTION_MODE = 0;
    public static final int RANDOM_TOURNAMENT_SELECTION_MODE = 1;
    public static final int RANDOM_BEST_SELECTION_MODE = 2;
    public static final int ROULETTE_TOURNAMENT_SELECTION_MODE = 3;
    public static final int ROULETTE_BEST_SELECTION_MODE = 4;
    public static final int TOURNAMENT_BEST_SELECTION_MODE = 5;

    public static final int SWITCH_GOAL_OBS_MODE = 0;
    public static final int SWITCH_GOAL_SIGHT_MODE = 1;
    public static final int SWITCH_REWARD_MODE = 2;
    public static final int SWITCH_NO_MODE = 3;

    public static final int GOAL_OBS_MODE = 0;
    public static final int GOAL_SIGHT_MODE = 1;
    public static final int GOAL_OBS_AGENTS_OBS_MODE = 2;
    public static final int GOAL_OBS_AGENTS_SIGHT_MODE = 3;
    public static final int GOAL_SIGHT_AGENTS_OBS_MODE = 4;
    public static final int GOAL_SIGHT_AGENTS_SIGHT_MODE = 5;


    /**
     * When to explore and when to exploit the classifiers
     */
    private static int explorationMode = 0;

    private static int switchMode = 0;

    private static int goalMode = 0;

    /**
     * Type of goal agent movement
     */
    private static int goalAgentMovementType = 0;

    /**
     * Total random goal agent movement (jumps to a free cell)
     */
    public static final int TOTAL_RANDOM_MOVEMENT = 0;

    /**
     * Random goal agent movement (neighboring cell)
     */
    public static final int RANDOM_MOVEMENT = 1;

    /**
     * Random goal agent movement, direction changes by max 1
     */
    public static final int RANDOM_DIRECTION_CHANGE = 2;

    /**
     * Intelligent goal agent movement (tend to move away from nearby agents), random if no agents in sight
     */
    public static final int INTELLIGENT_MOVEMENT = 3;

    /**
     * Goal agent movement always in the same direction as last movement
     */
    public static final int ALWAYS_SAME_DIRECTION = 4;

    /**
     * Goal agent with XCS learning classifier system
     */
    public static final int XCS_MOVEMENT = 5;

    /**
     * Number of times the goal agent executes its movement type
     */
    private static double goalAgentMovementSpeed = 1.5;

    /**
     * Random walk agents
     */
    public static final int RANDOMIZED_MOVEMENT_AGENT_TYPE = 0;

    /**
     * Agent with random walk that moves into the direction of the goal object if it is in sight
     */
    public static final int STATIC_AI_AGENT_TYPE = 1;

    /**
     * agent with DSXCS algorithm
     */
    public static final int DSXCS_AGENT_TYPE = 3;

    /**
     * agent with SXCS algorithm
     */
    public static final int SXCS_AGENT_TYPE = 4;

    /**
     * agent with a standard XCS implementation
     */
    public static final int STANDARD_XCS_AGENT_TYPE = 5;

    /**
     * agent with a standard XCS implementation with events
     */
    public static final int EVENT_XCS_AGENT_TYPE = 6;

    /**
     * Type of agent to test
     */
    private static int agentType = DSXCS_AGENT_TYPE;

    /**
     * starting random seed
     */
    private static long randomSeed = 0;

    /**
     * How other agents should be rewarded
     * @see #NO_EXTERNAL_REWARD
     * @see #REWARD_ALL_EQUALLY
     * @see #REWARD_COMPLEX
     * @see #REWARD_EGOISM
     * @see #REWARD_NEW
     * @see #REWARD_SIMPLE
     */
    private static int externalRewardMode = 0;

    public static final int NO_EXTERNAL_REWARD = 0;
    public static final int REWARD_ALL_EQUALLY = 1;
    public static final int REWARD_SIMPLE = 2;
    public static final int REWARD_COMPLEX = 3;
    public static final int REWARD_NEW = 4;
    public static final int REWARD_EGOISM = 5;


    public static void copyConfigFile(String file_name) throws Exception {
        File out_file = new File(Misc.getBaseFileName("config") + ".txt");
        File my_file = new File(file_name);
        if (my_file.exists()) {
            FileInputStream fis = new FileInputStream(my_file);
            FileOutputStream fos = new FileOutputStream(out_file);
            byte[] buf = new byte[1024];
            int i = 0;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
            fis.close();
            fos.close();
        } else {
            throw new Exception("File " + file_name + " not found.");
        }
    }

    /**
     * @return unique id in order to properly sort and compare results
     */
    public static BigInteger getProblemID() {
        BigInteger i = BigInteger.ZERO;
        i = i.add(BigInteger.valueOf(maxX));
        i = i.multiply(BigInteger.valueOf(128));
        i = i.add(BigInteger.valueOf(maxY));
        i = i.multiply(BigInteger.valueOf(4));
        i = i.add(BigInteger.valueOf(scenarioType));
        if(scenarioType == 0) {
            i = i.multiply(BigInteger.valueOf(11));
            i = i.add(BigInteger.valueOf((int)(obstaclePercentage*10.0)));
            i = i.multiply(BigInteger.valueOf(11));
            i = i.add(BigInteger.valueOf((int)(obstacleConnectionFactor*10.0)));
        }
        i = i.multiply(BigInteger.valueOf(16));
        i = i.add(BigInteger.valueOf((int)rewardDistance));
        i = i.multiply(BigInteger.valueOf(128));
        i = i.add(BigInteger.valueOf(maxAgents));
        i = i.multiply(BigInteger.valueOf(8));
        i = i.add(BigInteger.valueOf(goalAgentMovementType));
        i = i.multiply(BigInteger.valueOf(8));
        i = i.add(BigInteger.valueOf((int)(goalAgentMovementSpeed*10.0)));
        i = i.multiply(BigInteger.valueOf(8096));
        i = i.add(BigInteger.valueOf(numberOfSteps)); 
        return i;
    }

    /**
     * @param file_name Initialize configuration with values from file_name
     * @throws java.lang.Exception If there was an error reading from the file
     */
    public static void initialize(String file_name) throws Exception {
        // load old settings if file exists
        File my_file = new File(file_name);
        if (my_file.exists()) {

                BufferedReader p = new BufferedReader(new FileReader(my_file.getAbsoluteFile()));

                randomSeed = Long.valueOf(p.readLine());
                numberOfExperiments = Integer.valueOf(p.readLine());
                numberOfProblems = Integer.valueOf(p.readLine());
                numberOfSteps = Integer.valueOf(p.readLine());
                gifOutput = Boolean.valueOf(p.readLine());

                maxPopSize = Integer.valueOf(p.readLine());

                useQuadraticReward = Boolean.valueOf(p.readLine());

                maxX = Integer.valueOf(p.readLine());
                halfMaxX = maxX / 2;
                maxY = Integer.valueOf(p.readLine());
                halfMaxY = maxY / 2;
                scenarioType = Integer.valueOf(p.readLine());

                obstaclePercentage = Double.valueOf(p.readLine());

                obstacleConnectionFactor = Double.valueOf(p.readLine());

                rewardDistance = Double.valueOf(p.readLine());
                sightRange = Double.valueOf(p.readLine());

                maxAgents = Integer.valueOf(p.readLine());

                // number of steps for multi step problem
                maxStackSize = Integer.valueOf(p.readLine());

                coveringWildcardProbability = Double.valueOf(p.readLine());
                tournamentProbability = Double.valueOf(p.readLine());
                randomStart = Boolean.valueOf(p.readLine());
                doEvolutionaryAlgorithm = Boolean.valueOf(p.readLine());

                thetaSubsumer = Double.valueOf(p.readLine());
                epsilon0 = Double.valueOf(p.readLine());
                beta = Double.valueOf(p.readLine());

                predictionInitialization = Double.valueOf(p.readLine());
//                predictionInitializationAdaption = Boolean.valueOf(p.readLine());

                predictionErrorInitialization = Double.valueOf(p.readLine());
                fitnessInitialization = Double.valueOf(p.readLine());

                delta = Double.valueOf(p.readLine());
                thetaDel = Double.valueOf(p.readLine());

                doActionSetSubsumption = Boolean.valueOf(p.readLine());

                alpha = Double.valueOf(p.readLine());
                gamma = Double.valueOf(p.readLine());
                nu = Double.valueOf(p.readLine());
                thetaGA = Double.valueOf(p.readLine());

                predictionErrorReduction = Double.valueOf(p.readLine());
                fitnessReduction = Double.valueOf(p.readLine());
                mutationProbability = Double.valueOf(p.readLine());

                doGASubsumption = Boolean.valueOf(p.readLine());
                explorationMode = Integer.valueOf(p.readLine());
                switchMode = Integer.valueOf(p.readLine());
                goalMode = Integer.valueOf(p.readLine());

                goalAgentMovementType = Integer.valueOf(p.readLine());

                goalAgentMovementSpeed = Double.valueOf(p.readLine());

                agentType = Integer.valueOf(p.readLine());

                externalRewardMode = Integer.valueOf(p.readLine());
                p.close();
        } else {
            throw new Exception("Config file " + file_name + " not found.");
        }
    }

   /**
     * @return The number of experiments that are calculated for each configuration
     */
    public static int getNumberOfExperiments() {
        return numberOfExperiments;
    }

    /**
     * @return The number of problems that a single population of agents should be tested on
     */
    public static int getNumberOfProblems() {
        return numberOfProblems;
    }

    /**
     * @return The maximal number of steps executed in one trial in a multi-step problem.
     */
    public static int getNumberOfSteps() {
        return numberOfSteps;
    }

    /**
     * @return Whether to subsume new classifiers generated by the genetic algorithm
     */
    public static boolean isDoGASubsumption() {
        return doGASubsumption;
    }

    public static double getMutationProbability() {
        return mutationProbability;
    }

    public static double getThetaGA() {
        return thetaGA;
    }

    /**
     * @return The fall of rate in the fitness evaluation
     */
    public static double getAlpha() {
        return alpha;
    }

    /**
     * @return The discount rate in multi-step problems.
     */
    public static double getGamma() {
        return gamma;
    }

    /**
     * @return The exponent in the power function for the fitness evaluation
     */
    public static double getNu() {
        return nu;
    }

    public static boolean isDoActionSetSubsumption() {
        return doActionSetSubsumption;
    }

     /**
     * @return The fraction of the mean fitness of the population below which the fitness of a classifier may be considered in its vote for deletion.
     */
    public static double getDelta() {
        return delta;
    }

    /**
     * @return The threshold over which the fitness of a classifier may be considered in its deletion probability.
     */
    public static double getThetaDel() {
        return thetaDel;
    }

    /**
     * @return The maximal number of (micro-)classifiers of one rule set of an agent
     * If the maximum number is reached when a classifier was inserted determine which micro-classifier to delete
     * A deletion can be a removal of a whole classifier with numerosity = 1 or the decrease of the numberosity of a classifier by 1
     */
    public static int getMaxPopSize() {
        return maxPopSize;
    }

    /**
     * @return init value for the prediction of a classifier
     */
    public static double getPredictionInitialization() {
        return predictionInitialization;
    }

    /**
     * @return init value for the prediction error of a classifier
     */
    public static double getPredictionErrorInitialization() {
        return predictionErrorInitialization;
    }

    /**
     * @return init value for the fitness of a classifier
     */
    public static double getFitnessInitialization() {
        return fitnessInitialization;
    }

    /**
     * @return The learning rate for updating fitness, prediction, prediction error, and action set size estimate in XCS's classifiers.
     */
    public static double getBeta() {
        return beta;
    }

    /**
     * @return The experience of a classifier required to be a subsumer
     */
    public static double getThetaSubsumer() {
        return thetaSubsumer;
    }

    /**
     * @return The error threshold (prediction error) under which the accuracy of a classifier is set to one and the classifier is used as a possible subsumer
     */
    public static double getEpsilon0() {
        return epsilon0;
    }

    /**
     * @return X dimension of the grid
     */
    public static int getMaxX() {
        return maxX;
    }

    /**
     * @return half X dimension of the grid
     */
    public static int getHalfMaxX() {
        return halfMaxX;
    }

    /**
     * @return Y dimension of the grid
     */
    public static int getMaxY() {
        return maxY;
    }

    /**
     * @return half Y dimension of the grid
     */
    public static int getHalfMaxY() {
        return halfMaxY;
    }

    /**
     * @return maximal surveillance range
     */
    public static double getRewardDistance() {
        return rewardDistance;
    }

    /**
     * @return maximal sight range
     */
    public static double getSightRange() {
        return sightRange;
    }

    /**
     * @return number of agents in the grid
     */
    public static int getMaxAgents() {
        return maxAgents;
    }

    /**
     * @return size of the stack of SXCS agents
     */
    public static int getMaxStackSize() {
        return maxStackSize;
    }

    /**
     * @return probability for wild cards in the classifier
     */
    public static double getCoveringWildcardProbability() {
        return coveringWildcardProbability;
    }

    /**
     * @return probability value for the tournament selection method
     */
    public static double getTournamentProbability() {
        return tournamentProbability;
    }

    /**
     * @return true if an evolutionary algorithm should be used
     */
    public static boolean isDoEvolutionaryAlgorithm() {
        return doEvolutionaryAlgorithm;
    }

    /**
     * @return the factor of the reduction of the prediction error value
     */
    public static double getPredictionErrorReduction() {
        return predictionErrorReduction;
    }

    /**
     * @return the factor of the reduction of the fitness value
     */
    public static double getFitnessReduction() {
        return fitnessReduction;
    }

    /**
     * @return the type of movement of the goal object
     * @see
     */
    public static int getGoalAgentMovementType() {
        return goalAgentMovementType;
    }

    public static double getGoalAgentMovementSpeed() {
        return goalAgentMovementSpeed;
    }

    /**
     * @return number of problems times number of steps (number of problems multiplicated with number of steps)
     */
    public static int getTotalTimeSteps() {
        return getNumberOfProblems() * getNumberOfSteps();
    }

    /**
      * @return The agent type
        @see #RANDOMIZED_MOVEMENT_AGENT_TYPE
        @see #STATIC_AI_AGENT_TYPE
        @see #DSXCS_AGENT_TYPE
        @see #SXCS_AGENT_TYPE
        @see #STANDARD_XCS_AGENT_TYPE
        @see #EVENT_XCS_AGENT_TYPE
     */
    public static int getAgentType() {
        return agentType;
    }

    public static long getRandomSeed() {
        return randomSeed;
    }

    public static int getExplorationMode() {
        return explorationMode;
    }

    public static int getSwitchMode() {
        return switchMode;
    }

    public static int getGoalMode() {
        return goalMode;
    }

    public static double getObstaclePercentage() {
        return obstaclePercentage;
    }

    public static double getObstacleConnectionFactor() {
        if(obstacleConnectionFactor == 0.0) {
            return 0.01;
        }
        return obstacleConnectionFactor;
    }

    public static int getExternalRewardMode() {
        return externalRewardMode;
    }

    /**
     * @return the scenarioType
     */
    public static int getScenarioType() {
        return scenarioType;
    }

    /**
     * @return the gifOutput
     */
    public static boolean isGifOutput() {
        return gifOutput;
    }

    /**
     * @return the useQuadraticReward
     */
    public static boolean isUseQuadraticReward() {
        return useQuadraticReward;
    }

    /**
     * @return the randomStart
     */
    public static boolean isRandomStart() {
        return randomStart;
    }

    /**
     * @return the predictionInitializationAdaption
     */
    public static boolean isPredictionInitializationAdaption() {
        return predictionInitializationAdaption;
    }
}
