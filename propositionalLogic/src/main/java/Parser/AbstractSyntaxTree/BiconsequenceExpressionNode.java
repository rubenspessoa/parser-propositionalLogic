/*
 * This software and all files contained in it are distributed under the MIT license.
 *
 * Copyright (c) 2016 BARROS FILHO, Rubens
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

package Parser.AbstractSyntaxTree;

/**
 * Created by rubenspessoa on 27/08/16.
 */
public class BiconsequenceExpressionNode implements ExpressionNode {

    private ExpressionNode leftTerm;
    private ExpressionNode rightTerm;

    public BiconsequenceExpressionNode(ExpressionNode leftTerm, ExpressionNode rightTerm) {
        this.leftTerm = leftTerm;
        this.rightTerm = rightTerm;
    }

    public int getType() {
        return ExpressionNode.BICONSEQ_NODE;
    }

    public boolean getValue() throws Exception {
        return (!leftTerm.getValue() || rightTerm.getValue())
                && (!rightTerm.getValue() || leftTerm.getValue());
    }

    public void accept(ExpressionNodeVisitor visitor) {
        visitor.visit(this);
        leftTerm.accept(visitor);
        rightTerm.accept(visitor);
    }

    public ExpressionNode getLeftTerm() {
        return this.leftTerm;
    }

    public ExpressionNode getRightTerm() {
        return this.rightTerm;
    }

}
