package com.clawsoftware.agentsimulator.lcs;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.clawsoftware.agentsimulator.Misc.Log;
import com.clawsoftware.agentsimulator.Misc.Misc;
import com.clawsoftware.agentsimulator.agent.Configuration;
import com.clawsoftware.agentsimulator.agent.Sensors;

/**
 * Main class for the XCS Classifier
 *
 * @author Clemens Lode, clemens at lode.de, University Karlsruhe (TH)
 */
public class Classifier {

	/**
	 * Difference between the actual reward and the predicted payoff
	 */
	private double predictionError;
	/**
	 * predicted payoff
	 */
	private double prediction;

	/**
	 * Number of time this classifier was updated
	 */
	private double experience = 0.0;
	/**
	 * The action set size estimate of the classifier.
	 */
	private double actionSetSize;
	/**
	 * The timestamp a genetic algorithm was executed on an action set this
	 * classifier was part of
	 */
	private long gaTimestamp;
	/**
	 * Accuracy of the classifier
	 */
	private double fitness;
	/**
	 * Action to take when the condition matches. Depends on the rotation of the
	 * condition.
	 */
	private final Action action;
	/**
	 * The condition that has to match the current state in order for the
	 * classifier to be selected / activated
	 */
	private final Condition condition;
	/**
	 * The number of micro-classifiers
	 */
	private int numerosity = 1;
	/**
	 * list of all classifier sets that contain this classifier, important to
	 * keep track of the numerosity sum
	 */
	private final ArrayList<ClassifierSet> parents = new ArrayList<ClassifierSet>(
			1 + Configuration.getMaxStackSize());

	// nicht set_size! Ansonsten sind die ActionSetSizes der ersten Classifier
	// 1, 2, 3, 4, 5 statt 5, 5, 5, 5, 5
	// Action set size wird seperat zugewiesen, sobald die Zahl der
	// hinzugefügten Classifier bekannt sind
	/**
	 * Create a new classifier that covers the state and action
	 * 
	 * @param state
	 *            The current state
	 * @param action
	 *            The action this classifier should cover
	 * @param gaTimestamp
	 *            The current time step
	 * @param action_set_size
	 *            Size of the action set
	 * @see MainClassifierSet#coverAllValidActions
	 * @throws java.lang.Exception
	 *             if there was an error setting prediction, fitness or action
	 *             set size
	 */
	public Classifier(final Sensors state, final Action action,
			final long gaTimestamp, final double action_set_size,
			final double average_prediction) throws Exception {
		setGaTimestamp(gaTimestamp);

		setPrediction(average_prediction);
		setPredictionError(Configuration.getPredictionErrorInitialization());
		setFitness(Configuration.getFitnessInitialization());
		// will later (in the actionClassifierSet) be resetted to the actual
		// value
		setActionSetSize(action_set_size);

		this.condition = new Condition(state);
		this.action = new Action(action);
	}

	/**
	 * Constructs an identical XClassifier. However, the experience of the copy
	 * is set to 0 and the numerosity is set to 1 since this is indeed a new
	 * individual in a population.
	 * 
	 * @param old_classifier
	 *            The classifier we want to copy
	 */
	public Classifier(final Classifier old_classifier) throws Exception {

		setGaTimestamp(old_classifier.gaTimestamp);
		condition = old_classifier.condition.clone();
		action = new Action(old_classifier.action);

		prediction = old_classifier.prediction;
		predictionError = old_classifier.predictionError;
		// Here we should divide the fitness by the numerosity to get a accurate
		// value for the new one!
		setFitness(old_classifier.getFitness() / old_classifier.getNumerosity());
		actionSetSize = old_classifier.actionSetSize;
	}

