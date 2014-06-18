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

public enum SDKErrorCode {

  SDK_E_SUCCESS(0),
  SDK_E_ILLEGAL_ARGUMENT(-1),
  SDK_E_OUT_OF_MEMORY(-2),
  SDK_E_DEVICE_CONNECT_FAILED(-3),
  SDK_E_SERVICE_START_FAILED(-4),
  SDK_E_SERVICE_CONNECT_FAILED(-5),
  SDK_E_CONNECTION_TIMEOUT(-6),
  SDK_E_SEND_FAILED(-7),
  SDK_E_RECEIVE_FAILED(-8),
  SDK_E_LOCKDOWN_CONNECT_FAILED(-9),
  SDK_E_HANDSHAKE_FAILED(-10),
  SDK_E_PASSWORD_PROTECTED(-11),
  SDK_E_PAIRING_DIALOG_PENDING(-12),
  SDK_E_USER_DENIED_PAIRING(-13),
  SDK_E_EVENT_SUBSCRIBE_FAILED(-14),
  SDK_E_EVENT_UNSUBSCRIBE_FAILED(-15),
  SDK_E_APP_LAUNCH_FAILED(-16),
  SDK_E_DISK_IMAGE_MOUNT_FAILED(-17),
  SDK_E_FILE_OPEN_FAILED(-18),
  SDK_E_GET_VALUE_FAILED(-19),
  SDK_E_SET_VALUE_FAILED(-20),
  SDK_E_RESTART_FAILED(-21),
  SDK_E_SHUTDOWN_FAILED(-22),
  SDK_E_UNKNOWN_ERROR(-256),
  SDK_FORCE_SIGNED_TYPE(-1),
  SDK_UNKNOWN_ERROR_CODE(-256);
  private final int code;

  private SDKErrorCode(int code) {
    this.code=code;
  }

  public static void throwIfNeeded(int code) throws SDKException {
    if (code == 0) {
      return;
    } else {
      throw new SDKException(code);
    }
  }

  public int getCode() {
    return code;
  }
}
