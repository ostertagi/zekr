/*
 *               In the name of Allah
 * This file is part of The Zekr Project. Use is subject to
 * license terms.
 *
 * Author:         Mohsen Saboorian
 * Start Date:     Sep 10, 2004
 */

package net.sf.zekr.common.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.TransformerException;

import net.sf.zekr.common.resource.Translation;
import net.sf.zekr.common.resource.TranslationData;
import net.sf.zekr.common.runtime.InitRuntime;
import net.sf.zekr.common.runtime.RuntimeConfig;
import net.sf.zekr.common.runtime.RuntimeUtilities;
import net.sf.zekr.engine.language.Language;
import net.sf.zekr.engine.language.LanguageEngine;
import net.sf.zekr.engine.language.LanguagePack;
import net.sf.zekr.engine.log.Logger;
import net.sf.zekr.engine.xml.NodeList;
import net.sf.zekr.engine.xml.XmlReader;
import net.sf.zekr.engine.xml.XmlUtils;
import net.sf.zekr.engine.xml.XmlWriter;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This singleton class reads the config files by the first invocation of
 * <code>getInsatnce()</code>. You can then read any option by using available getter
 * methods.
 * 
 * @author Mohsen Saboorian
 * @since Zekr 1.0
 * @version 0.2
 */
public class ApplicationConfig extends ZekrConfigNaming {
	private final Logger logger = Logger.getLogger(this.getClass());
	private static ApplicationConfig thisInstance;

	private RuntimeConfig runtimeConfig = new RuntimeConfig();
	
	private XmlReader configReader;
	private LanguageEngine langEngine;
	private Language language;
	private ArrayList availableLanguages;

	private Element langElem;
	private Element quranElem;
	private Element viewElem;
	private Element transElem;

	private Translation translation = new Translation();

	private ApplicationConfig() {
		logger.info("Initializing application configurations...");
		configReader = new XmlReader(ApplicationPath.CONFIG_FILE);
		langElem = configReader.getElement(LANGUAGE_TAG);
		quranElem = configReader.getElement(QURAN_TAG);
		viewElem = configReader.getElement(VIEW_TAG);
		transElem = configReader.getElement(TRANSLATIONS_TAG);
		extractLangProps();
		extractRuntimeConfig();
		extractTransProps();
	}

	public static ApplicationConfig getInsatnce() {
		if (thisInstance == null)
			thisInstance = new ApplicationConfig();
		return thisInstance;
	}
	
	/**
	 * Will be called when any setXXX() method has been called to be taken effect.
	 */
	public void reload() {
		thisInstance = new ApplicationConfig();
	}

	/**
	 * This method extracts the application runtime configurations and store it into
	 * RuntimeConfig bean.
	 */
	private void extractRuntimeConfig() {
		runtimeConfig.setLanguage(langElem.getAttribute(ApplicationConfig.CURRENT_LANGUAGE_ATTR));
		runtimeConfig.setTextLayout(getQuranTextLayout());
	}

	/**
	 * This method extracts language properties from the corresponding node in the config
	 * file.
	 */
	private void extractLangProps() {
		language = new Language();
		boolean update = false;

		logger.info("Loading language pack info.");
		NodeList list = XmlUtils.getNodes(langElem, LANGUAGE_PACK_TAG);
		LanguagePack pack;
		Node node;
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			node = (Node) iter.next();
			pack = new LanguagePack();
			pack.setId(XmlUtils.getAttr(node, ID_ATTR));
			pack.setName(XmlUtils.getAttr(node, NAME_ATTR));
			pack.setLatinName(XmlUtils.getAttr(node, LATIN_NAME_ATTR));
			pack.setFile(XmlUtils.getAttr(node, FILE_ATTR));
			language.getLanguageMap().put(pack.getId(), pack);
		}
		logger.info("Available languages are: " + language.getLanguageMap().values());

		String currentLangId = langElem.getAttribute(CURRENT_LANGUAGE_ATTR);
		String defaultLangId = langElem.getAttribute(DEFAULT_LANGUAGE_ATTR);
		if ("".equals(langElem.getAttribute(DEFAULT_LANGUAGE_ATTR))) {
			logger.warn("Can not find default language pack. will set default to \"en\".");
			langElem.setAttribute(DEFAULT_LANGUAGE_ATTR, "en_US");
			defaultLangId = "en_US";
			update = true;
		}
		language.setDefaultLanguagePackId(defaultLangId);
		if ("".equals(langElem.getAttribute(CURRENT_LANGUAGE_ATTR))) {
			currentLangId = RuntimeUtilities.USER_LANGUAGE + "_" + RuntimeUtilities.USER_COUNTRY;
			logger.warn("Current language will be set to \"" + currentLangId + "\".");
			langElem.setAttribute(CURRENT_LANGUAGE_ATTR, currentLangId);
			update = true;
		}
		Node langPackNode = XmlUtils.getElementByNamedAttr(langElem.getChildNodes(), LANGUAGE_PACK_TAG, ID_ATTR, currentLangId);
		if (langPackNode == null) { // language pack details not found
			logger.error("Can not find the language pack with id=\"" + currentLangId + "\".");
			logger.error("Will load the default language pack instead.");
			langPackNode = XmlUtils.getElementByNamedAttr(langElem.getChildNodes(),
					LANGUAGE_PACK_TAG, ID_ATTR, defaultLangId);
			language.setActiveLanguagePack((LanguagePack) language.getLanguageMap().get(defaultLangId));
		} else {
			language.setActiveLanguagePack((LanguagePack) language.getLanguageMap().get(currentLangId));
		}
		
