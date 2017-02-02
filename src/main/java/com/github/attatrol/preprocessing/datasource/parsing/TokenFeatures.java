package com.github.attatrol.preprocessing.datasource.parsing;

import java.io.IOException;
import java.util.Arrays;

import com.github.attatrol.preprocessing.datasource.DataSource;
import com.github.attatrol.preprocessing.datasource.TextFileDataSource;
import com.github.attatrol.preprocessing.datasource.TitledDataSource;
import com.github.attatrol.preprocessing.datasource.TokenDataSourceUtils;
import com.github.attatrol.preprocessing.datasource.parsing.missing.MissingTokenSubstitutor;
import com.github.attatrol.preprocessing.datasource.parsing.missing.MissingTokenSubstitutorFactory;
import com.github.attatrol.preprocessing.datasource.parsing.record.RecordTokenizer;

/**
 * Common features of a token, POJO.
 * @author atta_troll
 *
 */
public class TokenFeatures {

	/**
	 * {@code true} if some records have omissions in this token.
	 */
	private boolean hasOmissions;

	/**
	 * {@code true} if user marked this token as not being used in cluster process. 
	 */
	private boolean isInUse;

	/**
	 * Title of the token.
	 */
	private String title;

	/**
	 * Token type, describes what kind of data is in this token.
	 */
	private TokenType type;

	/**
	 * Missing token substitutor.
	 */
	private MissingTokenSubstitutorFactory<?> missingTokenSubstitutorFactory;

	//
	// Getters and setters below
	//

	public boolean hasOmissions() {
		return hasOmissions;
	}

	public boolean isInUse() {
		return isInUse;
	}

	public void setHasOmissions(boolean hasOmissions) {
		this.hasOmissions = hasOmissions;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public TokenType getType() {
		return type;
	}

	public void setType(TokenType type) {
		this.type = type;
	}

	public void setInUse(boolean isInUse) {
		this.isInUse = isInUse;
	}

	public MissingTokenSubstitutorFactory<?> getMissingTokenSubstitutor() {
		return missingTokenSubstitutorFactory;
	}

	public void setMissingTokenSubstitutor(MissingTokenSubstitutorFactory<?> missingTokenSubstitutorFactory) {
		this.missingTokenSubstitutorFactory = missingTokenSubstitutorFactory;
	}

}
