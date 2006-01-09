/*
 *               In the name of Allah
 * This file is part of The Zekr Project. Use is subject to
 * license terms.
 *
 * Author:         Mohsen Saboorian
 * Start Date:     Oct 14, 2004
 */

package net.sf.zekr.engine.template;

import java.io.StringWriter;
import java.io.Writer;

import net.sf.zekr.common.config.ApplicationPath;
import net.sf.zekr.engine.log.Logger;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * An adapter class for velocity template engine.
 * @author Mohsen Saboorian
 * @since Zekr 1.0
 * @version 0.1
 */
public class TemplateEngine {
	VelocityContext context;
	private static TemplateEngine thisInstance;
	Template template;

	/**
	 * Instantiate a sample <code>TemplateEngine</code>
	 */
	private TemplateEngine() {
		try {
			Velocity.setExtendedProperties(new ExtendedProperties(ApplicationPath.VELOCITY_CONFIG));
//			Velocity.addProperty("file.resource.loader.path", ApplicationPath.TEMPLATE_DIR);
			Velocity.init();
			context = new VelocityContext();
		} catch (Exception e) {
			Logger.getLogger(this.getClass()).log(e);
		}
	}

	/**
	 * @return The template engine instance
	 */
	public static TemplateEngine getInstance() {
		if (thisInstance == null) {
			thisInstance = new TemplateEngine();
			return thisInstance;
		}
		thisInstance.resetContext();
		return thisInstance;
	}

	/**
	 * @param name
	 *            The file name of the desired template
	 * @return The associated <code>Template</code> object
	 * @throws Exception
	 */
	public Template getTemplate(String name) throws Exception {
		return Velocity.getTemplate(name);
	}

	/**
	 * Add a key-value pair to the template engine context
	 * 
	 * @param key
	 * @param obj
	 */
	public void put(String key, Object value) {
		context.put(key, value);
	}

	/**
	 * @param name
	 *            The file name of the desired template
	 * @return The result <code>String</code> after the context map is merged (applied)
	 *         into the source template file.
	 * @throws Exception
	 */
	public String getUpdated(String name) throws Exception {
		template = Velocity.getTemplate(name);
		Writer writer = new StringWriter();
		template.merge(context, writer);
		return writer.toString();
	}

	public void resetContext() {
		context = new VelocityContext();
	}

}