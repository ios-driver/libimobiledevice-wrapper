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
import org.libimobiledevice.ios.driver.binding.exceptions.SDKException;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceLibrary.*;
import org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary;

import java.util.logging.Logger;

import static org.libimobiledevice.ios.driver.binding.exceptions.ErrorCode.throwIfNeeded;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceLibrary.idevice_new;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.sdk_idevice_free;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.sdk_idevice_new_from_idevice;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.sdk_idevice_t;


public class IOSDevice {

  private static final Logger log = Logger.getLogger(IOSDevice.class.getName());
  private final String uuid;
  private final idevice_t  handle;
  private final sdk_idevice_t sdk_handle;
  private SysLogService sysLogService;


  IOSDevice(String uuid) throws LibImobileException, SDKException {
    if (uuid == null) {
      throw new IllegalArgumentException("uuid cannot be null");
    }
    this.uuid = uuid;

    PointerByReference ptr = new PointerByReference();
    throwIfNeeded(idevice_new(ptr, uuid));
    handle = new idevice_t(ptr.getValue());

    PointerByReference sdk = new PointerByReference();
    sdk_idevice_new_from_idevice(sdk, handle.getPointer());
    sdk_handle = new ImobiledeviceSdkLibrary.sdk_idevice_t(sdk.getValue());

  }

  idevice_t getHandle(){
    return handle;
  }

  sdk_idevice_t getSDKHandle(){
    return sdk_handle;
  }

  public void free() {
    sdk_idevice_free(sdk_handle);
  }


  public String getUUID() {
    return uuid;
  }

  public SysLogService getSysLogService() throws SDKException {
    if (sysLogService == null){
      sysLogService = new SysLogService(this);
    }
    return sysLogService;
  }
}




