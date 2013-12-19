package fr.utbm.tx52.fatools.constructs;

public class FAState extends AbstractFANode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6543243896004895161L;

	public static final String PROPERTY_ACCEPTING = "accepting";
	public static final String PROPERTY_FAILED = "failed";
	public static final String PROPERTY_SUCCESS = "success";

	private boolean isAccepting = false;
	private boolean hasFailed = false;
	private boolean isSuccessfull = false;

	public FAState() {
		super();
	}

	public FAState(String name) {
		super(name);
	}

	@Override
	public FANodeType getType() {
		return FANodeType.STATE;
	}

	public boolean isAccepting() {
		return this.isAccepting;
	}

	public void setAccepting(boolean accepting) {
		this.isAccepting=accepting;
	}
	
	public boolean hasFailed() {
		return this.hasFailed;
	}

	public void setFailed(boolean failed) {
		if(!(hasFailed==failed))
		{
			boolean old = hasFailed;
			hasFailed=failed;
			firePropertyChanged(PROPERTY_FAILED, old, hasFailed);
		}
	}
	
	public boolean isSuccessfull() {
		return this.isSuccessfull;
	}

	public void setSuccessfull(boolean successfull) {
		if(!(isSuccessfull==successfull))
		{
			boolean old = isSuccessfull;
			isSuccessfull=successfull;
			firePropertyChanged(PROPERTY_SUCCESS, old, isSuccessfull);
		}
	}
	
	
	
}
