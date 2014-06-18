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
import org.libimobiledevice.ios.driver.binding.model.ProvisioningProfileInfo;
import org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary;

import java.io.File;
import java.nio.IntBuffer;
import java.util.List;

import static org.libimobiledevice.ios.driver.binding.exceptions.SDKErrorCode.throwIfNeeded;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.provisioning_profile_info_from_filename_as_xml;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.provisioning_profile_service_free;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.provisioning_profile_service_get_profile_list;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.provisioning_profile_service_new;

public class ProvisioningService {

  private final ImobiledeviceSdkLibrary.sdk_idevice_provisioning_profile_service_t service;

  public ProvisioningService(IOSDevice d) throws SDKException {
    PointerByReference ptr = new PointerByReference();
    throwIfNeeded(provisioning_profile_service_new(d.getSDKHandle(), ptr));
    service =
        new ImobiledeviceSdkLibrary.sdk_idevice_provisioning_profile_service_t(ptr.getValue());
  }

  public void free() throws SDKException {
    throwIfNeeded(provisioning_profile_service_free(service));
  }


  public List<ProvisioningProfileInfo> getProfiles() throws Exception {
    PointerByReference ptr = new PointerByReference();
    IntBuffer count = IntBuffer.allocate(1);
    provisioning_profile_service_get_profile_list(service, ptr, count);
    int total = count.get();
    List<ProvisioningProfileInfo> profiles = ProvisioningProfileInfo.read(ptr, total);
    return profiles;
  }


  public static ProvisioningProfileInfo getProfile(File f) throws Exception {
    PointerByReference ptr = new PointerByReference();
    provisioning_profile_info_from_filename_as_xml(f.getAbsolutePath(), ptr);
    String raw = ptr.getValue().getString(0);
    return new ProvisioningProfileInfo(raw);
  }

  ;
}
