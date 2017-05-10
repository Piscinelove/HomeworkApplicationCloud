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
        name = "homeworkApi",
        version = "v1",
        resource = "homework",
        namespace = @ApiNamespace(
                ownerDomain = "backend.homeworkapp.audreycelia.example.com",
                ownerName = "backend.homeworkapp.audreycelia.example.com",
                packagePath = ""
        )
)
public class HomeworkEndpoint {

    private static final Logger logger = Logger.getLogger(HomeworkEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(Homework.class);
    }

    /**
     * Returns the {@link Homework} with the corresponding ID.
     *
     * @param homeworkId the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Homework} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "homework/{homeworkId}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Homework get(@Named("homeworkId") long homeworkId) throws NotFoundException {
        logger.info("Getting Homework with ID: " + homeworkId);
        Homework homework = ofy().load().type(Homework.class).id(homeworkId).now();
        if (homework == null) {
            throw new NotFoundException("Could not find Homework with ID: " + homeworkId);
        }
        return homework;
    }

    /**
     * Inserts a new {@code Homework}.
     */
    @ApiMethod(
            name = "insert",
            path = "homework",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Homework insert(Homework homework) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that homework.homeworkId has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(homework).now();
        logger.info("Created Homework with ID: " + homework.getHomeworkId());

        return ofy().load().entity(homework).now();
    }

    /**
     * Updates an existing {@code Homework}.
     *
     * @param homeworkId the ID of the entity to be updated
     * @param homework   the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code homeworkId} does not correspond to an existing
     *                           {@code Homework}
     */
    @ApiMethod(
            name = "update",
            path = "homework/{homeworkId}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Homework update(@Named("homeworkId") long homeworkId, Homework homework) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(homeworkId);
        ofy().save().entity(homework).now();
        logger.info("Updated Homework: " + homework);
        return ofy().load().entity(homework).now();
    }

    /**
     * Deletes the specified {@code Homework}.
     *
     * @param homeworkId the ID of the entity to delete
     * @throws NotFoundException if the {@code homeworkId} does not correspond to an existing
     *                           {@code Homework}
     */
    @ApiMethod(
            name = "remove",
            path = "homework/{homeworkId}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("homeworkId") long homeworkId) throws NotFoundException {
        checkExists(homeworkId);
        ofy().delete().type(Homework.class).id(homeworkId).now();
        logger.info("Deleted Homework with ID: " + homeworkId);
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
            path = "homework",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Homework> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<Homework> query = ofy().load().type(Homework.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<Homework> queryIterator = query.iterator();
        List<Homework> homeworkList = new ArrayList<Homework>(limit);
        while (queryIterator.hasNext()) {
            homeworkList.add(queryIterator.next());
        }
        return CollectionResponse.<Homework>builder().setItems(homeworkList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(long homeworkId) throws NotFoundException {
        try {
            ofy().load().type(Homework.class).id(homeworkId).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Homework with ID: " + homeworkId);
        }
    }
}