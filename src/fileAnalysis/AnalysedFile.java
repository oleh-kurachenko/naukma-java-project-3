/**
 * 
 */
package fileAnalysis;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Oleh Kurachenko
 *
 */
public class AnalysedFile {

	private File file;
	private boolean ready;
	private Map<String, Integer> library;

	/**
	 * @param file
	 */
	public AnalysedFile(File file) {
		super();
		this.file = file;
		ready = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return file + " " + ((ready) ? " - ready" : "- not ready");
	}

	public void prepare() throws IOException {
		library = new HashMap<String, Integer>();
		Integer tempInt = null;

		StreamTokenizer sTokenizer = null;

		sTokenizer = new StreamTokenizer(new FileReader(this.file));

		while (sTokenizer.nextToken() != StreamTokenizer.TT_EOF) {
			if (sTokenizer.ttype == StreamTokenizer.TT_WORD) {
				sTokenizer.sval = sTokenizer.sval.toLowerCase();
				tempInt = library.get(sTokenizer.sval);
				if (tempInt == null)
					library.put(sTokenizer.sval, 1);
				else {
					library.put(sTokenizer.sval, tempInt + 1);
				}
			}
		}

		ready = true;
	}

	/**
	 * @return the file
	 */
	private File getFile() {
		return file;
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public int get(String key) {
		Integer tempInt = library.get(key);
		if (tempInt == null)
			return 0;
		return tempInt.intValue();
	}

	/**
	 * 
	 * @return full name of file in {@link AnalysedFile}
	 */
	public String getFileName() {
		return file.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return file.equals(((AnalysedFile) obj).getFile());
	}
}
