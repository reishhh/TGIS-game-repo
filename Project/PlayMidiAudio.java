import java.io.BufferedInputStream;
import java.io.*;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.sound.midi.*;
import javax.sound.midi.Sequencer;
import java.lang.Exception.*;
import java.lang.Object.*;
	 
public class PlayMidiAudio {
	 
	 public static Sequencer sequencer = null;
	 public static InputStream is = null;
	 
	 public void playMidi( String filename, int loopCount )
	 {
	 	try {
		    sequencer = MidiSystem.getSequencer();
		    sequencer.open();
	 	} catch ( MidiUnavailableException e )
	 	{
	 		System.out.println("Sequencer Error!");
		}
       
       try {
		    is = new BufferedInputStream(new FileInputStream(new File(filename)));
	 	 } catch ( FileNotFoundException ev )
	 	 {
	 	 	System.out.println("MiDi File not found.");
	 	 }
	 	 
	 	 try {
	    	try {
	    		sequencer.setSequence(is);
	    	} catch ( InvalidMidiDataException evt )
	    	{
	    		System.out.println(" Invalid MiDi data found. ");
	    	}
	    } catch ( IOException evt )
	    {
	    	System.out.println( "MiDi file processing failed." );
	    }
	    
	 	 sequencer.setLoopCount(loopCount);
	 	 
	    sequencer.start();
	 }
	 
	 public void stopMiDiFile()
	 {
	 	sequencer.close();
	 }
	 
}

