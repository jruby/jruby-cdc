/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jruby.ext.ffi.jffi;

import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;

/**
 *
 * @author wayne
 */
interface IntParameterConverter {
    int intValue(ThreadContext context, IRubyObject value);
    int intValue(Invocation invocation, ThreadContext context, IRubyObject value);
    boolean needsInvocationSession();
}
