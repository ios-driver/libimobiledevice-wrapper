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

public class InstrumentsSDKException extends libImobileDeviceWrapperException {

  private final InstrumentsErrorCode code;

  public InstrumentsSDKException(int code) {
    for (InstrumentsErrorCode error : InstrumentsErrorCode.values()) {
      if (code == error.getCode()) {
        this.code = error;
        return;
      }
    }
    System.err
        .println("Cannot find the returned error code for the instruments call.Assigning unknown.");
    this.code = InstrumentsErrorCode.INSTRUMENTS_E_UNKNOWN_ERROR;
  }

  public InstrumentsSDKException(String message) {
    super(message);
    this.code = InstrumentsErrorCode.JAVA_ERROR;
  }
}
