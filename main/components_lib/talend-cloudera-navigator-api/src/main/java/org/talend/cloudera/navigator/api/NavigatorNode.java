// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.cloudera.navigator.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by pbailly on 16 Oct 2015 Detailled comment
 *
 */
public class NavigatorNode {

    private String name;

    private Map<String, String> schema;

    private Map<String, String> metadata;

    private List<String> inputNodes;

    private List<String> outputNodes;

    /**
     *
     * @param name
     * @param schema
     * @param inputNodes
     * @param outputNodes
     */
    public NavigatorNode(String name, Map<String, String> schema, List<String> inputNodes,
                         List<String> outputNodes) {
        this(name, schema, new HashMap<>(), inputNodes, outputNodes);
    }

    /**
     *
     * @param name
     * @param schema
     * @param inputNodes
     * @param outputNodes
     * @param metadata
     */
    public NavigatorNode(String name, Map<String, String> schema, Map<String, String> metadata,
                         List<String> inputNodes, List<String> outputNodes) {
        this.name = name;
        this.schema = schema;
        this.metadata = metadata;
        this.inputNodes = inputNodes;
        this.outputNodes = outputNodes;
    }

    public List<String> getInputNodes() {
        return this.inputNodes;
    }

    public void setInputNodes(List<String> inputNodes) {
        this.inputNodes = inputNodes;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getOutputNodes() {
        return this.outputNodes;
    }

    public void setOutputNodes(List<String> outputNodes) {
        this.outputNodes = outputNodes;
    }

    public Map<String, String> getSchema() {
        return this.schema;
    }

    public void setSchema(Map<String, String> schema) {
        this.schema = schema;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "[" + name + "|" + schema + "|" + metadata + "|" + inputNodes + "|" + outputNodes + "]";
    }
}
