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

package org.libimobiledevice.ios.driver.binding.exceptions;

public enum ErrorCode {
  LOCKDOWN_E_DICT_ERROR(-6),
  WEBINSPECTOR_E_UNKNOWN_ERROR(-256),
  WEBINSPECTOR_E_SSL_ERROR(-4),
  LOCKDOWN_E_SUCCESS(0),
  IDEVICE_E_UNKNOWN_ERROR(-2),
  LOCKDOWN_E_INVALID_ARG(-1),
  LOCKDOWN_E_UNKNOWN_ERROR(-256),
  LOCKDOWN_E_START_SERVICE_FAILED(-7),
  LOCKDOWN_E_INVALID_HOST_ID(-16),
  LOCKDOWN_E_GET_VALUE_PROHIBITED(-10),
  LOCKDOWN_E_NOT_ENOUGH_DATA(-8),
  LOCKDOWN_E_INVALID_ACTIVATION_RECORD(-18),
  WEBINSPECTOR_E_SUCCESS(0),
  IDEVICE_E_NO_DEVICE(-3),
  LOCKDOWN_E_PAIRING_FAILED(-4),
  LOCKDOWN_E_NO_RUNNING_SESSION(-15),
  WEBINSPECTOR_E_PLIST_ERROR(-2),
  IDEVICE_E_NOT_ENOUGH_DATA(-4),
  WEBINSPECTOR_E_MUX_ERROR(-3),
  WEBINSPECTOR_E_INVALID_ARG(-1),
  LOCKDOWN_E_REMOVE_VALUE_PROHIBITED(-11),
  LOCKDOWN_E_ACTIVATION_FAILED(-13),
  LOCKDOWN_E_SSL_ERROR(-5),
  LOCKDOWN_E_INVALID_SERVICE(-17),
  LOCKDOWN_E_SET_VALUE_PROHIBITED(-9),
  IDEVICE_E_SUCCESS(0),
  LOCKDOWN_E_INVALID_CONF(-2),
  LOCKDOWN_E_PASSWORD_PROTECTED(-14),
  LOCKDOWN_E_MUX_ERROR(-12),
  IDEVICE_E_SSL_ERROR(-6),
  LOCKDOWN_E_PLIST_ERROR(-3),
  IDEVICE_E_INVALID_ARG(-1),
  IDEVICE_E_BAD_HEADER(-5),
  JAVA_ERROR(-255);

  private final int code;

  private ErrorCode(int code) {
    this.code = code;
  }

  public static void throwIfNeeded(int code) throws LibImobileException {
    if (code == 0) {
      return;
    } else {
      throw new LibImobileException(code);
    }
  }

  public int getCode() {
    return code;
  }
}
