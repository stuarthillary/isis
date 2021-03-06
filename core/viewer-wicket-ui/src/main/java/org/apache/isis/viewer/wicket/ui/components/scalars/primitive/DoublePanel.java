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

package org.apache.isis.viewer.wicket.ui.components.scalars.primitive;

import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DoubleConverter;

import org.apache.isis.viewer.wicket.model.models.ScalarModel;
import org.apache.isis.viewer.wicket.ui.components.scalars.ScalarPanelTextFieldNumeric;
import org.apache.isis.viewer.wicket.ui.components.scalars.TextFieldValueModel;

/**
 * Panel for rendering scalars of type {@link Double} or <tt>double</tt>.
 */
public class DoublePanel extends ScalarPanelTextFieldNumeric<Double> {

    private static final long serialVersionUID = 1L;

    public DoublePanel(final String id, final ScalarModel scalarModel) {
        super(id, scalarModel, Double.class, DoubleConverter.INSTANCE);
    }
    
    @Override
    protected AbstractTextComponent<Double> createTextFieldForRegular(final String id) {
        final TextFieldValueModel<Double> textFieldValueModel = new TextFieldValueModel<>(this);
        return new TextField<Double>(id, textFieldValueModel, Double.class) {
            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unchecked")
            @Override
            public <C> IConverter<C> getConverter(Class<C> type) {
                return (IConverter<C>) (type == Double.class? DoubleConverter.INSTANCE: super.getConverter(type));
            }
        };
    }

    @Override
    protected String getScalarPanelType() {
        return "doublePanel";
    }

}
