/*
 *               In the name of Allah
 * This file is part of The Zekr Project. Use is subject to
 * license terms.
 *
 * Author:         Mohsen Saboorian
 * Start Date:     Sep 6, 2004
 */

package net.sf.zekr.ui;

import java.io.IOException;
import java.util.Map;

import net.sf.zekr.common.config.ApplicationConfig;
import net.sf.zekr.common.resource.QuranProperties;
import net.sf.zekr.common.resource.TranslationData;
import net.sf.zekr.common.resource.dynamic.HtmlRepository;
import net.sf.zekr.common.util.QuranLocation;
import net.sf.zekr.common.util.QuranPropertiesUtils;
import net.sf.zekr.engine.log.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

/**
 * Main Zekr form.
 * @author Mohsen Saboorian
 * @since Zekr 1.0
 */
public class QuranForm extends BaseForm {
	// Form widgets:
	private Composite body;
	private Browser quranBrowser;
	private Browser transBrowser;
	private Combo suraSelector;
	private Combo ayaSelector;
	private Label suraLabel;
	private Label ayaLabel;
	private Combo searchText;
	private Button applyButton;
	private Button searchButton;
	private Button sync;
	private Button match;
	private Button whole;
	private Table suraTable;
	private Map suraMap;
	private Group navigationGroup;
	private Group searchGroup;
	private Group navGroup;
	private Group leftGroup;
	private Group detailGroup;
	private SashForm sashForm;
	
	protected int viewLayout;
	protected static final int MIXED = 1;
	protected static final int SEPARATE = 2;
	protected static final int QURAN_ONLY = 3;
	protected static final int TRANS_ONLY = 4;

	private final String ID = "QURAN_FORM";

	private final Logger logger = Logger.getLogger(this.getClass());

	private QuranProperties quranProp;

	/** Specifies whether aya selector changed since a sura was selected. */
	protected boolean ayaChanged;

	/** Specifies whether sura selector changed for making a new sura view. */
	protected boolean suraChanged;

	/** The current Quran URL loaded in the browser */
	private String quranUrl;

	/** The current Translation URL loaded in the browser */
	private String transUrl;

	private ApplicationConfig config;
	private boolean isClosed;

	/**
	 * Initialize the QuranForm.
	 * 
	 * @param display
	 */
	public QuranForm(Display display) {
		this.display = display;
		config = ApplicationConfig.getInstance();
		quranProp = QuranProperties.getInstance();
		init();
	}

	private DisposeListener dl;
	private QuranLocation quranLoc;

	protected boolean updateTrans = true;
	protected boolean updateQuran = true;
	private QuranFormMenuFactory qmf;
	protected boolean clearOnExit = false;

