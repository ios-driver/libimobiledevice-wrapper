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

package org.libimobiledevice.ios.driver.sdk.test;

import org.libimobiledevice.ios.driver.binding.exceptions.LibImobileException;
import org.libimobiledevice.ios.driver.binding.sdk.IDeviceSDK;
import org.libimobiledevice.ios.driver.binding.sdk.InformationService;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.libimobiledevice.ios.driver.test.ConnectedDevices.main;

// TODO freynaud create a proprty file with expected values.
public class InformationServiceTest {

  private InformationService service;

  @BeforeClass
  public void setup() throws LibImobileException {
    IDeviceSDK d = new IDeviceSDK(main);
    service = new InformationService(d);
  }

  @AfterClass
  public void tearDown() {
    service.release();
  }


  @Test
  public void serviceCanGetName() throws LibImobileException {
    String name = service.getDeviceName();
    System.out.println(name);
  }

  @Test
  public void serviceCanGetType() throws LibImobileException {
    String type = service.getDeviceType();
    System.out.println("type :" + type);
  }

  @Test
  public void serviceCanGetLanguage() throws LibImobileException {
    String language = service.getLanguage();
    System.out.println("language :" + language);
  }

  @Test
  public void serviceCanGetLocale() throws LibImobileException {
    String locale = service.getLocale();
    System.out.println("locale :" + locale);
  }

  @Test
  public void serviceCanGetProductVersion() throws LibImobileException {
    String productVersion = service.getProductVersion();
    System.out.println("productVersion :" + productVersion);
  }

  @Test
  public void serviceCanGetValueAsXMLAll() throws LibImobileException {
    String xml = service.getValueAsXML(null,null);
    System.out.println("xml :" + xml);
  }

  @Test
  public void serviceCanGetValueAsXMLAllKeysOfADomain() throws LibImobileException {
    String xml = service.getValueAsXML("com.apple.international",null);
    System.out.println("xml :" + xml);
  }

  @Test
  public void serviceCanGetValueAsXMLOneKey() throws LibImobileException {
    String xml = service.getValueAsXML("com.apple.international","HostKeyboard");
    System.out.println("xml :" + xml);
  }

  @Test(enabled = false)
  public void serviceCanSetDeviceName() throws LibImobileException {
   service.setDeviceName("test device");
  }

  @Test
  public void serviceCanSetLocale() throws LibImobileException {
    service.setLocale("en_GB");
  }


  @Test
  public void serviceCanSetLanguage() throws LibImobileException {
    service.setLanguage("fr");
  }

  @Test
  public void serviceCanGetDeviceEnabled() throws LibImobileException {
    boolean isDev = service.isDevModeEnabled();
    System.out.println("dev mode :" + isDev);
  }
}
