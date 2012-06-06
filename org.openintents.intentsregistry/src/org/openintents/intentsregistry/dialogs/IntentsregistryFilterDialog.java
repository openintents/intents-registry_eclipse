package org.openintents.intentsregistry.dialogs;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.openintents.intentsregistry.IntentsregistryActivator;
import org.openintents.intentsregistry.IntentsregistryLog;
import org.openintents.intentsregistry.model.IntentsregistryResource;

public class IntentsregistryFilterDialog extends FilteredItemsSelectionDialog {

	private static final String DIALOG_SETTINGS = "IntentsregistryFilterDialogSettings";
	private boolean onlyLowerCase = false;

	private Action showOnlyLowerCaseStringsAction = new ShowOnlyLowerCaseStringsAction();

	public IntentsregistryFilterDialog(Shell shell, boolean multi) {
		super(shell, multi);
		setTitle("Intents Selection");
		setSelectionHistory(new IntentsregistryResourceSelectionHistory());
	}

	private Button checkButton;

	protected Control createExtendedContentArea(Composite parent) {
		checkButton = new Button(parent, SWT.CHECK);
		checkButton.setText("Only Lower Case Strings"); //$NON-NLS-1$
		checkButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				if (onlyLowerCase != ((Button) e.widget).getSelection()) {
					onlyLowerCase = ((Button) e.widget).getSelection();
					applyFilter();
				}
			}
		});
		return checkButton;
	}

	protected ItemsFilter createFilter() {
		return new ResourceFilter();
	}

	protected void fillContentProvider(AbstractContentProvider contentProvider, ItemsFilter itemsFilter, IProgressMonitor progressMonitor)
			throws CoreException {

		progressMonitor.beginTask("Searching", IntentsregistryResource.getResources().size()); //$NON-NLS-1$
		for (Iterator<String> iter = IntentsregistryResource.getResources().iterator(); 
				iter.hasNext();) {
			contentProvider.add(iter.next(), itemsFilter);
			progressMonitor.worked(1);
		}
		progressMonitor.done();
	}

	protected IDialogSettings getDialogSettings() {
		IDialogSettings settings = IntentsregistryActivator.getDefault().getDialogSettings().getSection(DIALOG_SETTINGS);
		if (settings == null) {
			settings = IntentsregistryActivator.getDefault().getDialogSettings().addNewSection(DIALOG_SETTINGS);
		}
		return settings;
	}

	public String getElementName(Object item) {
		return item.toString();
	}


	@SuppressWarnings("rawtypes")
	protected Comparator<?> getItemsComparator() {
		return new Comparator() {
			public int compare(Object arg0, Object arg1) {
				return arg0.toString().compareTo(arg1.toString());
			}
		};
	}

	protected IStatus validateItem(Object item) {
		return Status.OK_STATUS;
	}

	private class IntentsregistryResourceSelectionHistory extends SelectionHistory {
		/*
		 * @see
		 * org.eclipse.ui.dialogs.FilteredItemsSelectionDialog.SelectionHistory
		 * #restoreItemFromMemento(org.eclipse.ui.IMemento)
		 */
		protected Object restoreItemFromMemento(IMemento element) {
			return element.getString("resource"); //$NON-NLS-1$
		}

		/*
		 * @see
		 * org.eclipse.ui.dialogs.FilteredItemsSelectionDialog.SelectionHistory
		 * #storeItemToMemento(java.lang.Object, org.eclipse.ui.IMemento)
		 */
		protected void storeItemToMemento(Object item, IMemento element) {
			element.putString("resource", item.toString()); //$NON-NLS-1$
		}
	}

	private class ResourceFilter extends ItemsFilter {
		public final boolean onlyLowerCase = IntentsregistryFilterDialog.this.onlyLowerCase;

		public boolean matchItem(Object item) {
			String resource = item.toString();
			if (onlyLowerCase && Character.isUpperCase(resource.charAt(0)))
				return false;
			return matches(resource);
		}

		public boolean equalsFilter(ItemsFilter filter) {
			ResourceFilter resourceFilter = (ResourceFilter) filter;
			if (onlyLowerCase != resourceFilter.onlyLowerCase)
				return false;
			return super.equalsFilter(filter);
		}

		public boolean isSubFilter(ItemsFilter filter) {
			ResourceFilter resourceFilter = (ResourceFilter) filter;
			if (onlyLowerCase != resourceFilter.onlyLowerCase)
				return false;
			return super.isSubFilter(filter);
		}

		public boolean isConsistentItem(Object item) {
			return true;
		}
	}

	private class ShowOnlyLowerCaseStringsAction extends Action {
		/**
		 * Creates a new instance of the action.
		 */
		public ShowOnlyLowerCaseStringsAction() {
			super("Only Lower Case String", //$NON-NLS-1$
					IAction.AS_CHECK_BOX);
		}

		public void run() {
			if (onlyLowerCase != isChecked()) {
				onlyLowerCase = isChecked();
				applyFilter();
			}
		}
	}

	protected void fillViewMenu(IMenuManager menuManager) {
		super.fillViewMenu(menuManager);
		menuManager.add(showOnlyLowerCaseStringsAction);
	}

	protected void applyFilter() {
		super.applyFilter();
		checkButton.setSelection(onlyLowerCase);
		showOnlyLowerCaseStringsAction.setChecked(onlyLowerCase);
	}

	public String getKeyByValue(Map<String, String> map, String value) {
	    for (String entry : map.keySet()) {
	        if (map.get(entry).contains(value)) {
	            return entry;
	        }
	    }
	    return null;
	}
	
	/*
	 * @see Dialog#okPressed()
	 */
	protected void okPressed() {
		IEditorPart editorCurrent = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();

		IStructuredSelection fSelection = getSelectedItems();		
		
		String intent = getKeyByValue(IntentsregistryResource.getResourcez(), fSelection.getFirstElement().toString());
		
		if (editorCurrent instanceof AbstractTextEditor) {

			IDocument document;
			
				AbstractTextEditor editor = (AbstractTextEditor) editorCurrent;
				IEditorInput input = editor.getEditorInput();
				IDocumentProvider provider = editor.getDocumentProvider();
				document = provider.getDocument(input); 
				
				StyledText s = (StyledText) editor.getAdapter(Control.class);		

				try {
					document.replace(s.getCaretOffset(), 0, intent);
					s.setCaretOffset(s.getCaretOffset() + intent.length());					
				} catch (BadLocationException e) {
					IntentsregistryLog.logError(e);
				} 				
			
			if (document != null) {

				System.out.println("document contents:" + document.get());

			}			

		}
	
		super.okPressed();
		
	}

}
