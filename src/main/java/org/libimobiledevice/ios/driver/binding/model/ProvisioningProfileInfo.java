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


import com.dd.plist.NSArray;
import com.dd.plist.NSDate;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSString;
import com.dd.plist.PropertyListParser;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.PointerByReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.sdk_idevice_provisioning_profile_info_t;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.sdk_idevice_provisioning_profile_info_t.ByReference;


public class ProvisioningProfileInfo {


  private final String uuid;
  private final String name;
  private final String appId;
  private final String teamName;
  private final Date creationDate;
  private final Date expirationDate;
  private final List<String> devices;
  private final Entitlements entitlements;

  public ProvisioningProfileInfo(String raw) throws Exception {
    NSDictionary d = null;
    try {
      d = (NSDictionary) PropertyListParser.parse(raw.getBytes("UTf-8"));
    } catch (Exception e) {
      //ignore.
    }
    uuid = ((NSString) d.get("UUID")).getContent();
    name = ((NSString) d.get("Name")).getContent();
    appId = ((NSString) d.get("AppIDName")).getContent();
    teamName = ((NSString) d.get("TeamName")).getContent();
    creationDate = ((NSDate) d.get("CreationDate")).getDate();
    expirationDate = ((NSDate) d.get("ExpirationDate")).getDate();

    List<String> all = new ArrayList<String>();
    if (d.containsKey("ProvisionedDevices")) {
      NSArray ds = (NSArray) d.get("ProvisionedDevices");
      for (int i = 0; i < ds.count(); i++) {
        all.add(((NSString) ds.objectAtIndex(i)).getContent());
      }
    }
    devices = all;
    entitlements = new Entitlements(d.get("Entitlements").toXMLPropertyList());


  }

  public Entitlements getEntitlements() {
    return entitlements;
  }

  public ProvisioningProfileInfo(sdk_idevice_provisioning_profile_info_t profile) throws Exception {
    uuid = profile.uuid.getString(0);
    name = profile.name.getString(0);
    appId = profile.app_id_name.getString(0);
    teamName = profile.team_name.getString(0);
    creationDate = null;
    expirationDate = null;
    devices = readAllDevices(profile);

    if (profile.entitlements != null) {
      String raw = profile.entitlements.getString(0);
      if (raw == null) {
        entitlements = null;
      } else {
        entitlements = new Entitlements(raw);
      }
    } else {
      entitlements = null;
    }
  }

  private List<String> readAllDevices(sdk_idevice_provisioning_profile_info_t profile) {
    if (profile.provisioned_devices == null) {
      return new ArrayList<String>();
    }

    String last = "";
    int i = 1;
    while (last != null) {
      String[] a = profile.provisioned_devices.getStringArray(0, i);
      i++;
      last = a[a.length - 1];
    }
    // last element of the list in C is null. No need to keep it for java.
    String[] a = profile.provisioned_devices.getStringArray(0, i - 2);
    return Arrays.asList(a);
  }

  public static List<ProvisioningProfileInfo> read(PointerByReference ptr, int count)
      throws Exception {
    Pointer ptrs = ptr.getValue();
    ByReference profileRef = (ByReference) Structure.newInstance(ByReference.class, ptrs);
    profileRef.read();
    ByReference[] profilesArray = (ByReference[]) profileRef.toArray(count);

    List<ProvisioningProfileInfo> res = new ArrayList<ProvisioningProfileInfo>();
    for (sdk_idevice_provisioning_profile_info_t profile : profilesArray) {
      ProvisioningProfileInfo info = new ProvisioningProfileInfo(profile);
      res.add(info);
    }

    return res;
  }

  public String getUuid() {
    return uuid;
  }

  public String getName() {
    return name;
  }

  public String getAppId() {
    return appId;
  }

  public String getTeamName() {
    return teamName;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public Date getExpirationDate() {
    return expirationDate;
  }

  public List<String> getDevices() {
    return devices;
  }

  @Override
  public String toString() {
    return "uuid:" + getUuid() + "expires:"+expirationDate+",devices : " + getDevices() + "ents :" + getEntitlements();
  }
}
