package org.opencb.opencga.storage.core.search;

import org.apache.solr.client.solrj.beans.Field;

import java.util.*;

/**
 * Created by wasim on 09/11/16.
 */

/**
 *  I M P O R T A N T:
 *
 * In order to insert VariantSearch objects into your solr cores/collections you must
 * add the below fields in the the file schema.xml located in the core/collection folder.
 *
 <field name="dbSNP" type="string" indexed="true" stored="true" multiValued="false"/>
 <field name="chromosome" type="string" indexed="true" stored="true" multiValued="false"/>
 <field name="start" type="int" indexed="true" stored="true" multiValued="false"/>
 <field name="end" type="int" indexed="true" stored="true" multiValued="false"/>
 <field name="xrefs" type="string" indexed="true" stored="true" multiValued="true"/>
 <field name="type" type="string" indexed="true" stored="true" multiValued="false"/>
 <field name="studies" type="string" indexed="true" stored="true" multiValued="true"/>
 <field name="phastCons" type="double" indexed="true" stored="true" multiValued="false"/>
 <field name="phylop" type="double" indexed="true" stored="true" multiValued="false"/>
 <field name="gerp" type="double" indexed="true" stored="true" multiValued="false"/>
 <field name="caddRaw" type="double" indexed="true" stored="true" multiValued="false"/>
 <field name="caddScaled" type="double" indexed="true" stored="true" multiValued="false"/>
 <field name="sift" type="double" indexed="true" stored="true" multiValued="false"/>
 <field name="polyphen" type="double" indexed="true" stored="true" multiValued="false"/>
 <field name="genes" type="string" indexed="true" stored="true" multiValued="true"/>
 <field name="soAcc" type="int" indexed="true" stored="true" multiValued="true"/>
 <field name="geneToSoAcc" type="string" indexed="true" stored="true" multiValued="true"/>
 <field name="traits" type="text_en" indexed="true" stored="true" multiValued="true"/>
 <dynamicField name="popFreq_*" type="double" indexed="true" stored="true" multiValued="false"/>
 */

public class VariantSearchModel {

    @Field
    private String id;

    @Field("dbSNP")
    private String dbSNP;

    @Field("chromosome")
    private String chromosome;

    @Field("start")
    private int start;

    @Field("end")
    private int end;

    @Field("xrefs")
    private Set<String> xrefs;

    @Field("type")
    private String type;

    @Field("studies")
    private List<String> studies;

    @Field("phastCons")
    private double phastCons;

    @Field("phylop")
    private double phylop;

    @Field("gerp")
    private double gerp;

    @Field("caddRaw")
    private double caddRaw;

    @Field("caddScaled")
    private double caddScaled;

    @Field("sift")
    private double sift;

    @Field("polyphen")
    private double polyphen;

    @Field("genes")
    private List<String> genes;

    @Field("soAcc")
    private Set<Integer> soAcc;

    @Field("geneToSoAcc")
    private Set<String> geneToSoAcc;

    @Field("traits")
    private Set<String> traits;

    @Field("stats_*")
    private Map<String, Float> stats;

    @Field("popFreq_*")
    private Map<String, Float> popFreq;



