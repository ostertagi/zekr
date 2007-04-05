/*
 *               In the name of Allah
 * This file is part of The Zekr Project. Use is subject to
 * license terms.
 *
 * Author:         Mohsen Saboorian
 * Start Date:     Mar 21, 2005
 */
package net.sf.zekr.common.resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.zekr.common.config.ApplicationConfig;
import net.sf.zekr.common.config.GlobalConfig;
import net.sf.zekr.common.util.CollectionUtils;
import net.sf.zekr.engine.language.LanguageEngine;
import net.sf.zekr.engine.search.Range;

/**
 * This class tightly depends on the class <code>QuranProperties</code>. Almost all public methods on this
 * class cache the static results once called, and if called more, read it from the cache.
 * 
 * @author Mohsen Saboorian
 * @since Zekr 1.0
 */
public class QuranPropertiesUtils {
	private static String[][] suraAyas = new String[114][];
	private static String[] suraNames = new String[114];
	private static String[] indexedSuraNames = new String[114];
	private static List[] juzInside = new ArrayList[114];
	private static List[] sajdaInside = new ArrayList[114];

	public static final int getSajdaType(String sajda) {
		return QuranPropertiesNaming.MINOR_SAJDA.equalsIgnoreCase(sajda) ? SajdaProperties.MINOR
				: SajdaProperties.MAJOR;
	}

	public static final boolean isMadani(String descent) {
		return QuranPropertiesNaming.MADANI.equalsIgnoreCase(descent);
	}

	/**
	 * @param suraNum sura number (counted from 1)
	 * @return a String array of aya numbers in a sura. e.g. ["1", "2", "3", ...]
	 */
	public static final String[] getSuraAyas(int suraNum) {
		int ayas;
		if (suraAyas[0] == null) { // not loaded yet
			QuranProperties props = QuranProperties.getInstance();
			for (int i = 0; i < 114; i++) {
				SuraProperties sura = props.getSura(i + 1);
				ayas = sura.getAyaCount();
				suraAyas[i] = new String[ayas];
				for (int j = 0; j < ayas; j++)
					suraAyas[i][j] = String.valueOf(j + 1);
			}
		}
		return suraAyas[suraNum - 1];
	}

	/**
	 * @return zero relative array of sura names
	 */
	public static final String[] getSuraNames() {
		if (suraNames[0] == null) { // not loaded yet
			QuranProperties props = QuranProperties.getInstance();
			for (Iterator iter = props.getSuraList().iterator(); iter.hasNext();) {
				SuraProperties element = (SuraProperties) iter.next();
				suraNames[element.getIndex() - 1] = element.getName();
			}
		}
		return suraNames;
	}

	/**
	 * @param suraNum (1-based)
	 * @return the <code>SuraProperties</code> object corresponding to <code>suraNum</code>
	 */
	public static final SuraProperties getSura(int suraNum) {
		QuranProperties props = QuranProperties.getInstance();
		return props.getSura(suraNum);
	}

	public static final String[] getIndexedSuraNames() {
		if (indexedSuraNames[0] == null) { // not loaded yet
			QuranProperties props = QuranProperties.getInstance();
			for (Iterator iter = props.getSuraList().iterator(); iter.hasNext();) {
				SuraProperties element = (SuraProperties) iter.next();
				indexedSuraNames[element.getIndex() - 1] = getIndexedSuraName(element.getIndex(), element.getName());
			}
		}
		return indexedSuraNames;
	}

	public static final String getIndexedSuraName(int suraNum, String suraName) {
		return suraName + " - " + suraNum;
	}

	public static final String getIndexedSuraName(int suraNum) {
		return getIndexedSuraName(suraNum, getSura(suraNum).name);
	}

