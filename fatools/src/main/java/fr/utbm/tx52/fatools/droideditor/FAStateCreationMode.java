package fr.utbm.tx52.fatools.droideditor;

import java.util.Set;

import org.arakhne.afc.math.continous.object2d.Circle2f;
import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.math.continous.object2d.Shape2f;
import org.arakhne.neteditor.android.actionmode.ActionModeOwner;
import org.arakhne.neteditor.android.actionmode.creation.AbstractAndroidCreationMode;
import org.arakhne.neteditor.android.actionmode.creation.AbstractNodeCreationMode;
import org.arakhne.neteditor.formalism.Node;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import fr.utbm.tx52.fatools.R;
import fr.utbm.tx52.fatools.constructs.FAState;
import fr.utbm.tx52.fatools.constructs.FiniteAutomata;

public class FAStateCreationMode extends AbstractNodeCreationMode {

	public FAStateCreationMode() {
		super(R.string.undo_fa_state);
	}

	@Override
	protected Shape2f getShape(Rectangle2f bounds) {
		return new Circle2f(bounds.getCenterX(), bounds.getCenterY(),
				Math.min(bounds.getWidth(), bounds.getHeight())/2f);
	}
	
	private static String findName(FiniteAutomata fa) {
		Set<String> names = fa.getNodeNames();
		int i = 1;
		String n = Integer.toString(i);
		while (names.contains(n)) {
			++i;
			n = Integer.toString(i);
		}
		return n;
	}

	@Override
	protected Node<?,?,?,?> createModelObject() {
		ActionModeOwner container = getModeManagerOwner();
		FiniteAutomata finiteAutomata = (FiniteAutomata)container.getGraph();
		FAState state = new FAState(findName(finiteAutomata));
		android.view.ActionMode bar = getActionBar();
		if (bar!=null) {
			CheckBox cb = (CheckBox)bar.getCustomView().findViewById(R.id.isaccepting);
			state.setAccepting(cb.isChecked());
		}
		return state;
	}

	@Override
	protected org.arakhne.neteditor.android.actionmode.creation.AbstractAndroidCreationMode.ActionBar createActionBarListener() {
		return new ActionBar();
	}

	protected class ActionBar extends AbstractAndroidCreationMode.ActionBar {

		/**
		 */
		public ActionBar() {
			//
		}

		/** Invoked when the action mode is created, ie. when
		 * {@code startActionMode()} was called.
		 * 
		 * @param mode is the new action mode.
		 * @param menu is the menu to populate with action buttons.
		 * @return <code>true</code> if the action mode should
		 * be created, <code>false</code> if entering this mode
		 * should be aborted.
		 */
		@Override
		public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
			super.onCreateActionMode(mode, menu);
			ActionModeOwner container = getModeManagerOwner();
			View customView = LayoutInflater.from(container.getContext()).inflate(R.layout.actionmode_fa_state, null);
			mode.setCustomView(customView);
			TextView titleWidget = (TextView)customView.findViewById(R.id.title);
			titleWidget.setText(R.string.actionmode_create_fa_state);
			return true;
		}

	}

}
