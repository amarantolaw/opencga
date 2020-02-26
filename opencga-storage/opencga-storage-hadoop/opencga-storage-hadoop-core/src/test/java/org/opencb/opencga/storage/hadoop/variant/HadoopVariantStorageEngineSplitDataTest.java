package org.opencb.opencga.storage.hadoop.variant;

import org.junit.*;
import org.junit.internal.matchers.ThrowableCauseMatcher;
import org.opencb.biodata.models.variant.Variant;
import org.opencb.commons.datastore.core.Query;
import org.opencb.commons.datastore.core.QueryOptions;
import org.opencb.opencga.storage.core.exceptions.StorageEngineException;
import org.opencb.opencga.storage.core.exceptions.StoragePipelineException;
import org.opencb.opencga.storage.core.metadata.VariantStorageMetadataManager;
import org.opencb.opencga.storage.core.metadata.models.SampleMetadata;
import org.opencb.opencga.storage.core.metadata.models.TaskMetadata;
import org.opencb.opencga.storage.core.variant.VariantStorageBaseTest;
import org.opencb.opencga.storage.core.variant.VariantStorageOptions;
import org.opencb.opencga.storage.core.variant.adaptors.VariantQueryParam;
import org.opencb.opencga.storage.core.variant.annotation.DefaultVariantAnnotationManager;
import org.opencb.opencga.storage.hadoop.variant.adaptors.VariantHadoopDBAdaptor;
import org.opencb.opencga.storage.hadoop.variant.index.sample.SampleIndexDBAdaptor;
import org.opencb.opencga.storage.hadoop.variant.index.sample.SampleIndexEntry;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.internal.matchers.ThrowableCauseMatcher.*;
import static org.junit.internal.matchers.ThrowableMessageMatcher.hasMessage;

public class HadoopVariantStorageEngineSplitDataTest extends VariantStorageBaseTest implements HadoopVariantStorageTest {

    public static final List<String> SAMPLES = Arrays.asList("NA19600", "NA19660", "NA19661", "NA19685");

