/**
 * Copyright (C) 2014-2016 Philip Helger (www.helger.com)
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
package com.helger.as4lib.marshaller;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.apache.xml.security.binding.xmldsig.ReferenceType;

import com.helger.as4lib.constants.CAS4;
import com.helger.jaxb.builder.JAXBWriterBuilder;
import com.helger.xml.namespace.MapBasedNamespaceContext;

/**
 * A reader builder for XMLDSig documents.
 *
 * @author Philip Helger
 * @param <JAXBTYPE>
 *        The XMLDSig implementation class to be written
 */
@NotThreadSafe
public class XMLDSigWriterBuilder <JAXBTYPE> extends JAXBWriterBuilder <JAXBTYPE, XMLDSigWriterBuilder <JAXBTYPE>>
{
  public XMLDSigWriterBuilder (@Nonnull final XMLDSigDocumentType eDocType)
  {
    super (eDocType);
    final MapBasedNamespaceContext aNSCtx = new MapBasedNamespaceContext ();
    aNSCtx.addMapping ("ds", CAS4.DS_NS);
    setNamespaceContext (aNSCtx);
  }

  @Nonnull
  public static XMLDSigWriterBuilder <ReferenceType> dsigReference ()
  {
    return new XMLDSigWriterBuilder<> (XMLDSigDocumentType.REFERENCE);
  }
}
