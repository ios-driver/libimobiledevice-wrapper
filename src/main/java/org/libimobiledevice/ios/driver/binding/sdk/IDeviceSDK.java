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

import org.libimobiledevice.ios.driver.binding.exceptions.LibImobileException;
import org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceInstrumentsLibrary;
import org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceLibrary;
import org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary;

import java.nio.IntBuffer;

public class IDeviceSDK extends IDevice {

  private final ImobiledeviceSdkLibrary.sdk_idevice_t sdk_handle;
  private
  ImobiledeviceSdkLibrary.sdk_idevice_information_service_t
      sdk_idevice_information_service_t;

  private IDeviceSDK() throws IllegalAccessException {
    throw new IllegalAccessException();
  }

  public IDeviceSDK(String uuid) throws LibImobileException {
    super(uuid);
    PointerByReference sdk = new PointerByReference();
    ImobiledeviceSdkLibrary.sdk_idevice_new_from_idevice(sdk, handle.getPointer());
    sdk_handle = new ImobiledeviceSdkLibrary.sdk_idevice_t(sdk.getValue());
  }

  ImobiledeviceSdkLibrary.sdk_idevice_t getHandle() {
    return sdk_handle;
  }

  ImobiledeviceInstrumentsLibrary.idevice_t getHandleIdevice() {
    return handle;
  }


  public boolean isDeveloperMode() {
    IntBuffer enabled = IntBuffer.allocate(1);
    ImobiledeviceSdkLibrary
        .information_service_is_developer_mode_enabled(getInfoService(), enabled);
    if (enabled.get(0) == 0) {
      return false;
    }
    return true;
  }

  private synchronized ImobiledeviceSdkLibrary.sdk_idevice_information_service_t getInfoService() {
    if (sdk_idevice_information_service_t == null) {
      PointerByReference ptr = new PointerByReference();
      ImobiledeviceSdkLibrary.information_service_new(sdk_handle, ptr);
      sdk_idevice_information_service_t =
          new ImobiledeviceSdkLibrary.sdk_idevice_information_service_t(ptr.getValue());
    }
    return sdk_idevice_information_service_t;
  }

  public void release() {
    ImobiledeviceSdkLibrary.sdk_idevice_free(sdk_handle);
  }


}
