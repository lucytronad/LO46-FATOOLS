package fr.utbm.tx52.fatools.constructs;

public class FAState extends AbstractFANode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6543243896004895161L;

	public static final String PROPERTY_ENTERACTION = "enterAction";
	public static final String PROPERTY_INSIDEACTION = "insideAction";
	public static final String PROPERTY_EXITACTION = "exitAction";
	public static final String PROPERTY_ACCEPTING = "accepting";

	private String enterAction = null;
	private String insideAction = null;
	private String exitAction = null;
	private boolean isAccepting = false;


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

	public String getEnterAction() {
		return this.enterAction;
	}

	public String getExitAction() {
		return this.exitAction;
	}

	public String getAction() {
		return this.insideAction;
	}

	public void setEnterAction(String action) {
		String na = (action==null || action.isEmpty()) ? null : action;
		if ((this.enterAction==null && na!=null)||
				(this.enterAction!=null && !this.enterAction.equals(na))) {
			String old = this.enterAction;
			this.enterAction = na;
			ensureActionCode(old, action);
			firePropertyChanged(PROPERTY_ENTERACTION, old, this.enterAction);
		}
	}

	public void setEnterAction(String action, String code) {
		String na = (action==null || action.isEmpty()) ? null : action;
		if ((this.enterAction==null && na!=null)||
				(this.enterAction!=null && !this.enterAction.equals(na))) {
			String old = this.enterAction;
			this.enterAction = na;
			setActionCode(code);
			firePropertyChanged(PROPERTY_ENTERACTION, old, this.enterAction);
		}
		else {
			setActionCode(code);
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

	public void setActionCode(String code) {
		FiniteAutomata machine = getGraph();
		if (machine!=null) {
			machine.setActionCode(getAction(), code);
		}
	}

	public String getActionCode() {
		FiniteAutomata machine = getGraph();
		if (machine!=null) {
			return machine.getActionCode(getAction());
		}
		return null;
	}

	public void setAction(String action) {
		String na = (action==null || action.isEmpty()) ? null : action;
		if ((this.insideAction==null && na!=null)||
				(this.insideAction!=null && !this.insideAction.equals(na))) {
			String old = this.insideAction;
			this.insideAction = na;
			ensureActionCode(old, action);
			firePropertyChanged(PROPERTY_INSIDEACTION, old, this.insideAction); 
		}
	}

	public void setAction(String action, String code) {
		String na = (action==null || action.isEmpty()) ? null : action;
		if ((this.insideAction==null && na!=null)||
				(this.insideAction!=null && !this.insideAction.equals(na))) {
			String old = this.insideAction;
			this.insideAction = na;
			setActionCode(code);
			firePropertyChanged(PROPERTY_INSIDEACTION, old, this.insideAction); 
		}
		else {
			setActionCode(code);
		}
	}

	public void setExitAction(String action, String code) {
		String na = (action==null || action.isEmpty()) ? null : action;
		if ((this.exitAction==null && na!=null)||
				(this.exitAction!=null && !this.exitAction.equals(na))) {
			String old = this.exitAction;
			this.exitAction = na;
			setActionCode(code);
			firePropertyChanged(PROPERTY_EXITACTION, old, this.exitAction); 
		}
		else {
			setActionCode(code);
		}
	}
	
	public void setExitActionCode(String code) {
		FiniteAutomata machine = getGraph();
		if (machine!=null) {
			machine.setActionCode(getExitAction(), code);
		}
	}
}
