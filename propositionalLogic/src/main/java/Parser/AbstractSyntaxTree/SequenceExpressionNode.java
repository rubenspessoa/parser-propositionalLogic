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

package Parser.AbstractSyntaxTree;

import java.util.LinkedList;

public abstract class SequenceExpressionNode implements ExpressionNode {

    public class Term {

        public ExpressionNode expression;

        public Term(ExpressionNode expression) {
            super();
            this.expression = expression;
        }
    }

    protected LinkedList<Term> terms;

    public SequenceExpressionNode() {
        this.terms = new LinkedList<Term>();
    }

    public SequenceExpressionNode(ExpressionNode a) {
        this.terms = new LinkedList<Term>();
        this.terms.add(new Term(a));
    }

    public void add(ExpressionNode a) {
        this.terms.add(new Term(a));
    }
}