	protected void init() {
		title = langEngine.getMeaningById(ID, "TITLE");
		shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setText(title);
		shell.setImages(new Image[] { new Image(display, resource.getString("icon.form16")),
				new Image(display, resource.getString("icon.form32")) });
		shell.setMenuBar((qmf = new QuranFormMenuFactory(this, shell)).getQuranFormMenu());
		ayaChanged = false;
		suraChanged = false;
		makeFrame();
		setLayout(config.getViewProp("view.viewLayout")); // set the layout
		initLocation();

		dl = new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				close();
				if (!shell.isDisposed())
					shell.removeDisposeListener(dl);
			}
		};
		shell.addDisposeListener(dl);

		shell.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (REFRESH_VIEW.equals(e.data)) {
					reload();
				} else if (RECREATE_VIEW.equals(e.data)) {
					recreate();
				} else if (CLEAR_ON_EXIT.equals(e.data)) {
					clearOnExit = true;
				}
			}
		});
}

	protected void reload() {
		try {
			config.getRuntime().recreateCache();
			suraChanged = true;
			apply();
		} catch (IOException e) {
			logger.log(e);
		}
	}

	private void initLocation() {
//		quranLoc = config.getQuranLocation();
		quranLoc = new QuranLocation(config.getProps().getString("view.quranLoc"));
		logger.info("Loading last visited Quran location: " + quranLoc + ".");
		if (quranLoc.getAya() == 1)
			setQuranView(quranLoc.getSura(), 0);
		else
			setQuranView(quranLoc.getSura(), quranLoc.getAya());
	}

	/**
	 * This method allocates and adds proper widgets to the <b>QuranForm</b>.
	 */
	private void makeFrame() {
		GridData gd;
		GridLayout gl;

		FillLayout fl = new FillLayout(SWT.VERTICAL);
		shell.setLayout(fl);

		GridLayout pageLayout = new GridLayout(2, false);
		body = new Composite(shell, langEngine.getSWTDirection());
		body.setLayout(pageLayout);

		Composite workPane = new Composite(body, SWT.NONE);
		gd = new GridData(GridData.FILL_VERTICAL);
		workPane.setLayoutData(gd);
		gl = new GridLayout(1, false);
		gl.marginHeight = gl.marginWidth = 2;
		workPane.setLayout(gl);

		Composite bgroup = new Composite(body, SWT.NONE);
		gd = new GridData(GridData.FILL_BOTH);
		bgroup.setLayoutData(gd);
		fl = new FillLayout(SWT.VERTICAL);
		fl.marginHeight = fl.marginWidth = 2;
		bgroup.setLayout(fl);

		Composite browsers = new Group(bgroup, SWT.NONE);
		fl = new FillLayout(SWT.VERTICAL);
		browsers.setLayout(fl);

		sashForm = new SashForm(browsers, SWT.SMOOTH | SWT.VERTICAL);
		sashForm.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		sashForm.SASH_WIDTH = 3;

		quranBrowser = new Browser(sashForm, SWT.NONE);
		fl = new FillLayout(SWT.VERTICAL);
		fl.marginHeight = 2;
		quranBrowser.setLayout(fl);
		quranBrowser.addStatusTextListener(new StatusTextListener() {
			public void changed(StatusTextEvent event) {
				if (event.text != null && !"".equals(event.text)) {
					doBrowserCallback(event.text);
				}
			}

			private void doBrowserCallback(String message) {
				if (message.startsWith("ZEKR::")) {
					quranBrowser.execute("window.status='';"); // clear the status text
					if (message.substring(6, 10).equals("GOTO")) {
						int sura = Integer.parseInt(message.substring(message.indexOf(' '),
								message.indexOf('-')).trim());
						int aya = Integer.parseInt(message.substring(message.indexOf('-') + 1,
								message.indexOf(';')).trim());
						logger.info("Goto (" + sura + ", " + aya + ")");
						setQuranView(sura, aya);
					} else if (message.substring(6, 11).equals("TRANS")) {
						int sura = Integer.parseInt(message.substring(message.indexOf(' '),
								message.indexOf('-')).trim());
						int aya = Integer.parseInt(message.substring(message.indexOf('-') + 1,
								message.indexOf(';')).trim());
						logger.info("Show translation: (" + sura + ", " + aya + ")");
						TranslationData td = config.getTranslation().getDefault();
						td.load(); // make sure that translation is loaded.
						PopupBox pe = new PopupBox(shell, langEngine
								.getMeaning("TRANSLATION"), td.get(sura, aya), FormUtils
								.toSwtDirection(td.direction));
						Point p = display.getCursorLocation();
						p.y += 15;
						int x = (int) (quranBrowser.getSize().x * (.5));
						pe.open(new Point(x, 100), new Point(p.x - x / 2, p.y));
					}
				}
			}
		});

		transBrowser = new Browser(sashForm, SWT.NONE);
		fl = new FillLayout(SWT.VERTICAL);
		transBrowser.setLayout(fl);

		navGroup = new Group(workPane, SWT.NONE);
		navGroup.setText(langEngine.getMeaning("OPTION") + ":");
		gl = new GridLayout(2, false);
		navGroup.setLayout(gl);
		navGroup.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL
				| GridData.HORIZONTAL_ALIGN_FILL));

		suraLabel = new Label(navGroup, SWT.NONE);
		suraLabel.setText(langEngine.getMeaning("SURA") + ":");

		suraSelector = new Combo(navGroup, SWT.NONE | SWT.READ_ONLY);
		ayaSelector = new Combo(navGroup, SWT.NONE | SWT.READ_ONLY);

		suraSelector.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		suraSelector.setItems(QuranPropertiesUtils.getIndexedSuraNames());
		suraSelector.setVisibleItemCount(15);
		suraSelector.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				onSuraChanged();
				if (sync.getSelection())
					apply();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				onSuraChanged();
				apply();
			}
		});
		suraSelector.select(0);

		ayaLabel = new Label(navGroup, SWT.NONE);
		ayaLabel.setText(langEngine.getMeaning("AYA") + ":");

		ayaSelector.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_BEGINNING));
		ayaSelector.setItems(QuranPropertiesUtils.getSuraAyas(1));
		ayaSelector.setVisibleItemCount(10);
		ayaSelector.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ayaChanged = true;
				if (sync.getSelection())
					apply();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				ayaChanged = true;
				apply();
			}
		});
		ayaSelector.select(0);
		ayaSelector.moveBelow(ayaLabel);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		ayaSelector.setLayoutData(gd);

		sync = new Button(navGroup, SWT.CHECK);
		sync.setText(langEngine.getMeaning("SYNCHRONOUS"));

		applyButton = new Button(navGroup, SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);

		applyButton.setLayoutData(gd);
		applyButton.setText(langEngine.getMeaning("SHOW"));
		applyButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				apply();
			}
		});

		Composite navComposite = new Composite(navGroup, SWT.NONE);
		gl = new GridLayout(4, false);
		navComposite.setLayout(gl);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		navComposite.setLayoutData(gd);

		int style = SWT.PUSH | SWT.FLAT;
		Button prevSura = new Button(navComposite, style);
		Button prevAya = new Button(navComposite, style);
		Button nextAya = new Button(navComposite, style);
		Button nextSura = new Button(navComposite, style);

		gd = new GridData(GridData.FILL_BOTH);
		prevAya.setLayoutData(gd);
		gd = new GridData(GridData.FILL_BOTH);
		prevSura.setLayoutData(gd);
		gd = new GridData(GridData.FILL_BOTH);
		nextAya.setLayoutData(gd);
		gd = new GridData(GridData.FILL_BOTH);
		nextSura.setLayoutData(gd);

		int l = langEngine.getSWTDirection();
		prevSura.setImage(new Image(display, l == SWT.RIGHT_TO_LEFT ? resource
				.getString("icon.nextNext") : resource.getString("icon.prevPrev")));
		prevAya.setImage(new Image(display, l == SWT.RIGHT_TO_LEFT ? resource
				.getString("icon.next") : resource.getString("icon.prev")));
		nextAya.setImage(new Image(display, l == SWT.RIGHT_TO_LEFT ? resource
				.getString("icon.prev") : resource.getString("icon.next")));
		nextSura.setImage(new Image(display, l == SWT.RIGHT_TO_LEFT ? resource
				.getString("icon.prevPrev") : resource.getString("icon.nextNext")));

		prevSura.setToolTipText(langEngine.getMeaning("PREV_SURA"));
		prevAya.setToolTipText(langEngine.getMeaning("PREV_AYA"));
		nextAya.setToolTipText(langEngine.getMeaning("NEXT_AYA"));
		nextSura.setToolTipText(langEngine.getMeaning("NEXT_SURA"));

		prevSura.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (suraSelector.getSelectionIndex() > 0) {
					suraSelector.select(suraSelector.getSelectionIndex() - 1);
					onSuraChanged();
					apply();
				}
			}
		});

		prevAya.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (ayaSelector.getSelectionIndex() > 0) {
					ayaSelector.select(ayaSelector.getSelectionIndex() - 1);
					ayaChanged = true;
					apply();
				}
			}
		});

		nextAya.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (ayaSelector.getSelectionIndex() < ayaSelector.getItemCount() - 1) {
					ayaSelector.select(ayaSelector.getSelectionIndex() + 1);
					ayaChanged = true;
					apply();
				}
			}
		});

		nextSura.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (suraSelector.getSelectionIndex() < suraSelector.getItemCount() - 1) {
					suraSelector.select(suraSelector.getSelectionIndex() + 1);
					onSuraChanged();
					apply();
				}
			}
		});

		detailGroup = new Group(workPane, SWT.NONE);
		detailGroup.setText(langEngine.getMeaning("DETAILS") + ":");
		gl = new GridLayout(1, true);
		detailGroup.setLayout(gl);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		detailGroup.setLayoutData(gd);
		
		gd = new GridData(GridData.FILL_BOTH);
		gd.grabExcessVerticalSpace = true;
		suraMap = QuranPropertiesUtils.getSuraPropMap(suraSelector.getSelectionIndex() + 1);
		suraTable = FormUtils.getTableForMap(detailGroup, suraMap, langEngine.getMeaning("NAME"),
				langEngine.getMeaning("VALUE"), 70, 70, gd, SWT.NONE);

		searchGroup = new Group(workPane, SWT.NONE);
		searchGroup.setText(langEngine.getMeaning("SEARCH"));
		gl = new GridLayout(2, false);
		searchGroup.setLayout(gl);
		gd = new GridData(GridData.FILL_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL);
		searchGroup.setLayoutData(gd);

		searchText = new Combo(searchGroup, SWT.DROP_DOWN | SWT.RIGHT_TO_LEFT);
		searchText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		searchText.setVisibleItemCount(8);
		searchText.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent e) {
				find();
			}
		});

		searchButton = new Button(searchGroup, SWT.PUSH);
		searchButton.setText(langEngine.getMeaning("SEARCH"));
		searchButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				find();
			}
		});

		KeyAdapter ka = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == 13) {
					find();
				}
			}
		};

		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING, GridData.BEGINNING, true, false);
		gd.horizontalSpan = 2;
		whole = new Button(searchGroup, SWT.CHECK);
		whole.setSelection(true);
		whole.setText(langEngine.getMeaning("WHOLE_QURAN"));
		whole.setLayoutData(gd);
		whole.addKeyListener(ka);

		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING, GridData.BEGINNING, true, false);
		gd.horizontalSpan = 2;
		match = new Button(searchGroup, SWT.CHECK);
		match.setText(langEngine.getMeaning("MATCH_DIACRITIC"));
		match.setLayoutData(gd);
		match.addKeyListener(ka);
		match.pack();
	}

	void apply() {
		logger.info("Start updating view...");
		updateView();
		suraMap = QuranPropertiesUtils.getSuraPropMap(suraSelector.getSelectionIndex() + 1);
		FormUtils.updateTable(suraTable, suraMap);
		logger.info("Updating view done.");
		suraChanged = false;
	}

	private void setQuranView(int sura, int aya) {
		suraSelector.select(sura - 1);
		onSuraChanged();
		if (aya != 0) {
			ayaSelector.select(aya - 1);
			ayaChanged = true;
		}
		apply();
	}

	private ProgressAdapter qpl, tpl;

	protected void updateView() {
		final int aya = ayaSelector.getSelectionIndex() + 1;
		final int sura = suraSelector.getSelectionIndex() + 1;

		quranLoc = new QuranLocation(sura, aya);
		logger.info("Set location to " + quranLoc);
//		config.setQuranLocation(quranLoc);
		config.getProps().setProperty("view.quranLoc", quranLoc.toString());

		qpl = new ProgressAdapter() {
			public void completed(ProgressEvent event) {
				if (ayaChanged) {
					quranBrowser.execute("focusOnAya('" + sura + "_" + aya + "');");
				}
				removeProgressListener(qpl);
			}

			void removeProgressListener(ProgressListener pl) {
				quranBrowser.removeProgressListener(pl);
			}
		};
		tpl = new ProgressAdapter() {
			public void completed(ProgressEvent event) {
				if (ayaChanged) {
					transBrowser.execute("focusOnAya('" + sura + "_" + aya + "');");
				}
				removeProgressListener(tpl);
			}

			void removeProgressListener(ProgressListener pl) {
				transBrowser.removeProgressListener(pl);
			}
		};
		if (updateQuran)
			updateQuranView();
		if (updateTrans)
			updateTransView();
	}

	private void updateTransView() {
		if (suraChanged) {
			transBrowser.addProgressListener(tpl);
			logger.info("Set translation location to " + quranLoc);
			transBrowser.setUrl(transUrl = HtmlRepository.getTransUrl(quranLoc.getSura(), 0));
		} else {
			transBrowser.execute("focusOnAya('" + quranLoc.getSura() + "_" + quranLoc.getAya() + "');");
		}
	}

	private void updateQuranView() {
		if (suraChanged) {
			quranBrowser.addProgressListener(qpl);
			logger.info("Set Quran location to " + quranLoc);
			if (viewLayout == MIXED)
				quranBrowser.setUrl(quranUrl = HtmlRepository.getMixedUrl(quranLoc.getSura(), 0));
			else
				quranBrowser.setUrl(quranUrl = HtmlRepository.getQuranUrl(quranLoc.getSura(), 0));
		} else {
			quranBrowser.execute("focusOnAya('" + quranLoc.getSura() + "_" + quranLoc.getAya() + "');");
		}
	}

	
	private void onSuraChanged() {
		ayaSelector
				.setItems(QuranPropertiesUtils.getSuraAyas(suraSelector.getSelectionIndex() + 1));
		ayaSelector.select(0);
		ayaChanged = false; // It must be set to true after ayaSelector.select
		suraChanged = true; // It must be set to false after apply()
	}

	void find() {
		String str = searchText.getText();
		if (searchText.getItemCount() <= 0 || !str.equals(searchText.getItem(0)))
			if (!"".equals(str))
				searchText.add(str, 0);
		if (searchText.getItemCount() > 40)
			searchText.remove(40, searchText.getItemCount() - 1);
		if (!"".equals(str.trim()) && str.indexOf('$') == -1 && str.indexOf('\\') == -1) {
			if (whole.getSelection()) {
				ayaChanged = true;
				suraChanged = true;
				logger.info("Will search the whole Quran for \"" + str
						+ "\" with dicritic match set to " + match.getSelection() + ".");
				quranBrowser.setUrl(quranUrl = HtmlRepository.getSearchQuranUrl(str, match
						.getSelection()));
				logger.info("End of search.");
			} else {
				logger.info("Start searching the current view for \"" + str
						+ "\" with diacritic match set to " + match.getSelection() + ".");
				if (viewLayout != TRANS_ONLY)
					quranBrowser.execute("find(\"" + str + "\", " + match.getSelection() + ");");
				else
					transBrowser.execute("find(\"" + str + "\", " + match.getSelection() + ");");
				logger.info("End of search.");
			}
		}
	}

	void recreate() {
		logger.info("Recreating the form...");
		Point size = shell.getSize();
		Point loc = shell.getLocation();
		boolean mx = shell.getMaximized();
		shell.close();
		init();
		shell.setLocation(loc);
		shell.setMaximized(mx);
		show(size.x, size.y);
	}

	/**
	 * Shows maximized Quran shell.
	 */
	public void show() {
		shell.setMaximized(true);
		shell.open();
	}

	public void show(int width, int height) {
		shell.setSize(width, height);
		shell.open();
	}

	public Browser getQuranBrowser() {
		return quranBrowser;
	}

	public void setQuranBrowser(Browser quranBrowser) {
		this.quranBrowser = quranBrowser;
	}

	public String getQuranUrl() {
		return quranUrl;
	}

	public String getCurrentUrl() {
		if (viewLayout == TRANS_ONLY)
			return transUrl;
		else
			return quranUrl;
	}

	protected void setLayout(String layout) {
		logger.info("Set layout to " + layout);
		if (layout.equals(ApplicationConfig.QURAN_ONLY_LAYOUT)) {
			sashForm.setMaximizedControl(quranBrowser);
			viewLayout = QURAN_ONLY;
			updateQuran = true;
			updateTrans = false;
		} else if (layout.equals(ApplicationConfig.TRANS_ONLY_LAYOUT)) {
			sashForm.setMaximizedControl(transBrowser);
			viewLayout = TRANS_ONLY;
			updateQuran = false;
			updateTrans = true;
		} else if (layout.equals(ApplicationConfig.SEPARATE_LAYOUT)) {
			if (viewLayout == SEPARATE) // if already is separate, balance weights
				sashForm.setWeights(new int[] {1, 1});
			sashForm.setMaximizedControl(null);
			viewLayout = SEPARATE;
			updateQuran = true;
			updateTrans = true;
		} else if (layout.equals(ApplicationConfig.MIXED_LAYOUT)) {
			sashForm.setMaximizedControl(quranBrowser);
			viewLayout = MIXED;
			updateQuran = true;
			updateTrans = false;
		}
	}

	public void close() {
		config.updateFile();
		if (clearOnExit) {
			logger.info("Clear cache directory.");
			config.getRuntime().clearCache();
		}
		logger.info("Disposing all resources...");
	}

}
