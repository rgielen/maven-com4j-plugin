package com4j.tlbimp.driver;

import com4j.tlbimp.BindingException;
import com4j.tlbimp.ErrorListener;
import com4j.tlbimp.FileCodeWriter;
import com4j.tlbimp.def.IWTypeLib;
import com4j.tlbimp.driver.LibConfig;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.ArrayList;

/**
 * Maven2 mojo for running the com4j process to produce .java files for the
 * Java-COM bridge. No required parameters are mentioned below, but you must
 * specify EITHER "libId" or "file" as documented below.
 * 
 * Effectively, this is what runs to generate .java code for something like
 * iTunes:
 * 
 * <br/> <code>
 * java -jar tlbimp.jar -o generated -p com.mycompany.com4j.itunes &quot;C:\Program Files\iTunes\iTunes.exe&quot;
 * </code>
 * <br/>
 * 
 * But we're using it from a Maven2 pom.xml file instead! This allows us to
 * automate code generation without worrying about how com4j is setup.
 * 
 * @author Jason Thrasher
 * 
 * @goal gen
 * @phase generate-sources
 * @requiresProject
 * @requiresDependencyResolution
 */
public class Com4jgenMojo extends AbstractMojo implements ErrorListener {

	/**
	 * The Maven Project. We'll add a new source directory to this project if
	 * everthing is successful.
	 * 
	 * @parameter default-value="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

  /**
   * Directory in which to create the Java COM wrapper files. This directory
   * will be added to the Maven project's compile source directory list and
   * will therfore be auto-compiled when the Maven compile phase is run.
   * 
   * @parameter property="outputDirectory"
   *            default-value="${project.build.directory}/generated-sources/com4j/java"
   */
  public File outputDirectory;

  /**
   * List of COM libraries definitions
   * @parameter property="libraries"
   * */
  private ArrayList <LibConfig> libraries = new ArrayList <LibConfig> ();

	public void execute() throws MojoExecutionException {
    for (LibConfig l : libraries) 
      {
		    getLog().debug("Starting Com4jMojo for: " + l.file);
      }

		checkEnv();

		validate();

		// all is good, now proceed with launch
		Driver driver = new Driver();

    for (LibConfig l : libraries)
      {
        Lib lib = new Lib();
        // libId wins over the specified file
        if (l.libId != null) 
          {
            lib.setLibid(l.libId);
            if (l.libVer != null)
              lib.setLibver(l.libVer);
          } 
        else 
          {
            lib.setFile(l.file);
          }
        lib.setPackage(l._package);

        try 
          {
            lib.validate(); // could throw IAE
            getLog().info(
                "Generating COM for LIBID: " + lib.getLibid()
                    + " found here: " + lib.getFile());
            driver.addLib(lib);
          } 
        catch (Exception e) 
          {
            // com4j may throw warnings if it can't handle something (like MS
            // Excel), we should continue with the mojo though
            getLog ().warn (
              "Com4j had an error while adding library: \n" 
              + e.getMessage ()
              + "\n\tLIBID: " + l.libId
              + " at " + l.file);

            throw new MojoExecutionException(e.getMessage());
          }
      }

		try 
      {
        driver.run(new FileCodeWriter(outputDirectory), this);
      } 
    catch (NullPointerException npe) 
      {
        getLog()
            .warn(
                "Com4j had an NPE error while running."
                    + " This usually happens when it can't create an interface."
                    + " You many need to manually touch the files before trying to compile them.");
      } 
    catch (Exception e) 
      {
        // com4j may throw warnings if it can't handle something (like MS
        // Excel), we should continue with the mojo though
        getLog().warn(
            "Com4j had an error while running: \n" + e.getMessage());
        throw new MojoExecutionException(e.getMessage());
      }

		getLog().debug("adding generated files to Maven compile source");
		// add outputDirectory to compile path for the project
		project.addCompileSourceRoot(outputDirectory.getAbsolutePath());

    for (LibConfig l : libraries) 
      {
		    getLog().debug("Finished Com4jMojo for: " + l.file);
      }
	}

	/**
	 * Check the runtime environment.
	 * 
	 * @throws MojoExecutionException
	 */
	private void checkEnv() throws MojoExecutionException {
		// check OS
		String osName = System.getProperty("os.name");
		if (!osName.startsWith("Windows")) {
			getLog().warn("Wrong OS: " + osName);
			throw new MojoExecutionException(
					"Com4j can only be run on a Windows operating system, and you're running: "
							+ osName);
		}

		// check output dir exists
		if (!outputDirectory.exists() && !outputDirectory.mkdirs()) {
			getLog().warn("outputDirectory couldn't be created");
			throw new MojoExecutionException("The output directory "
					+ outputDirectory
					+ " doesn't exist and couldn't be created.");
		}
	}

	/**
	 * Check the configuration from the pom.xml
	 * 
	 * @throws MojoExecutionException
	 */
	private void validate() throws MojoExecutionException {

    for (LibConfig l : libraries)
      {
        StringBuilder sb = new StringBuilder ();
        sb.append ("You specified <file> and <libId>.  The <libId> always wins. ");
        sb.append ("\n\t<file>").append (l.file).append ("</file>");
        sb.append ("\n\t<libId>").append (l.libId).append ("</libId>");

        if ((l.file == null && l.libId == null) || (l.file != null && l.libId != null)) {
          getLog().warn(sb.toString ());
        }

        // check that COM target exists
        if (l.file != null && !l.file.exists()) {
          getLog().warn("Can't find file: " + l.file);
          throw new MojoExecutionException(
              "The native COM target file couldn't be found: " + l.file);
        }
      }
	}

	public void started(IWTypeLib lib) {
		getLog().info("Generating definitions from " + lib.getName());
	}

	public void error(BindingException e) {
		getLog().error(e.getMessage());
	}

	public void warning(String message) {
		getLog().warn(message);
	}

}
