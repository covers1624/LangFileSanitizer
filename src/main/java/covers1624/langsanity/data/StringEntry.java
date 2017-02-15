package covers1624.langsanity.data;

/**
 * Created by covers1624 on 15/02/2017.
 */
public class StringEntry extends DataEntry {

    private final String entry;

    public StringEntry(String entry) {
        this.entry = entry;
    }

    @Override
    public String getEntry() {
        return entry;
    }

    @Override
    public String toString() {
        if (entry.equals("\n")) {
            return "NewLine.";
        }
        return super.toString();
    }
}
