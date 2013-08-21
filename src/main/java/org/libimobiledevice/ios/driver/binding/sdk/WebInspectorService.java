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

package org.libimobiledevice.ios.driver.binding.sdk;

import com.sun.jna.ptr.PointerByReference;

import org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceLibrary;
import org.libimobiledevice.ios.driver.binding.raw.PlistLibrary;

import java.nio.IntBuffer;

public class WebInspectorService {

  private ImobiledeviceLibrary.webinspector_client_t webinspector_client;
  private final IDeviceSDK device;


  //  private https://dl.dropboxusercontent.com/u/24687868/bugs/issue1previousscreenmanualclick.png web_client;
//  private final IOSDevice device;
//  private static final IMobileDeviceService lib = IMobileDeviceService.INSTANCE;
//  private static final int TIMEOUT_MS = 1000;
//  private volatile boolean busy = false;
//  private volatile boolean stopping = false;
//
//
  public WebInspectorService(IDeviceSDK device) {
    this.device = device;
  }

  //
  public void startWebInspector() {
    PointerByReference ptr = new PointerByReference();
    ImobiledeviceLibrary
        .webinspector_client_start_service(device.getHandleIdevice(), ptr, "libimobile-java");
    webinspector_client = new ImobiledeviceLibrary.webinspector_client_t(ptr.getValue());
  }

  //
//
  public String receiveMessage() {
    synchronized (this) {
      PointerByReference plist = new PointerByReference();
//      if (stopping) {
//        return null;
//      }
//      try {
//        busy = true;
//        throwIfNeeded(

      ImobiledeviceLibrary.webinspector_receive_with_timeout(webinspector_client, plist, 5000);

      PointerByReference plist_xml = new PointerByReference();
      IntBuffer buff = IntBuffer.allocate(1);
      PlistLibrary.plist_to_xml(plist.getValue(), plist_xml, buff);
      if (plist_xml != null && plist_xml.getValue() != null) {
        return plist_xml.getValue().getString(0);
      } else {
        return null;
      }
//      } catch (LibIMobileException e) {
//        // ignore. It's the receive timing out when nothing happens.
//        return null;
//      } finally {
//        busy = false;
//      }
//    }

//      return null;
    }
  }

  public void sendMessage(String xml) {
    synchronized (this) {
//      device.ensureConnected();
//      ensureServiceStarted();
//      busy = true;
//      if (stopping) {
//        throw new LibIMobileException("Cannot send messages while the service is shutting down.");
//      }
//      throwIfNeeded(lib.webinspector_send_message(device.getHandle(), web_client, xml));
      PointerByReference ptr = new PointerByReference();
      PlistLibrary.plist_from_xml(xml, xml.length(), ptr);
      ImobiledeviceLibrary.webinspector_send(webinspector_client, ptr.getValue());
//      busy = false;
    }
  }

  //
//  void ensureServiceStarted() {
//    if (web_client == null) {
//      throw new LibIMobileException("WebInspector service not started.Cannot receive messages");
//    }
//  }
//
  public void stopWebInspector() {
    ImobiledeviceLibrary.webinspector_client_free(webinspector_client);
//    synchronized (this) {
//      if (web_client != null) {
//        stopping = true;
//        while (busy) {
//          sleep(250);
//        }
//        try {
//          throwIfNeeded(lib.webinspector_stopService(web_client));
//        } finally {
//          web_client = null;
//          stopping = false;
//
//        }
//      }
//    }

  }
//
//  private void sleep(long ms) {
//    try {
//      Thread.sleep(ms);
//    } catch (InterruptedException e) {
//      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//    }
//
//  }
}