	/**
	 * generate a random classifier
	 * 
	 * @throws java.lang.Exception
	 *             if there was an error setting prediction, fitness or action
	 *             size
	 * @see MainClassifierSet#MainClassifierSet(int)
	 */
	public Classifier() throws Exception {
		setGaTimestamp(0);

		setPrediction(Configuration.getPredictionInitialization());
		setPredictionError(Configuration.getPredictionErrorInitialization());
		setFitness(Configuration.getFitnessInitialization());
		// will later (in the actionClassifierSet) be resetted to the actual
		// value
		setActionSetSize(Configuration.getMaxPopSize() / Action.MAX_DIRECTIONS);

		this.condition = new Condition();
		this.action = new Action(Misc.nextInt(Action.MAX_DIRECTIONS));

	}

	public Classifier clone(final ClassifierSet cs) throws Exception {
		final Classifier new_cl = new Classifier(this);
		new_cl.setNumerosity(getNumerosity());
		new_cl.experience = getExperience();
		new_cl.addParent(cs);
		return new_cl;
	}

	/**
	 * Crossing over with one or two points depending if there are obstacle
	 * sensors
	 * 
	 * @param childA
	 *            First child with parental genetic data
	 * @param childB
	 *            Second child with parental genetic data
	 */
	public static void crossOverClassifiers(final Classifier childA,
			final Classifier childB) {
		// combine condition and action parts of classifier to form strings
		final int[] childAStr = childA.getCondition().getData();
		final int[] childBStr = childB.getCondition().getData();

		// two fixed crossover points, dividing goal agent, agents and obstacles
		final int crossoverIndex1 = Condition.AGENT_DISTANCE_INDEX;
		final int crossoverIndex2 = Condition.OBSTACLE_DISTANCE_INDEX;

		// do the crossover
		final int[] newChildAStr = new int[childAStr.length];
		final int[] newChildBStr = new int[childBStr.length];

		switch (Misc.nextInt(3)) {
		case 0:
			for (int i = crossoverIndex1; i < crossoverIndex2; i++) {
				newChildAStr[i] = childBStr[i];
				newChildBStr[i] = childAStr[i];
			}
			for (int i = crossoverIndex2; i < childAStr.length; i++) {
				newChildAStr[i] = childAStr[i];
				newChildBStr[i] = childBStr[i];
			}
			break;
		case 1:
			for (int i = crossoverIndex1; i < crossoverIndex2; i++) {
				newChildAStr[i] = childAStr[i];
				newChildBStr[i] = childBStr[i];
			}
			for (int i = crossoverIndex2; i < childAStr.length; i++) {
				newChildAStr[i] = childBStr[i];
				newChildBStr[i] = childAStr[i];
			}
			break;
		case 2:
			for (int i = crossoverIndex1; i < crossoverIndex2; i++) {
				newChildAStr[i] = childBStr[i];
				newChildBStr[i] = childAStr[i];
			}
			for (int i = crossoverIndex2; i < childAStr.length; i++) {
				newChildAStr[i] = childBStr[i];
				newChildBStr[i] = childAStr[i];
			}
			break;
		}
		childA.condition.setData(newChildAStr);
		childB.condition.setData(newChildBStr);
	}

	/**
	 * @param s
	 *            The current sensor state
	 * @return all absolute directions this classifier matches the sensor state
	 * @see MainClassifierSet#coverAllActions
	 * @see AppliedClassifierSet#AppliedClassifierSet
	 */
	public boolean isMatchingState(final Sensors s) {
		return condition.isMatchingState(s);
	}

	public double getEgoFactor() throws Exception {
		return getCondition().getEgoFactor(action.getDirection())
				* this.getFitness() * this.getPrediction();
	}

	/**
	 * @return true if the classifier is experienced and accurate enough
	 * @see Configuration#getThetaSubsumer
	 * @see Configuration#getEpsilon0
	 */
	public boolean isPossibleSubsumer() throws Exception {
		if (getExperience() < Configuration.getThetaSubsumer()
				|| getPredictionError() >= Configuration.getEpsilon0()) {
			return false;
		}
		return true;
	}

