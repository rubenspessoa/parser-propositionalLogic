/*
 * This software and all files contained in it are distrubted under the MIT license.
 * 
 * Copyright (c) 2013 Cogito Learning Ltd
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

/*
 * Modified by rubenspessoa on 06/08/16.
 */

/**
 * @mainpage
 * This Parser is a modification of CogPar (a versatile parser for mathematical expressions) which aims
 * to work with logical sentences.
 *
 * CogPar and this variation of Cogpar are distributed under the MIT license,
 * so feel free to use it in your own projects.
 */

package Parser.Lexer;

import Parser.AbstractSyntaxTree.*;
import java.util.LinkedList;

/**
 * A parser for logical expressions. The parser class defines a method
 * parse() which takes a string and returns an ExpressionNode that holds a
 * representation of the expression.
 *
 * Parsing is implemented in the form of a recursive descent parser.
 *
 */

public class Parser {

    LinkedList<Token> tokens;
    Token lookahead;

    /**
     * Parse a logical expression in a string and return an ExpressionNode.
     *
     * This is a convenience method that first converts the string into a linked
     * list of tokens using the expression tokenizer provided by the Tokenizer
     * class.
     *
     * @param expression
     *          the string holding the input
     * @return the internal representation of the expression in form of an
     *         expression tree made out of ExpressionNode objects
     */

    public ExpressionNode parse(String expression) throws Exception {
        Tokenizer tokenizer = Tokenizer.getExpressionTokenizer();
        tokenizer.tokenize(expression);
        LinkedList<Token> tokens = tokenizer.getTokens();
        return this.parse(tokens);
    }

    /**
     * Parse a logical expression contained in a list of tokens and return
     * an ExpressionNode.
     *
     * @param tokens
     *          a list of tokens holding the tokenized input
     * @return the internal representation of the expression in form of an
     *         expression tree made out of ExpressionNode objects
     */
    private ExpressionNode parse(LinkedList<Token> tokens) throws Exception {

        // implementing a recursive descent parser
        this.tokens = (LinkedList<Token>) tokens.clone();
        lookahead = this.tokens.getFirst();

        // top level non-terminal is expression
        ExpressionNode expr = expression();

        if (lookahead.token != Token.EPSILON)
            throw new ParserException("Unexpected symbol" + lookahead + "found");

        return expr;
    }

    /** handles the non-terminal expression */
    private ExpressionNode expression() throws Exception {
        // only one rule
        // expression -> cons bicons_op
        ExpressionNode c = cons();
        return biconsOp(c);
    }

    /** handles the non-terminal bicons */
    private ExpressionNode biconsOp(ExpressionNode expr) throws Exception {
        // biconsOp -> BICONSEQ cons biconsOp
        if (lookahead.token == Token.BICONSEQ) {
            nextToken();
            ExpressionNode c = cons();
            BiconsequenceExpressionNode biconseq = new BiconsequenceExpressionNode(expr, c);
            return biconsOp(biconseq);
        }

        return expr;
    }

    /** handles the non-terminal cons */
    private ExpressionNode cons() throws Exception {
        // cons -> disj consOp
        ExpressionNode d = disj();
        return consOp(d);
    }

    /** handles the non-terminal consOp */
    private ExpressionNode consOp(ExpressionNode expr) throws Exception {
        // consOp -> CONSEQ disj consOp
        if (lookahead.token == Token.CONSEQ) {
            nextToken();
            ExpressionNode d = disj();
            ConsequenceExpressionNode conseq = new ConsequenceExpressionNode(expr, d);
            return consOp(conseq);
        }

        return expr;
    }

    /** handles the non-terminal disj */
    private ExpressionNode disj() throws Exception {
        // disj -> conj disjOp
        ExpressionNode c = conj();
        return disjOp(c);
    }

    /** handles the non-terminal disjOp */
    private ExpressionNode disjOp(ExpressionNode expr) throws Exception {
        // disjOp -> DISJ conj disjOp
        if (lookahead.token == Token.DISJ) {
            DisjuctionExpressionNode disj = new DisjuctionExpressionNode(expr);
            nextToken();
            ExpressionNode c = conj();
            disj.add(c);
            return disjOp(disj);
        }

        return expr;
    }

    /** handles the non-terminal conj */
    private ExpressionNode conj() throws Exception {
        ExpressionNode a = argument();
        return conjOp(a);
    }

    /** handles the non-terminal conjOp */
    private ExpressionNode conjOp(ExpressionNode expr) throws Exception {
        // conjOp -> CONJ argument conjOp
        if (lookahead.token == Token.CONJ) {
            ConjunctionExpressionNode conj = new ConjunctionExpressionNode(expr);
            nextToken();
            ExpressionNode a = argument();
            conj.add(a);
            return conjOp(conj);
        }

        return expr;
    }

    /** handles the non-terminal argument */
    private ExpressionNode argument() throws Exception {
        // argument -> NEG argument
        if (lookahead.token == Token.NEG) {
            ExpressionNode neg = new NegationExpressionNode();
            nextToken();
            argument();
            return neg;
            // argument -> VARIABLE
        } else if (lookahead.token == Token.VARIABLE) {
            ExpressionNode var = new VariableExpressionNode(lookahead.sequence);
            nextToken();
            return var;
        } else if (lookahead.token == Token.BOOL) {
            ExpressionNode var = new ConstantExpressionNode(lookahead.sequence);
            nextToken();
            return var;
            // argument -> OPENBRACKET expression CLOSEBRACKET
        } else if (lookahead.token == Token.OPEN_BRACKET) {
            nextToken();
            ExpressionNode expr = expression();

            if (lookahead.token != Token.CLOSE_BRACKET) {
                throw new ParserException("Closing brackets expected and" + lookahead.sequence + "found instead.");
            }

            nextToken();
            return expr;
        } else if (lookahead.token == Token.EPSILON) {
            throw new ParserException("Unexpected end of input.");
        } else {
            throw new ParserException("Unexpected symbol '" + lookahead.sequence + "' was found.");
        }
    }

    /**
     * Remove the first token from the list and store the next token in lookahead
     */
    private void nextToken() {

        tokens.pop();

        if (tokens.isEmpty())
            lookahead = new Token(Token.EPSILON, "", -1);
        else
            lookahead = tokens.getFirst();
    }
}