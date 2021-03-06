package fr.univtls2.web.moviesearch.services.persistence;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.NoSuchElementException;

import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import fr.univtls2.web.moviesearch.model.Entity;

/**
 * <p>Generic implementation of a Data Access Object.</p>
 * <p>It consists of 4 main methods :</p>
 * <ul>
 * 		<li>Create / Update</li>
 * 		<li>Delete</li>
 * 		<li>Read one object</li>
 * 		<li>Read a set of objects</li>
 * </ul>
 *
 * @author Vianney Dupoy de Guitard
 * @param <T>
 */
public abstract class GenericDao<T extends Entity> {

	/** The database connection to use. */
	@Inject
	private DatabaseConnection co;

	/** The JSON serializer/deserializer, customized for ObjectId serialization. */
	@Inject
	private Gson gson;

	/** The collection to use.*/
	private DBCollection col;

	/** The type of the objects to manipulate. */
	private final Class<T> type;

	/**
	 * Constructor, needs the type of the manipulated entities.
	 * @param pType : the type.
	 */
	public GenericDao(Class<T> pType) {
		type = pType;
	}

	/**
	 * Inserts or updates the entity.
	 * @param entity : the entity to insert/update.
	 */
	public void saveOrUpdate(T entity) {
		entity.generateIdIfNew();
		DBObject dbObject = (DBObject) JSON.parse(gson.toJson(entity));
		getCollection().save(dbObject);
	}

	/**
	 * Inserts a list of entities to the collection.
	 * Contains optimizations for large inserts or updates.
	 *
	 * @param entities : the entities to upsert.
	 */
	public void saveOrUpdate(Collection<T> entities) {
		if (entities.isEmpty()) {
			return;
		}

		BulkWriteOperation bulk = getCollection().initializeUnorderedBulkOperation();

		for (T entity : entities) {
			entity.generateIdIfNew();
			DBObject dbEntity = (DBObject) JSON.parse(gson.toJson(entity));
			bulk.find(new BasicDBObject("_id", entity.getId())).upsert().update(new BasicDBObject("$set", dbEntity));
		}
		bulk.execute();
	}

	/**
	 * Removes from the collection the entity.
	 * Contains optimizations for large deletes.
	 *
	 * @param entity : the entity to delete.
	 */
	public void delete(T entity) {
		DBObject dbEntity = (DBObject) JSON.parse(gson.toJson(entity));
		getCollection().remove(dbEntity);
	}

	/**
	 * Removes from the collection a list of entities.
	 * @param entities : the entities to remove.
	 */
	public void delete(Collection<T> entities) {
		ObjectId[] ids = new ObjectId[entities.size()];
		int index = 0;
		for (T entity : entities) {
			ids[index] = entity.getId();
			index++;
		}
		getCollection().remove(new BasicDBObject("_id", new BasicDBObject("$in", ids)));
	}

	/**
	 * <p>Searches for a set of entities, corresponding to criteria.</p>
	 * <p>Example:</p>
	 * <pre>
	 * {@code 
	 * Term term = new TermBuilder().word("test").create();
	 * dao.find(term); // will look for the term of the word "test".
	 * }
	 * </pre>
	 *
	 * @param criteria : the criteria.
	 * @return the set of entities corresponding to the criteria.
	 *
	 * @throws NoResultFoundException if no entity is matching the criteria.
	 */
	public List<T> find(T criteria) throws NoSuchElementException {
		try {
			List<T> results = new ArrayList<T>();
			DBObject query = (DBObject) JSON.parse(gson.toJson(criteria));
			DBCursor cursor = getCollection().find(query);
			for (int i = 0; i < cursor.count(); i++) {
				DBObject result = cursor.next();
				results.add(gson.fromJson(result.toString(), type));
			}
			return results;
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException();
		}
	}

	/**
	 * Returns a cursor to browse.
	 * @return a cursor to browse all the collection.
	 */
	public DBTypedCursor<T> findAll() {
		DBCursor dbCursor = getCollection().find();
		return new DBTypedCursor<>(type, dbCursor);
	}

	/**
	 * <p>Searches for only one entity, correspond to the criteria.</p>
	 * <p>Example:</p>
	 * <pre>{@code
	 * Term term = new TermBuilder().word("Test").create();
	 * dao.findOne(term); // will look for a term with the word 'test'.
	 * }</pre>
	 *
	 * @param criteria : the criteria.
	 * @return one result corresponding to the criteria.
	 * @throws NoResultFoundException if no entity is matching the criteria.
	 */
	public T findOne(T criteria) throws NoSuchElementException {
		try {
			DBObject query = (DBObject) JSON.parse(gson.toJson(criteria));
			DBObject result = getCollection().find(query).next();
			if (result == null) {
				return null;
			}
			return gson.fromJson(result.toString(), type);
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException();
		}
	}

	/**
	 * <p>Searches for all entities that match one field.</p>
	 * <p>For instance, it can look for a list of terms that matches a list of words:</p>
	 * <pre>{@code
	 * dao.findByField(wordList, 'word'); // will look for all terms with a word in the word list.}
	 *
	 * @param values : values to look for.
	 * @param field : the field associated to the values.
	 * @return all the matching results.
	 */
	protected Deque<T> findByField(Iterable<String> values, String field) {
		Deque<T> result = new ArrayDeque<>();
		DBCursor cursor = getCollection().find(new BasicDBObject(field, new BasicDBObject("$in", values)));
		for (DBObject dbObj : cursor) {
			result.add(getGson().fromJson(dbObj.toString(), type));
		}
		return result;
	}

	/**
	 * @return the number of entities present.
	 */
	public int countAll() {
		return getCollection().find().count();
	}

	/**
	 * @return the collection to use.
	 */
	protected DBCollection getCollection() {
		if (col == null) {
			col = co.getDb().getCollection(getCollectionName());
		}
		return col;
	}

	/**
	 * @return the collection name for these entities.
	 */
	protected abstract String getCollectionName();

	/**
	 * @return the gson serializer.
	 */
	protected Gson getGson() {
		return gson;
	}
}
