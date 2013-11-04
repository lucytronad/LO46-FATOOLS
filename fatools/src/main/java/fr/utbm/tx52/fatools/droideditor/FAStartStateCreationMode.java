package fr.utbm.tx52.fatools.droideditor;

import org.arakhne.afc.math.continous.object2d.Circle2f;
import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.math.continous.object2d.Shape2f;
import org.arakhne.neteditor.android.actionmode.creation.AbstractNodeCreationMode;
import org.arakhne.neteditor.formalism.Node;

import fr.utbm.tx52.fatools.R;
import fr.utbm.tx52.fatools.constructs.FAStartState;
import android.view.ActionMode;

public class FAStartStateCreationMode extends AbstractNodeCreationMode {
	
	public FAStartStateCreationMode() {
		super(R.string.undo_fa_start);
	}

	@Override
	protected void onActionBarOpened(ActionMode bar) {
		bar.setTitle(R.string.actionmode_create_fa_start);
	}

	@Override
	protected Shape2f getShape(Rectangle2f bounds) {
		return new Circle2f(bounds.getCenterX(), bounds.getCenterY(),
				Math.min(bounds.getWidth(), bounds.getHeight())/2f);
	}

	@Override
	protected Node<?,?,?,?> createModelObject() {
		return new FAStartState();
	}
}
