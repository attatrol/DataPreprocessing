package com.github.attatrol.preprocessing.ui.i18n;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * This variation of {@link #I18nComboBox} uses output of {@link #toString()} method of an
 * item as a key for {@link #I18nProvider}. This is particularly useful with enumerations.
 * @author atta_troll
 *
 * @param <V>
 */
public class ToStringMappingI18nComboBox<V> extends ComboBox<V> {

    public ToStringMappingI18nComboBox(I18nProvider i18nProvider) {
        super();
        setCellFactory(new Callback<ListView<V>, ListCell<V>>() {
            @Override
            public ListCell<V> call(ListView<V> p) {
                ListCell<V> cell = new ListCell<V>() {
                    @Override
                    protected void updateItem(V item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText("");
                        }
                        else {
                            setText(i18nProvider.getValue(item.toString()));
                        }
                    }
                };
                return cell;
            }
        });
        setButtonCell(new ListCell<V>() {
            @Override
            protected void updateItem(V item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText("");
                }
                else {
                    setText(i18nProvider.getValue(item.toString()));
                }
            }
        });
    }

}
