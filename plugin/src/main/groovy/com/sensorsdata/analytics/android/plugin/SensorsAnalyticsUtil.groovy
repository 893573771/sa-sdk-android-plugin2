/*
 * Created by wangzhuozhou on 2015/08/12.
 * Copyright 2015－2020 Sensors Data Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sensorsdata.analytics.android.plugin

import groovy.transform.CompileStatic
import org.apache.commons.io.IOUtils
import org.apache.commons.io.output.ByteArrayOutputStream
import org.objectweb.asm.Opcodes

@CompileStatic
class SensorsAnalyticsUtil {
    public static final int ASM_VERSION = Opcodes.ASM6
    private static final HashSet<String> targetFragmentClass = new HashSet()
    private static final HashSet<String> targetMenuMethodDesc = new HashSet()
    private static final HashSet<String> specialClass = new HashSet()

    static {
        /**
         * Menu
         */
        targetMenuMethodDesc.add("onContextItemSelected(Landroid/view/MenuItem;)Z")
        targetMenuMethodDesc.add("onOptionsItemSelected(Landroid/view/MenuItem;)Z")

        /**
         * For Android App Fragment
         */
        targetFragmentClass.add('android/app/Fragment')
        targetFragmentClass.add('android/app/ListFragment')
        targetFragmentClass.add('android/app/DialogFragment')

        /**
         * For Support V4 Fragment
         */
        targetFragmentClass.add('android/support/v4/app/Fragment')
        targetFragmentClass.add('android/support/v4/app/ListFragment')
        targetFragmentClass.add('android/support/v4/app/DialogFragment')

        /**
         * For AndroidX Fragment
         */
        targetFragmentClass.add('androidx/fragment/app/Fragment')
        targetFragmentClass.add('androidx/fragment/app/ListFragment')
        targetFragmentClass.add('androidx/fragment/app/DialogFragment')

        for (className in SensorsAnalyticsTransformHelper.special) {
            specialClass.add(className.replace('.', '/'))
        }

    }

    static boolean isPublic(int access) {
        return (access & Opcodes.ACC_PUBLIC) != 0
    }

    static boolean isStatic(int access) {
        return (access & Opcodes.ACC_STATIC) != 0
    }

    static boolean isTargetMenuMethodDesc(String nameDesc) {
        return targetMenuMethodDesc.contains(nameDesc)
    }

    static boolean isInstanceOfFragment(String superName) {
        return targetFragmentClass.contains(superName)
    }

    static boolean isTargetClassInSpecial(String className) {
        return specialClass.contains(className)
    }

    static byte[] toByteArrayAndAutoCloseStream(InputStream input) throws Exception {
        ByteArrayOutputStream output = null
        try {
            output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 4]
            int n = 0
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n)
            }
            output.flush()
            return output.toByteArray()
        } catch (Exception e) {
            throw e
        } finally {
            IOUtils.closeQuietly(output)
            IOUtils.closeQuietly(input)
        }
    }
}
