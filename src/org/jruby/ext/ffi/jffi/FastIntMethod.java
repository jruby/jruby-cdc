
package org.jruby.ext.ffi.jffi;

import com.kenai.jffi.Function;
import com.kenai.jffi.Invoker;
import org.jruby.RubyModule;
import org.jruby.internal.runtime.methods.CallConfiguration;
import org.jruby.internal.runtime.methods.DynamicMethod;
import org.jruby.runtime.Arity;
import org.jruby.runtime.Visibility;

abstract class FastIntMethod extends DynamicMethod {
    protected static final Invoker invoker = Invoker.getInstance();
    protected final Arity arity;
    protected final Function function;
    protected final IntResultConverter resultConverter;
    protected final boolean needsInvocationSession;
    public FastIntMethod(RubyModule implementationClass, Function function,
            IntResultConverter resultConverter, IntParameterConverter[] paramConverters) {
        super(implementationClass, Visibility.PUBLIC, CallConfiguration.NO_FRAME_NO_SCOPE);
        this.resultConverter = resultConverter;
        this.arity = Arity.fixed(paramConverters.length);
        this.function = function;

        boolean needsInvocation = false;
        for (IntParameterConverter c : paramConverters) {
            if (c.needsInvocationSession()) {
                needsInvocation = true;
                break;
            }
        }
        this.needsInvocationSession = needsInvocation;
    }
    @Override
    public final DynamicMethod dup() {
        return this;
    }
    @Override
    public final Arity getArity() {
        return arity;
    }
    @Override
    public final boolean isNative() {
        return true;
    }
}
