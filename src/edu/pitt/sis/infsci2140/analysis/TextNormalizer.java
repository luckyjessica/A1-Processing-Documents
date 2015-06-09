package edu.pitt.sis.infsci2140.analysis;

public class TextNormalizer {
	
	// YOU MUST IMPLEMENT THIS METHOD
	public static char[] normalize( char[] chars ) {
		// return the normalized version of the word characters (replacing all uppercase characters into the corresponding lowercase characters)
        for(int i = 0; i < chars.length; i++)
        {
                {
                        chars[i] = (char)(chars[i] + 32);
                }
        }//if the char is uppercase, change it to lowercase
        return chars;
	}
}
