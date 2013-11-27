package fr.utbm.tx52.fatools.figures;

import java.util.UUID;

import org.arakhne.afc.math.continous.object2d.Ellipse2f;
import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.ui.vector.Color;
import org.arakhne.afc.ui.vector.Dimension;
import org.arakhne.afc.ui.vector.Font;
import org.arakhne.afc.ui.vector.VectorToolkit;
import org.arakhne.neteditor.fig.figure.node.CircleNodeFigure;
import org.arakhne.neteditor.fig.graphics.ViewGraphics2D;
import org.arakhne.neteditor.formalism.ModelObjectEvent;

import android.graphics.Paint;
import android.graphics.Rect;
import fr.utbm.tx52.fatools.constructs.FAAnchor;
import fr.utbm.tx52.fatools.constructs.FAState;

public class FAStateFigure extends CircleNodeFigure<FAState, FAAnchor> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8039121028238470714L;

	public FAStateFigure(UUID viewId, float x, float y) {
		super(viewId, x, y);
		setResizeDirections();
		setMinimalDimension(40, 40);
		setMaximalDimension(40, 40);
	}

	public FAStateFigure(UUID viewId) {
		this(viewId, 0, 0);
	}

	@Override
	protected void paintNode(ViewGraphics2D g) {
		// Ask to the graphic tool to output the name of
		// the name inside the next drawn shape
		g.setInteriorText(getModelObject().getName());

		// Draw the default circle (filling and outlining).
		// Remember that an interior text will be paint also.
		super.paintNode(g);

		boolean isAccepting = getModelObject().isAccepting();
		if(isAccepting) {
			Rectangle2f figureBounds = g.getCurrentViewComponentBounds();
			Ellipse2f insideOval = new Ellipse2f(
					figureBounds.getMinX()+5,
					figureBounds.getMinY()+5,
					figureBounds.getWidth()-10,
					figureBounds.getHeight()-10);
			g.setInteriorPainted(false);
			g.setOutlineDrawn(true);
			g.draw(insideOval);
		}
	}

}
