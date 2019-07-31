package org.opencb.opencga.catalog.managers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.opencb.biodata.models.commons.Disorder;
import org.opencb.biodata.models.pedigree.IndividualProperty;
import org.opencb.commons.datastore.core.QueryOptions;
import org.opencb.commons.test.GenericTest;
import org.opencb.opencga.catalog.exceptions.CatalogException;
import org.opencb.opencga.core.models.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

public class AbstractClinicalManagerTest extends GenericTest {

    public final static String PASSWORD = "password";
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public CatalogManagerExternalResource catalogManagerResource = new CatalogManagerExternalResource();

    public CatalogManager catalogManager;
    public String token;
    public String studyFqn;
    public Family family;
    public ClinicalAnalysis clinicalAnalysis;

    @Before
    public void setUp() throws IOException, CatalogException, URISyntaxException {
        catalogManager = catalogManagerResource.getCatalogManager();
        setUpCatalogManager(catalogManager);
    }

    public void setUpCatalogManager(CatalogManager catalogManager) throws IOException, CatalogException, URISyntaxException {

        catalogManager.getUserManager().create("user", "User Name", "mail@ebi.ac.uk", PASSWORD, "", null, Account.Type.FULL, null, null);

        token = catalogManager.getUserManager().login("user", PASSWORD);

        catalogManager.getProjectManager().create("1000G", "Project about some genomes", "", "ACME", "Homo sapiens", null, null, "GRCh38",
                new QueryOptions(), token);

        Study study = catalogManager.getStudyManager().create("1000G", "phase1", null, "Phase 1", Study.Type.TRIO, null, "Done", null, null,
                null, null, null, null, null, null, token).first();
        studyFqn = study.getFqn();

        URI familyVCF = getClass().getResource("/biofiles/family.vcf").toURI();

        catalogManager.getFileManager().upload(studyFqn, new FileInputStream(new java.io.File(familyVCF)),
                new File().setPath(Paths.get(familyVCF).getFileName().toString()), false, true, token);

        family = catalogManager.getFamilyManager().create(studyFqn, getFamily(), QueryOptions.empty(), token).first();

        // Clinical analysis
        ClinicalAnalysis auxClinicalAnalysis = new ClinicalAnalysis()
                .setId("analysis").setDescription("My description").setType(ClinicalAnalysis.Type.FAMILY)
                .setDueDate("20180510100000")
                .setDisorder(getDisorder())
                .setProband(getChild())
                .setFamily(getFamily());

        clinicalAnalysis = catalogManager.getClinicalAnalysisManager().create(studyFqn, auxClinicalAnalysis, QueryOptions.empty(), token)
                .first();

    }

    private Individual getMother() {
        return new Individual().setId("mother")
                .setDisorders(Collections.emptyList())
                .setSamples(Collections.singletonList(new Sample().setId("s2")));
    }

    private Individual getFather() {
        return new Individual().setId("father")
                .setDisorders(Collections.singletonList(getDisorder()))
                .setSamples(Collections.singletonList(new Sample().setId("s1")));
    }

    private Individual getChild() {
        return new Individual().setId("child")
                .setDisorders(Collections.singletonList(getDisorder()))
                .setFather(getFather())
                .setMother(getMother())
                .setSex(IndividualProperty.Sex.MALE)
                .setSamples(Collections.singletonList(new Sample().setId("s3")));
    }

    private Family getFamily () {
        return new Family("family", "family", null, null, Arrays.asList(getChild(), getFather(), getMother()), "", 3,
                Collections.emptyList(), Collections.emptyMap());
    }

    private Disorder getDisorder() {
        return new Disorder("disorder", "Disorder", "source", "description", null, null);
    }

}