package fr.utbm.tx52.fatools.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fr.utbm.tx52.fatools.constructs.AbstractFANode;
import fr.utbm.tx52.fatools.constructs.FAEdge;
import fr.utbm.tx52.fatools.constructs.FANodeType;
import fr.utbm.tx52.fatools.constructs.FAState;
import fr.utbm.tx52.fatools.constructs.FiniteAutomata;

public class Simulator {

	private String simulationString;
	private FiniteAutomata simulationGraph;
	private List<AbstractFANode> activeStates;
	private boolean started = false;

	public Simulator() {}

	public Simulator(FiniteAutomata graph) {
		simulationGraph=graph;
		simulationString="";
		activeStates = new ArrayList<AbstractFANode>();
		reset();
	}

	public void reset() {
		activeStates.clear();
		started=false;
		Set<AbstractFANode> nodes = simulationGraph.getNodes();
		for(AbstractFANode node : nodes) {
			if(node.getType()==FANodeType.START_STATE) {
				activeStates.add(node);
				node.setActive(true);
				node.setFailed(false);
			}
			if(node.getType()==FANodeType.STATE) {
				node.setActive(false);
				((FAState)node).setSuccessfull(false);
			}
			node.setFailed(false);
		}
	}

	public void setSimulationString(String s) {
		simulationString=s;
	}

	public boolean emptyClosure() {

		boolean activeStateAdded = false;

		List<AbstractFANode> currentStates = new ArrayList<AbstractFANode>(activeStates);
		for(AbstractFANode state : currentStates) {

			Iterable<FAEdge> edges = state.getOutgoingEdges();

			for(FAEdge edge : edges) {
				if(edge.getLabel()==null || edge.getLabel().isEmpty()) {
					AbstractFANode node = edge.getEndAnchor().getNode();
					if(!activeStates.contains(node)) {
						node.setActive(true);
						activeStates.add(node);
						activeStateAdded = true;
					}
				}
			}

		}

		return activeStateAdded;

	}

	private boolean processStartState() {

		List<AbstractFANode> currentStates = new ArrayList<AbstractFANode>(activeStates);
		for(AbstractFANode state : currentStates) {

			if(state.getType()==FANodeType.START_STATE) {

				Iterable<FAEdge> edges = state.getOutgoingEdges();

				for(FAEdge edge : edges) {

					AbstractFANode node = edge.getEndAnchor().getNode();

					if(node.getType()==FANodeType.START_STATE) {
						state.setFailed(true);
						node.setFailed(true);
						return false;
					}

					node.setActive(true);
					activeStates.add(node);
				}



			}
			else {
				((FAState)state).hasFailed();
				return false;
			}

			state.setActive(false);
			activeStates.remove(state);
		}
		return true;

	}

	public void processState()
	{
		List<AbstractFANode> currentStates = new ArrayList<AbstractFANode>(activeStates);
		for(AbstractFANode state : currentStates) {

			activeStates.remove(state);
			state.setActive(false);

			Iterable<FAEdge> edges = state.getOutgoingEdges();

			for(FAEdge edge : edges) {
				if(edge.getLabel()!=null && !edge.getLabel().isEmpty()) 
				{
					String label = edge.getLabel();
					if(label.length()==1){
						if(simulationString.charAt(0)==label.charAt(0)){

							AbstractFANode node = edge.getEndAnchor().getNode();

							node.setActive(true);
							if(!activeStates.contains(node))
								activeStates.add(node);
						}
					}
				}

			}



		}

		currentStates.clear();

	}

	public boolean runStepByStepSimulation() {

		if(started==false) {
			if(processStartState()==true) {
				started=true;
				return true;
			}
			else {
				return false;
			}
		}
		else {
			if(!simulationString.isEmpty()) {
				if(emptyClosure()) {
					return true;
				}
				processState();
				simulationString=simulationString.substring(1,simulationString.length());
				if (activeStates.isEmpty()) {
					for(AbstractFANode state : simulationGraph.getNodes())
						state.setFailed(true);
					return false;
				}
				return true;
			}
			else {
				for(AbstractFANode node : activeStates) {

					if(node.getType()==FANodeType.STATE)
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

}
