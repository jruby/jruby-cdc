/***** BEGIN LICENSE BLOCK *****
 * Version: CPL 1.0/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Common Public
 * License Version 1.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.eclipse.org/legal/cpl-v10.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Copyright (C) 2001-2004 Jan Arne Petersen <jpetersen@uni-bonn.de>
 * Copyright (C) 2002 Benoit Cerrina <b.cerrina@wanadoo.fr>
 * Copyright (C) 2002-2004 Anders Bengtsson <ndrsbngtssn@yahoo.se>
 * Copyright (C) 2004 Thomas E Enebo <enebo@acm.org>
 * 
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the CPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the CPL, the GPL or the LGPL.
 ***** END LICENSE BLOCK *****/
package org.jruby.runtime;

import org.jruby.RubyModule;
import org.jruby.lexer.yacc.SourcePosition;
import org.jruby.runtime.builtin.IRubyObject;

/**
 *
 * @author  jpetersen
 * @version $Revision$
 */
public class Frame {
    private IRubyObject self = null;
    private IRubyObject[] args = null;
    private String lastFunc = null;
    private RubyModule lastClass = null;
    private final SourcePosition position;
    private Iter iter = Iter.ITER_NOT;

    public Frame(IRubyObject self,
                 IRubyObject[] args,
                 String lastFunc,
                 RubyModule lastClass,
                 SourcePosition position,
                 Iter iter)
    {
        this.self = self;
        this.args = args;
        this.lastFunc = lastFunc;
        this.lastClass = lastClass;
        this.position = position;
        this.iter = iter;
    }

    public Frame(Frame frame) {
        this(frame.self, frame.args, frame.lastFunc, frame.lastClass, frame.position, frame.iter);
    }

    /** Getter for property args.
     * @return Value of property args.
     */
    public IRubyObject[] getArgs() {
        return args;
    }

    /** Setter for property args.
     * @param args New value of property args.
     */
    public void setArgs(IRubyObject[] args) {
        this.args = args;
    }

    /**
     * @return the frames current position
     */
    public SourcePosition getPosition() {
        return position;
    }

    /** Getter for property iter.
     * @return Value of property iter.
     */
    public Iter getIter() {
        return iter;
    }

    /** Setter for property iter.
     * @param iter New value of property iter.
     */
    public void setIter(Iter iter) {
        this.iter = iter;
    }

    public boolean isBlockGiven() {
        return iter.isBlockGiven();
    }

    /** Getter for property lastClass.
     * @return Value of property lastClass.
     */
    public RubyModule getLastClass() {
        return lastClass;
    }

    /** Setter for property lastClass.
     * @param lastClass New value of property lastClass.
     */
    public void setLastClass(RubyModule lastClass) {
        this.lastClass = lastClass;
    }

    /** Getter for property lastFunc.
     * @return Value of property lastFunc.
     */
    public String getLastFunc() {
        return lastFunc;
    }

    /** Setter for property lastFunc.
     * @param lastFunc New value of property lastFunc.
     */
    public void setLastFunc(String lastFunc) {
        this.lastFunc = lastFunc;
    }

    /** Getter for property self.
     * @return Value of property self.
     */
    public IRubyObject getSelf() {
        return self;
    }

    /** Setter for property self.
     * @param self New value of property self.
     */
    public void setSelf(IRubyObject self) {
        this.self = self;
    }

    public Frame duplicate() {
        final IRubyObject[] newArgs;
        if (args != null) {
            newArgs = new IRubyObject[args.length];
            System.arraycopy(args, 0, newArgs, 0, args.length);
        } else {
            newArgs = null;
        }
        return new Frame(self, newArgs, lastFunc, lastClass, position, iter);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer sb = new StringBuffer(50);
        sb.append(position != null ? position.toString() : "-1");
        sb.append(':');
        if (lastFunc != null) {
            sb.append("in ");
            sb.append(lastFunc);
        }
        return sb.toString();
    }
}
