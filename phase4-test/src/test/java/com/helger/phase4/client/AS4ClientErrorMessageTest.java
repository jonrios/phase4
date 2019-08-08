/**
 * Copyright (C) 2015-2019 Philip Helger (www.helger.com)
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
package com.helger.phase4.client;

import static org.junit.Assert.fail;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.WillNotClose;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.helger.phase4.client.AS4ClientErrorMessage;
import com.helger.phase4.error.EEbmsError;
import com.helger.phase4.server.MockJettySetup;
import com.helger.phase4.server.MockPModeGenerator;
import com.helger.phase4.servlet.mgr.AS4ServerConfiguration;
import com.helger.phase4.soap.ESOAPVersion;
import com.helger.phase4.util.AS4ResourceHelper;

/**
 * Test class for class {@link AS4ClientErrorMessage}
 *
 * @author Philip Helger
 */
public final class AS4ClientErrorMessageTest
{
  @WillNotClose
  private static AS4ResourceHelper s_aResMgr;

  @BeforeClass
  public static void startServer () throws Exception
  {
    AS4ServerConfiguration.internalReinitForTestOnly ();
    MockJettySetup.startServer ();
    s_aResMgr = MockJettySetup.getResourceManagerInstance ();
    MockPModeGenerator.ensureMockPModesArePresent ();
  }

  @AfterClass
  public static void shutDownServer () throws Exception
  {
    s_aResMgr = null;
    MockJettySetup.shutDownServer ();
  }

  private static void _ensureInvalidState (@Nonnull final AS4ClientErrorMessage aClient) throws Exception
  {
    try
    {
      aClient.buildMessage ("bla", null);
      fail ();
    }
    catch (final IllegalStateException ex)
    {
      // expected
    }
  }

  private static void _ensureValidState (@Nonnull final AS4ClientErrorMessage aClient) throws Exception
  {
    try
    {
      aClient.buildMessage ("bla", null);
    }
    catch (final IllegalStateException ex)
    {
      fail ();
    }
  }

  @Test
  public void buildMessageMandatoryCheckFailure () throws Exception
  {
    final AS4ClientErrorMessage aClient = new AS4ClientErrorMessage (s_aResMgr);
    _ensureInvalidState (aClient);
    aClient.setSOAPVersion (ESOAPVersion.AS4_DEFAULT);
    _ensureInvalidState (aClient);
    aClient.addErrorMessage (EEbmsError.EBMS_INVALID_HEADER, Locale.US);
    _ensureInvalidState (aClient);
    aClient.setRefToMessageID ("referencefortestingpurpose");
    _ensureValidState (aClient);
  }
}