package fr.utbm.tx52.fatools.droideditor;

import org.arakhne.neteditor.android.actionmode.creation.AbstractEdgeCreationMode;
import org.arakhne.neteditor.formalism.Edge;

import fr.utbm.tx52.fatools.R;
import fr.utbm.tx52.fatools.constructs.FAEdge;
import android.view.ActionMode;

class FAEdgeCreationMode extends AbstractEdgeCreationMode {

    /**
     */
    public FAEdgeCreationMode() {
            super(R.string.undo_fa_edge);
    }

    @Override
    protected void onActionBarOpened(ActionMode bar) {
            bar.setTitle(R.string.actionmode_create_fa_edge);
    }
    
    @Override
    protected Edge<?,?,?,?> createEdge() {
            return new FAEdge();
    }
                    
}