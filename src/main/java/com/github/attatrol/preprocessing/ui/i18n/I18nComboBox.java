
package com.github.attatrol.preprocessing.ui.i18n;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * There is a need to localize names of combo box items, so we have to inject this names using some
 * {@link I18nProvider}.
 * 
 * @author atta_troll
 *
 * @param <V>
 */
public class I18nComboBox<V> extends ComboBox<V> {

    public I18nComboBox(I18nProvider i18nProvider) {
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
                            setText(i18nProvider.getValue(item));
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
                    setText(i18nProvider.getValue(item));
                }
            }
        });
    }

}
