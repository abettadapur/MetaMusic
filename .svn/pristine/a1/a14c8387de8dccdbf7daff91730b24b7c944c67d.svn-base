package org.thingswithworth.metamusic.fileio;

import android.os.Environment;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * A simple utility class to hold methods to scan directories for files according to a provided filter.
 *
 * @author Andrey Kurenkov
 * @version 1.0
 */
public class FileScanner {
    private static final File root = Environment.getExternalStorageDirectory();
    /**
     * @param filter  filter to accepts files by
     * @return all the files found
     */
    public static File[] scanFileSystem(FileFilter filter){

        return recursiveFileFind(root,filter,true);
    }
    /**
     * Utility method to scan a directory and all its subdirectories for files that match a filter. This method attempts to
     * avoid loops by checking for link/schortcut folder files, but this has not been thoroughly tested as of yet. TODO: just
     * do graph search instead of checking link?
     *
     * @param startFrom
     *            The directory to start in (will scan this and all subdirectories as BFS/DFS)
     * @param filter
     *            The filter to accept/deny files
     * @param DFS
     *            Whether to search with DFS or BFS. Mainly significant for memory.
     * @return Array of all files found that match the filter
     */
    public static File[] recursiveFileFind(File startFrom, FileFilter filter, boolean DFS) {
        if (startFrom == null || filter == null || !startFrom.isDirectory())
            return null;
        FileFilter directoryFilter = new DirectoryFilter();

        LinkedList<File> folders = new LinkedList<File>();
        ArrayList<File> files = new ArrayList<File>();
        folders.add(startFrom);
        while (!folders.isEmpty()) {
            File folder;
            if (DFS)
                folder = folders.removeLast();
            else
                folder = folders.removeFirst();

            for (File file : folder.listFiles(filter)) {
                files.add(file);
            }

            for (File file : folder.listFiles(directoryFilter)) {
                folders.add(file);
            }
        }
        return files.toArray(new File[files.size()]);
    }

    /**
     * Simple filter that accepts a Directory file that is not a shortcut/soft link.
     *
     * @author AK
     */
    public static class DirectoryFilter implements FileFilter {

        @Override
        public boolean accept(File file) {
            try {
                return file != null && file.isDirectory() && !file.isHidden() && !isSymlink(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    /** Simple filter that accepts a music file on Android.
     *
     * @author AK
    */
    public static class MusicFilter implements FileFilter {
        public final static String[] musicFileFormats={".3gp",".mp4",".m4a",".aac",".ts",".3gp",".flac",".mid",".mp3"};
        @Override
        public boolean accept(File file) {
            String fileName=file.getName();
            for(String format:musicFileFormats)
                if(fileName.endsWith(format))
                    return true;
            return false;
        }
    }

    /**
     * Method testing whether a file is a shortcut/soft link.
     *
     * @param file
     *            file to check
     * @return true if shortcut/symlink
     * @throws IOException
     */
    public static boolean isSymlink(File file) throws IOException {
        if (file == null)
            throw new NullPointerException("File must not be null");
        File canon;
        if (file.getParent() == null) {
            canon = file;
        } else {
            File canonDir = file.getParentFile().getCanonicalFile();
            canon = new File(canonDir, file.getName());
        }
        boolean testOne = !canon.getCanonicalFile().equals(canon.getAbsoluteFile());
        String path1 = file.getAbsoluteFile().getParentFile().getCanonicalPath() + File.separatorChar + file.getName();
        String path2 = file.getAbsoluteFile().getCanonicalPath();
        boolean testTwo = !(path1.equals(path2));
        return testOne && testTwo;
    }
}