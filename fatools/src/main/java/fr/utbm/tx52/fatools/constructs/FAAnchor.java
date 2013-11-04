package fr.utbm.tx52.fatools.constructs;

import org.arakhne.neteditor.formalism.standard.StandardAnchor;

public class FAAnchor extends StandardAnchor<FiniteAutomata, AbstractFANode, FAAnchor, FAEdge> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3771060287912805512L;

	public FAAnchor() {

	}

	public FANodeType getNodeType() {
		return getNode().getType();
	}

	@Override
	public boolean canConnectAsEndAnchor(FAEdge edge, FAAnchor startAnchor) {
		switch(getNodeType()) {
		case START_STATE:
			return false;
		case STATE:
			return true;
		default:
			throw new IllegalStateException();
		}
	}

	@Override
	public boolean canConnectAsStartAnchor(FAEdge edge, FAAnchor endAnchor) {
		return true;
	}
}
