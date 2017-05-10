package com.example.audreycelia.homeworkapp.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "examApi",
        version = "v1",
        resource = "exam",
        namespace = @ApiNamespace(
                ownerDomain = "backend.homeworkapp.audreycelia.example.com",
                ownerName = "backend.homeworkapp.audreycelia.example.com",
                packagePath = ""
        )
)
public class ExamEndpoint {

    private static final Logger logger = Logger.getLogger(ExamEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(Exam.class);
    }

    /**
     * Returns the {@link Exam} with the corresponding ID.
     *
     * @param examId the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Exam} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "exam/{examId}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Exam get(@Named("examId") long examId) throws NotFoundException {
        logger.info("Getting Exam with ID: " + examId);
        Exam exam = ofy().load().type(Exam.class).id(examId).now();
        if (exam == null) {
            throw new NotFoundException("Could not find Exam with ID: " + examId);
        }
        return exam;
    }

    /**
     * Inserts a new {@code Exam}.
     */
    @ApiMethod(
            name = "insert",
            path = "exam",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Exam insert(Exam exam) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that exam.examId has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(exam).now();
        logger.info("Created Exam with ID: " + exam.getExamId());

        return ofy().load().entity(exam).now();
    }

    /**
     * Updates an existing {@code Exam}.
     *
     * @param examId the ID of the entity to be updated
     * @param exam   the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code examId} does not correspond to an existing
     *                           {@code Exam}
     */
    @ApiMethod(
            name = "update",
            path = "exam/{examId}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Exam update(@Named("examId") long examId, Exam exam) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(examId);
        ofy().save().entity(exam).now();
        logger.info("Updated Exam: " + exam);
        return ofy().load().entity(exam).now();
    }

    /**
     * Deletes the specified {@code Exam}.
     *
     * @param examId the ID of the entity to delete
     * @throws NotFoundException if the {@code examId} does not correspond to an existing
     *                           {@code Exam}
     */
    @ApiMethod(
            name = "remove",
            path = "exam/{examId}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("examId") long examId) throws NotFoundException {
        checkExists(examId);
        ofy().delete().type(Exam.class).id(examId).now();
        logger.info("Deleted Exam with ID: " + examId);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "list",
            path = "exam",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Exam> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<Exam> query = ofy().load().type(Exam.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<Exam> queryIterator = query.iterator();
        List<Exam> examList = new ArrayList<Exam>(limit);
        while (queryIterator.hasNext()) {
            examList.add(queryIterator.next());
        }
        return CollectionResponse.<Exam>builder().setItems(examList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(long examId) throws NotFoundException {
        try {
            ofy().load().type(Exam.class).id(examId).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Exam with ID: " + examId);
        }
    }
}