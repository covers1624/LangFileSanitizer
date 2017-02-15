package covers1624.langsanity.util;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.filefilter.IOFileFilter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by covers1624 on 11/02/2017.
 */
public class ResourceWalker extends DirectoryWalker {

    private File folder;
    private List<File> files;

    public ResourceWalker(IOFileFilter fileFilter) {
        super(fileFilter, -1);
    }

    public void setFolder(File folder) {
        reset();
        this.folder = folder;
    }

    public void reset() {
        files = null;
        folder = null;
    }

    public List<File> startWalk() throws IOException {
        files = new LinkedList<>();

        walk(folder, new ArrayList());

        return files;
    }

    @Override
    protected void handleFile(File file, int depth, Collection results) throws IOException {
        files.add(file);
    }
}