    @ClassRule
    public static HadoopExternalResource externalResource = new HadoopExternalResource();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        VariantHbaseTestUtils.printVariants(getVariantStorageEngine().getDBAdaptor(), newOutputUri());
    }

    @Test
    public void testMultiChromosomeSplitData() throws Exception {

        variantStorageEngine.getOptions().put(VariantStorageOptions.STUDY.key(), STUDY_NAME);
        variantStorageEngine.index(Collections.singletonList(getResourceUri("by_chr/chr20.variant-test-file.vcf.gz")),
                outputUri, true, true, true);

        VariantStorageMetadataManager mm = variantStorageEngine.getMetadataManager();
        int studyId = mm.getStudyId(STUDY_NAME);
        for (String sample : SAMPLES) {
            SampleMetadata sampleMetadata = mm.getSampleMetadata(studyId, mm.getSampleId(studyId, sample));
            assertEquals(TaskMetadata.Status.READY, sampleMetadata.getIndexStatus());
            assertEquals(TaskMetadata.Status.NONE, sampleMetadata.getAnnotationStatus());
            assertEquals(TaskMetadata.Status.NONE, SampleIndexDBAdaptor.getSampleIndexStatus(sampleMetadata));
        }

        variantStorageEngine.annotate(new Query(), new QueryOptions(DefaultVariantAnnotationManager.OUT_DIR, outputUri));
        for (String sample : SAMPLES) {
            SampleMetadata sampleMetadata = mm.getSampleMetadata(studyId, mm.getSampleId(studyId, sample));
            assertEquals(TaskMetadata.Status.READY, sampleMetadata.getIndexStatus());
            assertEquals(TaskMetadata.Status.READY, sampleMetadata.getAnnotationStatus());
            assertEquals(TaskMetadata.Status.READY, SampleIndexDBAdaptor.getSampleIndexStatus(sampleMetadata));
        }

        variantStorageEngine.getOptions().put(VariantStorageOptions.STUDY.key(), STUDY_NAME);
        variantStorageEngine.getOptions().put(VariantStorageOptions.LOAD_SPLIT_DATA.key(), true);
        variantStorageEngine.index(Collections.singletonList(getResourceUri("by_chr/chr21.variant-test-file.vcf.gz")),
                outputUri, true, true, true);

        for (String sample : SAMPLES) {
            SampleMetadata sampleMetadata = mm.getSampleMetadata(studyId, mm.getSampleId(studyId, sample));
            assertEquals(TaskMetadata.Status.READY, sampleMetadata.getIndexStatus());
            assertEquals(TaskMetadata.Status.NONE, sampleMetadata.getAnnotationStatus());
            assertEquals(TaskMetadata.Status.NONE, SampleIndexDBAdaptor.getSampleIndexStatus(sampleMetadata));
        }
        variantStorageEngine.annotate(new Query(), new QueryOptions(DefaultVariantAnnotationManager.OUT_DIR, outputUri));
        for (String sample : SAMPLES) {
            SampleMetadata sampleMetadata = mm.getSampleMetadata(studyId, mm.getSampleId(studyId, sample));
            assertEquals(TaskMetadata.Status.READY, sampleMetadata.getIndexStatus());
            assertEquals(TaskMetadata.Status.READY, sampleMetadata.getAnnotationStatus());
            assertEquals(TaskMetadata.Status.READY, SampleIndexDBAdaptor.getSampleIndexStatus(sampleMetadata));
        }


        variantStorageEngine.getOptions().put(VariantStorageOptions.STUDY.key(), STUDY_NAME);
        variantStorageEngine.getOptions().put(VariantStorageOptions.LOAD_SPLIT_DATA.key(), true);
        variantStorageEngine.index(Collections.singletonList(getResourceUri("by_chr/chr22.variant-test-file.vcf.gz")),
                outputUri, true, true, true);

        for (Variant variant : variantStorageEngine) {
            String expectedFile = "chr" + variant.getChromosome() + ".variant-test-file.vcf.gz";
            assertEquals(1, variant.getStudies().get(0).getFiles().size());
            assertEquals(expectedFile, variant.getStudies().get(0).getFiles().get(0).getFileId());
            if (variant.getChromosome().equals("20") || variant.getChromosome().equals("21")) {
                Assert.assertNotNull(variant.getAnnotation().getConsequenceTypes());
                Assert.assertFalse(variant.getAnnotation().getConsequenceTypes().isEmpty());
            } else {
                assertTrue(variant.getAnnotation() == null || variant.getAnnotation().getConsequenceTypes().isEmpty());
            }
        }

        variantStorageEngine.forEach(new Query(VariantQueryParam.FILE.key(), "chr21.variant-test-file.vcf.gz"), variant -> {
            assertEquals("21", variant.getChromosome());
            String expectedFile = "chr" + variant.getChromosome() + ".variant-test-file.vcf.gz";
            assertEquals(1, variant.getStudies().get(0).getFiles().size());
            assertEquals(expectedFile, variant.getStudies().get(0).getFiles().get(0).getFileId());
        }, QueryOptions.empty());
    }

    @Test
    public void testMultiChromosomeFail() throws Exception {
        URI outDir = newOutputUri();

        variantStorageEngine.getOptions().put(VariantStorageOptions.STUDY.key(), STUDY_NAME);
        variantStorageEngine.index(Collections.singletonList(getResourceUri("by_chr/chr20.variant-test-file.vcf.gz")),
                outDir, true, true, true);

        variantStorageEngine.getOptions().put(VariantStorageOptions.STUDY.key(), STUDY_NAME);
        variantStorageEngine.getOptions().put(VariantStorageOptions.LOAD_SPLIT_DATA.key(), false);

        StorageEngineException expected = StorageEngineException.alreadyLoadedSamples("chr21.variant-test-file.vcf.gz", SAMPLES);
//        thrown.expectMessage(expected.getMessage());
        thrown.expectCause(isA(expected.getClass()));
        thrown.expectCause(hasMessage(is(expected.getMessage())));
        variantStorageEngine.index(Collections.singletonList(getResourceUri("by_chr/chr21.variant-test-file.vcf.gz")),
                outDir, true, true, true);
    }

    @Test
    public void testDuplicatedVariantsFail() throws Exception {
        URI outDir = newOutputUri();

        variantStorageEngine.getOptions().put(VariantStorageOptions.LOAD_SPLIT_DATA.key(), true);
        variantStorageEngine.getOptions().put(VariantStorageOptions.STUDY.key(), STUDY_NAME);
        variantStorageEngine.index(Collections.singletonList(getResourceUri("by_chr/chr20.variant-test-file.vcf.gz")),
                outDir, true, true, true);

        variantStorageEngine.index(Collections.singletonList(getResourceUri("by_chr/chr21.variant-test-file.vcf.gz")),
                outputUri, true, true, true);


        thrown.expect(StoragePipelineException.class);
        thrown.expect(hasCause(isA(StorageEngineException.class)));
        thrown.expect(hasCause(hasCause(isA(ExecutionException.class))));
        thrown.expect(hasCause(hasCause(hasCause(isA(IllegalArgumentException.class)))));
        thrown.expect(hasCause(hasCause(hasCause(hasMessage(containsString("Already loaded variant 20:238441:T:C"))))));
        variantStorageEngine.index(Collections.singletonList(getResourceUri("by_chr/chr20-21.variant-test-file.vcf.gz")),
                outDir, true, true, true);
    }


    @Test
    public void testLoadByRegionFail() throws Exception {
        URI outDir = newOutputUri();

        VariantStorageMetadataManager mm = variantStorageEngine.getMetadataManager();

        variantStorageEngine.getOptions().put(VariantStorageOptions.STUDY.key(), STUDY_NAME + "_split");
        variantStorageEngine.index(Collections.singletonList(getResourceUri("by_chr/chr22_1-1.variant-test-file.vcf.gz")),
                outDir, true, true, true);

        int studyId_split = mm.getStudyId(STUDY_NAME + "_split");

//        variantStorageEngine.annotate(new Query(), new QueryOptions(DefaultVariantAnnotationManager.OUT_DIR, outputUri));
//        for (String sample : SAMPLES) {
//            SampleMetadata sampleMetadata = mm.getSampleMetadata(studyId_split, mm.getSampleId(studyId_split, sample));
//            assertEquals(TaskMetadata.Status.READY, sampleMetadata.getIndexStatus());
//            assertEquals(TaskMetadata.Status.READY, sampleMetadata.getAnnotationStatus());
//            assertEquals(TaskMetadata.Status.READY, SampleIndexDBAdaptor.getSampleIndexStatus(sampleMetadata));
//        }

        variantStorageEngine.getOptions().put(VariantStorageOptions.LOAD_SPLIT_DATA.key(), true);
        variantStorageEngine.index(Collections.singletonList(getResourceUri("by_chr/chr22_1-2.variant-test-file.vcf.gz")),
                outputUri, true, true, true);


        for (String sample : SAMPLES) {
            SampleMetadata sampleMetadata = mm.getSampleMetadata(studyId_split, mm.getSampleId(studyId_split, sample));
            assertEquals(TaskMetadata.Status.READY, sampleMetadata.getIndexStatus());
            assertEquals(TaskMetadata.Status.NONE, sampleMetadata.getAnnotationStatus());
            assertEquals(TaskMetadata.Status.NONE, SampleIndexDBAdaptor.getSampleIndexStatus(sampleMetadata));
        }

        variantStorageEngine.getOptions().put(VariantStorageOptions.LOAD_SPLIT_DATA.key(), false);
        variantStorageEngine.getOptions().put(VariantStorageOptions.STUDY.key(), STUDY_NAME);
        variantStorageEngine.index(Collections.singletonList(getResourceUri("by_chr/chr22.variant-test-file.vcf.gz")),
                outputUri, true, true, true);

        int studyId_normal = mm.getStudyId(STUDY_NAME);
        for (String sample : SAMPLES) {
            SampleMetadata sampleMetadata = mm.getSampleMetadata(studyId_normal, mm.getSampleId(studyId_normal, sample));
            assertEquals(TaskMetadata.Status.READY, sampleMetadata.getIndexStatus());
            assertEquals(TaskMetadata.Status.NONE, sampleMetadata.getAnnotationStatus());
            assertEquals(TaskMetadata.Status.NONE, SampleIndexDBAdaptor.getSampleIndexStatus(sampleMetadata));
        }

        VariantHadoopDBAdaptor dbAdaptor = (VariantHadoopDBAdaptor) variantStorageEngine.getDBAdaptor();
        SampleIndexDBAdaptor sampleIndexDBAdaptor = new SampleIndexDBAdaptor(dbAdaptor.getHBaseManager(), dbAdaptor.getTableNameGenerator(), dbAdaptor.getMetadataManager());

        for (Integer sampleId : mm.getIndexedSamples(studyId_normal)) {
            Iterator<SampleIndexEntry> itExp = sampleIndexDBAdaptor.rawIterator(studyId_normal, sampleId);
            Iterator<SampleIndexEntry> itAct = sampleIndexDBAdaptor.rawIterator(studyId_split, sampleId);

            while (itExp.hasNext()) {
                assertTrue(itAct.hasNext());
                SampleIndexEntry exp = itExp.next();
                SampleIndexEntry act = itAct.next();
                for (String gt : exp.getGts().keySet()) {
                    assertEquals(gt, exp.getGtEntry(gt), act.getGtEntry(gt));
                }
                assertEquals(exp, act);
            }
            assertFalse(itAct.hasNext());
        }

    }
}
