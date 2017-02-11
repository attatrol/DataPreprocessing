package com.github.attatrol.preprocessing.ui;

import com.github.attatrol.preprocessing.ui.i18n.UiI18nProvider;
import com.github.attatrol.preprocessing.ui.misc.PositiveDoubleReturnDialog;

/**
 * Dialog used to set p-Norm p coefficient.
 * @author atta_troll
 *
 */
class PNormCoeffReturnDialog extends PositiveDoubleReturnDialog {

    /**
     * Default ctor.
     */
    public PNormCoeffReturnDialog() {
        super(UiI18nProvider.INSTANCE.getValue("p.norm.dialog.title"),
                UiI18nProvider.INSTANCE.getValue("p.norm.dialog.info"));
    }

    @Override
    protected void validate() throws Exception {
        super.validate();
        if (Double.parseDouble(textField.getText()) < 1.) {
            throw new IllegalStateException(
                    UiI18nProvider.INSTANCE.getValue("p.norm.dialog.bad.value"));
        }
    }
}
