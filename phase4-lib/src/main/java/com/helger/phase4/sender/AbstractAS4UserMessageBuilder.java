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
package com.helger.phase4.sender;

import java.security.cert.X509Certificate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.string.StringHelper;
import com.helger.phase4.attachment.Phase4OutgoingAttachment;
import com.helger.phase4.client.AS4ClientUserMessage;
import com.helger.phase4.client.IAS4SignalMessageConsumer;
import com.helger.phase4.ebms3header.Ebms3Property;
import com.helger.phase4.messaging.domain.MessageHelperMethods;
import com.helger.phase4.model.MessageProperty;
import com.helger.phase4.model.pmode.IPMode;

/**
 * Abstract builder base class for a user message.
 *
 * @author Philip Helger
 * @param <IMPLTYPE>
 *        The implementation type
 * @since 0.10.0
 */
public abstract class AbstractAS4UserMessageBuilder <IMPLTYPE extends AbstractAS4UserMessageBuilder <IMPLTYPE>> extends
                                                    AbstractAS4MessageBuilder <IMPLTYPE>
{
  protected IPMode m_aPMode;

  protected String m_sServiceType;
  protected String m_sService;
  protected String m_sAction;
  protected String m_sAgreementRef;
  protected String m_sPModeID;

  protected String m_sFromPartyIDType;
  protected String m_sFromPartyID;
  protected String m_sFromRole;

  protected String m_sToPartyIDType;
  protected String m_sToPartyID;
  protected String m_sToRole;

  protected String m_sConversationID;

  protected final ICommonsList <MessageProperty> m_aMessageProperties = new CommonsArrayList <> ();

  protected X509Certificate m_aReceiverCertificate;
  protected String m_sEndpointURL;

  protected final ICommonsList <Phase4OutgoingAttachment> m_aAttachments = new CommonsArrayList <> ();

  protected IAS4SignalMessageConsumer m_aSignalMsgConsumer;

  /**
   * Create a new builder, with the following fields already set:<br>
   * {@link #pmode(IPMode)}<br>
   */
  public AbstractAS4UserMessageBuilder ()
  {
    super ();
    // Set default values
    try
    {
      pmode (pmodeResolver ().getPModeOfID (null, "s", "a", "i", "r", "a", null));
    }
    catch (final Exception ex)
    {
      throw new IllegalStateException ("Failed to init AbstractAS4UserMessageBuilder", ex);
    }
  }

  /**
   * @return The currently set P-Mode. May be <code>null</code>.
   */
  @Nullable
  public final IPMode pmode ()
  {
    return m_aPMode;
  }

  /**
   * Set the PMode to be used. By default a generic PMode is used.
   *
   * @param aPMode
   *        The PMode to be used. May be <code>null</code>.
   * @return this for chaining
   */
  @Nonnull
  public final IMPLTYPE pmode (@Nullable final IPMode aPMode)
  {
    m_aPMode = aPMode;
    return thisAsT ();
  }

  /**
   * Set the "Service" value only, leaving the type <code>null</code>.
   *
   * @param sServiceValue
   *        Service value. May be <code>null</code>.
   * @return this for chaining.
   */
  @Nonnull
  public final IMPLTYPE service (@Nullable final String sServiceValue)
  {
    return service (null, sServiceValue);
  }

  /**
   * Set the "Service" value consisting of type and value. It's optional. If the
   * "Service" value is not set, it the "service type" defaults to the "process
   * identifier scheme" and the "service value" defaults to the "process
   * identifier value".
   *
   * @param sServiceType
   *        Service type. May be <code>null</code>.
   * @param sServiceValue
   *        Service value. May be <code>null</code>.
   * @return this for chaining.
   */
  @Nonnull
  public final IMPLTYPE service (@Nullable final String sServiceType, @Nullable final String sServiceValue)
  {
    m_sServiceType = sServiceType;
    m_sService = sServiceValue;
    return thisAsT ();
  }

  /**
   * Set the "Action" value. It's optional. If the "Action" value is not set, it
   * defaults to the "document type identifier value" (URI encoded).
   *
   * @param sAction
   *        Action value. May be <code>null</code>.
   * @return this for chaining.
   */
  @Nonnull
  public final IMPLTYPE action (@Nullable final String sAction)
  {
    m_sAction = sAction;
    return thisAsT ();
  }

  /**
   * Set the "AgreementRef" value. It's optional.
   *
   * @param sAgreementRef
   *        Agreement reference. May be <code>null</code>.
   * @return this for chaining.
   */
  @Nonnull
  public final IMPLTYPE agreementRef (@Nullable final String sAgreementRef)
  {
    m_sAgreementRef = sAgreementRef;
    return thisAsT ();
  }

  /**
   * Set the optional PMode ID for packaging in the user message.
   *
   * @param s
   *        Pmode ID. May be <code>null</code>.
   * @return this for chaining
   */
  @Nonnull
  public final IMPLTYPE pmodeID (@Nullable final String s)
  {
    m_sPModeID = s;
    return thisAsT ();
  }

  /**
   * Set the "from party ID type".
   *
   * @param sFromPartyIDType
   *        The from party ID.
   * @return this for chaining
   */
  @Nonnull
  public final IMPLTYPE fromPartyIDType (@Nullable final String sFromPartyIDType)
  {
    m_sFromPartyIDType = sFromPartyIDType;
    return thisAsT ();
  }

  /**
   * Set the "from party ID".
   *
   * @param sFromPartyID
   *        The from party ID.
   * @return this for chaining
   */
  @Nonnull
  public final IMPLTYPE fromPartyID (@Nullable final String sFromPartyID)
  {
    m_sFromPartyID = sFromPartyID;
    return thisAsT ();
  }

  /**
   * Set the "from party role". This is optional
   *
   * @param sFromRole
   *        The from role. May be <code>null</code>.
   * @return this for chaining
   */
  @Nonnull
  public final IMPLTYPE fromRole (@Nullable final String sFromRole)
  {
    m_sFromRole = sFromRole;
    return thisAsT ();
  }

  /**
   * Set the "to party ID type".
   *
   * @param sToPartyIDType
   *        The to party ID.
   * @return this for chaining
   */
  @Nonnull
  public final IMPLTYPE toPartyIDType (@Nullable final String sToPartyIDType)
  {
    m_sToPartyIDType = sToPartyIDType;
    return thisAsT ();
  }

  /**
   * Set the "to party ID".
   *
   * @param sToPartyID
   *        The to party ID.
   * @return this for chaining
   */
  @Nonnull
  public final IMPLTYPE toPartyID (@Nullable final String sToPartyID)
  {
    m_sToPartyID = sToPartyID;
    return thisAsT ();
  }

  /**
   * Set the "to party role". This is optional
   *
   * @param sToRole
   *        The to role. May be <code>null</code>.
   * @return this for chaining
   */
  @Nonnull
  public final IMPLTYPE toRole (@Nullable final String sToRole)
  {
    m_sToRole = sToRole;
    return thisAsT ();
  }

  /**
   * Set the optional AS4 conversation ID. If this field is not set, a random
   * conversation ID is created.
   *
   * @param sConversationID
   *        The optional AS4 conversation ID to be used. May be
   *        <code>null</code>.
   * @return this for chaining
   */
  @Nonnull
  public final IMPLTYPE conversationID (@Nullable final String sConversationID)
  {
    m_sConversationID = sConversationID;
    return thisAsT ();
  }

  @Nonnull
  public final IMPLTYPE addMessageProperty (@Nullable final Ebms3Property a)
  {
    return addMessageProperty (a == null ? null : MessageProperty.builder (a));
  }

  @Nonnull
  public final IMPLTYPE addMessageProperty (@Nullable final MessageProperty.Builder a)
  {
    return addMessageProperty (a == null ? null : a.build ());
  }

  @Nonnull
  public final IMPLTYPE addMessageProperty (@Nullable final MessageProperty a)
  {
    if (a != null)
      m_aMessageProperties.add (a);
    return thisAsT ();
  }

  @Nonnull
  public final IMPLTYPE messageProperty (@Nullable final Ebms3Property a)
  {
    return messageProperty (a == null ? null : MessageProperty.builder (a));
  }

  @Nonnull
  public final IMPLTYPE messageProperty (@Nullable final MessageProperty.Builder a)
  {
    return messageProperty (a == null ? null : a.build ());
  }

  @Nonnull
  public final IMPLTYPE messageProperty (@Nullable final MessageProperty a)
  {
    if (a == null)
      m_aMessageProperties.clear ();
    else
      m_aMessageProperties.set (a);
    return thisAsT ();
  }

  @Nonnull
  public final IMPLTYPE messageProperties (@Nullable final MessageProperty... a)
  {
    m_aMessageProperties.setAll (a);
    return thisAsT ();
  }

  @Nonnull
  public final IMPLTYPE messageProperties (@Nullable final Iterable <? extends MessageProperty> a)
  {
    m_aMessageProperties.setAll (a);
    return thisAsT ();
  }

  /**
   * Set the receiver certificate.
   *
   * @param aCertificate
   *        The certificate of the receiver to be used. May be
   *        <code>null</code>.
   * @return this for chaining
   */
  @Nonnull
  public final IMPLTYPE receiverCertificate (@Nullable final X509Certificate aCertificate)
  {
    m_aReceiverCertificate = aCertificate;
    return thisAsT ();
  }

  /**
   * Set an receiver AS4 endpoint URL, independent of its usability.
   *
   * @param sEndointURL
   *        The endpoint URL to be used. May be <code>null</code>.
   * @return this for chaining
   */
  @Nonnull
  public final IMPLTYPE endpointURL (@Nullable final String sEndointURL)
  {
    m_sEndpointURL = sEndointURL;
    return thisAsT ();
  }

  /**
   * Add an optional attachment
   *
   * @param a
   *        The attachment to be added. May be <code>null</code>.
   * @return this for chaining
   */
  @Nonnull
  public final IMPLTYPE addAttachment (@Nullable final Phase4OutgoingAttachment.Builder a)
  {
    return addAttachment (a == null ? null : a.build ());
  }

  /**
   * Add an optional attachment
   *
   * @param a
   *        The attachment to be added. May be <code>null</code>.
   * @return this for chaining
   */
  @Nonnull
  public final IMPLTYPE addAttachment (@Nullable final Phase4OutgoingAttachment a)
  {
    if (a != null)
      m_aAttachments.add (a);
    return thisAsT ();
  }

  /**
   * Set optional attachment. All existing attachments are overridden.
   *
   * @param a
   *        The attachment to be set. May be <code>null</code>.
   * @return this for chaining
   */
  @Nonnull
  public final IMPLTYPE attachment (@Nullable final Phase4OutgoingAttachment.Builder a)
  {
    return attachment (a == null ? null : a.build ());
  }

  /**
   * Set optional attachment. All existing attachments are overridden.
   *
   * @param a
   *        The attachment to be set. May be <code>null</code>.
   * @return this for chaining
   */
  @Nonnull
  public final IMPLTYPE attachment (@Nullable final Phase4OutgoingAttachment a)
  {
    if (a == null)
      m_aAttachments.clear ();
    else
      m_aAttachments.set (a);
    return thisAsT ();
  }

  /**
   * Set optional attachments. All existing attachments are overridden.
   *
   * @param a
   *        The attachment to be set. May be <code>null</code>.
   * @return this for chaining
   */
  @Nonnull
  public final IMPLTYPE attachments (@Nullable final Phase4OutgoingAttachment... a)
  {
    m_aAttachments.setAll (a);
    return thisAsT ();
  }

  /**
   * Set optional attachments. All existing attachments are overridden.
   *
   * @param a
   *        The attachment to be set. May be <code>null</code>.
   * @return this for chaining
   */
  @Nonnull
  public final IMPLTYPE attachments (@Nullable final Iterable <? extends Phase4OutgoingAttachment> a)
  {
    m_aAttachments.setAll (a);
    return thisAsT ();
  }

  /**
   * Set an optional Ebms3 Signal Message Consumer. If this consumer is set, the
   * response is trying to be parsed as a Signal Message. This method is
   * optional and must not be called prior to sending.
   *
   * @param aSignalMsgConsumer
   *        The optional signal message consumer. May be <code>null</code>.
   * @return this for chaining
   */
  @Nonnull
  public final IMPLTYPE signalMsgConsumer (@Nullable final IAS4SignalMessageConsumer aSignalMsgConsumer)
  {
    m_aSignalMsgConsumer = aSignalMsgConsumer;
    return thisAsT ();
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  public boolean isEveryRequiredFieldSet ()
  {
    if (!super.isEveryRequiredFieldSet ())
      return false;

    if (m_aPMode == null)
      return false;

    // m_sServiceType may be null
    // m_sService may be null
    // m_sAction may be null
    // m_sAgreementRef may be null
    // m_sPModeID may be null

    // m_sFromPartyIDType may be null
    if (StringHelper.hasNoText (m_sFromPartyID))
      return false;
    if (StringHelper.hasNoText (m_sFromRole))
      return false;

    // m_sToPartyIDType may be null
    if (StringHelper.hasNoText (m_sToPartyID))
      return false;
    if (StringHelper.hasNoText (m_sToRole))
      return false;

    // m_sConversationID is optional

    // m_aMessageProperties is final

    // m_aReceiverCertificate is optional
    if (StringHelper.hasNoText (m_sEndpointURL))
      return false;

    // m_aAttachments may be null

    // m_aSignalMsgConsumer may be null

    // All valid
    return true;
  }

  /**
   * This method applies all builder parameters onto the user message, except
   * the attachments.
   *
   * @param aUserMsg
   *        The user message the parameters should be applied to. May not be
   *        <code>null</code>.
   */
  @OverridingMethodsMustInvokeSuper
  protected void applyToUserMessage (@Nonnull final AS4ClientUserMessage aUserMsg)
  {
    if (m_nMaxRetries >= 0)
      aUserMsg.setMaxRetries (m_nMaxRetries);
    if (m_nRetryIntervalMS >= 0)
      aUserMsg.setRetryIntervalMS (m_nRetryIntervalMS);

    aUserMsg.setHttpClientFactory (m_aHttpClientFactory);

    // Otherwise Oxalis dies
    aUserMsg.setQuoteHttpHeaders (false);
    aUserMsg.setSoapVersion (m_eSoapVersion);
    aUserMsg.setSendingDateTimeOrNow (m_aSendingDateTime);
    // Set the keystore/truststore parameters
    aUserMsg.setAS4CryptoFactory (m_aCryptoFactory);
    aUserMsg.setPMode (m_aPMode, true);

    // Set after PMode
    if (m_aReceiverCertificate != null)
      aUserMsg.cryptParams ().setCertificate (m_aReceiverCertificate);

    aUserMsg.setAgreementRefValue (m_sAgreementRef);
    if (StringHelper.hasText (m_sPModeID))
      aUserMsg.setPModeID (m_sPModeID);
    else
      aUserMsg.setPModeIDFactory (x -> null);
    aUserMsg.setServiceType (m_sServiceType);
    aUserMsg.setServiceValue (m_sService);
    aUserMsg.setAction (m_sAction);
    if (StringHelper.hasText (m_sMessageID))
      aUserMsg.setMessageID (m_sMessageID);
    // Empty conversation ID is okay
    aUserMsg.setConversationID (m_sConversationID != null ? m_sConversationID : MessageHelperMethods.createRandomConversationID ());

    aUserMsg.setFromPartyIDType (m_sFromPartyIDType);
    aUserMsg.setFromPartyID (m_sFromPartyID);
    aUserMsg.setFromRole (m_sFromRole);

    aUserMsg.setToPartyIDType (m_sToPartyIDType);
    aUserMsg.setToPartyID (m_sToPartyID);
    aUserMsg.setToRole (m_sToRole);

    for (final MessageProperty aItem : m_aMessageProperties)
      aUserMsg.ebms3Properties ().add (aItem.getAsEbms3Property ());
  }
}
