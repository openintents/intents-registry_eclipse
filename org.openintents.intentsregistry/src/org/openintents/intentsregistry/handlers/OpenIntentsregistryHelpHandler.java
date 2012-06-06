package org.openintents.intentsregistry.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

/**
 * Show help for the Intents Registry
 */
public class OpenIntentsregistryHelpHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {

	      // Show the help window
	      // PlatformUI.getWorkbench().getHelpSystem().displayHelp();

	      // Show context sensitive help
	      // PlatformUI.getWorkbench().getHelpSystem().displayHelp(
	      // "com.qualityeclipse.favorites.favorites_view");

	      // Show a specific help page
	      PlatformUI.getWorkbench().getHelpSystem().displayHelpResource("/org.openintents.intentsregistry.help/html/toc.html");

	      return null;
	   }

}