		if (update)
			updateFile();
	}
	
	/**
	 * This method extracts translation properties from the corresponding node in the config
	 * file.
	 */
	private void extractTransProps() {
		TranslationData td;
		logger.info("Loading translation files info.");
		NodeList list = XmlUtils.getNodes(transElem, TRANSLATION_ITEM_TAG);
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Element elem = (Element) iter.next();
			td = new TranslationData();
			td.langId = elem.getAttribute(LANG_ID_ATTR);
			td.transId = elem.getAttribute(TRANS_ID_ATTR);
			td.encoding = elem.getAttribute(ENCODING_ATTR);
			td.file = elem.getAttribute(FILE_ATTR);
			translation.add(td);
		}
	}

	public List getAvailableLanguages() {
		if (availableLanguages != null)
			return availableLanguages;

		Map langMap = language.getLanguageMap();
		availableLanguages = new ArrayList();
		for (Iterator iter = langMap.entrySet().iterator(); iter.hasNext();) {
			Entry elem = (Entry) iter.next();
			availableLanguages.add(elem.getValue());
		}
		return availableLanguages;
	}

	public Node getLanguageNode() {
		return langElem;
	}

	/**
	 * @return application language engine
	 * @see net.sf.zekr.engine.language#getInstance()
	 */
	public LanguageEngine getLanguageEngine() {
		if (langEngine == null)
			langEngine = LanguageEngine.getInstance();
		return langEngine;
	}
	
	public void setCurrentLanguage(String langId) {
		logger.info("Set current language to " + langId);
		language.setActiveLanguagePack(langId);
		LanguageEngine.getInstance().reload();
		try {
			InitRuntime.recreateHtmlCache();
		} catch (IOException e) {
			logger.log(e);
		}
		langElem.setAttribute(CURRENT_LANGUAGE_ATTR, langId);
	}

	/**
	 * @param id config file id
	 * @return  config file path
	 */
	public String getConfigFile(String id) {
		Element elem = XmlUtils.getElementByNamedAttr(XmlUtils.getNodes(quranElem, QURAN_CONFIG_TAG),
			QURAN_CONFIG_TAG, ID_ATTR, id);
		return ApplicationPath.RESOURCE_DIR + "/" + elem.getAttribute(FILE_ATTR);
	}

	public String getQuranText() {
		Element elem = XmlUtils.getElementByNamedAttr(XmlUtils.getNodes(quranElem, QURAN_TEXT_TAG),
			QURAN_TEXT_TAG, ID_ATTR, QURAN_ARABIC_TEXT_ID);

		return ApplicationPath.RESOURCE_DIR + "/" + elem.getAttribute(FILE_ATTR);
	}

	public static String getQuranTrans(String name) {
		return ApplicationPath.TRANSLATION_DIR + "/" + name;
	}

	public String getQuranTextLayout() {
		Element elem = XmlUtils.getElementByNamedAttr(XmlUtils.getNodes(viewElem, ITEM_TAG), ITEM_TAG,
			ID_ATTR, TEXT_LAYOUT_ID);
		return elem.getAttribute(VALUE_ATTR);
	}
	
	public void setQuranTextLayout(String newLayout) {
		Element elem = XmlUtils.getElementByNamedAttr(XmlUtils.getNodes(viewElem, ITEM_TAG), ITEM_TAG,
				ID_ATTR, TEXT_LAYOUT_ID);
		elem.setAttribute(VALUE_ATTR, newLayout);
	}

	public RuntimeConfig getRuntimeConfig() {
		return runtimeConfig;
	}

	public Language getLanguage() {
		return language;
	}

	public void updateFile() {
		logger.info("Update XML configuration file.");
		try {
			XmlWriter.writeXML(configReader.getDocument(), new File(ApplicationPath.CONFIG_FILE));
		} catch (TransformerException e) {
			logger.log(e);
		}
	}

	public Translation getTranslation() {
		return translation;
	}
}