    public VariantSearchModel() {
        this.genes = new ArrayList<>();
        this.soAcc = new HashSet<>();
        this.geneToSoAcc = new HashSet<>();
        this.popFreq = new HashMap<>();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VariantSearchModel{");
        sb.append("id='").append(id).append('\'');
        sb.append(", dbSNP='").append(dbSNP).append('\'');
        sb.append(", chromosome='").append(chromosome).append('\'');
        sb.append(", start=").append(start);
        sb.append(", end=").append(end);
        sb.append(", xrefs=").append(xrefs);
        sb.append(", type='").append(type).append('\'');
        sb.append(", studies=").append(studies);
        sb.append(", phastCons=").append(phastCons);
        sb.append(", phylop=").append(phylop);
        sb.append(", gerp=").append(gerp);
        sb.append(", caddRaw=").append(caddRaw);
        sb.append(", caddScaled=").append(caddScaled);
        sb.append(", sift=").append(sift);
        sb.append(", polyphen=").append(polyphen);
        sb.append(", genes=").append(genes);
        sb.append(", soAcc=").append(soAcc);
        sb.append(", geneToSoAcc=").append(geneToSoAcc);
        sb.append(", traits=").append(traits);
        sb.append(", stats=").append(stats);
        sb.append(", popFreq=").append(popFreq);
        sb.append('}');
        return sb.toString();
    }

    public String getId() {
        return id;
    }

    public VariantSearchModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getDbSNP() {
        return dbSNP;
    }

    public VariantSearchModel setDbSNP(String dbSNP) {
        this.dbSNP = dbSNP;
        return this;
    }

    public String getChromosome() {
        return chromosome;
    }

    public VariantSearchModel setChromosome(String chromosome) {
        this.chromosome = chromosome;
        return this;
    }

    public int getStart() {
        return start;
    }

    public VariantSearchModel setStart(int start) {
        this.start = start;
        return this;
    }

    public int getEnd() {
        return end;
    }

    public VariantSearchModel setEnd(int end) {
        this.end = end;
        return this;
    }

    public Set<String> getXrefs() {
        return xrefs;
    }

    public VariantSearchModel setXrefs(Set<String> xrefs) {
        this.xrefs = xrefs;
        return this;
    }

    public String getType() {
        return type;
    }

    public VariantSearchModel setType(String type) {
        this.type = type;
        return this;
    }

    public List<String> getStudies() {
        return studies;
    }

    public VariantSearchModel setStudies(List<String> studies) {
        this.studies = studies;
        return this;
    }

    public double getPhastCons() {
        return phastCons;
    }

    public VariantSearchModel setPhastCons(double phastCons) {
        this.phastCons = phastCons;
        return this;
    }

    public double getPhylop() {
        return phylop;
    }

    public VariantSearchModel setPhylop(double phylop) {
        this.phylop = phylop;
        return this;
    }

    public double getGerp() {
        return gerp;
    }

    public VariantSearchModel setGerp(double gerp) {
        this.gerp = gerp;
        return this;
    }

    public double getCaddRaw() {
        return caddRaw;
    }

    public VariantSearchModel setCaddRaw(double caddRaw) {
        this.caddRaw = caddRaw;
        return this;
    }

    public double getCaddScaled() {
        return caddScaled;
    }

    public VariantSearchModel setCaddScaled(double caddScaled) {
        this.caddScaled = caddScaled;
        return this;
    }

    public double getSift() {
        return sift;
    }

    public VariantSearchModel setSift(double sift) {
        this.sift = sift;
        return this;
    }

    public double getPolyphen() {
        return polyphen;
    }

    public VariantSearchModel setPolyphen(double polyphen) {
        this.polyphen = polyphen;
        return this;
    }

    public List<String> getGenes() {
        return genes;
    }

    public VariantSearchModel setGenes(List<String> genes) {
        this.genes = genes;
        return this;
    }

    public Set<Integer> getSoAcc() {
        return soAcc;
    }

    public VariantSearchModel setSoAcc(Set<Integer> soAcc) {
        this.soAcc = soAcc;
        return this;
    }

    public Set<String> getGeneToSoAcc() {
        return geneToSoAcc;
    }

    public VariantSearchModel setGeneToSoAcc(Set<String> geneToSoAcc) {
        this.geneToSoAcc = geneToSoAcc;
        return this;
    }

    public Set<String> getTraits() {
        return traits;
    }

    public VariantSearchModel setTraits(Set<String> traits) {
        this.traits = traits;
        return this;
    }

    public Map<String, Float> getStats() {
        return stats;
    }

    public VariantSearchModel setStats(Map<String, Float> stats) {
        this.stats = stats;
        return this;
    }

    public Map<String, Float> getPopFreq() {
        return popFreq;
    }

    public VariantSearchModel setPopFreq(Map<String, Float> popFreq) {
        this.popFreq = popFreq;
        return this;
    }
}
