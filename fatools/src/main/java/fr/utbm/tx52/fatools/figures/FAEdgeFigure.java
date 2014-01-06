package fr.utbm.tx52.fatools.figures;

import java.util.UUID;

import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.ui.vector.PathUtil;
import org.arakhne.neteditor.fig.figure.Figure;
import org.arakhne.neteditor.fig.figure.coercion.CoercedFigure;
import org.arakhne.neteditor.fig.figure.coercion.CoercedTextFigure;
import org.arakhne.neteditor.fig.figure.edge.PolylineEdgeFigure;
import org.arakhne.neteditor.fig.figure.edge.symbol.TriangleEdgeSymbol;
import org.arakhne.neteditor.fig.view.ViewComponentPropertyChangeEvent;
import org.arakhne.neteditor.fig.view.ViewComponentPropertyChangeListener;
import org.arakhne.neteditor.formalism.ModelObjectEvent;
import org.arakhne.neteditor.formalism.ModelObjectEvent.Type;

import fr.utbm.tx52.fatools.constructs.FAEdge;

public class FAEdgeFigure extends PolylineEdgeFigure<FAEdge> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6481431245160523451L;

	private final ViewComponentPropertyChangeListener listener = new ViewComponentPropertyChangeListener() {
		public void propertyChange(ViewComponentPropertyChangeEvent event) {
			if (PROPERTY_TEXT.equals(event.getPropertyName())) {
				String text = (String)event.getNewValue();
				getModelObject().setLabel(text);
			}
		}
	};

	public FAEdgeFigure(UUID viewId, float x1, float y1, float x2, float y2) {
		super(viewId, x1, y1, x2, y2);
		setEndSymbol(new TriangleEdgeSymbol(true));
	}

	public FAEdgeFigure(UUID viewId) {
		this(viewId, 0, 0, 0, 0);
	}

	@Override
	public CoercedFigure addAssociatedFigureIntoView(String figureId,
			CoercedFigure figure) {
		CoercedFigure previous = super.addAssociatedFigureIntoView(figureId, figure);
		if (previous instanceof CoercedTextFigure) {
			previous.removeViewComponentPropertyChangeListener(this.listener);
		}
		if (figure instanceof CoercedTextFigure) {
			figure.addViewComponentPropertyChangeListener(this.listener);
		}
		return previous;
	}

	@Override
	public CoercedFigure removeAssociatedFigureFromView(String figureId) {
		CoercedFigure fig = super.removeAssociatedFigureFromView(figureId);
		if (fig instanceof CoercedTextFigure) {
			fig.removeViewComponentPropertyChangeListener(this.listener);
		}
		return fig;
	}

	@Override
	protected void updateFromModel(ModelObjectEvent event) {
		super.updateFromModel(event);
		if ((event==null)
				|| (event.getType()==Type.PROPERTY_CHANGE &&
				FAEdge.PROPERTY_LABEL.equals(event.getPropertyName()))) { 
			FAEdge mo = getModelObject();
			String label = mo==null ? "" : mo.getExternalLabel(); //$NON-NLS-1$
			if (label==null || label.isEmpty()) {
				removeAssociatedFigureFromView("majorLabel"); //$NON-NLS-1$
			}
			else {
				Figure figure = getAssociatedFigureInView("majorLabel"); //$NON-NLS-1$
				if (figure==null) {
					Point2D anchor = PathUtil.interpolate(getPath(), .5f);
					CoercedTextFigure text = new CoercedTextFigure(
							getViewUUID(),
							label,
							anchor.getX(),
							anchor.getY());
					text.setAnchorDescriptor(.5f);
					addAssociatedFigureIntoView("majorLabel", text); //$NON-NLS-1$
				}
				else if (figure instanceof CoercedTextFigure) {
					CoercedTextFigure text = (CoercedTextFigure)figure;
					text.setText(label);
					text.fitToContent();
				}
			}
		}
	}	

}
