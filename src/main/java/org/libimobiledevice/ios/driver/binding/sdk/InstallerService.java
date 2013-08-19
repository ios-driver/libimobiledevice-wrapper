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

import java.io.File;
import java.nio.IntBuffer;
import java.util.List;

public class InstallerService {

  private final ImobiledeviceSdkLibrary.sdk_idevice_installation_service_t service;

  public InstallerService(IDeviceSDK d) {
    PointerByReference ptr = new PointerByReference();
    ImobiledeviceSdkLibrary.installation_service_new(d.getHandle(), ptr);
    service = new ImobiledeviceSdkLibrary.sdk_idevice_installation_service_t(ptr.getValue());
  }


  public List<Object> listApplications() {
    PointerByReference ptr = new PointerByReference();
    ImobiledeviceSdkLibrary
        .installation_service_get_application_list_as_xml(service, 1, ptr);
    String all = ptr.getValue().getString(0);
   return null;
  }

  public void install(File ipa) {
    ImobiledeviceSdkLibrary.sdk_idevice_installation_service_status_cb_t callback =
        new ImobiledeviceSdkLibrary.sdk_idevice_installation_service_status_cb_t() {
          @Override
          public void apply(Pointer operation, Pointer message, int precent_complete,
                            Pointer user_data) {
            System.out.println(operation.getString(0) + precent_complete+"%");
            System.out.println(message.getString(0));
          }
        };
    ImobiledeviceSdkLibrary.installation_service_install_application_from_archive_with_callback(service,ipa.getAbsolutePath(),callback,null);
  }
}
