package jcomponents;

import java.io.File;

public class FileTester {

	public static void main(String[] args) {
		File root = new File("C:\\");
		File subroot = new File("C:\\", root.list()[1]);
		
		System.out.println("Root: " + root + "\nSubroot: " + subroot);
		
		System.out.println("Parent of subroot: " + subroot.getParent());
		System.out.println("Parent of root: " + root.getParent());
		if (root.getParent() == null) System.out.println("God bless!");
	}

}
