package org.openintents.intentsregistry.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;

public class AddToIntentsregistryHandler extends AbstractHandler {

	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		MessageDialog.openConfirm(null, "Add", "The \"Add to Intentsregistry\" handler was called");
		return null;
	}

}
