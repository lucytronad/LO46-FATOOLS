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

	private Rectangle2f nameBox = null;

	public FAStateFigure(UUID viewId, float x, float y) {
		super(viewId, x, y);
		setResizeDirections();
		setMinimalDimension(40, 40);
		setMaximalDimension(40, 40);
	}

	public FAStateFigure(UUID viewId) {
		this(viewId, 0, 0);
	}

	public boolean isInNameBox(Point2D position) {
		if (position!=null && this.nameBox!=null) {
			return this.nameBox.contains(position);
		}
		return false;
	}

	private Dimension getPreferredSize() {
		FAState mo = getModelObject();
		if (mo!=null) {
			String[] texts = new String[] {
					mo.getName(),
					mo.getEnterAction(),
					mo.getExitAction(),
					mo.getAction()
			};

			Font defaultFont = VectorToolkit.font();
			float fontSize = defaultFont.getSize();
			float maxWidth = 0;
			float height = fontSize;
			for(String str : texts) {
				if (str!=null && !str.isEmpty()) {
					float w = fontSize * str.length();
					if (maxWidth<w) maxWidth = w;
					height += fontSize + 2;
				}
			}
			if (height<getMinimalHeight()) height = getMinimalHeight();
			if (maxWidth<getMinimalWidth()) maxWidth = getMinimalWidth();
			if (maxWidth>getMaximalWidth()) maxWidth = getMaximalWidth();
			if (height>getMaximalHeight()) height = getMaximalHeight();
			return VectorToolkit.dimension(maxWidth, height);
		}
		return getMinimalDimension();
	}

	private void expandsBoundsIfNecessary() {
		Dimension pref = getPreferredSize();
		Rectangle2f bounds = getBounds();
		if (pref.width()>bounds.getWidth() || pref.height()>bounds.getHeight()) {
			setBounds(
					bounds.getMinX(),
					bounds.getMinY(),
					Math.max(pref.width(), bounds.getWidth()),
					Math.max(pref.height(), bounds.getHeight()));
			avoidCollision();
		}
	}

	@Override
	public void fitToContent() {
		Dimension pref = getPreferredSize();
		Rectangle2f bounds = getBounds();
		if (pref.width()!=bounds.getWidth() || pref.height()!=bounds.getHeight()) {
			setBounds(
					bounds.getMinX(),
					bounds.getMinY(),
					pref.width(),
					pref.height());
			avoidCollision();
		}
	}

	 @Override
	 protected void updateFromModel(ModelObjectEvent event) {
		 super.updateFromModel(event);
		 expandsBoundsIfNecessary();
	 }

	 @Override
	 protected void paintNode(ViewGraphics2D g) {
		 boolean isAccepting = getModelObject().isAccepting();
		 Rectangle2f figureBounds = g.getCurrentViewComponentBounds();
		 Ellipse2f oval = new Ellipse2f(
				 figureBounds.getMinX(),
				 figureBounds.getMinY(),
				 figureBounds.getWidth(),
				 figureBounds.getHeight());
		 g.setOutlineDrawn(true);
		 g.setInteriorPainted(false);
		 Color old = g.setFillColor(getLineColor());
		 g.draw(oval);
		 if(isAccepting) {
			 Ellipse2f insideOval = new Ellipse2f(
					 figureBounds.getMinX()+5,
					 figureBounds.getMinY()+5,
					 figureBounds.getWidth()-10,
					 figureBounds.getHeight()-10);
			 g.draw(insideOval);
		 }
		 Paint p = new Paint();
		 Rect bounds = new Rect();
		 p.getTextBounds(getModelObject().getName(), 0, getModelObject().getName().length(), bounds);
		 g.drawString(getModelObject().getName(),
				 figureBounds.getMinX()+figureBounds.getWidth()/2-bounds.width()/2,
				 figureBounds.getMinY()+figureBounds.getHeight()/2+bounds.height()/2);
		 g.setFillColor(old);
	 }

}
