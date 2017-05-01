package net.jonbell.examples.bytecode.instrumenting;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import java.nio.file.Paths;

/* Here is the entry point for maven plugin */
@Mojo(name = "integration-test", defaultPhase = LifecyclePhase.PROCESS_CLASSES)
public class InstrumenterMojo extends AbstractMojo {

	@Component
	private MavenProject project;
	@Parameter(defaultValue = "false", readonly = true)
	private boolean instrumentTest;
	@Parameter(defaultValue = "")
	private String oldVersionPath;
	@Parameter(defaultValue = "")
	private String newVersionPath;
	@Parameter(defaultValue = "parse")
	private String tempPath;
	@Parameter(defaultValue = "")
	private String outputPath;
    
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
        String[] mdiffArgs = new String[4];

        //System.out.println("oldVersionPath:" + oldVersionPath);
        //System.out.println("newVersionPath:" + newVersionPath);
        //System.out.println("outputPath:" + outputPath);

        mdiffArgs[0] = oldVersionPath;
        mdiffArgs[1] = newVersionPath;
        mdiffArgs[2] = tempPath;
        mdiffArgs[3] = outputPath;
        
        //System.out.println("arg0:" + mdiffArgs[0]);
        //System.out.println("arg1:" + mdiffArgs[1]);
        //System.out.println("arg2:" + mdiffArgs[2]);
        //System.out.println("arg3:" + mdiffArgs[3]);
        //System.out.println("pwd:" + Paths.get(".").toAbsolutePath().normalize().toString());

        Instrumenter.main(mdiffArgs);
	}
}
