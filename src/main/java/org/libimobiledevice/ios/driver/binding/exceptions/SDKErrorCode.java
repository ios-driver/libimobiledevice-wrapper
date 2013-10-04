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

  SDK_UNKNOWN_ERROR_CODE(-256);
  private final int code;

  private SDKErrorCode(int code) {
    this.code = code;
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
