/**
 * Copyright (C) 2015-2020 Philip Helger (www.helger.com)
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
package com.helger.phase4.peppol;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.helger.bdve.peppol.PeppolValidation3_10_1;
import com.helger.commons.system.SystemProperties;
import com.helger.peppol.sml.ESML;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.phase4.dump.AS4DumpManager;
import com.helger.phase4.mgr.MetaAS4Manager;
import com.helger.phase4.servlet.dump.AS4IncomingDumperFileBased;
import com.helger.phase4.servlet.dump.AS4OutgoingDumperFileBased;
import com.helger.phase4.servlet.dump.AS4RawResponseConsumerWriteToFile;
import com.helger.servlet.mock.MockServletContext;
import com.helger.smpclient.peppol.SMPClientReadOnly;
import com.helger.web.scope.mgr.WebScopeManager;
import com.helger.xml.serialize.read.DOMReader;

/**
 * Example for sending something to the Elcom Test endpoint.
 *
 * @author Philip Helger
 */
public final class MainPhase4PeppolSenderElcomOrder
{
  private static final Logger LOGGER = LoggerFactory.getLogger (MainPhase4PeppolSenderElcomOrder.class);

  public static void main (final String [] args)
  {
    // Enable in-memory managers
    SystemProperties.setPropertyValue (MetaAS4Manager.SYSTEM_PROPERTY_PHASE4_MANAGER_INMEMORY, true);

    WebScopeManager.onGlobalBegin (MockServletContext.create ());

    // Dump (for debugging purpose only)
    AS4DumpManager.setIncomingDumper (new AS4IncomingDumperFileBased ());
    AS4DumpManager.setOutgoingDumper (new AS4OutgoingDumperFileBased ());

    try
    {
      final Element aPayloadElement = DOMReader.readXMLDOM (new File ("src/test/resources/examples/test-order.xml")).getDocumentElement ();
      if (aPayloadElement == null)
        throw new IllegalStateException ("Failed to read XML file to be send");

      // Start configuring here
      final IParticipantIdentifier aReceiverID = Phase4PeppolSender.IF.createParticipantIdentifierWithDefaultScheme ("0088:elcom-test");
      if (Phase4PeppolSender.builder ()
                            .documentTypeID (Phase4PeppolSender.IF.createDocumentTypeIdentifierWithDefaultScheme ("urn:oasis:names:specification:ubl:schema:xsd:Order-2::Order##urn:fdc:peppol.eu:poacc:trns:order:3::2.1"))
                            .processID (Phase4PeppolSender.IF.createProcessIdentifierWithDefaultScheme ("urn:fdc:peppol.eu:poacc:bis:ordering:3"))
                            .senderParticipantID (Phase4PeppolSender.IF.createParticipantIdentifierWithDefaultScheme ("9915:phase4-test-sender"))
                            .receiverParticipantID (aReceiverID)
                            .senderPartyID ("POP000306")
                            .payload (aPayloadElement)
                            .smpClient (new SMPClientReadOnly (Phase4PeppolSender.URL_PROVIDER, aReceiverID, ESML.DIGIT_TEST))
                            .rawResponseConsumer (new AS4RawResponseConsumerWriteToFile ())
                            .validationConfiguration (PeppolValidation3_10_1.VID_OPENPEPPOL_ORDER_V3,
                                                      new Phase4PeppolValidatonResultHandler ())
                            .sendMessage ()
                            .isSuccess ())
      {
        LOGGER.info ("Successfully sent Peppol message via AS4");
      }
      else
      {
        LOGGER.error ("Failed to send Peppol message via AS4");
      }
    }
    catch (final Phase4PeppolValidationException ex)
    {
      LOGGER.error ("Error sending Peppol message via AS4: " + ex.getValidationResult ());
    }
    catch (final Exception ex)
    {
      LOGGER.error ("Error sending Peppol message via AS4", ex);
    }
    finally
    {
      WebScopeManager.onGlobalEnd ();
    }
  }
}