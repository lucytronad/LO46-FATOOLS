package fr.utbm.tx52.fatools.constructs;

import java.util.Map;

import org.arakhne.neteditor.formalism.standard.StandardEdge;

public class FAEdge extends StandardEdge<FiniteAutomata, AbstractFANode, FAAnchor, FAEdge> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3397413506768740027L;

	public static final String PROPERTY_LABEL = "label";

	private String label = null;

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

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		String na = (label==null || label.isEmpty()) ? null : label;
		if ((this.label==null && na!=null) ||
				(this.label!=null && !this.label.equals(na))) {
			String old = this.label;
			this.label = na;
			firePropertyChanged(PROPERTY_LABEL, old, this.label); 
		}

	}
	
	@Override
	public Map<String, Object> getProperties() {
		Map<String,Object> properties = super.getProperties();
		properties.put(PROPERTY_LABEL, this.label);
		return properties;
	}
	
	@Override
    public Map<String, Class<?>> getUIEditableProperties() {
		Map<String,Class<?>> properties = super.getUIEditableProperties();
		properties.put(PROPERTY_LABEL, String.class);
    	return properties;
    }

	@Override
	public void setProperties(Map<String, Object> properties) {
		super.setProperties(properties);
		if (properties!=null) {
			setLabel(propGetString(PROPERTY_LABEL, this.label, true, properties));
		}
	}
	
	@Override
	public String getExternalLabel() {
		return getLabel();
	}

}
