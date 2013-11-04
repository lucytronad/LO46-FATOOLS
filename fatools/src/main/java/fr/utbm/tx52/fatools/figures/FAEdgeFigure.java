package fr.utbm.tx52.fatools.figures;

import java.util.UUID;

import org.arakhne.neteditor.fig.figure.coercion.CoercedFigure;
import org.arakhne.neteditor.fig.figure.coercion.CoercedTextFigure;
import org.arakhne.neteditor.fig.figure.edge.PolylineEdgeFigure;
import org.arakhne.neteditor.fig.figure.edge.symbol.TriangleEdgeSymbol;
import org.arakhne.neteditor.fig.view.ViewComponentPropertyChangeEvent;
import org.arakhne.neteditor.fig.view.ViewComponentPropertyChangeListener;

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
				int idx = text.indexOf("/"); //$NON-NLS-1$
				if (idx==-1) {
					getModelObject().setGuard(text);
					getModelObject().setAction(null);
				}
				else {
					getModelObject().setGuard(text.substring(0, idx));
					getModelObject().setAction(text.substring(idx+1));
				}
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

}
