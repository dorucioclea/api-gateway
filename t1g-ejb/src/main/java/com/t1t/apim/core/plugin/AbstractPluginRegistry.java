package com.t1t.apim.core.plugin;

import com.t1t.apim.common.plugin.*;
import com.t1t.apim.core.IPluginRegistry;
import com.t1t.apim.core.exceptions.InvalidPluginException;
import com.t1t.apim.core.i18n.Messages;
import com.t1t.apim.exceptions.ExceptionFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Serves as a common base class for concrete implementations of {@link IPluginRegistry}.
 *
 */
public abstract class AbstractPluginRegistry implements IPluginRegistry {

    private File pluginsDir;
    private Map<PluginCoordinates, Plugin> pluginCache = new HashMap<>();
    private Object mutex = new Object();

    /**
     * Constructor.
     * @param pluginsDir the plugin's directory
     */
    public AbstractPluginRegistry(File pluginsDir) {
        this.setPluginsDir(pluginsDir);
    }

    /**
     * @see IPluginRegistry#loadPlugin(PluginCoordinates)
     */
    @Override
    public Plugin loadPlugin(PluginCoordinates coordinates) throws InvalidPluginException {
        if (pluginCache.containsKey(coordinates)) {
            return pluginCache.get(coordinates);
        }

        synchronized (mutex) {
            String pluginRelativePath = PluginUtils.getPluginRelativePath(coordinates);
            File pluginDir = new File(pluginsDir, pluginRelativePath);
            if (!pluginDir.exists()) {
                pluginDir.mkdirs();
            }
            File pluginFile = new File(pluginDir, "plugin." + coordinates.getType()); //$NON-NLS-1$
            // Doesn't exist?  Better download it
            if (!pluginFile.exists()) {
                downloadPlugin(pluginFile, coordinates);
            }
            // Still doesn't exist?  That's a failure.
            if (!pluginFile.exists()) {
                throw new InvalidPluginException(Messages.i18n.format("AbstractPluginRegistry.PluginNotFound")); //$NON-NLS-1$
            }
            PluginClassLoader pluginClassLoader;
            try {
                pluginClassLoader = createPluginClassLoader(pluginFile);
            } catch (IOException e) {
                throw new InvalidPluginException(Messages.i18n.format("AbstractPluginRegistry.InvalidPlugin", pluginFile.getAbsolutePath()), e); //$NON-NLS-1$
            }
            URL specFile = pluginClassLoader.getResource(PluginUtils.PLUGIN_SPEC_PATH);
            if (specFile == null) {
                throw new InvalidPluginException(Messages.i18n.format("AbstractPluginRegistry.MissingPluginSpecFile", PluginUtils.PLUGIN_SPEC_PATH)); //$NON-NLS-1$
            }
            try {
                PluginSpec spec = PluginUtils.readPluginSpecFile(specFile);
                Plugin plugin = new Plugin(spec, coordinates, pluginClassLoader);
                return plugin;
            } catch (Exception e) {
                throw new InvalidPluginException(Messages.i18n.format("AbstractPluginRegistry.FailedToReadSpecFile", PluginUtils.PLUGIN_SPEC_PATH), e); //$NON-NLS-1$
            }
        }
    }

    /**
     * Creates a plugin classloader for the given plugin file.
     * @param pluginFile
     * @throws IOException
     */
    protected PluginClassLoader createPluginClassLoader(final File pluginFile) throws IOException {
        PluginClassLoader cl = new PluginClassLoader(pluginFile, Thread.currentThread().getContextClassLoader()) {
            @Override
            protected File createWorkDir(File pluginArtifactFile) throws IOException {
                File workDir = new File(pluginFile.getParentFile(), ".work"); //$NON-NLS-1$
                workDir.mkdirs();
                return workDir;
            }
        };
        return cl;
    }

    /**
     * Downloads the plugin via its maven GAV information.  This will first look in the local
     * .m2 directory.  If the plugin is not found there, then it will try to download the
     * plugin from one of the configured remote maven repositories.
     * @param pluginFile
     * @param coordinates
     */
    protected void downloadPlugin(File pluginFile, PluginCoordinates coordinates) {
        // First check the .m2 directory
        File m2Dir = PluginUtils.getUserM2Repository();
        if (m2Dir != null) {
            File artifactFile = PluginUtils.getM2Path(m2Dir, coordinates);
            if (artifactFile.isFile()) {
                try {
                    FileUtils.copyFile(artifactFile, pluginFile);
                    return;
                } catch (IOException e) {
                    artifactFile.delete();
                    throw ExceptionFactory.systemErrorException(e);
                }
            }
        }

        // Didn't find it in .m2, so try downloading it.
        Set<URL> repositories = getMavenRepositories();
        for (URL mavenRepoUrl : repositories) {
            if (downloadFromMavenRepo(pluginFile, coordinates, mavenRepoUrl)) {
                return;
            }
        }
    }

    /**
     * Tries to download the plugin from the given remote maven repository.
     * @param pluginFile
     * @param coordinates
     * @param mavenRepoUrl
     */
    protected boolean downloadFromMavenRepo(File pluginFile, PluginCoordinates coordinates, URL mavenRepoUrl) {
        String artifactSubPath = PluginUtils.getMavenPath(coordinates);
        InputStream istream = null;
        OutputStream ostream = null;
        try {
            URL artifactUrl = new URL(mavenRepoUrl, artifactSubPath);
            istream = artifactUrl.openStream();
            ostream = new FileOutputStream(pluginFile);
            IOUtils.copy(istream, ostream);
            ostream.flush();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            IOUtils.closeQuietly(istream);
            IOUtils.closeQuietly(ostream);
        }
    }

    /**
     * A valid set of remove maven repository URLs.
     */
    protected Set<URL> getMavenRepositories() {
        return PluginUtils.getDefaultMavenRepositories();
    }

    /**
     * @param pluginsDir the pluginsDir to set
     */
    public void setPluginsDir(File pluginsDir) {
        this.pluginsDir = pluginsDir;
        if (!this.pluginsDir.exists()) {
            this.pluginsDir.mkdirs();
        }
    }

}
