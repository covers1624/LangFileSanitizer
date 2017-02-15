package covers1624.langsanity.data;

/**
 * Created by covers1624 on 15/02/2017.
 */
public class LangEntry extends DataEntry {

    private final String key;
    private final String value;

    public LangEntry(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getEntry() {
        return getKey() + "=" + getValue();
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

}
