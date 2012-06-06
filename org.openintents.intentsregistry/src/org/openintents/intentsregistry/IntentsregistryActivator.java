package org.openintents.intentsregistry;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * The activator class controls the plug-in life cycle
 */
public class IntentsregistryActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.openintents.intentsregistry"; //$NON-NLS-1$

	// The shared instance
	private static IntentsregistryActivator plugin;
	
	// The configuration preferences
	private IEclipsePreferences configPrefs;
	
	/**
	 * The constructor
	 */
	public IntentsregistryActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/**
	 * This method is called when the plug-in is stopped.
	 */
	public void stop(BundleContext context) throws Exception {
		saveConfigPrefs();
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static IntentsregistryActivator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	/**
	 * Answer the configuration location for this plug-in
	 * 
	 * @return the plugin's config directory (not <code>null</code>)
	 */
	public File getConfigDir() {
		Location location = Platform.getConfigurationLocation();
		if (location != null) {
			URL configURL = location.getURL();
			if (configURL != null && configURL.getProtocol().startsWith("file")) {
				return new File(configURL.getFile(), PLUGIN_ID);
			}
		}
		// If the configuration directory is read-only,
		// then return an alternate location
		// rather than null or throwing an Exception.
		return getStateLocation().toFile();
	}
	
	/**
	 * Answer the configuration preferences shared among multiple workspaces.
	 * 
	 * @return the configuration preferences or <code>null</code> if the
	 *         configuration directory is read-only or unspecified.
	 */
	@SuppressWarnings("deprecation")
	public Preferences getConfigPrefs() {
		if (configPrefs == null)
			configPrefs = new ConfigurationScope().getNode(PLUGIN_ID);
		return configPrefs;
	}
	
	/**
	 * Save the configuration preferences if they have been loaded
	 */
	public void saveConfigPrefs() {
		if (configPrefs != null) {
			try {
				configPrefs.flush();
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Return the plug-in's version
	 */
	public Version getVersion() {
		return new Version((String) getBundle().getHeaders().get(
				org.osgi.framework.Constants.BUNDLE_VERSION));
	}
}
