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
 * Copyright (C) 2005 Thomas E Enebo <enebo@acm.org>
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
package org.jruby;

import java.io.IOException;

import org.jruby.javasupport.Java;
import org.jruby.javasupport.JavaObject;
import org.jruby.runtime.Arity;
import org.jruby.runtime.Block;
import org.jruby.runtime.CallbackFactory;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.runtime.load.Library;
import org.jruby.internal.runtime.methods.DynamicMethod;

/**
 * Module which defines JRuby-specific methods for use. 
 */
public class RubyJRuby {
    public static RubyModule createJRuby(Ruby runtime) {
        runtime.getModule("Kernel").callMethod(runtime.getCurrentContext(),"require", runtime.newString("java"));
        RubyModule jrubyModule = runtime.defineModule("JRuby");
        CallbackFactory callbackFactory = runtime.callbackFactory(RubyJRuby.class);
        jrubyModule.defineModuleFunction("parse", 
                callbackFactory.getOptSingletonMethod("parse"));
        jrubyModule.getMetaClass().defineAlias("ast_for", "parse");
        jrubyModule.defineModuleFunction("runtime", 
                callbackFactory.getSingletonMethod("runtime"));
        jrubyModule.defineModuleFunction("reference", 
                                         callbackFactory.getFastSingletonMethod("reference", RubyKernel.IRUBY_OBJECT));

        return jrubyModule;
    }

    public static RubyModule createJRubyExt(Ruby runtime) {
        runtime.getModule("Kernel").callMethod(runtime.getCurrentContext(),"require", runtime.newString("java"));
        RubyModule mJRubyExt = runtime.getOrCreateModule("JRuby").defineModuleUnder("Extensions");
        CallbackFactory cf = runtime.callbackFactory(RubyJRuby.class);

        mJRubyExt.defineFastPublicModuleFunction("steal_method", cf.getFastSingletonMethod("steal_method", IRubyObject.class, IRubyObject.class));
        mJRubyExt.defineFastPublicModuleFunction("steal_methods", cf.getFastOptSingletonMethod("steal_methods"));

        runtime.getObject().includeModule(mJRubyExt);

        return mJRubyExt;
    }

    public static class ExtLibrary implements Library {
        public void load(Ruby runtime) throws IOException {
            RubyJRuby.createJRubyExt(runtime);
        }
    }
    
    public static IRubyObject runtime(IRubyObject recv, Block unusedBlock) {
        return Java.java_to_ruby(recv, JavaObject.wrap(recv.getRuntime(), recv.getRuntime()), Block.NULL_BLOCK);
    }
    
    public static IRubyObject parse(IRubyObject recv, IRubyObject[] args, Block block) {
        if(block.isGiven()) {
            Arity.checkArgumentCount(recv.getRuntime(),args,0,0);
            return Java.java_to_ruby(recv, JavaObject.wrap(recv.getRuntime(), block.getIterNode().getBodyNode()), Block.NULL_BLOCK);
        } else {
            Arity.checkArgumentCount(recv.getRuntime(),args,1,3);
            String filename = "-";
            boolean extraPositionInformation = false;
            RubyString content = args[0].convertToString();
            if(args.length>1) {
                filename = args[1].convertToString().toString();
                if(args.length>2) {
                    extraPositionInformation = args[2].isTrue();
                }
            }
            return Java.java_to_ruby(recv, JavaObject.wrap(recv.getRuntime(), 
               recv.getRuntime().parse(content.toString(), filename, null, 0, extraPositionInformation)), Block.NULL_BLOCK);
        }
    }

    public static IRubyObject steal_method(IRubyObject recv, IRubyObject type, IRubyObject methodName) {
        RubyModule to_add = null;
        if(recv instanceof RubyModule) {
            to_add = (RubyModule)recv;
        } else {
            to_add = recv.getSingletonClass();
        }
        String name = methodName.toString();
        if(!(type instanceof RubyModule)) {
            throw recv.getRuntime().newArgumentError("First argument must be a module/class");
        }

        DynamicMethod method = ((RubyModule)type).searchMethod(name);
        if(method == null || method.isUndefined()) {
            throw recv.getRuntime().newArgumentError("No such method " + name + " on " + type);
        }

        to_add.addMethod(name, method);
        return recv.getRuntime().getNil();
    }

    public static IRubyObject steal_methods(IRubyObject recv, IRubyObject[] args) {
        Arity.checkArgumentCount(recv.getRuntime(), args, 1, -1);
        IRubyObject type = args[0];
        for(int i=1;i<args.length;i++) {
            steal_method(recv, type, args[i]);
        }
        return recv.getRuntime().getNil();
    }

    public static IRubyObject reference(IRubyObject recv, IRubyObject obj) {
        return Java.wrap(recv.getRuntime().getModule("JavaUtilities"), JavaObject.wrap(recv.getRuntime(), obj));
    }
}
