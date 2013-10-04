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

import org.libimobiledevice.ios.driver.binding.exceptions.SDKException;

import static org.libimobiledevice.ios.driver.binding.exceptions.SDKErrorCode.throwIfNeeded;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.sdk_idevice_syslog_service_t;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.syslog_service_new;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.syslog_service_start_capture;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.syslog_service_stop_capture;

public class SysLogService {

  private final sdk_idevice_syslog_service_t service;

  public SysLogService(IOSDevice d) throws SDKException {
    PointerByReference ptr = new PointerByReference();

    throwIfNeeded(syslog_service_new(d.getSDKHandle(), ptr));
    service = new sdk_idevice_syslog_service_t(ptr.getValue());

  }

  public void start(SyslogMessageHandler handler) throws SDKException {

    if (handler == null) {
      handler = new SyslogMessageHandler() {
        @Override
        protected void onCharacter(char c) {
          System.out.print(c);
        }
      };
    }
    throwIfNeeded(syslog_service_start_capture(service, handler, null));
  }

  public void stop() throws SDKException {
    throwIfNeeded(syslog_service_stop_capture(service));
  }


}
