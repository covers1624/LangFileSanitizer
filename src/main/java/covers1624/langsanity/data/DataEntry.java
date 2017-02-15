package covers1624.langsanity.data;

/**
 * Created by covers1624 on 15/02/2017.
 */
public abstract class DataEntry {

    public abstract String getEntry();

    @Override
    public String toString() {
        return getEntry();
    }
}
