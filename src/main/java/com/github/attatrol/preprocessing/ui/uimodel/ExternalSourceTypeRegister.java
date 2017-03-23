
package com.github.attatrol.preprocessing.ui.uimodel;

import java.io.File;
import java.io.IOException;

import com.github.attatrol.preprocessing.datasource.syntax.SyntaxRegister;

import javafx.scene.layout.Pane;

/**
 * Data source type defines external format of data source.
 * 
 * @author atta_troll
 *
 */
public enum ExternalSourceTypeRegister {

    /**
     * Data source that is located in a single file.
     */
    SINGLE_FILE(new SyntaxRegister[] {
        SyntaxRegister.COMMA_SEPARATED_LINES,
        SyntaxRegister.COMMA_SEPARATED_TITLED_LINES,
        SyntaxRegister.SEMICOLON_SEPARATED_LINES,
        SyntaxRegister.SEMICOLON_SEPARATED_TITLED_LINES,
        SyntaxRegister.TABULATION_SEPARATED_LINES,
        SyntaxRegister.TABULATION_SEPARATED_TITLED_LINES,
    }) {

        @Override
        public Pane createPreviewPane(Object externalSource) throws IOException {
            return PreviewFilePane.createPreviewFilePane((File) externalSource);
        }
    },
    /**
     * Data source that is disseminated among different files in some folder.
     */
    DIRECTORY(new SyntaxRegister[0]) {

        @Override
        public Pane createPreviewPane(Object externalSource) throws IOException {
            return PreviewFilePane.createPreviewFilePane((File) externalSource);
        }
    };
    /*
     * TODO add JDBC connection here.
     */

    /**
     * Possible syntaxes for a data source type.
     */
    private SyntaxRegister[] possibleSyntaxes;

    /**
     * Default ctor.
     * 
     * @param possibleSyntaxes
     *        possible data source syntaxes
     */
    ExternalSourceTypeRegister(SyntaxRegister[] possibleSyntaxes) {
        this.possibleSyntaxes = possibleSyntaxes;
    }

    /**
     * @return possible syntaxes for a data source type
     */
    public SyntaxRegister[] getPossibleSyntaxes() {
        return possibleSyntaxes;
    }

    /**
     * Factory method for a preview pane. This pane should
     * have enough info for user to determine syntax of the data source.
     * @param externalSource external source object
     * @return
     * @throws IOException
     */
    public abstract Pane createPreviewPane(Object externalSource) throws IOException;

}
