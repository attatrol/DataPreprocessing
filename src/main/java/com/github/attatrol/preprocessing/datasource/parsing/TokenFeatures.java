package com.github.attatrol.preprocessing.datasource.parsing;

import com.github.attatrol.preprocessing.datasource.parsing.missing.MissingTokenSubstitutorFactory;

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
