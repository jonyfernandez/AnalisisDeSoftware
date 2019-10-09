package soporteJavaParser;

import java.io.File;

public class DirExplorer {
    public interface FileHandler {
        void handle(int level, String path, File file);
    }
 
    public interface Filter {
        boolean interested(int level, String path, File file);
    }
 
    private FileHandler fileHandler;
    private Filter filter;
 
    /**
     * DirExplorer se configura con un filter y un fileHandler.
     * @param filter: define qué tipo de archivos son "interesantes".
     * @param fileHandler: define qué hacer frente a un archivo interesante.
     */
    public DirExplorer(Filter filter, FileHandler fileHandler) {
        this.filter = filter;
        this.fileHandler = fileHandler;
    }
 
    public void explore(File root) {
        explore(0, "", root);
    }
 
    /**
     * Explora recursivamente un directorio.
     * Por cada archivo, decide si es interesante, y si lo es lo deja en manos
     * del handler.
     * @param level: en qué nivel estamos
     * @param path: ruta (string)
     * @param file: archivo o directorio (File)
     */
    private void explore(int level, String path, File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                explore(level + 1, path + "/" + child.getName(), child);
            }
        } else {
            if (filter.interested(level, path, file)) {
                fileHandler.handle(level, path, file);
            }
        }
    }
 
}