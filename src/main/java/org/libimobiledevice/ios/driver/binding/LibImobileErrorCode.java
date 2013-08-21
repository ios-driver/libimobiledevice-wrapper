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

package org.libimobiledevice.ios.driver.binding;

public enum LibImobileErrorCode {

  IDEVICE_E_SUCCESS(0),
  IDEVICE_E_INVALID_ARG(-1),
  IDEVICE_E_UNKNOWN_ERROR(-2),
  IDEVICE_E_NO_DEVICE(-3),
  IDEVICE_E_NOT_ENOUGH_DATA(-4),
  IDEVICE_E_BAD_HEADER(-5),
  IDEVICE_E_SSL_ERROR(-6);

  private final int status_code;

  LibImobileErrorCode(int status_code) {
    this.status_code = status_code;
  }
}
