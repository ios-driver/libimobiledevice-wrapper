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

public class SDKException extends libImobileDeviceWrapperException {

  private final SDKErrorCode code;

  public SDKException(int code) {
    for (SDKErrorCode error : SDKErrorCode.values()) {
      if (code == error.getCode()) {
        this.code = error;
        return;
      }
    }
    System.err
        .println("Cannot find the returned error code for the SDK call.Assigning unknown.");
    this.code = SDKErrorCode.SDK_UNKNOWN_ERROR_CODE;
  }

  public SDKException(String message) {
    super(message);
    code = SDKErrorCode.SDK_UNKNOWN_ERROR_CODE;
  }
}