	/**
	 * If there is any juz start within the sura, it will be returned. <br>
	 * This method is the same as <code>getJuzInside()</code>, but with a different return type.
	 * 
	 * @param suraNum sura number (counted from 1)
	 * @return a <code>List</code> of <code>JuzProperties</code>
	 */
	public static final List getJuzInsideSura(int suraNum) {
		if (juzInside[0] == null) { // not loaded yet
			QuranProperties props = QuranProperties.getInstance();
			for (Iterator iter = props.getSuraList().iterator(); iter.hasNext();) {
				SuraProperties sura = (SuraProperties) iter.next();
				List juzNum = new ArrayList();
				List list = props.getJuzList();

				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					JuzProperties juz = (JuzProperties) iterator.next();
					if (juz.getSuraNumber() == sura.getIndex())
						juzNum.add(juz);
				}
				juzInside[sura.getIndex() - 1] = juzNum;
			}
		}
		return juzInside[suraNum - 1];
	}

	/**
	 * If there is any juz start within the sura, it will be returned.
	 * 
	 * @param suraNum sura number (counted from 1)
	 * @return <code>int</code> array of juz numbers
	 */
	public static final int[] getJuzInside(int suraNum) {
		return getJuz(getJuzInsideSura(suraNum));
	}

	private static final int[] getJuz(List list) {
		int[] juzArray = new int[list.size()];
		JuzProperties juz;
		for (int i = 0; i < list.size(); i++) {
			juz = (JuzProperties) list.get(i);
			juzArray[i] = juz.getIndex();
		}
		return juzArray;
	}

	public static final JuzProperties getFirstJuzOf(int suraNum) {
		QuranProperties props = QuranProperties.getInstance();
		List list = props.getJuzList();

		Iterator iter = list.iterator();
		JuzProperties juz1 = (JuzProperties) iter.next();
		while (iter.hasNext()) {
			JuzProperties juz2 = (JuzProperties) iter.next();
			if (suraNum >= juz1.getSuraNumber() && suraNum <= juz2.getSuraNumber()) {
				if (juz2.getSuraNumber() == suraNum && juz2.getAyaNumber() == 1)
					return juz2;
				return juz1;
			}
			juz1 = juz2;
		}
		return juz1; // 30th juz
	}

	/**
	 * @param suraNum sura number (counted from 1)
	 * @return <code>List</code> of <code>SajdaProperties</code> inside <code>suraNum</code>
	 */
	public static final List getSajdaInsideList(int suraNum) {
		if (sajdaInside[0] == null) { // not loaded yet
			QuranProperties props = QuranProperties.getInstance();
			for (Iterator iter = props.getSuraList().iterator(); iter.hasNext();) {
				SuraProperties sura = (SuraProperties) iter.next();
				List sajdaList = new ArrayList();
				List list = props.getSajdaList();

				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					SajdaProperties sajda = (SajdaProperties) iterator.next();
					if (sajda.getSuraNumber() == sura.getIndex())
						sajdaList.add(sajda);
				}
				sajdaInside[sura.getIndex() - 1] = sajdaList;
			}
		}
		return sajdaInside[suraNum - 1];
	}

	/**
	 * @param suraNum sura number (counted from 1)
	 * @return Sura properties as a <code>Map</code>
	 */
	public static final Map getSuraPropsMap(int suraNum) {
		LanguageEngine dict = ApplicationConfig.getInstance().getLanguageEngine();
		QuranPropertiesUtils.getSuraNames(); // load!
		SuraProperties sura = QuranProperties.getInstance().getSura(suraNum);
		Map map = new LinkedHashMap();
		map.put(dict.getMeaning("NAME"), suraNames[suraNum - 1]);
		map.put(dict.getMeaning("NUMBER"), new Integer(sura.getIndex()));
		map.put(dict.getMeaning("AYA_COUNT"), new Integer(sura.getAyaCount()));
		map.put(dict.getMeaning("DESCENT"), getSuraDescent(sura.isMadani()));
		map.put(dict.getMeaning("JUZ"), getSuraJuz(suraNum));

		return map;
	}

	public static final String propsToClipboadrFormat(Map suraProps) {
		StringBuffer ret = new StringBuffer();
		for (Iterator iterator = suraProps.entrySet().iterator(); iterator.hasNext();) {
			Entry entry = (Entry) iterator.next();
			ret.append(entry.getKey() + ": " + entry.getValue() + GlobalConfig.LINE_SEPARATOR);
		}
		return ret.length() > 0 ? ret.substring(0, ret.length() - GlobalConfig.LINE_SEPARATOR.length()) : ret.toString();
	}
	/**
	 * @param isMadani
	 * @return localized <code>String</code> of descent type of a sura (Makki or Madani)
	 */
	public static final String getSuraDescent(boolean isMadani) {
		LanguageEngine dict = ApplicationConfig.getInstance().getLanguageEngine();
		return isMadani ? dict.getMeaning("MADANI") : dict.getMeaning("MAKKI");
	}

	public static final String getSuraJuz(int suraNum) {
		int[] juzList = getJuzInside(suraNum);
		int firstJuz = getFirstJuzOf(suraNum).getIndex();
		if (juzList.length == 0) {
			juzList = new int[1];
			juzList[0] = firstJuz;
		} else if (juzList[0] != firstJuz) {
			juzList = CollectionUtils.concat(new int[] { firstJuz }, juzList);
		}
		return CollectionUtils.getLocalizedList(juzList);
	}

	public static final List getSuraJuzAsList(int suraNum) {
		List retList = new ArrayList(getJuzInsideSura(suraNum));
		JuzProperties firstJuz = getFirstJuzOf(suraNum);
		if (retList.size() == 0) {
			retList.add(firstJuz);
		} else if (retList.get(0) != firstJuz) {
			retList.add(0, firstJuz);
		}
		return retList;
	}
	
	/**
	 * @param juz juz number to find its suras
	 * @return a <code>{@link Range}</code> object whose from and to are both inclusive.
	 */
	public static final Range getSuraInsideJuz(int juz) {
		QuranProperties props = QuranProperties.getInstance();
		int startSura = props.getJuz(juz).getSuraNumber();
		int fromSura, toSura;
		fromSura = startSura;
		if (juz >= 30) {
			toSura = 114;
		} else {
			JuzProperties jp = props.getJuz(juz + 1);
			if (jp.getAyaNumber() > 1)
				toSura = jp.getSuraNumber();
			else
				toSura = jp.getSuraNumber() - 1;
		}
		return new Range(fromSura, toSura);
	}
}
