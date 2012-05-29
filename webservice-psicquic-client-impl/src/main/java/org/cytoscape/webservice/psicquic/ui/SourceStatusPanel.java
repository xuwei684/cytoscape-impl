package org.cytoscape.webservice.psicquic.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.task.create.CreateNetworkViewTaskFactory;
import org.cytoscape.webservice.psicquic.PSICQUICRestClient;
import org.cytoscape.webservice.psicquic.PSICQUICRestClient.SearchMode;
import org.cytoscape.webservice.psicquic.RegistryManager;
import org.cytoscape.webservice.psicquic.task.ImportNetworkFromPSICQUICTask;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskManager;

public class SourceStatusPanel extends JPanel {

	private static final long serialVersionUID = 6996385373168492882L;

	private static final Color SELECTED_ROW = new Color(0xaa, 0xaa, 0xaa, 200);

	private boolean cancelFlag = false;

	private final RegistryManager manager;
	private final PSICQUICRestClient client;
	private final String query;
	private final CyNetworkManager networkManager;

	private final TaskManager<?,?> taskManager;

	private final SearchMode mode;

	private final CreateNetworkViewTaskFactory createViewTaskFactory;
	
	private volatile boolean enableItem = true;

	/**
	 * Creates new form PSICQUICResultDialog
	 * 
	 */
	public SourceStatusPanel(final String query, final PSICQUICRestClient client, final RegistryManager manager,
			final CyNetworkManager networkManager, final Map<String, Long> result, final TaskManager taskManager,
			SearchMode mode, final CreateNetworkViewTaskFactory createViewTaskFactory) {
		this.manager = manager;
		this.client = client;
		this.query = query;
		this.networkManager = networkManager;
		this.taskManager = taskManager;
		this.mode = mode;
		this.createViewTaskFactory = createViewTaskFactory;

		setTableModel(result);
		initComponents();
		setCoumnWidth();
	}
	
	public void enableComponents(final boolean enable) {
		this.clearButtonActionPerformed(null);
		
		this.resultTable.setEnabled(enable);
		this.resultScrollPane.setEnabled(enable);
		this.selectAllButton.setEnabled(enable);
		this.importNetworkButton.setEnabled(enable);
		this.clearSelectionButton.setEnabled(enable);
		this.setEnabled(enable);
		this.enableItem = enable;
	}

	public Set<String> getSelected() {
		if (cancelFlag)
			return null;

		final Set<String> selectedService = new HashSet<String>();

		TableModel model = this.resultTable.getModel();
		for (int i = 0; i < model.getRowCount(); i++) {
			Boolean selected = (Boolean) model.getValueAt(i, 0);

			if (selected)
				selectedService.add(model.getValueAt(i, 1).toString());
		}

		return selectedService;
	}

	private void setCoumnWidth() {

		resultTable.getTableHeader().setReorderingAllowed(false);
		resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		// Checkbox
		resultTable.getColumnModel().getColumn(0).setPreferredWidth(70);
		// Name
		resultTable.getColumnModel().getColumn(1).setPreferredWidth(150);
		// Number of result
		resultTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		// Status
		resultTable.getColumnModel().getColumn(3).setPreferredWidth(100);

		resultTable.setSelectionBackground(SELECTED_ROW);
		resultTable.setDefaultRenderer(String.class, new StringCellRenderer());

	}

