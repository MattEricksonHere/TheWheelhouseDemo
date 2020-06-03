# TheWheelhouseDemo

The Wheelhouse Demo is a Java application that allows users to play a virtual electric piano using their computer keyboard, and displaying a live analysis of the musical notes being played.

## Features

![Application Screenshot](/images/readme1.png)

### Tonnetz

A diagram that helps to visualize the harmonic relationships between notes and chords. Find more information here: https://en.wikipedia.org/wiki/Tonnetz

### Harmonics

A graph showing the frequencies of the overtones and undertones of each note being played. When these frequencies line up in consonance, a blue line appears. When these frequencies clash in dissonance, a red line appears between them.

### Keys

A graphical piano that allows the user to play notes using their computer keyboard. For example, the 'Z' key will play a C4 note, the 'S' key will play a C#4 note, and so on. A higher octave can be played beginning on the 'Q' key.

This feature will also analyze the notes being played and determine what chord is being played, displaying the chord's symbol on the screen.

## Dependencies

There are two libraries packaged with this software.

### JVstHost

JVstHost is an open-source library used to load VSTs (virtual instruments) for use in Java applications. Find source code and more information here: https://github.com/mhroth/jvsthost

### mda ePiano

"mda ePiano" is an open-source, lightweight electric piano synthesizer. Find source code and more information here: http://mda.smartelectronix.com/synths.htm

## Running Cloned Source Code

The software can be run by cloning the source code and running the main function in src\DemoMasterFrame.java. All dependencies are currently included in the repository.

This has been tested using the Eclipse IDE. If using another environment, make sure the included JVstHost.jar is included in the build path.

**Due to the requirements of the libraries used, the Java project must be run using a 32-bit JRE.**
