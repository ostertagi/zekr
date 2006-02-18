/*
 *               In the name of Allah
 * This file is part of The Zekr Project. Use is subject to
 * license terms.
 *
 * Author:         Mohsen Saboorian
 * Start Date:     Aug 4, 2005
 */
package net.sf.zekr.common.config;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * This class is used to handle dynamic resource bundles which use <i>Velocity</i> as the
 * template engine.
 * 
 * @author Mohsen Saboorian
 * @since Zekr 1.0
 * @version 0.1
 */
public class ResourceManager {
	private static ResourceBundle resource;
	private static ResourceManager thisInstance;

	private ResourceManager() {
		try {
			resource = new PropertyResourceBundle(new VelocityInputStream(
					"res/resource-path.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ResourceManager getInstance() {
		if (thisInstance == null)
			return (thisInstance = new ResourceManager());
		return thisInstance;
	}

	public String getString(String key) {
		return resource.getString(key);
	}
}
