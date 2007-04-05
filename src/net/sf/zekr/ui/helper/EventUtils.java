/*
 *               In the name of Allah
 * This file is part of The Zekr Project. Use is subject to
 * license terms.
 *
 * Author:         Mohsen Saboorian
 * Start Date:     Jan 23, 2007
 */
package net.sf.zekr.ui.helper;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Mohsen Saboorian
 * @since Zekr 1.0
 */
public class EventUtils {
	/**
	 * Creates and sends an event of type <code>EventProtocol.CUSTOM_ZEKR_EVENT</code> to all shells listening for this
	 * event. <code>Event.data</code> is set as <code>eventName</code> parameter.
	 * 
	 * @param eventName
	 *           <code>Event.data</code> to be sent
	 */
	public static void sendEvent(String eventName) {
		sendEvent(EventProtocol.CUSTOM_ZEKR_EVENT, eventName);
	}

	/**
	 * Creates and sends an event of type <code>eventType</code> to all shells listening for this event.
	 * <code>Event.data</code> is set as <code>eventName</code> parameter.
	 * 
	 * @param eventType
	 *           <code>Event.type</code> to be sent
	 * @param eventName
	 *           <code>Event.data</code> to be sent
	 */
	public static void sendEvent(int eventType, String eventName) {
		Shell shells[] = Display.getCurrent().getShells();
		Event event = createEvent(eventType, eventName);
		for (int i = 0; i < shells.length; i++) {
			if (shells[i].isListening(eventType))
				shells[i].notifyListeners(eventType, event);
		}
	}

	/**
	 * Creates and sends an event of type <code>eventType</code> to the specified shell. <code>Event.data</code> is
	 * set as <code>eventName</code> parameter.
	 * 
	 * @param shell
	 *           event target
	 * @param eventType
	 *           <code>Event.type</code> to be sent
	 * @param eventName
	 *           <code>Event.data</code> to be sent
	 */
	public static void sendEvent(Shell shell, int eventType, String eventName) {
		Event event = createEvent(eventType, eventName);
		shell.notifyListeners(eventType, event);
	}

	/**
	 * Creates and sends an event of type <code>EventProtocol.CUSTOM_ZEKR_EVENT</code> to the specified shell.
	 * <code>Event.data</code> is set as <code>eventName</code> parameter.
	 * 
	 * @param shell
	 *           event target
	 * @param eventName
	 *           <code>Event.data</code> to be sent
	 */
	public static void sendEvent(Shell shell, String eventName) {
		Event event = createEvent(EventProtocol.CUSTOM_ZEKR_EVENT, eventName);
		shell.notifyListeners(EventProtocol.CUSTOM_ZEKR_EVENT, event);
	}

	private static Event createEvent(int eventType, String eventName) {
		Event event = new Event();
		event.data = eventName;
		event.type = eventType;
		return event;
	}

}
