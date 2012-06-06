package org.openintents.intentsregistry.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.openintents.intentsregistry.dialogs.IntentsregistryFilterDialog;

/**
 * Open the intents registry filter dialog
 */
public class OpenIntentsregistryFilterDialogHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		new IntentsregistryFilterDialog(HandlerUtil.getActiveShell(event), isEnabled()).open();
		return null;
	}
}
