package com.github.attatrol.preprocessing.ui.i18n;

/**
 * Provides internationalization service.
 * @author atta_troll
 *
 */
public interface I18nProvider {

    /**
     * Get localized string value for a key.
     * @param key key of a value
     * @return value
     */
    String getValue(String key);

    /**
     * Get localized name for an object.
     * Default realization forms a key from class name of an object,
     * then calls {{@link #getValue(String)}.
     * @param object an object
     * @return value
     */
    default String getValue(Object object) {
        return getValue(String.format("name.%s", object.getClass()));
    }

}
