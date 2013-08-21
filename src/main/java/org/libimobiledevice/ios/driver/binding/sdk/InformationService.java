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

import org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary;

import java.nio.IntBuffer;

public class InformationService {

  private final ImobiledeviceSdkLibrary.sdk_idevice_information_service_t
      sdk_idevice_information_service_t;


  public InformationService(IDeviceSDK deviceSDK) {
    PointerByReference ptr = new PointerByReference();
    ImobiledeviceSdkLibrary.information_service_new(deviceSDK.getHandle(), ptr);
    sdk_idevice_information_service_t =
        new ImobiledeviceSdkLibrary.sdk_idevice_information_service_t(ptr.getValue());
  }


  public boolean isDevModeEnabled() {
    IntBuffer enabled = IntBuffer.allocate(1);
    ImobiledeviceSdkLibrary
        .information_service_is_developer_mode_enabled(sdk_idevice_information_service_t, enabled);
    if (enabled.get(0) == 0) {
      return false;
    }
    return true;
  }


  public String getDeviceName() {
    PointerByReference ptr = new PointerByReference();
    ImobiledeviceSdkLibrary
        .information_service_get_device_name(sdk_idevice_information_service_t, ptr);
    return ptr.getValue().getString(0);
  }


  public String getDeviceType() {
    PointerByReference ptr = new PointerByReference();
    ImobiledeviceSdkLibrary
        .information_service_get_device_type(sdk_idevice_information_service_t, ptr);
    return ptr.getValue().getString(0);
  }

  public String getLanguage() {
    PointerByReference ptr = new PointerByReference();
    ImobiledeviceSdkLibrary
        .information_service_get_language(sdk_idevice_information_service_t, ptr);
    return ptr.getValue().getString(0);
  }


  public String getLocale() {
    PointerByReference ptr = new PointerByReference();
    ImobiledeviceSdkLibrary.information_service_get_locale(sdk_idevice_information_service_t, ptr);
    return ptr.getValue().getString(0);
  }

  public String getProductVersion() {
    PointerByReference ptr = new PointerByReference();
    ImobiledeviceSdkLibrary.information_service_get_product_version(
        sdk_idevice_information_service_t, ptr);
    return ptr.getValue().getString(0);
  }

  public String getValueAsXML(String domain, String key) {
    PointerByReference ptr = new PointerByReference();
    ImobiledeviceSdkLibrary
        .information_service_get_value_as_xml(sdk_idevice_information_service_t, domain, key, ptr);
    return ptr.getValue().getString(0);
  }

  public void setDeviceName(String name) {
    ImobiledeviceSdkLibrary
        .information_service_set_device_name(sdk_idevice_information_service_t, name);
  }

  public void setLanguage(String language) {
    ImobiledeviceSdkLibrary
        .information_service_set_language(sdk_idevice_information_service_t, language);
  }

  public void setLocale(String locale) {
    ImobiledeviceSdkLibrary
        .information_service_set_locale(sdk_idevice_information_service_t, locale);
  }

  public void release() {
    ImobiledeviceSdkLibrary.information_service_free(sdk_idevice_information_service_t);
  }

  @Override
  public void finalize() {
    release();
  }
}
