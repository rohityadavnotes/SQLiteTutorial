package com.sqlite.tutorial.utilities.file;

import com.sqlite.tutorial.utilities.string.StringUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileUtils {

    public static final String TAG = FileUtils.class.getSimpleName();

    private FileUtils() {
        throw new UnsupportedOperationException("You can't create instance of Util class. Please use as static..");
    }

    /**
     * create file object using String file path
     *
     * @param filePath file path as string
     * @return file object
     */
    public static File getFileByPath(String filePath) {
        return StringUtils.isSpace(filePath) ? null : new File(filePath);
    }

    /**
     * Determine whether the file exists
     *
     * @param filePath file path
     * @return {@code true}: exists<br>{@code false}: does not exist
     */
    public static boolean isFileExists(String filePath) {
        return isFileExists(getFileByPath(filePath));
    }

    /**
     * Determine whether the file exists
     *
     * @param file file
     * @return {@code true}: exists<br>{@code false}: does not exist
     */
    public static boolean isFileExists(File file) {
        return file != null && file.exists();
    }

    /**
     * Determine whether it is a directory
     *
     * @param dirPath Directory path
     * @return {@code true}: Yes<br>{@code false}: No
     */
    public static boolean isDirectory(String dirPath) {
        return isDirectory(getFileByPath(dirPath));
    }

    /**
     * Determine whether it is a directory
     *
     * @param file file
     * @return {@code true}: Yes<br>{@code false}: No
     */
    public static boolean isDirectory(File file) {
        return isFileExists(file) && file.isDirectory();
    }

    /**
     * Determine whether it is a file
     *
     * @param filePath file path
     * @return {@code true}: Yes<br>{@code false}: No
     */
    public static boolean isFile(String filePath) {
        return isFile(getFileByPath(filePath));
    }

    /**
     * Determine whether it is a file
     *
     * @param file file
     * @return {@code true}: Yes<br>{@code false}: No
     */
    public static boolean isFile(File file) {
        return isFileExists(file) && file.isFile();
    }

    /**
     * Judge whether the directory exists, if it does not exist, judge whether the creation is successful
     *
     * @param dirPath file path
     * @return {@code true}: Exist or create successfully<br>{@code false}: Does not exist or create failed
     */
    public static boolean createOrExistsDir(String dirPath) {
        return createOrExistsDir(getFileByPath(dirPath));
    }

    /**
     * Judge whether the directory exists, if it does not exist, judge whether the creation is successful
     *
     * @param file file
     * @return {@code true}: Exist or create successfully<br>{@code false}: Does not exist or create failed
     */
    public static boolean createOrExistsDir(File file) {
        /* If it exists, it returns true if it is a directory, it returns false if it is a file, or if it does not exist, it returns whether the creation is successful */
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    /**
     * Judge whether the file exists, if it does not exist, judge whether the creation is successful
     *
     * @param filePath file path
     * @return {@code true}: Exist or create successfully<br>{@code false}: Does not exist or create failed
     */
    public static boolean createOrExistsFile(String filePath) {
        return createOrExistsFile(getFileByPath(filePath));
    }

    /**
     * Judge whether the file exists, if it does not exist, judge whether the creation is successful
     *
     * @param file file
     * @return {@code true}: Exist or create successfully<br>{@code false}: Does not exist or create failed
     */
    public static boolean createOrExistsFile(File file) {
        if (file == null) return false;
        /* If it exists, it returns true if it is a file, or false if it is a directory */
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Determine whether the file exists, and delete it before creating
     *
     * @param filePath file path
     * @return {@code true}: Created successfully<br>{@code false}: Created failed
     */
    public static boolean createFileByDeleteOldFile(String filePath) {
        return createFileByDeleteOldFile(getFileByPath(filePath));
    }

    /**
     * Determine whether the file exists, and delete it before creating
     *
     * @param file file
     * @return {@code true}: Created successfully<br>{@code false}: Created failed
     */
    public static boolean createFileByDeleteOldFile(File file) {
        if (file == null) return false;
        /* The file exists and the deletion fails to return false */
        if (file.exists() && file.isFile() && !file.delete()) return false;
        /* Return false if directory creation fails */
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Rename file
     *
     * @param filePath file path
     * @param newName new name
     * @return {@code true}: Rename succeeded<br>{@code false}: Rename failed
     */
    public static boolean rename(String filePath, String newName) {
        return rename(getFileByPath(filePath), newName);
    }

    /**
     * Rename file
     *
     * @param file file
     * @param newName new name
     * @return {@code true}: Rename succeeded<br>{@code false}: Rename failed
     */
    public static boolean rename(File file, String newName) {
        /* If the file is empty, return false */
        if (file == null) return false;
        /* If the file does not exist, return false */
        if (!file.exists()) return false;
        /* The new file name is empty and returns false */
        if (StringUtils.isSpace(newName)) return false;
        /* If the file name has not changed, return true */
        if (newName.equals(file.getName())) return true;
        File newFile = new File(file.getParent() + File.separator + newName);
        /* If the renamed file already exists, return false */
        return !newFile.exists() && file.renameTo(newFile);
    }

    /**
     * Copy or move directory
     *
     * @param srcDirPath source directory path
     * @param destDirPath target directory path
     * @param isMove whether to move
     * @return {@code true}: Copy or move successfully<br>{@code false}: Copy or move failed
     */
    private static boolean copyOrMoveDir(String srcDirPath, String destDirPath, boolean isMove) {
        return copyOrMoveDir(getFileByPath(srcDirPath), getFileByPath(destDirPath), isMove);
    }

    /**
     * Copy or move directory
     *
     * @param srcDir source directory
     * @param destDir target directory
     * @param isMove whether to move
     * @return {@code true}: Copy or move successfully<br>{@code false}: Copy or move failed
     */
    private static boolean copyOrMoveDir(File srcDir, File destDir, boolean isMove) {
        if (srcDir == null || destDir == null) return false;
        /**
         * If the target directory is in the source directory, return false, if you don’t understand, think about how to end the recursion
         *
         * srcPath: F:\\MyGithub\\AndroidUtilCode\\utilcode\\src\\test\\res
         * destPath: F:\\MyGithub\\AndroidUtilCode\\utilcode\\src\\test\\res1
         *
         * To prevent misjudgment in the above situation, a path separator must be added after each
         */
        String srcPath = srcDir.getPath() + File.separator;
        String destPath = destDir.getPath() + File.separator;
        if (destPath.contains(srcPath)) return false;
        /* return false if the source file does not exist or is not a directory */
        if (!srcDir.exists() || !srcDir.isDirectory()) return false;
        /* return false if the target directory does not exist */
        if (!createOrExistsDir(destDir)) return false;
        File[] files = srcDir.listFiles();
        for (File file : files) {
            File oneDestFile = new File(destPath + file.getName());
            if (file.isFile()) {
                /* If the operation fails, return false */
                if (!copyOrMoveFile(file, oneDestFile, isMove)) return false;
            } else if (file.isDirectory()) {
                /* If the operation fails, return false */
                if (!copyOrMoveDir(file, oneDestFile, isMove)) return false;
            }
        }
        return !isMove || deleteDir(srcDir);
    }

    /**
     * Copy or move files
     *
     * @param srcFilePath source file path
     * @param destFilePath target file path
     * @param isMove whether to move
     * @return {@code true}: Copy or move successfully<br>{@code false}: Copy or move failed
     */
    private static boolean copyOrMoveFile(String srcFilePath, String destFilePath, boolean isMove) {
        return copyOrMoveFile(getFileByPath(srcFilePath), getFileByPath(destFilePath), isMove);
    }

    /**
     * Copy or move files
     *
     * @param srcFile source file
     * @param destFile target file
     * @param isMove whether to move
     * @return {@code true}: Copy or move successfully<br>{@code false}: Copy or move failed
     */
    private static boolean copyOrMoveFile(File srcFile, File destFile, boolean isMove) {
        if (srcFile == null || destFile == null) return false;
        /* return false if the source file does not exist or is not a file */
        if (!srcFile.exists() || !srcFile.isFile()) return false;
        /* If the target file exists and is a file, it returns false */
        if (destFile.exists() && destFile.isFile()) return false;
        /* return false if the target directory does not exist */
        if (!createOrExistsDir(destFile.getParentFile())) return false;
        try {
            return writeFileFromIS(destFile, new FileInputStream(srcFile), false)
                    && !(isMove && !deleteFile(srcFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Copy directory
     *
     * @param srcDirPath source directory path
     * @param destDirPath target directory path
     * @return {@code true}: Copy successfully<br>{@code false}: Copy failed
     */
    public static boolean copyDir(String srcDirPath, String destDirPath) {
        return copyDir(getFileByPath(srcDirPath), getFileByPath(destDirPath));
    }

    /**
     * Copy directory
     *
     * @param srcDir source directory
     * @param destDir target directory
     * @return {@code true}: Copy successfully<br>{@code false}: Copy failed
     */
    public static boolean copyDir(File srcDir, File destDir) {
        return copyOrMoveDir(srcDir, destDir, false);
    }

    /**
     * Copy files
     *
     * @param srcFilePath source file path
     * @param destFilePath target file path
     * @return {@code true}: Copy successfully<br>{@code false}: Copy failed
     */
    public static boolean copyFile(String srcFilePath, String destFilePath) {
        return copyFile(getFileByPath(srcFilePath), getFileByPath(destFilePath));
    }

    /**
     * Copy files
     *
     * @param srcFile source file
     * @param destFile target file
     * @return {@code true}: Copy successfully<br>{@code false}: Copy failed
     */
    public static boolean copyFile(File srcFile, File destFile) {
        return copyOrMoveFile(srcFile, destFile, false);
    }

    /**
     * Move directory
     *
     * @param srcDirPath source directory path
     * @param destDirPath target directory path
     * @return {@code true}: Move successfully<br>{@code false}: Move failed
     */
    public static boolean moveDir(String srcDirPath, String destDirPath) {
        return moveDir(getFileByPath(srcDirPath), getFileByPath(destDirPath));
    }

    /**
     * Move directory
     *
     * @param srcDir source directory
     * @param destDir target directory
     * @return {@code true}: Move successfully<br>{@code false}: Move failed
     */
    public static boolean moveDir(File srcDir, File destDir) {
        return copyOrMoveDir(srcDir, destDir, true);
    }

    /**
     * Move files
     *
     * @param srcFilePath source file path
     * @param destFilePath target file path
     * @return {@code true}: Move successfully<br>{@code false}: Move failed
     */
    public static boolean moveFile(String srcFilePath, String destFilePath) {
        return moveFile(getFileByPath(srcFilePath), getFileByPath(destFilePath));
    }

    /**
     * Move files
     *
     * @param srcFile source file
     * @param destFile target file
     * @return {@code true}: Move successfully<br>{@code false}: Move failed
     */
    public static boolean moveFile(File srcFile, File destFile) {
        return copyOrMoveFile(srcFile, destFile, true);
    }

    /**
     * Delete directory
     *
     * @param dirPath directory path
     * @return {@code true}: Deleted successfully<br>{@code false}: Deleted failed
     */
    public static boolean deleteDir(String dirPath) {
        return deleteDir(getFileByPath(dirPath));
    }

    /**
     * Delete directory
     *
     * @param dir directory
     * @return {@code true}: Deleted successfully<br>{@code false}: Deleted failed
     */
    public static boolean deleteDir(File dir) {
        if (dir == null) return false;
        /* return true if the directory does not exist */
        if (!dir.exists()) return true;
        /* Not a directory returns false */
        if (!dir.isDirectory()) return false;
        /* Now the file exists and is a folder */
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!deleteFile(file)) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * Delete Files
     *
     * @param srcFilePath file path
     * @return {@code true}: Deleted successfully<br>{@code false}: Deleted failed
     */
    public static boolean deleteFile(String srcFilePath) {
        return deleteFile(getFileByPath(srcFilePath));
    }

    /**
     * Delete Files
     *
     * @param file file
     * @return {@code true}: Delete successfully<br> {@code false}: Delete failed
     */
    public static boolean deleteFile(File file) {
        return file != null && (!file.exists() || file.isFile() && file.delete());
    }

    /**
     * Delete all files in the directory
     *
     * @param dirPath directory path
     * @return {@code true}: Deleted successfully<br>{@code false}: Deleted failed
     */
    public static boolean deleteFilesInDir(String dirPath) {
        return deleteFilesInDir(getFileByPath(dirPath));
    }

    /**
     * Delete all files in the directory
     *
     * @param dir directory
     * @return {@code true}: Deleted successfully<br>{@code false}: Deleted failed
     */
    public static boolean deleteFilesInDir(File dir) {
        if (dir == null) return false;
        /* The directory does not exist, return true */
        if (!dir.exists()) return true;
        /* Not a directory return false */
        if (!dir.isDirectory()) return false;
        /* Now the file exists and is a folder */
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!deleteFile(file)) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false;
                }
            }
        }
        return true;
    }

    /**
     * Get all files in the directory
     *
     * @param dirPath directory path
     * @param isRecursive whether to recurse into subdirectories
     * @return file linked list
     */
    public static List<File> listFilesInDir(String dirPath, boolean isRecursive) {
        return listFilesInDir(getFileByPath(dirPath), isRecursive);
    }

    /**
     * Get all files in the directory
     *
     * @param dir directory
     * @param isRecursive whether to recurse into subdirectories
     * @return file linked list
     */
    public static List<File> listFilesInDir(File dir, boolean isRecursive) {
        if (isRecursive) return listFilesInDir(dir);
        if (dir == null || !isDirectory(dir)) return null;
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        Collections.addAll(list, files);
        return list;
    }

    /**
     * Get all files in the directory including subdirectories
     *
     * @param dirPath directory path
     * @return file linked list
     */
    public static List<File> listFilesInDir(String dirPath) {
        return listFilesInDir(getFileByPath(dirPath));
    }

    /**
     * Get all files in the directory including subdirectories
     *
     * @param dir directory
     * @return file linked list
     */
    public static List<File> listFilesInDir(File dir) {
        if (dir == null || !isDirectory(dir)) return null;
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                list.add(file);
                if (file.isDirectory()) {
                    list.addAll(listFilesInDir(file));
                }
            }
        }
        return list;
    }

    /**
     * Get all files with the suffix name in the directory
     * <p>Ignore case</p>
     *
     * @param dirPath directory path
     * @param suffix suffix
     * @param isRecursive whether to recurse into subdirectories
     * @return file linked list
     */
    public static List<File> listFilesInDirWithFilter(String dirPath, String suffix, boolean isRecursive) {
        return listFilesInDirWithFilter(getFileByPath(dirPath), suffix, isRecursive);
    }

    /**
     * Get all files with the suffix name in the directory
     * <p>Ignore case</p>
     *
     * @param dir directory
     * @param suffix suffix
     * @param isRecursive whether to recurse into subdirectories
     * @return file linked list
     */
    public static List<File> listFilesInDirWithFilter(File dir, String suffix, boolean isRecursive) {
        if (isRecursive) return listFilesInDirWithFilter(dir, suffix);
        if (dir == null || !isDirectory(dir)) return null;
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.getName().toUpperCase().endsWith(suffix.toUpperCase())) {
                    list.add(file);
                }
            }
        }
        return list;
    }

    /**
     * Get all files with the suffix name in the directory including subdirectories
     * <p>Ignore case</p>
     *
     * @param dirPath directory path
     * @param suffix suffix
     * @return file linked list
     */
    public static List<File> listFilesInDirWithFilter(String dirPath, String suffix) {
        return listFilesInDirWithFilter(getFileByPath(dirPath), suffix);
    }

    /**
     * Get all files with the suffix name in the directory including subdirectories
     * <p>Ignore case</p>
     *
     * @param dir directory
     * @param suffix suffix
     * @return file linked list
     */
    public static List<File> listFilesInDirWithFilter(File dir, String suffix) {
        if (dir == null || !isDirectory(dir)) return null;
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.getName().toUpperCase().endsWith(suffix.toUpperCase())) {
                    list.add(file);
                }
                if (file.isDirectory()) {
                    list.addAll(listFilesInDirWithFilter(file, suffix));
                }
            }
        }
        return list;
    }

    /**
     * Get all the files in the directory that meet the filter
     *
     * @param dirPath directory path
     * @param filter filter
     * @param isRecursive whether to recurse into subdirectories
     * @return file linked list
     */
    public static List<File> listFilesInDirWithFilter(String dirPath, FilenameFilter filter, boolean isRecursive) {
        return listFilesInDirWithFilter(getFileByPath(dirPath), filter, isRecursive);
    }

    /**
     * Get all the files in the directory that meet the filter
     *
     * @param dir directory
     * @param filter filter
     * @param isRecursive whether to recurse into subdirectories
     * @return file linked list
     */
    public static List<File> listFilesInDirWithFilter(File dir, FilenameFilter filter, boolean isRecursive) {
        if (isRecursive) return listFilesInDirWithFilter(dir, filter);
        if (dir == null || !isDirectory(dir)) return null;
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (filter.accept(file.getParentFile(), file.getName())) {
                    list.add(file);
                }
            }
        }
        return list;
    }

    /**
     * Get all filter-compliant files in the directory including subdirectories
     *
     * @param dirPath directory path
     * @param filter filter
     * @return file linked list
     */
    public static List<File> listFilesInDirWithFilter(String dirPath, FilenameFilter filter) {
        return listFilesInDirWithFilter(getFileByPath(dirPath), filter);
    }

    /**
     * Get all filter-compliant files in the directory including subdirectories
     *
     * @param dir directory
     * @param filter filter
     * @return file linked list
     */
    public static List<File> listFilesInDirWithFilter(File dir, FilenameFilter filter) {
        if (dir == null || !isDirectory(dir)) return null;
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (filter.accept(file.getParentFile(), file.getName())) {
                    list.add(file);
                }
                if (file.isDirectory()) {
                    list.addAll(listFilesInDirWithFilter(file, filter));
                }
            }
        }
        return list;
    }

    /**
     * Get the file with the specified file name in the directory including subdirectories
     * <p>Ignore case</p>
     *
     * @param dirPath directory path
     * @param fileName file name
     * @return file linked list
     */
    public static List<File> searchFileInDir(String dirPath, String fileName) {
        return searchFileInDir(getFileByPath(dirPath), fileName);
    }

    /**
     * Get the file with the specified file name in the directory including subdirectories
     * <p>Ignore case</p>
     *
     * @param dir directory
     * @param fileName file name
     * @return file linked list
     */
    public static List<File> searchFileInDir(File dir, String fileName) {
        if (dir == null || !isDirectory(dir)) return null;
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.getName().toUpperCase().equals(fileName.toUpperCase())) {
                    list.add(file);
                }
                if (file.isDirectory()) {
                    list.addAll(searchFileInDir(file, fileName));
                }
            }
        }
        return list;
    }

    /**
     * Write input stream to file
     *
     * @param filePath path
     * @param is input stream
     * @param append whether to append at the end of the file
     * @return {@code true}: successful writing<br>{@code false}: writing failed
     */
    public static boolean writeFileFromIS(String filePath, InputStream is, boolean append) {
        return writeFileFromIS(getFileByPath(filePath), is, append);
    }

    /**
     * Write input stream to file
     *
     * @param file file
     * @param is input stream
     * @param append whether to append at the end of the file
     * @return {@code true}: successful writing<br>{@code false}: writing failed
     */
    public static boolean writeFileFromIS(File file, InputStream is, boolean append) {
        if (file == null || is == null) return false;
        if (!createOrExistsFile(file)) return false;
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file, append));
            byte data[] = new byte[MemoryUtils.KB];
            int len;
            while ((len = is.read(data)) != -1) {
                os.write(data, 0, len);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeIO(is, os);
        }
    }

    /**
     * Write string to file
     *
     * @param filePath file path
     * @param content write content
     * @param append whether to append at the end of the file
     * @return {@code true}: successful writing<br>{@code false}: writing failed
     */
    public static boolean writeFileFromString(String filePath, String content, boolean append) {
        return writeFileFromString(getFileByPath(filePath), content, append);
    }

    /**
     * Write string to file
     *
     * @param file file
     * @param content write content
     * @param append whether to append at the end of the file
     * @return {@code true}: successful writing<br>{@code false}: writing failed
     */
    public static boolean writeFileFromString(File file, String content, boolean append) {
        if (file == null || content == null) return false;
        if (!createOrExistsFile(file)) return false;
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file, append);
            fileWriter.write(content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeIO(fileWriter);
        }
    }

    /**
     * Specify code to read file to List by line
     *
     * @param filePath file path
     * @param charsetName encoding format
     * @return file line linked list
     */
    public static List<String> readFile2List(String filePath, String charsetName) {
        return readFile2List(getFileByPath(filePath), charsetName);
    }

    /**
     * Specify code to read file to List by line
     *
     * @param file file
     * @param charsetName encoding format
     * @return file line linked list
     */
    public static List<String> readFile2List(File file, String charsetName) {
        return readFile2List(file, 0, 0x7FFFFFFF, charsetName);
    }

    /**
     * Specify code to read file to List by line
     *
     * @param filePath file path
     * @param st The starting line number to be read
     * @param end The number of ending lines to be read
     * @param charsetName encoding format
     * @return contains a list of specified lines
     */
    public static List<String> readFile2List(String filePath, int st, int end, String
            charsetName) {
        return readFile2List(getFileByPath(filePath), st, end, charsetName);
    }

    /**
     * Specify code to read file to List by line
     *
     * @param file file
     * @param st The starting line number to be read
     * @param end The number of ending lines to be read
     * @param charsetName encoding format
     * @return contains the list from the start line to the end line
     */
    public static List<String> readFile2List(File file, int st, int end, String charsetName) {
        if (file == null) return null;
        if (st > end) return null;
        BufferedReader reader = null;
        try {
            String line;
            int curLine = 1;
            List<String> list = new ArrayList<>();
            if (StringUtils.isSpace(charsetName)) {
                reader = new BufferedReader(new FileReader(file));
            } else {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));
            }
            while ((line = reader.readLine()) != null) {
                if (curLine > end) break;
                if (st <= curLine && curLine <= end) list.add(line);
                ++curLine;
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            closeIO(reader);
        }
    }

    /**
     * Specify the code to read the file line by line into the string
     *
     * @param filePath file path
     * @param charsetName encoding format
     * @return string
     */
    public static String readFile2String(String filePath, String charsetName) {
        return readFile2String(getFileByPath(filePath), charsetName);
    }

    /**
     * Specify the code to read the file line by line into the string
     *
     * @param file file
     * @param charsetName encoding format
     * @return string
     */

    /**
     * Specify the code to read the file line by line into the string
     *
     * @param file file
     * @param charsetName encoding format
     * @return string
     */
    public static String readFile2String(File file, String charsetName) {
        if (file == null) return null;
        BufferedReader reader = null;
        try {
            StringBuilder sb = new StringBuilder();
            if (StringUtils.isSpace(charsetName)) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            } else {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));
            }
            String line;
            while ((line = reader.readLine()) != null) {
                /* Change behavior in windows system\r\n, Linux is\n   */
                sb.append(line).append("\r\n");
            }
            /* To remove the final newline */
            return sb.delete(sb.length() - 2, sb.length()).toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            closeIO(reader);
        }
    }


    /**
     * Read the file into the character array
     *
     * @param filePath file path
     * @return character array
     */
    public static byte[] readFile2Bytes(String filePath) {
        return readFile2Bytes(getFileByPath(filePath));
    }

    /**
     * Read the file into the character array
     *
     * @param file file
     * @return character array
     */
    public static byte[] readFile2Bytes(File file) {
        if (file == null) return null;
        try {
            return ConvertUtils.inputStream2Bytes(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Simple access to file encoding format
     *
     * @param filePath file path
     * @return file encoding
     */
    public static String getFileCharsetSimple(String filePath) {
        return getFileCharsetSimple(getFileByPath(filePath));
    }

    /**
     * Simple access to file encoding format
     *
     * @param file file
     * @return file encoding
     */
    public static String getFileCharsetSimple(File file) {
        int p = 0;
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            p = (is.read() << 8) + is.read();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
           closeIO(is);
        }
        switch (p) {
            case 0xefbb:
                return "UTF-8";
            case 0xfffe:
                return "Unicode";
            case 0xfeff:
                return "UTF-16BE";
            default:
                return "GBK";
        }
    }

    /**
     * Get the number of file lines
     *
     * @param filePath file path
     * @return file line number
     */
    public static int getFileLines(String filePath) {
        return getFileLines(getFileByPath(filePath));
    }

    /**
     * Get the number of file lines
     *
     * @param file file
     * @return file line number
     */
    public static int getFileLines(File file) {
        int count = 1;
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[1024];
            int readChars;
            while ((readChars = is.read(buffer, 0, 1024)) != -1) {
                for (int i = 0; i < readChars; ++i) {
                    if (buffer[i] == '\n') ++count;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(is);
        }
        return count;
    }

    /**
     * Get file size
     * <p>For example: getFileSize(filePath, ConstUtils.MB); The returned file size unit is MB</p>
     *
     * @param filePath file path
     * @param unit <ul>
     * <li>{@link MemoryUtils.MemoryUnit#BYTE}: bytes</li>
     * <li>{@link MemoryUtils.MemoryUnit#KB}: kilobytes</li>
     * <li>{@link MemoryUtils.MemoryUnit#MB}: Mega </li>
     * <li>{@link MemoryUtils.MemoryUnit#GB}: GB</li>
     * </ul>
     * @return file size is in unit
     */
    public static double getFileSize(String filePath,  MemoryUtils.MemoryUnit unit) {
        return getFileSize(getFileByPath(filePath), unit);
    }

    /**
     * Get file size
     * <p>For example: getFileSize(file, ConstUtils.MB); The returned file size unit is MB</p>
     *
     * @param file file
     * @param unit <ul>
     * <li>{@link MemoryUtils.MemoryUnit#BYTE}: bytes</li>
     * <li>{@link MemoryUtils.MemoryUnit#KB}: kilobytes</li>
     * <li>{@link MemoryUtils.MemoryUnit#MB}: Mega </li>
     * <li>{@link MemoryUtils.MemoryUnit#GB}: GB</li>
     * </ul>
     * @return file size is in unit
     */
    public static double getFileSize(File file,  MemoryUtils.MemoryUnit unit) {
        if (!isFileExists(file)) return -1;
        return ConvertUtils.byte2Size(file.length(), unit);
    }

    /**
     * Get the MD5 check code of the file
     *
     * @param filePath file path
     * @return MD5 checksum of the file
     */
    public static String getFileMD5ToString(String filePath) {
        File file = StringUtils.isSpace(filePath) ? null : new File(filePath);
        return getFileMD5ToString(file);
    }

    /**
     * Get the MD5 check code of the file
     *
     * @param filePath file path
     * @return MD5 checksum of the file
     */
    public static byte[] getFileMD5(String filePath) {
        File file = StringUtils.isSpace(filePath) ? null : new File(filePath);
        return getFileMD5(file);
    }

    /**
     * Get the MD5 check code of the file
     *
     * @param file file
     * @return MD5 checksum of the file
     */
    public static String getFileMD5ToString(File file) {
        return ConvertUtils.bytes2HexString(getFileMD5(file));
    }

    /**
     * Get the MD5 check code of the file
     *
     * @param file file
     * @return MD5 checksum of the file
     */
    public static byte[] getFileMD5(File file) {
        if (file == null) return null;
        DigestInputStream dis = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            dis = new DigestInputStream(fis, md);
            byte[] buffer = new byte[1024 * 256];
            while (dis.read(buffer) > 0) ;
            md = dis.getMessageDigest();
            return md.digest();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(dis);
        }
        return null;
    }

    /**
     * Get the longest directory in the full path
     *
     * @param file file
     * @return filePath longest directory
     */
    public static String getDirName(File file) {
        if (file == null) return null;
        return getDirName(file.getPath());
    }

    /**
     * Get the longest directory in the full path
     *
     * @param filePath file path
     * @return filePath longest directory
     */
    public static String getDirName(String filePath) {
        if (StringUtils.isSpace(filePath)) return filePath;
        int lastSep = filePath.lastIndexOf(File.separator);
        return lastSep == -1 ? "" : filePath.substring(0, lastSep + 1);
    }

    /**
     * Get the file name in the full path
     *
     * @param file file
     * @return file name
     */
    public static String getFileName(File file) {
        if (file == null) return null;
        return getFileName(file.getPath());
    }

    /**
     * Get the file name in the full path
     *
     * @param filePath file path
     * @return file name
     */
    public static String getFileName(String filePath) {
        if (StringUtils.isSpace(filePath)) return filePath;
        int lastSep = filePath.lastIndexOf(File.separator);
        return lastSep == -1 ? filePath : filePath.substring(lastSep + 1);
    }

    /**
     * Get the file name without extension in the full path
     *
     * @param file file
     * @return File name without extension
     */
    public static String getFileNameNoExtension(File file) {
        if (file == null) return null;
        return getFileNameNoExtension(file.getPath());
    }

    /**
     * Get the file name without extension in the full path
     *
     * @param filePath file path
     * @return File name without extension
     */
    public static String getFileNameNoExtension(String filePath) {
        if (StringUtils.isSpace(filePath)) return filePath;
        int lastPoi = filePath.lastIndexOf('.');
        int lastSep = filePath.lastIndexOf(File.separator);
        if (lastSep == -1) {
            return (lastPoi == -1 ? filePath : filePath.substring(0, lastPoi));
        }
        if (lastPoi == -1 || lastSep > lastPoi) {
            return filePath.substring(lastSep + 1);
        }
        return filePath.substring(lastSep + 1, lastPoi);
    }

    /**
     * Get the file extension in the full path
     *
     * @param file file
     * @return file extension
     */
    public static String getFileExtension(File file) {
        if (file == null) return null;
        return getFileExtension(file.getPath());
    }

    /**
     * Get the file extension in the full path
     *
     * @param filePath file path
     * @return file extension
     */
    public static String getFileExtension(String filePath) {
        if (StringUtils.isSpace(filePath)) return filePath;
        int lastPoi = filePath.lastIndexOf('.');
        int lastSep = filePath.lastIndexOf(File.separator);
        if (lastPoi == -1 || lastSep >= lastPoi) return "";
        return filePath.substring(lastPoi + 1);
    }

    /**
     * Turn off IO
     *
     * @param closeables closeable
     */
    public static void closeIO(Closeable... closeables) {
        if (closeables == null) return;
        try {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    closeable.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
