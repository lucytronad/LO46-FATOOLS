package fr.utbm.tx52.fatools.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fr.utbm.tx52.fatools.constructs.AbstractFANode;
import fr.utbm.tx52.fatools.constructs.FAEdge;
import fr.utbm.tx52.fatools.constructs.FANodeType;
import fr.utbm.tx52.fatools.constructs.FAStartState;
import fr.utbm.tx52.fatools.constructs.FAState;
import fr.utbm.tx52.fatools.constructs.FiniteAutomata;

public class Simulator {

	private String simulationString;
	private FiniteAutomata simulationGraph;
	private List<AbstractFANode> activeStates;
	private List<FAState> currentStates;

	public Simulator() {}

	public Simulator(FiniteAutomata graph) {
		simulationGraph=graph;
		simulationString="";
		activeStates = new ArrayList<AbstractFANode>();
		currentStates = new ArrayList<FAState>();
		reset();
	}

	public void reset() {
		activeStates.clear();
		Set<AbstractFANode> nodes = simulationGraph.getNodes();
		for(AbstractFANode node : nodes) {
			if(node.getType()==FANodeType.START_STATE) {
				activeStates.add(node);
				node.setActive(true);
			}
			if(node.getType()==FANodeType.STATE) {
				node.setActive(false);
				((FAState)node).setFailed(false);
				((FAState)node).setSuccessfull(false);
			}
		}
	}

	public void setSimulationString(String s) {
		simulationString=s;
	}

	private void processStartState(FAStartState state) {

		if(state.getType()==FANodeType.START_STATE) {

			Iterable<FAEdge> edges = state.getOutgoingEdges();

			for(FAEdge edge : edges) {

				state.setActive(false);
				activeStates.remove(state);

				AbstractFANode node = edge.getEndAnchor().getNode();

				node.setActive(true);
				activeStates.add(node);

				currentStates.add((FAState)node);
			}

		}

	}

	public void processState(FAState state)
	{
		if(!(state.hasOutgoingEdges()) && state.getType()==FANodeType.STATE) {
			if (!(((FAState)state).isAccepting())) {
				((FAState)state).setFailed(true);
			}
		}

		Iterable<FAEdge> edges = state.getOutgoingEdges();
		
		for(FAEdge edge : edges) {
			
			if(edge.getLabel()==null || edge.getLabel().isEmpty()) {

				activeStates.remove(state);
				state.setActive(false);

				AbstractFANode node = edge.getEndAnchor().getNode();

				if(currentStates.contains(node)) {
					((FAState)node).setFailed(true);
				}
				else
				{
					node.setActive(true);
					activeStates.add(node);

					currentStates.add((FAState)node);

					processState((FAState)node);
				}
			}
			else //the edge has a label and this is not a FAStartState
			{
				String label = edge.getLabel();
				if(label.length()!=1 || simulationString.charAt(0)!=label.charAt(0)) {
					((FAState)state).setFailed(true);
				}
				else {

					activeStates.remove(state);
					state.setActive(false);

					AbstractFANode node = edge.getEndAnchor().getNode();

					node.setActive(true);
					activeStates.add(node);
				}
			}
		}
	}

	public boolean runStepByStepSimulation() {

		if(!simulationString.isEmpty()) {
			
			for(AbstractFANode state : activeStates) {
				
				if(state.getType()==FANodeType.START_STATE)
				{
					processStartState((FAStartState)state);
				}
				else {
					processState((FAState)state);
					simulationString=simulationString.substring(1,simulationString.length());
					currentStates.clear();
				}
				
			}

			return true;
		}
		else {
			for(AbstractFANode node : simulationGraph.getNodes()) {

				if(node.isActive() && node.getType()==FANodeType.STATE)
				{
					if(((FAState)node).isAccepting())
						((FAState)node).setSuccessfull(true);
					else
						((FAState)node).setFailed(true);
				}
			}

			return false;
		}		

	}

}
