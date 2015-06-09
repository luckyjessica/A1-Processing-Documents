package edu.pitt.sis.infsci2140.index;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
;

public class TrecwebCollection implements DocumentCollection {
	//initialize the bufferedreader
	// use a matecher f to define the start and the end of the doc
	BufferedReader br = null;
	Matcher f = null;
	ArrayDeque<Map> text = new ArrayDeque<Map>();
	// YOU SHOULD IMPLEMENT THIS METHOD
	public TrecwebCollection( FileInputStream instream ) throws IOException {
		// This constructor should take an inputstream of the collection file as the input parameter.
		this.br = new BufferedReader(new InputStreamReader(instream));
		String docno = "DOCNO";
		String content = "CONTENT";
		String regex = "<DOCNO>(.*)</DOCNO>";
		Pattern p = Pattern.compile(regex);
		Map<String, Object> document = new HashMap<String, Object>();
		String line = "";
		String doc = "";
		String LL = br.readLine();
		//read the file
		while(LL != null) {
			f = p.matcher(LL);
			if(f.matches()) {
				document.clear();
				doc = f.group(1);
				document.put(docno, doc);
				while(!LL.equals("</DOCHDR>")) {
					LL = br.readLine();
				}
				LL = br.readLine();
				line = "";
				while(!LL.equals("</DOC>")){
					line += LL;
					LL = br.readLine();
				}
				//convert the string to a new array
				char[] c = line.toCharArray();
				//Associate the value and the key
				document.put(content, c);
				this.text.addLast(new HashMap<String, Object>(document));
			} else {
				LL = br.readLine();
			}
		}
		br.close();
	}
	
	// YOU SHOULD IMPLEMENT THIS METHOD
	public Map<String, Object> nextDocument() throws IOException {
		// Read the definition of this method from edu.pitt.sis.infsci2140.index.DocumentCollection interface 
		// and follow the assignment instructions to implement this method.
		if(!text.isEmpty()) {
			return text.removeFirst();
		}
		return null;
	}
	
}
