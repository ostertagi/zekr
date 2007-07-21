/*
 *               In the name of Allah
 * This file is part of The Zekr Project. Use is subject to
 * license terms.
 *
 * Author:         Mohsen Saboorian
 * Start Date:     Sep 3, 2004
 */

package net.sf.zekr.ui.helper;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * @author Mohsen Saboorian
 * @since Zekr 1.0
 */
public class FormUtils {
	/**
	 * @param display
	 *           The <code>Device</code> to extract it's bounds.
	 * @return A <code>Point</code> containing (maxX, maxY) of device.
	 */
	public static Point getScreenSize(Display display) {
		Rectangle r = display.getBounds();
		return new Point(r.width, r.height);
	}

	/**
	 * This method shall be used to find upper-left position of a <code>Rectangle</code>, which then be centered on
	 * the screen.
	 * 
	 * @param display
	 *           The display to extract it's bounds.
	 * @param widgetSize
	 *           widget size and position.
	 * @return Proper (x, y) value.
	 */
	public static Point getScreenCenter(Display display, Rectangle widgetSize) {
		Point p = getScreenSize(display);
		return new Point((p.x - widgetSize.width) / 2, (p.y - widgetSize.height) / 2);
	}

	public static Table getTableFromMap(Composite parent, Map map, String title1, String title2, int width1, int width2,
			Object layoutData, int style) {
		Table table = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION | style);
		table.setLayoutData(layoutData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableColumn nameCol = new TableColumn(table, SWT.NONE);
		nameCol.setText(title1);
		nameCol.setWidth(width1);

		TableColumn valueCol = new TableColumn(table, SWT.NONE);
		valueCol.setText(title2);
		valueCol.setWidth(width2);

		String key, value;
		for (Iterator iter = map.keySet().iterator(); iter.hasNext();) {
			key = (String) iter.next();
			value = map.get(key).toString();
			new TableItem(table, SWT.NONE).setText(new String[] { key, value });
		}

		return table;
	}

	public static Table getEditableTable(Composite parent, Map map, String title1, String title2, int width1,
			int width2, Object layoutData, int style) {
		Table table = getTableFromMap(parent, map, title1, title2, width1, width2, layoutData, style);
		TableItem[] items = table.getItems();
		Iterator it = map.values().iterator();
		for (int i = 0; i < items.length; i++) {
			TableEditor editor = new TableEditor(table);
			Text text = new Text(table, SWT.NONE);
			editor.grabHorizontal = true;
			editor.setEditor(text, items[i], 1);
			text.setText((String) it.next());
		}
		return table;
	}

	public static void addRow(Table table, String str1, String str2) {
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(0, str1);
		item.setText(1, str2);
	}

	public static void addEditableRow(Table table, String str1, String str2) {
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(0, str1);
		TableEditor editor = new TableEditor(table);
		Text text = new Text(table, SWT.NONE);
		editor.grabHorizontal = true;
		editor.setEditor(text, item, 1);
		text.setText(str2);
	}

	/**
	 * For internal use only.
	 */
	public static void updateTable(Table table, Map map) {
		TableItem[] items = table.getItems();
		String key, value;
		int i = 0;
		for (Iterator iter = map.keySet().iterator(); iter.hasNext(); i++) {
			key = (String) iter.next();
			value = map.get(key).toString();
			items[i].setText(new String[] { key, value });
		}
	}

	/**
	 * @param direction
	 *           can be either <tt>rtl</tt> or <tt>ltr</tt>
	 * @return <code>SWT.RIGHT_TO_LEFT</code> if direction is <tt>rtl</tt> (ignoring the case),
	 *         <code>SWT.LEFT_TO_RIGHT</code> otherwise.
	 */
	public static int toSwtDirection(String direction) {
		return "rtl".equalsIgnoreCase(direction) ? SWT.RIGHT_TO_LEFT : SWT.LEFT_TO_RIGHT;
	}

	public static void removeSelection(Table table) {
		table.remove(table.getSelectionIndices()[0]);
	}

	public static Point getCenter(Shell parent, Shell shell) {
		int x = parent.getLocation().x + parent.getSize().x / 2;
		int y = parent.getLocation().y + parent.getSize().y / 2;
		return new Point(x - (shell.getSize().x / 2), y - (shell.getSize().y / 2));
	}
}