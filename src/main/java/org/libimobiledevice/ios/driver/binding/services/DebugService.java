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

import java.nio.IntBuffer;

import static org.libimobiledevice.ios.driver.binding.exceptions.SDKErrorCode.throwIfNeeded;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.debug_service_free;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.debug_service_launch_application_by_bundle_identifier;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.debug_service_new;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.sdk_idevice_debug_service_t;

public class DebugService {

  private final sdk_idevice_debug_service_t service;

  public DebugService(IOSDevice d) throws SDKException {
    PointerByReference ptr = new PointerByReference();
    throwIfNeeded(debug_service_new(d.getSDKHandle(), ptr));
    service = new sdk_idevice_debug_service_t(ptr.getValue());
  }

  public void free() throws SDKException {
    throwIfNeeded(debug_service_free(service));
  }

  public int launch(String bundleId) throws SDKException {
    PointerByReference envt = new PointerByReference();
    PointerByReference args = new PointerByReference();
    IntBuffer pidptr = IntBuffer.allocate(1);
    int res =
        debug_service_launch_application_by_bundle_identifier(service, bundleId, envt, args,
                                                              pidptr);
    int pid = pidptr.get(0);
    return pid;
  }
}


