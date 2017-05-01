package net.jonbell.examples.bytecode.instrumenting;

import java.util.*;
import java.lang.instrument.Instrumentation;
import java.nio.file.Paths;

/* Here is the entry point for maven java agent */
public class PreMain {
	public static boolean IS_RUNTIME_INST = true;
        public static String M_PATH = "target/test-classes/edu/gmu/swe/test/muplay/";

	public static void premain(String args, Instrumentation inst) {
        //System.out.println("args:" + args);
        
        String[] mdiffArgs = new String[3];
        String[] classFiles = args.split(",");
        String oldClassFolder = classFiles[0];
        String newClassFolder = classFiles[1];
        mdiffArgs[0] = oldClassFolder;
        mdiffArgs[1] = newClassFolder;
        mdiffArgs[2] = "parse";

        //System.out.println("arg0:" + mdiffArgs[0]);
        //System.out.println("arg1:" + mdiffArgs[1]);
        //System.out.println("arg2:" + mdiffArgs[2]);

        Instrumenter.main(mdiffArgs);
		
	}
}
