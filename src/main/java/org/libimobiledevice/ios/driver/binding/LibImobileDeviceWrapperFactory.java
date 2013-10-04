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

package org.libimobiledevice.ios.driver.binding;

import com.sun.jna.NativeLibrary;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LibImobileDeviceWrapperFactory {


  public static LibImobileDeviceWrapperFactory INSTANCE = new LibImobileDeviceWrapperFactory();

  static {
    File f = new File(System.getProperty("user.home"));
    File out = new File(f, "libimobiledevice_jna/darwin");
    //out.delete();
    out.mkdirs();
    //out.deleteOnExit();

    unpack("libimobiledevice-sdk.dylib", out);
    unpack("libimobiledevice-instruments.dylib", out);
    unpack("libcrypto.1.0.0.dylib", out);
    unpack("libimobiledevice.dylib", out);
    unpack("libplist.dylib", out);
    unpack("libssl.1.0.0.dylib", out);
    unpack("libusbmuxd.dylib", out);
    unpack("libz.1.dylib", out);
    unpack("libxml2.dylib", out);
//    unpack("liblzma.5.dylib", out);
//    unpack("libiconv.2.dylib", out);
    System.out.println("all unpack in " + out.getAbsolutePath());

    NativeLibrary.addSearchPath("imobiledevice", out.getAbsolutePath());
    NativeLibrary.addSearchPath("imobiledevice-sdk", out.getAbsolutePath());

  }

  private LibImobileDeviceWrapperFactory() {

  }

  private static void unpack(String lib, File out) {
    InputStream
        in =
        Thread.currentThread().getContextClassLoader().getResourceAsStream("darwin/" + lib);
    if (in == null) {
      System.err.println("Cannot load " + lib);
    }

    File o = new File(out, lib);
    try {
      FileOutputStream w = new FileOutputStream(o);
      IOUtils.copy(in, w);
      IOUtils.closeQuietly(w);
      IOUtils.closeQuietly(in);
    } catch (IOException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }

  }
}
