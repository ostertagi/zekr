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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.zekr.common.config.ApplicationConfig;
import net.sf.zekr.common.config.GlobalConfig;
import net.sf.zekr.common.util.CollectionUtils;
import net.sf.zekr.engine.language.LanguageEngine;
import net.sf.zekr.engine.revelation.RevelationData;
import net.sf.zekr.engine.search.Range;

// TODO: all the caching items on this class should be gradually moved 
// to a normal class which is instantiated and hold in ApplicationConfig.
// Only a single instance of this class is created then.

/**
 * This class tightly depends on the class <code>QuranProperties</code>. Almost all public methods on this
 * class cache the static results once called, and if called more, read it from the cache.<br>
 * <br>
 * This cache is not designed to be thread-safe. It is highly unsafe!
 * 
 * @author Mohsen Saboorian
 */
public class QuranPropertiesUtils {
	/** Number of Quran ayas based on Uthmani Mushaf */
	public static int QURAN_AYA_COUNT = 6236;

	/** Number of Quran suras */
	public static int QURAN_SURA_COUNT = 114;

	private static String[][] suraAyas = new String[114][];
	private static String[] suraNames = new String[114];
	private static String[] indexedSuraNames = new String[114];
	private static List[] juzInside = new ArrayList[114];
	private static List[] sajdaInside = new ArrayList[114];
	private static int[] aggrAyaCount = new int[114];
	private static int[] revelOrder = new int[QURAN_AYA_COUNT];
	private static IQuranLocation[] absoluteLocation = new IQuranLocation[QURAN_AYA_COUNT];
	private static IQuranLocation[][] locationCache = new IQuranLocation[QURAN_SURA_COUNT][];

	public static final int getSajdaType(String sajda) {
		return QuranPropertiesNaming.RECOMMENDED_SAJDA.equalsIgnoreCase(sajda) ? SajdaProperties.MINOR
				: SajdaProperties.MAJOR;
	}

	public static final boolean isMadani(String descent) {
		return QuranPropertiesNaming.MADANI.equalsIgnoreCase(descent);
	}

