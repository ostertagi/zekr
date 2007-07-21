/*
 *               In the name of Allah
 * This file is part of The Zekr Project. Use is subject to
 * license terms.
 *
 * Author:         Mohsen Saboorian
 * Start Date:     Jan 21, 2005
 */

package net.sf.zekr.common.runtime;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import net.sf.zekr.common.config.ApplicationConfig;
import net.sf.zekr.common.config.GlobalConfig;
import net.sf.zekr.common.resource.IRangedQuranText;
import net.sf.zekr.common.resource.QuranText;
import net.sf.zekr.common.resource.RangedQuranText;
import net.sf.zekr.common.util.UriUtils;
import net.sf.zekr.engine.log.Logger;
import net.sf.zekr.engine.search.SearchScope;
import net.sf.zekr.engine.search.lucene.QuranTextSearcher;
import net.sf.zekr.engine.template.AbstractQuranViewTemplate;
import net.sf.zekr.engine.template.AdvancedQuranSearchResultTemplate;
import net.sf.zekr.engine.template.ITransformer;
import net.sf.zekr.engine.template.MixedViewTemplate;
import net.sf.zekr.engine.template.MultiTranslationViewTemplate;
import net.sf.zekr.engine.template.QuranSearchResultTemplate;
import net.sf.zekr.engine.template.QuranViewTemplate;
import net.sf.zekr.engine.template.TransSearchResultTemplate;
import net.sf.zekr.engine.template.TranslationViewTemplate;
import net.sf.zekr.engine.translation.TranslationData;

/**
 * HTML Creator object.
 * 
 * @author Mohsen Saboorian
 * @since Zekr 1.0
 */
public class HtmlRepository {
	private final static Logger logger = Logger.getLogger(HtmlRepository.class);
	private static ApplicationConfig config = ApplicationConfig.getInstance();

