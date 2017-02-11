package com.github.attatrol.preprocessing.ui.i18n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Localizes UI of this application. Eager singleton
 * @author atta_troll
 *
 */
public class UiI18nProvider implements I18nProvider {

    /**
     * Singleton instance.
     */
    public static final I18nProvider INSTANCE = new UiI18nProvider();

    public static final String UNKNOWN = "VALUE_MISSING";

    /**
     * Current bundle.
     */
    private ResourceBundle currentBundle;

    /**
     * Default ctor.
     */
    private UiI18nProvider() {
        currentBundle = ResourceBundle.getBundle("uistrings");
    }

    @Override
    public String getValue(String key) {
        try {
            return currentBundle.getString(key);
        }
        catch (MissingResourceException ex) {
            return UNKNOWN;
        }
    }

    @Override
    public String getValue(Object object) {
        return getValue(String.format("name.%s", object.getClass().getName()));
    }
}
