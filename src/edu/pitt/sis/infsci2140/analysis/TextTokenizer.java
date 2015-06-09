package edu.pitt.sis.infsci2140.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * TextTokenizer can split a sequence of text into individual word tokens.
 */
public class TextTokenizer {
	private List<String> words = new ArrayList<String>();
	private int pos = 0;// the number of words has been visited
	private int size = 0;
        
    // YOU MUST IMPLEMENT THIS METHOD
	public TextTokenizer( char[] texts ) {
		// this constructor will tokenize the input texts (usually it is a char array for a whole document)
		List<Character> a_word = new ArrayList<Character>();
		int ix = 0;
		int len = texts.length;
		for(; ix < len; ++ix)
		{   //check if the character is alphabet or number,if it is a symbol,clear it
			if(Character.isLetterOrDigit(texts[ix]))
			{
				a_word.add(texts[ix]);
				if(ix == len - 1)
				{
					this.add_word(a_word);
					a_word.clear();
					break;
					}
				}
			else
			{
				this.add_word(a_word);
				a_word.clear();
				}
			}
		this.size = this.words.size();
		}
        
        // YOU MUST IMPLEMENT THIS METHOD
	public char[] nextWord() {
		// read and return the next word of the document; or return null if it is the end of the document
		char ret_v[] = null;
		if(pos < size)
		{
			ret_v = words.get(pos).toCharArray();
			pos++;
			}// if it is not the end of the text, continuously getting the next word.
		return ret_v;
		}
	private void add_word(List<Character> a_word)
	{
		if(a_word.size() > 0)
		{
			String w = "";
			for(int j = 0; j < a_word.size(); ++j)
				w += a_word.get(j);
			this.words.add(w);
			}
		}
}

