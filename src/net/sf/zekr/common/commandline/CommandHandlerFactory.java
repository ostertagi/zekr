/*
 *               In the name of Allah
 * This file is part of The Zekr Project. Use is subject to
 * license terms.
 *
 * Author:         Mohsen Saboorian
 * Start Date:     Jul 15, 2007
 */
package net.sf.zekr.common.commandline;

import java.util.ArrayList;
import java.util.List;

/**
 * This class creates command handler classes.
 * 
 * @author Mohsen Saboorian
 * @since Zekr 1.0
 */
public class CommandHandlerFactory implements CommandConstants {
	/**
	 * @param args
	 *           arguments to be parsed
	 * @return a list of <code>CommandHandler</code> items
	 */
	public static List getCommandHandler(String[] args) {
		List commandHandlers = new ArrayList();
		for (int i = 0; i < args.length; i++) {
			String command = args[i].trim();
			if (HELP_COMMAND_VERBOSE.equals(command) || HELP_COMMAND.equals(command)) {
				commandHandlers.add(new HelpCommandHandler(command));
			} else if (CLEAN_COMMAND.equals(command)) {
				List options = new ArrayList();
				i = fillOptions(options, args, i);
				commandHandlers.add(new CleanCommandHandler(command, (String[]) options.toArray(new String[0])));
			} else if (INDEX_COMMAND.equals(command)) {
				List options = new ArrayList();
				i = fillOptions(options, args, i);
				commandHandlers.add(new IndexCommandHandler(command, (String[]) options.toArray(new String[0])));
			} else if (WORKSPACE_COMMAND.equals(command)) {
				List options = new ArrayList();
				i = fillOptions(options, args, i);
				commandHandlers.add(new WorkspaceCommandHandler(command, (String[]) options.toArray(new String[0])));
			} else {
				commandHandlers.add(new OtherCommandHandler(command, null));
			}
		}
		return commandHandlers;
	}

	private static int fillOptions(List options, String[] args, int i) {
		while (i + 1 < args.length && !args[i + 1].startsWith("-")) { // check if there is some options for this command
			i++;
			options.add(args[i]);
		}
		return i;
	}
}
