/*
 * Copyright 2012-2014 eBay Software Foundation and ios-driver committers
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

package org.libimobiledevice.ios.driver.binding.model;


import com.dd.plist.NSDictionary;
import com.dd.plist.NSNumber;
import com.dd.plist.NSString;
import com.dd.plist.XMLPropertyListParser;

public class Entitlements {

  private final Boolean getTaskAllow;

  public String getAppId() {
    return appId;
  }

  public Boolean getGetTaskAllow() {
    return getTaskAllow;
  }

  private final String appId;


  public Entitlements(String raw) throws Exception {
    NSDictionary ents = (NSDictionary) XMLPropertyListParser.parse(raw.getBytes("UTF-8"));
    NSNumber n = (NSNumber) ents.get("get-task-allow");
    getTaskAllow = n == null ? false : n.boolValue();

    NSString s = (NSString) ents.get("application-identifier");
    appId = s == null ? null : s.getContent();
//    ents.get("keychain-access-groups");
  }

  @Override
  public String toString() {
    return "get-task-allow:" + getGetTaskAllow() + ", application-identifier:" + getAppId();
  }
}