	/**
	 * @param c
	 *            The classifier we want to compare
	 * @return true if this classifier is a subsumer of the other classifier
	 * @see Classifier#isPossibleSubsumer
	 * @see Classifier#isMoreGeneral
	 */
	public boolean subsumes(final Classifier c) {
		if (condition.isMoreGeneral(c.getCondition())) {
			if (action.getDirection() == c.getDirection()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Test all rotations to determine (phenotype) equality
	 * 
	 * @param c
	 *            the classifier to compare to
	 * @return true if both classifiers are phenotypical equal
	 */
	public boolean equals(final Classifier c) {
		if (condition.equals(c.getCondition())) {
			if (action.getDirection() == c.getDirection()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @see Configuration#delta
	 * @param mean_fitness
	 *            The mean fitness in the population.
	 * @return Probability for deletion of the classifier.
	 * @throws java.lang.Exception
	 *             If the resulting deletion probability is out of range
	 */
	public double getDelProp(final double mean_fitness) throws Exception {
		double del_prop = 0.0;
		if (getFitness() / getNumerosity() >= Configuration.getDelta()
				* mean_fitness
				|| getExperience() < Configuration.getThetaDel()) {
			del_prop = getActionSetSize() * getNumerosity();
		} else {
			del_prop = getNumerosity() * getNumerosity() * getActionSetSize()
					* mean_fitness / getFitness();
		}
		return del_prop;
	}

	/**
	 * The accuracy is determined from the prediction error of the classifier
	 * using Wilson's power function as published in 'Get Real! XCS with
	 * continuous-valued inputs' (1999)
	 * 
	 * @return The accuracy of the classifier
	 * @see Configuration#getEpsilon0
	 * @see Configuration#getAlpha
	 * @see Configuration#getNu
	 */
	public double getAccuracy() {
		if (getPredictionError() <= Configuration.getEpsilon0()) {
			return 1.;
		} else {
			return Configuration.getAlpha()
					* Math.pow(
							getPredictionError() / Configuration.getEpsilon0(),
							-Configuration.getNu());
		}
	}

	/**
	 * Updates the fitness of the classifier according to the relative accuracy.
	 * 
	 * @param accSum
	 *            The sum of all the accuracies in the action set
	 * @param accuracy
	 *            The accuracy of the classifier.
	 * @param factor
	 *            Weight of the update
	 * @throws java.lang.Exception
	 *             if the fitness is out of range
	 * @see Configuration#beta
	 */
	public void updateFitness(final double accSum, final double accuracy,
			final double factor) throws Exception {
		try {
			setFitness(getFitness() + factor * Configuration.getBeta()
					* (accuracy * getNumerosity() / accSum - getFitness()));
		} catch (final Exception e) {
			Log.errorLog("" + accSum + " / " + accuracy + " / " + getFitness());
			throw e;
		}
	}

	/**
	 * Updates the prediction error of the classifier according to P.
	 * 
	 * @param P
	 *            The actual Q-payoff value (actual reward + max of predicted
	 *            reward in the following situation).
	 * @param factor
	 *            Weight of the update
	 * @see Configuration#beta
	 */
	public void updatePredictionError(final double P, final double factor)
			throws Exception {
		if (getExperience() < 1. / Configuration.getBeta()) {
			setPredictionError((getPredictionError() * (getExperience() - 1.0) + Math
					.abs(P - prediction)) / getExperience());
		} else {
			if (Double.isNaN(getPredictionError() + Configuration.getBeta()
					* (Math.abs(P - prediction) - getPredictionError()))) {
				throw new Exception("prediction error out of range "
						+ getPredictionError() + " * " + P + " - " + prediction);
			}
			setPredictionError(getPredictionError() + factor
					* Configuration.getBeta()
					* (Math.abs(P - prediction) - getPredictionError()));
		}
	}

	/**
	 * Updates the prediction of the classifier according to P.
	 * 
	 * @param P
	 *            The actual Q-payoff value (actual reward + max of predicted
	 *            reward in the following situation).
	 * @param factor
	 *            Weight of the update
	 * @see Configuration#beta
	 */
	public void updatePrediction(final double P, final double factor)
			throws Exception {

		if (getExperience() < 1. / Configuration.getBeta()) {
			setPrediction((prediction * (getExperience() - 1.0) + P)
					/ getExperience());
		} else {
			setPrediction(prediction + factor * Configuration.getBeta()
					* (P - prediction));
		}
	}

	/**
	 * Updates the action set size to find the average of the action set sizes
	 * this classifier is part of
	 * 
	 * @param numerosity_sum
	 *            Numerosity of the action classifier set in question
	 * @throws java.lang.Exception
	 *             If the action set size is out of bounds
	 * @see Configuration#beta
	 */
	public void updateActionSetSize(final int numerosity_sum) throws Exception {
		if (Configuration.getBeta() * getExperience() < 1.0) {
			try {
				setActionSetSize((getActionSetSize() * (getExperience() - 1.0) + numerosity_sum)
						/ getExperience());
			} catch (final Exception e) {
				throw new Exception(e + " : " + getActionSetSize() + " * ("
						+ getExperience() + " - 1.0) + " + numerosity_sum
						+ ") / " + getExperience());
			}

		} else {
			try {
				setActionSetSize(getActionSetSize() + Configuration.getBeta()
						* (numerosity_sum - getActionSetSize()));
			} catch (final Exception e) {
				throw new Exception(e + " : " + getActionSetSize() + " + "
						+ Configuration.getBeta() + " * (" + numerosity_sum
						+ " - " + getActionSetSize() + ")");
			}

		}
	}

	public void testActionSetSize(final double new_value) {
		if (actionSetSize == 0.0) {
			actionSetSize = new_value;
		}
	}

	/**
	 * @param predicted_payoff
	 *            The predicted average payoff of this classifier
	 * @throws java.lang.Exception
	 *             If the prediction is out of range
	 */
	public void setPrediction(final double predicted_payoff) throws Exception {
		if (Double.isNaN(predicted_payoff)
				|| Double.isInfinite(predicted_payoff)) {
			throw new Exception("Prediction out of range " + predicted_payoff);
		}

		// if(predicted_payoff < 0.0 || predicted_payoff > (10.0 *
		// LCS_Agent.MAX_REWARD * getNumerosity())) {
		// throw new Exception("Prediction out of range: " + predicted_payoff +
		// " [num: " + getNumerosity() + "] from " + prediction + ")");
		// }
		prediction = predicted_payoff;
	}

	/**
	 * Applies a niche mutation to the classifier. This method calls
	 * mutateCondition(state) and mutateAction(numberOfActions) and returns if
	 * at least one bit or the action was mutated.
	 * 
	 * @param state
	 *            The current state
	 * @return true if the condition was changed
	 */
	public boolean applyMutation(final Sensors state) {
		boolean changed = false;
		if (action.mutateAction()) {
			changed = true;
		}
		if (condition.mutateCondition(state)) {
			changed = true;
		}
		return changed;
	}

	/**
	 * Adds to the numerosity of the classifier.
	 * 
	 * @param num
	 *            The added numerosity (can be negative!).
	 * @see ClassifierSet#changeNumerositySum
	 * @see ClassifierSet#removeClassifier
	 */
	public void addNumerosity(final int num) throws Exception {
		final int old_num = numerosity;
		numerosity += num;
		if (numerosity == 0) {
			fitness = 0.01;
		} else if (old_num > 0) {
			setFitness(getFitness() * numerosity / old_num);
		}

		for (final ClassifierSet p : parents) {
			p.changeNumerositySum(num);
			if (numerosity == 0) {
				p.removeClassifier(this);
			}
		}
	}

	/**
	 * Register a parent to the classifier (important to update numerosity)
	 * 
	 * @param p
	 *            The parent
	 */
	public void addParent(final ClassifierSet p) throws Exception {
		parents.add(p);
		p.changeNumerositySum(getNumerosity());
	}

	/**
	 * Unregister parent from the classifier (to free resources)
	 * 
	 * @param p
	 *            The parent
	 */
	public void removeParent(final ClassifierSet p) {
		parents.remove(p);
	}

	/**
	 * @param fitness
	 *            The new fitness value
	 * @throws java.lang.Exception
	 *             If the fitness was out of range
	 */
	public void setFitness(final double fitness) throws Exception {
		if (Double.isNaN(fitness) || Double.isInfinite(fitness)) {
			throw new Exception("Fitness out of range " + fitness);
		}

		if (fitness > 0.0 && fitness <= getNumerosity()) {
			this.fitness = fitness;
			if (this.fitness < 0.01) {
				this.fitness = 0.01;
			}
		} else {
			throw new Exception("Fitness out of range: " + fitness + " [num: "
					+ getNumerosity() + "] from " + this.fitness + ")");
		}
	}

	/**
	 * @param actionSetSize
	 *            The new actionSetSize value
	 * @throws java.lang.Exception
	 *             If the actionSetSize was out of range
	 */
	public void setActionSetSize(final double actionSetSize) throws Exception {
		if (actionSetSize < 0.0
				|| actionSetSize > 100 * Configuration.getMaxPopSize()) {
			throw new Exception("Action set size out of range ("
					+ actionSetSize + ")");
		}
		this.actionSetSize = actionSetSize;
	}

	/**
	 * @param factor
	 *            Increases the Experience of the classifier by this amount
	 * @throws java.lang.Exception
	 *             if the factor is out of range
	 */
	public void increaseExperience(final double factor) throws Exception {
		if (factor < 0.0 || factor > 1.0) {
			throw new Exception("Factor out of range (" + factor + ")");
		}
		experience += factor;
	}

	public void setPredictionError(final double error) throws Exception {
		this.predictionError = error;
	}

	public void setGaTimestamp(final long gaTimestamp) {
		this.gaTimestamp = gaTimestamp;
	}

	public final int getDirection() {
		return action.getDirection();
	}

	public final Action getAction() {
		return action;
	}

	public final Condition getCondition() {
		return condition;
	}

	public double getFitness() {
		// if(Double.isNaN(fitness) || Double.isInfinite(fitness)) {
		// throw new Exception("Fitness out of range " + fitness);
		// }
		return fitness;
	}

	public double getActionSetSize() {
		return actionSetSize;
	}

	/**
	 * @return number of times the classifier was in the action set
	 */
	public double getExperience() {
		return experience;
	}

	public long getGaTimestamp() {
		return gaTimestamp;
	}

	public int getNumerosity() throws Exception {
		if (numerosity == 0) {
			throw new Exception("Numerosity == 0");
		}
		return numerosity;
	}

	public void setNumerosity(final int numerosity) throws Exception {
		if (numerosity == 0) {
			throw new Exception("Numerosity == 0");
		}
		this.numerosity = numerosity;
	}

	public double getPredictionError() {
		return predictionError;
	}

	public double getPrediction() throws Exception {
		if (Double.isNaN(prediction) || Double.isInfinite(prediction)) {
			throw new Exception("Prediction out of range " + prediction);
		}
		return prediction;
	}

	@Override
	public String toString() {
		String output = new String();
		output += condition.toString();
		try {
			output += "-";
			output += action.toString();
			output += " :";
			output += " [Fi: " + new DecimalFormat("0.00").format(getFitness())
					+ "]";
			output += " [Ex: "
					+ new DecimalFormat("00000.0").format(getExperience())
					+ "]";
			output += " [Pr: "
					+ new DecimalFormat("0.00").format(getPrediction()) + "]";
			output += " [PE: "
					+ new DecimalFormat("0.00").format(getPredictionError())
					+ "]";
			output += " [AS: "
					+ new DecimalFormat("000.0").format(getActionSetSize())
					+ "]";
			output += " [Ti: "
					+ new DecimalFormat("00000").format(getGaTimestamp()) + "]";
			output += " [Nu: "
					+ new DecimalFormat("000").format(getNumerosity()) + "]";
			output += " [Pa: "
					+ new DecimalFormat("000").format(parents.size()) + "]";
		} catch (final Exception e) {
		}

		return output;
	}
}
