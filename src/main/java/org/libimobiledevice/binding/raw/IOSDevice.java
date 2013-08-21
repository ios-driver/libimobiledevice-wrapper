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

package org.libimobiledevice.binding.raw;

import com.sun.jna.ptr.PointerByReference;

import org.libimobiledevice.binding.raw.exceptions.LibImobileException;
import org.libimobiledevice.binding.raw.exceptions.NonDevDeviceException;
import org.libimobiledevice.ios.driver.binding.ImobiledeviceInstrumentsLibrary;
import org.libimobiledevice.ios.driver.binding.ImobiledeviceLibrary;
import org.libimobiledevice.ios.driver.binding.ImobiledeviceSdkLibrary;
import org.libimobiledevice.ios.driver.sdk.IDevice;

import java.nio.IntBuffer;


public class IOSDevice {

  private final String uuid;
  private final ImobiledeviceInstrumentsLibrary.idevice_t handle;
  private final ImobiledeviceSdkLibrary.sdk_idevice_t sdk_handle;
  private final IDevice idevice;
  private
  ImobiledeviceSdkLibrary.sdk_idevice_information_service_t
      sdk_idevice_information_service_t;

  IOSDevice(String uuid) throws LibImobileException {
    if (uuid == null) {
      throw new IllegalArgumentException("uuid cannot be null");
    }

    idevice = new IDevice(uuid);
    this.uuid = uuid;
    PointerByReference ptr = new PointerByReference();
    ImobiledeviceLibrary.idevice_new(ptr, uuid);
    handle = new ImobiledeviceInstrumentsLibrary.idevice_t(ptr.getValue());

    PointerByReference sdk = new PointerByReference();
    ImobiledeviceSdkLibrary.sdk_idevice_new_from_idevice(sdk, handle.getPointer());
    sdk_handle = new ImobiledeviceSdkLibrary.sdk_idevice_t(sdk.getValue());

    if (!isDeveloperMode()) {
      throw new NonDevDeviceException();
    }
  }

  public ImobiledeviceInstrumentsLibrary.idevice_t getIDeviceTHandle() {
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

  private ImobiledeviceSdkLibrary.sdk_idevice_t getSDKHandle() {
    PointerByReference ptr = new PointerByReference();
    ImobiledeviceSdkLibrary.sdk_idevice_new_from_idevice(ptr, handle.getPointer());
    return new ImobiledeviceSdkLibrary.sdk_idevice_t(ptr.getValue());
  }


  private synchronized ImobiledeviceSdkLibrary.sdk_idevice_information_service_t getInfoService() {
    if (sdk_idevice_information_service_t == null) {
      PointerByReference ptr = new PointerByReference();
      ImobiledeviceSdkLibrary.information_service_new(getSDKHandle(), ptr);
      sdk_idevice_information_service_t =
          new ImobiledeviceSdkLibrary.sdk_idevice_information_service_t(ptr.getValue());
    }
    return sdk_idevice_information_service_t;
  }

  public void setLanguage(String language) {
    PointerByReference ptr = new PointerByReference();
    ImobiledeviceSdkLibrary.information_service_new(getSDKHandle(), ptr);

    ImobiledeviceSdkLibrary.sdk_idevice_information_service_t
        service =
        new ImobiledeviceSdkLibrary.sdk_idevice_information_service_t(ptr.getValue());
    ImobiledeviceSdkLibrary.information_service_set_language(service, language);
  }

  //
//  public IDevice_t getHandle() {
//    return device;
//  }
//


  @Override
  protected void finalize() {
    System.out.println("release resources");
    disconnect();
  }


  //
//  public String getDeviceId() {
//    return uuid;
//  }
//
//  public void install(File ipa) {
//    new Installer(this).install(ipa);
//  }
//
//  public void archive(String bundleId) {
//    //lib.archive(device, this.uuid, bundleId, new InstallerCallback());
//  }
//
//  public void uninstall(String bundleId) {
//    installer_t option = new installer_t();
//    option.uninstall_mode = 1;
//    option.uuid = uuid;
//    option.appid = bundleId;
//    //lib.install(device, option, new InstallerCallback());
//  }
//
//
//  public String listApplication(String type) {
//    PointerByReference xml = new PointerByReference();
//    int res = lib.list_applications(device, type, xml);
//    return xml.getValue().getString(0);
//  }
//
//  public ApplicationInfo getApplication(String bundleId) {
//    for (ApplicationInfo info : getApplications()) {
//      //System.out.println("App :" + info);
//      if (info.getApplicationId().equals(bundleId)) {
//        return info;
//      }
//    }
//    return null;
//  }
//
//  public void emptyApplicationCache(String bundleId) {
//    //throwIfNeeded(lib.emptyApplicationCache(device,bundleId));
//    lib.emptyApplicationCache(device, bundleId);
//  }
//
//  public String listArchives() {
//    PointerByReference xml = new PointerByReference();
//    int res = lib.list_archives(device, xml);
//    return xml.getValue().getString(0);
//  }
//
//  public void setLockDownValue(String domain, String key, String value) {
//    //lockdownd_client_t lockdownd = lockdownd();
//    IMobileDeviceService.StatusCode
//        .throwIfNeeded(lib.setLockDownValue(device, domain, key, value));
//  }
//
//  // TODO return a NSObject for dd.plist ?
//  public synchronized String getLockDownValue(String domain, String key) {
//    PointerByReference result = new PointerByReference();
//    IMobileDeviceService.StatusCode
//        .throwIfNeeded(lib.getLockDownValue(device, domain, key, result));
//    return result.getValue().getString(0);
//  }
//
  public void disconnect() {
    /*if (handle == null) {
      return;
    } else {
      ImobiledeviceSdkLibrary.sdk_idevice_free(sdk_handle);
    }*/
  }

  //
  public boolean isConnected() {
    return handle != null;
  }
//
//
//  public List<ApplicationInfo> getApplications() {
//    String xml = listApplication("list_all");
//    return ApplicationInfo.extractApplications(xml);
//  }
//
//
//
//  public WebInspectorService getWebInspectorService(){
//    if (webInspectorService ==null){
//      webInspectorService = new WebInspectorService(this);
//    }
//    return webInspectorService;
//  }
}
