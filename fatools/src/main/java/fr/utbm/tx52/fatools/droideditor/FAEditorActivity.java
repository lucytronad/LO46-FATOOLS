package fr.utbm.tx52.fatools.droideditor;

import java.io.File;

import org.arakhne.afc.io.filefilter.FileFilter;
import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.ui.android.about.AboutDialog;
import org.arakhne.afc.ui.undo.UndoListener;
import org.arakhne.afc.ui.undo.UndoManager;
import org.arakhne.neteditor.android.actionmode.FigureActionModeManager;
import org.arakhne.neteditor.android.actionmode.creation.TextDecorationCreationMode;
import org.arakhne.neteditor.android.activity.AbstractEditorActivity;
import org.arakhne.neteditor.android.activity.FigureView;
import org.arakhne.neteditor.fig.factory.FigureFactory;
import org.arakhne.neteditor.fig.figure.Figure;

import fr.utbm.tx52.fatools.R;
import fr.utbm.tx52.fatools.constructs.FiniteAutomata;
import fr.utbm.tx52.fatools.figures.FAFigureFactory;
import fr.utbm.tx52.fatools.simulator.SimulationMode;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class FAEditorActivity extends AbstractEditorActivity<FiniteAutomata> {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final FigureView<FiniteAutomata> view = getFigureView();

		view.getUndoManager().addUndoListener(
				new UndoListener() {
					//@Override
					public void undoListChanged(UndoManager manager) {
						invalidateOptionsMenu();
					}
				});
	}

	@Override
	protected void onActionPerformed(boolean isEditionEnabled, Point2D hitPosition, Figure... figures) {
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.fa_editor, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		try {
			FigureView<FiniteAutomata> viewer = getFigureView();
			switch(item.getItemId()) {
			case R.id.menu_fa_start_simulation:
			{
				SimulationMode simulationMode = new SimulationMode(viewer);
				startActionMode(simulationMode);
				return true;
				
			}
			case R.id.menu_fa_create_start:
			{
				FAStartStateCreationMode mode = new FAStartStateCreationMode();
				FigureActionModeManager modeManager = viewer.getActionModeManager();
				modeManager.startMode(mode);
				return true;
			}
			case R.id.menu_fa_create_state:
			{
				FAStateCreationMode mode = new FAStateCreationMode();
				FigureActionModeManager modeManager = viewer.getActionModeManager();
				modeManager.startMode(mode);
				return true;
			}
			case R.id.menu_fa_create_edge:
			{
				FAEdgeCreationMode mode = new FAEdgeCreationMode();
				FigureActionModeManager modeManager = viewer.getActionModeManager();
				modeManager.startMode(mode);
				return true;
			}
			case R.id.menu_decoration_text:
			{
				TextDecorationCreationMode mode = new TextDecorationCreationMode();
				FigureView<?> view = getFigureView();
				FigureActionModeManager modeManager = view.getActionModeManager();
				modeManager.startMode(mode);
				return true;
			}
			case R.id.menu_resetView:
				viewer.resetView();
				return true;
			case R.id.menu_newdocument:
				runNewDocumentAction(true);
				return true;
			case R.id.menu_load:
				runLoadAction(true);
				return true;
			case R.id.menu_save:
				runSaveAction();
				return true;
			case R.id.menu_saveas:
				runSaveAsAction();
				return true;
			case R.id.menu_revert:
				viewer.getUndoManager().undo();
				return true;
			case R.id.menu_revert_revert:
				viewer.getUndoManager().redo();
				return true;
			case R.id.menu_settings:
				break;
			case R.id.menu_about:
				AboutDialog dialog = new AboutDialog(
						this,
						R.drawable.ic_launcher,
						null,
						null);
				dialog.show();
				break;
			default:
			}
			return super.onMenuItemSelected(featureId, item);
		}
		catch(Throwable e) {
			showError(e);
			return false;
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem item;

		FigureView<?> viewer = getFigureView();
		UndoManager undoManager = viewer.getUndoManager();

		File currentDocument = getCurrentDocument();
		boolean hasChanged = hasChanged();

		item = menu.findItem(R.id.menu_newdocument);
		item.setVisible(currentDocument!=null || hasChanged);

		item = menu.findItem(R.id.menu_save);
		item.setVisible(hasChanged);

		item = menu.findItem(R.id.menu_saveas);
		item.setVisible(currentDocument!=null);

		item = menu.findItem(R.id.menu_revert);
		item.setEnabled(undoManager.canUndo());

		item = menu.findItem(R.id.menu_revert_revert);
		item.setEnabled(undoManager.canRedo());

		return true;
	}

	@Override
	protected Class<? extends FileFilter> getPreferredFileFilter() {
		return null;
	}

	@Override
	protected final Class<FiniteAutomata> getPreferredGraphType() {
		return FiniteAutomata.class;
	}

	@Override
	protected Class<? extends FigureFactory<FiniteAutomata>> getPreferredFigureFactory() {
		return FAFigureFactory.class;
	}

}
