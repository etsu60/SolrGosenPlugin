package SolrGosenPlugin;
import hudson.Launcher;
import hudson.Extension;
import hudson.util.FormValidation;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.AbstractProject;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import net.sf.json.JSONObject;

import org.apache.solr.handler.extraction.ExtractingRequestHandler;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;

import java.io.File;
import java.io.IOException;

/**
 * Sample {@link Builder}.
 *
 * <p>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked
 * and a new {@link SolarIndexBuilder} is created. The created
 * instance is persisted to the project configuration XML by using
 * XStream, so this allows you to use instance fields (like {@link #filePath})
 * to remember the configuration.
 *
 * <p>
 * When a build is performed, the {@link #perform(AbstractBuild, Launcher, BuildListener)}
 * method will be invoked.
 *
 * @author Kohsuke Kawaguchi
 */
public class SolarIndexBuilder extends Builder {

    private final String filePath;

    // Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
    @DataBoundConstructor
    public SolarIndexBuilder(String filePath) {
        this.filePath = filePath;
        System.out.println(filePath);
    }

    /**
     * We'll use this from the <tt>config.jelly</tt>.
     */
    public String getFilePath() {
        return filePath;
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) {

    	listener.getLogger().println("perform()");
    	listener.getLogger().println("filePath=" + filePath);


        ExtractingRequestHandler handler = new ExtractingRequestHandler();

        listener.getLogger().println(handler);


//        if (getDescriptor().isCorrectFilePath())
//            listener.getLogger().println("");
//        else
//            listener.getLogger().println("");
        return true;
    }

    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

    /**
     * Descriptor for {@link SolarIndexBuilder}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     *
     * <p>
     * See <tt>src/main/resources/hudson/plugins/hello_world/HelloWorldBuilder/*.jelly</tt>
     * for the actual HTML fragment for the configuration screen.
     */
    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

    	private String filePath;

        /**
         * Performs on-the-fly validation of the form field 'name'.
         *
         * @param value
         *      This parameter receives the value that the user has typed.
         * @return
         *      Indicates the outcome of the validation. This is sent to the browser.
         */
        public FormValidation doCheckInputValue(@QueryParameter String value)
                throws IOException, ServletException {

            System.out.println("doCheckInputValue()");
            System.out.println("value" + value);




        	if ( new File(value).exists() ) {
                return FormValidation.ok();
            } else {
                return FormValidation.error("Please set a correct file path.");
            }
        }

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types
            return true;
        }

        /**
         */
        public String getDisplayName() {
            return "Create Index of Solr";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {

            System.out.println("configure()");
            System.out.println("StaplerRequest=" + req);
            System.out.println("JSONObject:"+ formData);

        	filePath = formData.getString("filePath");
            save();
            return super.configure(req,formData);
        }

        /**
         */
        public String getCorrectFilePath() {
            return filePath;
        }
    }
}

