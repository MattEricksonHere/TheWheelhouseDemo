# TheWheelhouseDemo

The Wheelhouse Demo is a Java application that allows users to play a virtual electric piano using their computer keyboard, and displaying a live analysis of the musical notes being played.

## Dependencies

There are two libraries packaged with this software.

### JVstHost

JVstHost is an open-source library used to load VSTs (virtual instruments) for use in Java applications. Find source code and more information here: https://github.com/mhroth/jvsthost

### mda ePiano

"mda ePiano" is an open-source, lightweight electric piano synthesizer. Find source code and more information here: http://mda.smartelectronix.com/synths.htm

## To Install

### Running WheelhosueDemo.exe

The easiest way to run this software is to download and run WheelhosueDemo.exe.

**A 32-bit version of Java must be installed in order for this executable to run.**

This executable file was created using launch4j, a tool used to wrap Java applications into an .exe file. Find information here: http://launch4j.sourceforge.net/

**Note that this executable will copy "jvsthost2.dll" and "mda ePiano.dll" into the working directory at runtime.**

### Running WheelhouseDemo.jar

This software can also be run by downloading and running the executable JAR file.

Because of the restrictions of the libraries it uses, **this JAR file must be run using a 32-bit version of Java**. If your machine's default Java path points to a 64-bit version of Java, you will not be able to run this JAR file by double clicking it. If you have a 32-bit version of Java installed, you can run this JAR from the command prompt by directly executing the installed 32-bit Java executable:
```
"C:\Program Files (x86)\Java\jre[JRE_VERSION_HERE]\bin\java.exe" -jar WheelhouseDemo.jar
```

**Note that this executable will temporarily copy "jvsthost2.dll" and "mda ePiano.dll" into the working directory at runtime.**

### Running Cloned Source Code

The software can be run by cloning the source code and running the main function in src\DemoMasterFrame.java.

This has been tested using the Eclipse IDE.