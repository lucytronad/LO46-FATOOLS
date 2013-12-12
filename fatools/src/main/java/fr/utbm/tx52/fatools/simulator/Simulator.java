package fr.utbm.tx52.fatools.simulator;

import java.util.List;

import fr.utbm.tx52.fatools.constructs.FAState;
import fr.utbm.tx52.fatools.constructs.FiniteAutomata;

public class Simulator {
	
	private String simulationString;
	private FiniteAutomata simulationGraph;
	private List<FAState> actifStates;
	
	public Simulator() {
		simulationGraph=null;
	}
	
	public Simulator(FiniteAutomata graph) {
		simulationGraph=graph;
	}
	
	public void setSimulationString(String s) {
		simulationString=s;
	}
	
	public boolean runStepByStepSimulation() {
		return false;
		
	}
	
	public boolean runGlobalSimulation() {
		return false;
		
	}

}
