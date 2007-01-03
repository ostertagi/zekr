/*
 *               In the name of Allah
 * This file is part of The Zekr Project. Use is subject to
 * license terms.
 *
 * Author:         Mohsen Saboorian
 * Start Date:     Sep 29, 2006
 */
package net.sf.zekr.ui;

import java.util.Iterator;
import java.util.List;

import net.sf.zekr.engine.search.SearchScope;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class ManageScopesForm extends BaseForm {
	private static final String FORM_ID = "MANAGE_SCOPES_FORM";
	private Shell shell;
	private Shell parent;
	private Display display;
	private Composite body;
	private List searchScopeList;
	private org.eclipse.swt.widgets.List listWidget;
	private Button editBut;
	private Button removeBut;
	private Button newBut;
	private boolean canceled = true;
	protected int selectedIndex = -1;

	public ManageScopesForm(Shell parent, List searchScopes) {
		this.parent = parent;
		display = parent.getDisplay();
		this.searchScopeList = searchScopes;
		shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.SYSTEM_MODAL | SWT.RESIZE);
		FillLayout fl = new FillLayout();
		shell.setLayout(fl);
		shell.setText(meaning("TITLE"));
		shell.setImages(new Image[] { new Image(display, resource.getString("icon.editScope16")),
				new Image(display, resource.getString("icon.editScope32")) });

		init();
		shell.pack();
		shell.setSize(300, 300);
	}

	private void init() {
		body = new Composite(shell, langEngine.getSWTDirection());
		body.setLayout(new GridLayout(1, false));

		GridData gd = new GridData(GridData.FILL_BOTH);
		listWidget = new org.eclipse.swt.widgets.List(body, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);

		String[] items = new String[searchScopeList.size()];
		int i = 0;
		for (Iterator iter = searchScopeList.iterator(); iter.hasNext(); i++) {
			SearchScope ss = (SearchScope) iter.next();
			items[i] = ss.toString();
		}

		listWidget.setItems(items);
		listWidget.setLayoutData(gd);
		listWidget.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int c = listWidget.getSelectionCount();
				if (c == 0) {
					removeBut.setEnabled(false);
					editBut.setEnabled(false);
				} else if (c > 1) {
					removeBut.setEnabled(true);
					editBut.setEnabled(false);
				} else {
					removeBut.setEnabled(true);
					editBut.setEnabled(true);
				}
			}
		});
		listWidget.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) {
				int height = listWidget.getItemHeight() * listWidget.getItemCount() - 1;
				if (e.y <= height && listWidget.getSelectionCount() == 1)
					edit();
			}
		});
		listWidget.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.character == SWT.DEL) {
					remove();
				}
			}
		});

		gd = new GridData();
		gd.horizontalAlignment = SWT.LEAD;

		RowLayout rl = new RowLayout(SWT.HORIZONTAL);

		Composite manageButComposite = new Composite(body, SWT.NONE);
		manageButComposite.setLayout(rl);
		manageButComposite.setLayoutData(gd);

		RowData rd = new RowData();
		rd.width = 40;
		newBut = new Button(manageButComposite, SWT.PUSH);
		newBut.setToolTipText(langEngine.getMeaning("NEW"));
		newBut.setImage(new Image(display, resource.getString("icon.add")));
		newBut.setLayoutData(rd);
		newBut.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			};

			public void widgetSelected(SelectionEvent e) {
				SearchScopeForm ssf = new SearchScopeForm(shell);
				if (ssf.open()) {
					SearchScope ss = ssf.getSearchScope();
					searchScopeList.add(ss);
					listWidget.add(ss.toString());
				}
			};
		});

		rd = new RowData();
		rd.width = 40;
		removeBut = new Button(manageButComposite, SWT.PUSH);
		removeBut.setToolTipText(langEngine.getMeaning("REMOVE"));
		removeBut.setImage(new Image(display, resource.getString("icon.remove")));
		removeBut.setLayoutData(rd);
		removeBut.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			};

			public void widgetSelected(SelectionEvent e) {
				remove();
			};
		});

		rd = new RowData();
		rd.width = 40;
		editBut = new Button(manageButComposite, SWT.PUSH);
		editBut.setToolTipText(langEngine.getMeaning("EDIT"));
		editBut.setImage(new Image(display, resource.getString("icon.edit")));
		editBut.setLayoutData(rd);
		editBut.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			};

			public void widgetSelected(SelectionEvent e) {
				edit();
			};
		});

		if (i > 0) {
			listWidget.select(0);
		} else {
			removeBut.setEnabled(false);
			editBut.setEnabled(false);
		}

		gd = new GridData(GridData.FILL_HORIZONTAL);
		new Label(body, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(gd);

		gd = new GridData();
		gd.horizontalAlignment = SWT.TRAIL;

		rl = new RowLayout(SWT.HORIZONTAL);

		Composite butComposite = new Composite(body, SWT.NONE);
		butComposite.setLayout(rl);
		butComposite.setLayoutData(gd);

		rd = new RowData();
		rd.width = 80;
		Button okBut = new Button(butComposite, SWT.PUSH);
		Button cancelBut = new Button(butComposite, SWT.PUSH);
		okBut.setText("&" + langEngine.getMeaning("OK"));
		okBut.setLayoutData(rd);
		okBut.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			};

			public void widgetSelected(SelectionEvent e) {
				canceled = false;
				selectedIndex = listWidget.getSelectionIndex();
				shell.close();
			};
		});
		shell.setDefaultButton(okBut);

		rd = new RowData();
		rd.width = 80;
		cancelBut.setText("&" + langEngine.getMeaning("CANCEL"));
		cancelBut.setLayoutData(rd);
		cancelBut.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			};

			public void widgetSelected(SelectionEvent e) {
				canceled = true;
				shell.close();
			};
		});

	}

	private void remove() {
		int[] indices = listWidget.getSelectionIndices();
		for (int i = indices.length - 1; i >= 0; i--) {
			searchScopeList.remove(indices[i]);
			listWidget.remove(indices[i]);
		}
		if (listWidget.getSelectionCount() == 0) {
			removeBut.setEnabled(false);
			editBut.setEnabled(false);
		}
	}

	private void edit() {
		int index = listWidget.getSelectionIndex();
		SearchScope selectedSearchScope = (SearchScope) searchScopeList.get(index);
		SearchScopeForm ssf = new SearchScopeForm(shell, selectedSearchScope);
		if (ssf.open()) {
			SearchScope ss = ssf.getSearchScope();
			searchScopeList.set(index, ss);
			listWidget.setItem(index, ss.toString());
		}
	}

	public List getSearchScopeList() {
		return searchScopeList;
	}

	/**
	 * This method should be called after OK button pressed.
	 * 
	 * @return selected item index, or -1 if no item selected.
	 */
	public int getSelectedIndex() {
		return selectedIndex;
	}

	/**
	 * @return <code>true</code> if ok pressed, <code>false</code> otherwise.
	 */
	public boolean open() {
		shell.setLocation(FormUtils.getCenter(parent, shell));
		super.show();
		loopEver();
		return !canceled;
	}

	protected Shell getShell() {
		return shell;
	}

	protected Display getDisplay() {
		return display;
	}

	private String meaning(String key) {
		return langEngine.getMeaningById(FORM_ID, key);
	}
}
