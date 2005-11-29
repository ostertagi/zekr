/*
 *               In the name of Allah
 * This file is part of The Zekr Project. Use is subject to
 * license terms.
 *
 * Author:         Mohsen Saboorian
 * Start Date:     Mar 23, 2005
 */
package net.sf.zekr.common.util;

import net.sf.zekr.common.config.ApplicationConfig;
import net.sf.zekr.engine.language.LanguageEngine;

/**
 * @author Mohsen Saboorian
 * @since Zekr 1.0
 * @version 0.1
 */
public class CollectionUtils {
	public static String getLocalizeList(int[] array) {
		StringBuffer ret = new StringBuffer();
		LanguageEngine dict = ApplicationConfig.getInsatnce().getLanguageEngine();
		String delim = dict.getMeaning("COMMA");
		for (int i = 0; i < array.length - 1; i++) {
			ret.append(array[i]);
			ret.append(delim).append("");
		}
		ret.append(array[array.length - 1]);
		return ret.toString();
	}

	public static int[] concat(int[] array1, int[] array2) {
		int[] ret = new int[array1.length + array2.length];
		int i;
		for (i = 0; i < array1.length; i++) {
			ret[i] = array1[i];
		}
		for (int j = 0; i < ret.length; j++, i++) {
			ret[i] = array2[j];
		}
		return ret;
	}
}
