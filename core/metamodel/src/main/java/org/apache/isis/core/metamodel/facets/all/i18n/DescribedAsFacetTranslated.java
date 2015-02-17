/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.isis.core.metamodel.facets.all.i18n;

import org.apache.isis.applib.services.i18n.TranslationService;
import org.apache.isis.core.metamodel.facetapi.FacetAbstract;
import org.apache.isis.core.metamodel.facetapi.IdentifiedHolder;
import org.apache.isis.core.metamodel.facets.all.describedas.DescribedAsFacet;

public class DescribedAsFacetTranslated extends FacetAbstract implements DescribedAsFacet{

    private final String context;
    private final String originalText;
    private final TranslationService translationService;

    private String value;

    public DescribedAsFacetTranslated(
            final String context, final String originalText,
            final TranslationService translationService,
            final IdentifiedHolder holder) {
        super(DescribedAsFacet.class, holder, Derivation.NOT_DERIVED);
        this.context = context;
        this.originalText = originalText;
        this.translationService = translationService;
    }

    @Override
    public String value() {
        // this strange algorithm is because the translationService's mode changes
        // between the time the metamodel is first built and when it is subsequently
        // used.  We can't distinguish (when in write mode) as to whether it is
        // because we are in startup (prior to init'ing the services) or whether in
        // prototype mode.  We therefore never cache if in write mode (this ensures that
        // the PoWriter gets to see the translation request) but do then start caching
        // if we find that we're in read mode (after init of the TranslationServicePo).
        switch (translationService.getMode()) {
            case WRITE:
                return translated();
            case READ:
                // don't cache
                if(value == null) {
                    value = translated();
                }
                break;
        }
        return value;
    }

    private String translated() {
        return translationService.translate(context, originalText);
    }
}
