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

import org.libimobiledevice.ios.driver.binding.exceptions.InstrumentsSDKException;

public enum InstrumentsErrorCode {

  INSTRUMENTS_E_TIMEOUT(-7),
  INSTRUMENTS_E_MUX_ERROR(-3),
  INSTRUMENTS_E_BAD_VERSION(-5),
  INSTRUMENTS_E_UNKNOWN_ERROR(-256),
  INSTRUMENTS_E_PLIST_ERROR(-2),
  INSTRUMENTS_E_SSL_ERROR(-4),
  INSTRUMENTS_E_START_SERVICE_ERROR(-8),
  INSTRUMENTS_E_SUCCESS(0),
  INSTRUMENTS_E_NO_MEM(-6),
  INSTRUMENTS_E_INVALID_ARG(-1),
  JAVA_ERROR(-255);
  private final int code;

  private InstrumentsErrorCode(int code) {
    this.code = code;
  }

  public static void throwIfNeeded(int code) throws InstrumentsSDKException {
    if (code == 0){
      return;
    }else {
      throw new InstrumentsSDKException(code);
    }
  }

  public int getCode() {
    return code;
  }
}
