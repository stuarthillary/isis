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
package org.apache.isis.core.metamodel.services.i18n.po;

import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.services.i18n.LocaleProvider;
import org.apache.isis.applib.services.i18n.TranslationService;
import org.apache.isis.applib.services.i18n.UrlResolver;

/**
 * Not annotated with &#64;DomainService, but is registered as a fallback by <tt>ServicesInstallerFallback</tt>.
 */
public class TranslationServicePo implements TranslationService {

    public static Logger LOG = LoggerFactory.getLogger(TranslationServicePo.class);

    public static final String KEY_DEPLOYMENT_TYPE = "isis.deploymentType";
    public static final String KEY_PO_MODE = "isis.services.translation.po.mode";

    private boolean prototype;

    private PoAbstract po;

    /**
     * Defaults to writer mode because the service isn't available while the metamodel is bring instantiated,
     * however we want to force the translations to be cached in case required later.
     */
    public TranslationServicePo() {
        po = new PoWriter(this);
    }

    //region > init, shutdown

    @Programmatic
    @PostConstruct
    public void init(final Map<String,String> config) {

        final String deploymentType = config.get(KEY_DEPLOYMENT_TYPE);
        prototype = deploymentType==null ||
                    deploymentType.toLowerCase().contains("prototype") ||
                    deploymentType.toLowerCase().contains("test") ;

        String translationMode = config.get(KEY_PO_MODE);
        final boolean forceRead =
                translationMode != null &&
                        ("read".equalsIgnoreCase(translationMode) ||
                         "reader".equalsIgnoreCase(translationMode));

        if (!prototype || forceRead) {
            final PoReader poReader = new PoReader(this);
            poReader.init(config);
            po = poReader;
        }
    }

    @Programmatic
    @PreDestroy
    public void shutdown() {
        po.shutdown();
    }
    //endregion

    boolean isPrototype() {
        return prototype;
    }


    @Override
    @Programmatic
    public String translate(final String context, final String text) {
        return po.translate(context, text);
    }

    @Override
    public String translate(final String context, final String singularText, final String pluralText, final int num) {
        return po.translate(context, singularText, pluralText, num);
    }

    @Override
    public Mode getMode() {
        return po.getMode();
    }

    /**
     * Not API
     */
    public String toPo() {
        if (!prototype) {
            throw new IllegalStateException("Not in prototype mode");
        }
        return  ((PoWriter)po).toPot();
    }

    @Inject
    private
    UrlResolver urlResolver;

    @Inject
    private
    LocaleProvider localeProvider;


    @Programmatic
    public UrlResolver getUrlResolver() {
        return urlResolver;
    }

    @Programmatic
    public LocaleProvider getLocaleProvider() {
        return localeProvider;
    }
}
