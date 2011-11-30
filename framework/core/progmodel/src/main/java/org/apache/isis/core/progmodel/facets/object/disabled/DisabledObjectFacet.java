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

package org.apache.isis.core.progmodel.facets.object.disabled;

import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.facetapi.Facet;
import org.apache.isis.core.metamodel.facets.object.immutable.ImmutableFacet;
import org.apache.isis.core.metamodel.interactions.DisablingInteractionAdvisor;

/**
 * Mechanism for determining whether this object is should be disabled.
 * 
 * <p>
 * Even though all the properties of an object may themselves be enabled, there could be reasons to disable the object.
 * 
 * <p>
 * In the standard Apache Isis Programming Model, typically corresponds to the <tt>disabled</tt> method.
 * 
 * @see ImmutableFacet
 */
public interface DisabledObjectFacet extends Facet, DisablingInteractionAdvisor {

    /**
     * The reason the object is disabled.
     * 
     * <p>
     * . If the object is actually enabled, should return <tt>null</tt>.
     */
    public String disabledReason(ObjectAdapter adapter);

}
