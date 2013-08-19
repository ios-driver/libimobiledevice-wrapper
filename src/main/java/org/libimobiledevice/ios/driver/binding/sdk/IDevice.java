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

public class IDevice {

  protected final ImobiledeviceInstrumentsLibrary.idevice_t handle;

  protected IDevice() throws IllegalAccessException {
    throw new IllegalAccessException();
  }

  public IDevice(String uuid) throws LibImobileException {
    PointerByReference ptr = new PointerByReference();
    LibImobileException.throwIfNeeded(ImobiledeviceLibrary.idevice_new(ptr, uuid));
    handle = new ImobiledeviceInstrumentsLibrary.idevice_t(ptr.getValue());
  }

  public void release() throws LibImobileException {
    LibImobileException.throwIfNeeded(ImobiledeviceLibrary.idevice_free(handle));

  }
}
