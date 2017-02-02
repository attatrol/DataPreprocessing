
package com.github.attatrol.preprocessing.datasource.parsing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Type of current data record token.
 * 
 * @author atta_troll
 *
 */
public enum TokenType {
    /**
     * Integer value.
     */
    INTEGER,
    /**
     * Float value
     */
    FLOAT,
    /**
     * Some string value, interpreted as categorical
     */
    CATEGORICAL_STRING,
    /**
     * Boolean data, either true or false.
     */
    BINARY,
    /**
     * Boolean data, either 0 or 1.
     */
    BINARY_DIGITAL,
    /**
     * Undefined token, columns containing this should be excluded from further processing
     */
    UNKNOWN,
    /**
     * Undefined token, value missing, should be excluded from further processing
     */
    MISSING;

    /**
     * Hasse diagram representing strict order on the token type set.
     */
    private static Map<TokenType, TokenType[]> hasseDiagram;

    /**
     * Token types that can be parsed as this type.
     */
    private Set<TokenType> subtypes;

    /**
     * @return token types that can be parsed as this type
     */
    public Set<TokenType> getSubtypes() {
        return subtypes;
    }

    /**
     * Hidden setter.
     * 
     * @param subtypes
     *        token types that can be parsed as this type
     */
    private void setSubtypes(Set<TokenType> subtypes) {
        this.subtypes = Collections.unmodifiableSet(subtypes);
    }

    /*
     * Setup subtypes. Be sure you have no cycles in type hierarchy!
     */
    static {
        MISSING.setSubtypes(EnumSet.noneOf(TokenType.class));
        BINARY_DIGITAL.setSubtypes(EnumSet.of(MISSING));
        BINARY.setSubtypes(EnumSet.of(MISSING));
        INTEGER.setSubtypes(EnumSet.of(MISSING, BINARY_DIGITAL));
        FLOAT.setSubtypes(EnumSet.of(MISSING, BINARY_DIGITAL, INTEGER));
        CATEGORICAL_STRING.setSubtypes(EnumSet.of(MISSING, BINARY_DIGITAL, INTEGER, FLOAT, BINARY));
        UNKNOWN.setSubtypes(
                EnumSet.of(MISSING, BINARY_DIGITAL, INTEGER, FLOAT, BINARY, CATEGORICAL_STRING));

        hasseDiagram = generateHasseDiagram();
    }

    /**
     * Hasse diagram represents strict ordered set presentation of this enum. Remember, it is not
     * immutable as it is not possible to enforce immutability for value arrays, so all consumers
     * must not modify it, otherwise major errors during token type detection would arise.
     * 
     * @return Hasse diagram
     */
    public static Map<TokenType, TokenType[]> getHasseDiagram() {
        return hasseDiagram;
    }

    /**
     * Gets general types for this token type in their order of appearance based on
     * {@link #hasseDiagram}.
     * 
     * @param tokenType
     *        a token type
     * @return general types for this token type
     */
    public static TokenType[] getGeneralTypes(TokenType tokenType) {
        Set<TokenType> generalTypes = EnumSet.noneOf(TokenType.class);
        List<TokenType> currentLayer = new ArrayList<>();
        for (TokenType generalType : hasseDiagram.get(tokenType)) {
            currentLayer.add(generalType);
        }
        while (!currentLayer.isEmpty()) {
            final List<TokenType> nextLayer = new ArrayList<>();
            currentLayer.forEach(t -> {
                generalTypes.add(t);
                for (TokenType t1 : hasseDiagram.get(t)) {
                    nextLayer.add(t1);
                }
            });
            currentLayer = nextLayer;
        }
        return generalTypes.toArray(new TokenType[generalTypes.size()]);
    }

    /**
     * Finds closest common ancestor using Hasse diagram.
     * 
     * @param type1
     *        some token type nonnull
     * @param type2
     *        some token type nonnull
     * @return closest common ancestor of type1 and type2
     */
    public static TokenType getClosestCommonAncestor(TokenType type1, TokenType type2) {
        if (type1 == type2) {
            return type1;
        }
        else if (type1.getSubtypes().contains(type2)) {
            return type1;
        }
        else if (type2.getSubtypes().contains(type1)) {
            return type2;
        }
        else {
            for (TokenType ancestor1 : hasseDiagram.get(type1)) {
                final TokenType commonAncestor = getClosestCommonAncestor(ancestor1, type2);
                if (commonAncestor != null) {
                    return commonAncestor;
                }
            }
            return null;
        }
    }

    /**
     * Generates a Hasse diagram for all token types. This is needed because we have to determine
     * closest common supertype for any 2 token types to figure out type of some token column (which
     * is present in a data source).
     * 
     * @return map that point to the closest supertypes for each token type
     * @throws IllegalStateException
     *         if connected Hasse diagram can't be build (make sure that {@link #UNKNOWN} has all
     *         types as subtypes).
     */
    private static Map<TokenType, TokenType[]> generateHasseDiagram() throws IllegalStateException {
        Map<TokenType, Set<TokenType>> ancestors = new EnumMap<>(TokenType.class);
        for (TokenType type : TokenType.values()) {
            final Set<TokenType> supertypes = EnumSet.noneOf(TokenType.class);
            ancestors.put(type, supertypes);
            for (TokenType type1 : TokenType.values()) {
                if (type1.getSubtypes().contains(type)) {
                    supertypes.add(type1);
                }
            }
        }
        for (TokenType type : TokenType.values()) {
            final Set<TokenType> supertypes = ancestors.get(type);
            Iterator<TokenType> iterator = supertypes.iterator();
            while (iterator.hasNext()) {
                if (getLongestPath(type, iterator.next(), ancestors, 0) > 1) {
                    iterator.remove();
                }
            }
        }
        Map<TokenType, TokenType[]> result = new EnumMap<>(TokenType.class);
        ancestors.forEach((type, supertypes) -> result.put(type,
                supertypes.toArray(new TokenType[supertypes.size()])));
        return result;
    }

    /**
     * Calculates longest possible path from token to token in ordered Hasse diagram.
     * 
     * @param beginPoint
     *        initial point
     * @param endPoint
     *        end point
     * @param ancestors
     *        Hasse diagram prototype
     * @param currentPathLength
     *        current number of steps done
     * @return longest possible path
     */
    private static int getLongestPath(TokenType beginPoint, TokenType endPoint,
            Map<TokenType, Set<TokenType>> ancestors, int currentPathLength) {
        Iterator<TokenType> iterator = ancestors.get(beginPoint).iterator();
        int longestPath = 0;
        if (beginPoint == endPoint) {
            return currentPathLength;
        }
        while (iterator.hasNext()) {
            TokenType nextPoint = iterator.next();
            final int newPath =
                    getLongestPath(nextPoint, endPoint, ancestors, currentPathLength + 1);
            if (longestPath < newPath) {
                longestPath = newPath;
            }
        }
        return longestPath;
    }

    /**
     * Some token types are beyond scope of current program, they are unsupported and here is a
     * method to check if some token type is supported.
     * 
     * @param type
     *        token type to check
     * @return result of check
     */
    public static boolean isSupportedTokenType(TokenType type) {
        return type != TokenType.UNKNOWN && type != TokenType.MISSING;
    }
}
