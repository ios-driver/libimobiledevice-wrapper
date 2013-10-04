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

import java.nio.IntBuffer;

import static org.libimobiledevice.ios.driver.binding.exceptions.SDKErrorCode.throwIfNeeded;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.information_service_free;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.information_service_get_device_name;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.information_service_get_device_type;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.information_service_get_language;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.information_service_get_locale;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.information_service_get_product_version;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.information_service_get_value_as_xml;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.information_service_is_developer_mode_enabled;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.information_service_new;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.information_service_set_device_name;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.information_service_set_language;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.information_service_set_locale;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.sdk_idevice_information_service_t;

public class InformationService {

  private final sdk_idevice_information_service_t sdk_idevice_information_service_t;

  public InformationService(IOSDevice deviceSDK) throws SDKException {
    PointerByReference ptr = new PointerByReference();
    throwIfNeeded(information_service_new(deviceSDK.getSDKHandle(), ptr));
    sdk_idevice_information_service_t = new sdk_idevice_information_service_t(ptr.getValue());
  }

  public boolean isDevModeEnabled() throws SDKException {
    IntBuffer enabled = IntBuffer.allocate(1);
    throwIfNeeded(
        information_service_is_developer_mode_enabled(sdk_idevice_information_service_t, enabled));
    if (enabled.get(0) == 0) {
      return false;
    }
    return true;
  }

  public String getDeviceName() throws SDKException {
    PointerByReference ptr = new PointerByReference();
    throwIfNeeded(information_service_get_device_name(sdk_idevice_information_service_t, ptr));
    return getValue(ptr);
  }

  public void setDeviceName(String name) throws SDKException {
    throwIfNeeded(information_service_set_device_name(sdk_idevice_information_service_t, name));
  }

  public String getDeviceType() throws SDKException {
    PointerByReference ptr = new PointerByReference();
    throwIfNeeded(information_service_get_device_type(sdk_idevice_information_service_t, ptr));
    return getValue(ptr);
  }

  public String getLanguage() throws SDKException {
    PointerByReference ptr = new PointerByReference();
    throwIfNeeded(information_service_get_language(sdk_idevice_information_service_t, ptr));
    return getValue(ptr);
  }

  public void setLanguage(String language) throws SDKException {
    throwIfNeeded(information_service_set_language(sdk_idevice_information_service_t, language));
  }

  public String getLocale() throws SDKException {
    PointerByReference ptr = new PointerByReference();
    throwIfNeeded(information_service_get_locale(sdk_idevice_information_service_t, ptr));
    return getValue(ptr);
  }

  public void setLocale(String locale) throws SDKException {
    throwIfNeeded(information_service_set_locale(sdk_idevice_information_service_t, locale));
  }

  public String getProductVersion() throws SDKException {
    PointerByReference ptr = new PointerByReference();
    throwIfNeeded(information_service_get_product_version(sdk_idevice_information_service_t, ptr));
    return getValue(ptr);
  }

  public String getValueAsXML(String domain, String key) throws SDKException {
    PointerByReference ptr = new PointerByReference();
    throwIfNeeded(
        information_service_get_value_as_xml(sdk_idevice_information_service_t, domain, key, ptr));
    return getValue(ptr);
  }

  private String getValue(PointerByReference ptr) throws SDKException {
    if (ptr == null) {
      throw new SDKException("Bug ? pointer should have been assigned by the info_service");
    } else if (ptr.getValue() == null) {
      throw new SDKException("Didn't get a value back. Something wrong in info_service");
    } else {
      return ptr.getValue().getString(0);
    }

  }

  public void release() throws SDKException {
    throwIfNeeded(information_service_free(sdk_idevice_information_service_t));
  }

}