	public static List getSajdaList() {
		return QuranProperties.getInstance().getSajdaList();
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

	public static final void resetIndexedSuraNames() {
		indexedSuraNames[0] = null;
	}

	public static final String[] getIndexedSuraNames() {
		if (indexedSuraNames[0] == null) { // not loaded yet
			QuranProperties props = QuranProperties.getInstance();
			for (Iterator iter = props.getSuraList().iterator(); iter.hasNext();) {
				SuraProperties sura = (SuraProperties) iter.next();
				indexedSuraNames[sura.getIndex() - 1] = getIndexedSuraName(sura);
			}
		}
		return indexedSuraNames;
	}

	public static final String getIndexedSuraName(int suraNum, String suraName) {
		return suraNum + ". " + suraName;
	}

	public static final String getIndexedSuraName(SuraProperties sura) {
		return getIndexedSuraName(sura.getIndex(), sura.toText());
	}

	public static final String getIndexedSuraName(int suraNum) {
		return getIndexedSuraName(getSura(suraNum));
	}

	public static final JuzProperties getJuz(int juzNum) {
		QuranProperties props = QuranProperties.getInstance();
		return props.getJuz(juzNum);
	}

	public static final SajdaProperties getSajda(int sajdaNum) {
		QuranProperties props = QuranProperties.getInstance();
		return props.getSajda(sajdaNum);
	}

	/**
	 * Find and return the sajda for this location.
	 * 
	 * @param location
	 * @return corresponding SajdaProperties (if any), or <code>null</code>, if there is no sajda for this
	 *         location.
	 */
	public static final SajdaProperties getSajda(IQuranLocation location) {
		List sajdaList = getSajdaInsideList(location.getSura());
		for (int i = 0; i < sajdaList.size(); i++) {
			SajdaProperties sajda = (SajdaProperties) sajdaList.get(i);
			if (sajda.getAyaNumber() == location.getAya())
				return sajda;
		}
		return null;
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

	public static final JuzProperties getJuzOf(IQuranLocation iql) {
		return getJuzOf(iql.getSura(), iql.getAya());
	}

	public static final JuzProperties getJuzOf(int suraNum, int ayaNum) {
		List juzList = getSuraJuzAsList(suraNum);
		JuzProperties juz = (JuzProperties) juzList.get(0);
		for (int i = 1; i < juzList.size(); i++) {
			JuzProperties j = (JuzProperties) juzList.get(i);
			if (suraNum > j.getSuraNumber() || (suraNum == j.getSuraNumber() && ayaNum >= j.getAyaNumber())) {
				juz = j;
			} else {
				break;
			}
		}
		return juz;
	}

	/**
	 * @return a number between 0 to 7, meaning the hizb quarter in a juz. For example 0 means the first
	 *         quranter of Hizb 1, 4 means the first quarter of Hizb 2, and 7 means the third quarter of Hizb
	 *         2.
	 */
	public static final int getHizbQuadIndex(int suraNum, int ayaNum) {
		JuzProperties juz = getJuzOf(suraNum, ayaNum);
		IQuranLocation hizbQuads[] = juz.getHizbQuarters();
		int quadIndex = 0;
		for (int i = 1; i < hizbQuads.length; i++) {
			IQuranLocation ql = hizbQuads[i];
			if (suraNum > hizbQuads[i].getSura() || (suraNum == ql.getSura() && ayaNum >= ql.getAya())) {
				quadIndex = i;
			} else {
				break;
			}
		}
		return quadIndex;
	}

	public static int getHizbQuadIndex(IQuranLocation quranLoc) {
		return getHizbQuadIndex(quranLoc.getSura(), quranLoc.getAya());
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
		RevelationData rd = ApplicationConfig.getInstance().getRevelation().getDefault();
		QuranPropertiesUtils.getSuraNames(); // load!
		SuraProperties sura = QuranProperties.getInstance().getSura(suraNum);
		Map map = new LinkedHashMap();
		map.put(dict.getMeaning("NAME"), getSura(suraNum).toText());
		map.put(dict.getMeaning("NUMBER"), new Integer(sura.getIndex()));
		map.put(dict.getMeaning("AYA_COUNT"), new Integer(sura.getAyaCount()));
		map.put(dict.getMeaning("DESCENT"), getSuraDescent(sura.isMadani()));
		map.put(dict.getMeaning("JUZ"), getSuraJuz(suraNum));
		if (rd != null)
			map.put(dict.getMeaning("REVEL_ORDER"), String.valueOf(rd.getOrder(suraNum)));
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

	/**
	 * Returns aggregative sum of aya count from sura 1 to sura i<br>
	 * (for sura: 1 to suraNum parameter) &#8721;(<i>aya count</i>)
	 * 
	 * @param suraNum sura number (counted from 1)
	 * @return the sum of aya count from sura 1 to suraNum - 1.
	 */
	public static final int getAggregateAyaCount(int suraNum) {
		if (aggrAyaCount[113] == 0) { // not loaded yet
			int k = 0;
			for (int i = 0; i < 114; i++) {
				aggrAyaCount[i] = k;
				k += getSura(i + 1).getAyaCount();
			}
		}
		return aggrAyaCount[suraNum - 1];
	}

	/**
	 * @param quranPage
	 * @return a list of Quran locations of type {@link IQuranPage}.
	 */
	public static final List getPageData(IQuranPage quranPage) {
		List locList = new ArrayList();
		IQuranLocation from = quranPage.getFrom();
		IQuranLocation to = quranPage.getFrom();
		while (from != null && to.compareTo(from) >= 0) {
			locList.add(from);
			from = from.getNext();
		}
		return locList;
	}

	/**
	 * @param quranText
	 * @param quranPage
	 * @return a list of Quran locations of type {@link IQuranPage}.
	 */
	public static final List getPageData(IQuranText quranText, IQuranPage quranPage) {
		List ayaList = new ArrayList();
		IQuranLocation from = quranPage.getFrom();
		IQuranLocation to = quranPage.getFrom();
		while (from != null && to.compareTo(from) >= 0) {
			ayaList.add(quranText.get(from));
			from = from.getNext();
		}
		return ayaList;
	}

	public static void updateLocalizedSuraNames() {
		QuranProperties props = QuranProperties.getInstance();
		props.quranPropsReader.updateLocalizedSuraNames();
	}

	public static int getRevelationOrder(IQuranLocation location) {
		return 0;
	}

	/**
	 * @return all Quran locations as an array. The size of the array is
	 *         {@link QuranPropertiesUtils#QURAN_AYA_COUNT}.
	 */
	public static IQuranLocation[] getLocations() {
		QuranProperties props = QuranProperties.getInstance();
		if (absoluteLocation[0] == null) { // not loaded yet
			absoluteLocation = new IQuranLocation[QURAN_AYA_COUNT];
			int absolute = 0;
			for (int sura = 1; sura <= 114; sura++) {
				int ayaCount = props.getSura(sura).getAyaCount();
				locationCache[sura - 1] = new IQuranLocation[ayaCount];
				IQuranLocation[] suraLoc = locationCache[sura - 1];
				for (int aya = 1; aya <= ayaCount; aya++) {
					absoluteLocation[absolute] = new QuranLocation(sura, aya);
					suraLoc[aya - 1] = absoluteLocation[absolute];
					absolute++;
				}
			}
		}
		return absoluteLocation;
	}

	/**
	 * @param absoluteAyaNum a positive number between 1 and 6236.
	 * @return an IQuranLocation instance for this aya.
	 */
	public static final IQuranLocation getLocation(int absoluteAyaNum) {
		return getLocations()[absoluteAyaNum - 1];
	}

	public static final boolean isValid(int sura, int aya) {
		QuranProperties qp = QuranProperties.getInstance();
		return between(sura, 1, QuranPropertiesUtils.QURAN_SURA_COUNT) && between(aya, 1, qp.getSura(sura).getAyaCount());
	}

	public static final boolean isValid(IQuranLocation quranLocation) {
		return isValid(quranLocation.getSura(), quranLocation.getAya());
	}

	private final static boolean between(int num, int from, int to) {
		return num >= from && num <= to;
	}

	/**
	 * This method returns a Quran location, which is looked up from a cache of al Quran location. Since
	 * {@link IQuranLocation} is immutable, it can be shared.
	 * 
	 * @param sura (counted from 1)
	 * @param aya (counted from 1)
	 * @return a cached Quran location
	 */
	public static final IQuranLocation getLocation(int sura, int aya) {
		return locationCache[sura - 1][aya - 1];
	}

	/**
	 * @param iql Quran location to be looked up
	 * @return Quran location absolute aya index or -1 if nothing found.
	 */
	public static final int getAbsoluteLocation(IQuranLocation iql) {
		for (int i = 0; i < absoluteLocation.length; i++) {
			if (iql.equals(absoluteLocation[i]))
				return i;
		}
		return -1;
	}

	/**
	 * @param loc1 first location
	 * @param loc2 second location
	 * @return absolute aya number for location 1 - absolute number for location 2
	 */
	public static final int diff(IQuranLocation loc1, IQuranLocation loc2) {
		return getAbsoluteLocation(loc1) - getAbsoluteLocation(loc2);
	}
}
