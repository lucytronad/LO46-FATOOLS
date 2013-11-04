package fr.utbm.tx52.fatools.figures;

import java.util.UUID;

import org.arakhne.afc.ui.vector.Dimension;
import org.arakhne.afc.ui.vector.VectorToolkit;
import org.arakhne.neteditor.fig.anchor.AnchorFigure;
import org.arakhne.neteditor.fig.anchor.InvisibleCircleAnchorFigure;
import org.arakhne.neteditor.fig.factory.AbstractStandardFigureFactory;
import org.arakhne.neteditor.fig.factory.FigureFactoryException;
import org.arakhne.neteditor.fig.figure.Figure;
import org.arakhne.neteditor.fig.subfigure.SubFigure;
import org.arakhne.neteditor.fig.view.ViewComponentConstants;
import org.arakhne.neteditor.formalism.ModelObject;
import org.arakhne.neteditor.formalism.Node;

import fr.utbm.tx52.fatools.constructs.FAAnchor;
import fr.utbm.tx52.fatools.constructs.FAEdge;
import fr.utbm.tx52.fatools.constructs.FAStartState;
import fr.utbm.tx52.fatools.constructs.FAState;
import fr.utbm.tx52.fatools.constructs.FiniteAutomata;

public class FAFigureFactory extends AbstractStandardFigureFactory<FiniteAutomata> {

	public Figure createFigureFor(UUID viewID, FiniteAutomata graph, ModelObject object,
			float x, float y) throws FigureFactoryException {
		Figure fig = null;
		FAAnchor anchor = null;
		if (object instanceof FAState) {
			FAState node = (FAState) object;
			anchor = node.getAnchors().get(0);
			FAStateFigure figure = new FAStateFigure(viewID, x, y);
			figure.setModelObject(node);
			fig = figure;
		}
		else if (object instanceof FAStartState) {
			FAStartState node = (FAStartState) object;
			anchor = node.getAnchors().get(0);
			FAStartStateFigure figure = new FAStartStateFigure(viewID, x, y);
			figure.setModelObject(node);
			fig = figure;
		}

		if (fig!=null && anchor!=null) {
			AnchorFigure<FAAnchor> subfig;
			subfig = createStateAnchorFigure(viewID,
					Math.max(fig.getWidth(), fig.getHeight()));
			subfig.setModelObject(anchor);
			return fig;
		}
		throw new FigureFactoryException();
	}

	public Figure createFigureFor(UUID viewId, FiniteAutomata graph, ModelObject object,
			float x1, float y1, float x2, float y2)
					throws FigureFactoryException {
		if (object instanceof FAEdge) {
			FAEdgeFigure figure = new FAEdgeFigure(viewId, x1, y1, x2, y2);
			figure.setModelObject((FAEdge)object);
			return figure;
		}
		throw new FigureFactoryException();
	}

	public SubFigure createSubFigureInside(UUID viewId, FiniteAutomata graph, Figure parent,
			ModelObject object) {
		if (object instanceof FAAnchor) {
			if (parent instanceof FAStartStateFigure || parent instanceof FAStateFigure) {
				AnchorFigure<FAAnchor> subfig = createStateAnchorFigure(viewId,
						Math.max(parent.getWidth(), parent.getHeight()));
				subfig.setModelObject((FAAnchor)object);
				return subfig;
			}
		}
		throw new FigureFactoryException();
	}

	@Override
	protected Dimension getPreferredNodeSize(Node<?, ?, ?, ?> node) {
		return VectorToolkit.dimension(
                ViewComponentConstants.DEFAULT_MINIMAL_SIZE,
                ViewComponentConstants.DEFAULT_MINIMAL_SIZE);
	}

	private static AnchorFigure<FAAnchor> createStateAnchorFigure(UUID viewID, float size) {
		AnchorFigure<FAAnchor> figure = new InvisibleCircleAnchorFigure<FAAnchor>(
				viewID, 0, 0, size/2f);
		return figure;
	}

}
