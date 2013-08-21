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

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

import org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary;

public class SysLogService {

  private final ImobiledeviceSdkLibrary.sdk_idevice_syslog_service_t service;
  private final ImobiledeviceSdkLibrary.sdk_idevice_syslog_service_read_cb_t callback =
      new ImobiledeviceSdkLibrary.sdk_idevice_syslog_service_read_cb_t() {
        @Override
        public void apply(byte c, Pointer user_data) {
          System.out.print((char) c);
        }
      };

  public SysLogService(IDeviceSDK d) {
    PointerByReference ptr = new PointerByReference();
    ImobiledeviceSdkLibrary.syslog_service_new(d.getHandle(), ptr);
    service = new ImobiledeviceSdkLibrary.sdk_idevice_syslog_service_t(ptr.getValue());

  }

  public void start() {
    ImobiledeviceSdkLibrary.syslog_service_start_capture(service, callback, null);
  }

  public void stop() {
    ImobiledeviceSdkLibrary.syslog_service_stop_capture(service);
  }


}
