package de.uniwue.compare;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import de.uniwue.compare.token.Token;

/**
 * Connection between original and revised tokens.
 * Tokens are typically considered connected if they are equal or reference
 * the "same" text with changes inside
 */
public class ConnectedContent {
	private LinkedList<Token> original, revised;
	private final ContentType contentType;
	private String varianceType;

	public ConnectedContent(Token original, Token revised, ContentType contentType, String varianceType) {
		this(new LinkedList<Token>(Arrays.asList(original)),new LinkedList<Token>(Arrays.asList(revised)),contentType, varianceType);
	}
	public ConnectedContent(List<? extends Token> original, List<? extends Token> revised,
			ContentType contentType, String varianceType) {
		this.original = new LinkedList<>(original);
		this.revised = new LinkedList<>(revised);
		this.contentType = contentType;
		this.varianceType = varianceType;
	}

	public ConnectedContent(List<Token> equalContent) {
		if(equalContent.size() == 0)
			throw new IllegalArgumentException("Can't create an empty connected content of type equal");
		this.original = new LinkedList<Token>();
		addOriginal(equalContent);
		this.contentType = ContentType.EQUAL;
		this.varianceType = "NONE";
	}

	public ConnectedContent(ContentType type, String varianceType) {
		this(new LinkedList<Token>(), new LinkedList<Token>(), type, varianceType);
	}

	public LinkedList<Token> getOriginal() {
		if(original != null)
			return new LinkedList<Token>(original);
		else
			return new LinkedList<Token>();
	}

	public LinkedList<Token> getRevised() {
		if(revised != null)
			return new LinkedList<Token>(revised);
		else
			return new LinkedList<Token>();
	}

	public ContentType getContentType() {
		return contentType;
	}

	public String getVarianceType() {
		return varianceType;
	}

	public void setVarianceType(String varianceType) {
		this.varianceType = varianceType;
	}

	public void addOriginal(Collection<? extends Token> original) {
		this.original.addAll(original);
	}

	public void addOriginal(Token... original) {
		this.original.addAll(Arrays.asList(original));
	}

	public void addRevised(Collection<? extends Token> revised) {
		if (contentType.equals(ContentType.EQUAL))
			this.original.addAll(revised);
		this.revised.addAll(revised);
	}

	public void addRevised(Token... revised) {
		this.addRevised(Arrays.asList(revised));
	}

	public String getOriginalAsText() {
		String toString = "";
		long lastEnd = original.size() > 0 ? original.getFirst().getBegin() : 0;
		for (Token token : original) {
			if (token.getBegin() < token.getEnd()) {
				toString += token.getContent();
				if (lastEnd < token.getBegin())
					toString += " ";
				lastEnd = token.getEnd();
			}
		}

		return toString;
	}

	public String getRevisedAsText() {
		String toString = "";
		long lastEnd = revised.size() > 0 ? revised.getFirst().getBegin() : 0;
		for (Token token : revised) {
			if (token.getBegin() < token.getEnd()) {
				if (lastEnd < token.getBegin())
					toString += " ";
				toString += token.getContent();
				lastEnd = token.getEnd();
			}
		}
		return toString;
	}

	@Override
	public String toString() {
		return "[" + contentType +" | " + varianceType + ": \'" + original + "\', \'" + revised + "\']";
	}
}
