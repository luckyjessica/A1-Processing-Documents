package edu.pitt.sis.infsci2140.index;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.util.Hashtable;
import java.util.Map;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Set;
import java.util.TreeSet;
import java.io.IOException;

import edu.pitt.sis.infsci2140.analysis.StopwordsRemover;
import edu.pitt.sis.infsci2140.analysis.TextNormalizer;
import edu.pitt.sis.infsci2140.analysis.TextTokenizer;

public class MyIndexWriter {
	//transfer term to posting sequencely
	TreeMap<String,Hashtable<Integer,Integer>> dictionary = new TreeMap<String,Hashtable<Integer,Integer>>(); 
	//docidmap stores DOCID --> DOCNO, in DOCID order
	Map<Integer,String> docidmap = new TreeMap<Integer,String>();
	//docnomap stores DOCNO-->DOCID, in DOCNO order
	Map<String, Integer> docnomap = new TreeMap<String, Integer>();
	//create files named by docid, in which contain postinglist for each doc
	int docid = 0;
	//initialize bufferdwriter
	protected StopwordsRemover stoprmv = null;
	protected BufferedWriter didmapOutput = null;
	protected BufferedWriter dnomapOutput = null;
	protected BufferedWriter dicOutput = null;
	protected BufferedWriter postingOutput = null;
	protected File dir; 
	
	public MyIndexWriter( File dir ) throws IOException {
		this.dir = dir;
	}
	
	public MyIndexWriter( String path_dir ) throws IOException {
		this.dir = new File(path_dir);
		if( !this.dir.exists() ) {
			this.dir.mkdir();
		}
	}
	
	/**
	 * This method build index for each document.
	 * NOTE THAT: in your implementation of the index, you should transform your string docnos into non-negative integer docids !!!
	 * In MyIndexReader, you should be able to request the integer docid for docnos.
	 * 
	 * @param docno Docno
	 * @param tokenizer A tokenizer that iteratively gives out each token in the document.
	 * @throws IOException
	 */
	public void index( String docno, TextTokenizer tokenizer) throws IOException {
		// you should implement this method to build index for each document
		Hashtable<Integer,Integer> posting = null; 
		Map<String, Integer> postingList = new TreeMap<String,Integer>(); 
		//generate output
		didmapOutput = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(this.dir+"/docidmap.txt"), "UTF-8" ) );
		dnomapOutput = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(this.dir+"/docnomap.txt"), "UTF-8" ) );
		dicOutput = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(this.dir+"/dictionary.txt"), "UTF-8" ) );
		postingOutput = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(this.dir+"/"+docid+".txt"), "UTF-8" ) );
		FileInputStream instream_stopwords = null;
		instream_stopwords = new FileInputStream("stop_words.txt");
		stoprmv = new StopwordsRemover(instream_stopwords);
		docidmap.put(docid, docno);
		docnomap.put(docno, docid);
		
		char[] word = null;
		//load tokens from doc
		while( ( word=tokenizer.nextWord() ) != null ) {
			word = TextNormalizer.normalize(word); 
			if( !stoprmv.isStopword(word) ) {
				//get the word
				String wd = String.valueOf(word);
				//decide if the doc already has the word
				if(dictionary.containsKey(wd)) { 
					posting = dictionary.get(wd);
					//freq +1
					if(posting.containsKey(docid)) { 
						int freq = posting.get(docid);
						freq++;
						posting.put(docid, freq);
						postingList.put(wd, freq);
					} 
					else {						
						int freq = 1;
						posting.put(docid, freq);
						postingList.put(wd, freq);
					}
					dictionary.put(wd, posting);	
					} 
				//if it is a new word, create new posting
				else 
				{
					posting = new Hashtable<Integer,Integer>();
					posting.put(docid, 1);
					postingList.put(wd, 1);
					dictionary.put(wd, posting);
				}
			}
		}
		//run the postingoutput function
		outputPosting(postingOutput,postingList);
		docid++;
	}
	
	private void printDocnoMap(Map<String, Integer> docnoMap,BufferedWriter docnoMapOutput) throws IOException {
		//get the docno
		Set<String> set = docnoMap.keySet();
		Iterator<String> it = set.iterator();
		int id=0;
		String no = "";
		while(it.hasNext()) {
			no = it.next();
			id = docnoMap.get(no);
			docnoMapOutput.write(no.replaceAll(" ", "")+"-->"+id);
			docnoMapOutput.write("\n");
		}
		docnoMapOutput.close();
	}

	private void printDocidMap(Map<Integer, String> docidMap,BufferedWriter docidMapOutput) throws IOException {
		//get the docid
		Set<Integer> set = docidMap.keySet();
		Iterator<Integer> it = set.iterator();
		int id=0;
		String no = "";
		while(it.hasNext()) {
			id = it.next();
			no = docidMap.get(id).replaceAll(" ", "");
			docidMapOutput.write(id+"-->"+no);
			docidMapOutput.write("\n");
		}
	}

	static void outputDictionary(BufferedWriter bw,TreeMap<String, Hashtable<Integer, Integer>> dictionary) throws IOException {
		//output the dictinary
		Hashtable<Integer,Integer> table = null;
		TreeSet<Integer> docids = null;	
		Set<String> outer = dictionary.keySet();
		Iterator<String> outerIt = outer.iterator();	
		while(outerIt.hasNext()) {
			//get docid and freq for each doc
			docids = new TreeSet<Integer>();
			long ctf = 0;
			String term = outerIt.next();
			table = dictionary.get(term);
			Set<Integer> innerSet = table.keySet();
			Iterator<Integer> inner = innerSet.iterator();
			while(inner.hasNext()) {
				int docid = inner.next();
				//sum the freq
				ctf+= table.get(docid);
				docids.add(docid);
			}
			//output
			bw.write(term);
			String s = "";
			Iterator<Integer> it = docids.iterator();
			while(it.hasNext()) {
				s += "-->"+it.next();
			}
			bw.write(s+"-->"+ctf+"\n");
		}
		bw.close();
		
	}
	static void outputPosting(BufferedWriter bw, Map<String, Integer> tree) throws IOException {
		//generate posting list by docid
		Set<String> set = tree.keySet();
		Iterator<String> it = set.iterator();
		while(it.hasNext()) {
			String term = it.next();
			int freq = tree.get(term);
			bw.write(term+"-->"+freq+"\n");
		}
		bw.close();
	}
	/**
	 * Close the index writer, and you should output all the buffered content (if any).
	 * @throws IOException
	 */
	public void close() throws IOException {
		// you should implement this method if necessary
		printDocidMap(docidmap,didmapOutput);
		printDocnoMap(docnomap,dnomapOutput);
		outputDictionary(dicOutput,dictionary);
	}
}
