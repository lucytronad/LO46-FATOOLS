package fr.utbm.tx52.fatools.simulator;

import org.arakhne.neteditor.android.activity.FigureView;

import fr.utbm.tx52.fatools.R;
import fr.utbm.tx52.fatools.constructs.AbstractFANode;
import fr.utbm.tx52.fatools.constructs.FANodeType;
import fr.utbm.tx52.fatools.constructs.FAState;
import fr.utbm.tx52.fatools.constructs.FiniteAutomata;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class SimulationMode implements android.view.ActionMode.Callback {

	private Simulator simulator=null;
	private FiniteAutomata finiteAutomata=null;
	private boolean toReset = false;
	private String simString="";
	
	public SimulationMode(FigureView<FiniteAutomata> viewer) {
		this.finiteAutomata=viewer.getGraph();
		this.simulator = new Simulator(this.finiteAutomata);
	}
	
	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menu_fa_step_backward_simulation:
		{
			return true;
		}
		case R.id.menu_fa_step_forward_simulation:
		{
			if(toReset==true)
			{
				simulator.reset();				
				simulator.setSimulationString(simString);
				toReset=false;
				return true;
			}
			if(simulator.runStepByStepSimulation()==false)
				toReset=true;
			return true;

		}
		case R.id.menu_fa_play_simulation:
		{
			if(toReset==true)
			{
				simulator.reset();
				simulator.setSimulationString(simString);
				toReset=false;
				return true;
			}			
			while(simulator.runStepByStepSimulation());
			toReset=true;
			return true;
		}
		}
		return false;
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		mode.getMenuInflater().inflate(R.menu.fa_simulation, menu);
		View test = menu.findItem(R.id.menu_fa_string_simulation).getActionView();
		final EditText simulationString = (EditText) test.findViewById(R.id.edit_simulation_string);
		simulationString.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				simString = simulationString.getText().toString();
				simulator.reset();
				simulator.setSimulationString(simString);
			}
		});
		return true;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		this.simulator=null;
		for(AbstractFANode node : finiteAutomata.getNodes()) {
			node.setActive(false);
			if(node.getType()==FANodeType.STATE) {
				((FAState)node).setFailed(false);
				((FAState)node).setSuccessfull(false);
			}
		}
		this.finiteAutomata=null;
		
		
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		// TODO Auto-generated method stub
		return false;
	}

}
