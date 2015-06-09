package edu.pitt.sis.infsci2140.analysis;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class StopwordsRemover {
	//initialize the stopword list and the bufferreader
	private Set<String> stop_words = new HashSet<String>();
	private BufferedReader stopwd_br = null;
	
	// YOU MUST IMPLEMENT THIS METHOD
	public StopwordsRemover( FileInputStream instream ) {
		// load and store the stop words from the fileinputstream with appropriate data structure
		// that you believe is suitable for matching stop words.
		DataInputStream data = new DataInputStream(instream);
		this.stopwd_br = new BufferedReader(new InputStreamReader(data));
		//get the stop words set from stop words file
		try
		{
			String wd;
			while((wd = stopwd_br.readLine()) != null)
			{
				if(!wd.isEmpty())
				{
					stop_words.add(wd);
					}                                
				}
			stopwd_br.close();
			}
		//print out the place when there is an error
		catch(IOException e)
		{
			e.printStackTrace();
			}
		}
        
        // YOU MUST IMPLEMENT THIS METHOD
        public boolean isStopword( char[] word ) {
                // return true if the input word is a stopword, or false if not                
                return stop_words.contains(new String(word));
        }
}