/*
* Copyright 2015-2020 OpenCB
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.opencb.opencga.client.rest;

import org.opencb.commons.datastore.core.ObjectMap;
import org.opencb.opencga.client.config.ClientConfiguration;
import org.opencb.opencga.client.exceptions.ClientException;
import org.opencb.opencga.core.models.job.Job;
import org.opencb.opencga.core.models.operations.variant.*;
import org.opencb.opencga.core.response.RestResponse;


/**
 * This class contains methods for the Operation webservices.
 *    Client version: 2.0.0
 *    PATH: operation
 */
public class OperationClient extends AbstractParentClient {

    public OperationClient(String token, ClientConfiguration configuration) {
        super(token, configuration);
    }

    /**
     * Create and load variant annotations into the database.
     * @param data Variant annotation index params.
     * @param params Map containing any additional optional parameters.
     * @return a RestResponse object.
     * @throws ClientException ClientException if there is any server error.
     */
    public RestResponse<Job> indexVariantAnnotation(VariantAnnotationIndexParams data, ObjectMap params) throws ClientException {
        params = params != null ? params : new ObjectMap();
        params.put("body", data);
        return execute("operation", null, "variant/annotation", null, "index", params, POST, Job.class);
    }

    /**
     * Find variants where not all the samples are present, and fill the empty values, excluding HOM-REF (0/0) values.
     * @param data Variant aggregate params.
     * @param params Map containing any additional optional parameters.
     * @return a RestResponse object.
     * @throws ClientException ClientException if there is any server error.
     */
    public RestResponse<ObjectMap> aggregateVariant(VariantAggregateParams data, ObjectMap params) throws ClientException {
        params = params != null ? params : new ObjectMap();
        params.put("body", data);
        return execute("operation", null, "variant", null, "aggregate", params, POST, ObjectMap.class);
    }

    /**
     * Index a variant score in the database.
     * @param data Variant score index params. scoreName: Unique name of the score within the study. cohort1: Cohort used to compute the
     *     score. Use the cohort 'ALL' if all samples from the study where used to compute the score. cohort2: Second cohort used to
     *     compute the score, typically to compare against the first cohort. If only one cohort was used to compute the score, leave empty.
     *     inputColumns: Indicate which columns to load from the input file. Provide the column position (starting in 0) for the column
     *     with the score with 'SCORE=n'. Optionally, the PValue column with 'PVALUE=n'. The, to indicate the variant associated with the
     *     score, provide either the columns ['CHROM', 'POS', 'REF', 'ALT'], or the column 'VAR' containing a variant representation with
     *     format 'chr:start:ref:alt'. e.g. 'CHROM=0,POS=1,REF=3,ALT=4,SCORE=5,PVALUE=6' or 'VAR=0,SCORE=1,PVALUE=2'. resume: Resume a
     *     previously failed indexation.
     * @param params Map containing any additional optional parameters.
     * @return a RestResponse object.
     * @throws ClientException ClientException if there is any server error.
     */
    public RestResponse<ObjectMap> indexVariantScore(VariantScoreIndexParams data, ObjectMap params) throws ClientException {
        params = params != null ? params : new ObjectMap();
        params.put("body", data);
        return execute("operation", null, "variant/score", null, "index", params, POST, ObjectMap.class);
    }

    /**
     * Find variants where not all the samples are present, and fill the empty values.
     * @param data Variant aggregate family params.
     * @param params Map containing any additional optional parameters.
     * @return a RestResponse object.
     * @throws ClientException ClientException if there is any server error.
     */
    public RestResponse<ObjectMap> aggregateVariantFamily(VariantAggregateFamilyParams data, ObjectMap params) throws ClientException {
        params = params != null ? params : new ObjectMap();
        params.put("body", data);
        return execute("operation", null, "variant/family", null, "aggregate", params, POST, ObjectMap.class);
    }

    /**
     * Creates a secondary index using a search engine. If samples are provided, sample data will be added to the secondary index.
     * @param data Variant secondary index params.
     * @param params Map containing any additional optional parameters.
     * @return a RestResponse object.
     * @throws ClientException ClientException if there is any server error.
     */
    public RestResponse<ObjectMap> secondaryIndexVariant(VariantSecondaryIndexParams data, ObjectMap params) throws ClientException {
        params = params != null ? params : new ObjectMap();
        params.put("body", data);
        return execute("operation", null, "variant", null, "secondaryIndex", params, POST, ObjectMap.class);
    }

    /**
     * Remove a secondary index from the search engine for a specific set of samples.
     * @param params Map containing any additional optional parameters.
     * @return a RestResponse object.
     * @throws ClientException ClientException if there is any server error.
     */
    public RestResponse<ObjectMap> deleteVariantSecondaryIndex(ObjectMap params) throws ClientException {
        params = params != null ? params : new ObjectMap();
        return execute("operation", null, "variant/secondaryIndex", null, "delete", params, DELETE, ObjectMap.class);
    }

    /**
     * Build and annotate the sample index.
     * @param data Variant sample index params.
     * @param params Map containing any additional optional parameters.
     * @return a RestResponse object.
     * @throws ClientException ClientException if there is any server error.
     */
    public RestResponse<Job> indexSampleGenotype(VariantSampleIndexParams data, ObjectMap params) throws ClientException {
        params = params != null ? params : new ObjectMap();
        params.put("body", data);
        return execute("operation", null, "variant/sample/genotype", null, "index", params, POST, Job.class);
    }

    /**
     * Save a copy of the current variant annotation at the database.
     * @param data Variant annotation save params.
     * @param params Map containing any additional optional parameters.
     * @return a RestResponse object.
     * @throws ClientException ClientException if there is any server error.
     */
    public RestResponse<ObjectMap> saveVariantAnnotation(VariantAnnotationSaveParams data, ObjectMap params) throws ClientException {
        params = params != null ? params : new ObjectMap();
        params.put("body", data);
        return execute("operation", null, "variant/annotation", null, "save", params, POST, ObjectMap.class);
    }

    /**
     * Build the family index.
     * @param data Variant family index params.
     * @param params Map containing any additional optional parameters.
     * @return a RestResponse object.
     * @throws ClientException ClientException if there is any server error.
     */
    public RestResponse<Job> indexFamilyGenotype(VariantFamilyIndexParams data, ObjectMap params) throws ClientException {
        params = params != null ? params : new ObjectMap();
        params.put("body", data);
        return execute("operation", null, "variant/family/genotype", null, "index", params, POST, Job.class);
    }

    /**
     * Deletes a saved copy of variant annotation.
     * @param params Map containing any additional optional parameters.
     * @return a RestResponse object.
     * @throws ClientException ClientException if there is any server error.
     */
    public RestResponse<ObjectMap> deleteVariantAnnotation(ObjectMap params) throws ClientException {
        params = params != null ? params : new ObjectMap();
        return execute("operation", null, "variant/annotation", null, "delete", params, DELETE, ObjectMap.class);
    }

    /**
     * Remove a variant score in the database.
     * @param params Map containing any additional optional parameters.
     * @return a RestResponse object.
     * @throws ClientException ClientException if there is any server error.
     */
    public RestResponse<ObjectMap> deleteVariantScore(ObjectMap params) throws ClientException {
        params = params != null ? params : new ObjectMap();
        return execute("operation", null, "variant/score", null, "delete", params, DELETE, ObjectMap.class);
    }
}
