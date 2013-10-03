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

import org.libimobiledevice.ios.driver.binding.ApplicationInfo;
import org.libimobiledevice.ios.driver.binding.InstallerCallback;
import org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary;

import java.io.File;
import java.util.List;

public class InstallerService {

  private static final Object lock = new Object();
  private final ImobiledeviceSdkLibrary.sdk_idevice_installation_service_t service;

  public InstallerService(IDeviceSDK d) {
    synchronized (lock) {
      System.out.println("creating service");
      PointerByReference ptr = new PointerByReference();
      ImobiledeviceSdkLibrary.installation_service_new(d.getHandle(), ptr);
      service = new ImobiledeviceSdkLibrary.sdk_idevice_installation_service_t(ptr.getValue());
      System.out.println("service created");
    }
  }

  public List<ApplicationInfo> listApplications() {
    PointerByReference ptr = new PointerByReference();
    ImobiledeviceSdkLibrary
        .installation_service_get_application_list_as_xml(service, 1, ptr);
    String all = ptr.getValue().getString(0);
    return parse(all);
  }

  private List<ApplicationInfo> parse(String raw) {
    List<ApplicationInfo> infos = ApplicationInfo.extractApplications(raw);
    return infos;
  }

  public void install(File ipa, final InstallerCallback cb) {
    System.out.println("starting install");
    final ImobiledeviceSdkLibrary.sdk_idevice_installation_service_status_cb_t callback;

    if (cb == null) {
      System.out.println("callback was null. Using default.");
      callback = new ImobiledeviceSdkLibrary.sdk_idevice_installation_service_status_cb_t() {
        @Override
        public void apply(Pointer operation, Pointer message, int precent_complete,
                          Pointer user_data) {
          System.out.println(operation.getString(0) + precent_complete + "%");
          System.out.println(message.getString(0));
        }
      };
    } else {
      callback = new ImobiledeviceSdkLibrary.sdk_idevice_installation_service_status_cb_t() {

        @Override
        public void apply(Pointer operation, Pointer message, int precent_complete,
                          Pointer user_data) {
          try {
            String op = operation.getString(0);
            int percent = precent_complete;
            String msg = message.getString(0);
            cb.onMessage(op, percent, msg);
          } catch (Exception e) {
            System.err.println("CB ERROR " + e.getMessage());
          }
        }
      };
    }
    ImobiledeviceSdkLibrary
        .installation_service_install_application_from_archive_with_callback(service,
                                                                             ipa.getAbsolutePath(),
                                                                             callback, null);
  }

  public void uninstall(String bundleId) {
    ImobiledeviceSdkLibrary.installation_service_uninstall_application(service, bundleId);
  }
}
