/*
 * Copyright 2012-2014 eBay Software Foundation and ios-driver committers
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

import java.io.File;

import static org.libimobiledevice.ios.driver.binding.exceptions.SDKErrorCode.throwIfNeeded;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.image_mounting_service_free;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.image_mounting_service_mount_image;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.image_mounting_service_new;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.sdk_idevice_image_mounting_service_t;

public class ImageMountingService {

  private final sdk_idevice_image_mounting_service_t service;

  public ImageMountingService(IOSDevice d) throws SDKException {
    PointerByReference ptr = new PointerByReference();
    throwIfNeeded(image_mounting_service_new(d.getSDKHandle(), ptr));
    service = new sdk_idevice_image_mounting_service_t(ptr.getValue());
  }

  public void free() throws SDKException {
    throwIfNeeded(image_mounting_service_free(service));
  }

  public void mount(File dmg) throws SDKException {
    if (dmg.exists()) {
      throwIfNeeded(
          image_mounting_service_mount_image(service, dmg.getAbsolutePath(), null));
    } else {
      throw new SDKException(dmg + " doesn't exists.");
    }
  }
}
