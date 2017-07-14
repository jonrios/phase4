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
package com.helger.as4.server.standalone;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.photon.jetty.JettyStopper;

/**
 * @author Philip Helger
 */
public final class JettyStopAS4TEST
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (JettyStopAS4TEST.class);

  public static void main (final String [] args) throws IOException
  {
    s_aLogger.info ("Stopping Jetty:8080");
    new JettyStopper ().run ();
  }
}
