package covers1624.langsanity.launch;

import covers1624.langsanity.LangSanitizer;

import java.io.File;

/**
 * Created by covers1624 on 14/02/2017.
 */
public class Launch {

    public static void main(String[] args) {
        File langFolder = null;
        File outFolder = null;
        File remapFile = null;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--lang":
                    langFolder = new File(args[++i]);
                    break;
                case "--out":
                    outFolder = new File(args[++i]);
                    break;
                case "--remap":
                    remapFile = new File(args[++i]);
                    break;
            }
        }

        if (langFolder == null) {
            throw new RuntimeException("Lang folder not specified. use --lang");
        }
        if (!langFolder.exists()) {
            throw new RuntimeException("Unable to locate lang folder at " + langFolder.getAbsolutePath());
        }
        
        LangSanitizer.run(langFolder, outFolder, remapFile);
    }

}
