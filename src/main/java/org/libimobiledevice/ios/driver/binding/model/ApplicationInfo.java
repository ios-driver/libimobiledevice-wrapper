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

package org.libimobiledevice.ios.driver.binding.model;

import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSNumber;
import com.dd.plist.NSObject;
import com.dd.plist.NSString;
import com.dd.plist.XMLPropertyListParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ApplicationInfo {

  private static final
  java.util.logging.Logger
      log = java.util.logging.Logger.getLogger(ApplicationInfo.class.getName());

  private final Map<String, Object> properties;


  public static List<ApplicationInfo>  extractApplications(String rawXML) {
    List<ApplicationInfo> infos = new ArrayList<ApplicationInfo>();
    NSArray apps = null;
    try {
      apps = (NSArray) XMLPropertyListParser.parse(rawXML.getBytes("UTF-8"));
    } catch (Exception e) {
      log.warning("Error parsing the xml returned : " + e.getMessage() + " , xml=\n" + rawXML);
      return infos;
    }
    for (int i = 0; i < apps.count(); i++) {
      NSObject app = apps.objectAtIndex(i);
      ApplicationInfo info = new ApplicationInfo(app);
      infos.add(info);
    }
    return infos;
  }

  public ApplicationInfo(NSObject app) {
    properties = cast(app);
  }

  public Object getProperty(String key) {
    return properties.get(key);
  }

  public Set<String> keySet() {
    return properties.keySet();

  }

  public Map<String, Object> getProperties() {
    return properties;
  }

@SuppressWarnings("unchecked")
private <T> T cast(NSObject value) {
    if (value == null) {
      return null;
    } else if (value instanceof NSString) {
      return (T) value.toString();
    } else if (value instanceof NSNumber) {
      NSNumber number = (NSNumber) value;
      if (number.isInteger()) {
        return (T) new Integer(number.intValue());
      } else if (number.isBoolean()) {
        return (T) new Boolean(number.boolValue());
      } else {
        // TODO can be long, float or double
        return (T) new Float(number.floatValue());
      }
    } else if (value instanceof NSArray) {
      List<T> res = new ArrayList<T>();
      NSArray array = (NSArray) value;
      for (int i = 0; i < array.count(); i++) {
        res.add((T) cast(array.objectAtIndex(i)));
      }
      return (T) res;
    } else if (value instanceof NSDictionary) {
      Map<String, Object> res = new HashMap<String, Object>();
      for (String key : ((NSDictionary) value).allKeys()) {
        NSObject o = ((NSDictionary) value).objectForKey(key);
        res.put(key, cast(o));
      }
      return (T) res;
    } else {
      log.warning("Can't cast from " + value.getClass());
    }
    return null;
  }

  public String getApplicationId() {
    return (String) properties.get("CFBundleIdentifier");
  }

  @Override
  public String toString() {
    return getApplicationId() + ":\n" + properties.toString();
  }

}

