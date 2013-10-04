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
import org.libimobiledevice.ios.driver.binding.model.ApplicationInfo;

import java.io.File;
import java.util.List;

import static org.libimobiledevice.ios.driver.binding.exceptions.SDKErrorCode.throwIfNeeded;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.installation_service_free;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.installation_service_get_application_list_as_xml;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.installation_service_install_application_from_archive_with_callback;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.installation_service_new;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.installation_service_uninstall_application;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.sdk_idevice_installation_service_status_cb_t;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.sdk_idevice_installation_service_t;

public class InstallerService {

  private static final Object lock = new Object();
  private final sdk_idevice_installation_service_t service;

  public InstallerService(IOSDevice d) throws SDKException {
    synchronized (lock) {
      PointerByReference ptr = new PointerByReference();
      throwIfNeeded(installation_service_new(d.getSDKHandle(), ptr));
      service = new sdk_idevice_installation_service_t(ptr.getValue());
    }
  }

  public List<ApplicationInfo> listApplications() throws SDKException {
    PointerByReference ptr = new PointerByReference();
    throwIfNeeded(installation_service_get_application_list_as_xml(service, 1, ptr));
    String all = ptr.getValue().getString(0);
    return parse(all);
  }

  private List<ApplicationInfo> parse(String raw) {
    List<ApplicationInfo> infos = ApplicationInfo.extractApplications(raw);
    return infos;
  }

  public void install(File ipa, InstallCallback cb) throws SDKException {
    System.out.println("starting install");
    final sdk_idevice_installation_service_status_cb_t callback;

    if (cb == null) {
      System.err.println("install callback not specified.souting messages");
      cb = new InstallCallback() {
        @Override
        protected void onUpdate(String operation, int percent, String message) {
          System.out.println(operation + ", " + percent + "% , " + message);
        }
      };
    }
    throwIfNeeded(installation_service_install_application_from_archive_with_callback(service,
                                                                                      ipa.getAbsolutePath(),
                                                                                      cb,
                                                                                      null));
  }

  public void uninstall(String bundleId) throws SDKException {
    throwIfNeeded(installation_service_uninstall_application(service, bundleId));
  }

  public void free() throws SDKException {
    throwIfNeeded(installation_service_free(service));
  }

}
