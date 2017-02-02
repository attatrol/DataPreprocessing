package com.github.attatrol.preprocessing.ui;

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
        super("P-Norm coefficient setup dialog",
                "Enter P-Norm coefficient value (must be greater than 1)");
    }

    @Override
    protected void validate() throws Exception {
        super.validate();
        if (Double.parseDouble(textField.getText()) < 1.) {
            throw new IllegalStateException("Value must be greater than 1");
        }
    }
}
