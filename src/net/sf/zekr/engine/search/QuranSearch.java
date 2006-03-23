/*
 *               In the name of Allah
 * This file is part of The Zekr Project. Use is subject to
 * license terms.
 *
 * Author:         Mohsen Saboorian
 * Start Date:     Oct 29, 2005
 */
package net.sf.zekr.engine.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.zekr.common.util.IQuranText;
import net.sf.zekr.common.util.QuranLocation;
import net.sf.zekr.common.util.Range;

/**
 * @author Mohsen Saboorian
 * @since Zekr 1.0
 * @version 0.2
 */
public class QuranSearch {
	IQuranText quran;
	boolean match;
	int resultCount;
	int maxAyaMatch = 200;

	/**
	 * will call <code>this(quran, false)</code>
	 * 
	 * @param quran the Quran text to search on
	 */
	public QuranSearch(IQuranText quran) {
		this(quran, false);
	}

	/**
	 * @param quran the Quran text to search on
	 * @param matchDiac will ignore diacritics in search for both keyword and text if
	 *            <code>false</code>
	 */
	public QuranSearch(IQuranText quran, boolean matchDiac) {
		this.quran = quran;
		this.match = matchDiac;
	}

	private List find(String src, String keyword) {
		Finder f;
		if (match) {
			f = new Finder() {
				public Range indexOf(String src, String key) {
					return SearchUtils.indexOfMatchDiacritic(src, key);
				}
			};
		} else {
			f = new Finder() {
				public Range indexOf(String src, String key) {
					return SearchUtils.indexOfIgnoreDiacritic(src, key);
				}
			};
		}

		Range r = f.indexOf(src, keyword);
		if (r == null)
			return null;

		List ret = new ArrayList();
		while (r != null) {
			ret.add(r);
			r = f.indexOf(src, keyword, r.to);
		}

		return ret;
	}

	/**
	 * Finds all occurrences of <code>keyword</code> in <code>IQuranText</code> and
	 * returns it as <code>result</code> <code>Map</code>.
	 * 
	 * @param result return result <code>Map</code>
	 * @param keyword keyword to be found
	 * @return <code>false</code> if too much results found (more than
	 *         <code>maxAyaMatch</code>), otherwise <code>true</code>.
	 */
	public boolean findAll(Map result, String keyword) {
		String aya;
		int ayaNum;
		List l;
		for (int i = 1; i <= 114; i++) {
			ayaNum = quran.getSura(i).length;
			for (int j = 1; j <= ayaNum; j++) {
				aya = quran.get(i, j);
				if ((l = find(aya, keyword)) != null) {
					result.put(new QuranLocation(i, j), l);
					resultCount += l.size();
					if (result.size() >= maxAyaMatch)
						return false;
				}
			}
		}

		return true;
	}

	public int getResultCount() {
		return resultCount;
	}

	public void setMaxAyaMatch(int maxAyaMatch) {
		this.maxAyaMatch = maxAyaMatch;
	}

	public int getMaxAyaMatch() {
		return maxAyaMatch;
	}
}

abstract class Finder {
	public abstract Range indexOf(String src, String key);

	public Range indexOf(String src, String key, int off) {
		Range r = indexOf(src.substring(off), key);
		if (r == null)
			return null;
		return new Range(r.from + off, r.to + off);
	}
}
