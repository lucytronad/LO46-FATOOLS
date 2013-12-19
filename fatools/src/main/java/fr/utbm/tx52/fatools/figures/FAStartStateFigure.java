package fr.utbm.tx52.fatools.figures;

import java.util.UUID;

import org.arakhne.afc.math.continous.object2d.Ellipse2f;
import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.ui.vector.VectorToolkit;
import org.arakhne.neteditor.fig.figure.node.CircleNodeFigure;
import org.arakhne.neteditor.fig.graphics.ViewGraphics2D;

import fr.utbm.tx52.fatools.constructs.FAAnchor;
import fr.utbm.tx52.fatools.constructs.FAStartState;

public class FAStartStateFigure extends CircleNodeFigure<FAStartState, FAAnchor> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3255809380457426159L;

	public FAStartStateFigure(UUID viewId, float x, float y) {
		super(viewId, x, y);
		setResizeDirections();
		setMinimalDimension(20, 20);
		setMaximalDimension(20, 20);
	}

	public FAStartStateFigure(UUID viewId) {
		this(viewId, 0, 0);
	}

	@Override
	protected void paintNode(ViewGraphics2D g) {
		
		boolean isActive = getModelObject().isActive();
		if(isActive) {
			g.setFillColor(VectorToolkit.color(0, 255, 0));
		}
		else {
			g.setFillColor(VectorToolkit.color(0, 0, 0));
		}
		
		Rectangle2f figureBounds = g.getCurrentViewComponentBounds();
		Ellipse2f oval = new Ellipse2f(
				figureBounds.getMinX(),
				figureBounds.getMinY(),
				figureBounds.getWidth(),
				figureBounds.getHeight());
		g.setOutlineDrawn(false);
		g.setInteriorPainted(true);;
		g.draw(oval);
	}

}
