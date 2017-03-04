package covers1624.langsanity;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import covers1624.langsanity.data.DataEntry;
import covers1624.langsanity.data.LangEntry;
import covers1624.langsanity.data.StringEntry;
import covers1624.langsanity.util.LangFileFilter;
import covers1624.langsanity.util.LogHelper;
import covers1624.langsanity.util.ResourceWalker;
import covers1624.langsanity.util.Utils;

import java.io.*;
import java.util.*;

/**
 * Created by covers1624 on 15/02/2017.
 */
public class LangSanitizer {

    public static Map<String, String> keyRemaps = new HashMap<>();

    public static Map<Integer, String> positionKeyMap = new LinkedHashMap<>();
    public static Map<Integer, DataEntry> usLangEntries = new LinkedHashMap<>();
    public static Map<String, Map<Integer, DataEntry>> localeLangEntries = new HashMap<>();
    public static Map<String, List<DataEntry>> invalidLocaleEntries = new HashMap<>();

    public static void run(File langFolder, File outputFolder, File remapFile) {
        if (remapFile != null) {
            if (!remapFile.exists()) {
                LogHelper.error("Remap file specified but does not exist..");
                new FileNotFoundException(remapFile.getAbsolutePath()).printStackTrace();
            } else {
                parseRemapFile(remapFile);
            }
        }
        File usLangFile = new File(langFolder, "en_US.lang");

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(usLangFile), Charsets.UTF_8));

            String line;
            int idx = 1;
            while((line = reader.readLine()) != null) {
                DataEntry entry;
                if (line.isEmpty()) {
                    entry = new StringEntry("");
                } else if (line.startsWith("#") || line.startsWith("//")) {
                    if (line.startsWith("//")) {
                        line = "#" + line.substring(2);
                    }
                    entry = new StringEntry(line.trim());
                } else {
                    if (!line.contains("=")) {
                        LogHelper.warn("Invalid line inside en_US lang file! Line#: %s, Line Data: %s", idx, line);
                        idx++;
                        continue;
                    }
                    try {
                        String[] split = line.split("=");
                        entry = new LangEntry(remapKey(split[0]), split[1]);
                        positionKeyMap.put(idx, split[0]);
                    } catch (Exception e) {
                        LogHelper.fatal("Error reading line %s!", idx);
                        e.printStackTrace();
                        idx++;
                        continue;
                    }
                }
                usLangEntries.put(idx++, entry);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read en_US language file!", e);
        }
        Utils.tryCreateDirectory(outputFolder);
        try {
            PrintWriter writer = new PrintWriter(Utils.tryCreateFile(new File(outputFolder, "en_US.lang")), Charsets.UTF_8.name());
            for (int line = 0; line < usLangEntries.keySet().size(); line++) {
                DataEntry entry = usLangEntries.get(line + 1);
                writer.println(entry.getEntry());
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException("Fatal exception whilst copying en_US lang file to output directory.", e);
        }

        LogHelper.info("Loaded following remaps..");
        for (Map.Entry<String, String> entry : keyRemaps.entrySet()) {
            LogHelper.info(entry.getKey() + " -> " + entry.getValue());
        }

        ResourceWalker walker = new ResourceWalker(new LangFileFilter());
        walker.setFolder(langFolder);
        try {
            for (File file : walker.startWalk()) {
                parseLangFile(file);
            }
        } catch (Exception e) {
            throw new RuntimeException("Fatal exception occurred whilst parsing lang files.", e);
        }

        for (String locale : localeLangEntries.keySet()) {
            File langFile = new File(outputFolder, locale + ".lang");
            writeLangFile(langFile, locale);
        }

        for (Map.Entry<String, List<DataEntry>> entry : invalidLocaleEntries.entrySet()) {
            try {
                PrintWriter writer = new PrintWriter(Utils.tryCreateFile(new File(outputFolder, entry.getKey() + ".lang.old")), Charsets.UTF_8.name());
                for (DataEntry e : entry.getValue()) {
                    writer.println(e.getEntry());
                }
                writer.flush();
                writer.close();
            } catch (Exception e) {
                throw new RuntimeException("Fatal error whilst writing invalid lang file entries for locale " + entry.getKey(), e);
            }
        }
    }

    public static void parseRemapFile(File remapFile) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(remapFile), Charsets.UTF_8));

            String line;
            while ((line = reader.readLine()) != null) {
                if (Strings.isNullOrEmpty(line)) {
                    continue;
                }
                String[] split = line.split("=");
                keyRemaps.put(split[0], split[1]);
            }

        } catch (Exception e) {
            throw new RuntimeException("Fatal error whilst reading remap file..", e);
        }
    }

    public static DataEntry getDataEntryForLocale(int line, String locale) {
        Map<Integer, DataEntry> langMap = localeLangEntries.get(locale);
        if (langMap.containsKey(line)) {
            return langMap.get(line);
        } else {
            DataEntry entry = usLangEntries.get(line);
            if (entry instanceof LangEntry) {
                LogHelper.info("Key %s missing from locale %s.. Using en_US translation..", ((LangEntry) entry).getKey(), locale);
            }
            return entry;
        }
    }

    public static String remapKey(String key) {
        if (keyRemaps.containsKey(key)) {
            return keyRemaps.get(key);
        }
        return key;
    }

    public static int getEntryIndex(LangEntry langEntry) {
        for (Map.Entry<Integer, DataEntry> entry : usLangEntries.entrySet()) {
            if (entry.getValue() instanceof LangEntry) {
                if (((LangEntry) entry.getValue()).getKey().equals(langEntry.getKey())) {
                    return entry.getKey();
                }
            }
        }
        return -1;
    }

    public static void parseLangFile(File langFile) {
        if (langFile.getName().equals("en_US.lang")) {
            return;
        }
        String locale = langFile.getName().replace(".lang", "");
        Map<Integer, DataEntry> langMap = new LinkedHashMap<>();
        List<DataEntry> invalidLines = new LinkedList<>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(langFile), Charsets.UTF_8));

            String line;
            int idx = 1;
            while((line = reader.readLine()) != null) {
                if (line.isEmpty() || line.startsWith("#") || line.startsWith("//") || !line.contains("=")) {
                    continue;
                }
                try {
                    String[] split = line.split("=");
                    LangEntry entry = new LangEntry(remapKey(split[0]), split[1]);
                    int index = getEntryIndex(entry);
                    if (index == -1) {
                        invalidLines.add(entry);
                        LogHelper.info("Unknown line inside %s lang file, LineNumber: %s, LineData: %s, RemappedData: %s", locale, idx, line, entry.getEntry());
                    } else {
                        langMap.put(index, entry);
                    }
                    idx++;
                } catch (Exception e) {
                    LogHelper.fatal("Error reading line %s!", idx);
                    e.printStackTrace();
                    idx++;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to read %s language file!", locale), e);
        }

        if (!invalidLines.isEmpty()) {
            invalidLocaleEntries.put(locale, invalidLines);
        }

        localeLangEntries.put(locale, langMap);
    }

    public static void writeLangFile(File file, String locale) {
        try {
            PrintWriter writer = new PrintWriter(Utils.tryCreateFile(file), Charsets.UTF_8.name());
            for (int line = 0; line < usLangEntries.keySet().size(); line++) {
                DataEntry entry = getDataEntryForLocale(line + 1, locale);
                writer.println(entry.getEntry());
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException("Fatal exception writing lang file for locale " + locale, e);
        }
    }

}
