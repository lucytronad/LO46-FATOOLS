package fr.utbm.tx52.fatools.constructs;

import org.arakhne.neteditor.formalism.standard.StandardMonoAnchorNode;

public abstract class AbstractFANode extends StandardMonoAnchorNode<FiniteAutomata, AbstractFANode, FAAnchor, FAEdge> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8602932924212177344L;

	public AbstractFANode() {
		this(null);
	}

	public AbstractFANode(String name) {
		super(new FAAnchor());
		setName(name);
	}

	public abstract FANodeType getType();

	protected String getActionCode(String actionName) {
		FiniteAutomata machine = getGraph();
		if (machine!=null) {
			return machine.getActionCode(actionName);
		}
		return null;
	}

}
