package fr.utbm.tx52.fatools.constructs;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

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
				firePropertyChanged(PROPERTY_ACTIONCODES+actionName, old, code);
			}
		}
		else {
			String old = this.actionCodes.put(actionName, code);
			if (!code.equals(old)) {
				firePropertyChanged(PROPERTY_ACTIONCODES+actionName, old, code);
			}
		}
	}

	public String getActionCode(String actionName) {
		if (actionName==null || actionName.isEmpty()) return null;
		return this.actionCodes.get(actionName);
	}
	
	public Map<String,String> getActionCodes() {
		cleanActionCodes();
		return Collections.unmodifiableMap(this.actionCodes);
	}

	@Override
	public Map<String, Object> getProperties() {
		Map<String,Object> properties = super.getProperties();
		cleanActionCodes();
		for(Entry<String,String> entry : this.actionCodes.entrySet()) {
			properties.put(PROPERTY_ACTIONCODES+entry.getKey(), entry.getValue());
		}
		return properties;
	}

	private void cleanActionCodes() {
		if (this.actionChanged) {
			this.actionChanged = false;

			Set<String> actions = new TreeSet<String>(); 

			for(FAEdge edge : getEdges()) {
				String name = edge.getAction();
				if (name!=null && !name.isEmpty())
					actions.add(name);
			}

			this.actionCodes.keySet().retainAll(actions);
		}
	}

	@Override
	public void setProperties(Map<String, Object> properties) {
		super.setProperties(properties);
		if (properties!=null) {
			for(Entry<String,Object> prop : properties.entrySet()) {
				if (prop.getKey().startsWith(PROPERTY_ACTIONCODES)) {
					String actionName = prop.getKey().substring(PROPERTY_ACTIONCODES.length());
					Object obj = prop.getValue();
					String code = (obj==null) ? null : obj.toString();
					setActionCode(actionName, code);
				}
			}
		}
	}

}
