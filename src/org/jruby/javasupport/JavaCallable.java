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
 * Copyright (C) 2002-2004 Anders Bengtsson <ndrsbngtssn@yahoo.se>
 * Copyright (C) 2004 Stefan Matthias Aust <sma@3plus4.de>
 * Copyright (C) 2004 Jan Arne Petersen <jpetersen@uni-bonn.de>
 * Copyright (C) 2004 David Corbin <dcorbin@users.sourceforge.net>
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
package org.jruby.javasupport;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import org.jruby.Ruby;
import org.jruby.RubyArray;
import org.jruby.RubyBoolean;
import org.jruby.RubyClass;
import org.jruby.RubyFixnum;
import org.jruby.RubyString;
import org.jruby.runtime.CallbackFactory;
import org.jruby.runtime.builtin.IRubyObject;

public abstract class JavaCallable extends JavaAccessibleObject implements ParameterTypes {

    public JavaCallable(Ruby runtime, RubyClass rubyClass) {
        super(runtime, rubyClass);
    }

    public static void registerRubyMethods(Ruby runtime, RubyClass result) {
        
        CallbackFactory callbackFactory = runtime.callbackFactory(JavaCallable.class);

        result.defineFastMethod("public?",  callbackFactory.getFastMethod("public_p"));
        result.defineFastMethod("arity", callbackFactory.getFastMethod("arity"));
        result.defineFastMethod("argument_types", callbackFactory.getFastMethod("argument_types"));
        result.defineFastMethod("parameter_types", callbackFactory.getFastMethod("parameter_types"));
        result.defineFastMethod("exception_types", callbackFactory.getFastMethod("exception_types"));
        result.defineFastMethod("generic_parameter_types", callbackFactory.getFastMethod("generic_parameter_types"));
        result.defineFastMethod("generic_exception_types", callbackFactory.getFastMethod("generic_exception_types"));
        result.defineFastMethod("parameter_annotations", callbackFactory.getFastMethod("parameter_annotations"));
        result.defineFastMethod("varargs?", callbackFactory.getFastMethod("varargs_p"));
        result.defineFastMethod("to_generic_string", callbackFactory.getFastMethod("to_generic_string"));
        result.defineFastMethod("inspect", callbackFactory.getFastMethod("inspect"));
    }

    public abstract int getArity();
    public abstract int getModifiers();
    public abstract Class<?>[] getParameterTypes();
    public abstract Class<?>[] getExceptionTypes();
    public abstract Type[] getGenericExceptionTypes();
    public abstract Type[] getGenericParameterTypes();
    public abstract Annotation[][] getParameterAnnotations();
    public abstract boolean isVarArgs();
    public abstract String toGenericString();
    /**
     * @return the name used in the head of the string returned from inspect()
     */
    protected abstract String nameOnInspection();

    public final RubyFixnum arity() {
        return getRuntime().newFixnum(getArity());
    }

    public final RubyArray argument_types() {
        return JavaClass.getRubyArray(getRuntime(), getParameterTypes());
    }

    // same as argument_types, but matches name in java.lang.reflect.Constructor/Method
    public IRubyObject parameter_types() {
        return JavaClass.getRubyArray(getRuntime(), getParameterTypes());
    }

    public IRubyObject exception_types() {
        return JavaClass.getRubyArray(getRuntime(), getExceptionTypes());
    }

    public IRubyObject generic_parameter_types() {
        return Java.getInstance(getRuntime(), getGenericParameterTypes());
    }

    public IRubyObject generic_exception_types() {
        return Java.getInstance(getRuntime(), getGenericExceptionTypes());
    }

    public IRubyObject parameter_annotations() {
        return Java.getInstance(getRuntime(), getParameterAnnotations());
    }
    
    public RubyBoolean varargs_p() {
        return getRuntime().newBoolean(isVarArgs());
    }
    
    public RubyString to_generic_string() {
        return getRuntime().newString(toGenericString());
    }

    public IRubyObject inspect() {
        StringBuffer result = new StringBuffer();
        result.append(nameOnInspection());
        Class<?>[] parameterTypes = getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            result.append(parameterTypes[i].getName());
            if (i < parameterTypes.length - 1) {
                result.append(',');
            }
        }
        result.append(")>");
        return getRuntime().newString(result.toString());
    }


    public RubyBoolean public_p() {
        return RubyBoolean.newBoolean(getRuntime(), Modifier.isPublic(getModifiers()));
    }


}
