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

import org.libimobiledevice.ios.driver.binding.exceptions.SDKErrorCode;
import org.libimobiledevice.ios.driver.binding.exceptions.SDKException;

import static org.libimobiledevice.ios.driver.binding.exceptions.SDKErrorCode.throwIfNeeded;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.sdk_idevice_webinspector_service_t;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.webinspector_service_free;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.webinspector_service_new;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.webinspector_service_receive_message_with_timeout;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.webinspector_service_send_message;


public class WebInspectorService {

  private final IOSDevice device;
  private sdk_idevice_webinspector_service_t service;
  private volatile boolean listening;
  private volatile boolean stopping;
  private final Object listeningLock = new Object();
  private final Object stoppingLock = new Object();

  public WebInspectorService(IOSDevice device) {
    this.device = device;
  }

  public void startWebInspector() throws SDKException {
    PointerByReference ptr = new PointerByReference();
    SDKErrorCode.throwIfNeeded(webinspector_service_new(device.getSDKHandle(), ptr));
    service = new sdk_idevice_webinspector_service_t(ptr.getValue());
    stopping = false;
    listening = false;
  }

  public String receiveMessage() throws SDKException {

    synchronized (stoppingLock) {
      if (stopping) {
        throw new RuntimeException("inspector stopping.");
      }
    }
    synchronized (listeningLock) {
      listening = true;
      try {
        listening = true;
        PointerByReference xml = new PointerByReference();
        webinspector_service_receive_message_with_timeout(service, xml, 5000);
        return (xml == null || xml.getValue() == null) ? null : xml.getValue().getString(0);
      } finally {
        listening = false;
      }
    }

  }

  public void sendMessage(String xml) throws SDKException {
    throwIfNeeded(webinspector_service_send_message(service, xml));
  }

  public void stopWebInspector() throws SDKException {
    synchronized (stoppingLock) {
      stopping = true;
    }
    synchronized (listeningLock) {
      while (listening) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }

    if (service != null) {
      throwIfNeeded(webinspector_service_free(service));
    }
  }
}

