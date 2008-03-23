/*
 *               In the name of Allah
 * This file is part of The Zekr Project. Use is subject to
 * license terms.
 *
 * Author:         Mohsen Saboorian
 * Start Date:     Mar 22, 2008
 */
package net.sf.zekr.common.resource.filter;

import java.util.regex.Pattern;

import net.sf.zekr.engine.search.tanzil.RegexSeachUtils;

import org.apache.commons.lang.StringUtils;

public class QuranFilterUtils {
	private static Pattern SEARCH_RESULT_SIGN = Pattern.compile(RegexSeachUtils
			.regTrans("[$HIGH_SALA-$RUB_EL_HIZB$SAJDAH]"));

	private static final String ALEFMADDA = RegexSeachUtils.regTrans("$ALEF$MADDA");
	private static final String ALEF_WITH_MADDA_ABOVE = RegexSeachUtils.regTrans("$ALEF_WITH_MADDA_ABOVE");

	public static final String filterSearchResult(String text) {
		text = SEARCH_RESULT_SIGN.matcher(text).replaceAll("");
		text = StringUtils.replace(text, ALEFMADDA, ALEF_WITH_MADDA_ABOVE);
		return text;

	}

	private static final Pattern HARAKA = Pattern.compile(RegexSeachUtils.regTrans("[$HARAKA]"));
	private static final Pattern SIGN = Pattern.compile(RegexSeachUtils.regTrans("[$HIGH_SALA-$LOW_MEEM]"));

	public static String filterHarakat(String text) {
		return HARAKA.matcher(text).replaceAll("");
	}

	public static String filterSign(String text) {
		return SIGN.matcher(text).replaceAll("");
	}

	private static final Pattern TEH = Pattern.compile(RegexSeachUtils.regTrans("[$TEH$MARBUTA]"));
	private static final Pattern ALEF = Pattern.compile(RegexSeachUtils.regTrans("[$ALEF$"
			+ "ALEF_WITH_MADDA_ABOVE$ALEF_WITH_HAMZA_ABOVE$ALEF_WITH_HAMZA_BELOW$ALEF_WASLA]"));
	private static final Pattern WAW = Pattern
			.compile(RegexSeachUtils.regTrans("[$WAW$WAW_WITH_HAMZA_ABOVE$SMALL_WAW]"));
	private static final Pattern YEH = Pattern.compile(RegexSeachUtils.regTrans("[$YEH$ALEF_MAKSURA"
			+ "$YEH_WITH_HAMZA$SMALL_YEH]"));

	public static String filterSimilarCharacters(String text) {
		text = RegexSeachUtils.pregReplace(text, TEH, "$TEH");
		text = RegexSeachUtils.pregReplace(text, ALEF, "$ALEF");
		text = RegexSeachUtils.pregReplace(text, WAW, "$WAW");
		text = RegexSeachUtils.pregReplace(text, YEH, "$YEH");
		return text;
	}

	private static final Pattern SPACE = Pattern.compile("\\s+");

	public static String filterExtraWhiteSpaces(String text) {
		return SPACE.matcher(text).replaceAll(" ");
	}
}