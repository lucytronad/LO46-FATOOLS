package fr.utbm.tx52.fatools.constructs;

import java.util.Map;

import org.arakhne.neteditor.formalism.standard.StandardEdge;

public class FAEdge extends StandardEdge<FiniteAutomata, AbstractFANode, FAAnchor, FAEdge> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3397413506768740027L;

	public static final String PROPERTY_ACTION = "action";

	private String action = null;

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
	
	@Override
	public Map<String, Object> getProperties() {
		Map<String,Object> properties = super.getProperties();
		properties.put(PROPERTY_ACTION, this.action);
		return properties;
	}
	
	@Override
    public Map<String, Class<?>> getUIEditableProperties() {
		Map<String,Class<?>> properties = super.getUIEditableProperties();
		properties.put(PROPERTY_ACTION, String.class);
    	return properties;
    }

	@Override
	public void setProperties(Map<String, Object> properties) {
		super.setProperties(properties);
		if (properties!=null) {
			setAction(propGetString(PROPERTY_ACTION, this.action, false, properties));
		}
	}
	
	@Override
	public String getExternalLabel() {
		if (getAction()!=null) {
			return this.getName();
		}
		return "";
	}

}
