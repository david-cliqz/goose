/**
 * Licensed to Gravity.com under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  Gravity.com licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gravity.goose

import network.{HtmlFetcher, AbstractHtmlFetcher}
import org.jsoup.nodes.Element
import java.util.Date
import reflect.BeanProperty
import com.gravity.goose.extractors.{StandardContentExtractor, ContentExtractor, AdditionalDataExtractor, PublishDateExtractor}


/**
 * Created by Jim Plush
 * User: jim
 * Date: 8/16/11
 */

/**
 * @param localStoragePath this is the local storage path used to place images
 *                         to inspect them, should be writable
 * @param minBytesForImages What's the minimum bytes for an image we'd accept
 *                          is, alot of times we want to filter out the author's
 *                          little images in the beginning of the article
 * @param enableImageFetching set this guy to false if you don't care about
 *                            getting images, otherwise you can either use the
 *                            default image extractor to implement the
 *                            ImageExtractor interface to build your own
 * @param imagemagickConvertPath path to your imagemagick convert executable, on
 *                               the mac using mac ports this is the default
 *                               listed (Note: not on Linux...)
 * @param imagemagickIdentifyPath path to your imagemagick identify executable
 * @param connectionTimeout Connection timeout for the crawler.
 * @param socketTimeout Socket timeout for the crawler.
 * @param browserUserAgent used as the user agent that is sent with your web
 *                         requests to extract an article
 * @param publishDateExtractor Pass in to extract article publish dates.
 * @param additionalDataExtractor Pass in to extract any additional data not
 *                                defined within {@link Article}.
 */
class Configuration(
    @BeanProperty
    var localStoragePath: String = "/tmp/goose",
    @BeanProperty
    var minBytesForImages: Int = 4500,
    @BeanProperty
    var enableImageFetching: Boolean = true,
    @BeanProperty
    var imagemagickConvertPath: String = "/opt/local/bin/convert",
    @BeanProperty
    var imagemagickIdentifyPath: String = "/opt/local/bin/identify",
    @BeanProperty
    var connectionTimeout: Int = 10000,
    @BeanProperty
    var socketTimeout: Int = 10000,
    @BeanProperty
    var browserUserAgent: String =
      "Mozilla/5.0 (X11; U; Linux x86_64; de; rv:1.9.2.8) " +
      "Gecko/20100723 Ubuntu/10.04 (lucid) Firefox/3.6.8",
    var contentExtractor: ContentExtractor = StandardContentExtractor,
    var publishDateExtractor: PublishDateExtractor = new PublishDateExtractor {
      def extract(rootElement: Element): Date = {
        null
      }
    },
    var additionalDataExtractor: AdditionalDataExtractor =
      new AdditionalDataExtractor,
    var htmlFetcher: AbstractHtmlFetcher = HtmlFetcher) {

  /**
   * Default constructor for Java interoperability. See
   * https://issues.scala-lang.org/browse/SI-4278 why it looks like this. :(
   */
  def this() = this(minBytesForImages = 4500)

  def setContentExtractor(extractor: ContentExtractor) {
    if (extractor == null) throw new IllegalArgumentException("extractor must not be null!")
    contentExtractor = extractor
  }

  def getPublishDateExtractor: PublishDateExtractor = {
    publishDateExtractor
  }

  /**
  * Pass in to extract article publish dates.
  * @param extractor a concrete instance of {@link PublishDateExtractor}
  * @throws IllegalArgumentException if the instance passed in is <code>null</code>
  */
  def setPublishDateExtractor(extractor: PublishDateExtractor) {
    if (extractor == null) throw new IllegalArgumentException("extractor must not be null!")
    this.publishDateExtractor = extractor
  }

  def getAdditionalDataExtractor: AdditionalDataExtractor = {
    additionalDataExtractor
  }

  /**
  * Pass in to extract any additional data not defined within {@link Article}
  * @param extractor a concrete instance of {@link AdditionalDataExtractor}
  * @throws IllegalArgumentException if the instance passed in is <code>null</code>
  */
  def setAdditionalDataExtractor(extractor: AdditionalDataExtractor) {
    this.additionalDataExtractor = extractor
  }

  def setHtmlFetcher(fetcher: AbstractHtmlFetcher) {
    require(fetcher != null, "fetcher MUST NOT be null!")
    this.htmlFetcher = fetcher
  }

  def getHtmlFetcher: AbstractHtmlFetcher = htmlFetcher

}
