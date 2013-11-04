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
import org.arakhne.neteditor.android.actionmode.creation.BitmapDecorationCreationMode;
import org.arakhne.neteditor.android.actionmode.creation.EllipseDecorationCreationMode;
import org.arakhne.neteditor.android.actionmode.creation.PolygonDecorationCreationMode;
import org.arakhne.neteditor.android.actionmode.creation.PolylineDecorationCreationMode;
import org.arakhne.neteditor.android.actionmode.creation.RectangleDecorationCreationMode;
import org.arakhne.neteditor.android.actionmode.creation.TextDecorationCreationMode;
import org.arakhne.neteditor.android.activity.AbstractEditorActivity;
import org.arakhne.neteditor.android.activity.FigureView;
import org.arakhne.neteditor.fig.factory.FigureFactory;
import org.arakhne.neteditor.fig.figure.Figure;
import org.arakhne.neteditor.formalism.ModelObject;

import fr.utbm.tx52.fatools.R;
import fr.utbm.tx52.fatools.constructs.FAEdge;
import fr.utbm.tx52.fatools.constructs.FAState;
import fr.utbm.tx52.fatools.constructs.FiniteAutomata;
import fr.utbm.tx52.fatools.figures.FAEdgeFigure;
import fr.utbm.tx52.fatools.figures.FAFigureFactory;
import fr.utbm.tx52.fatools.figures.FAStateFigure;
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
		MultiValue<FAStateFigure> stateFigure = new MultiValue<FAStateFigure>();
		MultiValue<FAEdgeFigure> transitionFigure = new MultiValue<FAEdgeFigure>();
		for(Figure figure : figures) {
			if (figure instanceof FAStateFigure) {
				stateFigure.add((FAStateFigure)figure);
			}
			else if (figure instanceof FAEdgeFigure) {
				transitionFigure.add((FAEdgeFigure)figure);
			}
		}
		if (stateFigure.isSet() && !stateFigure.isMultipleDifferentValues()) {
			FAStateFigure figure = stateFigure.get();
			String actionCode = null;
			int actionId = 2;
			if (figure.isInEnterActionBox(hitPosition)) {
				actionCode = figure.getModelObject().getEnterAction();
				actionId = 1;
			}
			else if (figure.isInExitActionBox(hitPosition)) {
				actionCode = figure.getModelObject().getExitAction();
				actionId = 3;
			}
			else if (figure.isInInsideActionBox(hitPosition)) {
				actionCode = figure.getModelObject().getAction();
			}
			if (actionCode==null || actionCode.isEmpty()) {
				actionCode = DEFAULT_ACTION_NAME;
			}
			openEditor(actionId, figure.getModelObject(), actionCode);
		}
		else if (transitionFigure.isSet() && !transitionFigure.isMultipleDifferentValues()) {
			FAEdgeFigure figure = transitionFigure.get();
			String actionCode = figure.getModelObject().getAction();
			if (actionCode==null || actionCode.isEmpty()) {
				actionCode = DEFAULT_ACTION_NAME;
			}
			openEditor(0, figure.getModelObject(), actionCode);
		}
	}

	private void openEditor(int id, ModelObject mo, String actionName) {
		File tempFile = null;
		try {
			tempFile = File.createTempFile("facode_"+id+"_"+mo.getUUID().toString()+"_"+actionName+"_", ".java", getCacheDir()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			tempFile.deleteOnExit();

			FileWriter fw = new FileWriter(tempFile);
			try {
				String code = getFigureView().getGraph().getActionCode(actionName);
				if (code!=null) fw.write(code.trim());
			}
			finally {
				fw.close();
			}

			Intent intent = new Intent(Intent.ACTION_EDIT); 
			Uri uri = Uri.fromFile(tempFile); 
			intent.setDataAndType(uri, "text/x-java-source"); //$NON-NLS-1$
			startActivityForResult(intent, CODE_EDITOR_REQUEST_CODE);
		}
		catch (IOException e) {
			if (tempFile!=null) tempFile.delete();
			showError(e);
		}
		catch (ActivityNotFoundException e) {
			if (tempFile!=null) tempFile.delete();
			showError(e);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode==CODE_EDITOR_REQUEST_CODE) {
			if (resultCode==RESULT_OK && data!=null) {
				Uri uri = data.getData();
				try {
					// Create a file instance from the URI
					URI jURI = new URI(uri.toString());
					URL jURL = jURI.toURL();
					File file = FileSystem.convertURLToFile(jURL);

					int actionId = 0;
					UUID id = null;
					String actionName = null;
					String basename = file.getName();
					Pattern pattern = Pattern.compile("^facode_(.*?)_(.*?)_(.*?)_"); //$NON-NLS-1$
					Matcher matcher = pattern.matcher(basename);
					if (matcher.find()) {
						actionId = Integer.parseInt(matcher.group(1));
						id = UUID.fromString(matcher.group(2));
						actionName = matcher.group(3);
					}

					if (id!=null && actionName!=null && !actionName.isEmpty()) {
						String content;
						try {
							FileReader fr = new FileReader(file);
							try {
								content = readFully(fr);
							}
							finally {
								fr.close();
							}
						}
						finally {
							file.delete();
						}

						ModelObject obj = getFigureView().getGraph().findModelObject(id);
						if (obj instanceof FAState) {
							FAState state = (FAState)obj;
							switch(actionId) {
							case 1:
								state.setEnterAction(actionName, content);
								break;
							case 2:
								state.setAction(actionName, content);
								break;
							case 3:
								state.setExitAction(actionName, content);
								break;
							default:
							}
						}
						else if (obj instanceof FAEdge) {
							((FAEdge)obj).setAction(actionName, content);
						}
					}
				}
				catch (Exception e) {
					showError(e);
				}
			}
		}
		else {
			super.onActivityResult(requestCode, resultCode, data);
		}
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
			case R.id.menu_fa_create_start:
			{
				FAStartStateCreationMode mode = new FAStartStateCreationMode();
				//FigureView<?> view = getFigureView();
				FigureActionModeManager modeManager = viewer.getActionModeManager();
				modeManager.startMode(mode);
				return true;
			}
			case R.id.menu_fa_create_state:
			{
				FAStateCreationMode mode = new FAStateCreationMode();
				//FigureView<?> view = getFigureView();
				FigureActionModeManager modeManager = viewer.getActionModeManager();
				modeManager.startMode(mode);
				return true;
			}
			case R.id.menu_fa_create_edge:
			{
				FAEdgeCreationMode mode = new FAEdgeCreationMode();
				//FigureView<?> view = getFigureView();
				FigureActionModeManager modeManager = viewer.getActionModeManager();
				modeManager.startMode(mode);
				return true;
			}
			/*case R.id.menu_decoration_rectangle:
			{
				RectangleDecorationCreationMode mode = new RectangleDecorationCreationMode();
				FigureView<?> view = getFigureView();
				FigureActionModeManager modeManager = view.getActionModeManager();
				modeManager.startMode(mode);
				return true;
			}
			case R.id.menu_decoration_ellipse:
			{
				EllipseDecorationCreationMode mode = new EllipseDecorationCreationMode();
				FigureView<?> view = getFigureView();
				FigureActionModeManager modeManager = view.getActionModeManager();
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
			case R.id.menu_decoration_image:
			{
				BitmapDecorationCreationMode mode = new BitmapDecorationCreationMode();
				FigureView<?> view = getFigureView();
				FigureActionModeManager modeManager = view.getActionModeManager();
				modeManager.startMode(mode);
				return true;
			}
			case R.id.menu_decoration_polyline:
			{
				PolylineDecorationCreationMode mode = new PolylineDecorationCreationMode();
				FigureView<?> view = getFigureView();
				FigureActionModeManager modeManager = view.getActionModeManager();
				modeManager.startMode(mode);
				return true;
			}
			case R.id.menu_decoration_polygon:
			{
				PolygonDecorationCreationMode mode = new PolygonDecorationCreationMode();
				FigureView<?> view = getFigureView();
				FigureActionModeManager modeManager = view.getActionModeManager();
				modeManager.startMode(mode);
				return true;
			}*/
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
