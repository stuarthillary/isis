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

import java.util.SortedMap;
import java.util.SortedSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.isis.applib.services.i18n.TranslationService;

class PoWriter extends PoAbstract {

    public static Logger LOG = LoggerFactory.getLogger(PoWriter.class);

    private static class Block {
        private final String msgId;
        private final SortedSet<String> contexts = Sets.newTreeSet();
        private String msgIdPlural;

        private Block(final String msgId) {
            this.msgId = msgId;
        }
    }

    private final SortedMap<String, Block> blocksByMsgId = Maps.newTreeMap();

    public PoWriter(final TranslationServicePo translationServicePo) {
        super(translationServicePo, TranslationService.Mode.WRITE);
    }

    //region > shutdown

    @Override
    void shutdown() {
        final StringBuilder buf = new StringBuilder();
        buf.append("\n");
        buf.append("\n##############################################################################");
        buf.append("\n#");
        buf.append("\n# .pot file");
        buf.append("\n#");
        buf.append("\n# generated at: ").append(LocalDateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        buf.append("\n# generated by: ").append(TranslationServicePo.class.getSimpleName());
        buf.append("\n#");
        buf.append("\n# Translate this file to each required language and place in WEB-INF, eg:");
        buf.append("\n#");
        buf.append("\n#     /WEB-INF/translations_en-US.po");
        buf.append("\n#     /WEB-INF/translations_en.po");
        buf.append("\n#     /WEB-INF/translations_fr-FR.po");
        buf.append("\n#     /WEB-INF/translations_fr.po");
        buf.append("\n#     /WEB-INF/translations.po");
        buf.append("\n#");
        buf.append("\n# If the app uses TranslatableString (eg for internationalized validation");
        buf.append("\n# messages), or if the app calls the TranslationService directly, then ensure");
        buf.append("\n# that all text to be translated has been captured by running a full");
        buf.append("\n# integration test suite that fully exercises all behaviour");
        buf.append("\n#");
        buf.append("\n##############################################################################");
        buf.append("\n");
        buf.append("\n");
        buf.append(toPot());
        buf.append("\n");
        buf.append("\n");
        buf.append("\n##############################################################################");
        buf.append("\n# end of .pot file");
        buf.append("\n##############################################################################");
        buf.append("\n");
        LOG.info(buf.toString());
    }
    //endregion


    public String translate(final String context, final String msgId) {

        final Block block = blockFor(msgId);
        block.contexts.add(context);

        return msgId;
    }

    @Override
    String translate(final String context, final String msgId, final String msgIdPlural, final int num) {

        final Block block = blockFor(msgId);
        block.contexts.add(context);
        block.msgIdPlural = msgIdPlural;

        return null;
    }

    private Block blockFor(final String msgId) {
        Block block = blocksByMsgId.get(msgId);
        if(block == null) {
            block = new Block(msgId);
            blocksByMsgId.put(msgId, block);
        }
        return block;
    }

    /**
     * Not API
     */
    String toPot() {
        final StringBuilder buf = new StringBuilder();
        for (final String msgId : blocksByMsgId.keySet()) {
            final Block block = blocksByMsgId.get(msgId);
            for (final String context : block.contexts) {
                buf.append("#: ").append(context).append("\n");
            }
            buf.append("msgid \"").append(msgId).append("\"\n");
            if(block.msgIdPlural == null) {
                buf.append("msgstr \"\"\n");
            } else {
                buf.append("msgid_plural \"").append(block.msgIdPlural).append("\"\n");
                buf.append("msgstr[0] \"\"\n");
                buf.append("msgstr[1] \"\"\n");
            }
            buf.append("\n\n");
        }
        return buf.toString();
    }

}
