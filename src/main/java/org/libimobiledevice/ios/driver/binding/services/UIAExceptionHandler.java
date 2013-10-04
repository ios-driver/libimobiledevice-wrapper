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

package org.libimobiledevice.ios.driver.binding.services;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

import org.libimobiledevice.ios.driver.binding.raw.PlistLibrary;

import java.nio.IntBuffer;

import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceInstrumentsLibrary.uiautomation_exception_handler_t;

public abstract class UIAExceptionHandler implements uiautomation_exception_handler_t {


  @Override
  public int apply(Pointer client, Pointer message, int line, Pointer source) {
    PointerByReference xml_ptr = new PointerByReference();
    IntBuffer buff = IntBuffer.allocate(1);
    PlistLibrary.plist_to_xml(message, xml_ptr, buff);
    String msg = xml_ptr.getValue().getString(0);
    onException(msg);
    return 0;
  }

  public abstract void onException(String message);
}
