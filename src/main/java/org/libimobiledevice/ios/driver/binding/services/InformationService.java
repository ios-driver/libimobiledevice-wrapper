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

import com.dd.plist.NSNumber;
import com.dd.plist.XMLPropertyListParser;
import com.sun.jna.ptr.PointerByReference;

import org.libimobiledevice.ios.driver.binding.exceptions.SDKException;

import java.nio.IntBuffer;
import java.util.Date;

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
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.information_service_set_value_from_string;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.sdk_idevice_information_service_t;

public class InformationService {

  private final sdk_idevice_information_service_t sdk_idevice_information_service_t;
  private final IOSDevice device;

  public InformationService(IOSDevice device) throws SDKException {
    this.device = device;
    PointerByReference ptr = new PointerByReference();
    throwIfNeeded(information_service_new(device.getSDKHandle(), ptr));
    sdk_idevice_information_service_t = new sdk_idevice_information_service_t(ptr.getValue());
  }

  public boolean isDevModeEnabled() throws SDKException {
    IntBuffer enabled = IntBuffer.allocate(1);
    // TODO: freynaud when enabled = false, return code is -1 ?
    information_service_is_developer_mode_enabled(sdk_idevice_information_service_t, enabled);
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
    String current = getLanguage();
    if (language.equals(current)) {
      return;
    }
//    Date now = getDate();
//    IsRestartedListener listener = new IsRestartedListener(now);
//
//    device.getSysLogService().addListener(listener);
    throwIfNeeded(information_service_set_language(sdk_idevice_information_service_t, language));

//    long deadline = System.currentTimeMillis() + 20000;
//    while (!listener.isDone()) {
//      try {
//        Thread.sleep(500);
//      } catch (InterruptedException e) {
//        ignore.
//      }
//
//      if (System.currentTimeMillis() > deadline) {
//        System.out.println("didn't find a clue in " + listener.toString());
//      }
//    }
  }

  public boolean isPasswordProtected() throws SDKException {
    PointerByReference ptr = new PointerByReference();
    throwIfNeeded(information_service_get_value_as_xml(sdk_idevice_information_service_t, null,
                                                       "PasswordProtected", ptr));
    String raw = getValue(ptr);
    try {
      NSNumber n = (NSNumber) XMLPropertyListParser.parse(raw.getBytes("UTF-8"));
      return n.boolValue();
    } catch (Exception e) {
      throw new SDKException("Cannot parse returned xml " + raw);
    }
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

  public void setValue(String domain, String key, String value) throws SDKException {
    throwIfNeeded(
        information_service_set_value_from_string(sdk_idevice_information_service_t, domain, key,
                                                  value));
  }


  public String getValueAsXML(String domain, String key) throws SDKException {
    PointerByReference ptr = new PointerByReference();
    int
        code =
        information_service_get_value_as_xml(sdk_idevice_information_service_t, domain, key, ptr);
    if (code == 0) {
      return getValue(ptr);
      // seems to be the code for key not found.
    } else if (code == -1) {
      return null;
    } else {
      throw new SDKException(code);
    }
  }

  public Date getDate() throws SDKException {
    PointerByReference ptr = new PointerByReference();
    throwIfNeeded(
        information_service_get_value_as_xml(sdk_idevice_information_service_t, null,
                                             "TimeIntervalSince1970", ptr));
    String xml = getValue(ptr);
    try {
      NSNumber time = (NSNumber) XMLPropertyListParser.parse(xml.getBytes("UTF-8"));
      long ts = (long) (time.doubleValue() * 1000);
      return new Date(ts);
    } catch (Exception e) {
      throw new SDKException("Cannot parse response = " + xml + " ->" + e.getMessage());
    }

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

  public void free() throws SDKException {
    throwIfNeeded(information_service_free(sdk_idevice_information_service_t));
  }

  /**
   * look for the magic string after this given time (the device buffers the last logs, so if you
   * change the language plenty of time in a row, you risk getting the logs from the previous
   * changes
   */
  private static class IsRestartedListener implements SysLogListener {

    private final Date after;
    private volatile boolean isDone = false;

    IsRestartedListener(Date after) {
      this.after = after;
    }

    private boolean isDone() {
      return isDone;
    }

    // TODO ios7 no SIM : Oct 15 10:52:23 iPhone profiled[862] <Notice>: (Note ) profiled: Service stopping.
    // ios7 wityh sim federicos-iPhone profiled[2961] <Notice>: (Note ) profiled: Service stopping.
    @Override
    public void onLog(SysLogLine line) {
      if (line.getMessage().contains("ALS: SetDisplayFactor: factor=1.0000") &&
          line.getDate().after(after)) {
        isDone = true;
      }
    }
  }


}
