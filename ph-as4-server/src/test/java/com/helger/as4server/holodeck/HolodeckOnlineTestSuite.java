/**
 * Copyright (C) 2015-2017 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.as4server.holodeck;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import com.helger.as4server.message.ReceiptMessageTest;
import com.helger.as4server.message.UserMessageOneAttachmentTest;
import com.helger.as4server.message.UserMessageSoapBodyPayloadTest;
import com.helger.as4server.settings.AS4ServerConfiguration;

@RunWith (Categories.class)
@IncludeCategory (IHolodeckTests.class)
@SuiteClasses ({ UserMessageSoapBodyPayloadTest.class,
                 UserMessageOneAttachmentTest.class,
                 ReceiptMessageTest.class })
@Ignore
public class HolodeckOnlineTestSuite
{
  /** The default URL where Holodeck is supposed to run */
  // TODO will be changed soon
  public static final String DEFAULT_HOLODECK_URI = "http://localhost:8080/msh";

  @BeforeClass
  public static void init ()
  {
    AS4ServerConfiguration.reinitForTestOnly ();
    AS4ServerConfiguration.getMutableSettings ().setValue ("server.jetty.enabled", false);
    AS4ServerConfiguration.getMutableSettings ().setValue ("server.address", DEFAULT_HOLODECK_URI);
  }
}
