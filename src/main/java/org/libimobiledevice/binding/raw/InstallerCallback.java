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

package org.libimobiledevice.binding.raw;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

import org.libimobiledevice.binding.raw.installer.Installer;

import java.nio.LongBuffer;

public class InstallerCallback /*implements IMobileDeviceService.install_status_cb */{

//  private final IMobileDeviceService lib = IMobileDeviceService.INSTANCE;
//  private final Installer installer;
//
//  public InstallerCallback(Installer installer) {
//    this.installer = installer;
//  }
//
//
//  @Override
//  public void apply(Pointer operation, Pointer status, Pointer idvoid) {
//    //lib.status_cb_c(operation.getString(0), status, idvoid, new Logger("todo"));
//    event_t event = installer.event;
//    //System.out.println(event);
//    if (status != null && operation != null) {
//      //printf("INSIDE status_cb_c with status\n");
//      Pointer npercent = lib.plist_dict_get_item(status, "PercentComplete");
//      Pointer nstatus = lib.plist_dict_get_item(status, "Status");
//      Pointer nerror = lib.plist_dict_get_item(status, "Error");
//      long percent = 0;
//      String status_msg = null;
//      if (npercent!= null) {
//        LongBuffer val = LongBuffer.allocate(1);
//        lib.plist_get_uint_val(npercent, val);
//        percent = val.get(0);
//      }
//      if (nstatus != null) {
//        PointerByReference ptr = new PointerByReference();
//        lib.plist_get_string_val(nstatus, ptr);
//        status_msg = ptr.getValue().getString(0);
//        if ("Complete".equals(status_msg)) {
//          event.op_completed = 1;
//        }
//      }
//      if (nerror == null) {
//        //System.out.println(status_msg);
//        if (npercent == null) {
//          System.out.println("op:"+operation.getString(0)+", "+status_msg);
//          //logger(id);
//          //logger(operation);
//          //logger(status_msg);
//        } else {
//          System.out.println("op:"+operation.getString(0)+", "+status_msg+" , % :"+percent);
//          //printf( "%s;%s;%s;%d%%\n", event->uuid, operation, status_msg, percent);
//          //logger(id);
//          //logger(operation);
//          //logger(status_msg);
//          //logger("more %");
//        }
//      } else {
//        String err_msg = null;
//        PointerByReference ptr = new PointerByReference();
//        lib.plist_get_string_val(nerror, ptr);
//        err_msg = ptr.getValue().getString(0);
//        //printf("%s;%s;Error occured: %s", event->uuid, operation, err_msg);
//        //free(err_msg);
//        System.out.println("error :" + err_msg);
//        event.err_occured = 1;
//      }
//
//
//    } else {
//      System.out.println("called with wrong data ?");
//    }
//  }

}
