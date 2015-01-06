/*
 * Copyright 2012-2013 eBay Software Foundation and ios-driver committers
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.libimobiledevice.ios.driver.binding.raw;

import com.sun.jna.NativeLibrary;
import com.sun.jna.Platform;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JNAInit {

  private static boolean initialize = false;
  private static File output;

  public static synchronized boolean init() {
    if (initialize) {
      return true;
    }

    // create the tmp dir where all the lib will be loaded from.
    // by default JNA created 1 different folder for each lib, which breaks libraries having a
    // @loader_path/libcrypto.1.0.0.dylib syntax. Creating this helper class to have everything in
    // one place, jna.tmpdir
    String home = System.getProperty("user.home");
    File jna = new File(home, "/.ios-driver/jna/darwin");
    jna.mkdirs();
    output =jna;

//    System.setProperty("jna.library.path", jna.getAbsolutePath());
//    System.out.println("jna.library.path=" + System.getProperty("jna.library.path"));

    // extract everything in it
    List<String> libs = new ArrayList<String>();
    if (Platform.isMac()) {
      libs.add("crypto.1.0.0");
      libs.add("iconv.2");
      libs.add("ssl.1.0.0");
      libs.add("z.1");
      libs.add("lzma.5");
    }

    if (Platform.isLinux() || Platform.isWindows()) {
      libs.add("crypto");
      libs.add("ssl");
    }

    libs.add("imobiledevice.4");
    libs.add("imobiledevice-sdk");
    libs.add("plist.2");
    libs.add("usbmuxd.2");
    libs.add("xml2.2");
    //libs.add("zip");

    for (String lib : libs) {
      unpack(lib, jna);
    }
    NativeLibrary.addSearchPath("imobiledevice-sdk", jna.getAbsolutePath());

    File dst = new File(jna, "idevicedebug");
    copy("darwin/idevicedebug", dst);
    dst.setExecutable(true);

    initialize = true;
    return true;
  }

  public static File getTemporaryJNAFolder(){
    return output;
  }


  private static void unpack(String lib, File out) {
    String libName = "unknown platform";
    String resource = "unknown platform";
    if (Platform.isMac()) {
      libName = "lib" + lib + ".dylib";
      resource = "darwin/" + libName;
    } else if (Platform.isWindows()) {
      libName = "lib" + lib + ".dll";
      if (Platform.is64Bit()) {
        resource = "win32-x86-64/" + libName;
      } else {
        resource = "win32-x86/" + libName;
      }
    } else if (Platform.isLinux()) {
      libName = "lib" + lib + ".so";
      if (Platform.is64Bit()) {
        resource = "linux-x86-64/" + libName;
      } else {
        resource = "linux-x86/" + libName;
      }
    } else {
      throw new RuntimeException("Unknown platform");
    }

    File dst = new File(out, libName);
    copy(resource, dst);


  }


  private static void copy(String resource, File dst) {
    InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
    if (in == null) {
      System.err.println("Cannot load " + resource);
    }

    try {
      FileOutputStream w = new FileOutputStream(dst);
      IOUtils.copy(in, w);
      IOUtils.closeQuietly(w);
      IOUtils.closeQuietly(in);
    } catch (IOException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }
}