	private void setTableModel(final Map<String, Long> result) {
		final StatusTableModel model = new StatusTableModel();
		model.addColumn("Import");
		model.addColumn("Data Source Name");
		model.addColumn("Tags");
		model.addColumn("Records Found");
		model.addColumn("Status");

		for (final String serviceName : manager.getAllServiceNames()) {
			final Object[] rowValues = new Object[5];

			rowValues[1] = serviceName;
			rowValues[2] = manager.getTagMap().get(serviceName).toString();
			if (result != null) {
				final String targetURL = manager.getActiveServices().get(serviceName);
				if (targetURL != null) {
					Integer count = result.get(targetURL).intValue();
					if (count < 0)
						rowValues[3] = 0;
					else
						rowValues[3] = count;
				} else
					rowValues[3] = 0;
			} else {
				rowValues[3] = 0;
			}

			if (manager.isActive(serviceName)) {
				rowValues[4] = "Active";
				if (((Integer) rowValues[3]) != 0)
					rowValues[0] = true;
				else
					rowValues[0] = false;
			} else {
				rowValues[0] = false;
				rowValues[4] = "Inactive";
			}
			model.addRow(rowValues);

		}
		this.resultTable = new JTable(model);
		model.fireTableDataChanged();
		repaint();
		updateUI();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {
		titlePanel = new javax.swing.JPanel();
		titleLabel = new javax.swing.JLabel();
		resultScrollPane = new javax.swing.JScrollPane();

		buttonPanel = new javax.swing.JPanel();
		importNetworkButton = new javax.swing.JButton();
		cancelButton = new javax.swing.JButton();
		clearSelectionButton = new JButton();
		selectAllButton = new JButton();
		clusterResultCheckBox = new JCheckBox("Mearge Networks (Cluster Result)");

		titlePanel.setBackground(java.awt.Color.white);

		titleLabel.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
		titleLabel.setText("Data Service Status");

		GroupLayout titlePanelLayout = new GroupLayout(titlePanel);
		titlePanel.setLayout(titlePanelLayout);
		titlePanelLayout.setHorizontalGroup(titlePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						titlePanelLayout.createSequentialGroup().addContainerGap().addComponent(titleLabel)
								.addContainerGap(200, Short.MAX_VALUE)));
		titlePanelLayout.setVerticalGroup(titlePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
				titlePanelLayout.createSequentialGroup().addContainerGap().addComponent(titleLabel)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		resultScrollPane.setBackground(java.awt.Color.white);
		resultScrollPane.setViewportView(resultTable);

		buttonPanel.setBackground(java.awt.Color.white);

		importNetworkButton.setText("Import");
		importNetworkButton.setPreferredSize(new java.awt.Dimension(70, 28));
		importNetworkButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				importButtonActionPerformed(evt);
			}
		});

		cancelButton.setText("Close");
		cancelButton.setPreferredSize(new java.awt.Dimension(70, 28));
		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelButtonActionPerformed(evt);
			}
		});

		clearSelectionButton.setText("Clear");
		clearSelectionButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				clearButtonActionPerformed(evt);
			}
		});
		selectAllButton.setText("Select All");
		selectAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				selectAllButtonActionPerformed(evt);
			}
		});
		
		clusterResultCheckBox.setToolTipText("<html><h3>Cluster to single network</h3></html>");

		GroupLayout buttonPanelLayout = new GroupLayout(buttonPanel);
		buttonPanel.setLayout(buttonPanelLayout);
		buttonPanelLayout.setHorizontalGroup(buttonPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						GroupLayout.Alignment.TRAILING,
						buttonPanelLayout.createSequentialGroup().addComponent(clearSelectionButton)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(selectAllButton)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(clusterResultCheckBox)
								.addContainerGap(100, Short.MAX_VALUE).addComponent(cancelButton)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(importNetworkButton).addContainerGap()));
		buttonPanelLayout.setVerticalGroup(buttonPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						GroupLayout.Alignment.TRAILING,
						buttonPanelLayout
								.createSequentialGroup()
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(
										buttonPanelLayout
												.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(clearSelectionButton, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(selectAllButton, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(clusterResultCheckBox, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(importNetworkButton, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(cancelButton, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addContainerGap()));

		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(titlePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(resultScrollPane, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
				.addComponent(buttonPanel));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addComponent(titlePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(resultScrollPane, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(buttonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)));

	}

	private void importButtonActionPerformed(ActionEvent evt) {

		final boolean toBeClustered = clusterResultCheckBox.isSelected();
		final Set<String> targetSources = getSelected();
		final Set<String> sourceURLs = new HashSet<String>();
		for (String source : targetSources) {
			sourceURLs.add(manager.getActiveServices().get(source));
		}

		// Execute Import Task
		final ImportNetworkFromPSICQUICTask networkTask = new ImportNetworkFromPSICQUICTask(query, client,
				networkManager, manager, sourceURLs, mode, createViewTaskFactory, toBeClustered);

		taskManager.execute(new TaskIterator(networkTask));
		
		Window parentWindow = ((Window) getRootPane().getParent());
		parentWindow.pack();
		repaint();
		
		parentWindow.toFront();
	}

	private void cancelButtonActionPerformed(ActionEvent evt) {
		((Window) this.getRootPane().getParent()).dispose();
	}

	private void clearButtonActionPerformed(ActionEvent evt) {
		for (int i = 0; i < resultTable.getRowCount(); i++)
			resultTable.setValueAt(Boolean.FALSE, i, 0);
	}

	private void selectAllButtonActionPerformed(ActionEvent evt) {
		for (int i = 0; i < resultTable.getRowCount(); i++) {
			if (((Number) resultTable.getValueAt(i, 2)).intValue() == 0)
				resultTable.setValueAt(Boolean.FALSE, i, 0);
			else
				resultTable.setValueAt(Boolean.TRUE, i, 0);
		}
	}

	// Variables declaration - do not modify
	private javax.swing.JPanel buttonPanel;
	private javax.swing.JButton cancelButton;
	private javax.swing.JButton importNetworkButton;
	private JButton clearSelectionButton;
	private JButton selectAllButton;
	private JCheckBox clusterResultCheckBox;

	private javax.swing.JScrollPane resultScrollPane;
	private javax.swing.JTable resultTable;
	private javax.swing.JLabel titleLabel;
	private javax.swing.JPanel titlePanel;

	// End of variables declaration

	private final class StatusTableModel extends DefaultTableModel {
		private static final long serialVersionUID = -7798626850196524108L;

		StatusTableModel() {
			super();
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			final int count = (Integer) getValueAt(row, 3);
			final String sourceName = getValueAt(row, 1).toString();
			if (column == 0 && manager.isActive(sourceName) && count != 0)
				return true;
			else
				return false;
		}

		@Override
		public Class<?> getColumnClass(int colIdx) {
			if (colIdx == 0)
				return Boolean.class;
			else if (colIdx == 3)
				return Integer.class;
			else
				return String.class;
		}
	}

	private final class StringCellRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1694430839330920845L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {

			if (value == null) {
				this.setEnabled(false);
				return this;
			}

			final String serviceName = (String) table.getValueAt(row, 1);

			this.setText(value.toString());

			if (!manager.isActive(serviceName))
				this.setForeground(Color.red);
			else
				this.setForeground(table.getForeground());

			if (isSelected)
				this.setBackground(table.getSelectionBackground());
			else
				this.setBackground(table.getBackground());

			this.setHorizontalAlignment(SwingConstants.CENTER);
			
			this.setEnabled(enableItem);

			return this;
		}
	}

}
