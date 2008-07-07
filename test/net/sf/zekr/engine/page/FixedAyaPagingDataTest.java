/*
 *               In the name of Allah
 * This file is part of The Zekr Project. Use is subject to
 * license terms.
 *
 * Author:         Mohsen Saboorian
 * Start Date:     Jun 28, 2008
 */
package net.sf.zekr.engine.page;

import net.sf.zekr.ZekrBaseTest;
import net.sf.zekr.common.resource.IQuranPage;
import net.sf.zekr.common.resource.QuranLocation;
import net.sf.zekr.common.resource.QuranPropertiesUtils;

public class FixedAyaPagingDataTest extends ZekrBaseTest {

	public FixedAyaPagingDataTest() throws Exception {
		super();
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public final void testGetId() {
		FixedAyaPagingData fapd = new FixedAyaPagingData(70);
		assertEquals(fapd.getId(), FixedAyaPagingData.ID);
	}

	public final void testFixedAyaPagingData1() {
		int maxAya = QuranPropertiesUtils.QURAN_AYA_COUNT;
		FixedAyaPagingData fapd = new FixedAyaPagingData(7);

		assertEquals(fapd.getAyaPerPage(), 7);
		assertEquals(fapd.getPageList().size(), maxAya / 7 + (((maxAya - (maxAya / 7))) == 0 ? 0 : 1));
		assertEquals(((IQuranPage) fapd.getPageList().get(0)).getFrom(), new QuranLocation(1, 1));
		assertEquals(((IQuranPage) fapd.getPageList().get(2)).getFrom(), new QuranLocation(2, 8));
		assertEquals(((IQuranPage) fapd.getPageList().get(2)).getFrom().getNext().getNext().getNext().getNext().getNext()
				.getNext().getNext(), ((IQuranPage) fapd.getPageList().get(3)).getFrom());
	}

	public final void testFixedAyaPagingData2() {
		FixedAyaPagingData fapd1 = new FixedAyaPagingData(10);
		IQuranPage p11_10 = (IQuranPage) fapd1.getPageList().get(10);
		IQuranPage p12_10 = (IQuranPage) fapd1.getPageList().get(11);
		FixedAyaPagingData fapd2 = new FixedAyaPagingData(20);
		IQuranPage p6_20 = (IQuranPage) fapd2.getPageList().get(5);
		assertEquals(p12_10.getTo(), p6_20.getTo());
		assertEquals(p11_10.getFrom(), p6_20.getFrom());
	}

	public final void testGetAyaPerPage() {
		FixedAyaPagingData fapd = new FixedAyaPagingData(7);
		assertEquals(fapd.getAyaPerPage(), 7);
		fapd = new FixedAyaPagingData(87);
		assertEquals(fapd.getAyaPerPage(), 87);
	}
}