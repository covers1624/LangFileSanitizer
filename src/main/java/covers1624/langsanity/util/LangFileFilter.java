package covers1624.langsanity.util;

import org.apache.commons.io.filefilter.AbstractFileFilter;

import java.io.File;

/**
 * Created by covers1624 on 11/02/2017.
 */
public class LangFileFilter extends AbstractFileFilter {

    @Override
    public boolean accept(File file) {
        return file.isFile() && file.getAbsolutePath().endsWith(".lang");
    }
}
