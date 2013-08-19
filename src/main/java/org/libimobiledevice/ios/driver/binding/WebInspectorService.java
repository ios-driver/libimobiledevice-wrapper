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

public class WebInspectorService {

//  private webinspector_client_t web_client;
//  private final IOSDevice device;
//  private static final IMobileDeviceService lib = IMobileDeviceService.INSTANCE;
//  private static final int TIMEOUT_MS = 1000;
//  private volatile boolean busy = false;
//  private volatile boolean stopping = false;
//
//
//  public WebInspectorService(IOSDevice device) {
//    if (device == null) {
//      throw new LibIMobileException(
//          "WebInspector service requires an active connection to the device.");
//    }
//    device.ensureConnected();
//    this.device = device;
//  }
//
//  public void startWebInspector() {
//    System.out.println("starting inspector for " + device.getDeviceId());
//    synchronized (this) {
//      device.ensureConnected();
//      if (web_client != null) {
//        throw new LibIMobileException("WebInspector already started.");
//      }
//
//      PointerByReference ptr = new PointerByReference();
//      lib.webinspector_startService(device.getHandle(), ptr);
//      web_client = new webinspector_client_t(ptr);
//    }
//  }
//
//
//  public String receiveMessage() {
//    synchronized (this) {
//      device.ensureConnected();
//      ensureServiceStarted();
//      PointerByReference xml = new PointerByReference();
//      if (stopping) {
//        return null;
//      }
//      try {
//        busy = true;
//        throwIfNeeded(
//            lib.webinspector_receive_message(device.getHandle(), web_client, xml, TIMEOUT_MS));
//        Pointer p = xml.getValue();
//        if (p == null) {
//          return null;
//        } else {
//          return p.getString(0);
//        }
//      } catch (LibIMobileException e) {
//        // ignore. It's the receive timing out when nothing happens.
//        return null;
//      } finally {
//        busy = false;
//      }
//    }
//  }
//
//  public void sendMessage(String xml) {
//    synchronized (this) {
//      device.ensureConnected();
//      ensureServiceStarted();
//      busy = true;
//      if (stopping) {
//        throw new LibIMobileException("Cannot send messages while the service is shutting down.");
//      }
//      throwIfNeeded(lib.webinspector_send_message(device.getHandle(), web_client, xml));
//      busy = false;
//    }
//  }
//
//  void ensureServiceStarted() {
//    if (web_client == null) {
//      throw new LibIMobileException("WebInspector service not started.Cannot receive messages");
//    }
//  }
//
//  public void stopWebInspector() {
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
//  }
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
