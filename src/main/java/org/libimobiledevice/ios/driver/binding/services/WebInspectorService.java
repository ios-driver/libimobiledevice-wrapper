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

import com.sun.jna.ptr.PointerByReference;

import org.libimobiledevice.ios.driver.binding.exceptions.LibImobileException;
import org.libimobiledevice.ios.driver.binding.raw.PlistLibrary;

import java.nio.IntBuffer;

import static org.libimobiledevice.ios.driver.binding.exceptions.ErrorCode.throwIfNeeded;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceLibrary.webinspector_client_free;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceLibrary.webinspector_client_start_service;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceLibrary.webinspector_client_t;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceLibrary.webinspector_receive_with_timeout;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceLibrary.webinspector_send;

public class WebInspectorService {

  private final IOSDevice device;
  private webinspector_client_t webinspector_client;

  public WebInspectorService(IOSDevice device) {
    this.device = device;
  }

  public void startWebInspector() throws LibImobileException {
    PointerByReference ptr = new PointerByReference();
    throwIfNeeded(
        webinspector_client_start_service(device.getHandle(), ptr, "libimobile-java"));
    webinspector_client = new webinspector_client_t(ptr.getValue());
  }

  public String receiveMessage() throws LibImobileException {
    synchronized (this) {
      PointerByReference plist = new PointerByReference();
      webinspector_receive_with_timeout(webinspector_client, plist, 5000);
      PointerByReference plist_xml = new PointerByReference();
      IntBuffer buff = IntBuffer.allocate(1);
      PlistLibrary.plist_to_xml(plist.getValue(), plist_xml, buff);
      if (plist_xml != null && plist_xml.getValue() != null) {
        return plist_xml.getValue().getString(0);
      } else {
        return null;
      }
    }
  }

  public void sendMessage(String xml) throws LibImobileException {
    PointerByReference ptr = new PointerByReference();
    PlistLibrary.plist_from_xml(xml, xml.length(), ptr);
    throwIfNeeded(webinspector_send(webinspector_client, ptr.getValue()));
  }

  public void stopWebInspector() throws LibImobileException {
    throwIfNeeded(webinspector_client_free(webinspector_client));
  }
}

