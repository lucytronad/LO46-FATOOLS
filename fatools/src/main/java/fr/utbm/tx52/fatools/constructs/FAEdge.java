package fr.utbm.tx52.fatools.constructs;

import org.arakhne.neteditor.formalism.standard.StandardEdge;

public class FAEdge extends StandardEdge<FiniteAutomata, AbstractFANode, FAAnchor, FAEdge> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3397413506768740027L;

	public static final String PROPERTY_GUARD = "guard";
	public static final String PROPERTY_ACTION = "action";

	private String action = null;
	private String guard = null;

	public FAEdge() {

	}

	public void setStartNode(FAState node) {
		FAAnchor anchor = null;
		if (node!=null && !node.getAnchors().isEmpty()) {
			anchor = node.getAnchors().get(0);
		}
		setStartAnchor(anchor);
	}

	public void setEndNode(FAState node) {
		FAAnchor anchor = null;
		if (node!=null && !node.getAnchors().isEmpty()) {
			anchor = node.getAnchors().get(0);
		}
		setEndAnchor(anchor);
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(Object object) {
		String na = (action==null || action.isEmpty()) ? null : action;
		if ((this.action==null && na!=null) ||
				(this.action!=null && !this.action.equals(na))) {
			String old = this.action;
			this.action = na;
			ensureActionCode(old, action);
			firePropertyChanged(PROPERTY_ACTION, old, this.action); 
		}

	}

	private void ensureActionCode(String oldAction, String newAction) {
		String newCode = getActionCode(newAction);
		if (newCode==null) {
			String oldCode = getActionCode(oldAction);
			if(oldCode!=null) {
				setActionCode(oldCode);
			}
		}
	}

	public String getActionCode() {
		FiniteAutomata machine = getGraph();
		if (machine!=null) {
			return machine.getActionCode(getAction());
		}
		return null;
	}

	protected String getActionCode(String actionName) {
        FiniteAutomata machine = getGraph();
        if (machine!=null) {
                return machine.getActionCode(actionName);
        }
        return null;
}
	
	public void setActionCode(String code) {
		FiniteAutomata machine = getGraph();
		if (machine!=null) {
			machine.setActionCode(getAction(), code);
		}
	}

	public void setAction(String action, String code) {
		String na = (action==null || action.isEmpty()) ? null : action;
		if ((this.action==null && na!=null) ||
				(this.action!=null && !this.action.equals(na))) {
			String old = this.action;
			this.action = na;
			setActionCode(code);
			firePropertyChanged(PROPERTY_ACTION, old, this.action); 
		}
		else {
			setActionCode(code);
		}
	}

	public String getGuard() {
		return this.guard;
	}

	public void setGuard(String guard) {
		String ng = (guard==null || guard.isEmpty()) ? null : guard;
		if ((this.guard==null && ng!=null) ||
				(this.guard!=null && !this.guard.equals(ng))) {
			String old = this.guard;
			this.guard = ng;
			firePropertyChanged(PROPERTY_GUARD, old, this.guard); 
		}
	}


}
