package jcomponents;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

class FileSystemModel implements TreeModel {
	private TreeFile root;
	private static final String NAME_OF_SUPERDIRECTORY = "Drives";

	private List<TreeModelListener> listeners = new LinkedList<TreeModelListener>();

	public FileSystemModel() {
		//TreeFile tf = new TreeFile(null);
		this.root = new TreeFile();
	}

	public Object getRoot() {
		return root;
	}
	
	public Object getChild(Object parent, int index) {
		String[] children = ((TreeFile) parent).list();
		return new TreeFile(((TreeFile) parent).getFile(), children[index]);
	}
	
	public int getChildCount(Object parent) {
		if (((TreeFile) parent).isDirectory()) {
			String[] children = ((TreeFile) parent).list();
			if (children != null)
				return children.length;
		}
		return 0;
	}
	
	public boolean isLeaf(Object node) {
		return ((TreeFile) node).isFile();
	}
	// Here possible error in comparation
	public int getIndexOfChild(Object parent, Object child) {
		TreeFile tDirectory = (TreeFile) parent;
		TreeFile tFile = (TreeFile) child;
		String[] children = tDirectory.list();
		for (int i = 0; i < children.length; i++) {
			if (tFile.getName().equals(children[i])) {
				return i;
			}
		}
		return -1;

	}
	// TODO - later
	public void valueForPathChanged(TreePath path, Object value) {
		/*File oldFile = (File) path.getLastPathComponent();
		String fileParentPath = oldFile.getParent();
		String newFileName = (String) value;
		File targetFile = new File(fileParentPath, newFileName);
		oldFile.renameTo(targetFile);
		File parent = new File(fileParentPath);
		int[] changedChildrenIndices = { getIndexOfChild(parent, targetFile) };
		Object[] changedChildren = { targetFile };
		fireTreeNodesChanged(path.getParentPath(), changedChildrenIndices, changedChildren);
		*/
	}
	// - linked to previus, unrefactored
	@SuppressWarnings({ "unused", "rawtypes" })
	private void fireTreeNodesChanged(TreePath parentPath, int[] indices, Object[] children) {
		TreeModelEvent event = new TreeModelEvent(this, parentPath, indices, children);
		Iterator iterator = listeners.iterator();
		TreeModelListener listener = null;
		while (iterator.hasNext()) {
			listener = (TreeModelListener) iterator.next();
			listener.treeNodesChanged(event);
		}
	}
	
	public void addTreeModelListener(TreeModelListener listener) {
		//listeners.add(listener);
	}
	
	public void removeTreeModelListener(TreeModelListener listener) {
		//listeners.remove(listener);
	}
	
	class TreeFile{
		private File file;
		
		/**
		 * "Super" file system node
		 */
		TreeFile() {
			this.file = null;
		}
		
		/**
		 * "Super" file system node
		 */
		TreeFile(File file) {
			this.file = file;
		}
		
		/**
		 * @param parent
		 * @param child
		 */
		TreeFile(File parent, String child) {
			file = new File(parent, child);
		}

		/**
		 * @param parent
		 * @param child
		 */
		TreeFile(String parent, String child) {
			file = new File(parent, child);
		}

		/**
		 * @param pathname
		 */
		TreeFile(String pathname) {
			file = new File(pathname);
		}
		
		/**
		 * @return
		 * @see java.io.File#getName()
		 */
		String getName() {
			return (file == null)?NAME_OF_SUPERDIRECTORY:((file.getParent() == null)?file.toString():file.getName());
		}

		/**
		 * @return
		 * @see java.io.File#isFile()
		 */
		boolean isFile() {
			return (file == null)?false:file.isFile();
		}
		
		/**
		 * @return
		 * @see java.io.File#isDirectory()
		 */
		boolean isDirectory() {
			return (file == null)?true:file.isDirectory();
		}

		/**
		 * @return
		 * @see java.io.File#listFiles()
		 */
		File[] listFiles() {
			return (file == null)?File.listRoots():file.listFiles();
		}
		
		/**
		 * @return
		 * @see java.io.File#list()
		 */
		String[] list() {
			if (file == null) {
				File[] fileArray = File.listRoots();
				String[] tempArray = new String[fileArray.length];
				for (int i = 0; i<fileArray.length; i++) {
					tempArray[i] = fileArray[i].toString();
				}
				return tempArray;
			}
			return file.list();
		}

		/**
		 * @return the file
		 */
		File getFile() {
			return file;
		}

		public String toString() {
			return getName();
		}
	}
	
	
}