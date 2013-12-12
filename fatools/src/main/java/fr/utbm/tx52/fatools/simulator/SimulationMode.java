package fr.utbm.tx52.fatools.simulator;

import org.arakhne.neteditor.android.activity.FigureView;

import fr.utbm.tx52.fatools.R;
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
	
	public SimulationMode(FigureView<FiniteAutomata> viewer) {
		this.simulator = new Simulator(viewer.getGraph());
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
			return true;
		}
		case R.id.menu_fa_play_simulation:
		{
			
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
				simulator.setSimulationString(simulationString.getText().toString());
			}
		});
		return true;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		// TODO Auto-generated method stub
		return false;
	}

}