	/**
	 * The method will create a new HTML file if
	 * <ul>
	 * <li>Sura HTML file does not exist at <code>QURAN_CACHE_DIR</code>
	 * <li>HTML file exists but the file size is zero
	 * <li><code>update</code> is true
	 * </ul>
	 * Otherwise the file will be read from the HTML cache.
	 * 
	 * @param sura
	 *           sura number <b>(which is counted from 1) </b>
	 * @param aya
	 *           the aya number (this will affect on the end of the URL, which appends something like: #<code>sura</code>,
	 *           e.g. <code>file:///somepath/sura.html#5</code>. <b>Please note that <code>aya</code> should be sent
	 *           and counted from 1. </b> If <code>aya</code> is 0 the URL will not have <code>#ayaNumber</code> at
	 *           the end of it.
	 * @param update
	 *           Specify whether recreate the HTML file if it also exists.
	 * @return URL to the sura HTML file
	 * @throws HtmlGenerationException
	 */
	public static String getQuranUri(int sura, int aya, boolean update) throws HtmlGenerationException {
		try {
			File file = new File(Naming.getQuranCacheDir() + File.separator + sura + ".html");
			// if the file doesn't exist, or a zero-byte file exists, or if the
			// update flag (which signals to recreate the html file) is set
			if (!file.exists() || file.length() == 0 || update) {
				logger.info("Create simple Quran HTML file: " + file);
				OutputStreamWriter osw = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(file)),
						GlobalConfig.OUT_HTML_ENCODING);
				AbstractQuranViewTemplate aqvt;
				aqvt = new QuranViewTemplate(QuranText.getInstance(), sura, aya);
				osw.write(aqvt.transform());
				osw.close();
			}
			return UriUtils.toURI(file);// + ((aya == 0) ? "" : "#" + aya);
		} catch (Exception e) {
			throw new HtmlGenerationException(e);
		}
	}

	public static String getMixedUri(int sura, int aya, boolean update) throws HtmlGenerationException {
		try {
			TranslationData td = config.getTranslation().getDefault();
			File file = new File(Naming.getMixedCacheDir() + File.separator + sura + "_" + td.id + ".html");
			// if the file doesn't exist, or a zero-byte file exists, or if the
			// update flag (which signals to recreate the html file) is set
			if (!file.exists() || file.length() == 0 || update) {
				logger.info("Create Quran mixed HTML file: " + file);
				OutputStreamWriter osw = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(file)),
						GlobalConfig.OUT_HTML_ENCODING);
				MixedViewTemplate mvt = new MixedViewTemplate(QuranText.getInstance(), td, sura, aya);
				osw.write(mvt.transform());
				osw.close();
			}
			return UriUtils.toURI(file);// + ((aya == 0) ? "" : "#" + aya);
		} catch (Exception e) {
			throw new HtmlGenerationException(e);
		}
	}

	public static String getCustomMixedUri(int sura, int aya, boolean update) throws HtmlGenerationException {
		try {
			List tdList = config.getCustomTranslationList();
			StringBuffer tidList = new StringBuffer();
			for (int i = 0; i < tdList.size(); i++) {
				String tid = ((TranslationData) tdList.get(i)).id;
				tidList.append(tid);
				if (i + 1 < tdList.size())
					tidList.append("-");
			}
			File file = new File(Naming.getMixedCacheDir() + File.separator + sura + "_" + tidList + ".html");
			if (!file.exists() || file.length() == 0 || update) {
				logger.info("Create Quran file: " + file);
				OutputStreamWriter osw = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(file)),
						GlobalConfig.OUT_HTML_ENCODING);
				TranslationData[] transData = (TranslationData[]) tdList.toArray(new TranslationData[] {});
				ITransformer mtvt = new MultiTranslationViewTemplate(QuranText.getInstance(), transData, sura, aya);
				osw.write(mtvt.transform());
				osw.close();
			}
			return UriUtils.toURI(file);// + ((aya == 0) ? "" : "#" + aya);
		} catch (Exception e) {
			throw new HtmlGenerationException(e);
		}
	}

	/**
	 * @param searcher
	 * @param pageNo
	 *           0-based page number
	 * @return generated search result HTML
	 * @throws HtmlGenerationException
	 */
	public static String getAdvancedSearchQuranUri(QuranTextSearcher searcher, int pageNo)
			throws HtmlGenerationException {
		try {
			File file = new File(Naming.getSearchCacheDir() + File.separator + searcher.getRawQuery().hashCode() + "_"
					+ pageNo + ".html");
			logger.info("Create search file: " + file + " for keyword: \"" + searcher.getRawQuery() + "\".");
			OutputStreamWriter osw = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(file)),
					GlobalConfig.OUT_HTML_ENCODING);
			ITransformer qsrt = new AdvancedQuranSearchResultTemplate(searcher, pageNo);
			osw.write(qsrt.transform());
			osw.close();
			return UriUtils.toURI(file);
		} catch (Exception e) {
			throw new HtmlGenerationException(e);
		}
	}

	public static String getSearchQuranUri(String keyword, boolean matchDiac, SearchScope searchScope)
			throws HtmlGenerationException {
		try {
			File file = new File(Naming.getSearchCacheDir() + File.separator + keyword.hashCode() + ".html");
			// if (!file.exists() || file.length() == 0) {
			logger.info("Create search file: " + file + " for keyword: \"" + keyword + "\".");
			OutputStreamWriter osw = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(file)),
					GlobalConfig.OUT_HTML_ENCODING);

			RangedQuranText rqt = new RangedQuranText(QuranText.getSimpleTextInstance(), searchScope);

			ITransformer qsrt = new QuranSearchResultTemplate(rqt, keyword, matchDiac);

			osw.write(qsrt.transform());
			osw.close();
			// }
			return UriUtils.toURI(file);
		} catch (Exception e) {
			throw new HtmlGenerationException(e);
		}
	}

	public static String getSearchTransUri(String keyword, boolean matchDiac, boolean matchCase, SearchScope searchScope)
			throws HtmlGenerationException {
		try {
			TranslationData td = config.getTranslation().getDefault();
			IRangedQuranText eqt = new RangedQuranText(td, searchScope);

			String suffix = "_" + td.id + "_" + matchCase + ".html";
			File file = new File(Naming.getSearchCacheDir() + File.separator + keyword.hashCode() + suffix);

			// if (!file.exists() || file.length() == 0) {
			logger.info("Create search file: " + file + " for keyword: \"" + keyword + "\".");
			OutputStreamWriter osw = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(file)),
					GlobalConfig.OUT_HTML_ENCODING);

			ITransformer gsrt = new TransSearchResultTemplate(eqt, keyword, matchCase);

			osw.write(gsrt.transform());
			osw.close();
			// }
			return UriUtils.toURI(file);
		} catch (Exception e) {
			throw new HtmlGenerationException(e);
		}
	}

	/**
	 * @param sura
	 * @param aya
	 * @return <code>HtmlRepository#getQuranUri(sura, aya, false);</code>
	 */
	public static String getQuranUri(int sura, int aya) throws HtmlGenerationException {
		return getQuranUri(sura, aya, false);
	}

	public static String getTransUri(int sura, int aya) throws HtmlGenerationException {
		try {
			TranslationData td = config.getTranslation().getDefault();
			File file = new File(Naming.getTransCacheDir() + "/" + sura + "_" + td.id + ".html");
			// if the file doesn't exist, or a zero-byte file exists
			if (!file.exists() || file.length() == 0) {
				logger.info("Create simple translation HTML file: " + file);
				OutputStreamWriter osw = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(file)),
						GlobalConfig.OUT_HTML_ENCODING);
				// td.load(); // load if not loaded before
				AbstractQuranViewTemplate qvt = new TranslationViewTemplate(td, sura, aya);
				osw.write(qvt.transform());
				osw.close();
			}
			return UriUtils.toURI(file);// + ((aya == 0) ? "" : "#" + aya);
		} catch (Exception e) {
			throw new HtmlGenerationException(e);
		}
	}

	/**
	 * @param sura
	 * @param aya
	 * @return <code>HtmlRepository#getMixedUri(sura, aya, false);</code>
	 */
	public static String getMixedUri(int sura, int aya) throws HtmlGenerationException {
		return getMixedUri(sura, aya, false);
	}

	/**
	 * @param sura
	 * @param aya
	 * @return <code>HtmlRepository#getCustomMixedUri(sura, aya, false);</code>
	 */
	public static String getCustomMixedUri(int sura, int aya) throws HtmlGenerationException {
		return getCustomMixedUri(sura, aya, false);
	}

}