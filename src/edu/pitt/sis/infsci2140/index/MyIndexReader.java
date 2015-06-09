package edu.pitt.sis.infsci2140.index;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.BufferedReader;

/**
 * A class for reading your index.
 */
public class MyIndexReader {
	//decide whether has a token or not, default false
	boolean flag = false;
	protected File dir;	
	public MyIndexReader( File dir ) throws IOException {
		this.dir = dir;		
	}	
	public MyIndexReader( String path_dir ) throws IOException {
		this( new File(path_dir) );			
	}
	
	/**
	 * Get the (non-negative) integer docid for the requested docno.
	 * If -1 returned, it indicates the requested docno does not exist in the index.
	 * 
	 * @param docno
	 * @return
	 */
	public int getDocid( String docno ) {
		// you should implement this method.
		//get the docid the token appears
		if(flag) 
		{
			try {
				BufferedReader br = new BufferedReader( new InputStreamReader( new FileInputStream(this.dir+"/docnomap.txt"), "UTF-8" ) );
				String line = br.readLine();
				//split id and no by "-->"
				while(line != null) {					
					String[] rv = line.split("-->");
					if(rv[0]==docno) {
						br.close();
						return Integer.parseInt(rv[1]);
					} 
					//else, continue to read
					else {
						line = br.readLine();
					}
				}			
				br.close();				
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			} 
			return -1;
		}
		return -1;
	}
	
	/**
	 * Retrive the docno for the integer docid.
	 * 
	 * @param docid
	 * @return
	 */
	public String getDocno( int docid ) {
		// you should implement this method.
		//get the docno the token appears
		if(flag) 
		{
			try {
				BufferedReader br = new BufferedReader( new InputStreamReader( new FileInputStream(this.dir+"/docidmap.txt"), "UTF-8" ) );
				String ln = br.readLine();
				while(ln != null) {
					//split id and no by "-->"
					String[] rv = ln.split("-->");
					if(Integer.parseInt(rv[0]) == docid) {
						br.close();
						return rv[1];
					}
					ln = br.readLine();	
				}
				br.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			return null;
		}
		return null;
	}
	
	/**
	 * Get the posting list for the requested token.
	 * 
	 * The posting list records the documents' docids the token appears and corresponding frequencies of the term, such as:
	 *  
	 *  [docid]		[freq]
	 *  1			3
	 *  5			7
	 *  9			1
	 *  13			9
	 * 
	 * ...
	 * 
	 * In the returned 2-dimension array, the first dimension is for each document, and the second dimension records the docid and frequency.
	 * 
	 * For example:
	 * array[0][0] records the docid of the first document the token appears.
	 * array[0][1] records the frequency of the token in the documents with docid = array[0][0]
	 * ...
	 * 
	 * NOTE that the returned posting list array should be ranked by docid from the smallest to the largest. 
	 * 
	 * @param token
	 * @return
	 */
	public int[][] getPostingList( String token ) throws IOException {
		// you should implement this method.
		if(flag) {
			BufferedReader dictionaryReader = new BufferedReader( new InputStreamReader( new FileInputStream(this.dir+"/dictionary.txt"), "UTF-8" ) );
			BufferedReader postingReader = null;
			String line = dictionaryReader.readLine();
			while(line != null) {
				//split posting and freq by "-->"
				String[] rv = line.split("-->");
				if(rv[0].equals(token)){
					int[][] postingList=new int[rv.length-2][2]; //docids are between the term and ctf, so is length-2
					int x = 0;
					for(int i=1;i<rv.length-1;i++) {
						int docid = Integer.parseInt(rv[i]);
						postingReader = new BufferedReader( new InputStreamReader( new FileInputStream(this.dir+"/"+docid+".txt"), "UTF-8" ) );
						String ln = postingReader.readLine();
						while(ln !=null) {
							String[] r = ln.split("-->");
							if(r[0].equals(token)){
								int freq = Integer.parseInt(r[1]);
								//array[x][0] records the docid of the first document the token appears.
								//array[x][1] records the frequency of the token in the documents with docid = array[x][0]
								postingList[x][0] = docid;
								postingList[x][1] = freq;
								x++;
							}
							ln = postingReader.readLine();							
						}
						postingReader.close();
					}
					dictionaryReader.close();
					return postingList;					
				}				
				line = dictionaryReader.readLine();				
			}
			dictionaryReader.close();	
			return null;			
		}
		return null;
	}
	
	/**
	 * Return the number of documents that contains the token.
	 * 
	 * @param token
	 * @return
	 */
	public int DocFreq( String token ) throws IOException {
		// you should implement this method.
		BufferedReader br = new BufferedReader( new InputStreamReader( new FileInputStream(this.dir+"/dictionary.txt"), "UTF-8" ) );
		int docFreq = 0;
		String ln = br.readLine();
		//get the doc freq
		while(ln != null) {
			//split doc and freq by "-->"
			String[] rv = ln.split("-->");
			if(rv[0].equals(token)) {
				docFreq = rv.length-2; 
				br.close();
				flag = true; 
				return docFreq;
			} 
			ln = br.readLine();
		}
		br.close();
		return 0;
	}
	
	/**
	 * Return the total number of times the token appears in the collection.
	 * 
	 * @param token
	 * @return
	 */
	public long CollectionFreq( String token ) throws IOException {
		// you should implement this method.
		//get the collection freq
		if(flag) 
		{
			BufferedReader br = new BufferedReader( new InputStreamReader( new FileInputStream(this.dir+"/dictionary.txt"), "UTF-8" ) );
			long ctf = 0;
			String line = br.readLine();
			while(line != null) {
				String[] rv = line.split("-->");
				if(rv[0].equals(token)) {
					ctf = Long.parseLong((rv[rv.length-1])); 
					br.close();
					return ctf;
				}
				line = br.readLine();
			}
			br.close();
			return 0;
		}
		return 0;
	}
	public void close() throws IOException {
		// you should implement this method when necessary
	}
}
