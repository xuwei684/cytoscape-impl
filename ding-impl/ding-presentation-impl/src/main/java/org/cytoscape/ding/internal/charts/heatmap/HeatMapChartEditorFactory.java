package org.cytoscape.ding.internal.charts.heatmap;

import javax.swing.JComponent;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.ding.internal.util.IconManager;
import org.cytoscape.view.presentation.charts.CyChart;
import org.cytoscape.view.presentation.charts.CyChartEditorFactory;
import org.cytoscape.view.presentation.property.values.CyColumnIdentifierFactory;

public class HeatMapChartEditorFactory implements CyChartEditorFactory<HeatMapLayer> {

	private final CyApplicationManager appMgr;
	private final IconManager iconMgr;
	private final CyColumnIdentifierFactory colIdFactory;
	
	public HeatMapChartEditorFactory(final CyApplicationManager appMgr, final IconManager iconMgr,
			final CyColumnIdentifierFactory colIdFactory) {
		this.appMgr = appMgr;
		this.iconMgr = iconMgr;
		this.colIdFactory = colIdFactory;
	}
	
	@Override
	public Class<? extends CyChart<HeatMapLayer>> getSupportedClass() {
		return (Class<? extends CyChart<HeatMapLayer>>) HeatMapChart.class;
	}

	@Override
	public JComponent createEditor(final CyChart<HeatMapLayer> chart) {
		return new HeatMapChartEditor((HeatMapChart)chart, appMgr, iconMgr, colIdFactory);
	}
}
