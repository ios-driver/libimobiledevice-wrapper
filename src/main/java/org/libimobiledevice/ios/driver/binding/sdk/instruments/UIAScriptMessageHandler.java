package org.libimobiledevice.ios.driver.binding.sdk.instruments;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

import org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceInstrumentsLibrary;
import org.libimobiledevice.ios.driver.binding.raw.PlistLibrary;

import java.nio.IntBuffer;

public class UIAScriptMessageHandler
    implements ImobiledeviceInstrumentsLibrary.uiautomation_message_handler_t {

  @Override
  public int apply(Pointer client, Pointer message) {
    PointerByReference xml_ptr = new PointerByReference();
    IntBuffer buff = IntBuffer.allocate(1);
    PlistLibrary.plist_to_xml(message, xml_ptr, buff);
    String msg = xml_ptr.getValue().getString(0);
    System.out.println("UIAScriptMessageHandler:"+msg);
    //ImobiledeviceSdkLibrary.sdk_idevice_free_string(xml_ptr.getPointer());
    return 1;
  }
}
