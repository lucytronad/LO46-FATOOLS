package fr.utbm.tx52.fatools.droideditor;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.arakhne.afc.io.filefilter.FileFilter;
import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.ui.android.about.AboutDialog;
import org.arakhne.afc.ui.undo.UndoListener;
import org.arakhne.afc.ui.undo.UndoManager;
import org.arakhne.afc.util.MultiValue;
import org.arakhne.afc.vmutil.FileSystem;
import org.arakhne.neteditor.android.actionmode.FigureActionModeManager;
import org.arakhne.neteditor.android.actionmode.creation.TextDecorationCreationMode;
import org.arakhne.neteditor.android.activity.AbstractEditorActivity;
import org.arakhne.neteditor.android.activity.FigureView;
import org.arakhne.neteditor.fig.factory.FigureFactory;
import org.arakhne.neteditor.fig.figure.Figure;
import org.arakhne.neteditor.formalism.ModelObject;

import fr.utbm.tx52.fatools.R;
import fr.utbm.tx52.fatools.constructs.FAEdge;
import fr.utbm.tx52.fatools.constructs.FiniteAutomata;
import fr.utbm.tx52.fatools.figures.FAEdgeFigure;
import fr.utbm.tx52.fatools.figures.FAFigureFactory;
import fr.utbm.tx52.fatools.figures.FAStateFigure;
import fr.utbm.tx52.fatools.simulator.SimulationMode;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class FAEditorActivity extends AbstractEditorActivity<FiniteAutomata> {
	
	private static final int CODE_EDITOR_REQUEST_CODE = 9123;
    
    private static final String DEFAULT_ACTION_NAME = "doAction";
	
	private static String readFully(Reader stream) throws IOException {
		StringBuilder content = new StringBuilder();
		char[] buffer = new char[2048];
		int n;

		n = stream.read(buffer);
		while (n>0) {
			content.append(buffer, 0, n);
			n = stream.read(buffer);
		}

		return content.toString();
	}

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
				FigureView<?> view = getFigureView();
				FigureActionModeManager modeManager = viewer.getActionModeManager();
				modeManager.startMode(mode);
				return true;
			}
			case R.id.menu_fa_create_state:
			{
				FAStateCreationMode mode = new FAStateCreationMode();
				FigureView<?> view = getFigureView();
				FigureActionModeManager modeManager = viewer.getActionModeManager();
				modeManager.startMode(mode);
				return true;
			}
			case R.id.menu_fa_create_edge:
			{
				FAEdgeCreationMode mode = new FAEdgeCreationMode();
				FigureView<?> view = getFigureView();
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
