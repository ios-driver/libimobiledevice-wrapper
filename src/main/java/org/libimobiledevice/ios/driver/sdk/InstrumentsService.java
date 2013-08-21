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

package org.libimobiledevice.ios.driver.sdk;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

import org.libimobiledevice.ios.driver.binding.ImobiledeviceInstrumentsLibrary;
import org.libimobiledevice.ios.driver.binding.PlistLibrary;

import java.nio.IntBuffer;

public class InstrumentsService {

  private final IDeviceSDK device;
  private final ImobiledeviceInstrumentsLibrary.instruments_client_t instruments_client;
  private final ImobiledeviceInstrumentsLibrary.uiautomation_client_t automation_client;
  private final ImobiledeviceInstrumentsLibrary.processcontrol_client_t process_control_client;

  private final Handler msgHandler = new Handler();
  private final
  ImobiledeviceInstrumentsLibrary.uiautomation_exception_handler_t
      exHandler =
      new HandlerEx();

  private final String bundleId;
  private int pid;


  public InstrumentsService(IDeviceSDK device, String bundleId, MessageHandler handler) {
    this.device = device;
    this.bundleId = bundleId;
    if (handler == null) {
      System.out.println("Handler not specified. Souting msges");
      handler = new MessageHandler() {
        @Override
        public void handle(String message) {
          System.out.println(message);
        }
      };
    }

    msgHandler.setCallback(handler);
    // TODO freynaud
    PointerByReference ptr = new PointerByReference();
    ImobiledeviceInstrumentsLibrary
        .instruments_client_start_service(device.getHandleIdevice(), ptr, "libimobile-java");
    instruments_client = new ImobiledeviceInstrumentsLibrary.instruments_client_t(ptr.getValue());

    PointerByReference ptr2 = new PointerByReference();
    ImobiledeviceInstrumentsLibrary.uiautomation_client_new(instruments_client, ptr2);
    automation_client = new ImobiledeviceInstrumentsLibrary.uiautomation_client_t(ptr2.getValue());

    PointerByReference ptr3 = new PointerByReference();
    ImobiledeviceInstrumentsLibrary.processcontrol_client_new(instruments_client, ptr3);
    process_control_client =
        new ImobiledeviceInstrumentsLibrary.processcontrol_client_t(ptr3.getValue());

    ImobiledeviceInstrumentsLibrary
        .uiautomation_client_set_message_handler(automation_client, msgHandler);
    ImobiledeviceInstrumentsLibrary.uiautomation_client_set_exception_handler(automation_client,
                                                                              exHandler);

    IntBuffer pid_ptr = IntBuffer.allocate(1);
    ImobiledeviceInstrumentsLibrary
        .processcontrol_client_process_identifier_for_bundle_identifier(process_control_client,
                                                                        bundleId, pid_ptr);
    pid = pid_ptr.get(0);

    if (pid > 0) {
      ImobiledeviceInstrumentsLibrary.processcontrol_client_kill_pid(process_control_client, pid);
    }

    ImobiledeviceInstrumentsLibrary
        .uiautomation_client_configure_launch_environment(automation_client,
                                                          new PointerByReference());

    ImobiledeviceInstrumentsLibrary
        .processcontrol_client_launch_suspended_process_with_bundle_identifier(
            process_control_client, bundleId, new PointerByReference(), new PointerByReference(),
            pid_ptr);
    pid = pid_ptr.get(0);

    ImobiledeviceInstrumentsLibrary
        .processcontrol_client_start_observing_pid(process_control_client, pid);
    ImobiledeviceInstrumentsLibrary.processcontrol_client_resume_pid(process_control_client, pid);

    ImobiledeviceInstrumentsLibrary
        .uiautomation_client_start_agent_for_app_with_pid(automation_client, bundleId, pid);

    // wait for agent ready.
    int res = 0;
    while (res != 1) {
      try {
        res =
            ImobiledeviceInstrumentsLibrary.uiautomation_client_get_agent_ready(automation_client);
        Thread.sleep(250);
      } catch (InterruptedException e) {
      }
    }
  }


  class Handler implements ImobiledeviceInstrumentsLibrary.uiautomation_message_handler_t {

    private MessageHandler h;

    @Override
    public int apply(Pointer client, Pointer message) {
      PointerByReference xml_ptr = new PointerByReference();
      IntBuffer buff = IntBuffer.allocate(1);
      PlistLibrary.plist_to_xml(message, xml_ptr, buff);
      String msg = xml_ptr.getValue().getString(0);
      h.handle(msg);
      //System.err.println(System.currentTimeMillis()+" -- ");
      //Counter.INSTANCE.lap();
      //ImobiledeviceSdkLibrary.sdk_idevice_free_string(xml_ptr.getPointer());
      return 1;
    }

    public void setCallback(MessageHandler h) {
      this.h = h;
    }
  }

  class HandlerEx implements ImobiledeviceInstrumentsLibrary.uiautomation_exception_handler_t {


    @Override
    public int apply(Pointer client, Pointer message, int line, Pointer source) {
      return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
  }


  /**
   * executes a script on the device. Doesn't wait for the script to return, and will crash if you
   * send a script before the previous one has returned.
   */
  public void executeScriptNonManaged(String script) {
    String safe = "try{ " + script + ""
                  + "} catch (err){"
                  + "UIALogger.logMessage('there was an error.'+err.message);"
                  //+ "throw err;"
                  + "}";
    // + "UIALogger.logMessage('instruments_scriptCompleted "+id +"');";
    //callback.setBusy(true);

    ImobiledeviceInstrumentsLibrary.uiautomation_client_start_script_with_info(automation_client,
                                                                               this.bundleId, safe,
                                                                               "ios-driver");

    //callback.waitForScript();
  }

  //
//  public void startApp(String bundleId) {
//    state = new instruments_state_t();
//    state.bundleIdentifier = bundleId;
//    if ("com.apple.mobilesafari".equals(bundleId)) {
//      state.wait_for_agent = 0;
//    }
//
//    callback = new InstrumentsCallbackImpl(state, handler);
//    client = device.lockdownd();
//    PointerByReference args = new PointerByReference();
//    PointerByReference envt = new PointerByReference();
//    lib.instruments_start(device.getHandle(), client, state, envt, args, l, callback);
//    /*while (state.agent_is_ready == 0) {
//      System.out.println("waiting ...");
//      try {
//        Thread.sleep(1000);
//      } catch (InterruptedException e) {
//        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//      }
//
//    }*/
//  }
//
  public void stopApp() {
    ImobiledeviceInstrumentsLibrary.processcontrol_client_kill_pid(process_control_client, pid);
    ImobiledeviceInstrumentsLibrary
        .processcontrol_client_stop_observing_pid(process_control_client, pid);
    ImobiledeviceInstrumentsLibrary.processcontrol_client_free(process_control_client);
    ImobiledeviceInstrumentsLibrary.uiautomation_client_free(automation_client);
    ImobiledeviceInstrumentsLibrary.instruments_client_free(instruments_client);

  }

}
