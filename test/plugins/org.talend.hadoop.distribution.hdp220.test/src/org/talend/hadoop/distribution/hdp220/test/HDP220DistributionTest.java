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
package org.talend.hadoop.distribution.hdp220.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.talend.hadoop.distribution.EHadoopVersion;
import org.talend.hadoop.distribution.component.HBaseComponent;
import org.talend.hadoop.distribution.component.HCatalogComponent;
import org.talend.hadoop.distribution.component.HDFSComponent;
import org.talend.hadoop.distribution.component.HadoopComponent;
import org.talend.hadoop.distribution.component.HiveComponent;
import org.talend.hadoop.distribution.component.ImpalaComponent;
import org.talend.hadoop.distribution.component.MRComponent;
import org.talend.hadoop.distribution.component.PigComponent;
import org.talend.hadoop.distribution.component.SparkBatchComponent;
import org.talend.hadoop.distribution.component.SparkStreamingComponent;
import org.talend.hadoop.distribution.component.SqoopComponent;
import org.talend.hadoop.distribution.hdp220.HDP220Distribution;

/**
 * Test class for the {@link HDP220Distribution} distribution.
 *
 */
public class HDP220DistributionTest {

    private final static String YARN_APPLICATION_CLASSPATH = "$HADOOP_CONF_DIR,/usr/hdp/current/hadoop-client/*,/usr/hdp/current/hadoop-client/lib/*,/usr/hdp/current/hadoop-hdfs-client/*,/usr/hdp/current/hadoop-hdfs-client/lib/*,/usr/hdp/current/hadoop-mapreduce-client/*,/usr/hdp/current/hadoop-mapreduce-client/lib/*,/usr/hdp/current/hadoop-yarn-client/*,/usr/hdp/current/hadoop-yarn-client/lib/*"; //$NON-NLS-1$

    @Test
    public void testHDP220Distribution() throws Exception {
        HadoopComponent distribution = new HDP220Distribution();
        assertNotNull(distribution.getDistributionName());
        assertNotNull(distribution.getVersionName(null));
        assertTrue(distribution.doSupportS3());
        assertEquals(HDP220Distribution.DISTRIBUTION_NAME, distribution.getDistribution());
        assertEquals(HDP220Distribution.VERSION, distribution.getVersion());
        assertEquals(EHadoopVersion.HADOOP_2, distribution.getHadoopVersion());
        assertTrue(distribution.doSupportKerberos());
        assertTrue(distribution.doSupportUseDatanodeHostname());
        assertFalse(distribution.doSupportGroup());
        assertTrue(distribution.doSupportOldImportMode());
        assertTrue(((HDFSComponent) distribution).doSupportSequenceFileShortType());
        assertFalse(((MRComponent) distribution).isExecutedThroughWebHCat());
        assertTrue(((MRComponent) distribution).doSupportCrossPlatformSubmission());
        assertTrue(((MRComponent) distribution).doSupportImpersonation());
        assertEquals(((MRComponent) distribution).getYarnApplicationClasspath(), YARN_APPLICATION_CLASSPATH);
        assertTrue(((HBaseComponent) distribution).doSupportNewHBaseAPI());
        assertTrue(((SqoopComponent) distribution).doJavaAPISupportStorePasswordInFile());
        assertTrue(((SqoopComponent) distribution).doJavaAPISqoopImportSupportDeleteTargetDir());
        assertTrue(((SqoopComponent) distribution).doJavaAPISqoopImportAllTablesSupportExcludeTable());
        assertTrue(((PigComponent) distribution).doSupportHCatalog());
        assertFalse(((PigComponent) distribution).pigVersionPriorTo_0_12());
        assertTrue(((PigComponent) distribution).doSupportHBase());
        assertTrue(((PigComponent) distribution).doSupportTezForPig());
        assertTrue(((HiveComponent) distribution).doSupportEmbeddedMode());
        assertTrue(((HiveComponent) distribution).doSupportStandaloneMode());
        assertTrue(((HiveComponent) distribution).doSupportHive1());
        assertTrue(((HiveComponent) distribution).doSupportHive2());
        assertTrue(((HiveComponent) distribution).doSupportTezForHive());
        assertTrue(((HiveComponent) distribution).doSupportHBaseForHive());
        assertTrue(((HiveComponent) distribution).doSupportSSL());
        assertTrue(((HiveComponent) distribution).doSupportORCFormat());
        assertTrue(((HiveComponent) distribution).doSupportAvroFormat());
        assertTrue(((HiveComponent) distribution).doSupportParquetFormat());
        assertFalse(((HiveComponent) distribution).doSupportStoreAsParquet());
        assertFalse(((HiveComponent) distribution).doSupportClouderaNavigator());

        assertFalse(distribution instanceof SparkBatchComponent);
        assertFalse(distribution instanceof SparkStreamingComponent);
        assertTrue(distribution instanceof HCatalogComponent);
        assertFalse(distribution instanceof ImpalaComponent);
    }

}
