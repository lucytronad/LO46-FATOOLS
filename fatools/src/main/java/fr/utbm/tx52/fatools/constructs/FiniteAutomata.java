package fr.utbm.tx52.fatools.constructs;

import java.util.Map;
import java.util.TreeMap;

import org.arakhne.neteditor.formalism.standard.StandardGraph;

public class FiniteAutomata extends StandardGraph<FiniteAutomata, AbstractFANode, FAAnchor, FAEdge> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3716897314436419806L;

	public static final String PROPERTY_ACTIONCODES = "actionCode#"; 

	private final Map<String,String> actionCodes = new TreeMap<String,String>();
	private boolean actionChanged = false;

	public FiniteAutomata() {

	}

	public void setActionCode(String actionName, String code) {
		if (actionName==null || actionName.isEmpty()) return;
		if (code==null || code.isEmpty()) {
			String old = this.actionCodes.remove(actionName);
			if (old!=null) {
				this.actionChanged = true;
				firePropertyChanged(PROPERTY_ACTIONCODES+actionName, old, code);
			}
		}
		else {
			String old = this.actionCodes.put(actionName, code);
			if (!code.equals(old)) {
				this.actionChanged = true;
				firePropertyChanged(PROPERTY_ACTIONCODES+actionName, old, code);
			}
		}
	}

	public String getActionCode(String actionName) {
		if (actionName==null || actionName.isEmpty()) return null;
		return this.actionCodes.get(actionName);
	}

}
