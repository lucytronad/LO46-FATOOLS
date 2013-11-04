package fr.utbm.tx52.fatools.constructs;

public class FAStartState extends AbstractFANode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3482009023229983955L;

	public FAStartState() {
		super();
	}
	
	@Override
	public FANodeType getType() {
		return FANodeType.START_STATE;
	}

	

}
